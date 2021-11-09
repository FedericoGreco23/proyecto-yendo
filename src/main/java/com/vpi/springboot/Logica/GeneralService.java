package com.vpi.springboot.Logica;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.Administrador;
import com.vpi.springboot.Modelo.Categoria;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Promocion;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.TokenVerificacion;
import com.vpi.springboot.Modelo.dto.DTBuscarRestaurante;
import com.vpi.springboot.Modelo.dto.DTCategoriaProducto;
import com.vpi.springboot.Modelo.dto.DTListarRestaurante;
import com.vpi.springboot.Modelo.dto.DTProducto;
import com.vpi.springboot.Modelo.dto.DTPromocion;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Repositorios.AdministradorRepositorio;
import com.vpi.springboot.Repositorios.CategoriaRepositorio;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.DireccionRepositorio;
import com.vpi.springboot.Repositorios.ProductoRepositorio;
import com.vpi.springboot.Repositorios.PromocionRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.Repositorios.VerificacionRepositorio;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;

@Service
public class GeneralService implements GeneralServicioInterfaz {

	@Autowired
	private ClienteRepositorio clienteRepo;
	@Autowired
	private DireccionRepositorio dirRepo;
	@Autowired
	private RestauranteRepositorio resRepo;
	@Autowired
	private AdministradorRepositorio adminRepo;
	@Autowired
	private ProductoRepositorio proRepo;
	@Autowired
	private PromocionRepositorio promoRepo;
	@Autowired
	private CategoriaRepositorio catRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private VerificacionRepositorio tokenRepo;
	@Autowired
	private MailService mailSender;
	

/*	@Override
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
	public Boolean decodePass(String passGuardada, String passIngresada) throws Exception {
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
	}*/
//--------------------------------------------

	private String randomPass(int length) {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 18) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}

	@Override
	public DTRespuesta recuperarPassword(String mail) throws UsuarioException {
		String randomPass = randomPass(6);
		
		// Se tiene que ver cómo se genera la contraseña opcional
		String pass = passwordEncoder.encode(randomPass);
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
		if (to.contains("@")) {
			String topic = "Cambio de contraseña.";
			String body = mailSender.getPasswordReset(randomPass);
			try {
				mailSender.sendMail(to, body, topic);
			} catch (MessagingException e) {
				System.out.println("Error al mandar mail: " + e.getMessage());
			}
			return new DTRespuesta("Mail enviado con contraseña.");
		} else {
			return new DTRespuesta("Mail de usuario inválido.");
		}
	}

	@Override
	public DTRespuesta activarCuenta(String token) {
		TokenVerificacion verificacion = tokenRepo.findByToken(token);

		if (verificacion == null) {
			return new DTRespuesta("El token no pudo ser encontrado.");
		}
		Cliente cliente = verificacion.getUsuario();

		// Verificamos que el token del cliente siga vigente
		Calendar cal = Calendar.getInstance();
		if ((verificacion.getFechaExpiracion().getTime() - cal.getTime().getTime()) <= 0) {
			TokenVerificacion newToken = new TokenVerificacion(cliente);
			tokenRepo.save(newToken);

			String to = cliente.getMail();
			//TODO hacer página para activar cuenta
			String body = mailSender.getMailVerificacion("https://www.youtube.com/");
			String topic = "Verificación de usuario " + cliente.getNickname() + ".";
			try {
				mailSender.sendMail(to, body, topic);
			} catch (MessagingException e) {
				return new DTRespuesta("No se pudo mandar mail: " + e.getMessage());
			} finally {
				tokenRepo.delete(verificacion);
			}

			tokenRepo.delete(verificacion);
			return new DTRespuesta("El token expiró. Se ha reenviado un nuevo mail de verificación.");
		}

		cliente.setVerificado(true);
		clienteRepo.save(cliente);
		tokenRepo.delete(verificacion); //borramos la verificación vieja
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
	public List<DTBuscarRestaurante> buscarRestaurante(String texto, String nombreCategoria, int idDireccion)
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
			if (idDireccion == 0) {
				for (Restaurante restaurante : restaurantes) {
					DTBuscarRestaurantes.add(new DTBuscarRestaurante(restaurante.getNombre(), restaurante.getFoto(),
							restaurante.getDireccion(), restaurante.getMail()));
				}
			} else {
				//Calculo de distancia entre restaurante y cliente
				double lat1;
				double lng1;
				double lat2;
				double lng2; 
				//Obtengo los datos de latitud y longitud del cliente que recibo su idDireccion
				Optional<Direccion> optionalDireccion = dirRepo.findById(idDireccion);
				Direccion direccion = optionalDireccion.get();
				lat1 = direccion.getGeoLocalizacion().getLatitud();
				lng1 = direccion.getGeoLocalizacion().getLongitud();
				
				for (Restaurante restaurante : restaurantes) {
					lat2 = restaurante.getGeoLocalizacion().getLatitud();
					lng2 = restaurante.getGeoLocalizacion().getLongitud();
					
					double radioTierra = 6371;//en kilómetros  
			        double dLat = Math.toRadians(lat2 - lat1);  
			        double dLng = Math.toRadians(lng2 - lng1);  
			        double sindLat = Math.sin(dLat / 2);  
			        double sindLng = Math.sin(dLng / 2);  
			        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)  
			                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));  
			        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));  
			        double distancia = radioTierra * va2; 
					if (distancia < 15) {
						DTBuscarRestaurantes.add(new DTBuscarRestaurante(restaurante.getNombre(), restaurante.getFoto(),
								restaurante.getDireccion(), restaurante.getMail(), false));
					} else {
						DTBuscarRestaurantes.add(new DTBuscarRestaurante(restaurante.getNombre(), restaurante.getFoto(),
								restaurante.getDireccion(), restaurante.getMail(), true));
					}
				}
			}
		}
		
		List<DTBuscarRestaurante> DTBuscarRestaurantesSolo5 = new ArrayList<DTBuscarRestaurante>();
		if (DTBuscarRestaurantes != null) {
			int i = 0;
			if (DTBuscarRestaurantes.size() >= 5) {
				while (i < 5) {
					DTBuscarRestaurantesSolo5.add(DTBuscarRestaurantes.get(i));
					i = i + 1;
				}
			} else if (DTBuscarRestaurantes.size() < 5 && DTBuscarRestaurantes.size() > 0) {
				while (i < DTBuscarRestaurantes.size()) {
					DTBuscarRestaurantesSolo5.add(DTBuscarRestaurantes.get(i));
					i = i + 1;
				}
			}
		}
		//return DTBuscarRestaurantes;
		return DTBuscarRestaurantesSolo5;
	}

	@Override
	public Map<String, Object> listarRestaurantes(int page, int size, int horarioApertura, String nombre,
			String categoria, String sort, int order, int idDireccion) throws RestauranteException {
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
			if (!categoria.equalsIgnoreCase("")) {
				// Aplico nombre y categoria
				pageRestaurante = resRepo.listarRestauranteDesdeClientePorNombreYCategoria(nombre, categoria,
						EnumEstadoRestaurante.ACEPTADO, paging);
			} else {
				// Aplico solo nombre
				pageRestaurante = resRepo.buscarRestaurantesPorEstadoNoBloqueadosYActivosPorNombre(nombre,
						EnumEstadoRestaurante.ACEPTADO, paging);
			}

		} else if (!categoria.equalsIgnoreCase("")) {
			// Aplico solo categoria
			pageRestaurante = resRepo.listarRestauranteDesdeClientePorCategoria(categoria,
					EnumEstadoRestaurante.ACEPTADO, paging);
		} else {
			// No aplico ni categoria ni nombre
			pageRestaurante = resRepo.buscarRestaurantesPorEstadoNoBloqueadosYActivos(EnumEstadoRestaurante.ACEPTADO,
					paging);
		}
		

		restaurantes = pageRestaurante.getContent();
		int pagina = pageRestaurante.getNumber();
		long totalElements = pageRestaurante.getTotalElements();
		
		// Calculo de distancia entre restaurante y cliente
		if (idDireccion != 0) {
			double lat1;
			double lng1;
			double lat2;
			double lng2;
			//Obtengo los datos de latitud y longitud del cliente que recibo su idDireccion
			Optional<Direccion> optionalDireccion = dirRepo.findById(idDireccion);
			Direccion direccion = optionalDireccion.get();
			lat1 = direccion.getGeoLocalizacion().getLatitud();
			lng1 = direccion.getGeoLocalizacion().getLongitud();
			
			for (Restaurante r : restaurantes) {
				lat2 = r.getGeoLocalizacion().getLatitud();
				lng2 = r.getGeoLocalizacion().getLongitud();

				double radioTierra = 6371;//en kilómetros
		        double dLat = Math.toRadians(lat2 - lat1);
		        double dLng = Math.toRadians(lng2 - lng1);
		        double sindLat = Math.sin(dLat / 2);
		        double sindLng = Math.sin(dLng / 2);
		        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
		                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
		        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
		        double distancia = radioTierra * va2;
				if (distancia < 15) {

					if (horarioApertura > 0) {
						if (r.getHorarioApertura().getHour() >= horarioApertura) {
							DTListarRestaurantes.add(new DTListarRestaurante(r));
						} else {
							totalElements = totalElements - 1;
						}
					} else {
						DTListarRestaurantes.add(new DTListarRestaurante(r));
					}
				} else {
					totalElements = totalElements - 1;
				}
			}
		} else {
			// Si el horarioApertura en el filtro es menor o igual que el horarioApertura
			// del restaurante se muestra
			for (Restaurante r : restaurantes) {
				if (horarioApertura > 0) {
					if (r.getHorarioApertura().getHour() >= horarioApertura) {
						DTListarRestaurantes.add(new DTListarRestaurante(r));
					} else {
						totalElements = totalElements - 1;
					}
				} else {
					DTListarRestaurantes.add(new DTListarRestaurante(r));
				}
			}
		}
		
		response.put("currentPage", pagina);
		response.put("totalItems", totalElements);
		response.put("restaurantes", DTListarRestaurantes);

		return response;
	}

	@Override
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
			if (!(p instanceof Promocion)) {
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

	public List<DTPromocion> listarPromocionesRestaurante(String mailRestaurante) throws RestauranteException {
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
	public Map<String, Object> buscarMenusPromociones(String mailRestaurante, String producto)
			throws RestauranteException {
		Optional<Restaurante> optionalRestaurante = resRepo.findById(mailRestaurante);
		if (!optionalRestaurante.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionNombre(mailRestaurante));
		}
		Restaurante restaurante = optionalRestaurante.get();

		Map<String, Object> retorno = new HashMap<>();
		List<DTProducto> dtproductos = new ArrayList<>();
		List<DTPromocion> dtpromociones = new ArrayList<>();
		List<Producto> productos = proRepo.findAllByParametro(restaurante, producto);

		for (Producto p : productos) {
			if (p instanceof Promocion) {
				Promocion promocion = (Promocion) p;
				dtpromociones.add(new DTPromocion(promocion));
			} else {
				dtproductos.add(new DTProducto(p));
			}
		}

		retorno.put("productos", dtproductos);
		retorno.put("promociones", dtpromociones);

		return retorno;
	}
}