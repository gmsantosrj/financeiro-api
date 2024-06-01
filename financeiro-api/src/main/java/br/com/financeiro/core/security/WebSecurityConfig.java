package br.com.financeiro.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author: GILMAR
 * @since: 30 de mai. de 2024
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomBasicAuthFilter customBasicAuthFilter) throws Exception {
		http	
		.authorizeHttpRequests(
				authorize -> {
				    authorize.anyRequest().authenticated();
				})
		
		.sessionManagement(se -> se.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.addFilterBefore(customBasicAuthFilter, BasicAuthenticationFilter.class)
		.csrf(csrf -> csrf.disable());
		return http.build();
	}

}
