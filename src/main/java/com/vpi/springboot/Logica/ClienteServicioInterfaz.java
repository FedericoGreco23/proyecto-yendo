package com.vpi.springboot.Logica;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.vpi.springboot.Modelo.Calificacion;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.dto.DTBuscarRestaurante;
import com.vpi.springboot.Modelo.dto.DTCalificacionRestaurante;
import com.vpi.springboot.Modelo.dto.DTCarrito;
import com.vpi.springboot.Modelo.dto.DTDireccion;
import com.vpi.springboot.Modelo.dto.DTPedido;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;
import com.vpi.springboot.exception.CarritoException;
import com.vpi.springboot.exception.DireccionException;
import com.vpi.springboot.exception.PedidoException;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.ReclamoException;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;

public interface ClienteServicioInterfaz {
	public List<DTDireccion> getDireccionCliente(String mail) throws UsuarioException;

	public DTRespuesta altaCliente(Cliente usuario) throws UsuarioException, Exception;

//	public List<Cliente> obtenerClientes();

	public DTRespuesta altaDireccion(DTDireccion direccion, String mail) throws UsuarioException;

	public DTRespuesta bajaCuenta(String mail) throws UsuarioException;

	public DTRespuesta modificarDireccion(int id, DTDireccion nueva, String mail) throws UsuarioException;

	public DTRespuesta eliminarDireccion(Integer id, String mail) throws UsuarioException;

	public DTRespuesta agregarACarrito(int producto, int cantidad, String mail, String mailRestaurante)
			throws ProductoException;

	public DTCarrito verCarrito(String mail);

	public DTPedido altaPedido(int idCarrito, EnumMetodoDePago pago, int idDireccion, String mail, String comentario)
			throws RestauranteException, CarritoException, DireccionException;

	public DTRespuesta altaReclamo(int idPedido, String mailCliente, String comentario) throws ReclamoException;

	public DTRespuesta eliminarProductoCarrito(int idProducto, int cantABorrar, String mail);

	public DTRespuesta eliminarCarrito(int idCarrito, String mail) throws CarritoException;

	public Map<String, Object> listarPedidos(int size, int page, String varRestaurante,
			String fecha, String sort, int order, String mailCliente) throws UsuarioException;

	public DTRespuesta calificarRestaurante(String mailCliente, String mailRestaurante, Calificacion calificacion)
			throws UsuarioException, RestauranteException;

	public DTRespuesta bajaCalificacionRestaurante(String mailCliente, String mailRestaurante)
			throws UsuarioException, RestauranteException;

	public Map<String, Object> listarReclamos(int size, int page, String restaurante, String estado, String fecha, String sort, int order, String mailCliente)
			throws UsuarioException;

	public DTPedido buscarPedidoRealizado(int numeroPedido) throws PedidoException;
	
	public Map<String, Object> consultarCalificacion(int page, int size, String sort, int order, String mailCliente);

	public Map<String, Object> listarRestaurantesPorZona(int page, int size, int horarioApertura, String nombre,
			String categoria, String sort, int order, int idDireccion) throws UsuarioException;
	
	public DTCalificacionRestaurante getCalificacionRestaurante(String mailCliente, String mailRestaurante) throws UsuarioException, RestauranteException;

	public DTRespuesta setToken(String token, String mailCliente);
}
