package com.vpi.springboot.Logica;

import java.util.List;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Usuario;

public interface UsuarioServicioInterfaz {

	public void createTodo(Cliente usuario);
	
	public List<Cliente> getAllClientes();
	

}