package com.vpi.springboot.Logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
		if (optionalCliente.isPresent()) { // cliente
			Cliente cliente = optionalCliente.get();
			if (cliente.getContrasenia().equals(password))
				return "cliente";
			else
				throw new UsuarioException(UsuarioException.PassIncorrecta());
		} else {
			Optional<Restaurante> optionalRestaurante = resRepo.findById(mail);
			if (optionalRestaurante.isPresent()) { // restaurante
				Restaurante restaurante = optionalRestaurante.get();
				if (restaurante.getContrasenia().equals(password))
					return "restaurante";
				else
					throw new UsuarioException(UsuarioException.PassIncorrecta());
			} else {
				Optional<Administrador> optionalAdmin = adminRepo.findById(mail);
				if (optionalAdmin.isPresent()) { // administrador
					Administrador administrador = optionalAdmin.get();
					if (administrador.getContrasenia().equals(password))
						return "administrador";
					else
						throw new UsuarioException(UsuarioException.PassIncorrecta());
				} else {
					throw new UsuarioException(UsuarioException.NotFoundException(mail));
				}
			}
		}
	}

	public void recuperarPassword(String mail) throws UsuarioException {
		// Se tiene que ver cómo se genera la contraseña opcional
		String pass = "passTemporal";
		String to = "";
		
		Optional<Cliente> optionalCliente = clienteRepo.findById(mail);
		if (optionalCliente.isPresent()) { // cliente
			Cliente cliente = optionalCliente.get();
			cliente.setContrasenia(pass);
			clienteRepo.save(cliente);
			to = cliente.getMail();
		} else {
			Optional<Restaurante> optionalRestaurante = resRepo.findById(mail);
			if (optionalRestaurante.isPresent()) { // restaurante
				Restaurante restaurante = optionalRestaurante.get();
				restaurante.setContrasenia(pass);
				resRepo.save(restaurante);
				to = restaurante.getMail();
			} else {
				Optional<Administrador> optionalAdmin = adminRepo.findById(mail);
				if (optionalAdmin.isPresent()) { // administrador
					Administrador administrador = optionalAdmin.get();
					administrador.setContrasenia(pass);
					adminRepo.save(administrador);
					to = administrador.getMail();
				} else {
					throw new UsuarioException(UsuarioException.NotFoundException(mail));
				}
			}
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
		String to = "";
		int tipo;

		Optional<Cliente> optionalCliente = clienteRepo.findById(mail);
		if (optionalCliente.isPresent()) { // cliente
			Cliente cliente = optionalCliente.get();
			to = cliente.getMail();
			tipo = 0;
		} else {
			Optional<Restaurante> optionalRestaurante = resRepo.findById(mail);
			if (optionalRestaurante.isPresent()) { // restaurante
				Restaurante restaurante = optionalRestaurante.get();
				to = restaurante.getMail();
				tipo = 1;
			} else {
				Optional<Administrador> optionalAdmin = adminRepo.findById(mail);
				if (optionalAdmin.isPresent()) { // administrador
					Administrador administrador = optionalAdmin.get();
					to = administrador.getMail();
					tipo = 2;
				} else {
					throw new UsuarioException(UsuarioException.NotFoundException(mail));
				}
			}
		}

		// TODO Tenemos que ver de qué forma el usuario verifica su cuenta
		// Mandar el link no funciona
		String servicio = "localhost:8080/api/general/activar/?mail=" + mail + "&tipo=" + tipo;

		if (to.contains("@") && to.contains(".com")) {
			String topic = "Verificación de mail.";
			String body = "Para verificar su mail, por favor use el siguiente link: \n" + servicio;
			mailSender.sendMail(to, body, topic);
		}
	}

	// Para activar la cuenta del usuario al enviar el mail
	// 0 -> cliente
	// 1 -> restaurante
	// 2 -> administrador
	public void activarCuenta(String mail, int tipoUsuario) {
		switch (tipoUsuario) {
		case 0:
			Optional<Cliente> optionalCliente = clienteRepo.findById(mail);
			Cliente cliente = optionalCliente.get();
			cliente.setActivo(true);
			clienteRepo.save(cliente);
			break;
		case 1:
			Optional<Restaurante> optionalRestaurante = resRepo.findById(mail);
			Restaurante res = optionalRestaurante.get();
			res.setActivo(true);
			resRepo.save(res);
			break;
		case 2:
			Optional<Administrador> optionalAdmin = adminRepo.findById(mail);
			Administrador admin = optionalAdmin.get();
			admin.setActivo(true);
			adminRepo.save(admin);
			break;
		}
	}

	// 0 -> cliente
	// 1 -> restaurante
	// 2 -> administrador
	public List<DTUsuario> listarUsuariosRegistrados(int page, int size, int tipoUsuario) {
		List<DTUsuario> usuarios = new ArrayList<DTUsuario>();
		Pageable paging = PageRequest.of(page, size);

		switch (tipoUsuario) {
		case 0:
			Page<Cliente> pageClientes = clienteRepo.findAll(paging);
			List<Cliente> clientes = pageClientes.getContent();
			for (Cliente c : clientes) {
				usuarios.add(new DTUsuario(c, "Cliente"));
			}
			break;
		case 1:
			Page<Restaurante> pageRestaurantes = resRepo.findAll(paging);
			List<Restaurante> restaurantes = pageRestaurantes.getContent();
			for (Restaurante c : restaurantes) {
				usuarios.add(new DTUsuario(c, "Restaurante"));
			}
			break;
		case 2:
			Page<Administrador> pageAdministradores = adminRepo.findAll(paging);
			List<Administrador> administrador = pageAdministradores.getContent();
			for (Administrador c : administrador) {
				usuarios.add(new DTUsuario(c, "Administrador"));
			}
			break;
		}

		return usuarios;
	}
}
