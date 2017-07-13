package main;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/uzman/**").fullyAuthenticated().and()
                .authorizeRequests().anyRequest().permitAll().and()
                .formLogin().loginPage("/giris").defaultSuccessUrl("/uzman/", true).failureUrl("/giris?hata").and()
                .logout().logoutUrl("/cikis").logoutSuccessUrl("/giris?cikis");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=uzmanlar")
                .contextSource(contextSource())
                .passwordCompare()
                .passwordEncoder(new LdapShaPasswordEncoder())
                .passwordAttribute("userPassword");
    }

    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        DefaultSpringSecurityContextSource dsscs = new DefaultSpringSecurityContextSource("ldap://localhost:10389/o=obss");
        dsscs.setUserDn("uid=admin,ou=system");
        dsscs.setPassword("secret");
        return dsscs;
    }

}
