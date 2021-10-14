package com.vpi.springboot.Logica;

import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.exception.RestauranteException;

public interface RestauranteServicioInterfaz {

	void altaRestaurante(Restaurante rest) throws RestauranteException;

}