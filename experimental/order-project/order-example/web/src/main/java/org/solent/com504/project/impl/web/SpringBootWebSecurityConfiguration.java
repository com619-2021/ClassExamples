package org.solent.com504.project.impl.web;

import org.solent.com504.project.impl.service.ServiceConfiguration;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

// see https://stackoverflow.com/questions/58883936/can-i-have-multiple-configurations-in-spring-security-for-securing-web-applicati
@Configuration
@EnableWebSecurity
public class SpringBootWebSecurityConfiguration {

    // see https://stackoverflow.com/questions/48453980/spring-5-0-3-requestrejectedexception-the-request-was-rejected-because-the-url
    // avoids errors when behind reverse proxy org.springframework.security.web.firewall.RequestRejectedException: The request was rejected because the URL contained a potentially malicious String "//"
//    @Bean
//    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
//        StrictHttpFirewall firewall = new StrictHttpFirewall();
//        firewall.setAllowUrlEncodedSlash(true);
//        return firewall;
//    }
    
    // this may not be needed now the proxy is working OK
    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }

    // this is probably not needed now proxy is working OK (not working anyway)
    // see https://stackoverflow.com/questions/32577346/spring-security-change-all-instances-of-redirectstrategy
//    @Bean
//    public RedirectStrategy createRedirectStrategy() {
//        // create the redirect strategy to set the urls to context relative
//        DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//        redirectStrategy.setContextRelative(true);
//
//        return redirectStrategy;
//    }

    @Configuration
    @Order(1)
    public static class RestApiSecurityConfig extends WebSecurityConfigurerAdapter {

        //TODO fix security so that we cn make Rest calls with basic authentication
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .antMatcher("/rest/**")
                    .authorizeRequests()
                    .antMatchers("/rest/openapi.json").permitAll()
                    .antMatchers("/rest/**").permitAll() //  .antMatchers("/rest/**").hasAnyRole("REST_USER", "GLOBAL_ADMIN") // ROLE_GLOBAL_ADMIN
                    //  .and().httpBasic();
                    ;

            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }

    @Configuration
    @Order(2)
    public static class LoginFormSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/",
                            "/home",
                            "/contact",
                            "/about",
                            "/index.html",
                            "/resources/**",
                            "/images/**",
                            "/swagger-ui/**",
                            "/registration",
                            "/rest/openapi.json"
                    ).permitAll()
                    .antMatchers("/mvc/**"
                    ).hasRole("USER") // ROLE_USER 
                    .antMatchers("/users").hasRole("GLOBAL_ADMIN") // ROLE_GLOBAL_ADMIN
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/home", true)
                    .permitAll()
                    .and()
                    .logout()
                    .permitAll()
                    .logoutSuccessUrl("/login?logout")
                    .and().csrf().ignoringAntMatchers("/rest/**",
                          "/login"  ); // prevents csrf checking on rest api and login
        }

    }
}
