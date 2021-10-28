package com.vpi.springboot.Logica;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	@Autowired
	private PasswordEncoder passwordEncoder;

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

	public DTRespuesta recuperarPassword(String mail) throws UsuarioException {
		// Se tiene que ver cómo se genera la contraseña opcional
		String pass = passwordEncoder.encode("123456");
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
			String body = "Su contraseña fue cambiada a 123456.\n"
					+ "Por favor cambie su contraseña al ingresar a su cuenta.";
			mailSender.sendMail(to, body, topic);
			return new DTRespuesta("Mail enviado con contraseña.");
		} else {
			return new DTRespuesta("Mail de usuario inválido.");
		}

	}

	public DTRespuesta verificarMail(String mail) throws UsuarioException {
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
			return new DTRespuesta("Mail enviado con contraseña.");
		} else {
			return new DTRespuesta("Mail de usuario inválido.");
		}
	}

	// Para activar la cuenta del usuario al enviar el mail
	// 0 -> cliente
	// 1 -> restaurante
	// 2 -> administrador
	public DTRespuesta activarCuenta(String mail, int tipoUsuario) {
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

		return new DTRespuesta("Cuenta activada");
	}

	@Override
	public DTRestaurante getRestaurante(String mail) throws RestauranteException {
		Optional<Restaurante> restaurante;
		restaurante = resRepo.findById(mail);
		DTRestaurante DTRestaurante = new DTRestaurante(restaurante.get().getMail(), restaurante.get().getFoto(),
				restaurante.get().getNombre(), restaurante.get().getDireccion(),
				restaurante.get().getCalificacionPromedio(), restaurante.get().getHorarioApertura(),
				restaurante.get().getHorarioCierre(), restaurante.get().getTiempoEstimadoMinimo(),
				restaurante.get().getTiempoEstimadoMaximo(), restaurante.get().getCostoDeEnvio(),
				restaurante.get().getGeoLocalizacion(), restaurante.get().getDiasAbierto(),
				restaurante.get().getAbierto());
		// DTRestaurante DTRestaurante = new DTRestaurante(restaurante.get());

		return DTRestaurante;
	}

	@Override
	public List<DTBuscarRestaurante> buscarRestaurante(String texto, String nombreCategoria)
			throws RestauranteException {
		List<Restaurante> restaurantes = new ArrayList<Restaurante>();
		List<DTBuscarRestaurante> DTBuscarRestaurantes = new ArrayList<DTBuscarRestaurante>();
		if (!texto.equalsIgnoreCase("")) {
			if (!nombreCategoria.equalsIgnoreCase("")) {
				// Aplico los 2 filtros
				restaurantes = resRepo.buscarRestauranteDesdeClientePorNombreYCategoria(texto, nombreCategoria,
						EnumEstadoRestaurante.ACEPTADO);
			} else {
				// Aplico solo nombre
				restaurantes = resRepo.buscarRestauranteDesdeClientePorNombre(texto, EnumEstadoRestaurante.ACEPTADO);
			}
		} else if (!nombreCategoria.equalsIgnoreCase("")) {
			// Aplico solo categoria
			restaurantes = resRepo.buscarRestauranteDesdeClientePorCategoria(nombreCategoria,
					EnumEstadoRestaurante.ACEPTADO);
		}
		if (restaurantes != null) {
			for (Restaurante restaurante : restaurantes) {
				DTBuscarRestaurantes.add(new DTBuscarRestaurante(restaurante.getNombre(), restaurante.getFoto(),
						restaurante.getDireccion(), restaurante.getMail()));
			}
		}
		return DTBuscarRestaurantes;
	}

	@Override
	public Map<String, Object> listarRestaurantes(int page, int size, int horarioApertura, String nombre, String sort, int order) throws RestauranteException {
		Map<String, Object> response = new HashMap<>();
		List<DTListarRestaurante> DTListarRestaurantes = new ArrayList<DTListarRestaurante>();
		List<Restaurante> restaurantes = new ArrayList<Restaurante>();

		Sort sorting;
		Pageable paging;
		
		if (!sort.equalsIgnoreCase("")) {
			if (order == 1) {
				sorting = Sort.by(Sort.Order.desc(sort));
			} else {
				sorting = Sort.by(Sort.Order.asc(sort));
			}
			paging = PageRequest.of(page, size, sorting);
		} else {
			paging = PageRequest.of(page, size);
		}
		
		Page<Restaurante> pageRestaurante;

		// Devuelve los restaurantes aceptados no bloqueados y activos
		if (!nombre.equalsIgnoreCase("")) {
			//Aplico nombre
			pageRestaurante = resRepo.buscarRestaurantesPorEstadoNoBloqueadosYActivosPorNombre(nombre, EnumEstadoRestaurante.ACEPTADO, paging);
		} else {
			pageRestaurante = resRepo.buscarRestaurantesPorEstadoNoBloqueadosYActivos(EnumEstadoRestaurante.ACEPTADO, paging);
		}
		

		restaurantes = pageRestaurante.getContent();
		int pagina = pageRestaurante.getNumber();
		long totalElements = pageRestaurante.getTotalElements();
		// Si el horarioApertura en el filtro es menor o igual que el horarioApertura
		// del restaurante se muestra
		if (horarioApertura > 0) {
			for (Restaurante r : restaurantes) {
				if (r.getHorarioApertura().getHour() >= horarioApertura) {
					DTListarRestaurantes.add(new DTListarRestaurante(r));
				} else {
					totalElements = totalElements - 1;
				}
			}
		} else {
			for (Restaurante r : restaurantes) {
				DTListarRestaurantes.add(new DTListarRestaurante(r));
			}
		}
		//response.put("currentPage", pageRestaurante.getNumber());
		//response.put("totalItems", pageRestaurante.getTotalElements());
		response.put("currentPage", pagina);
		response.put("totalItems", totalElements);
		response.put("restaurantes", DTListarRestaurantes);

		return response;
	}

//	public Map<String, Object> listarMenusRestaurante(String attr, int order, int page, int size,
//			String mailRestaurante) throws RestauranteException {
//		Optional<Restaurante> optionalRestaurante = resRepo.findById(mailRestaurante);
//		if (!optionalRestaurante.isPresent()) {
//			throw new RestauranteException(RestauranteException.NotFoundExceptionNombre(mailRestaurante));
//		}
//
//		Pageable paging;
//		if (attr == null || attr.isEmpty())
//			paging = PageRequest.of(page, size);
//		else {
//			Sort sort;
//			if (order == 1)
//				sort = Sort.by(attr).descending();
//			else
//				sort = Sort.by(attr).ascending();
//			paging = PageRequest.of(page, size, sort);
//		}
//
//		Restaurante restaurante = optionalRestaurante.get();
//		Map<String, Object> response = new HashMap<>();
//		Page<Producto> pageProducto = proRepo.findAllByRestaurante(restaurante, paging);
//		List<DTProducto> retorno = new ArrayList<DTProducto>();
//		List<Producto> productos = pageProducto.getContent();
//
//		response.put("currentPage", pageProducto.getNumber());
//		response.put("totalItems", pageProducto.getTotalElements());
//
//		for (Producto p : productos) {
//			if (!(p.getClass() == Promocion.class)) {
//				retorno.add(new DTProducto(p));
//			}
//		}
//
//		response.put("productos", retorno);
//		return response;
//	}

	public List<DTCategoriaProducto> listarMenus(String mailRestaurante) throws RestauranteException {
		Optional<Restaurante> optionalRestaurante = resRepo.findById(mailRestaurante);
		if (!optionalRestaurante.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionNombre(mailRestaurante));
		}
		Restaurante restaurante = optionalRestaurante.get();

		List<DTCategoriaProducto> response = new ArrayList<>();
		Map<Categoria, List<Producto>> map = new HashMap<>();

		Categoria sinCategoria = new Categoria("sinCategoria");
		map.put(sinCategoria, new ArrayList<>());

		List<Producto> productos = proRepo.findAllByRestaurante(restaurante);
		for (Producto p : productos) {
			Categoria categoria = p.getCategoria();
			// Debería prevenir que retorne promociones
			if(!(p instanceof Promocion)) {
				if (categoria != null) {
					if (!map.containsKey(categoria)) {
						map.put(categoria, new ArrayList<>());
						map.get(categoria).add(p);
					} else
						map.get(categoria).add(p);
				} else
					map.get(sinCategoria).add(p);
			}
		}

		response = map.entrySet().stream().map(e -> new DTCategoriaProducto(e.getKey(), e.getValue()))
				.collect(Collectors.toList());

		for (int x = 0; x < response.size(); x++) {
			for (int i = x + 1; i < response.size(); i++) {
				// Ordeno por cantidad de productos
				if (response.get(x).getProductos().size() < response.get(i).getProductos().size()) {
					DTCategoriaProducto aux = new DTCategoriaProducto();
					aux.setCategoria(response.get(x).getCategoria());
					aux.setProductos(response.get(x).getProductos());
					response.get(x).setCategoria(response.get(i).getCategoria());
					response.get(x).setProductos(response.get(i).getProductos());
					response.get(i).setCategoria(aux.getCategoria());
					response.get(i).setProductos(aux.getProductos());
					// Cuando la cantidad de productos es igual
				} else if (response.get(x).getProductos().size() == response.get(i).getProductos().size()) {
					// Ordeno por órden alfabético
					if (response.get(x).getCategoria().getNombre()
							.compareTo(response.get(i).getCategoria().getNombre()) > 0) {
						DTCategoriaProducto aux = new DTCategoriaProducto();
						aux.setCategoria(response.get(x).getCategoria());
						aux.setProductos(response.get(x).getProductos());
						response.get(x).setCategoria(response.get(i).getCategoria());
						response.get(x).setProductos(response.get(i).getProductos());
						response.get(i).setCategoria(aux.getCategoria());
						response.get(i).setProductos(aux.getProductos());
					}
				}
			}
		}

		return response;
	}

	public List<DTPromocion> listarPromocionesRestaurante(String mailRestaurante)
			throws RestauranteException {
		Optional<Restaurante> optionalRestaurante = resRepo.findById(mailRestaurante);
		if (!optionalRestaurante.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionNombre(mailRestaurante));
		}
		Restaurante restaurante = optionalRestaurante.get();

		List<Promocion> promociones = promoRepo.findAllByRestaurante(restaurante);
		List<DTPromocion> response = new ArrayList<>();
		for (Promocion p : promociones) {
			response.add(new DTPromocion(p));
		}

		return response;
	}

	@Override
	public List<Categoria> listarCategorias() {
		return catRepo.findAll();

	}
	
	@Override
	public void registrarPagoEnEfectivo(String fecha, Double costoTotal, EnumMetodoDePago metodoDePago, String clienteMail, String restauranteMail) {
		MongoClientURI uri = new MongoClientURI("mongodb+srv://grupo1:grupo1@cluster0.l17sm.mongodb.net/prueba-concepto");
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase dataBase = mongoClient.getDatabase("prueba-concepto");
		MongoCollection<Document> collectionPedidos = dataBase.getCollection("pedidos");
		
		Document document = new Document();
		
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		document.put("Fecha", fecha);
		document.put("CostoTotal", costoTotal);
		document.put("MetodoDePago", metodoDePago.toString());
		document.put("ClienteMail", clienteMail);
		document.put("RestauranteMail", restauranteMail);
		collectionPedidos.insertOne(document);
	}
}