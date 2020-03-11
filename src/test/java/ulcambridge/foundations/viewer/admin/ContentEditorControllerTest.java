package ulcambridge.foundations.viewer.admin;

import com.google.common.collect.Range;
import com.google.common.truth.Truth;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ulcambridge.foundations.viewer.testing.BaseCUDLApplicationContextTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.google.common.truth.Truth.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

public class ContentEditorControllerTest extends BaseCUDLApplicationContextTest {
    @Autowired
    private MockMvc mockMvc;

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @CsvSource({
        "POST,update/html",
        "POST,add/image",
        "GET,browse/images?CKEditor=&CKEditorFuncNum=&langCode=",
        "POST,delete/image"
    })
    public @interface ProtectedControllerMethodsSource {}

    @ParameterizedTest
    @ProtectedControllerMethodsSource
    public void endpointsRequireCredentials(String method, String path) throws Exception {
        mockMvc.perform(request(HttpMethod.valueOf(method), "/editor/" + path))
            .andDo(result -> assertThat(result.getResolvedException()).isInstanceOf(AuthenticationCredentialsNotFoundException.class));
    }

    @ParameterizedTest
    @ProtectedControllerMethodsSource
    @WithMockUser
    public void usersWithoutAdminRoleAreDenied(String method, String path) throws Exception {
        mockMvc.perform(request(HttpMethod.valueOf(method), "/editor/" + path))
            .andDo(result -> assertThat(result.getResolvedException()).isInstanceOf(AccessDeniedException.class));
    }

    @ParameterizedTest
    @ProtectedControllerMethodsSource
    @WithMockUser(roles={"ADMIN"})
    public void usersWithAdminRoleAreAllowed(String method, String path) throws Exception {
        mockMvc.perform(request(HttpMethod.valueOf(method), "/editor/" + path))
            .andDo(result -> {
                // The controller implements all the logic directly, so we can't really avoid actions happening by
                // mocking domain objects. What should happen is not really clear as a result...
                if(result.getResolvedException() != null) {
                    Truth.assertThat(result.getResolvedException()).isNotInstanceOf(AccessDeniedException.class);
                    Truth.assertThat(result.getResolvedException()).isNotInstanceOf(AuthenticationException.class);
                } else {
                    Truth.assertThat(result.getResponse().getStatus()).isIn(Range.closedOpen(200, 300));
                }
            });
    }
}
