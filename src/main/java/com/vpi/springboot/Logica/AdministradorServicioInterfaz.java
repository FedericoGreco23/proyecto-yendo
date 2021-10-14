package com.vpi.springboot.Logica;

import java.util.List;
import java.util.Map;

import com.vpi.springboot.Modelo.Administrador;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.exception.AdministradorException;

public interface AdministradorServicioInterfaz {
	public void crearAdministrador(Administrador admin) throws AdministradorException;

	public void eliminarUsuario(String mail);

	public void bloquearUsuario(String mail, String clienteRestaurante);

	public void desbloquearUsuario(String mail, String clienteRestaurante);
	
	public Map<String, Object> listarRestaurantes(int page, int size, int estado);
}