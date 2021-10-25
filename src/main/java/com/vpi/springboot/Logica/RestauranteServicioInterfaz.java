package com.vpi.springboot.Logica;

import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.DTPedido;
import com.vpi.springboot.Modelo.dto.DTRespuesta;

import java.util.Map;

import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Promocion;
import com.vpi.springboot.exception.CategoriaException;
import com.vpi.springboot.exception.PedidoException;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.PromocionException;
import com.vpi.springboot.exception.RestauranteException;

public interface RestauranteServicioInterfaz {
	public DTRespuesta altaMenu(Producto menu, String varRestaurante)
			throws ProductoException, RestauranteException, Exception, CategoriaException;

	public DTRespuesta bajaMenu(int id) throws ProductoException;

	public DTRespuesta modificarMenu(Producto menu) throws ProductoException;

	public Map<String, Object> listarPedidos(int page, int size, String nombreRestaurante) throws RestauranteException;

	public DTRespuesta altaRestaurante(Restaurante rest) throws RestauranteException, CategoriaException;

	public DTRespuesta abrirRestaurante(String mail);

	public DTRespuesta cerrarRestaurante(String mail);

	public DTRespuesta confirmarPedido(int idPedido) throws PedidoException;
	
	public DTRespuesta modificarDescuentoProducto(int idProducto, int descuento) throws ProductoException;
	
	public DTRespuesta modificarPromocion(Promocion promo) throws PromocionException, ProductoException;
	
	public DTRespuesta bajaPromocion(int idPromo) throws PromocionException;
	
	public DTRespuesta rechazarPedido(int idPedido) throws PedidoException;
	
	public DTPedido buscarPedidoRecibido(int numeroPedido) throws PedidoException;
}
