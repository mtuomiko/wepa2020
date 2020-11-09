package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Profile("production")
@Configuration
@EnableWebSecurity
public class ProductionSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // poistetaan csrf-tarkistus käytöstä h2-konsolin vuoksi
        //http.csrf().disable();
        // sallitaan framejen käyttö
        //http.headers().frameOptions().sameOrigin();

//        http.authorizeRequests()
//                .antMatchers("/accounts", "/accounts/**", "/h2-console", "/h2-console/**",
//                        "/register").permitAll()
//                .anyRequest().authenticated().and()
//                .formLogin().loginPage("/login").permitAll().and()
//                .logout().permitAll();
        http.authorizeRequests()
                .antMatchers("/posts").authenticated()
                .antMatchers("/**").permitAll().and()
                .formLogin().loginPage("/login").permitAll().and()
                .logout().permitAll();
    }

    @Override
    public void configure(WebSecurity sec) throws Exception {
        sec.ignoring().antMatchers("/css/**");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
