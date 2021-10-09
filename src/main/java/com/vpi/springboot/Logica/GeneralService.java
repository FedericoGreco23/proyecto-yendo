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
	@Autowired
	private MailService mailSender;

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

	public void recuperarPassword(String mail) throws UsuarioException {
		Optional<Cliente> optionalCliente = clienteRepo.findById(mail);
		Optional<Restaurante> optionalRestaurante = resRepo.findById(mail);
		Optional<Administrador> optionalAdmin = adminRepo.findById(mail);
		String pass = "passTemporal";
		String to = "";

		// Se tiene que ver cómo se va a generar la nueva contraseña
		if (optionalCliente.isPresent()) {
			Cliente cliente = optionalCliente.get();
			cliente.setContrasenia(pass);
			clienteRepo.save(cliente);
			to = cliente.getMail();
		} else if (optionalRestaurante.isPresent()) {
			Restaurante restaurante = optionalRestaurante.get();
			restaurante.setContrasenia(pass);
			resRepo.save(restaurante);
			to = restaurante.getMail();
		} else if (optionalAdmin.isPresent()) {
			Administrador administrador = optionalAdmin.get();
			administrador.setContrasenia(pass);
			adminRepo.save(administrador);
			to = administrador.getMail();
		} else {
			throw new UsuarioException(UsuarioException.NotFoundException(mail));
		}

		// Enviamos el mail a la cuenta del usuario
		// Verificamos que tiene @ por si acaso
		if (to.contains("@")) {
			String topic = "Cambio de contraseña.";
			String body = "Su contraseña fue cambiada a " + pass + ".\n"
					+ "Por favor cambie su contraseña al ingresar a su cuenta.";
			mailSender.sendMail(to, body, topic);
		}
	}

	public void verificarMail(String mail) throws UsuarioException {
		Optional<Cliente> optionalCliente = clienteRepo.findById(mail);
		Optional<Restaurante> optionalRestaurante = resRepo.findById(mail);
		Optional<Administrador> optionalAdmin = adminRepo.findById(mail);
		String to = "";
		String tipo = "";

		// Se tiene que ver cómo se va a generar la nueva contraseña
		if (optionalCliente.isPresent()) {
			Cliente cliente = optionalCliente.get();
			to = cliente.getMail();
			tipo = "Cliente";
		} else if (optionalRestaurante.isPresent()) {
			Restaurante restaurante = optionalRestaurante.get();
			to = restaurante.getMail();
			tipo = "Restaurante";
		} else if (optionalAdmin.isPresent()) {
			Administrador administrador = optionalAdmin.get();
			to = administrador.getMail();
			tipo = "Administrador";
		} else {
			throw new UsuarioException(UsuarioException.NotFoundException(mail));
		}

		// Tenemos que ver de qué forma el usuario verifica su cuenta
		// Mandar el link no funciona
		String servicio = "localhost:8080/api/general/activar/?mail=" + mail + "&tipo=" + tipo;

		if (to.contains("@") && to.contains(".com")) {
			String topic = "Verificación de mail.";
			String body = "Para verificar su mail, por favor use el siguiente link: \n" + servicio;
			mailSender.sendMail(to, body, topic);
		}
	}

	// Para activar la cuenta del usuario al enviar el mail
	public void activarCuenta(String mail, String tipo) {
		if (tipo.equals("Cliente")) {
			Optional<Cliente> optionalCliente = clienteRepo.findById(mail);
			Cliente cliente = optionalCliente.get();
			cliente.setActivo(true);
			clienteRepo.save(cliente);
		} else if (tipo.equals("Restaurante")) {
			Optional<Restaurante> optionalRestaurante = resRepo.findById(mail);
			Restaurante res = optionalRestaurante.get();
			res.setActivo(true);
			resRepo.save(res);
		} else if (tipo.equals("Administrador")) {
			Optional<Administrador> optionalAdmin = adminRepo.findById(mail);
			Administrador admin = optionalAdmin.get();
			admin.setActivo(true);
			adminRepo.save(admin);
		}
	}

	public List<String> listarUsuariosRegistrados() {
		List<String> usuarios = new ArrayList<String>();

		List<Cliente> clientes = (List<Cliente>) clienteRepo.findAll();
		List<Administrador> admins = (List<Administrador>) adminRepo.findAll();
		List<Restaurante> restaurantes = (List<Restaurante>) resRepo.findAll();

		for (Administrador a : admins) {
			usuarios.add(a.getMail());
		}
		for (Restaurante r : restaurantes) {
			usuarios.add(r.getMail());
		}
		for (Cliente c : clientes) {
			usuarios.add(c.getMail());
		}

		return usuarios;
	}
}
