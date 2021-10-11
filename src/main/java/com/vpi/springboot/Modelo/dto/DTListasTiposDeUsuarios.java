package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.util.List;

public class DTListasTiposDeUsuarios implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<DTAdministrador> administradores;
	private List<DTRestaurante> restaurantes;
	private List<DTCliente> clientes;

	public DTListasTiposDeUsuarios() {
		super();
	}
	
	public DTListasTiposDeUsuarios(List<DTAdministrador> administradores, List<DTRestaurante> restaurantes, List<DTCliente> clientes) {
		super();
		this.administradores = administradores;
		this.restaurantes = restaurantes;
		this.clientes = clientes;
	}


//----------------------GETTERS Y SETTERS---------------------------------------------------------
	
	
	public List<DTAdministrador> getAdministradores() {
		return administradores;
	}

	public void setAdministradores(List<DTAdministrador> administradores) {
		this.administradores = administradores;
	}

	public List<DTRestaurante> getRestaurantes() {
		return restaurantes;
	}

	public void setRestaurantes(List<DTRestaurante> restaurantes) {
		this.restaurantes = restaurantes;
	}

	public List<DTCliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<DTCliente> clientes) {
		this.clientes = clientes;
	}

}
