package com.vpi.springboot.Logica;

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
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.Repositorios.AdministradorRepositorio;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.exception.AdministradorException;

@Service
public class AdministradorService implements AdministradorServicioInterfaz {

	@Autowired
	private AdministradorRepositorio repo;

	@Autowired
	private ClienteRepositorio clienteRepo;

	@Autowired
	private RestauranteRepositorio restauranteRepo;

	@Override
	public void crearAdministrador(Administrador admin) throws AdministradorException {
		Optional<Administrador> optionalUser = repo.findById(admin.getMail());
		if (optionalUser.isPresent()) {
			throw new AdministradorException(AdministradorException.AdministradorYaExiste(admin.getMail()));
		} else {
			String mail = admin.getMail();
			if (mail.contains("@"))
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
	
	@Override
	public Map<String, Object> listarRestaurantes(int page, int size, int estado) {
		Map<String, Object> response = new HashMap<>();
		List<DTRestaurante> retorno = new ArrayList<DTRestaurante>();
		List<Restaurante> restaurantes = new ArrayList<Restaurante>();
		Pageable paging = PageRequest.of(page, size);
		Page<Restaurante> pageRestaurante;
		Boolean tipo;
		String estadoString = "";
		
		switch(estado) {
		case 0:
			estadoString = "ACEPTADO";
			tipo = true;
			break;
		case 1: 
			estadoString = "EN_ESPERA";
			tipo = true;
			break;
		case 2: 
			estadoString = "RECHAZADO";
			tipo = true;
			break;
		default: //por default busca todos los restaurantes
			tipo = false;
			break;
		}
		
		if(tipo)
			pageRestaurante = restauranteRepo.findByEstado(estadoString, paging);
		else 
			pageRestaurante = restauranteRepo.findAll(paging);
		
		restaurantes = pageRestaurante.getContent();
		
		for(Restaurante r : restaurantes) {
			retorno.add(new DTRestaurante(r));
		}
		
		response.put("currentPage", pageRestaurante.getNumber());
		response.put("totalItems", pageRestaurante.getTotalElements());
		response.put("restaurantes", retorno);
		
		return response;
	}
}
