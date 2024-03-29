package com.vpi.springboot.Logica;

import java.util.List;
import java.util.Map;

import com.vpi.springboot.Modelo.Categoria;
import com.vpi.springboot.Modelo.dto.DTBuscarRestaurante;
import com.vpi.springboot.Modelo.dto.DTCategoriaProducto;
import com.vpi.springboot.Modelo.dto.DTPromocion;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.TokenException;
import com.vpi.springboot.exception.UsuarioException;

public interface GeneralServicioInterfaz {
//	public String iniciarSesion(String mail, String password) throws UsuarioException, Exception;

	public String recuperarPassword(String mail) throws UsuarioException;

	public DTRespuesta activarCuenta(String token) throws TokenException;
	
	public DTRestaurante getRestaurante(String mail) throws RestauranteException;
	
	public List<DTBuscarRestaurante> buscarRestaurante(String nombre, String nombreCategoria, int idDireccion) throws RestauranteException;
	
	public List<Categoria> listarCategorias();
	
	public Map<String, Object> listarRestaurantes(int page, int size, int horarioApertura, String nombre, String categoria, String sort, int order, int idDireccion) throws RestauranteException;

//	public Map<String, Object> listarMenusRestaurante(String attr, int order, int page, int size, String nombreRestaurante)
//			throws RestauranteException;
	
	public List<DTCategoriaProducto> listarMenus(String nombreRestaurante)
			throws RestauranteException;

	public List<DTPromocion> listarPromocionesRestaurante(String nombreRestaurante)
			throws RestauranteException;

	public Map<String, Object> buscarMenusPromociones(String mailRestaurante, String producto) throws RestauranteException;
}