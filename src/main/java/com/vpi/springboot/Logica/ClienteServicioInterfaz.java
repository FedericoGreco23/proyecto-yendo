package com.vpi.springboot.Logica;

import java.util.List;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.exception.UsuarioException;

public interface ClienteServicioInterfaz {
	public List<Direccion> getDireccionCliente(String mail) throws UsuarioException;
	
	public void altaCliente(Cliente usuario) throws UsuarioException, Exception;
	
	public List<Cliente> obtenerClientes();
	
	public void altaDireccion(Direccion direccion, String mail) throws UsuarioException;
	
	public void bajaCuenta(String mail) throws UsuarioException;
}