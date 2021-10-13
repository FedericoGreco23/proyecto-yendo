package com.vpi.springboot.Logica;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.Administrador;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.Usuario;
import com.vpi.springboot.Repositorios.AdministradorRepositorio;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.exception.UsuarioException;
import com.vpi.springboot.security.util.MyDetails;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private ClienteRepositorio clienteRepo;
	@Autowired
	private RestauranteRepositorio resRepo;
	@Autowired
	private AdministradorRepositorio adminRepo;
	
	
	/**
	 * este metodo serà llamado por spring para cargar un usuario por su nombre de
	 * usuario
	 */
	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
		// aca hay que buscar al usuario en la DB
		// para probar se usa uno fijo
		Usuario usuario = new Usuario();

		Optional<Cliente> optionalCliente = clienteRepo.findById(mail);
		Optional<Restaurante> optionalRestaurante = resRepo.findById(mail);
		Optional<Administrador> optionalAdmin = adminRepo.findById(mail);

		if (optionalCliente.isPresent()) { // cliente
			usuario = optionalCliente.get();
		} else if (optionalRestaurante.isPresent()) { // restaurante
			usuario = optionalRestaurante.get();

		} else if (optionalAdmin.isPresent()) { // administrador
			usuario = optionalAdmin.get();
		}
		
		return new MyDetails(usuario);
	}

}
