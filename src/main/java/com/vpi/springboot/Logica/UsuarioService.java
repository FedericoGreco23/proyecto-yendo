package com.vpi.springboot.Logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Repositorios.ClienteRepositorio;

@Service
public class UsuarioService implements UsuarioServicioInterfaz {

	@Autowired
	private ClienteRepositorio userRepo;
	
	
	
	@Override
	public void createTodo(Cliente usuario) {
		Optional<Cliente> optionalUser = userRepo.findById(usuario.getMail());
		if(optionalUser.isPresent()) {
			
		}else {
			userRepo.save(usuario);
		}

	}

	@Override
	public List<Cliente> getAllClientes() {
		Iterable<Cliente> usuario = userRepo.findAll();
			List<Cliente> clientes =new ArrayList<Cliente>();
		usuario.forEach(c -> clientes.add(c));

			return clientes;

	}





}
