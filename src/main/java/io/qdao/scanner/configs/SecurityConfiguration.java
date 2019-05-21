package io.qdao.scanner.configs;

import io.qdao.scanner.filters.JWTAuthenticationFilter;
import io.qdao.scanner.handler.JwtAuthenticationSuccessHandler;
import io.qdao.scanner.providers.JwtAuthenticationProvider;
import io.qdao.scanner.registrars.RestAuthenticationEntryPoint;
import io.qdao.scanner.services.UserAuthenticateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Collections;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder() {
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                char minor = encodedPassword.charAt(2);
                String encodePass = encodedPassword;
                if ('a' != minor) {
                    encodePass = encodePass.substring(0, 2) + 'a' + encodePass.substring(3); //Костыль
                }
                return super.matches(rawPassword, encodePass);
            }
        };
    }

    @Bean
    JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider();
    }

    @Bean
    JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
        return new JwtAuthenticationSuccessHandler();
    }

    @Bean
    RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    AuthenticationManager authenticationManager(JwtAuthenticationProvider provider) {
        return new ProviderManager(Collections.singletonList(provider));
    }

    @Configuration
    static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Value("${app.character-encoding:UTF8}")
        private String characterEncoding;

        @Autowired
        private  PasswordEncoder encoder;

        @Autowired
        private UserDetailsService userDetailsService;

        @Autowired
        private UserAuthenticateFactory userAuthenticateFactory;

        @Autowired
        private JwtAuthenticationProvider jwtAuthenticationProvider;

        @Autowired
        private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(encoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .formLogin().loginPage("/login").defaultSuccessUrl("/")
                    .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout")
                    .and()
                    .addFilterAfter(new CharacterEncodingFilter(characterEncoding), BasicAuthenticationFilter.class)
                    .addFilterAfter(new JWTAuthenticationFilter(userAuthenticateFactory, authenticationManager, restAuthenticationEntryPoint), BasicAuthenticationFilter.class);
        }
    }
}
