package de.astahsrm.gremiomat.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authmanagerbuilder) throws Exception {
        PasswordEncoder pwenc = passwordEncoder();
        authmanagerbuilder.inMemoryAuthentication().withUser("user").password(pwenc.encode("user")).roles("USER").and()
                .withUser("admin").password(pwenc.encode("admin")).roles(USER, ADMIN);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/assets/**", "/css/**", "/favicon/**", "/img/**").permitAll()
            .antMatchers("/gremien/**", "/gremien**").permitAll()
            .antMatchers("/register", "/logout").permitAll()
            .antMatchers(HttpMethod.DELETE).hasRole(ADMIN)
            .antMatchers("/user*", "/user/*").authenticated()
            .anyRequest().hasAnyRole(ADMIN)
                .and()
                    .formLogin()
                    //.loginPage("/login")
                    .defaultSuccessUrl("/userseite")
                    .permitAll()
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .permitAll();
    }
}
