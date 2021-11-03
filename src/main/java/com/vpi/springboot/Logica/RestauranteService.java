package com.vpi.springboot.Logica;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vpi.springboot.IdCompuestas.CalificacionClienteId;
import com.vpi.springboot.Modelo.Administrador;
import com.vpi.springboot.Modelo.Calificacion;
import com.vpi.springboot.Modelo.CalificacionCliente;
import com.vpi.springboot.Modelo.CalificacionRestaurante;
import com.vpi.springboot.Modelo.Carrito;
import com.vpi.springboot.Modelo.Categoria;
import com.vpi.springboot.Modelo.GeoLocalizacion;
import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Promocion;
import com.vpi.springboot.Modelo.Reclamo;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.DTCalificacionCliente;
import com.vpi.springboot.Modelo.dto.DTCalificacionRestaurante;
import com.vpi.springboot.Modelo.dto.DTCarrito;
import com.vpi.springboot.Modelo.dto.DTPedido;
import com.vpi.springboot.Modelo.dto.DTProductoIdCantidad;
import com.vpi.springboot.Modelo.dto.DTPromocionConPrecio;
import com.vpi.springboot.Modelo.dto.DTReclamo;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTRestaurantePedido;
import com.vpi.springboot.Modelo.dto.EnumEstadoPedido;
import com.vpi.springboot.Modelo.dto.EnumEstadoReclamo;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;
import com.vpi.springboot.Repositorios.CalificacionClienteRepositorio;
import com.vpi.springboot.Repositorios.CalificacionRestauranteRepositorio;
import com.vpi.springboot.Repositorios.CategoriaRepositorio;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.GeoLocalizacionRepositorio;
import com.vpi.springboot.Repositorios.MongoRepositorioCarrito;
import com.vpi.springboot.Repositorios.PedidoRepositorio;
import com.vpi.springboot.Repositorios.ProductoRepositorio;
import com.vpi.springboot.Repositorios.PromocionRepositorio;
import com.vpi.springboot.Repositorios.ReclamoRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.Repositorios.mongo.RestaurantePedidosRepositorio;
import com.vpi.springboot.exception.AdministradorException;
import com.vpi.springboot.exception.CategoriaException;
import com.vpi.springboot.exception.PedidoException;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.PromocionException;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;

@Service
@EnableScheduling
public class RestauranteService implements RestauranteServicioInterfaz {

	@Autowired
	private RestauranteRepositorio restauranteRepo;
	@Autowired
	private PedidoRepositorio pedidoRepo;
	@Autowired
	private PromocionRepositorio promoRepo;
	@Autowired
	private CategoriaRepositorio catRepo;
	@Autowired
	private ProductoRepositorio proRepo;
	@Autowired
	private RestauranteRepositorio resRepo;
	@Autowired
	private MongoRepositorioCarrito mongoRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ProductoRepositorio productoRepo;
	@Autowired
	private ClienteRepositorio clienteRepo;
	@Autowired
	private CalificacionClienteRepositorio calClienteRepo;
	@Autowired
	private CalificacionRestauranteRepositorio calRestauranteRepo;
	@Autowired
	private ReclamoRepositorio recRepo;
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	private ClienteRepositorio userRepo;
	@Autowired
	private AdministradorService administradorService;
	@Autowired
	private RestaurantePedidosRepositorio resPedRepo;
	@Autowired
	private ClienteService clienteService;

	private DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");;

	/*
	 * private DTCarrito verCarrito(int id) { Optional<Carrito> optionalCarrito =
	 * mongoRepo.findById(id); if (optionalCarrito.isPresent()) { Carrito carrito =
	 * optionalCarrito.get(); DTCarrito dt = new DTCarrito(carrito.getId(),
	 * carrito.getProductoCarrito(), carrito.getMailRestaurante(),
	 * carrito.getCostoEnvio()); return dt; }
	 * 
	 * return null; }
	 */

	@Override
	public DTRespuesta altaRestaurante(Restaurante rest) throws RestauranteException, CategoriaException {

		// Seccion verificar que nombreRestaurante o restauranteMail no exista ya
		Optional<Restaurante> busquedaMail = restauranteRepo.findById(rest.getMail());
		Restaurante busquedaNombre = null;
		busquedaNombre = restauranteRepo.existeRestauranteNombre(rest.getNombre());
		if (busquedaMail.isPresent()) {
			throw new RestauranteException(RestauranteException.RestauranteYaExiste(rest.getMail()));
		} else if (busquedaNombre != null) {
			throw new RestauranteException(RestauranteException.RestauranteYaExiste(rest.getNombre()));
		}

		if (rest.getCategorias().size() > 0) {
			for (Categoria c : rest.getCategorias()) {
				Optional<Categoria> optionalCategoria = catRepo.findById(c.getNombre());
				if (!optionalCategoria.isPresent())
					throw new CategoriaException(CategoriaException.NotFoundException(c.getNombre()));
				else {
					// Se añaden categorías al restaurante
					Categoria categoria = optionalCategoria.get();
					rest.addCategoria(categoria);
				}
			}
		}

		rest.setActivo(true);
		rest.setBloqueado(false);
		rest.setCalificacionPromedio(5.0f);
		rest.setFechaCreacion(LocalDate.now());
		rest.setEstado(EnumEstadoRestaurante.EN_ESPERA);
		rest.setFechaApertura(null);
//		rest.setProductos(null);
//		rest.setReclamos(null);
//		rest.setPedidos(null);
		rest.setAbierto(false);
		rest.setContrasenia(passwordEncoder.encode(rest.getContrasenia()));

		restauranteRepo.save(rest);
		return new DTRespuesta("Restaurante " + rest.getNombre() + " dado de alta correctamente.");
	}

	@Override
	public DTRespuesta altaMenu(Producto menu, String varRestaurante)
			throws ProductoException, RestauranteException, CategoriaException, Exception {
		Optional<Restaurante> optionalRestaurante = restauranteRepo.findById(varRestaurante);
		if (!optionalRestaurante.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionNombre(varRestaurante));
		}
		Restaurante restaurante = optionalRestaurante.get();

		if (menu.getCategoria() != null) {
			Optional<Categoria> optionalCategoria = catRepo.findById(menu.getCategoria().getNombre());
			if (!optionalCategoria.isPresent())
				throw new CategoriaException(CategoriaException.NotFoundException(menu.getCategoria().getNombre()));
			else {
				Categoria categoria = optionalCategoria.get();
				restaurante.addCategoria(categoria);
				menu.setCategoria(categoria);
			}
		}

		// La query tira una excepción si retorna más de una tupla
		try {
			if (proRepo.findByNombre(menu.getNombre(), restaurante) == null) {
				menu.setRestaurante(restaurante);
				restaurante.addProducto(menu);
				restaurante.addCategoria(menu.getCategoria());
				restauranteRepo.save(restaurante);
				return new DTRespuesta("Menú agregado correctamente.");
			} else {
				throw new ProductoException(ProductoException.ProductoYaExiste(menu.getNombre()));
			}
		} catch (Exception e) {
			throw new ProductoException(ProductoException.ProductoYaExiste(menu.getNombre()));
		}
	}

	@Override
	public DTRespuesta bajaMenu(int id) throws ProductoException {
//		Boolean menuTomado = false;

		Optional<Producto> optionalProducto = proRepo.findById(id);
		if (optionalProducto.isPresent()) {
			// Busca entre todos los pedidos del restaurante si queda uno con el menú
			Producto producto = optionalProducto.get();
//			List<Pedido> pedidos = producto.getRestaurante().getPedidos();
//			for(Pedido p : pedidos) {
//				DTCarrito dtcarrito = verCarrito(p.getCarrito());
//				for(DTProductoCarrito c : dtcarrito.getDtProductoCarritoList()) {
//					if(c.getProducto().getNombre() == producto.getNombre()) {
//						menuTomado = true;
//					}
//				}
//			}
//
//			if(menuTomado) {
//				producto.setActivo(false);
//				proRepo.save(producto);
//			} else
//				proRepo.delete(producto);

			producto.setActivo(false);
			proRepo.save(producto);
			return new DTRespuesta("Menú dado de baja correctamente.");
		} else {
			throw new ProductoException(ProductoException.NotFoundExceptionId(id));
		}
	}

	@Override
	public DTRespuesta modificarMenu(Producto menu) throws ProductoException {
		if (menu.getRestaurante() != null) {
			throw new ProductoException("No puede cambiar el restaurante de un menú.");
		}

		Optional<Producto> optionalProducto = proRepo.findById(menu.getId());
		if (!optionalProducto.isPresent()) {
			throw new ProductoException(ProductoException.NotFoundExceptionId(menu.getId()));
		}

		Producto producto = optionalProducto.get();

		producto.setNombre(menu.getNombre());
		producto.setDescripcion(menu.getDescripcion());
		producto.setPrecio(menu.getPrecio());
		producto.setFoto(menu.getFoto());
		producto.setDescuento(menu.getDescuento());
		producto.setActivo(menu.isActivo());

		proRepo.save(producto);
		return new DTRespuesta("Menú modificado correctamente.");
	}

	@Override
	public Map<String, Object> listarPedidos(int page, int size, String mailRestaurante, String id, String fecha,
			String estado, String sort, int order) throws RestauranteException {
		Optional<Restaurante> optionalRestaurante = restauranteRepo.findById(mailRestaurante);
		if (!optionalRestaurante.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionNombre(mailRestaurante));
		}
		Restaurante restaurante = optionalRestaurante.get();

		Map<String, Object> response = new HashMap<>();
		List<DTPedido> retorno = new ArrayList<>();
		Pageable paging;

		if (!sort.equalsIgnoreCase("")) {
			if (order == 1)
				paging = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sort)));
			else
				paging = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sort)));
		} else {
			paging = PageRequest.of(page, size);
		}

		Page<Pedido> pagePedido;
		List<Pedido> pedidos = new ArrayList<>();

		if (!id.equalsIgnoreCase("") || !fecha.equalsIgnoreCase("") || !estado.equalsIgnoreCase("")) {
			// cliente/id + fecha + estado
			if (!id.equalsIgnoreCase("") && !fecha.equalsIgnoreCase("") && !estado.equalsIgnoreCase("")) {
				EnumEstadoPedido estadoPedido = EnumEstadoPedido.valueOf(estado.toUpperCase());
				LocalDate ld = LocalDate.parse(fecha, DATEFORMATTER);
				LocalDateTime dateI = LocalDateTime.of(ld, LocalTime.of(00, 01));
				LocalDateTime dateF = LocalDateTime.of(ld, LocalTime.of(23, 59));

				try { // id puede ser parseado a int
					int idPedido = Integer.parseInt(id);
					pagePedido = pedidoRepo.findByIdFechaEstado(idPedido, dateI, dateF, estadoPedido, restaurante,
							paging);
				} catch (Exception e) { // caso contrario
					pagePedido = pedidoRepo.findByClienteFechaEstado(id, dateI, dateF, estadoPedido, restaurante,
							paging);
				}
			}

			// cliente/id + fecha
			else if (!id.equalsIgnoreCase("") && !fecha.equalsIgnoreCase("")) {
				LocalDate ld = LocalDate.parse(fecha, DATEFORMATTER);
				LocalDateTime dateI = LocalDateTime.of(ld, LocalTime.of(00, 01));
				LocalDateTime dateF = LocalDateTime.of(ld, LocalTime.of(23, 59));

				try { // id puede ser parseado a int
					int idPedido = Integer.parseInt(id);
					pagePedido = pedidoRepo.findByIdFecha(idPedido, dateI, dateF, restaurante, paging);
				} catch (Exception e) { // caso contrario
					pagePedido = pedidoRepo.findByClienteFecha(id, dateI, dateF, restaurante, paging);
				}
			}

			// cliente/id + estado
			else if (!id.equalsIgnoreCase("") && !estado.equalsIgnoreCase("")) {
				EnumEstadoPedido estadoPedido = EnumEstadoPedido.valueOf(estado.toUpperCase());

				try { // id puede ser parseado a int
					int idPedido = Integer.parseInt(id);
					pagePedido = pedidoRepo.findByIdEstado(idPedido, estadoPedido, restaurante, paging);
				} catch (Exception e) { // caso contrario
					pagePedido = pedidoRepo.findByClienteEstado(id, estadoPedido, restaurante, paging);
				}
			}

			// fecha + estado
			else if (!fecha.equalsIgnoreCase("") && !estado.equalsIgnoreCase("")) {
				EnumEstadoPedido estadoPedido = EnumEstadoPedido.valueOf(estado.toUpperCase());
				LocalDate ld = LocalDate.parse(fecha, DATEFORMATTER);
				LocalDateTime dateI = LocalDateTime.of(ld, LocalTime.of(00, 01));
				LocalDateTime dateF = LocalDateTime.of(ld, LocalTime.of(23, 59));

				pagePedido = pedidoRepo.findByFechaEstado(dateI, dateF, estadoPedido, restaurante, paging);
			}

			// cliente/id
			else if (!id.equalsIgnoreCase("")) {
				try { // id puede ser parseado a int
					int idPedido = Integer.parseInt(id);
					pagePedido = pedidoRepo.findById(idPedido, restaurante, paging);
				} catch (Exception e) { // caso contrario
					pagePedido = pedidoRepo.findByCliente(id, restaurante, paging);
				}
			}

			// fecha
			else if (!fecha.equalsIgnoreCase("")) {
				LocalDate ld = LocalDate.parse(fecha, DATEFORMATTER);
				LocalDateTime dateI = LocalDateTime.of(ld, LocalTime.of(00, 01));
				LocalDateTime dateF = LocalDateTime.of(ld, LocalTime.of(23, 59));

				pagePedido = pedidoRepo.findByFecha(dateI, dateF, restaurante, paging);
			}

			// estado
			else if (!estado.equalsIgnoreCase("")) {
				EnumEstadoPedido estadoPedido = EnumEstadoPedido.valueOf(estado.toUpperCase());
				pagePedido = pedidoRepo.findByEstado(estadoPedido, restaurante, paging);
			} else
				pagePedido = pedidoRepo.findAllByRestaurante(restaurante, paging);

		} else {
			pagePedido = pedidoRepo.findAllByRestaurante(restaurante, paging);
		}

		pedidos = pagePedido.getContent();
		response.put("currentPage", pagePedido.getNumber());
		response.put("totalItems", pagePedido.getTotalElements());

		for (Pedido p : pedidos) {
			Optional<Carrito> optionalCarrito = mongoRepo.findById(p.getCarrito());
			if (optionalCarrito.isPresent())
				retorno.add(new DTPedido(p, new DTCarrito(optionalCarrito.get())));
			else
				retorno.add(new DTPedido(p));
		}

		response.put("pedidos", retorno);
		return response;
	}

	@Override
	public DTRespuesta abrirRestaurante(String mail) {
		Optional<Restaurante> restaurante = restauranteRepo.findById(mail);
		restaurante.get().setAbierto(true);
		restauranteRepo.save(restaurante.get());
		return new DTRespuesta("Restaurante abierto.");
	}

	@Override
	public DTRespuesta cerrarRestaurante(String mail) {
		Optional<Restaurante> restaurante = restauranteRepo.findById(mail);
		restaurante.get().setAbierto(false);
		restauranteRepo.save(restaurante.get());
		return new DTRespuesta("Restaurante cerrado.");
	}

	@Override
	public DTRespuesta confirmarPedido(int idPedido) throws PedidoException {
		Optional<Pedido> optionalPedido = pedidoRepo.findById(idPedido);
		if (optionalPedido.isPresent()) {
			Pedido pedido = optionalPedido.get();
			pedido.setEstadoPedido(EnumEstadoPedido.ACEPTADO);
			pedidoRepo.save(pedido);

			// se notifica a cliente
			String base64EncodedEmail = Base64.getEncoder()
					.encodeToString(pedido.getCliente().getMail().getBytes(StandardCharsets.UTF_8));

			/*
			 * DTPedidoParaAprobar pedidoDT = new DTPedidoParaAprobar(pedido);
			 * pedidoDT.setComentario(pedido.getComentario());
			 * pedidoDT.setDireccion(pedido.getDireccion()); pedidoDT.setCliente(new
			 * DTCliente(pedido.getCliente()));
			 */

			simpMessagingTemplate.convertAndSend("/topic/" + base64EncodedEmail,
					"Su pedido ha sido aceptado y se está siendo preparado");

			// fin notificacion

			return new DTRespuesta("Pedido " + idPedido + " confirmado.");
		} else {
			throw new PedidoException(PedidoException.NotFoundExceptionId(idPedido));
		}
	}

	@Override
	public DTRespuesta modificarDescuentoProducto(int idProducto, int descuento) throws ProductoException {
		Optional<Producto> optionalProducto = proRepo.findById(idProducto);
		if (optionalProducto.isPresent()) {
			// Busca entre todos los pedidos del restaurante si queda uno con el menú
			Producto producto = optionalProducto.get();
			producto.setDescuento(descuento);
			proRepo.save(producto);
			return new DTRespuesta("Menú modificado correctamente.");
		} else {
			throw new ProductoException(ProductoException.NotFoundExceptionId(idProducto));
		}
	}

	@Override
	public DTRespuesta modificarPromocion(Promocion promo) throws PromocionException, ProductoException {
		if (promo.getRestaurante() != null) {
			throw new PromocionException("No puede cambiar el restaurante de un menú.");
		}

		Optional<Promocion> optionalPromocion = promoRepo.findById(promo.getId());
		if (!optionalPromocion.isPresent()) {
			throw new PromocionException(PromocionException.NotFoundExceptionId(promo.getId()));
		}

		Promocion promocion = optionalPromocion.get();

		promocion.setNombre(promo.getNombre());
		promocion.setDescripcion(promo.getDescripcion());
		promocion.setPrecio(promo.getPrecio());
		promocion.setFoto(promo.getFoto());
		promocion.setDescuento(promo.getDescuento());
		promocion.setActivo(promo.isActivo());

		List<Producto> productos = new ArrayList<>();
		if (promocion.getProductos().size() > 0) {
			for (Producto p : promocion.getProductos()) {
				Optional<Producto> optionalProducto = proRepo.findById(p.getId());
				if (!optionalProducto.isPresent())
					throw new ProductoException(ProductoException.NotFoundExceptionId(p.getId()));
				else
					productos.add(optionalProducto.get());
			}
		}

		promocion.setProductos(productos);

		promoRepo.save(promocion);
		return new DTRespuesta("Promoción modificada correctamente.");
	}

	@Override
	public DTRespuesta bajaPromocion(int idPromo) throws PromocionException {
		Optional<Promocion> optionalPromocion = promoRepo.findById(idPromo);
		if (optionalPromocion.isPresent()) {
			Promocion promocion = optionalPromocion.get();

			promocion.setActivo(false);
			promoRepo.save(promocion);
			return new DTRespuesta("Promocion dada de baja correctamente.");
		} else {
			throw new PromocionException(PromocionException.NotFoundExceptionId(idPromo));
		}
	}

	@Override
	public DTRespuesta altaPromocion(DTPromocionConPrecio promocion, String mail)
			throws RestauranteException, PromocionException {
		Optional<Restaurante> restauranteOp = restauranteRepo.findById(mail);
		if (!restauranteOp.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionMail(mail));
		}

		try {
			Restaurante restaurante = restauranteOp.get();
			List<Promocion> promocionesList = promoRepo.findAllByRestauranteSimple(restaurante);
			// Producto productosRestaurante= productoRepo.findByIdAndRest(mail,
			// restaurante);

			Promocion promocionNueva = new Promocion(promocion.getNombre(), promocion.getDescripcion(),
					promocion.getPrecio(), promocion.getFoto(), promocion.getDescuento(), true);
			promocionNueva.setRestaurante(restaurante);

			for (DTProductoIdCantidad entry : promocion.getProductos()) {

				Producto prod = productoRepo.findByIdAndRest(entry.getId(), restaurante);

				for (int i = 0; i < entry.getCantidad(); i++) {
					List<Producto> prodList = promocionNueva.getProductos();
					prodList.add(prod);
					promocionNueva.setProductos(prodList);
				}
			}
			promoRepo.save(promocionNueva);

			return new DTRespuesta("Promocion ingresada con éxito.");

		} catch (Exception e) {
			throw new PromocionException(PromocionException.NotFoundException());
		}
	}

	@Override
	public DTRespuesta rechazarPedido(int idPedido) throws PedidoException {
		Optional<Pedido> optionalPedido = pedidoRepo.findById(idPedido);
		if (optionalPedido.isPresent()) {
			Pedido pedido = optionalPedido.get();
			pedido.setEstadoPedido(EnumEstadoPedido.RECHAZADO);
			pedidoRepo.save(pedido);

			// se notifica a cliente
			String base64EncodedEmail = Base64.getEncoder()
					.encodeToString(pedido.getCliente().getMail().getBytes(StandardCharsets.UTF_8));

			/*
			 * DTPedidoParaAprobar pedidoDT = new DTPedidoParaAprobar(pedido);
			 * pedidoDT.setComentario(pedido.getComentario());
			 * pedidoDT.setDireccion(pedido.getDireccion()); pedidoDT.setCliente(new
			 * DTCliente(pedido.getCliente()));
			 */

			simpMessagingTemplate.convertAndSend("/topic/" + base64EncodedEmail, "Su pedido ha sido rechazado");

			// fin notificacion

			return new DTRespuesta("Pedido " + idPedido + " rechazado.");

		} else {
			throw new PedidoException(PedidoException.NotFoundExceptionId(idPedido));
		}
	}

	@Override
	public DTRespuesta calificarCliente(String mailCliente, String mailRestaurante, Calificacion calificacion)
			throws UsuarioException, RestauranteException {
		Optional<Restaurante> optionalRestaurante = restauranteRepo.findById(mailRestaurante);
		if (!optionalRestaurante.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionMail(mailRestaurante));
		}
		Restaurante restaurante = optionalRestaurante.get();

		Optional<Cliente> optionalCliente = clienteRepo.findById(mailCliente);
		if (!optionalCliente.isPresent())
			throw new UsuarioException(UsuarioException.NotFoundException(mailCliente));
		Cliente cliente = optionalCliente.get();

		List<Pedido> pedidos = pedidoRepo.findByClienteRestaurante(cliente, restaurante);
		if (pedidos.size() == 0)
			throw new UsuarioException(UsuarioException.SinPedido(mailRestaurante));

		if(calificacion.getPuntaje() > 5)
			calificacion.setPuntaje(5);
		else if (calificacion.getPuntaje() < 1)
			calificacion.setPuntaje(1);
		
		calificacion.setFecha(LocalDateTime.now());
		CalificacionCliente calCliente = new CalificacionCliente(calificacion, restaurante, cliente);
		calClienteRepo.save(calCliente);

		// Calculamos la calificación del cliente y la guardamos
		List<CalificacionCliente> calificaciones = calClienteRepo.findByCliente(cliente);
		float avg = 0f;
		for (CalificacionCliente c : calificaciones) {
			avg += c.getPuntaje();
		}
		avg /= calificaciones.size();
		cliente.setCalificacionPromedio(avg);
		clienteRepo.save(cliente);

		return new DTRespuesta("Cliente " + mailCliente + " calificado correctamente");
	}

	@Override
	public DTRespuesta bajaCalificacionCliente(String mailCliente, String mailRestaurante)
			throws UsuarioException, RestauranteException {
		Optional<Restaurante> optionalRestaurante = restauranteRepo.findById(mailRestaurante);
		if (!optionalRestaurante.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionMail(mailRestaurante));
		}
		Restaurante restaurante = optionalRestaurante.get();

		Optional<Cliente> optionalCliente = clienteRepo.findById(mailCliente);
		if (!optionalCliente.isPresent())
			throw new UsuarioException(UsuarioException.NotFoundException(mailCliente));
		Cliente cliente = optionalCliente.get();

		List<Pedido> pedidos = pedidoRepo.findByClienteRestaurante(cliente, restaurante);
		if (pedidos.size() == 0)
			throw new UsuarioException(UsuarioException.SinPedido(mailRestaurante));

		Optional<CalificacionCliente> optionalCalificacion = calClienteRepo
				.findById(new CalificacionClienteId(cliente, restaurante));
		if (!optionalCalificacion.isPresent())
			throw new RestauranteException(RestauranteException.SinCalificacion(mailCliente));
		CalificacionCliente calCliente = optionalCalificacion.get();
		calClienteRepo.delete(calCliente);

		// Calculamos la calificación del cliente y la guardamos
		List<CalificacionCliente> calificaciones = calClienteRepo.findByCliente(cliente);
		float avg = 0f;
		if (calificaciones.size() > 0) {
			for (CalificacionCliente c : calificaciones) {
				avg += c.getPuntaje();
			}
			avg /= calificaciones.size();
		} else
			avg = 5.0f;

		cliente.setCalificacionPromedio(avg);
		clienteRepo.save(cliente);

		return new DTRespuesta("Calificación de cliente " + mailCliente + " eliminada correctamente");
	}

	public Map<String, Object> listarReclamos(int page, int size, String cliente, String estado, String fecha,
			String sort, int order, String mailRestaurante) throws RestauranteException {
		Optional<Restaurante> optionalRestaurante = restauranteRepo.findById(mailRestaurante);
		if (!optionalRestaurante.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionMail(mailRestaurante));
		}
		Restaurante restaurante = optionalRestaurante.get();

		Map<String, Object> response = new HashMap<>();
		List<DTReclamo> retorno = new ArrayList<>();
		Pageable paging;

		if (!sort.equalsIgnoreCase("")) {
			if (order == 1)
				paging = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sort)));
			else
				paging = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sort)));
		} else {
			paging = PageRequest.of(page, size);
		}

		Page<Reclamo> pageReclamo;
		List<Reclamo> reclamos = new ArrayList<>();

		if (!cliente.equalsIgnoreCase("") || !estado.equalsIgnoreCase("") || !fecha.equalsIgnoreCase("")) {
			// cliente + estado + fecha
			if (!cliente.equalsIgnoreCase("") && !estado.equalsIgnoreCase("") && !fecha.equalsIgnoreCase("")) {
				EnumEstadoReclamo estadoReclamo = EnumEstadoReclamo.valueOf(estado.toUpperCase());
				LocalDate ld = LocalDate.parse(fecha, DATEFORMATTER);
				LocalDateTime dateI = LocalDateTime.of(ld, LocalTime.of(00, 00));
				LocalDateTime dateF = LocalDateTime.of(ld, LocalTime.of(23, 59));
				pageReclamo = recRepo.findAllByRestauranteClienteEstadoFecha(cliente, restaurante, estadoReclamo, dateI,
						dateF, paging);
			} 
			
			// cliente + estado
			else if (!cliente.equalsIgnoreCase("") && !estado.equalsIgnoreCase("")) {
				EnumEstadoReclamo estadoReclamo = EnumEstadoReclamo.valueOf(estado.toUpperCase());
				pageReclamo = recRepo.findAllByRestauranteClienteEstado(cliente, restaurante, estadoReclamo, paging);
			} 
			
			// cliente + fecha
			else if (!cliente.equalsIgnoreCase("") && !fecha.equalsIgnoreCase("")) {
				LocalDate ld = LocalDate.parse(fecha, DATEFORMATTER);
				LocalDateTime dateI = LocalDateTime.of(ld, LocalTime.of(00, 00));
				LocalDateTime dateF = LocalDateTime.of(ld, LocalTime.of(23, 59));
				pageReclamo = recRepo.findAllByRestauranteClienteFecha(cliente, restaurante, dateI, dateF, paging);
			} 
			
			// estado + fecha
			else if(!estado.equalsIgnoreCase("") && !fecha.equalsIgnoreCase("")) {
				System.out.println("estado y fecha");
				EnumEstadoReclamo estadoReclamo = EnumEstadoReclamo.valueOf(estado.toUpperCase());
				LocalDate ld = LocalDate.parse(fecha, DATEFORMATTER);
				LocalDateTime dateI = LocalDateTime.of(ld, LocalTime.of(00, 00));
				LocalDateTime dateF = LocalDateTime.of(ld, LocalTime.of(23, 59));
				System.out.println(dateI);
				System.out.println(dateF);
				pageReclamo = recRepo.findAllByRestauranteEstadoFecha(restaurante, estadoReclamo, dateI,
						dateF, paging);
			}
			
			// cliente
			else if (!cliente.equalsIgnoreCase("")) {
				pageReclamo = recRepo.findAllByRestauranteCliente(cliente, restaurante, paging);
			}

			// estado
			else if (!estado.equalsIgnoreCase("")) {
				EnumEstadoReclamo estadoReclamo = EnumEstadoReclamo.valueOf(estado.toUpperCase());
				pageReclamo = recRepo.findAllByRestauranteEstado(estadoReclamo, restaurante, paging);
			}

			// fecha
			else if (!fecha.equalsIgnoreCase("")) {
				LocalDate ld = LocalDate.parse(fecha, DATEFORMATTER);
				LocalDateTime dateI = LocalDateTime.of(ld, LocalTime.of(00, 00));
				LocalDateTime dateF = LocalDateTime.of(ld, LocalTime.of(23, 59));
				pageReclamo = recRepo.findAllByRestauranteFecha(dateI, dateF, restaurante, paging);
			} else { //genérico
				pageReclamo = recRepo.findAllByRestaurante(restaurante, paging);
			}
		} else { //genérico
			pageReclamo = recRepo.findAllByRestaurante(restaurante, paging);
		}

		reclamos = pageReclamo.getContent();
		for (Reclamo r : reclamos) {
			retorno.add(new DTReclamo(r));
		}

		response.put("currentPage", pageReclamo.getTotalPages());
		response.put("totalItems", pageReclamo.getTotalElements());
		response.put("reclamos", retorno);

		return response;
	}

	@Override
	public DTPedido buscarPedidoRecibido(int numeroPedido) throws PedidoException {
		DTPedido DTpedido = null;

		if (numeroPedido > 0) {
			Pedido pedido = pedidoRepo.buscarPedidoPorNumero(numeroPedido);
			if (pedido == null) {
				throw new PedidoException(PedidoException.NotFoundExceptionId(numeroPedido));
			} else {
				DTpedido = new DTPedido(pedido);
			}
		} else {
			throw new PedidoException(PedidoException.NotValidId());
		}

		return DTpedido;
	}

	@Scheduled(cron = "*/59 */5 * * * *") // 1 vez cada 5 minutos
	public DTRespuesta guaradarEnMongo() {
		List<Object[]> lista = restauranteRepo.buscarRestaurantesConMasPedidos();
		// Map<String, Integer> restpedidos = new HashMap<String, Integer>();
		// List<DTRestaurantePedido> restaurantesPedidos = new
		// ArrayList<DTRestaurantePedido>();
		Optional<DTRestaurantePedido> optionalDt;
		Optional<Restaurante> optionalRes;
		Restaurante res;
		DTRestaurantePedido dt;
		for (Object[] object : lista) {
			optionalRes = restauranteRepo.findById((String) object[0]);
			res = optionalRes.get();
			optionalDt = resPedRepo.findById((String) object[0]);
			if (optionalDt.isPresent()) {
				dt = optionalDt.get();
				BigInteger big1 = (BigInteger) object[1];
				dt.setCantPedidos(big1.intValue());
				resPedRepo.save(dt);
			} else {
				BigInteger big = (BigInteger) object[1];
				dt = new DTRestaurantePedido((String) object[0], big.intValue());
				dt.setNombre(res.getNombre());
				resPedRepo.save(dt);
			}
		}

		// resPedRepo.saveAll(restaurantesPedidos);
		return new DTRespuesta("Base de datos actualizada");
	}


	// Uso esta funcion tanto para consultarCalificacionRestaurante como
	// clienteConsultaCalificacionRestaurante
	@Override
	public Map<String, Object> consultarCalificacion(int page, int size, String sort, int order, String mailRestaurante)
			throws RestauranteException {
		Map<String, Object> response = new HashMap<>();
		List<DTCalificacionRestaurante> DTCalificacionesRestaurante = new ArrayList<DTCalificacionRestaurante>();
		List<CalificacionRestaurante> calificacionRestaurantes = new ArrayList<CalificacionRestaurante>();

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

		Optional<Restaurante> optionalRestaurante = restauranteRepo.findById(mailRestaurante);
		Restaurante restaurante = optionalRestaurante.get();

		Page<CalificacionRestaurante> pageCalificacion;
		pageCalificacion = calRestauranteRepo.consultarCalificacion(restaurante, paging);
		calificacionRestaurantes = pageCalificacion.getContent();
		int pagina = pageCalificacion.getNumber();
		long totalElements = pageCalificacion.getTotalElements();

		for (CalificacionRestaurante c : calificacionRestaurantes) {
			DTCalificacionesRestaurante.add(new DTCalificacionRestaurante(c));
		}

		response.put("currentPage", pagina);
		response.put("totalItems", totalElements);
		response.put("calificacionGlobal", restaurante.getCalificacionPromedio());
		response.put("restaurantes", DTCalificacionesRestaurante);

		return response;
	}

	@Override
	public DTRespuesta registrarPago(int idPedido) {
		MongoClientURI uri = new MongoClientURI(
				"mongodb+srv://grupo1:grupo1@cluster0.l17sm.mongodb.net/prueba-concepto");
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase dataBase = mongoClient.getDatabase("prueba-concepto");
		MongoCollection<Document> collectionCarrito = dataBase.getCollection("carrito");

		Optional<Pedido> optionalPedido = pedidoRepo.findById(idPedido);
		Pedido pedido = optionalPedido.get();
		if (pedido.getMetodoDePago().equals(EnumMetodoDePago.EFECTIVO)) {
			pedido.setPago(true);
			pedidoRepo.save(pedido);
		}

		// Document buscado = (Document) collectionPedidos.find(new Document("_id",
		// idPedido)).first();
		// String idProducto = buscado.getString("productoCarrito");

		// Obtengo la informacion de los productos pedidos de un carrito
		Document carritoBuscado = collectionCarrito.find(new Document("_id", pedido.getCarrito())).first();
		List<BasicDBObject> listaDBObject = (List<BasicDBObject>) carritoBuscado.get("productoCarrito");

		Document carritoDocument;
		Document productoDocument;

		String idProducto;
		String cantidad;
		String categoria;
		String fecha;

		for (Object obj : listaDBObject) {
			carritoDocument = (Document) obj;
			cantidad = carritoDocument.get("cantidad").toString();
			productoDocument = (Document) carritoDocument.get("producto");
			idProducto = productoDocument.get("_id").toString();
			categoria = productoDocument.getString("categoria");
			fecha = pedido.getFecha().toString();

			ventaProducto(idProducto, cantidad, categoria, fecha);
		}

		return new DTRespuesta("Pago registrado con éxito.");
	}

	@Override
	public void ventaProducto(String idProducto, String cantidad, String categoria, String fecha) {
		MongoClientURI uri = new MongoClientURI(
				"mongodb+srv://grupo1:grupo1@cluster0.l17sm.mongodb.net/prueba-concepto");
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase dataBase = mongoClient.getDatabase("prueba-concepto");
		MongoCollection<Document> collectionPedidos = dataBase.getCollection("pedidos");

		Document document = new Document();

		document.put("idProducto", idProducto);
		document.put("cantidad", cantidad);
		document.put("categoria", categoria);
		document.put("fecha", fecha);
		collectionPedidos.insertOne(document);
	}

	@Override
	public DTRespuesta devolucionPedido(int idPedido) {
		Optional<Pedido> optionalPedido = pedidoRepo.findById(idPedido);
		Pedido pedido = optionalPedido.get();
		Pedido devolucion = new Pedido(pedido.getFecha(), pedido.getCostoTotal() * -1, pedido.getEstadoPedido(),
				pedido.getMetodoDePago(), pedido.getCarrito(), pedido.getDireccion(), pedido.getRestaurante(),
				pedido.getCliente(), pedido.getComentario(), pedido.getPago());

		pedidoRepo.save(devolucion);
		return new DTRespuesta("Devolucion registrada con éxito.");
	}
	
	@Override
	public DTRespuesta resolucionReclamo(int idReclamo, Boolean aceptoReclamo) throws IOException {
		Optional<Reclamo> optionalReclamo = recRepo.findById(idReclamo);
		Reclamo reclamo = optionalReclamo.get();
		String token = reclamo.getPedido().getCliente().getTokenDispositivo();
		String restaurante = reclamo.getRestaurante().getNombre();
		Message message;
		String appAndroid = "android";
		FileInputStream serviceAccount = new FileInputStream("src/main/java/Resource/yendo-5c371-firebase-adminsdk-rczst-500b815097.json");

		FirebaseOptions options = new FirebaseOptions.Builder()
		  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
		  .build();

		FirebaseApp.initializeApp(options);
		
		Map<String, String> map = new HashMap<>();
		
		map.put("llave", "valor");
		
		if (aceptoReclamo) {
			//Se acepta el reclamo
			//Se llama devolucion pedido
			//Se envia notificacion con mensaje
			
			reclamo.setEstado(EnumEstadoReclamo.ACEPTADO);
			//Envio notificacion al mobile
			
			Notification notification = Notification.builder()
					.setTitle("Resolucion de reclamo")
					.setBody("Su reclamo ha sido aceptado por el restaurante " + restaurante)
					.build();
			message = Message.builder()
	                  .putAllData(map)
	                  .putData("mensaje","Nuevo mensaje de sus reclamos")
	                  .setToken(token) // deviceId
	                  .setNotification(notification)
	                  .build();
			try {
				//DUDA QUE ES appAndroid
				FirebaseMessaging.getInstance(FirebaseApp.getInstance(appAndroid)).send(message);
				System.out.println("PRUEBA SE ENVIA NOTIFICACION");
			} catch (FirebaseMessagingException e) {
				System.out.println(e.getStackTrace());
				System.out.println("Mensaje de error: " + e.getMessagingErrorCode());
			}
			devolucionPedido(reclamo.getPedido().getId());
		} else {
			//No se acepta el reclamo
			//Se envia notificacion con mensaje
			
			reclamo.setEstado(EnumEstadoReclamo.RECHAZADO);
			//Envio notificacion al mobile
			Notification notification = Notification.builder()
					.setTitle("Resolucion de reclamo")
					.setBody("Su reclamo ha sido rechazado por el restaurante " + restaurante)
					.build();
			message = Message.builder()
	                  .putAllData(map)
	                  .putData("mensaje","Nuevo mensaje de sus reclamos")
	                  .setToken(token) // deviceId
	                  .setNotification(notification)
	                  .build();
			try {
				//DUDA QUE ES appAndroid
				FirebaseMessaging.getInstance(FirebaseApp.getInstance(appAndroid)).send(message);
				System.out.println("PRUEBA SE ENVIA NOTIFICACION");
			} catch (FirebaseMessagingException e) {
				System.out.println(e.getStackTrace());
				System.out.println("Mensaje de error: " + e.getMessagingErrorCode());
			}
		}
		return new DTRespuesta("Se envio la notificacion mobile.");
	}

	@Override
	public DTCalificacionCliente getCalificacionCliente(String mailCliente, String mailRestaurante)
			throws UsuarioException, RestauranteException {
		Optional<Cliente> optionalCliente = clienteRepo.findById(mailCliente);
		if (!optionalCliente.isPresent())
			throw new UsuarioException(UsuarioException.NotFoundException(mailCliente));
		Cliente cliente = optionalCliente.get();

		Optional<Restaurante> optionalRestaurante = restauranteRepo.findById(mailRestaurante);
		if (!optionalRestaurante.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionMail(mailRestaurante));
		}
		Restaurante restaurante = optionalRestaurante.get();


		CalificacionCliente calCliente = calClienteRepo.findByClienteRestaurante(cliente, restaurante);

		if (calCliente == null)
			return null;
//			throw new UsuarioException(RestauranteException.SinCalificacion(mailCliente));
		return new DTCalificacionCliente(calCliente);
	}
}
