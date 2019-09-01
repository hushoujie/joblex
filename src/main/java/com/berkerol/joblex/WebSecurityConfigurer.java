package com.berkerol.joblex;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    public WebSecurityConfigurer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/signin**", "/signup**", "/confirm/**", "/jobs", "/job/**").permitAll()
                .antMatchers("/recruiter/**", "/profiles", "/profile/**", "/application/**").hasAuthority("ADMIN")
                .antMatchers("/candidate/**", "/profiles", "/profile/**", "/application/**").hasAuthority("USER")
                .anyRequest().authenticated().and()
                .formLogin().loginPage("/signin").failureForwardUrl("/signin?error").permitAll().and()
                .logout().logoutUrl("/signout").logoutSuccessUrl("/signin?signout").permitAll();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .dataSource(dataSource);
    }
}
