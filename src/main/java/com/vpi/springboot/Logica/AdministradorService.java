package com.vpi.springboot.Logica;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.*;
import com.vpi.springboot.Modelo.dto.DTAdministrador;
import com.vpi.springboot.Modelo.dto.DTCliente;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
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
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AdministradorRepositorio adminRepo;

	@Autowired
	private ClienteRepositorio clienteRepo;

	@Autowired
	private RestauranteRepositorio resRepo;

	@Override
	public DTRespuesta crearAdministrador(Administrador admin) throws AdministradorException {
		Optional<Administrador> optionalUser = adminRepo.findById(admin.getMail());
		if (optionalUser.isPresent()) {
			throw new AdministradorException(AdministradorException.AdministradorYaExiste(admin.getMail()));
		} else {
			String mail = admin.getMail();
			if (mail.contains("@")) {
				admin.setActivo(true);
				admin.setBloqueado(false);
				admin.setContrasenia(passwordEncoder.encode(admin.getContrasenia()));
				admin.setFechaCreacion(LocalDate.now());
				adminRepo.save(admin);
				return new DTRespuesta("Administrador creado con éxito.");
			} else
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

	// 2 -> administrador
	// 1 -> restaurante
	// 0 -> cliente
	// Retorna la lista de usuario (Administrador, restaurante o cliente) de
	// elementos que cumplen con la busqueda
//	@SuppressWarnings("unused")
	@Override
	public Map<String, Object> buscarUsuario(int page, int size, int tipoUsuario, Integer antiguedadUsuario,
			String texto) {
		List<DTAdministrador> DTAdministradores = new ArrayList<DTAdministrador>();
		List<DTRestaurante> DTRestaurantes = new ArrayList<DTRestaurante>();
		List<DTCliente> DTClientes = new ArrayList<DTCliente>();
		List<DTUsuario> DTUsuarios = new ArrayList<DTUsuario>();
		List<Administrador> administradores = new ArrayList<Administrador>();
		List<Restaurante> restaurantes = new ArrayList<Restaurante>();
		List<Cliente> clientes = new ArrayList<Cliente>();

		Map<String, Object> response = new HashMap<>();
		// Page<DTUsuario> pageUsuario;

		Pageable paging = PageRequest.of(page, size);

		switch (tipoUsuario) {
		case 2:
			Page<Administrador> pageAdministradores;
			if (antiguedadUsuario > 0) {
				if (!texto.equalsIgnoreCase("")) {
					// Aplico los 3 filtros
					pageAdministradores = adminRepo.buscarAdministradorNombre(texto, paging);
					administradores = pageAdministradores.getContent();
					for (Administrador administrador : administradores) {
						long days = ChronoUnit.DAYS.between(administrador.getFechaCreacion(), LocalDate.now());
						if (days > antiguedadUsuario) {
							DTUsuarios.add(new DTUsuario(administrador, "Administrador"));
							// DTAdministradores.add(administrador.getDatos());
						}
					}

				} else {
					// Aplico tipo y antiguedad
					pageAdministradores = (Page<Administrador>) adminRepo.buscarAdministrador(paging);
					administradores = pageAdministradores.getContent();
					// administradores = adminRepo.buscarAdministrador();
					for (Administrador administrador : administradores) {
						long days = ChronoUnit.DAYS.between(administrador.getFechaCreacion(), LocalDate.now());
						if (days > antiguedadUsuario) {
							DTUsuarios.add(new DTUsuario(administrador, "Administrador"));
							// DTAdministradores.add(administrador.getDatos());
						}
					}
				}
			} else if (!texto.equalsIgnoreCase("")) {
				// tipo y nombreUsuario
				pageAdministradores = (Page<Administrador>) adminRepo.buscarAdministradorNombre(texto, paging);
				administradores = pageAdministradores.getContent();
				// administradores = adminRepo.buscarAdministradorNombre(texto);
				for (Administrador administrador : administradores) {
					DTUsuarios.add(new DTUsuario(administrador, "Administrador"));
					// DTAdministradores.add(administrador.getDatos());
				}
			} else {
				// Solo tipo usuario
				pageAdministradores = (Page<Administrador>) adminRepo.buscarAdministrador(paging);
				administradores = pageAdministradores.getContent();
				// administradores = adminRepo.buscarAdministrador();
				for (Administrador administrador : administradores) {
					DTUsuarios.add(new DTUsuario(administrador, "Administrador"));
					// DTAdministradores.add(administrador.getDatos());
				}
			}
			response.put("currentPage", pageAdministradores.getNumber());
			response.put("totalItems", pageAdministradores.getTotalElements());
			response.put("usuarios", DTUsuarios);

			// listaTiposDeUsuarios.setAdministradores(DTAdministradores);
			break;

		case 1:
			Page<Restaurante> pageRestaurantes;
			if (antiguedadUsuario > 0) {
				if (!texto.equalsIgnoreCase("")) {
					// Aplico los 3 filtros
					pageRestaurantes = (Page<Restaurante>) resRepo.buscarRestauranteNombre(texto, paging);
					restaurantes = pageRestaurantes.getContent();
					// restaurantes = restaRepo.buscarRestauranteNombre(texto);
					for (Restaurante restaurante : restaurantes) {
						long days = ChronoUnit.DAYS.between(restaurante.getFechaCreacion(), LocalDate.now());
						if (days > antiguedadUsuario) {
							DTUsuarios.add(new DTUsuario(restaurante, "Restaurante"));
							// DTRestaurantes.add(restaurante.getDatos());
						}
					}

				} else {
					// Aplico tipo y antiguedad
					pageRestaurantes = (Page<Restaurante>) resRepo.buscarRestaurante(paging);
					restaurantes = pageRestaurantes.getContent();
					// restaurantes = restaRepo.buscarRestaurante();
					for (Restaurante restaurante : restaurantes) {
						long days = ChronoUnit.DAYS.between(restaurante.getFechaCreacion(), LocalDate.now());
						if (days > antiguedadUsuario) {
							DTUsuarios.add(new DTUsuario(restaurante, "Restaurante"));
							// DTRestaurantes.add(restaurante.getDatos());
						}
					}
				}
			} else if (!texto.equalsIgnoreCase("")) {
				// tipo y nombreUsuario
				pageRestaurantes = (Page<Restaurante>) resRepo.buscarRestauranteNombre(texto, paging);
				restaurantes = pageRestaurantes.getContent();
				// restaurantes = restaRepo.buscarRestauranteNombre(texto);
				for (Restaurante restaurante : restaurantes) {
					DTUsuarios.add(new DTUsuario(restaurante, "Restaurante"));
					// DTRestaurantes.add(restaurante.getDatos());
				}
			} else {
				// Solo tipo usuario
				pageRestaurantes = (Page<Restaurante>) resRepo.buscarRestaurante(paging);
				restaurantes = pageRestaurantes.getContent();
				// restaurantes = restaRepo.buscarRestaurante();
				for (Restaurante restaurante : restaurantes) {
					DTUsuarios.add(new DTUsuario(restaurante, "Restaurante"));
					// DTRestaurantes.add(restaurante.getDatos());
				}
			}

			response.put("currentPage", pageRestaurantes.getNumber());
			response.put("totalItems", pageRestaurantes.getTotalElements());
			response.put("usuarios", DTUsuarios);

			// listaTiposDeUsuarios.setRestaurantes(DTRestaurantes);
			break;

		case 0:
			Page<Cliente> pageClientes;
			if (antiguedadUsuario > 0) {
				if (!texto.equalsIgnoreCase("")) {
					// Aplico los 3 filtros
					pageClientes = (Page<Cliente>) clienteRepo.buscarClienteNombre(texto, paging);
					clientes = pageClientes.getContent();
					// clientes = clienteRepo.buscarClienteNombre(texto);
					for (Cliente cliente : clientes) {
						long days = ChronoUnit.DAYS.between(cliente.getFechaCreacion(), LocalDate.now());
						if (days > antiguedadUsuario) {
							DTUsuarios.add(new DTUsuario(cliente, "Cliente"));
							// DTClientes.add(cliente.getDatos());
						}
					}

				} else {
					// Aplico tipo y antiguedad
					pageClientes = (Page<Cliente>) clienteRepo.buscarCliente(paging);
					clientes = pageClientes.getContent();
					// clientes = clienteRepo.buscarCliente();
					for (Cliente cliente : clientes) {
						long days = ChronoUnit.DAYS.between(cliente.getFechaCreacion(), LocalDate.now());
						if (days > antiguedadUsuario) {
							DTUsuarios.add(new DTUsuario(cliente, "Cliente"));
							// DTClientes.add(cliente.getDatos());
						}
					}
				}
			} else if (!texto.equalsIgnoreCase("")) {
				// tipo y nombreUsuario
				pageClientes = (Page<Cliente>) clienteRepo.buscarClienteNombre(texto, paging);
				clientes = pageClientes.getContent();
				// clientes = clienteRepo.buscarClienteNombre(texto);
				for (Cliente cliente : clientes) {
					DTUsuarios.add(new DTUsuario(cliente, "Cliente"));
					// DTClientes.add(cliente.getDatos());
				}
			} else {
				// Solo tipo usuario
				pageClientes = (Page<Cliente>) clienteRepo.buscarCliente(paging);
				clientes = pageClientes.getContent();
				// clientes = clienteRepo.buscarCliente();
				for (Cliente cliente : clientes) {
					DTUsuarios.add(new DTUsuario(cliente, "Cliente"));
					// DTClientes.add(cliente.getDatos());
				}
			}

			response.put("currentPage", pageClientes.getNumber());
			response.put("totalItems", pageClientes.getTotalElements());
			response.put("usuarios", DTUsuarios);

			// listaTiposDeUsuarios.setClientes(DTClientes);
			break;
		}

		return response;
		// return DTUsuarios;
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
		Optional<Restaurante> optionalRestaurante = resRepo.findById(varRestaurante);
		if (!optionalRestaurante.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionNombre(varRestaurante));
		}
		Restaurante restaurante = optionalRestaurante.get();

		switch (estado) {
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
