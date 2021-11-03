package com.vpi.springboot.Logica;

import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.DTCalificacionCliente;
import com.vpi.springboot.Modelo.dto.DTCalificacionRestaurante;
import com.vpi.springboot.Modelo.dto.DTPedido;
import com.vpi.springboot.Modelo.dto.DTPromocionConPrecio;
import com.vpi.springboot.Modelo.dto.DTRespuesta;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.vpi.springboot.Modelo.Calificacion;
import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Promocion;
import com.vpi.springboot.exception.CategoriaException;
import com.vpi.springboot.exception.PedidoException;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.PromocionException;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;

public interface RestauranteServicioInterfaz {
	public DTRespuesta altaMenu(Producto menu, String varRestaurante)
			throws ProductoException, RestauranteException, Exception, CategoriaException;

	public DTRespuesta bajaMenu(int id) throws ProductoException;

	public DTRespuesta modificarMenu(Producto menu) throws ProductoException;

	public Map<String, Object> listarPedidos(int page, int size, String mailRestaurante, String id, String fecha,
			String estado, String sort, int order) throws RestauranteException;

	public DTRespuesta altaRestaurante(Restaurante rest) throws RestauranteException, CategoriaException;

	public DTRespuesta abrirRestaurante(String mail);

	public DTRespuesta cerrarRestaurante(String mail);

	public DTRespuesta confirmarPedido(int idPedido) throws PedidoException;

	public DTRespuesta modificarDescuentoProducto(int idProducto, int descuento) throws ProductoException;

	public DTRespuesta modificarPromocion(Promocion promo) throws PromocionException, ProductoException;

	public DTRespuesta altaPromocion(DTPromocionConPrecio promocion, String mail)
			throws RestauranteException, PromocionException;

	public DTRespuesta bajaPromocion(int idPromo) throws PromocionException;

	public DTRespuesta rechazarPedido(int idPedido) throws PedidoException;

	public DTRespuesta calificarCliente(String mailCliente, String mailRestaurante, Calificacion calificacion)
			throws UsuarioException, RestauranteException;

	public DTRespuesta bajaCalificacionCliente(String mailCliente, String mailRestaurante)
			throws UsuarioException, RestauranteException;

	public Map<String, Object> listarReclamos(int page, int size, String cliente, String estado, String fecha, String sort, int order, String mailRestaurante) throws RestauranteException;

	public DTPedido buscarPedidoRecibido(int numeroPedido) throws PedidoException;

	public void cargarDatos();

	void corregirDatos();

	public Map<String, Object> consultarCalificacion(int page, int size, String sort, int order, String mailRestaurante) throws RestauranteException;

	public DTRespuesta registrarPago(int idPedido);
	
	public void ventaProducto(String idProducto, String cantidad, String categoria, String fecha);

	public DTRespuesta devolucionPedido(int idPedido);
	
	public DTRespuesta resolucionReclamo(int idReclamo, Boolean aceptoReclamo, String comentario) throws FileNotFoundException, IOException;
	
	public DTCalificacionCliente getCalificacionCliente(String mailCliente, String mailRestaurante) throws UsuarioException, RestauranteException;
	
	void cargarDatos2();
}
