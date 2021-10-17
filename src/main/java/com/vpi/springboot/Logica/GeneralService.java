package com.vpi.springboot.Logica;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

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
	private ProductoRepositorio proRepo;
	@Autowired
	private PromocionRepositorio promoRepo;
	@Autowired
	private MailService mailSender;
	@Autowired
	private CategoriaRepositorio catRepo;

	private static final int iterations = 20 * 1000;
	private static final int desiredKeyLen = 256;

	@Override
	public String iniciarSesion(String mail, String password) throws Exception {
		Optional<Cliente> optionalCliente = clienteRepo.findById(mail);
		if (optionalCliente.isPresent()) { // cliente
			Cliente cliente = optionalCliente.get();
			if (decodePass(cliente.getContrasenia(), password).equals(true))
				return "cliente";
			else
				throw new UsuarioException(UsuarioException.PassIncorrecta());
		} else {
			Optional<Restaurante> optionalRestaurante = resRepo.findById(mail);
			if (optionalRestaurante.isPresent()) { // restaurante
				Restaurante restaurante = optionalRestaurante.get();
				if (decodePass(restaurante.getContrasenia(), password).equals(true))
					return "restaurante";
				else
					throw new UsuarioException(UsuarioException.PassIncorrecta());
			} else {
				Optional<Administrador> optionalAdmin = adminRepo.findById(mail);
				if (optionalAdmin.isPresent()) { // administrador
					Administrador administrador = optionalAdmin.get();
					if (decodePass(administrador.getContrasenia(), password).equals(true))
						return "administrador";
					else
						throw new UsuarioException(UsuarioException.PassIncorrecta());
				} else {
					throw new UsuarioException(UsuarioException.NotFoundException(mail));
				}
			}
		}
	}

	// Compara las contraseñas
	private Boolean decodePass(String passGuardada, String passIngresada) throws Exception {
		String[] guardada = passGuardada.split("\\$");
		String ingresada = hash(passIngresada, Base64.getDecoder().decode(guardada[0]));
		if (ingresada.equals(guardada[1])) {
			return true;
		} else
			return false;
	}

	// METODO PARA HASHEAR CONTRASEÑA
	private static String hash(String password, byte[] salt) throws Exception {
		if (password == null || password.length() == 0)
			throw new IllegalArgumentException("Contraseña vacia.");
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		SecretKey key = f.generateSecret(new PBEKeySpec(password.toCharArray(), salt, iterations, desiredKeyLen));
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
//--------------------------------------------

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
	
	@Override
	public DTRestaurante getRestaurante(String mail) throws RestauranteException {
		Optional<Restaurante> restaurante;
		restaurante = resRepo.findById(mail);
		DTRestaurante DTRestaurante = new DTRestaurante(restaurante.get());
		
		return DTRestaurante;
	}
	
	
	
	
	
	@Override
	public Map<String, Object> listarRestaurantes(int page, int size, int horarioApertura) throws RestauranteException {
		Map<String, Object> response = new HashMap<>();
		List<DTListarRestaurante> DTListarRestaurantes = new ArrayList<DTListarRestaurante>();
		//List<DTRestaurante> DTRestaurantes = new ArrayList<DTRestaurante>();
		List<Restaurante> restaurantes = new ArrayList<Restaurante>();
		Pageable paging = PageRequest.of(page, size);
		Page<Restaurante> pageRestaurante;
		
		//pageRestaurante = resRepo.findByEstado(EnumEstadoRestaurante.ACEPTADO, paging);
		//Devuelve los restaurantes aceptados no bloqueados y activos
		pageRestaurante = resRepo.buscarRestaurantesPorEstadoNoBloqueadosYActivos(EnumEstadoRestaurante.ACEPTADO, paging);
		
		restaurantes = pageRestaurante.getContent();
		//Si el horarioApertura en el filtro es menor o igual que el horarioApertura del restaurante se muestra
		if (horarioApertura > 0) {
			for (Restaurante r : restaurantes) {
				if (r.getHorarioApertura().getHour() >= horarioApertura) {
					DTListarRestaurantes.add(new DTListarRestaurante(r));
				}
			}
		} else {
			for (Restaurante r : restaurantes) {
				DTListarRestaurantes.add(new DTListarRestaurante(r));
			}
		}
		response.put("currentPage", pageRestaurante.getNumber());
		response.put("totalItems", pageRestaurante.getTotalElements());
		response.put("restaurantes", DTListarRestaurantes);
		
		return response;
	}

	public Map<String, Object> listarMenusRestaurante(int page, int size, String mailRestaurante)
			throws RestauranteException {
		Optional<Restaurante> optionalRestaurante = resRepo.findById(mailRestaurante);
		if (!optionalRestaurante.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionNombre(mailRestaurante));
		}

		Restaurante restaurante = optionalRestaurante.get();
		Map<String, Object> response = new HashMap<>();
		Pageable paging = PageRequest.of(page, size);
		Page<Producto> pageProducto = proRepo.findAllByRestaurante(restaurante, paging);
		List<DTProducto> retorno = new ArrayList<DTProducto>();
		List<Producto> productos = pageProducto.getContent();

		response.put("currentPage", pageProducto.getNumber());
		response.put("totalItems", pageProducto.getTotalElements());

		for (Producto p : productos) {
			retorno.add(new DTProducto(p));
		}

		response.put("productos", retorno);
		return response;
	}

	public Map<String, Object> listarPromocionesRestaurante(int page, int size, String mailRestaurante)
			throws RestauranteException {
		Optional<Restaurante> optionalRestaurante = resRepo.findById(mailRestaurante);
		if (!optionalRestaurante.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionNombre(mailRestaurante));
		}

		Restaurante restaurante = optionalRestaurante.get();
		Map<String, Object> response = new HashMap<>();
		Pageable paging = PageRequest.of(page, size);
		Page<Promocion> promoPedido = promoRepo.findAllByRestaurante(restaurante, paging);
		List<Promocion> promociones = promoPedido.getContent();
		List<DTPromocion> retorno = new ArrayList<>();

		response.put("currentPage", promoPedido.getNumber());
		response.put("totalItems", promoPedido.getTotalElements());

		for (Promocion p : promociones) {
			retorno.add(new DTPromocion(p));
		}

		response.put("promociones", promociones);
		return response;
	}

	@Override
	public List<Categoria> listarCategorias() {
		return catRepo.findAll();
		
	}
}