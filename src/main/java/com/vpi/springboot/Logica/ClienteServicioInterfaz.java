package com.vpi.springboot.Logica;

import java.util.List;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.dto.DTCarrito;
import com.vpi.springboot.Modelo.dto.DTDireccion;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;
import com.vpi.springboot.exception.CarritoException;
import com.vpi.springboot.exception.DireccionException;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.ReclamoException;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;

public interface ClienteServicioInterfaz {
	public List<DTDireccion> getDireccionCliente(String mail) throws UsuarioException;
	
	public void altaCliente(Cliente usuario) throws UsuarioException, Exception;
	
	public List<Cliente> obtenerClientes();
	
	public void altaDireccion(DTDireccion direccion, String mail) throws UsuarioException;
	
	public void bajaCuenta(String mail) throws UsuarioException;

	void modificarDireccion(int id, DTDireccion nueva, String mail) throws UsuarioException;

	void eliminarDireccion(Integer id, String mail) throws UsuarioException;
	
	public void agregarACarrito(int producto, int cantidad, String mail, String mailRestaurante) throws ProductoException;
	
	public DTCarrito verCarrito(String mail);

	void altaPedido(int idCarrito, EnumMetodoDePago pago, int idDireccion, String mail, String comentario)
			throws RestauranteException, CarritoException, DireccionException;

	void altaReclamo(int idPedido, String mailCliente, String comentario) throws ReclamoException;

	
}