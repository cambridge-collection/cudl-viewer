package ulcambridge.foundations.viewer.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UsersDBDao implements UsersDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public User getByUsername(String username) {

		String authQuery = "SELECT authority FROM authorities where username = ?";

		final List<String> roles = (List<String>) jdbcTemplate.query(authQuery,
				new Object[] { username }, new RowMapper<String>() {
					public String mapRow(ResultSet resultSet, int rowNum)
							throws SQLException {
						return resultSet.getString("authority");
					}
				});

		String query = "SELECT username, password FROM users where username = ? and enabled = true";

		try {
			return (User) jdbcTemplate.queryForObject(query,
					new Object[] { username }, new RowMapper<User>() {
						public User mapRow(ResultSet resultSet, int rowNum)
								throws SQLException {
							return new User(resultSet.getString("username"),
									resultSet.getString("password"), roles);
						}
					});

		} catch (Exception e) {
			return null;
		}

	}

	public void createNewUser(String username) {
		
		String usersSQL = "INSERT into users (username, password, enabled) values (?, ?, ?)";
		jdbcTemplate.update(usersSQL, new Object[] {username, "", true});
		
		String authSQL = "INSERT into authorities (username, authority) values (?, ?)";
		jdbcTemplate.update(authSQL, new Object[] {username, "ROLE_USER"});
	
	}
}
