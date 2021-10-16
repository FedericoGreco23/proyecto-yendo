package com.vpi.springboot.Logica;

import java.util.List;
import java.util.Map;

import com.vpi.springboot.Modelo.Cliente;

public interface UsuarioServicioInterfaz {

	public void createTodo(Cliente usuario);
	
	public List<Cliente> getAllClientes();
	
}