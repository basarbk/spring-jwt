package com.example.article.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  TokenFilter tokenFilter;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http
      .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/api/1.0/articles").authenticated()
      .and()
      .authorizeRequests().anyRequest().permitAll();

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
  }

}
