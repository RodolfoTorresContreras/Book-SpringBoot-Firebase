
package com.firebase.firebase;

import com.firebase.firebase.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/img/**", "/listar/**", "/formUser/**").permitAll()
                .antMatchers("/ver/**").hasAnyRole("USER")
                .antMatchers("/img/*").hasAnyRole("USER")
                .antMatchers("/form/**").hasAnyRole("USER")
                .antMatchers("/micuenta/**").hasAnyRole("ADMIN")
                .antMatchers("/micuenta/**").hasAnyRole("USER")
                .antMatchers("/listarUser/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login")
                .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/error403");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {

       
        builder.userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder);
    }

}
