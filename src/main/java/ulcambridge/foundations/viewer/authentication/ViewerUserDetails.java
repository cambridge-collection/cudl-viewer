package ulcambridge.foundations.viewer.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A {@link UserDetails} implementation backed by a CUDL Viewer {@link User}.
 */
public class ViewerUserDetails implements UserDetails {

    private final User viewerUser;

    public ViewerUserDetails(User viewerUser) {
        Assert.notNull(viewerUser);

        this.viewerUser = viewerUser;
    }

    /**
     * @return the CUDL Viewer user backing this object.
     */
    public User getViewerUser() {
        return this.viewerUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.ofNullable(getViewerUser().getUserRoles())
            .map(roles -> roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList()))
            .orElse(Collections.emptyList());
    }

    @Override
    public String getPassword() {
        return getViewerUser().getPassword();
    }

    @Override
    public String getUsername() {
        return getViewerUser().getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getViewerUser().isEnabled();
    }
}
