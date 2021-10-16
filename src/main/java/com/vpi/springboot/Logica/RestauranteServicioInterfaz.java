package com.vpi.springboot.Logica;

import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.exception.RestauranteException;

public interface RestauranteServicioInterfaz {

	public void altaRestaurante(Restaurante rest) throws RestauranteException;
	public void abrirRestaurante(String mail);
	public void cerrarRestaurante(String mail);
}