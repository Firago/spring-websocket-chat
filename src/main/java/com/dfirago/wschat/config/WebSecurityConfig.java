package com.dfirago.wschat.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author dmfi
 */
@Configuration
@EnableWebSecurity
@EnableRedisHttpSession
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin()
                .loginPage("/index.html")
                .defaultSuccessUrl("/chat.html")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/index.html")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/js/**", "/lib/**", "/img/**", "/css/**", "/index.html", "/").permitAll()
                .antMatchers("/ws").denyAll()
                .anyRequest().authenticated();
                
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new AuthenticationProvider() {

            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
                List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("USER");
                return new UsernamePasswordAuthenticationToken(token.getName(), token.getCredentials(), authorities);
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }
        });
    }

}
