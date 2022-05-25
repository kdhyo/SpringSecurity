package io.security.corespringsecurity.security.configs;

import io.security.corespringsecurity.security.provider.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final AuthenticationDetailsSource authenticationDetailsSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/", "/users", "user/login/**").permitAll()
			.antMatchers("/mypage").hasRole("USER")
			.antMatchers("/messages").hasRole("MANAGER")
			.antMatchers("/config").hasRole("ADMIN")
			.anyRequest().authenticated()

			.and()
			.formLogin()
			.loginPage("/login")
			.loginProcessingUrl("/login_proc")
			.authenticationDetailsSource(authenticationDetailsSource)
			.defaultSuccessUrl("/")
			.permitAll()
		;
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		return new CustomAuthenticationProvider();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}
