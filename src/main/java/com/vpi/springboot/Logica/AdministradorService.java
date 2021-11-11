package com.vpi.springboot.Logica;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.vpi.springboot.Modelo.dto.DTCategoria;
import com.vpi.springboot.Modelo.dto.DTCliente;
import com.vpi.springboot.Modelo.dto.DTProductoVendido;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.Modelo.dto.DTRestaurantePedido;
import com.vpi.springboot.Modelo.dto.DTTopCategoria;
import com.vpi.springboot.Modelo.dto.DTUsuario;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Repositorios.AdministradorRepositorio;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.Repositorios.mongo.ProductosVendidosRepositorio;
import com.vpi.springboot.Repositorios.mongo.RestaurantePedidosRepositorio;
import com.vpi.springboot.Repositorios.mongo.TopCategoriasRepositorio;
import com.vpi.springboot.exception.AdministradorException;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;

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

	@Autowired
	private RestaurantePedidosRepositorio restaurantePedidosRepo;
	
	@Autowired
	private ProductosVendidosRepositorio productosVendidosRepo;
	
	@Autowired
	private TopCategoriasRepositorio topCategoriasRepo;
	
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
	public DTRespuesta eliminarUsuario(String mail, String clienteRestaurante) throws UsuarioException {
		if (clienteRestaurante.equalsIgnoreCase("Cliente")) {
			Optional<Cliente> cliente = clienteRepo.findById(mail);
			if (cliente.isPresent()) {
				cliente.get().setActivo(false);
				clienteRepo.save(cliente.get());
				return new DTRespuesta("Cliente eliminado con éxito.");
			} else {
				throw new UsuarioException(UsuarioException.NotFoundException(mail));
			}
		} else if (clienteRestaurante.equalsIgnoreCase("Restaurante")) {
			Optional<Restaurante> restaurante = resRepo.findById(mail);
			if (restaurante.isPresent()) {
				restaurante.get().setActivo(true);
				resRepo.save(restaurante.get());
			} else
				throw new UsuarioException(UsuarioException.NotFoundException(mail));
		}
		
		return new DTRespuesta("Usuario eliminado con éxito");
	}

	@Override
	public DTRespuesta bloquearUsuario(String mail, String clienteRestaurante) throws UsuarioException {
		if (clienteRestaurante.equalsIgnoreCase("Cliente")) {
			Optional<Cliente> cliente = clienteRepo.findById(mail);
			if (cliente.isPresent()) {
				cliente.get().setBloqueado(true);
				clienteRepo.save(cliente.get());
			} else
				throw new UsuarioException(UsuarioException.NotFoundException(mail));
		} else if (clienteRestaurante.equalsIgnoreCase("Restaurante")) {
			Optional<Restaurante> restaurante = resRepo.findById(mail);
			if (restaurante.isPresent()) {
				restaurante.get().setBloqueado(true);
				resRepo.save(restaurante.get());
			} else
				throw new UsuarioException(UsuarioException.NotFoundException(mail));
		}

		return new DTRespuesta("Usuario bloqueado con éxito.");
	}

	@Override
	public DTRespuesta desbloquearUsuario(String mail, String clienteRestaurante) throws UsuarioException {
		if (clienteRestaurante.equalsIgnoreCase("Cliente")) {
			Optional<Cliente> cliente = clienteRepo.findById(mail);
			if (cliente.isPresent()) {
				cliente.get().setBloqueado(false);
				clienteRepo.save(cliente.get());
			} else
				throw new UsuarioException(UsuarioException.NotFoundException(mail));
		} else if (clienteRestaurante.equalsIgnoreCase("Restaurante")) {
			Optional<Restaurante> restaurante = resRepo.findById(mail);
			if (restaurante.isPresent()) {
				restaurante.get().setBloqueado(false);
				resRepo.save(restaurante.get());
			} else
				throw new UsuarioException(UsuarioException.NotFoundException(mail));
		}

		return new DTRespuesta("Usuario desbloqueado con éxito.");
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
			String texto, String sort, int order) {
		List<DTUsuario> DTUsuarios = new ArrayList<DTUsuario>();
		List<Administrador> administradores = new ArrayList<Administrador>();
		List<Restaurante> restaurantes = new ArrayList<Restaurante>();
		List<Cliente> clientes = new ArrayList<Cliente>();

		Map<String, Object> response = new HashMap<>();

		//Sort sorting = Sort.by(Sort.Order.desc("calificacionPromedio"), Sort.Order.asc("mail"));
		Sort sorting;
		Pageable paging;
		
		if (!sort.equalsIgnoreCase("")) {
			if (order == 1) {
				sorting = Sort.by(Sort.Order.desc(sort));
			} else {
				sorting = Sort.by(Sort.Order.asc(sort));
			}
			paging = PageRequest.of(page, size, sorting);
		} else {
			paging = PageRequest.of(page, size);
		}
		
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
						}
					}
				} else {
					// Aplico tipo y antiguedad
					pageAdministradores = adminRepo.buscarAdministrador(paging);
					administradores = pageAdministradores.getContent();
					for (Administrador administrador : administradores) {
						long days = ChronoUnit.DAYS.between(administrador.getFechaCreacion(), LocalDate.now());
						if (days > antiguedadUsuario) {
							DTUsuarios.add(new DTUsuario(administrador, "Administrador"));
						}
					}
				}
			} else if (!texto.equalsIgnoreCase("")) {
				// tipo y nombreUsuario
				pageAdministradores = adminRepo.buscarAdministradorNombre(texto, paging);
				administradores = pageAdministradores.getContent();
				for (Administrador administrador : administradores) {
					DTUsuarios.add(new DTUsuario(administrador, "Administrador"));
				}
			} else {
				// Solo tipo usuario
				pageAdministradores = adminRepo.buscarAdministrador(paging);
				administradores = pageAdministradores.getContent();
				for (Administrador administrador : administradores) {
					DTUsuarios.add(new DTUsuario(administrador, "Administrador"));
				}
			}
			response.put("currentPage", pageAdministradores.getNumber());
			response.put("totalItems", pageAdministradores.getTotalElements());
			response.put("usuarios", DTUsuarios);

			break;

		case 1:
			Page<Restaurante> pageRestaurantes;
			if (antiguedadUsuario > 0) {
				if (!texto.equalsIgnoreCase("")) {
					// Aplico los 3 filtros
					pageRestaurantes = resRepo.buscarRestauranteNombre(texto, paging);
					restaurantes = pageRestaurantes.getContent();
					for (Restaurante restaurante : restaurantes) {
						long days = ChronoUnit.DAYS.between(restaurante.getFechaCreacion(), LocalDate.now());
						if (days > antiguedadUsuario) {
							DTUsuarios.add(new DTUsuario(restaurante, "Restaurante", restaurante.getCalificacionPromedio()));
						}
					}

				} else {
					// Aplico tipo y antiguedad
					pageRestaurantes = resRepo.buscarRestaurante(paging);
					restaurantes = pageRestaurantes.getContent();
					for (Restaurante restaurante : restaurantes) {
						long days = ChronoUnit.DAYS.between(restaurante.getFechaCreacion(), LocalDate.now());
						if (days > antiguedadUsuario) {
							DTUsuarios.add(new DTUsuario(restaurante, "Restaurante", restaurante.getCalificacionPromedio()));
						}
					}
				}
			} else if (!texto.equalsIgnoreCase("")) {
				// tipo y nombreUsuario
				pageRestaurantes = resRepo.buscarRestauranteNombre(texto, paging);
				restaurantes = pageRestaurantes.getContent();
				for (Restaurante restaurante : restaurantes) {
					DTUsuarios.add(new DTUsuario(restaurante, "Restaurante", restaurante.getCalificacionPromedio()));
				}
			} else {
				// Solo tipo usuario
				pageRestaurantes = resRepo.buscarRestaurante(paging);
				restaurantes = pageRestaurantes.getContent();
				for (Restaurante restaurante : restaurantes) {
					DTUsuarios.add(new DTUsuario(restaurante, "Restaurante", restaurante.getCalificacionPromedio()));
				}
			}

			response.put("currentPage", pageRestaurantes.getNumber());
			response.put("totalItems", pageRestaurantes.getTotalElements());
			response.put("usuarios", DTUsuarios);

			break;

		case 0:
			Page<Cliente> pageClientes;
			if (antiguedadUsuario > 0) {
				if (!texto.equalsIgnoreCase("")) {
					// Aplico los 3 filtros
					pageClientes = (Page<Cliente>) clienteRepo.buscarClienteNombre(texto, paging);
					clientes = pageClientes.getContent();
					for (Cliente cliente : clientes) {
						long days = ChronoUnit.DAYS.between(cliente.getFechaCreacion(), LocalDate.now());
						if (days > antiguedadUsuario) {
							DTUsuarios.add(new DTUsuario(cliente, "Cliente", cliente.getCalificacionPromedio()));
						}
					}

				} else {
					// Aplico tipo y antiguedad
					pageClientes = (Page<Cliente>) clienteRepo.buscarCliente(paging);
					clientes = pageClientes.getContent();
					for (Cliente cliente : clientes) {
						long days = ChronoUnit.DAYS.between(cliente.getFechaCreacion(), LocalDate.now());
						if (days > antiguedadUsuario) {
							DTUsuarios.add(new DTUsuario(cliente, "Cliente", cliente.getCalificacionPromedio()));
						}
					}
				}
			} else if (!texto.equalsIgnoreCase("")) {
				// tipo y nombreUsuario
				pageClientes = (Page<Cliente>) clienteRepo.buscarClienteNombre(texto, paging);
				clientes = pageClientes.getContent();
				for (Cliente cliente : clientes) {
					DTUsuarios.add(new DTUsuario(cliente, "Cliente", cliente.getCalificacionPromedio()));
				}
			} else {
				// Solo tipo usuario
				pageClientes = (Page<Cliente>) clienteRepo.buscarCliente(paging);
				clientes = pageClientes.getContent();
				for (Cliente cliente : clientes) {
					DTUsuarios.add(new DTUsuario(cliente, "Cliente", cliente.getCalificacionPromedio()));
				}
			}

			response.put("currentPage", pageClientes.getNumber());
			response.put("totalItems", pageClientes.getTotalElements());
			response.put("usuarios", DTUsuarios);

			break;
		}

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
			retorno.add(new DTRestaurante(r, r.getProductos()));
		}

		response.put("currentPage", pageRestaurante.getNumber());
		response.put("totalItems", pageRestaurante.getTotalElements());
		response.put("restaurantes", retorno);

		return response;
	}

	public DTRespuesta cambiarEstadoRestaurante(String varRestaurante, int estado) throws RestauranteException {
		Optional<Restaurante> optionalRestaurante = resRepo.findById(varRestaurante);
		if (!optionalRestaurante.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionNombre(varRestaurante));
		}
		Restaurante restaurante = optionalRestaurante.get();
		String est = "";

		switch (estado) {
		case 1:
			restaurante.setEstado(EnumEstadoRestaurante.ACEPTADO);
			est = "ACEPTADO";
			break;
		case 2:
			restaurante.setEstado(EnumEstadoRestaurante.RECHAZADO);
			est = "RECHAZADO";
			break;
		}

		resRepo.save(restaurante);
		return new DTRespuesta("Estado de restaurante cambiado a " + est + ".");
	}
	
	@Override
	public Map<String, Object> restaurantesConMasPedidos(int page, int size){
		Pageable paging;
		Map<String, Object> response = new HashMap<>();
		Page<DTRestaurantePedido> pageRestaurantePedido;
		Sort sort = Sort.by(Sort.Order.desc("cantPedidos"));
		paging = PageRequest.of(page, size, sort);
		pageRestaurantePedido = restaurantePedidosRepo.findAll(paging);
		List<DTRestaurantePedido> restauranteList = new ArrayList<DTRestaurantePedido>();
		restauranteList = pageRestaurantePedido.getContent();
		int pagina = pageRestaurantePedido.getNumber();
		long totalElements = pageRestaurantePedido.getTotalElements();
		response.put("currentPage", pagina);
		response.put("totalItems", totalElements);
		response.put("restaurantes", restauranteList);
		return response;
	}
	
	@Override
	public Map<String, Object> topProductos(int page, int size) {
		Sort sort = Sort.by(Sort.Order.desc("cantidad"));
		Pageable paging = PageRequest.of(page, size, sort);
		Map<String, Object> response = new HashMap<>();
		Page<DTProductoVendido> pageProductos;
		pageProductos = productosVendidosRepo.findAll(paging);
		List<DTProductoVendido> DTproductosVendidos = pageProductos.getContent();

		response.put("currentPage", pageProductos.getNumber());
		response.put("totalItems", pageProductos.getTotalElements());
		response.put("restaurantes", DTproductosVendidos);
		return response;
	}
	
	@Override
	public List<DTTopCategoria> topCategorias() {
		//List<DTProductoVendido> lista = productosVendidosRepo.findAllBy(Sort.by(Sort.Direction.DESC, "cantidad"));
		List<DTTopCategoria> lista = topCategoriasRepo.findAllBy(Sort.by(Sort.Direction.DESC, "cantidad"));
		return lista;
	}
}
