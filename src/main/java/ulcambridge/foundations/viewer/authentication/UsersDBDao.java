package ulcambridge.foundations.viewer.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class UsersDBDao implements UsersDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UsersDBDao(DataSource dataSource) {
        Assert.notNull(dataSource);
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

        String query = "SELECT username, password, email FROM users where username = ? and enabled = true";

        try {
            return (User) jdbcTemplate.queryForObject(query,
                    new Object[] { username }, new RowMapper<User>() {
                        public User mapRow(ResultSet resultSet, int rowNum)
                                throws SQLException {
                            return new User(resultSet.getString("username"),
                                    resultSet.getString("password"),
                                    resultSet.getString("email"), true, roles);
                        }
                    });

        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public User createUser(String username, String email) {

        // See if user exists already
        User user = getActiveUserByUsername(username);

        // user with this username already exists.
        // Note this updates the email address if this has changed.
        if (user!=null) {

            // has the email address changed?
            if (email!=null && !email.equals(user.getEmail())) {
                updateDetails(username, email);
            }

            return user;
        }

        user = new User (username, "", email, true, Arrays.asList("ROLE_USER"));
        add(user);
        return user;
    }

    private void add(final User user) {

        String usersSQL = "INSERT into users (username, password, email, enabled) values (?, ?, ?, ?)";
        jdbcTemplate.update(usersSQL, new Object[] {user.getUsername(), user.getPassword(), user.getEmail(), user.isEnabled()});

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


    /**
     * Updates the email address for this user as they have logged in with a new email address.
     *
     * @param username
     * @param email
     */
    private void updateDetails(String username, String email) {

        String updateSQL = "UPDATE users SET email = ? WHERE username = ?";
        jdbcTemplate.update(updateSQL, new Object[] {email, username});

    }

    @Override
    public AdminUser getAdminUserByUsername(String username) {

        String query = "SELECT username, name, email FROM adminusers where username = ?";

        try {
            return (AdminUser) jdbcTemplate.queryForObject(query,
                    new Object[] { username }, new RowMapper<User>() {
                        public AdminUser mapRow(ResultSet resultSet, int rowNum)
                                throws SQLException {
                            User user = getActiveUserByUsername(resultSet.getString("username"));
                            AdminUser adminUser = new AdminUser(user.getUsername(), user.getPassword(), user.getEmail(), user.isEnabled(), user.getUserRoles());
                            adminUser.setAdminEmail(resultSet.getString("email"));
                            adminUser.setAdminName(resultSet.getString("name"));
                            return adminUser;
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
