package com.vpi.springboot.Logica;

import com.vpi.springboot.Modelo.Restaurante;

import java.util.Map;

import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.exception.CategoriaException;
import com.vpi.springboot.exception.PedidoException;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.RestauranteException;

public interface RestauranteServicioInterfaz {
	public void altaMenu(Producto menu, String varRestaurante)
			throws ProductoException, RestauranteException, Exception, CategoriaException;

	public void bajaMenu(int id) throws ProductoException;

	public void modificarMenu(Producto menu) throws ProductoException;

	public Map<String, Object> listarPedidos(int page, int size, String nombreRestaurante) throws RestauranteException;

	public void altaRestaurante(Restaurante rest) throws RestauranteException, CategoriaException;
	public void abrirRestaurante(String mail);
	public void cerrarRestaurante(String mail);
	public void confirmarPedido(int idPedido) throws PedidoException;
}
