package com.vpi.springboot.Logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.*;
import com.vpi.springboot.Repositorios.AdministradorRepositorio;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.exception.AdministradorException;
import com.vpi.springboot.exception.UsuarioException;

@Service
public class AdministradorService implements AdministradorServicioInterfaz {

	@Autowired
	private AdministradorRepositorio repo;
	
	@Autowired
	private ClienteRepositorio clienteRepo;
	
	@Autowired
	private RestauranteRepositorio restauranteRepo;
	
	@Override
	public void crearAdministrador(Administrador admin) throws AdministradorException{
		Optional<Administrador> optionalUser = repo.findById(admin.getMail());
		if(optionalUser.isPresent()) {
			throw new AdministradorException(AdministradorException.AdministradorYaExiste());
		} else {
			String mail = admin.getMail();
			if(mail.contains("@") && mail.contains(".com"))
				repo.save(admin);
			else
				throw new AdministradorException("Tiene que introducir un mail válido.");
		}
	}
	
	@Override
	public void eliminarUsuario(String mail) {
		Optional<Cliente> cliente = clienteRepo.findById(mail);
		cliente.get().setActivo(false);
		clienteRepo.save(cliente.get());
	}
	
	@Override
	public void bloquearUsuario(String mail, String clienteRestaurante) {
		if (clienteRestaurante.equals("Cliente")) {
			Optional<Cliente> cliente = clienteRepo.findById(mail);
			cliente.get().setBloqueado(true);
			clienteRepo.save(cliente.get());
		} else if (clienteRestaurante.equals("Restaurante")) {
			Optional<Restaurante> restaurante = restauranteRepo.findById(mail);
			restaurante.get().setBloqueado(true);
			restauranteRepo.save(restaurante.get());
		}
	}
	
	@Override
	public void desbloquearUsuario(String mail, String clienteRestaurante) {
		if (clienteRestaurante.equals("Cliente")) {
			Optional<Cliente> cliente = clienteRepo.findById(mail);
			cliente.get().setBloqueado(false);
			clienteRepo.save(cliente.get());
		} else if (clienteRestaurante.equals("Restaurante")) {
			Optional<Restaurante> restaurante = restauranteRepo.findById(mail);
			restaurante.get().setBloqueado(false);
			restauranteRepo.save(restaurante.get());
		}
	}
}
