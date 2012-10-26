package ulcambridge.foundations.viewer.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.mysql.jdbc.PreparedStatement;

public class UsersDBDao implements UsersDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public User getActiveUserByUsername(String username) {

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
									resultSet.getString("password"), true, roles);
						}
					});

		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public void createOpenIdUser(String username) {
		User user = new User (username, "", true, Arrays.asList("ROLE_USER"));
		add(user);
	}
	
	private void add(final User user) {
		
		String usersSQL = "INSERT into users (username, password, enabled) values (?, ?, ?)";
		jdbcTemplate.update(usersSQL, new Object[] {user.getUsername(), user.getPassword(), user.isEnabled()});
				
		String authSQL = "INSERT into authorities (username, authority) values (?, ?)";
		jdbcTemplate.batchUpdate(authSQL, 
	    new BatchPreparedStatementSetter() {
           
			@Override
			public void setValues(java.sql.PreparedStatement ps, int i)
					throws SQLException {
				
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getUserRoles().get(i));
				
			}

			@Override
			public int getBatchSize() {
				return user.getUserRoles().size();
			}
    });
	
	}


}
