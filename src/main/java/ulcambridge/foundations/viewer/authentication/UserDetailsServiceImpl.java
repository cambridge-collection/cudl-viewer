package ulcambridge.foundations.viewer.authentication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * The data access object for users.
     */
    private UsersDao userDao;

    /**
     * Constructor.
     *
     * @param userDao the data access object for users to set
     */
    @Autowired(required = true)
    public void setUsersDao(UsersDao userDao) {
        this.userDao = userDao;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    //@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException,
            DataAccessException {
    	
        User user = userDao.getByUsername(username);

        if (user == null) {
            //throw new UsernameNotFoundException("User not found: " + username);
        	userDao.createNewUser(username);
        	user = userDao.getByUsername(username);
        } 
        
        return makeUser(user);
        
    }

    /**
     * Makes user.
     *
     * @param user User
     *
     * @return Granted user
     */
    private org.springframework.security.core.userdetails.User makeUser(final User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                makeGrantedAuthorities(user));
    }

    /**
     * Returns granted authorities.
     *
     * @param user User
     *
     * @return granted authorities collection
     */
    private List<GrantedAuthority> makeGrantedAuthorities(final User user) {
        final List<GrantedAuthority> result = new ArrayList<GrantedAuthority>();
        
        Iterator<String> roleIt = user.getUserRoles().iterator();
        while (roleIt.hasNext()) {
        	String role = roleIt.next();
        	result.add(new GrantedAuthorityImpl(role));
        }
        
        return result;
    }
}

