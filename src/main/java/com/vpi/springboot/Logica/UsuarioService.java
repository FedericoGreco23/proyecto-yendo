package com.vpi.springboot.Logica;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
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
	
	
	// 0 -> administrador
	// 1 -> restaurante
	// 2 -> cliente
	//Retorna la lista de usuario (Administrador, restaurante o cliente) de elementos que cumplen con la busqueda
	@SuppressWarnings("unused")
	@Override
	public List<DTUsuario> buscarUsuario(int page, int size, int tipoUsuario, Integer antiguedadUsuario, String texto) {
		List<DTAdministrador> DTAdministradores = new ArrayList<DTAdministrador>();
		List<DTRestaurante> DTRestaurantes = new ArrayList<DTRestaurante>();
		List<DTCliente> DTClientes = new ArrayList<DTCliente>();
		List<DTUsuario> DTUsuarios = new ArrayList<DTUsuario>();
		List<Administrador> administradores = new ArrayList<Administrador>();
		List<Restaurante> restaurantes = new ArrayList<Restaurante>();
		List<Cliente> clientes = new ArrayList<Cliente>();
		
		Pageable paging = PageRequest.of(page, size);
		
		switch(tipoUsuario) {
		case 2:
			if (antiguedadUsuario > 0) {
				if (!texto.equalsIgnoreCase("")) {
					//Aplico los 3 filtros
					Page<Administrador> pageAdministradores = adminRepo.buscarAdministradorNombre(texto, paging);
					administradores = pageAdministradores.getContent();
					for (Administrador administrador : administradores) {				
						long days = ChronoUnit.DAYS.between(administrador.getFechaCreacion(), LocalDate.now());
						if (days > antiguedadUsuario) {
							DTUsuarios.add(new DTUsuario(administrador, "Administrador"));
							//DTAdministradores.add(administrador.getDatos());
						}
					}

				} else {
					//Aplico tipo y antiguedad
					Page<Administrador> pageAdministradores = (Page<Administrador>) adminRepo.buscarAdministrador(paging);
					administradores = pageAdministradores.getContent();
					//administradores = adminRepo.buscarAdministrador();
					for (Administrador administrador : administradores) {				
						long days = ChronoUnit.DAYS.between(administrador.getFechaCreacion(), LocalDate.now());
						if (days > antiguedadUsuario) {
							DTUsuarios.add(new DTUsuario(administrador, "Administrador"));
							//DTAdministradores.add(administrador.getDatos());
						}
					}
				}
			} else if (!texto.equalsIgnoreCase("")){
				//tipo y nombreUsuario
				Page<Administrador> pageAdministradores = (Page<Administrador>) adminRepo.buscarAdministradorNombre(texto, paging);
				administradores = pageAdministradores.getContent();
				//administradores = adminRepo.buscarAdministradorNombre(texto);
				for (Administrador administrador : administradores) {
					DTUsuarios.add(new DTUsuario(administrador, "Administrador"));
					//DTAdministradores.add(administrador.getDatos());
				}
			} else {
				//Solo tipo usuario
				Page<Administrador> pageAdministradores = (Page<Administrador>) adminRepo.buscarAdministrador(paging);
				administradores = pageAdministradores.getContent();
				//administradores = adminRepo.buscarAdministrador();
				for (Administrador administrador : administradores) {
					DTUsuarios.add(new DTUsuario(administrador, "Administrador"));
					//DTAdministradores.add(administrador.getDatos());
				}
			}
			
			//listaTiposDeUsuarios.setAdministradores(DTAdministradores);
			break;
			
			
		case 1:
			if (antiguedadUsuario > 0) {
				if (!texto.equalsIgnoreCase("")) {
					//Aplico los 3 filtros
					Page<Restaurante> pageRestaurantes = (Page<Restaurante>) restaRepo.buscarRestauranteNombre(texto, paging);
					restaurantes = pageRestaurantes.getContent();
					//restaurantes = restaRepo.buscarRestauranteNombre(texto);
					for (Restaurante restaurante : restaurantes) {				
						long days = ChronoUnit.DAYS.between(restaurante.getFechaCreacion(), LocalDate.now());
						if (days > antiguedadUsuario) {
							DTUsuarios.add(new DTUsuario(restaurante, "Restaurante"));
							//DTRestaurantes.add(restaurante.getDatos());
						}
					}
					
				} else {
					//Aplico tipo y antiguedad
					Page<Restaurante> pageRestaurantes = (Page<Restaurante>) restaRepo.buscarRestaurante(paging);
					restaurantes = pageRestaurantes.getContent();
					//restaurantes = restaRepo.buscarRestaurante();
					for (Restaurante restaurante : restaurantes) {				
						long days = ChronoUnit.DAYS.between(restaurante.getFechaCreacion(), LocalDate.now());
						if (days > antiguedadUsuario) {
							DTUsuarios.add(new DTUsuario(restaurante, "Restaurante"));
							//DTRestaurantes.add(restaurante.getDatos());
						}
					}
				}
			} else if (!texto.equalsIgnoreCase("")){
				//tipo y nombreUsuario
				Page<Restaurante> pageRestaurantes = (Page<Restaurante>) restaRepo.buscarRestauranteNombre(texto, paging);
				restaurantes = pageRestaurantes.getContent();
				//restaurantes = restaRepo.buscarRestauranteNombre(texto);
				for (Restaurante restaurante : restaurantes) {
					DTUsuarios.add(new DTUsuario(restaurante, "Restaurante"));
					//DTRestaurantes.add(restaurante.getDatos());
				}
			} else {
				//Solo tipo usuario
				Page<Restaurante> pageRestaurantes = (Page<Restaurante>) restaRepo.buscarRestaurante(paging);
				restaurantes = pageRestaurantes.getContent();
				//restaurantes = restaRepo.buscarRestaurante();
				for (Restaurante restaurante : restaurantes) {
					DTUsuarios.add(new DTUsuario(restaurante, "Restaurante"));
					//DTRestaurantes.add(restaurante.getDatos());
				}
			}
			
			//listaTiposDeUsuarios.setRestaurantes(DTRestaurantes);
			break;
			
			
		case 0:
			if (antiguedadUsuario > 0) {
				if (!texto.equalsIgnoreCase("")) {
					//Aplico los 3 filtros
					Page<Cliente> pageClientes = (Page<Cliente>) clienteRepo.buscarClienteNombre(texto, paging);
					clientes = pageClientes.getContent();
					//clientes = clienteRepo.buscarClienteNombre(texto);
					for (Cliente cliente : clientes) {
						long days = ChronoUnit.DAYS.between(cliente.getFechaCreacion(), LocalDate.now());
						if (days > antiguedadUsuario) {
							DTUsuarios.add(new DTUsuario(cliente, "Cliente"));
							//DTClientes.add(cliente.getDatos());
						}
					}
					
				} else {
					//Aplico tipo y antiguedad
					Page<Cliente> pageClientes = (Page<Cliente>) clienteRepo.buscarCliente(paging);
					clientes = pageClientes.getContent();
					//clientes = clienteRepo.buscarCliente();
					for (Cliente cliente : clientes) {
						long days = ChronoUnit.DAYS.between(cliente.getFechaCreacion(), LocalDate.now());
						if (days > antiguedadUsuario) {
							DTUsuarios.add(new DTUsuario(cliente, "Cliente"));
							//DTClientes.add(cliente.getDatos());
						}
					}
				}
			} else if (!texto.equalsIgnoreCase("")){
				//tipo y nombreUsuario
				Page<Cliente> pageClientes = (Page<Cliente>) clienteRepo.buscarClienteNombre(texto, paging);
				clientes = pageClientes.getContent();
				//clientes = clienteRepo.buscarClienteNombre(texto);
				for (Cliente cliente : clientes) {
					DTUsuarios.add(new DTUsuario(cliente, "Cliente"));
					//DTClientes.add(cliente.getDatos());
				}
			} else {
				//Solo tipo usuario
				Page<Cliente> pageClientes = (Page<Cliente>) clienteRepo.buscarCliente(paging);
				clientes = pageClientes.getContent();
				//clientes = clienteRepo.buscarCliente();
				for (Cliente cliente : clientes) {
					DTUsuarios.add(new DTUsuario(cliente, "Cliente"));
					//DTClientes.add(cliente.getDatos());
				}
			}

			//listaTiposDeUsuarios.setClientes(DTClientes);
			break;
		}
		
		return DTUsuarios;
	}
}
