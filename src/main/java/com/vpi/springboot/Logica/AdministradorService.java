package com.vpi.springboot.Logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.*;
import com.vpi.springboot.Repositorios.AdministradorRepositorio;
import com.vpi.springboot.exception.UsuarioException;

@Service
public class AdministradorService implements AdministradorServicioInterfaz {

	@Autowired
	private AdministradorRepositorio userRepo;

}
