package com.vpi.springboot.Logica;


import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.RestauranteException;

public interface RestauranteServicioInterfaz {
	public void altaMenu(Producto menu, String varRestaurante) throws ProductoException, RestauranteException, Exception;

	public void bajaMenu(int id) throws ProductoException;

	void altaRestaurante(Restaurante rest) throws RestauranteException;

	public void modificarMenu(Producto menu) throws ProductoException;
}