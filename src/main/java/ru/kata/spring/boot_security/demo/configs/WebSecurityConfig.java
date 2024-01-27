package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kata.spring.boot_security.demo.services.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private final UserService userService;

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserService userService) {
        this.successUserHandler = successUserHandler;
        this.userService = userService;
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return userService;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()  // Отключение CSRF защиты
                .authorizeRequests()  // Настройка правил авторизации
                .antMatchers("/", "/login").hasAnyRole("ADMIN", "USER")  // Разрешение доступа без аутентификации
                .antMatchers("/user", "/logout").hasRole("USER")  // для доступа к этим путям должна быть роль USER
                .antMatchers("/admin", "/create", "/edit", "/delete", "/user").hasRole("ADMIN")  // для доступа к этим путям должна быть роль ADMIN
                .and()  // Завершение настройки для authorizeRequests
                .formLogin()  // Включение формы для аутентификации
                .successHandler(successUserHandler).permitAll()  // Установка пользовательского обработчика успешной аутентификации для всех пользователей
                .and()  // Завершение настройки для formLogin
                .logout()  // Включение поддержки выхода из системы
                .logoutUrl("/logout").permitAll()  // Указание URL для выполнения выхода с доступом для всех пользователей
                .logoutSuccessUrl("/login");  // Установка URL для переадресации после успешного выхода
    }
}