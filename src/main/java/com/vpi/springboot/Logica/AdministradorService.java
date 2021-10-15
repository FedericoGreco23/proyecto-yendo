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
import com.vpi.springboot.Modelo.dto.DTUsuario;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Repositorios.AdministradorRepositorio;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.exception.AdministradorException;
import com.vpi.springboot.exception.RestauranteException;

@Service
public class AdministradorService implements AdministradorServicioInterfaz {

	@Autowired
	private AdministradorRepositorio adminRepo;

	@Autowired
	private ClienteRepositorio clienteRepo;

	@Autowired
	private RestauranteRepositorio resRepo;

	@Override
	public void crearAdministrador(Administrador admin) throws AdministradorException {
		Optional<Administrador> optionalUser = adminRepo.findById(admin.getMail());
		if (optionalUser.isPresent()) {
			throw new AdministradorException(AdministradorException.AdministradorYaExiste(admin.getMail()));
		} else {
			String mail = admin.getMail();
			if (mail.contains("@"))
				adminRepo.save(admin);
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
			Optional<Restaurante> restaurante = resRepo.findById(mail);
			restaurante.get().setBloqueado(true);
			resRepo.save(restaurante.get());
		}
	}

	@Override
	public void desbloquearUsuario(String mail, String clienteRestaurante) {
		if (clienteRestaurante.equals("Cliente")) {
			Optional<Cliente> cliente = clienteRepo.findById(mail);
			cliente.get().setBloqueado(false);
			clienteRepo.save(cliente.get());
		} else if (clienteRestaurante.equals("Restaurante")) {
			Optional<Restaurante> restaurante = resRepo.findById(mail);
			restaurante.get().setBloqueado(false);
			resRepo.save(restaurante.get());
		}
	}

	// 0 -> cliente
	// 1 -> restaurante
	// 2 -> administrador
	@Override
	public Map<String, Object> listarUsuariosRegistrados(int page, int size, int tipoUsuario) {
		Map<String, Object> response = new HashMap<>();
		List<DTUsuario> usuarios = new ArrayList<DTUsuario>();
		Pageable paging = PageRequest.of(page, size);

		switch (tipoUsuario) {
		case 0:
			Page<Cliente> pageClientes = clienteRepo.findAll(paging);
			List<Cliente> clientes = pageClientes.getContent();
			response.put("currentPage", pageClientes.getNumber());
			response.put("totalItems", pageClientes.getTotalElements());
			for (Cliente c : clientes) {
				usuarios.add(new DTUsuario(c, "Cliente"));
			}
			break;
		case 1:
			Page<Restaurante> pageRestaurantes = resRepo.findAll(paging);
			List<Restaurante> restaurantes = pageRestaurantes.getContent();
			response.put("currentPage", pageRestaurantes.getNumber());
			response.put("totalItems", pageRestaurantes.getTotalElements());
			for (Restaurante c : restaurantes) {
				usuarios.add(new DTUsuario(c, "Restaurante"));
			}
			break;
		case 2:
			Page<Administrador> pageAdministradores = adminRepo.findAll(paging);
			List<Administrador> administrador = pageAdministradores.getContent();
			response.put("currentPage", pageAdministradores.getNumber());
			response.put("totalItems", pageAdministradores.getTotalElements());
			for (Administrador c : administrador) {
				usuarios.add(new DTUsuario(c, "Administrador"));
			}
			break;
		}

		response.put("usuarios", usuarios);
		return response;
	}

	@Override
	public Map<String, Object> listarRestaurantes(int page, int size, int estado) {
		Map<String, Object> response = new HashMap<>();
		List<DTRestaurante> retorno = new ArrayList<DTRestaurante>();
		List<Restaurante> restaurantes = new ArrayList<Restaurante>();
		Pageable paging = PageRequest.of(page, size);
		Page<Restaurante> pageRestaurante;
		Boolean tipo;
		EnumEstadoRestaurante est = EnumEstadoRestaurante.ACEPTADO;

		switch (estado) {
		case 0:
			est = EnumEstadoRestaurante.ACEPTADO;
			tipo = true;
			break;
		case 1:
			est = EnumEstadoRestaurante.EN_ESPERA;
			tipo = true;
			break;
		case 2:
			est = EnumEstadoRestaurante.RECHAZADO;
			tipo = true;
			break;
		default: // por default busca todos los restaurantes
			tipo = false;
			break;
		}

		if (tipo)
			pageRestaurante = resRepo.findByEstado(est, paging);
		else
			pageRestaurante = resRepo.findAll(paging);

		restaurantes = pageRestaurante.getContent();

		for (Restaurante r : restaurantes) {
			retorno.add(new DTRestaurante(r));
		}

		response.put("currentPage", pageRestaurante.getNumber());
		response.put("totalItems", pageRestaurante.getTotalElements());
		response.put("restaurantes", retorno);

		return response;
	}

	public void cambiarEstadoRestaurante(String varRestaurante, int estado) throws RestauranteException {
		Restaurante restaurante = resRepo.findByNombre(varRestaurante);
		if(restaurante == null) 
			throw new RestauranteException(RestauranteException.NotFoundExceptionNombre(varRestaurante));
		
		
		switch(estado) {
		case 1:
			restaurante.setEstado(EnumEstadoRestaurante.ACEPTADO);
			break;
		case 2:
			restaurante.setEstado(EnumEstadoRestaurante.RECHAZADO);
			break;
		}
		
		resRepo.save(restaurante);
	}
}
