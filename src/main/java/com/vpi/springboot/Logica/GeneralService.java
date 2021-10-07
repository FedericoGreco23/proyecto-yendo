package com.vpi.springboot.Logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.*;
import com.vpi.springboot.Modelo.dto.*;
import com.vpi.springboot.Repositorios.*;
import com.vpi.springboot.exception.*;

@Service
public class GeneralService implements GeneralServicioInterfaz {

	@Autowired
	private UsuarioRepositorio userRepo;
	
	@Override
	public String iniciarSesion(String mail, String password) throws UsuarioException {
		Optional<Usuario> optionalUser = userRepo.findById(mail);
		if (optionalUser.isPresent()) {
			Usuario user = optionalUser.get();
			//Se tiene que ver como se va a guardar la password
			if (user.getContrasenia().equals(password)) {
				if(user instanceof Administrador) {
					return "administrador";
				} else if (user instanceof Cliente) {					
					return "cliente";
				} else if (user instanceof Restaurante) {
					return "restaurante";
				} else 
					return null;
				
			} else 
				throw new UsuarioException(UsuarioException.PassIncorrecta());
		} else {
			throw new UsuarioException(UsuarioException.NotFoundException(mail));
		}
	}
}
