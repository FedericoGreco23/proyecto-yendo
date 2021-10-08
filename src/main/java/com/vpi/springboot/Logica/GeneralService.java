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
	private ClienteRepositorio clienteRepo;
	@Autowired
	private RestauranteRepositorio resRepo;
	@Autowired
	private AdministradorRepositorio adminRepo;

	@Override
	public String iniciarSesion(String mail, String password) throws UsuarioException {
		Optional<Cliente> optionalCliente = clienteRepo.findById(mail);
		Optional<Restaurante> optionalRestaurante = resRepo.findById(mail);
		Optional<Administrador> optionalAdmin = adminRepo.findById(mail);

		// Se tiene que ver como se va a guardar la password
		if (optionalCliente.isPresent()) {
			Cliente cliente = optionalCliente.get();
			if (cliente.getContrasenia().equals(password))
				return "cliente";
			else
				throw new UsuarioException(UsuarioException.PassIncorrecta());
		} else if (optionalRestaurante.isPresent()) {
			Restaurante restaurante = optionalRestaurante.get();
			if (restaurante.getContrasenia().equals(password))
				return "restaurante";
			else
				throw new UsuarioException(UsuarioException.PassIncorrecta());
		} else if (optionalAdmin.isPresent()) {
			Administrador administrador = optionalAdmin.get();
			if (administrador.getContrasenia().equals(password))
				return "administrador";
			else
				throw new UsuarioException(UsuarioException.PassIncorrecta());
		} else {
			throw new UsuarioException(UsuarioException.NotFoundException(mail));
		}
	}

	public List<String> listarUsuariosRegistrados() {
		List<String> usuarios = new ArrayList<String>();
		
		List<Cliente> clientes = (List<Cliente>) clienteRepo.findAll();
		List<Administrador> admins = (List<Administrador>) adminRepo.findAll();
		List<Restaurante> restaurantes = (List<Restaurante>) resRepo.findAll();
		
		for(Administrador a : admins) {
			usuarios.add(a.getMail());
		}
		for(Restaurante r : restaurantes) {
			usuarios.add(r.getMail());
		}
		for(Cliente c : clientes) {
			usuarios.add(c.getMail());
		}
		
		return usuarios;
	}
}
