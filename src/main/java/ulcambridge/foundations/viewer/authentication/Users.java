package ulcambridge.foundations.viewer.authentication;

import org.springframework.security.core.context.SecurityContextHolder;

public class Users {

    public static AdminUser currentAdminUser(UsersDao usersDao) {
        String name = SecurityContextHolder.getContext()
            .getAuthentication().getName();

        // FIXME: can return null, need to improve error handling in DAOs
        return usersDao.getAdminUserByUsername(name);
    }

    private Users() {
        throw new RuntimeException();
    }
}
