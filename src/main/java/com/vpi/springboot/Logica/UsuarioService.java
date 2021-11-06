/*package com.vpi.springboot.Logica;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.*;
import com.vpi.springboot.Modelo.dto.DTAdministrador;
import com.vpi.springboot.Modelo.dto.DTCliente;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.Modelo.dto.DTUsuario;
import com.vpi.springboot.Repositorios.AdministradorRepositorio;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;

@Service
public class UsuarioService implements UsuarioServicioInterfaz {

	@Autowired
	private AdministradorRepositorio adminRepo;
	
	@Autowired
	private RestauranteRepositorio restaRepo;
	
	@Autowired
	private ClienteRepositorio clienteRepo;

	@Override
	public void createTodo(Cliente usuario) {
		Optional<Cliente> optionalUser = clienteRepo.findById(usuario.getMail());
		if (optionalUser.isPresent()) {

		} else {
			clienteRepo.save(usuario);
		}

	}

	@Override
	public List<Cliente> getAllClientes() {
		Iterable<Cliente> usuario = clienteRepo.findAll();
		List<Cliente> clientes = new ArrayList<Cliente>();
		usuario.forEach(c -> clientes.add(c));
		return clientes;
	}
	
	
	
}*/
