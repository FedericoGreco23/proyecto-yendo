package com.vpi.springboot.Logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.*;
import com.vpi.springboot.Repositorios.AdministradorRepositorio;
import com.vpi.springboot.exception.AdministradorException;
import com.vpi.springboot.exception.UsuarioException;

@Service
public class AdministradorService implements AdministradorServicioInterfaz {

	@Autowired
	private AdministradorRepositorio repo;
	
	@Override
	public void crearAdministrador(Administrador admin) throws AdministradorException{
		Optional<Administrador> optionalUser = repo.findById(admin.getMail());
		if(optionalUser.isPresent()) {
			throw new AdministradorException(AdministradorException.AdministradorYaExiste());
		} else {
			String mail = admin.getMail();
			if(mail.contains("@") && mail.contains(".com"))
				repo.save(admin);
			else
				throw new AdministradorException("Tiene que introducir un mail válido.");
		}
	}	
}
