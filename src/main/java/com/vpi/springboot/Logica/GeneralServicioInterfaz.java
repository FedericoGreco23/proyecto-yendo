package com.vpi.springboot.Logica;

import java.util.List;
import java.util.Map;

import com.vpi.springboot.Modelo.Categoria;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.dto.DTCategoriaProducto;
import com.vpi.springboot.Modelo.dto.DTProducto;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.Modelo.dto.DTUsuario;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;

public interface GeneralServicioInterfaz {
	public String iniciarSesion(String mail, String password) throws UsuarioException, Exception;

	public void recuperarPassword(String mail) throws UsuarioException;

	public void verificarMail(String mail) throws UsuarioException;

	public void activarCuenta(String mail, int tipoUsuario);
	
	public DTRestaurante getRestaurante(String mail) throws RestauranteException;
	
	public List<Categoria> listarCategorias();
	
	public Map<String, Object> listarRestaurantes(int page, int size, int horarioApertura) throws RestauranteException;

	public Map<String, Object> listarMenusRestaurante(String attr, int order, int page, int size, String nombreRestaurante)
			throws RestauranteException;
	
	public List<DTCategoriaProducto> listarMenus(String nombreRestaurante)
			throws RestauranteException;

	public Map<String, Object> listarPromocionesRestaurante(int page, int size, String nombreRestaurante)
			throws RestauranteException;
}