package ulcambridge.foundations.viewer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.util.Assert;

import javax.sql.DataSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public WebSecurityConfig(DataSource dataSource) {
        Assert.notNull(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(this.dataSource);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/auth/login")
                .and()
            .logout()
                .logoutUrl("/auth/logout")
                // FIXME: This
                .logoutSuccessUrl("/auth/login?error=Successfully%20logged%20out &amp;access=/mylibrary/")
                .deleteCookies("JSESSIONID")
                .and()
            .authorizeRequests()
                .antMatchers("/admin/**", "/editor/**").hasRole("ADMIN")
                .antMatchers("/mylibrary/**").hasRole("USER")
                .anyRequest().hasAnyRole("ANONYMOUS", "USER", "ADMIN")
                .and()
            .headers()
                .addHeaderWriter(
                    new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
    }
}
