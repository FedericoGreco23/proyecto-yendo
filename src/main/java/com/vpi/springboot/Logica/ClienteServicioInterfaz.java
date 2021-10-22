package com.vpi.springboot.Logica;

import java.util.List;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.dto.DTBuscarRestaurante;
import com.vpi.springboot.Modelo.dto.DTCarrito;
import com.vpi.springboot.Modelo.dto.DTDireccion;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;
import com.vpi.springboot.exception.CarritoException;
import com.vpi.springboot.exception.DireccionException;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.ReclamoException;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;

public interface ClienteServicioInterfaz {
	public List<DTDireccion> getDireccionCliente(String mail) throws UsuarioException;
	
	public DTRespuesta altaCliente(Cliente usuario) throws UsuarioException, Exception;
	
	public List<Cliente> obtenerClientes();
	
	public DTRespuesta altaDireccion(DTDireccion direccion, String mail) throws UsuarioException;
	
	public DTRespuesta bajaCuenta(String mail) throws UsuarioException;

	public DTRespuesta modificarDireccion(int id, DTDireccion nueva, String mail) throws UsuarioException;

	public DTRespuesta eliminarDireccion(Integer id, String mail) throws UsuarioException;
	
	public DTRespuesta agregarACarrito(int producto, int cantidad, String mail, String mailRestaurante) throws ProductoException;
	
	public DTCarrito verCarrito(String mail);

	public DTRespuesta altaPedido(int idCarrito, EnumMetodoDePago pago, int idDireccion, String mail, String comentario)
			throws RestauranteException, CarritoException, DireccionException;

	public DTRespuesta altaReclamo(int idPedido, String mailCliente, String comentario) throws ReclamoException;
	
	public void eliminarProductoCarrito(int idProducto,int cantABorrar, String mail);
	
	public void eliminarCarrito(int idCarrito, String mail) throws CarritoException;

	
}