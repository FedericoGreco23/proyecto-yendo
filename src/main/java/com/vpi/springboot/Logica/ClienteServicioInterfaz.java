package com.vpi.springboot.Logica;

import java.util.List;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Usuario;
import com.vpi.springboot.exception.UsuarioException;

public interface ClienteServicioInterfaz {

	public void createTodo(Cliente usuario);
	
	public List<Cliente> getAllClientes();

}