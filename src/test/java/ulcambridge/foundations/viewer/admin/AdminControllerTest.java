package ulcambridge.foundations.viewer.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import ulcambridge.foundations.viewer.testing.BaseCUDLApplicationContextTest;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AdminControllerTest extends BaseCUDLApplicationContextTest {
    @Autowired
    private AdminController adminController;

    @Test
    public void controllerMethodsAreNotCallableWithoutUserCredentialsPresent() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> adminController.handleAdminRequest());
    }

    @Test
    @WithMockUser()
    public void normalUserCannotAccessControllerMethods() {
        assertThrows(AccessDeniedException.class, () -> adminController.handleAdminRequest());
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void usersWithAdminRoleCanAccessControllerMethods() {
        assertThat(adminController.handleAdminRequest().getViewName()).isEqualTo("jsp/admin");
    }
}
