package com.odeyalo.analog.auth.config.security;

import com.odeyalo.analog.auth.config.security.filter.JwtTokenFilter;
import com.odeyalo.analog.auth.config.security.jwt.JwtTokenAuthenticationEntrypoint;
import com.odeyalo.analog.auth.service.oauth2.CustomOauth2UserService;
import com.odeyalo.analog.auth.service.oauth2.DefaultAuthenticationFailureHandler;
import com.odeyalo.analog.auth.service.oauth2.DefaultAuthenticationSuccessHandler;
import com.odeyalo.analog.auth.service.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.odeyalo.analog.auth.service.support.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
public class MicroserviceSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String EUREKA_ADMIN_ENTRYPOINT = "/";
    private static final String EUREKA_ENTRYPOINT = "/eureka/**";
    private static final String FAV_ICON_ENTRYPOINT = "/favicon.ico";
    private static final String AUTH_ENTRYPOINT = "/api/v1/auth/**";
    private static final String QRCODE_GENERATION_ENTRYPOINT = "/api/v1/qrcode/generate";
    private static final String OAUTH2_LOGIN_ENTRYPOINT = "/oauth2/callback/**";
    private static final String REFRESH_TOKEN_ENTRYPOINT = "/api/v1/refreshToken";
    private static final String QR_CODE_WEB_SOCKET_LOGIN_ENTRYPOINT = "/broadcast/**";

    private final JwtTokenFilter jwtTokenFilter;

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOauth2UserService oAuth2UserService;
    private final DefaultAuthenticationSuccessHandler successHandler;
    private final DefaultAuthenticationFailureHandler failureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository;
    public MicroserviceSecurityConfiguration(JwtTokenFilter jwtTokenFilter,
                                             CustomUserDetailsService customUserDetailsService,
                                             CustomOauth2UserService oAuth2UserService,
                                             DefaultAuthenticationSuccessHandler successHandler,
                                             DefaultAuthenticationFailureHandler failureHandler, HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.customUserDetailsService = customUserDetailsService;
        this.oAuth2UserService = oAuth2UserService;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.cookieOAuth2AuthorizationRequestRepository = cookieOAuth2AuthorizationRequestRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                .antMatchers(EUREKA_ADMIN_ENTRYPOINT,
                        EUREKA_ENTRYPOINT,
                        FAV_ICON_ENTRYPOINT).permitAll()
                .antMatchers(AUTH_ENTRYPOINT,
                        OAUTH2_LOGIN_ENTRYPOINT,
                        REFRESH_TOKEN_ENTRYPOINT,
                        QRCODE_GENERATION_ENTRYPOINT,
                        QR_CODE_WEB_SOCKET_LOGIN_ENTRYPOINT).permitAll()
                .anyRequest().authenticated()
                    .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new JwtTokenAuthenticationEntrypoint())
                    .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize/**")
                .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository)
                    .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                    .and()
                .userInfoEndpoint()
                .userService(oAuth2UserService)
                    .and()
                .successHandler(successHandler)
                .failureHandler(failureHandler);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("*/*")
                .antMatchers("/eureka/**")
                .antMatchers(HttpMethod.OPTIONS, "/**");
    }


    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MODERATOR > ROLE_USER");
        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
        return defaultWebSecurityExpressionHandler;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
