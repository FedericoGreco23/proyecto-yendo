package com.vpi.springboot.Logica;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class MyUserDetalilsService implements UserDetailsService{

	/**
	 * este metodo serà llamado por spring para cargar un usuario por su nombre de usuario
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//aca hay que buscar al usuario en la DB
		//para probar se usa uno fijo
		return new User("grupo1", "grupo1", new ArrayList<>());
	}

}
