package com.vpi.springboot.Logica;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.vpi.springboot.Modelo.Categoria;
import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.dto.DTBuscarRestaurante;
import com.vpi.springboot.Modelo.dto.DTCategoriaProducto;
import com.vpi.springboot.Modelo.dto.DTProducto;
import com.vpi.springboot.Modelo.dto.DTPromocion;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.Modelo.dto.DTRestaurantePedido;
import com.vpi.springboot.Modelo.dto.DTUsuario;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;

public interface GeneralServicioInterfaz {
	public String iniciarSesion(String mail, String password) throws UsuarioException, Exception;

	public DTRespuesta recuperarPassword(String mail) throws UsuarioException;

	public DTRespuesta verificarMail(String mail) throws UsuarioException;

	public DTRespuesta activarCuenta(String mail, int tipoUsuario);
	
	public DTRestaurante getRestaurante(String mail) throws RestauranteException;
	
	public List<DTBuscarRestaurante> buscarRestaurante(String nombre, String nombreCategoria) throws RestauranteException;
	
	public List<Categoria> listarCategorias();
	
	public Map<String, Object> listarRestaurantes(int page, int size, int horarioApertura, String nombre, String categoria, String sort, int order) throws RestauranteException;

//	public Map<String, Object> listarMenusRestaurante(String attr, int order, int page, int size, String nombreRestaurante)
//			throws RestauranteException;
	
	public List<DTCategoriaProducto> listarMenus(String nombreRestaurante)
			throws RestauranteException;

	public List<DTPromocion> listarPromocionesRestaurante(String nombreRestaurante)
			throws RestauranteException;

	public Map<String, Object> buscarMenusPromociones(String mailRestaurante, String producto) throws RestauranteException;
	
	public DTRespuesta registrarPago(int idPedido);
	
	public void ventaProducto(String idProducto, String cantidad, String categoria, String fecha);

	public DTRespuesta devolucionPedido(Pedido pedido);
}