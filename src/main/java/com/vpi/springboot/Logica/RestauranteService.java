package com.vpi.springboot.Logica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Repositorios.ClienteRepositorio;

@Service
public class RestauranteService implements RestauranteServicioInterfaz {

	@Autowired
	private ClienteRepositorio userRepo;
}
