package com.vpi.springboot.Logica;

import java.util.List;
import java.util.Map;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.dto.DTCarrito;
import com.vpi.springboot.Modelo.dto.DTDireccion;
import com.vpi.springboot.Modelo.dto.DTProductoCarrito;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;

public interface ClienteServicioInterfaz {
	public List<DTDireccion> getDireccionCliente(String mail) throws UsuarioException;
	
	public void altaCliente(Cliente usuario) throws UsuarioException, Exception;
	
	public Map<String, Object> listarRestaurantes(int page, int size, int horarioApertura) throws RestauranteException;
	
	public List<Cliente> obtenerClientes();
	
	public void altaDireccion(DTDireccion direccion, String mail) throws UsuarioException;
	
	public void bajaCuenta(String mail) throws UsuarioException;

	void modificarDireccion(int id, DTDireccion nueva, String mail) throws UsuarioException;

	void eliminarDireccion(Integer id, String mail) throws UsuarioException;
	
	public void agregarACarrito(DTProductoCarrito c, String mail) throws ProductoException;
	
	public DTCarrito verCarrito(String mail);
}