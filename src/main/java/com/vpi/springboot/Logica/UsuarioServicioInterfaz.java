package com.vpi.springboot.Logica;

import java.util.List;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Usuario;
import com.vpi.springboot.Modelo.dto.DTUsuario;
import com.vpi.springboot.exception.UsuarioException;

public interface UsuarioServicioInterfaz {

	public void createTodo(Cliente usuario);
	
	public List<Cliente> getAllClientes();
	
	public List<DTUsuario> buscarUsuario(int page, int size, int tipoUsuario, Integer antiguedadUsuario, String nombreUsuario);
	
}