package com.vpi.springboot.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.vpi.springboot.security.filtro.JwtRequestFilter;
import com.vpi.springboot.security.util.JwtAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter{

	@Autowired
	private MyUserDetailsService myUserDetalilsService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	/**
	 * la idea aca es configurar AuthenticationManagerBuilder pasandole el servicio que maneja la autenticacion creado por nosotros
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetalilsService);
		auth.authenticationProvider(authProvider());
	}
	
	/**
	 * para probar
	 * @return
	 
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	*/
	
	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
	
	/**
	 * para que el bean funcione...
	 */
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	
	/**
	 * esto define la seguridad de la aplicacion. los accesos y las redirecciones.
	 * y le dice que use el filtro
	 * Además le dice a Spring que no cree sesion de usuario
	 */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
				.authorizeRequests().antMatchers("/public/*", "/public/**", "/public/login", "/public/crear", "/public/recuperar", "/public/socket", "/public/socket/*", "/public/socket/**", "/public/socket/info", "/public/socket/info/*").permitAll().
						anyRequest().authenticated().and().
						exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().cors();
		
				httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	
		//como Spring no crea la sesion, la siguiente linea permite que para cada request se configure el security context
		//no se guarda una sesion en ningun momento
		//httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

	}
	/*
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

	*/
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        configuration.setAllowedOrigins(Arrays.asList("/**", "http://localhost:8100", "https://localhost:443","http://localhost:4200", "http://localhost:4201", "https://prueba-concepto-frontend.herokuapp.com", "http://190.134.71.243:4200", "https://190.134.71.243:4200"
        		, "http://190.134.71.243:8080", "https://190.134.71.243:8080"));//aca va host 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD", "CONNECT"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        //res.header('Access-Control-Allow-Headers', 'X-CSRF-Token, X-Requested-With, Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, X-Api-Version');
        configuration.setExposedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        Map<String, CorsConfiguration> corsConfigMap= new HashMap<String, CorsConfiguration>();
        corsConfigMap.put("/**", configuration);
        corsConfigMap.put("/public/*", configuration);
        corsConfigMap.put("/public/**", configuration);
        corsConfigMap.put("/public/login", configuration);
        corsConfigMap.put("/public/crear", configuration); 
        corsConfigMap.put("/public/socket", configuration); 
        corsConfigMap.put("/public/socket/*", configuration); 
        corsConfigMap.put("/public/socket/**", configuration); 
        corsConfigMap.put("/public/socket/info", configuration); 
        corsConfigMap.put("/public/socket/info/*", configuration); 
        
        
        
        source.setCorsConfigurations(corsConfigMap);
        return source;
    }
	
	
	@Bean
	public DaoAuthenticationProvider authProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService());
	    authProvider.setPasswordEncoder(encoder());
	    return authProvider;
	}
}
