package com.vpi.springboot.Logica;

import java.util.List;
import java.util.Map;

import com.vpi.springboot.Modelo.Administrador;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.exception.AdministradorException;
import com.vpi.springboot.exception.RestauranteException;

public interface AdministradorServicioInterfaz {
	public DTRespuesta crearAdministrador(Administrador admin) throws AdministradorException;

	public void eliminarUsuario(String mail);

	public void bloquearUsuario(String mail, String clienteRestaurante);

	public void desbloquearUsuario(String mail, String clienteRestaurante);
	
	public Map<String, Object> listarUsuariosRegistrados(int page, int size, int tipoUsuario);
	
	public Map<String, Object> buscarUsuario(int page, int size, int tipoUsuario, Integer antiguedadUsuario, String nombreUsuario);
	
	public Map<String, Object> listarRestaurantes(int page, int size, int estado);
	
	public void cambiarEstadoRestaurante(String varRestaurante, int estado) throws RestauranteException;
}