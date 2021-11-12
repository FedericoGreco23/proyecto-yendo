package com.vpi.springboot.Logica;

import java.util.List;
import java.util.Map;

import com.vpi.springboot.Modelo.Administrador;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTTopCategoria;
import com.vpi.springboot.exception.AdministradorException;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;

public interface AdministradorServicioInterfaz {
	public DTRespuesta crearAdministrador(Administrador admin) throws AdministradorException;

	public DTRespuesta eliminarUsuario(String mail, String clienteRestaurante) throws UsuarioException;

	public DTRespuesta bloquearUsuario(String mail, String clienteRestaurante) throws UsuarioException;

	public DTRespuesta desbloquearUsuario(String mail, String clienteRestaurante) throws UsuarioException;
	
	public Map<String, Object> listarUsuariosRegistrados(int page, int size, int tipoUsuario);
	
	public Map<String, Object> buscarUsuario(int page, int size, int tipoUsuario, Integer antiguedadUsuario, String nombreUsuario, String sort, int order, Boolean bloqueado, Boolean desbloqueado);
	
	public Map<String, Object> listarRestaurantes(int page, int size, int estado);
	
	public DTRespuesta cambiarEstadoRestaurante(String varRestaurante, int estado) throws RestauranteException;
	
	public Map<String, Object> restaurantesConMasPedidos(int page, int size);	
	
	public Map<String, Object> topProductos(int page, int size);
	
	public List<DTTopCategoria> topCategorias();
}