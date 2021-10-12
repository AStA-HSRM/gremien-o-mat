package de.astahsrm.gremiomat.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    @Autowired
    private MgmtUserDetailsService mgmtUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeRequests().antMatchers("/assets/**", "/css/**", "/favicon/**", "/img/**").permitAll()
            .antMatchers("/gremien/**","/gremien**").permitAll()
            .antMatchers("/api/**","/api**").permitAll()
            .antMatchers("/user/**", "/user**").authenticated()
            .antMatchers("/admin/**", "/admin**").hasAnyRole(ADMIN)

            .and()
            .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/mgmt")
            .loginProcessingUrl("/login")
            .failureUrl("/login?error=true")
            .permitAll()

            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .deleteCookies("JSESSIONID")
            .permitAll()

            .and()
            .rememberMe().key("uniqueAndSecret");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authmanagerbuilder) throws Exception {
        authmanagerbuilder.userDetailsService(mgmtUserDetailsService).passwordEncoder(passwordEncoder());
        
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
