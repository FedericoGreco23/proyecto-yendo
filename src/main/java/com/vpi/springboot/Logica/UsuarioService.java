package com.vpi.springboot.Logica;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.*;
import com.vpi.springboot.Modelo.dto.DTAdministrador;
import com.vpi.springboot.Modelo.dto.DTCliente;
import com.vpi.springboot.Modelo.dto.DTListasTiposDeUsuarios;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
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
	
	//Retornar un Datatype que contenga los 3 tipos de listas
	@SuppressWarnings("unused")
	@Override
	public DTListasTiposDeUsuarios buscarUsuario(String tipoUsuario, Integer antiguedadUsuario, String texto) {
		List<DTAdministrador> DTAdministradores = new ArrayList<DTAdministrador>();
		List<DTRestaurante> DTRestaurantes = new ArrayList<DTRestaurante>();
		List<DTCliente> DTClientes = new ArrayList<DTCliente>();
		List<Administrador> administradores = new ArrayList<Administrador>();
		List<Restaurante> restaurantes = new ArrayList<Restaurante>();
		List<Cliente> clientes = new ArrayList<Cliente>();
		
		DTListasTiposDeUsuarios listaTiposDeUsuarios = new DTListasTiposDeUsuarios(DTAdministradores, DTRestaurantes, DTClientes);
		
		if (!tipoUsuario.equalsIgnoreCase("")) {
			if (tipoUsuario.equalsIgnoreCase("Administrador")) {
				if (antiguedadUsuario > 0) {
					if (!texto.equalsIgnoreCase("")) {
						//Aplico los 3 filtros
						//Usamos la funcion de busqueda que corresponda si lleva busqueda con nombre o no y aplicamos una funcion de resta entre 2 fechas para rehacer la lista de elementos que cumplen la antiguedad pedida
						administradores = adminRepo.buscarAdministradorNombre(texto);
						for (Administrador administrador : administradores) {				
							long days = ChronoUnit.DAYS.between(administrador.getFechaCreacion(), LocalDate.now());
							if (days > antiguedadUsuario) {
								DTAdministradores.add(administrador.getDatos());
							}
						}

					} else {
						//Aplico tipo y antiguedad
						administradores = adminRepo.buscarAdministrador();
						for (Administrador administrador : administradores) {				
							long days = ChronoUnit.DAYS.between(administrador.getFechaCreacion(), LocalDate.now());
							if (days > antiguedadUsuario) {
								DTAdministradores.add(administrador.getDatos());
							}
						}
					}
				} else if (!texto.equalsIgnoreCase("")){
					//tipo y nombreUsuario
					administradores = adminRepo.buscarAdministradorNombre(texto);
					for (Administrador administrador : administradores) {
						DTAdministradores.add(administrador.getDatos());
					}
				} else {
					//Solo tipo usuario
					administradores = adminRepo.buscarAdministrador();
					for (Administrador administrador : administradores) {
						DTAdministradores.add(administrador.getDatos());
					}
				}
				
				listaTiposDeUsuarios.setAdministradores(DTAdministradores);
				return listaTiposDeUsuarios;
				
			} else if (tipoUsuario.equalsIgnoreCase("Restaurante")) {
				if (antiguedadUsuario > 0) {
					if (!texto.equalsIgnoreCase("")) {
						//Aplico los 3 filtros
						restaurantes = restaRepo.buscarRestauranteNombre(texto);
						for (Restaurante restaurante : restaurantes) {				
							long days = ChronoUnit.DAYS.between(restaurante.getFechaCreacion(), LocalDate.now());
							if (days > antiguedadUsuario) {
								DTRestaurantes.add(restaurante.getDatos());
							}
						}
						
					} else {
						//Aplico tipo y antiguedad
						restaurantes = restaRepo.buscarRestaurante();
						for (Restaurante restaurante : restaurantes) {				
							long days = ChronoUnit.DAYS.between(restaurante.getFechaCreacion(), LocalDate.now());
							if (days > antiguedadUsuario) {
								DTRestaurantes.add(restaurante.getDatos());
							}
						}
					}
				} else if (!texto.equalsIgnoreCase("")){
					//tipo y nombreUsuario
					restaurantes = restaRepo.buscarRestauranteNombre(texto);
					for (Restaurante restaurante : restaurantes) {
						DTRestaurantes.add(restaurante.getDatos());
					}
				} else {
					//Solo tipo usuario
					restaurantes = restaRepo.buscarRestaurante();
					for (Restaurante restaurante : restaurantes) {
						DTRestaurantes.add(restaurante.getDatos());
					}
				}
				
				listaTiposDeUsuarios.setRestaurantes(DTRestaurantes);
				return listaTiposDeUsuarios;
				
			} else {
				//tipoUsuario == Cliente
				if (antiguedadUsuario > 0) {
					if (!texto.equalsIgnoreCase("")) {
						//Aplico los 3 filtros
						clientes = clienteRepo.buscarClienteNombre(texto);
						for (Cliente cliente : clientes) {
							long days = ChronoUnit.DAYS.between(cliente.getFechaCreacion(), LocalDate.now());
							if (days > antiguedadUsuario) {
								DTClientes.add(cliente.getDatos());
							}
						}
						
					} else {
						//Aplico tipo y antiguedad
						clientes = clienteRepo.buscarCliente();
						for (Cliente cliente : clientes) {
							long days = ChronoUnit.DAYS.between(cliente.getFechaCreacion(), LocalDate.now());
							if (days > antiguedadUsuario) {
								DTClientes.add(cliente.getDatos());
							}
						}
					}
				} else if (!texto.equalsIgnoreCase("")){
					//tipo y nombreUsuario
					clientes = clienteRepo.buscarClienteNombre(texto);
					for (Cliente cliente : clientes) {
						DTClientes.add(cliente.getDatos());
					}
				} else {
					//Solo tipo usuario
					clientes = clienteRepo.buscarCliente();
					for (Cliente cliente : clientes) {
						DTClientes.add(cliente.getDatos());
					}
				}

				listaTiposDeUsuarios.setClientes(DTClientes);
				return listaTiposDeUsuarios;
				
			}
		} else if (!texto.equalsIgnoreCase("")) {
			if (antiguedadUsuario > 0) {
				//Aplico nombre y antiguedad
				administradores = adminRepo.buscarAdministradorNombre(texto);
				for (Administrador administrador : administradores) {				
					long days = ChronoUnit.DAYS.between(administrador.getFechaCreacion(), LocalDate.now());
					if (days > antiguedadUsuario) {
						DTAdministradores.add(administrador.getDatos());
					}
				}
				
				restaurantes = restaRepo.buscarRestauranteNombre(texto);
				for (Restaurante restaurante : restaurantes) {				
					long days = ChronoUnit.DAYS.between(restaurante.getFechaCreacion(), LocalDate.now());
					if (days > antiguedadUsuario) {
						DTRestaurantes.add(restaurante.getDatos());
					}
				}
				
				clientes = clienteRepo.buscarClienteNombre(texto);
				for (Cliente cliente : clientes) {
					long days = ChronoUnit.DAYS.between(cliente.getFechaCreacion(), LocalDate.now());
					if (days > antiguedadUsuario) {
						DTClientes.add(cliente.getDatos());
					}
				}
				
			} else {
				//Aplico solo nombre
				administradores = adminRepo.buscarAdministradorNombre(texto);
				for (Administrador administrador : administradores) {
					DTAdministradores.add(administrador.getDatos());
				}
				restaurantes = restaRepo.buscarRestauranteNombre(texto);
				for (Restaurante restaurante : restaurantes) {
					DTRestaurantes.add(restaurante.getDatos());
				}
				clientes = clienteRepo.buscarClienteNombre(texto);
				for (Cliente cliente : clientes) {
					DTClientes.add(cliente.getDatos());
				}
			}
			
		} else {
			//Aplico solo antiguedad
			administradores = adminRepo.buscarAdministrador();
			for (Administrador administrador : administradores) {				
				long days = ChronoUnit.DAYS.between(administrador.getFechaCreacion(), LocalDate.now());
				if (days > antiguedadUsuario) {
					DTAdministradores.add(administrador.getDatos());
				}
			}
			
			restaurantes = restaRepo.buscarRestaurante();
			for (Restaurante restaurante : restaurantes) {				
				long days = ChronoUnit.DAYS.between(restaurante.getFechaCreacion(), LocalDate.now());
				if (days > antiguedadUsuario) {
					DTRestaurantes.add(restaurante.getDatos());
				}
			}
			
			clientes = clienteRepo.buscarCliente();
			for (Cliente cliente : clientes) {
				long days = ChronoUnit.DAYS.between(cliente.getFechaCreacion(), LocalDate.now());
				if (days > antiguedadUsuario) {
					DTClientes.add(cliente.getDatos());
				}
			}
		}
		
		listaTiposDeUsuarios.setAdministradores(DTAdministradores);
		listaTiposDeUsuarios.setRestaurantes(DTRestaurantes);
		listaTiposDeUsuarios.setClientes(DTClientes);
		return listaTiposDeUsuarios;
	}
}
