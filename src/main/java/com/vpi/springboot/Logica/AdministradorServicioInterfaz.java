package com.vpi.springboot.Logica;

import java.util.List;

import com.vpi.springboot.Modelo.Administrador;
import com.vpi.springboot.exception.AdministradorException;

public interface AdministradorServicioInterfaz {
	public void crearAdministrador(Administrador admin) throws AdministradorException;
}