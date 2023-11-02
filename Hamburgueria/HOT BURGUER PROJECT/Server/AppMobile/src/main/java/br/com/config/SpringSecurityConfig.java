package br.com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSocket
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/Address/**").authenticated()
                .antMatchers("/Czc/**").authenticated()
                .antMatchers("/Image/**").authenticated()
                .antMatchers("/Order/**").authenticated()
                //.antMatchers("/Payment/**").authenticated()
                .antMatchers("/User/**").authenticated()
                //.antMatchers("/campaings/**").authenticated()
                .antMatchers("/Coupons/**").authenticated()
                .antMatchers("/ShoppingList/**").authenticated()
                .antMatchers("/hot-address/**").authenticated()
                .antMatchers("/hot-deliveryman/**").authenticated()
                .antMatchers("/hot-order/**").authenticated()
                .antMatchers("/hot-payment/**").authenticated()
                .antMatchers("/hot-products/**").authenticated()
                .antMatchers("/system/**").authenticated()
                //.antMatchers("/hot-products/**").authenticated()
                .antMatchers("/system/**").authenticated()
                .antMatchers("/hot-users/**").authenticated()
                .antMatchers("/spy/**").hasAnyRole("ADMIN")
                .antMatchers("/Collaborator/**").hasAnyRole("ADMIN","COLL")
                .antMatchers("/serverconfig/**").hasAnyRole("ADMIN","COLL")
                .and()
                .httpBasic()
                .and().csrf().disable();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                /*.withUser("CzcUser").password(passwordEncoder().encode("M@7832@RloN")).roles("USER")
                .and()
                .withUser("CzcAdmin").password(passwordEncoder().encode("M@Rlon7647wf5h")).roles("USER","ADMIN")
                .and()
                .withUser("CzcColl").password(passwordEncoder().encode("M@rlOn85iwh222hs")).roles("USER","COLL")
                .and()
                .withUser("Spy").password(passwordEncoder().encode("M@458844eee##")).roles("ADMIN")
                .and()*/
                .withUser("Hot.Burguer").password(passwordEncoder().encode("d44a153913397cfcb81912299dfeb9a9")).roles("USER")
                .and()
                .withUser("Hot.Burguer.Manager").password(passwordEncoder().encode("EYxuvx0UPHacHlotZAFEGJ8g0H3ik7bs")).roles("ADMIN");
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
