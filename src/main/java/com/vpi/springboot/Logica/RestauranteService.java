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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

import org.assertj.core.error.OptionalDoubleShouldHaveValueCloseToOffset;
import org.bson.Document;
import org.bson.conversions.Bson;
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
import com.mongodb.client.FindIterable;
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
import com.vpi.springboot.Modelo.dto.BalanceByFechaDTO;
import com.vpi.springboot.Modelo.dto.BalanceVentaDTO;
import com.vpi.springboot.Modelo.dto.DTCalificacionCliente;
import com.vpi.springboot.Modelo.dto.DTCalificacionRestaurante;
import com.vpi.springboot.Modelo.dto.DTCarrito;
import com.vpi.springboot.Modelo.dto.DTNotificacionSoket;
import com.vpi.springboot.Modelo.dto.DTPedido;
import com.vpi.springboot.Modelo.dto.DTProducto;
import com.vpi.springboot.Modelo.dto.DTProductoCarrito;
import com.vpi.springboot.Modelo.dto.DTProductoIdCantidad;
import com.vpi.springboot.Modelo.dto.DTProductoVendido;
import com.vpi.springboot.Modelo.dto.DTPromocionConPrecio;
import com.vpi.springboot.Modelo.dto.DTReclamo;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTRestaurantePedido;
import com.vpi.springboot.Modelo.dto.EnumEstadoPedido;
import com.vpi.springboot.Modelo.dto.EnumEstadoReclamo;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;
import com.vpi.springboot.Modelo.dto.FechaidPedidoMontoDTO;
import com.vpi.springboot.Modelo.dto.IdPedidoMontoDTO;
import com.vpi.springboot.Modelo.dto.PedidoMonto;
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
import com.vpi.springboot.Repositorios.mongo.BalanceVentasRepositorio;
import com.vpi.springboot.Repositorios.mongo.ProductosVendidosRepositorio;
import com.vpi.springboot.Repositorios.mongo.RestaurantePedidosRepositorio;
import com.vpi.springboot.exception.AdministradorException;
import com.vpi.springboot.exception.CategoriaException;
import com.vpi.springboot.exception.PedidoException;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.PromocionException;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;

import BeanFirebase.SingletonFirebase;

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
	@Autowired
	private BalanceVentasRepositorio balanceVentasRepo;
	@Autowired
	private MailService mailSender;
	@Autowired
	private ProductosVendidosRepositorio productosVendidosRepo;

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

	/*
	 * @PostConstruct public void init() throws IOException { FileInputStream
	 * serviceAccount = new FileInputStream(
	 * "src/main/java/Resource/yendo-5c371-firebase-adminsdk-rczst-500b815097.json")
	 * ; System.out.println("SE CARGA FIREBASE EN RESTAURANTE"); FirebaseOptions
	 * options = new FirebaseOptions.Builder()
	 * .setCredentials(GoogleCredentials.fromStream(serviceAccount)) .build();
	 *
	 * FirebaseApp.initializeApp(options); }
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

			// TESTEO ENVÍO MAIL
//			List<DTProductoCarrito> productos = new ArrayList<>();
//			System.out.println("entra FOR");
//			for(int i = 1; i < 5; i++) {
//				Optional<Producto> optionalProducto = productoRepo.findById(i);
//				DTProductoCarrito dtpc = new DTProductoCarrito(new DTProducto(optionalProducto.get()), i);
//				productos.add(dtpc);
//			}
//			Carrito carrito = new Carrito(500, "csuarez2211@gmail.com", "lapasiva@lapasiva.com", productos, true);

			Optional<Carrito> optionalCarrito = mongoRepo.findById(pedido.getCarrito());
			Carrito carrito = optionalCarrito.get();
			DTPedido dtpedido = new DTPedido(pedido, new DTCarrito(carrito));

			String to = pedido.getCliente().getMail();
			String body = mailSender.getConfirmarPedido(dtpedido, pedido.getCliente());
			String topic = "Confirmación de pedido para " + pedido.getCliente().getNickname() + ".";
			try {
				mailSender.sendMail(to, body, topic);
			} catch (MessagingException e) {
				return new DTRespuesta("No se pudo mandar mail: " + e.getMessage());
			}

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

			String to = pedido.getCliente().getMail();
			String body = mailSender.getRechazarPedido(pedido.getRestaurante().getNombre(), pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido());
			String topic = "Rechazo de pedido para " + pedido.getCliente().getNickname() + ".";
			try {
				mailSender.sendMail(to, body, topic);
			} catch (MessagingException e) {
				return new DTRespuesta("No se pudo mandar mail: " + e.getMessage());
			}
			
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

		if (calificacion.getPuntaje() > 5)
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
			else if (!estado.equalsIgnoreCase("") && !fecha.equalsIgnoreCase("")) {
				System.out.println("estado y fecha");
				EnumEstadoReclamo estadoReclamo = EnumEstadoReclamo.valueOf(estado.toUpperCase());
				LocalDate ld = LocalDate.parse(fecha, DATEFORMATTER);
				LocalDateTime dateI = LocalDateTime.of(ld, LocalTime.of(00, 00));
				LocalDateTime dateF = LocalDateTime.of(ld, LocalTime.of(23, 59));
				System.out.println(dateI);
				System.out.println(dateF);
				pageReclamo = recRepo.findAllByRestauranteEstadoFecha(restaurante, estadoReclamo, dateI, dateF, paging);
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
			} else { // genérico
				pageReclamo = recRepo.findAllByRestaurante(restaurante, paging);
			}
		} else { // genérico
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

	@Override
	public void cargarDatos() {

		/////////////////// DATOS///////////

		////////////// admin/////////////////

		Administrador admin = new Administrador("admin1@gmail.com", "123456", "099999999",
				"https://www.elliberal.com/wp-content/uploads/2021/09/elon-musk.jpg", false, true, LocalDate.now());
		try {
			administradorService.crearAdministrador(admin);
		} catch (AdministradorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// CATEGORIIA
		Map<String, String> categoriaFotoMap = new HashMap<String, String>();
		categoriaFotoMap.put("Bebidas", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/alcohol.jpeg");
		categoriaFotoMap.put("Comida oriental",
				"https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/china.jpg");
		categoriaFotoMap.put("Chivitos", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/chivitos.jpg");
		categoriaFotoMap.put("Empanadas",
				"https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/empanadas.jpg");
		categoriaFotoMap.put("Comida espanola",
				"https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/espanola.jpg");
		categoriaFotoMap.put("Hamburguesas",
				"https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/hamburguesa.jpg");
		categoriaFotoMap.put("Helados", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/helado.jpg");
		categoriaFotoMap.put("Comida italiana",
				"https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/italiana.jpg");
		categoriaFotoMap.put("Desayunos y Meriendas",
				"https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/meriendas.jpg");
		categoriaFotoMap.put("Comida mexicana",
				"https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/mexicana.jpg");
		categoriaFotoMap.put("Milanesas", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/milanesa.jpg");
		categoriaFotoMap.put("Parrillada",
				"https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/parrilla.jpg");
		categoriaFotoMap.put("Pasta", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/pasta.jpg");
		categoriaFotoMap.put("Pastelería",
				"https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/pasteleria.jpg");
		categoriaFotoMap.put("Picadas", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/picadas.jpg");
		categoriaFotoMap.put("Pizzas", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/pizzas.jpg");
		categoriaFotoMap.put("Postres", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/postre.jpg");
		categoriaFotoMap.put("Saludable",
				"https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/saludable.jpg");
		categoriaFotoMap.put("Comida vegana",
				"https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/vegano.jpg");
		categoriaFotoMap.put("Comida vegetariana",
				"https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/vegetariano.jpg");

		categoriaFotoMap.forEach((k, v) -> catRepo.save(new Categoria(k, v)));

		// Productos
		// bebidas
		String bebidasString = "Cocoroco,Vodka Spirytus,Absenta,Chinchón seco especial,Licor de cocuy,Bacanora,Mezcal,Whisky,Orujo blanco,Fernet,Coñac,Cachaza,Grappa,Aquavit,Becherovka,Gin de Menorca,Vodka,Ron,Tequila,Ouzo,Bourbon,Brandy,Ginebra,Chinchón,Jägermeister,Caña,Pisco,Aguardiente,Palo,Tía María,Limoncello,Punsch,Cherry Heering,Pacharán,Ratafia,Vino de Oporto,Vodka Azul o Rojo,Vino de arroz,Vermut,Jerez,Mariete,Vino,Pelin,Pulque,Cerveza,Chinchón conga,Sidra,Vodka Spirytus,Absenta,Chinchón seco especial,Licor de cocuy,Bacanora,Mezcal,Whisky,Orujo blanco,Fernet,Coñac,Cachaza,Grappa,Aquavit,Becherovka,Gin de Menorca,Vodka,Ron,Tequila,Ouzo,Bourbon,Brandy,Ginebra,Chinchón,Jägermeister,Caña,Pisco,Aguardiente,Palo,Tía María,Limoncello,Punsch,Cherry Heering,Pacharán,Ratafia,Vino de Oporto,Vodka Azul o Rojo,Vino de arroz,Vermut,Jerez,Mariete,Vino,Pelin,Pulque,Cerveza,Chinchón conga,Sidra,Vodka Spirytus,Absenta,Chinchón seco especial,Licor de cocuy,Bacanora,Mezcal,Whisky,Orujo blanco,Fernet,Coñac,Cachaza,Grappa,Aquavit,Becherovka,Gin de Menorca,Vodka,Ron,Tequila,Ouzo,Bourbon,Brandy,Ginebra,Chinchón,Jägermeister,Caña,Pisco,Aguardiente,Palo,Tía María,Limoncello,Punsch,Cherry Heering,Pacharán,Ratafia,Vino de Oporto,Vodka Azul o Rojo,Vino de arroz,Vermut,Jerez,Mariete,Vino,Pelin,Pulque,Cerveza,Chinchón conga,Sidra";
		List<String> bebidasList = Arrays.asList(bebidasString.split(","));
		List<Producto> bebidasProductoList = new ArrayList<Producto>();
		for (String s : bebidasList) {
			Producto p0 = new Producto(s, "la más rica " + s, (Math.random() * 200),
					"https://www.xlsemanal.com/wp-content/uploads/sites/3/2018/05/bebidas.jpg", 15, true);
			p0.setCategoria(catRepo.findById("Bebidas").get());
			bebidasProductoList.add(p0);
		}

		String orientalString = "Pekinés,Sopa Wan Tan,Mapo doufu,Rollitos de primavera,Zongzi,Pollo Gong Bao o Kung Pao,Jiaozi,Wan Tan Mee,Chow Mein,Huo Guo o Hot Pot,Pekinés,Sopa Wan Tan,Mapo doufu,Rollitos de primavera,Zongzi,Pollo Gong Bao o Kung Pao,Jiaozi,Wan Tan Mee,Chow Mein,Huo Guo o Hot Pot,Pekinés,Sopa Wan Tan,Mapo doufu,Rollitos de primavera,Zongzi,Pollo Gong Bao o Kung Pao,Jiaozi,Wan Tan Mee,Chow Mein,Huo Guo o Hot Pot,Pekinés,Sopa Wan Tan,Mapo doufu,Rollitos de primavera,Zongzi,Pollo Gong Bao o Kung Pao,Jiaozi,Wan Tan Mee,Chow Mein,Huo Guo o Hot Pot,Sopa Wan Tan,Mapo doufu,Rollitos de primavera,Zongzi,Pollo Gong Bao o Kung Pao,Jiaozi,Wan Tan Mee,Chow Mein,Huo Guo o Hot Pot,Pekinés,Sopa Wan Tan,Mapo doufu,Rollitos de primavera,Zongzi,Pollo Gong Bao o Kung Pao,Jiaozi,Wan Tan Mee,Chow Mein,Huo Guo o Hot Pot,Pekinés,Sopa Wan Tan,Mapo doufu,Rollitos de primavera,Zongzi,Pollo Gong Bao o Kung Pao,Jiaozi,Wan Tan Mee,Chow Mein,Huo Guo o Hot Pot,Pekinés,Sopa Wan Tan,Mapo doufu,Rollitos de primavera,Zongzi,Pollo Gong Bao o Kung Pao,Jiaozi,Wan Tan Mee,Chow Mein,Huo Guo o Hot Pot,Sopa Wan Tan,Mapo doufu,Rollitos de primavera,Zongzi,Pollo Gong Bao o Kung Pao,Jiaozi,Wan Tan Mee,Chow Mein,Huo Guo o Hot Pot,Pekinés,Sopa Wan Tan,Mapo doufu,Rollitos de primavera,Zongzi,Pollo Gong Bao o Kung Pao,Jiaozi,Wan Tan Mee,Chow Mein,Huo Guo o Hot Pot,Pekinés,Sopa Wan Tan,Mapo doufu,Rollitos de primavera,Zongzi,Pollo Gong Bao o Kung Pao,Jiaozi,Wan Tan Mee,Chow Mein,Huo Guo o Hot Pot,Pekinés,Sopa Wan Tan,Mapo doufu,Rollitos de primavera,Zongzi,Pollo Gong Bao o Kung Pao,Jiaozi,Wan Tan Mee,Chow Mein,Huo Guo o Hot Pot";
		List<String> orientalList = Arrays.asList(orientalString.split(","));
		List<Producto> orientalProductoList = new ArrayList<Producto>();
		for (String s : orientalList) {
			Producto p0 = new Producto(s, "埃斯特柏拉圖" + s, (Math.random() * 400),
					"https://dam.cocinafacil.com.mx/wp-content/uploads/2019/06/palillos.chinos.jpg", 20, true);
			p0.setCategoria(catRepo.findById("Comida oriental").get());
			orientalProductoList.add(p0);
		}

		String chivitoString = "chivito de la casa,chivito big, grosso,chivito gigante,chivito doble carne,chivito completo, al plato";
		List<String> chivitoList = Arrays.asList(chivitoString.split(","));
		List<Producto> chivitoProductoList = new ArrayList<Producto>();
		for (String s : chivitoList) {
			Producto p0 = new Producto(s, "Para los que saben lo que es bueno", (Math.random() * 500),
					"https://conocer365.uy/wp-content/uploads/2019/03/Tinkal_chivito_3-585x388.png", 5, true);
			p0.setCategoria(catRepo.findById("Chivitos").get());
			chivitoProductoList.add(p0);
		}

		// empanadas
		String empanadasString = "Carne Suave,Carne Dulce,Carne Picante,Pollo Gauchita,Pollo con Champignon,Espinaca y Queso,Verdura - Hierbabuena al limón,Mazorca, Cebolla y salsa blanca,Queso, Orégano y Aceitunas,Frutal (queso y frutas),Queso - Queso,Queso y Albahaca,Queso y Jamón";
		List<String> empanadasListString = Arrays.asList(empanadasString.split(","));
		List<Producto> empanadasProductoList = new ArrayList<Producto>();
		for (String s : empanadasListString) {
			Producto p0 = new Producto(s, "Para los que saben lo que es bueno", (Math.random() * 500),
					"https://elmundoenrecetas.s3.amazonaws.com/uploads/recipe/main_image/95/IMG_4693_1200px.jpg", 5,
					true);
			p0.setCategoria(catRepo.findById("Empanadas").get());
			empanadasProductoList.add(p0);
		}

		// pizza
		String pizzaString = "Pepperoni,Hawaiian,TALIA,Mexican,Margher,Margherita,Vegetarian,Chicken,Deluxe,Deluxe,Tree cheese,Sausage,Anchovy,Jalapene";
		List<String> pizzaListString = Arrays.asList(pizzaString.split(","));
		List<Producto> pizzaProductoList = new ArrayList<Producto>();
		for (String s : pizzaListString) {
			Producto p0 = new Producto(s, "Con los mejores ingredientes", (Math.random() * 500),
					"https://filesedc.com/uploads/other/2019/08/1200/los-15-tipos-de-pizza-mas-populares-y-sus-ingredientes.jpeg",
					5, true);
			p0.setCategoria(catRepo.findById("Pizzas").get());
			pizzaProductoList.add(p0);
		}

		// hamburguezas
		String burguerString = "LAÇADOR,FRITZ,GRINGO,ANITA,PAMPA BURGER,CHIMANGO,PIRATINI,MARAGATO,GARIBALDI,CAMPEREADA,LA PLATA,MACANUDO,BORGHETTINHO,LOBISOMEM,DO ARVOREDO,CHARRUA,GALO VEIO,PRO D'água";
		List<String> burguerListString = Arrays.asList(burguerString.split(","));
		List<Producto> burguerStringProductoList = new ArrayList<Producto>();
		for (String s : burguerListString) {
			Producto p0 = new Producto(s, "Para paladares exigentes", (Math.random() * 500),
					"https://media-cdn.tripadvisor.com/media/photo-s/14/ba/b3/45/burgers.jpg", 12, true);
			p0.setCategoria(catRepo.findById("Hamburguesas").get());
			burguerStringProductoList.add(p0);
		}

		///// seguir agregando

		Producto p = new Producto("Milanesa", "la mejor del condado", 200.0,
				"https://t2.rg.ltmcdn.com/es/images/4/9/8/img_milanesa_de_carne_11894_orig.jpg", 10, true);
		p.setCategoria(catRepo.findById("Milanesas").get());
		Producto p1 = new Producto("Helado", "el mejor del condado", 100.0,
				"https://i.blogs.es/098b7c/helados1/1366_2000.jpg", 10, true);
		p1.setCategoria(catRepo.findById("Helados").get());
		Producto p2 = new Producto("hamburguesa", "la mejor del condado", 250.0,
				"https://cdn.computerhoy.com/sites/navi.axelspringer.es/public/styles/1200/public/media/image/2020/08/hamburguesa-2028707.jpg?itok=ujl3qgM9",
				10, true);
		p2.setCategoria(catRepo.findById("Hamburguesas").get());

		////////// RESTAURANTE/////////
		String dirString = "Andes 1180,Prof. Bacigalupi 2244,Jaime Cibils 2878,Durazno 2116 entre Salterain y Requena,Cabo Polonio 2107,Lucas Obes 896,Pedro Fco. Berro 773 esq.Jaime Zudánez (Pocitos),8 de Octubre 2619,Av. Lezica 5831 casi Yegros,Mataojo 1862,Grecia 3194 y México,Av. Américo Ricaldoni 2804,Francisco Echagoyen 4949,8 de Octubre 3390, esquina Propios,Av. Arocena 1919,Gil 1065,Fernández Crespo 2274,Av. Millán 3898,20 de Febrero 2510 ,Ledo Arroyo Torres s/n casi Hernani,Enriqueta Compte y Riqué 1287,28 de Febrero 1097, esq. Elías Regules,Presbítero José Barrales 2500,Camino Maldonado 81201, Ruta 8 Km 16.800,Dr. Joaquín Requena 3010,18 de Julio 2205 esq. Alejandro Beisso,Bulevar España 2772, Esquina Ellauri,Blandengues 2020 esq.Constitución,José Batlle y Ordoñez 1401 esq. Rivera,18 de Diciembre 1600,Carlos Roxlo 1611, esquina Paysandú,Ana María Rubens 2324 esq. Camino Carrasco,18 de Julio 2205 esq. Alejandro Beisso,José Enrique Rodó 1875 casi Eduardo Acevedo,Camino Castro 711,Vasconcellos s/n esq. Osvaldo Cruz,Ruperto Pérez Martínez 882 ,Matilde Pacheco 4160,Andes 1180,Prof. Bacigalupi 2244,Jaime Cibils 2878,Durazno 2116 entre Salterain y Requena,Cabo Polonio 2107,Lucas Obes 896,Pedro Fco. Berro 773 esq.Jaime Zudánez (Pocitos),8 de Octubre 2619,Av. Lezica 5831 casi Yegros,Mataojo 1862,Grecia 3194 y México,Av. Américo Ricaldoni 2804,Francisco Echagoyen 4949,8 de Octubre 3390, esquina Propios,Av. Arocena 1919,Gil 1065,Fernández Crespo 2274,Av. Millán 3898,20 de Febrero 2510 ,Ledo Arroyo Torres s/n casi Hernani,Enriqueta Compte y Riqué 1287,28 de Febrero 1097, esq. Elías Regules,Presbítero José Barrales 2500,Camino Maldonado 81201, Ruta 8 Km 16.800,Dr. Joaquín Requena 3010,18 de Julio 2205 esq. Alejandro Beisso,Bulevar España 2772, Esquina Ellauri,Blandengues 2020 esq.Constitución,José Batlle y Ordoñez 1401 esq. Rivera";
		List<String> direccionesList = Arrays.asList(dirString.split(","));
		// RESTAURANTES
		String restoString = "La Pasiva,Sushi Go,La Taberna del Diablo,Burger King,Empanadas Mafalda,Il Mondo della Pizza,Fans,D' La Ribera,El Hornito,Grido,Tiqui Taca,Don Koto,Empanadas La Barca,Heladería Las Delicias,Pizza Trouville,Pizza Piedra,Pizzería Rodelú,Grazie Italia,Subway,Food y Love,La Isla,Soprano's,Chiviteria Marcos,Pizzería Cervantes,Sushiapp,Felipe,Chivipizza,El Club de la Papa Frita,OMG Fried Chicken,Cremona - Dicomo Pasta,Premium,Fábrica de Pastas La Bolognesa,Freddo,Lehmeyun 100,La Roca,El Noble,Fellini,Artico,Barbacoa,Billie Joe,Gelateria del Club,Los Tavarez,Pizzeria Papa Jorge,Sushi Time,Pastas Baccino,Sinestesia,Crêpas,Tropical Smoothies,Chajá Bistro,San Roque,McDonald's,Chesterhouse,La Cigale,Homeopatía Alemana,Supermercados,Farmacias,Farmashop,Nescafé Dolce Gusto,Crepez,I love Tacos,Porto Vanila,Laika,Heladería La Chicharra,Iberpark,Farmacia El Tunel,Chéntola Gelato Artesanal,Sbarro,Fabric Sushi,Al Dente Pastas Artesanales,Del Abuelo Helados Artesanales,Alberto's,Cuidate - Comida Saludable,El Horno de Juan,El Novillo Alegre,Heladería Facal,Hoy te Quiero,Asian Food,Mimoso Resto Bar,Donut City,Hong Kong - Comida China,Mr. Kebap's,Veggie Mafalda,Rudy,Pizza Club,Pizza's House,Axion,Ciudad Aventura,Tomato Gourmet,Futuro Refuerzos,Pizzabrossa,Mascotas,Devoto,Poked,Noah's,The Lab Coffee,26 Sushi,Sabores,The Paletas Factory,La Vienesa,Paparike,Magnum,La Chacha Empanadas,Mise en place,Bar La Cruz,Lehmeyun Pizza Turca Armenia,Almacén de Pizzas,BIGA - Pizza y Pasta,II Gufo,Heladeria Pecas,Gaucho Burger,Emporio Gastronómico,Chivitos lo de Pepe,Facal,La Boletería,Burger Club,McCafé - McDonald's,Green To Go,Flores,Miyagi Sushi,Cafeterías,Pizza Mania,Bebidas,Bao bao,Tiendas,Wing It";
		List<String> restaurantesList = Arrays.asList(restoString.split(","));

		// fotos restaurante
		String fotosRString = "https://www.alacarta.com.uy/wp-content/uploads/2021/09/alacarta-restaurante-adorado-01-300x160.jpg,https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4BL01pRdMb4Fh3Urp-QVU6Vd8OcXeuaSPHFrdJxMohPsxwt3XaMTOjgyHGO0syh6pKf0&usqp=CAU,https://media-cdn.tripadvisor.com/media/photo-s/11/23/53/c4/img-20171031-144058-largejpg.jpg,https://media-cdn.tripadvisor.com/media/photo-s/0d/38/b5/d4/outdoor-seating.jpg,https://media-cdn.tripadvisor.com/media/photo-p/17/ff/4e/1c/terrasse.jpg,https://media-cdn.tripadvisor.com/media/photo-s/12/dd/a6/cf/doble-v.jpg,https://media-cdn.tripadvisor.com/media/photo-p/1a/d9/1b/c7/photo0jpg.jpg,https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRc2btjCzipIQA0d3Pp4s22KIf5P0XDlbd5A&usqp=CAU,https://www.estudiomontevideo.com/wp-content/uploads/2018/08/010917-KANTINE-AC-ph-G-Viramonte-1015.jpg,https://media-cdn.tripadvisor.com/media/photo-s/16/26/0a/22/restaurant.jpg,https://media-cdn.tripadvisor.com/media/photo-s/0d/a8/5b/78/demode.jpg";
		List<String> fotosRestaurante = Arrays.asList(fotosRString.split(","));

		Double lat = -34.9128;
		Double lon = -56.1886;
		int calificacion = 5;
		for (String resto : restaurantesList) {
			calificacion = calificacion - 1;
			if (calificacion == 1) {
				calificacion = 5;
			}
			try {

				Double rand = (Double) (Math.random() * 0.1);
				Double latitud = BigDecimal.valueOf(lat + rand).setScale(4, RoundingMode.HALF_UP).doubleValue();
				Double longitud = BigDecimal.valueOf(lon + rand).setScale(4, RoundingMode.HALF_UP).doubleValue();

				Integer i = (int) (Math.random() * 2 + 1);
				Integer envio = (int) (Math.random() * 30 + 1);
				Integer randomFoto = (int) (Math.random() * 10);
				Integer telefono = (int) (Math.random() * 999999);
				Integer dir = (int) (Math.random() * direccionesList.size());
				// carga de productos

				GeoLocalizacion geo = new GeoLocalizacion();

				geo.setLatitud(latitud);
				geo.setLongitud(longitud);

				Restaurante r = new Restaurante(
						resto.toLowerCase().replace(" ", "") + "@" + resto.toLowerCase().replace(" ", "") + ".com",
						"123456", "9" + telefono.toString(), null, false, true, resto, direccionesList.get(dir),
						Float.valueOf(calificacion), EnumEstadoRestaurante.values()[i - 1], null, null, null, envio,
						null, null, "LMWJVSD", true);

				// Set<Categoria> categoriaRandom = new HashSet<Categoria>();

				r.setProductos(null);
				r.setFoto(fotosRestaurante.get(randomFoto));
				r.setGeoLocalizacion(geo);
				r.setProductos(new ArrayList<>());

				crearRestaurantesDePrueba(r);

			} catch (RestauranteException | CategoriaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}

		Integer bebidaNumero = bebidasProductoList.size() - 1;
		Integer orientalNumero = orientalProductoList.size() - 1;

		List<Restaurante> restauList = resRepo.findAll();

		for (Restaurante restau : restauList) {

			Set<Producto> productosRandom = new HashSet<Producto>();
			if (bebidaNumero > 3 && orientalNumero > 3) {

				productosRandom.add(bebidasProductoList.get(bebidaNumero));
				bebidaNumero = bebidaNumero - 1;
				productosRandom.add(bebidasProductoList.get(bebidaNumero));
				bebidaNumero = bebidaNumero - 1;
				productosRandom.add(bebidasProductoList.get(bebidaNumero));
				bebidaNumero = bebidaNumero - 1;

				// productosRandom.add(orientalProductoList.get(orientalNumero / 3));
				// orientalProductoList.remove(orientalProductoList.get(orientalNumero / 3));

				if (0 == bebidaNumero % 3) {

					productosRandom.addAll(chivitoProductoList);
				}
				if (0 == bebidaNumero % 5) {

					productosRandom.addAll(empanadasProductoList);
				}

				if (0 == bebidaNumero % 7) {

					productosRandom.addAll(pizzaProductoList);
				}

				if (0 == bebidaNumero % 2) {

					productosRandom.addAll(burguerStringProductoList);
				}

				if (bebidaNumero < 7) {
					productosRandom.add(orientalProductoList.get(orientalNumero));
					orientalNumero = orientalNumero - 1;
					productosRandom.add(orientalProductoList.get(orientalNumero));
					orientalNumero = orientalNumero - 1;
				}

			} else {
				productosRandom.add(p);
				productosRandom.add(p1);
				productosRandom.add(p2);
			}

			for (Producto pr : productosRandom) {
				try {
					pr.setNombre(pr.getNombre());

					altaMenu(pr, restau.getMail());
				} catch (ProductoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/*
			 * Set<Categoria> categoriaRandom = new HashSet<Categoria>(); for(Producto prod:
			 * productosRandom) { //prod.setRestaurante(r);
			 * categoriaRandom.add(prod.getCategoria()); }
			 */

			// guardar resto

			/////////////promociones//////////////////

			List<Producto> listProdu = productoRepo.findAllByRestaurante(restau);
			Integer promoRandomNum = (int) (Math.random() * listProdu.size());
			Integer promoRandomNum2 = (int) (Math.random() * listProdu.size());
			String fotoPromo="https://www.bolsalea.com/blog/media/Bolsa-take-away-personalizada.jpg";

			Integer precioPromo= (int) ((listProdu.get(promoRandomNum).getPrecio()+listProdu.get(promoRandomNum2).getPrecio())/2);
			List<DTProductoIdCantidad>	productosPromo=new ArrayList<DTProductoIdCantidad>();
			DTProductoIdCantidad pc= new DTProductoIdCantidad(listProdu.get(promoRandomNum).getId(), 2);
			DTProductoIdCantidad pc2= new DTProductoIdCantidad(listProdu.get(promoRandomNum2).getId(), 3);
			productosPromo.add(pc);
			productosPromo.add(pc2);
			DTPromocionConPrecio promocion=  new DTPromocionConPrecio(productosPromo, precioPromo, 10, "Pomo del mes",
					"Imperdible promo", fotoPromo);
			try {
				altaPromocion(promocion, restau.getMail());
			} catch (RestauranteException | PromocionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	public void cargarDatos2() {
		Double lat = -34.9128;
		Double lon = -56.1886;
		String dirString = "Andes 1180,Prof. Bacigalupi 2244,Jaime Cibils 2878,Durazno 2116 entre Salterain y Requena,Cabo Polonio 2107,Lucas Obes 896,Pedro Fco. Berro 773 esq.Jaime Zudánez (Pocitos),8 de Octubre 2619,Av. Lezica 5831 casi Yegros,Mataojo 1862,Grecia 3194 y México,Av. Américo Ricaldoni 2804,Francisco Echagoyen 4949,8 de Octubre 3390, esquina Propios,Av. Arocena 1919,Gil 1065,Fernández Crespo 2274,Av. Millán 3898,20 de Febrero 2510 ,Ledo Arroyo Torres s/n casi Hernani,Enriqueta Compte y Riqué 1287,28 de Febrero 1097, esq. Elías Regules,Presbítero José Barrales 2500,Camino Maldonado 81201, Ruta 8 Km 16.800,Dr. Joaquín Requena 3010,18 de Julio 2205 esq. Alejandro Beisso,Bulevar España 2772, Esquina Ellauri,Blandengues 2020 esq.Constitución,José Batlle y Ordoñez 1401 esq. Rivera,18 de Diciembre 1600,Carlos Roxlo 1611, esquina Paysandú,Ana María Rubens 2324 esq. Camino Carrasco,18 de Julio 2205 esq. Alejandro Beisso,José Enrique Rodó 1875 casi Eduardo Acevedo,Camino Castro 711,Vasconcellos s/n esq. Osvaldo Cruz,Ruperto Pérez Martínez 882 ,Matilde Pacheco 4160,Andes 1180,Prof. Bacigalupi 2244,Jaime Cibils 2878,Durazno 2116 entre Salterain y Requena,Cabo Polonio 2107,Lucas Obes 896,Pedro Fco. Berro 773 esq.Jaime Zudánez (Pocitos),8 de Octubre 2619,Av. Lezica 5831 casi Yegros,Mataojo 1862,Grecia 3194 y México,Av. Américo Ricaldoni 2804,Francisco Echagoyen 4949,8 de Octubre 3390, esquina Propios,Av. Arocena 1919,Gil 1065,Fernández Crespo 2274,Av. Millán 3898,20 de Febrero 2510 ,Ledo Arroyo Torres s/n casi Hernani,Enriqueta Compte y Riqué 1287,28 de Febrero 1097, esq. Elías Regules,Presbítero José Barrales 2500,Camino Maldonado 81201, Ruta 8 Km 16.800,Dr. Joaquín Requena 3010,18 de Julio 2205 esq. Alejandro Beisso,Bulevar España 2772, Esquina Ellauri,Blandengues 2020 esq.Constitución,José Batlle y Ordoñez 1401 esq. Rivera";
		List<String> direccionesList = Arrays.asList(dirString.split(","));

		// RESTAURANTES
		String restoString = "La Pasiva,Sushi Go,La Taberna del Diablo,Burger King,Empanadas Mafalda,Il Mondo della Pizza,Fans,D' La Ribera,El Hornito,Grido,Tiqui Taca,Don Koto,Empanadas La Barca,Heladería Las Delicias,Pizza Trouville,Pizza Piedra,Pizzería Rodelú,Grazie Italia,Subway,Food y Love,La Isla,Soprano's,Chiviteria Marcos,Pizzería Cervantes,Sushiapp,Felipe,Chivipizza,El Club de la Papa Frita,OMG Fried Chicken,Cremona - Dicomo Pasta,Premium,Fábrica de Pastas La Bolognesa,Freddo,Lehmeyun 100,La Roca,El Noble,Fellini,Artico,Barbacoa,Billie Joe,Gelateria del Club,Los Tavarez,Pizzeria Papa Jorge,Sushi Time,Pastas Baccino,Sinestesia,Crêpas,Tropical Smoothies,Chajá Bistro,San Roque,McDonald's,Chesterhouse,La Cigale,Homeopatía Alemana,Supermercados,Farmacias,Farmashop,Nescafé Dolce Gusto,Crepez,I love Tacos,Porto Vanila,Laika,Heladería La Chicharra,Iberpark,Farmacia El Tunel,Chéntola Gelato Artesanal,Sbarro,Fabric Sushi,Al Dente Pastas Artesanales,Del Abuelo Helados Artesanales,Alberto's,Cuidate - Comida Saludable,El Horno de Juan,El Novillo Alegre,Heladería Facal,Hoy te Quiero,Asian Food,Mimoso Resto Bar,Donut City,Hong Kong - Comida China,Mr. Kebap's,Veggie Mafalda,Rudy,Pizza Club,Pizza's House,Axion,Ciudad Aventura,Tomato Gourmet,Futuro Refuerzos,Pizzabrossa,Mascotas,Devoto,Poked,Noah's,The Lab Coffee,26 Sushi,Sabores,The Paletas Factory,La Vienesa,Paparike,Magnum,La Chacha Empanadas,Mise en place,Bar La Cruz,Lehmeyun Pizza Turca Armenia,Almacén de Pizzas,BIGA - Pizza y Pasta,II Gufo,Heladeria Pecas,Gaucho Burger,Emporio Gastronómico,Chivitos lo de Pepe,Facal,La Boletería,Burger Club,McCafé - McDonald's,Green To Go,Flores,Miyagi Sushi,Cafeterías,Pizza Mania,Bebidas,Bao bao,Tiendas,Wing It";
		List<String> restaurantesList = Arrays.asList(restoString.split(","));

		//////////////// clientes//////////////////////
		String clienteString = "Hernandez Monterroza,Adriana Marcela Rey Sanchez,Alejandro Abondano Acevedo,Alexander Carvajal Vargas,Andrea Catalina Acero Caro,Andrea Liliana Cruz Garcia,Andres Felipe Villa Monroy,Angela Patricia Mahecha Pineros,Angelica Lisseth Blanco Concha,Angelica Maria Rocha Garcia,Angie Tatiana FernÁNdez MartÍNez,Brigite Polanco Ruiz,Camilo Villamizar Aristizabal,Camilo RodrÍGuez Botero,Camilo Alberto CortÉS Montejo,Camilo Enrique Gomez Rodriguez,Carlos AndrÉS Polo Castellanos,Carlos Didier CastaÑO Contreras,Carlos Felipe MogollÓN PachÓN,Carol Ruchina Gomez Gianine,Carol Ruchina Gomez Gianine,Carolina Pintor Pinzon,Catherine Ospina Alfonso,Cinthya Fernanda DussÁN GuzmÁN,Claudia Liliana Torres Frias,Cristina Elizabeth Barthel Guardiola,Daniel GÓMez Delgado,Daniel AndrÉS Castiblanco Salgado,Daniela HernÁNdez Bravo,Daniela HernÁNdez Bravo,Daniela GuzmÁN,Daniela Katherinne Suarique ÁVila,Daniella Puerto Navia,Deny Marcela MuÑOz Lizarazo,Diana Carolina Lopez Rodriguez,Diana Catalina Diaz Beltran,Diego Alejandro Forero PeÑA,Estewil Carlos Quesada CalderÍN,Estewil Carlos Quesada CalderÍN,Fabian Andres Fino Andrade,Gabriel Felipe Herrera Moreno,Gabriel Mauricio Nieto Bustos,Gabriel Mauricio Nieto Bustos,Gloria Patricia Mendoza Alvear,Hugo AndrÉS Camargo Vargas,Ingrid Rocio Guerrero Penagos,IvÁN David Coral Burbano,Ivonne Jouliette Barrera Lopez,Jenny Fernanda SÁNchez Arenas,Jenny Viviana Moncaleano Preciado,Jorge Esteban Rey Botero,Jorge Mario Orozco DussÁN,Jorge Mario Orozco DussÁN,Jose Guillermo Marin Zubieta,Juan Camilo Ortega PeÑA,Juan Camilo Jimenez Cortes,Juan Camilo Jimenez Cortes,Juan Esteban Lanao SÁNchez,Juan Fernando Barjuch Moreno,Juan Sebastian Romero Escobar,Juan Sebastian Tarquino Acosta,Juan Sebastian Sanchez Sanchez,JuliÁN Romero Montoya,Julian Leonardo Sanchez Prada,Juliana Gaviria Garcia,July Catherine Gonzalez Suarez,Karen Eliana HernÁNdez Pulido,Laura Diaz Mejia,Laura Camila Puerto Castro,Laura Catalina Varon Buitrago,Laura Fernanda RodrÍGuez Torres,Laura Fernanda RodrÍGuez Torres,Laura Natalia Novoa Gomez,Laura Viviana Del RÍO Ayerbe,Leonardo AndrÉS DueÑAs Rojas,Lina MarÍA ZÚÑIga RamÍRez,Liseth Tatiana Sierra Villamil,Liseth Tatiana Sierra Villamil,Luisa Fernanda GarcÍA Fonnegra,Luisa Fernanda GarcÍA Fonnegra,Marcela Garcia Rueda,Maria Alejandra BolÍVar Galeano,Maria Alejandra Horta Ochoa,MarÍA AngÉLica BeltrÁN Castillo,MarÍA Camila Guacas JimÉNez,Maria Camila Nieto Bustos,Maria JosÉ GarcÍA Mora,Maria JosÉ GarcÍA Mora,Maria Margarita Perez Moreno,Maria Margarita Perez Moreno,Maria Natalia Cervantes Luna,Mariana Del Pilar Santos Milachay,Mario Fernando GarzÓN MuÑOz,MÓNica Alexandra Camacho Amaya,MÓNica Natalia Camargo Mendoza,Natalia Buitrago Contreras,Natalia Puentes Perdomo,Natalia Andrea GutiÉRrez Velasco,Natalia Melissa Barrero Forero,Natalia Vivy Casas PÁEz,Olga Stephannia Saman Jimenez,Olga Viviana Ovalle Solano,Oscar Fabian Castellanos Rojas,Oscar David Colmenares Barbudo,Oscar Julian Ulloa Orjuela,Pablo Uribe Antia,Paola Andrea Correa Larios,Rafael Alejandro Gonzalez Rojas,Rafael Andres Alvarez Castillo,Rafael Andres Alvarez Castillo,Ricardo Vega Zambrano,Ricardo Vega Zambrano,Sandra Ximena GarcÉS Parra,Sebastian Borda Melguizo,SebastiÁN Iregui Galeano,Yiriam Liliam Ochoa Sabogal,Yiriam Liliam Ochoa Sabogal,Yurany Catalina Cifuentes Mendez,Yuri Catalina Salazar Aristizabal";
		List<String> clientesList = Arrays.asList(clienteString.split(","));

		String fotosString = "https://www.worldlistmania.com/wp-content/uploads/2012/06/Albert-Einstein.jpg,https://images.squarespace-cdn.com/content/v1/55db733fe4b0725b23e574a4/1544535106667-LPCVOZ2AYJ1FH9UCRM2M/%40uponwalls_Johan-BergmarkFamiliar-faces_15.jpg,https://images.immediate.co.uk/production/volatile/sites/4/2018/08/untitled-16ae48c.jpg?quality=90&resize=620,http://i.imgur.com/VRzwmCo.jpg,https://images.squarespace-cdn.com/content/v1/55db733fe4b0725b23e574a4/1544535108472-8M5E3BBG9SBZYKYVEEI7/%40uponwalls_Johan-BergmarkFamiliar-faces_17.jpg,https://i.pinimg.com/564x/e2/72/c2/e272c25222e6487af8fb91110fbad160.jpg,https://www.adweek.com/files/2016_Feb/jeep-portraits-2.png,https://i2-prod.mirror.co.uk/incoming/article5895610.ece/ALTERNATES/s615b/John-Lennon-in-1970.jpg,https://i.pinimg.com/originals/10/3a/2e/103a2e3dcadd5cd93f5bf12cfc27c853.jpg,http://i.imgur.com/zULezEe.jpg,https://en.bcdn.biz/Images/2017/2/6/6eb75d47-5cfb-47e3-a504-0be37d9e88f1.jpg,https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQYo-7gpdn2uT9S1FBHp92pPG8GBts0Og4aGg&usqp=CAU,https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQiGcOPjsvD6TQu2CLQWyl0TLINqtg9f4049w&usqp=CAU,https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/dwayne-johnson-attends-the-premiere-of-universal-pictures-news-photo-1161880409-1566504158.jpg,https://s.hdnux.com/photos/51/23/24/10827008/3/1200x0.jpg,https://regardingluxury.com/wp-content/uploads/2020/11/Ed-Sheeran-copy.jpg,https://depor.com/resizer/P_nxav9NOm_JK2gP4cXNdK98xnk=/580x330/smart/filters:format(jpeg):quality(75)/cloudfront-us-east-1.images.arcpublishing.com/elcomercio/4OFBX4Q4KJHCXIGVUZGF3YFSRE.jpg,https://gcdn.emol.cl/belleza/files/2021/08/famosos-de-hollywood-que-no-se-ba%C3%B1an-jake-gyllenhaal.jpg,https://casaydiseno.com/wp-content/uploads/2018/08/mujeres-destacadas-scarlett-johansson-resized.jpg,https://media.airedesantafe.com.ar/p/0350cda3310213c3b27dff99b7e5afb2/adjuntos/268/imagenes/002/553/0002553357/estas-cuatro-celebridades-hollywood-modificaron-sus-nombres-el-objetivo-impulsar-su-carrera-o-hacerse-mas-conocidos.png,https://resources.stuff.co.nz/content/dam/images/1/u/m/u/q/7/image.related.StuffLandscapeSixteenByNine.1420x800.1umuth.png/1554841238132.jpg,https://ichef.bbci.co.uk/news/976/cpsprodpb/108D9/production/_106610876_daenerys976.jpg,https://pbs.twimg.com/media/DJjArBcUIAECPfC.jpg,https://tvline.com/wp-content/uploads/2019/05/game-of-thrones-series-finale-tyrion-lannister-peter-dinklage.jpg";
		List<String> fotosClientesList = Arrays.asList(fotosString.split(","));

		for (String c : clientesList) {
			try {

				Double rand = (Double) (Math.random() * 0.1);
				Double latitud = BigDecimal.valueOf(lat + rand).setScale(4, RoundingMode.HALF_UP).doubleValue();
				Double longitud = BigDecimal.valueOf(lon + rand).setScale(4, RoundingMode.HALF_UP).doubleValue();

				GeoLocalizacion GeoCliente = new GeoLocalizacion(latitud, longitud);

				Integer dir = (int) (Math.random() * direccionesList.size() - 1);
				Integer telefono = (int) (Math.random() * 99999999 + 1);
				Integer numero = (int) (Math.random() * 99 + 1);
				Integer fotoRandom = (int) (Math.random() * fotosClientesList.size());
				Cliente cliente = new Cliente(
						c.replace(" ", "").toLowerCase() + "@" + c.replace(" ", "").toLowerCase() + ".com", "123456",
						telefono.toString(), fotosClientesList.get(fotoRandom), false, true, null,
						c.split(" ")[0] + c.split(" ")[1] + numero, null, null, c.split(" ")[0], c.split(" ")[1], null);
				Direccion direccion = new Direccion();
				direccion.setCalleNro(direccionesList.get(dir));
				direccion.setCliente(cliente);
				direccion.setGeoLocalizacion(GeoCliente);
				cliente.addDireccion(direccion);
				try {

					crearCliente(cliente);
				} catch (Exception e) {

				}

				//////////// pedidos///////////////////

				Integer randomR = (int) (Math.random() * 21);
				String mailREsto = restaurantesList.get(randomR).toLowerCase().replace(" ", "") + "@"
						+ restaurantesList.get(randomR).toLowerCase().replace(" ", "") + ".com";
				Optional<Restaurante> resOp = resRepo.findById(mailREsto);
				Optional<Cliente> cli = userRepo.findById(cliente.getMail());
				if (cli.isPresent() && resOp.isPresent() && resOp.get().getActivo() && !resOp.get().getBloqueado()) {
					try {
						clienteService.agregarACarrito(resOp.get().getProductos().get(0).getId(), 3, cliente.getMail(),
								resOp.get().getMail());
						DTCarrito carrito = clienteService.verCarrito(cliente.getMail());
						clienteService.altaPedidosParaCargadeDatos((int) carrito.getId(), EnumMetodoDePago.EFECTIVO,
								cli.get().getDirecciones().get(0).getId(), cliente.getMail(), "Muero de hambre");
						// calificaciones
						clienteService.calificarRestaurante(cliente.getMail(), resOp.get().getMail(), new Calificacion(
								(int) (Math.random() * 2) + 3, "mejor imposible", null, LocalDateTime.now()));

						calificarCliente(cliente.getMail(), resOp.get().getMail(), new Calificacion(
								(int) (Math.random() * 2) + 3, "Sos todo lo que está bien", null, LocalDateTime.now()));
					} catch (Exception e) {

					}
				}

			} catch (Exception e) {
				System.out.print(e.toString());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private DTRespuesta crearRestaurantesDePrueba(Restaurante rest) throws RestauranteException, CategoriaException {

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
		// rest.setEstado(EnumEstadoRestaurante.EN_ESPERA);
		// rest.setFechaApertura(null);
		// rest.setProductos(null);
//		rest.setReclamos(null);
//		rest.setPedidos(null);
		LocalTime maximo = LocalTime.of(0, 50, 0);
		LocalTime minimo = LocalTime.of(0, 20, 0);
		rest.setTiempoEstimadoMaximo(maximo);
		rest.setTiempoEstimadoMinimo(minimo);
		rest.setHorarioApertura(LocalTime.of(10, 0, 0));
		rest.setHorarioCierre(LocalTime.of(20, 0, 0));

		rest.setAbierto(true);
		rest.setContrasenia(passwordEncoder.encode(rest.getContrasenia()));

		System.out.println(rest.toString());
		System.out.println(rest.toString());
		System.out.println(rest.toString());
		System.out.println(rest.toString());
		System.out.println(rest.toString());

		restauranteRepo.save(rest);
		return new DTRespuesta("Restaurante " + rest.getNombre() + " dado de alta correctamente.");
	}

	private DTRespuesta crearCliente(Cliente usuario) throws UsuarioException, Exception {
		if (emailExist(usuario.getMail())) {
			throw new UsuarioException(UsuarioException.UsuarioYaExiste(usuario.getMail()));
		}
		String mail = usuario.getMail();

		if (mail != null && !mail.isEmpty() && usuario.getNickname() != null) {
			usuario.setActivo(true);
			usuario.setBloqueado(false);
			usuario.setSaldoBono(0.0f);
			usuario.setCalificacionPromedio(5.0f);
			usuario.setFechaCreacion(LocalDate.now());
			usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
			try {
				userRepo.save(usuario);
				return new DTRespuesta("Cliente dado de alta con éxito.");
			} catch (Exception e) {
				throw new UsuarioException("Ya existe un usuario con el nickname " + usuario.getNickname());
			}
		} else {
			throw new UsuarioException("Mail, nickname y contraseña son campos obligatorios");
		}
	}

	private boolean emailExist(String mail) {
		return userRepo.findById(mail).isPresent();
	}

	@Override
	public void corregirDatos() {
		String restoString = "La Pasiva,Sushi Go,La Taberna del Diablo,Burger King,Empanadas Mafalda,Il Mondo della Pizza,Fans,D' La Ribera,El Hornito,Grido,Tiqui Taca,Don Koto,Empanadas La Barca,Heladería Las Delicias,Pizza Trouville,Pizza Piedra,Pizzería Rodelú,Grazie Italia,Subway,Food & Love,La Isla,Soprano's,Chiviteria Marcos,Pizzería Cervantes,Sushiapp,Felipe,Chivipizza,El Club de la Papa Frita,OMG Fried Chicken,Cremona - Dicomo Pasta,Premium,Fábrica de Pastas La Bolognesa,Freddo,Lehmeyun 100,La Roca,El Noble,Fellini,Artico,Barbacoa,Billie Joe,Gelateria del Club,Los Tavarez,Pizzeria Papa Jorge,Sushi Time,Pastas Baccino,Sinestesia,Crêpas,Tropical Smoothies,Chajá Bistro,San Roque,McDonald's,Chesterhouse,La Cigale,Homeopatía Alemana,Supermercados,Farmacias,Farmashop,Nescafé Dolce Gusto,Crepez,I love Tacos,Porto Vanila,Laika,Heladería La Chicharra,Iberpark,Farmacia El Tunel,Chéntola Gelato Artesanal,Sbarro,Fabric Sushi,Al Dente Pastas Artesanales,Del Abuelo Helados Artesanales,Alberto's,Cuidate - Comida Saludable,El Horno de Juan,El Novillo Alegre,Heladería Facal,Hoy te Quiero,Asian Food,Mimoso Resto Bar,Donut City,Hong Kong - Comida China,Mr. Kebap's,Veggie Mafalda,Rudy,Pizza Club,Pizza's House,Axion,Ciudad Aventura,Tomato Gourmet,Futuro Refuerzos,Pizzabrossa,Mascotas,Devoto,Poked,Noah's,The Lab Coffee,26 Sushi,Sabores,The Paletas Factory,La Vienesa,Paparike,Magnum,La Chacha Empanadas,Mise en place,Bar La Cruz,Lehmeyun Pizza Turca Armenia,Almacén de Pizzas,BIGA - Pizza y Pasta,II Gufo,Heladeria Pecas,Gaucho Burger,Emporio Gastronómico,Chivitos lo de Pepe,Facal,La Boletería,Burger Club,McCafé - McDonald's,Green To Go,Flores,Miyagi Sushi,Cafeterías,Pizza Mania,Bebidas,Bao bao,Tiendas,Wing It";
		List<String> restaurantesList = Arrays.asList(restoString.split(","));

		// fotos restaurante
		String fotosRString = "https://www.alacarta.com.uy/wp-content/uploads/2021/09/alacarta-restaurante-adorado-01-300x160.jpg,https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4BL01pRdMb4Fh3Urp-QVU6Vd8OcXeuaSPHFrdJxMohPsxwt3XaMTOjgyHGO0syh6pKf0&usqp=CAU,https://media-cdn.tripadvisor.com/media/photo-s/11/23/53/c4/img-20171031-144058-largejpg.jpg,https://media-cdn.tripadvisor.com/media/photo-s/0d/38/b5/d4/outdoor-seating.jpg,https://media-cdn.tripadvisor.com/media/photo-p/17/ff/4e/1c/terrasse.jpg,https://media-cdn.tripadvisor.com/media/photo-s/12/dd/a6/cf/doble-v.jpg,https://media-cdn.tripadvisor.com/media/photo-p/1a/d9/1b/c7/photo0jpg.jpg,https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRc2btjCzipIQA0d3Pp4s22KIf5P0XDlbd5A&usqp=CAU,https://www.estudiomontevideo.com/wp-content/uploads/2018/08/010917-KANTINE-AC-ph-G-Viramonte-1015.jpg,https://media-cdn.tripadvisor.com/media/photo-s/16/26/0a/22/restaurant.jpg,https://media-cdn.tripadvisor.com/media/photo-s/0d/a8/5b/78/demode.jpg";
		List<String> fotosRestaurante = Arrays.asList(fotosRString.split(","));

		Double lat = -34.9128;
		Double lon = -56.1886;
		for (String resto : restaurantesList) {
			Optional<Restaurante> rop = restauranteRepo
					.findById(resto.replace(" ", "") + "@" + resto.replace(" ", "") + ".com");
			if (rop.isPresent()) {
				GeoLocalizacion geo = new GeoLocalizacion();

				geo.setLatitud(lat);
				geo.setLongitud(lon);
				Restaurante restaurante = rop.get();
				restaurante.setGeoLocalizacion(geo);
				restauranteRepo.save(restaurante);
				lat = lat + 1;
				lon = lon + 1;
			}
		}
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

		// Obtengo la informacion de los productos pedidos de un carrito
		Document carritoBuscado = collectionCarrito.find(new Document("_id", pedido.getCarrito())).first();
		List<BasicDBObject> listaDBObject = (List<BasicDBObject>) carritoBuscado.get("productoCarrito");

		Document carritoDocument;
		Document productoDocument;

		String idProducto;
		String cantidad;
		String categoria;
		String fecha;
		String nombreProducto;

		String nombreRestaurante = pedido.getRestaurante().getNombre();

		for (Object obj : listaDBObject) {
			carritoDocument = (Document) obj;
			cantidad = carritoDocument.get("cantidad").toString();
			productoDocument = (Document) carritoDocument.get("producto");
			idProducto = productoDocument.get("_id").toString();
			categoria = productoDocument.getString("categoria");
			fecha = pedido.getFecha().toString();
			nombreProducto = productoDocument.getString("nombre");

			ventaProducto(idProducto, cantidad, categoria, fecha, nombreRestaurante, nombreProducto);
		}

		return new DTRespuesta("Pago registrado con éxito.");
	}

	@Override
	public void ventaProducto(String idProducto, String cantidad, String categoria, String fecha,
			String nombreRestaurante, String nombreProducto) {
		MongoClientURI uri = new MongoClientURI(
				"mongodb+srv://grupo1:grupo1@cluster0.l17sm.mongodb.net/prueba-concepto");
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase dataBase = mongoClient.getDatabase("prueba-concepto");
		MongoCollection<Document> collectionPedidos = dataBase.getCollection("pedidos");

		FindIterable<Document> elemento = collectionPedidos.find(new Document("_id", idProducto));

		if (elemento.first() != null) {
			// Si existe el elemento con el idProducto se modifica
			Document pedidoDocument = (Document) collectionPedidos.find(new Document("_id", idProducto)).first();

			// Conversiones a int y suma
			String cantidadDocumento = pedidoDocument.getString("cantidad");
			int cantidadDocumentoInt = Integer.parseInt(cantidadDocumento);
			int cantidadSuma = Integer.parseInt(cantidad);
			int cantidadSumada = cantidadDocumentoInt + cantidadSuma;
			String cantidadSumadaString = Integer.toString(cantidadSumada);

			// Modifico el valor
			Bson filter = eq("cantidad", pedidoDocument.get("cantidad"));
			Bson updateOperation = set("cantidad", cantidadSumadaString);
			collectionPedidos.updateOne(filter, updateOperation);

		} else {
			Document document = new Document();

			document.put("_id", idProducto);
			document.put("nombreProducto", nombreProducto);
			document.put("nombreRestaurante", nombreRestaurante);
			document.put("cantidad", cantidad);
			document.put("categoria", categoria);
			document.put("fecha", fecha);

			/*
			 * document.put("idProducto", idProducto); document.put("cantidad", cantidad);
			 * document.put("categoria", categoria); document.put("fecha", fecha);
			 */
			collectionPedidos.insertOne(document);
		}
	}

	@Override
	public DTRespuesta devolucionPedido(int idPedido, int idReclamo) {
		Optional<Pedido> optionalPedido = pedidoRepo.findById(idPedido);
		Pedido pedido = optionalPedido.get();
		Pedido devolucion = new Pedido(pedido.getFecha(), pedido.getCostoTotal() * -1, pedido.getEstadoPedido(),
				pedido.getMetodoDePago(), pedido.getCarrito(), pedido.getDireccion(), pedido.getRestaurante(),
				pedido.getCliente(), pedido.getComentario(), pedido.getPago());
		// devolucion.setEstadoPedido(EnumEstadoPedido.REEMBOLZADO);
		devolucion.setEstadoPedidido(null);
		pedido.setEstadoPedido(EnumEstadoPedido.REEMBOLZADO);
		List<Reclamo> reclamos = pedido.getReclamos();
		for (Reclamo reclamo : reclamos) {
			if (reclamo.getId() != idReclamo) {
				reclamo.setEstado(EnumEstadoReclamo.RECHAZADO);
				reclamo.setResolucion("El pedido ha sido devuelto");
			}
		}
		pedidoRepo.save(devolucion);
		pedidoRepo.save(pedido);
		return new DTRespuesta("Devolucion registrada con éxito.");
	}

	@Override
	public DTRespuesta resolucionReclamo(int idReclamo, Boolean aceptoReclamo, String comentario) throws IOException {
		Optional<Reclamo> optionalReclamo = recRepo.findById(idReclamo);
		Reclamo reclamo = optionalReclamo.get();
		String token = reclamo.getPedido().getCliente().getTokenDispositivo();
		String restaurante = reclamo.getRestaurante().getNombre();
		Message message;

		Map<String, String> map = new HashMap<>();

		map.put("llave", "valor");

		if (aceptoReclamo) {
			// Se acepta el reclamo
			// Se llama devolucion pedido
			// Se envia notificacion con mensaje

			reclamo.setEstado(EnumEstadoReclamo.ACEPTADO);
			reclamo.setResolucion(comentario);
			recRepo.save(reclamo);
			// Envio notificacion al mobile

			if (token != null) {

				Notification notification = Notification.builder().setTitle("Resolucion de reclamo")
						.setBody("Su reclamo ha sido aceptado por el restaurante " + restaurante).build();
				message = Message.builder().putAllData(map).putData("mensaje", "Nuevo mensaje de sus reclamos")
						.setToken(token) // deviceId
						.setNotification(notification).build();
				try {
					// DUDA QUE ES appAndroid
					FirebaseMessaging.getInstance().send(message);
					// FirebaseMessaging.getInstance(FirebaseApp.getInstance(appAndroid)).send(message);
					System.out.println("PRUEBA SE ENVIA NOTIFICACION");
				} catch (FirebaseMessagingException e) {
					System.out.println(e.getStackTrace());
					System.out.println("Mensaje de error: " + e.getMessagingErrorCode());
				}
			}

			devolucionPedido(reclamo.getPedido().getId(), idReclamo);
		} else {
			// No se acepta el reclamo
			// Se envia notificacion con mensaje

			reclamo.setEstado(EnumEstadoReclamo.RECHAZADO);
			// Envio notificacion al mobile

			if (token != null) {
				Notification notification = Notification.builder().setTitle("Resolucion de reclamo")
						.setBody("Su reclamo ha sido rechazado por el restaurante " + restaurante).build();
				message = Message.builder().putAllData(map).putData("mensaje", "Nuevo mensaje de sus reclamos")
						.setToken(token) // deviceId
						.setNotification(notification).build();
				try {
					// DUDA QUE ES appAndroid
					FirebaseMessaging.getInstance().send(message);
					// FirebaseMessaging.getInstance(FirebaseApp.getInstance(appAndroid)).send(message);
					System.out.println("PRUEBA SE ENVIA NOTIFICACION");
				} catch (FirebaseMessagingException e) {
					System.out.println(e.getStackTrace());
					System.out.println("Mensaje de error: " + e.getMessagingErrorCode());
				}
			}
		}

		// NOTIFICAR A CLIENTE
		// se notifica a cliente
		String base64EncodedEmail = Base64.getEncoder()
				.encodeToString(reclamo.getPedido().getCliente().getMail().getBytes(StandardCharsets.UTF_8));

		simpMessagingTemplate.convertAndSend("/topic/" + base64EncodedEmail, "Reclamo");// new DTNotificacionSoket("Su
																						// pedido ha sido aceptado y se
																						// está siendo preparado",
																						// "Reclamo"));

		// simpMessagingTemplate.convertAndSend("/topic/" + base64EncodedEmail,"Su
		// pedido ha sido aceptado y se está siendo preparado");

		// recRepo.save(reclamo); Lo muevo arriba porque sobreescribe
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

//////////////////////////////////////////ESTADISTICAS///////////////////////////////////

	/**
	* BALANCE DE VENTAS
	*/
	@Scheduled(cron = "*/59 */10 * * * *") // 1 vez cada 5 minutos
	public DTRespuesta actualizarBalanceVentas() {

		// descomentar
		// List<Pedido> pedidosList = pedidoRepo.findAllPagos();
		List<Pedido> pedidosList = pedidoRepo.findAll();

		for (Pedido pedido : pedidosList) {
			Optional<BalanceVentaDTO> balanceByMailOp = balanceVentasRepo.findById(pedido.getRestaurante().getMail());
			if (balanceByMailOp.isPresent()) {
				BalanceVentaDTO balanceByMail = balanceByMailOp.get();

				// primera venta con esa fecha
				if (!balanceByMail.getListaPedidos().stream()
						.filter(o -> o.getFecha().equals(pedido.getFecha().toLocalDate())).findFirst().isPresent()) {

					List<FechaidPedidoMontoDTO> fecha_PedidoMonto = balanceByMail.getListaPedidos();
					List<IdPedidoMontoDTO> pedidos = new ArrayList<IdPedidoMontoDTO>();
					IdPedidoMontoDTO idPedidoMontoDTO = new IdPedidoMontoDTO(pedido.getId(),
							BigDecimal.valueOf(pedido.getCostoTotal()).setScale(4, RoundingMode.HALF_UP).doubleValue(),
							pedido.getEstadoPedido().name());
					pedidos.add(idPedidoMontoDTO);
					FechaidPedidoMontoDTO fechaIdPedidoMonto = new FechaidPedidoMontoDTO(
							pedido.getFecha().toLocalDate(), pedidos);

					fecha_PedidoMonto.add(fechaIdPedidoMonto);
					balanceByMail.setListaPedidos(fecha_PedidoMonto);
					balanceByMail.setTotal(BigDecimal.valueOf(balanceByMail.getTotal() + pedido.getCostoTotal())
							.setScale(4, RoundingMode.HALF_UP).doubleValue());
					balanceVentasRepo.save(balanceByMail);
				}else {//ya hay pedidos en esa fecha
					Boolean yaAgregado=false;
					List<FechaidPedidoMontoDTO> fecha_PedidoMonto = balanceByMail.getListaPedidos();

					Optional<FechaidPedidoMontoDTO> idPedidoMonto = fecha_PedidoMonto.stream().filter(o -> o.getFecha().equals(pedido.getFecha().toLocalDate())).findFirst();
					if(idPedidoMonto!= null && idPedidoMonto.isPresent()) {
						List<IdPedidoMontoDTO> listaPedidos= idPedidoMonto.get().getPedidos();//lista de pedidos de esa fecha

						for(IdPedidoMontoDTO l: listaPedidos) {
							if(l.getIdPedido()==pedido.getId()) {
								yaAgregado=true;
							}
						}
						if (!yaAgregado) {
							IdPedidoMontoDTO idPedidoMontoDTO = new IdPedidoMontoDTO(
									pedido.getId(), BigDecimal.valueOf(pedido.getCostoTotal())
											.setScale(4, RoundingMode.HALF_UP).doubleValue(),
									pedido.getEstadoPedido().name());
							listaPedidos.add(idPedidoMontoDTO);
							idPedidoMonto.get().setPedidos(listaPedidos);

							// elimino FechaidPedidoMontoDTO que habia y agrego el nuevo
							// System.out.print(fecha_PedidoMonto!=null);
							for (FechaidPedidoMontoDTO f : fecha_PedidoMonto) {
								if (f.getFecha().equals(idPedidoMonto.get().getFecha())) {
									fecha_PedidoMonto.remove(f);
									break;
								}
							}
							fecha_PedidoMonto.add(idPedidoMonto.get());

							balanceByMail.setTotal(BigDecimal.valueOf(balanceByMail.getTotal() + pedido.getCostoTotal())
									.setScale(4, RoundingMode.HALF_UP).doubleValue());
							balanceByMail.setListaPedidos(fecha_PedidoMonto);

							balanceVentasRepo.save(balanceByMail);
						}

					}

				}
			} else {// el restaurante no está aun guardado

				BalanceVentaDTO balanceByMail = new BalanceVentaDTO();
				balanceByMail.set_id(pedido.getRestaurante().getMail());
				balanceByMail.setTotal(pedido.getCostoTotal());

				List<FechaidPedidoMontoDTO> fecha_PedidoMonto = balanceByMail.getListaPedidos();
				List<IdPedidoMontoDTO> pedidos = new ArrayList<>();
				IdPedidoMontoDTO idPedidoMontoDTO = new IdPedidoMontoDTO(pedido.getId(),
						BigDecimal.valueOf(pedido.getCostoTotal()).setScale(4, RoundingMode.HALF_UP).doubleValue(),
						pedido.getEstadoPedido().name());
				pedidos.add(idPedidoMontoDTO);
				FechaidPedidoMontoDTO fechaPedidoMontoDto = new FechaidPedidoMontoDTO(pedido.getFecha().toLocalDate(),
						pedidos);
				fecha_PedidoMonto.add(fechaPedidoMontoDto);

				balanceByMail.setListaPedidos(fecha_PedidoMonto);

				balanceVentasRepo.save(balanceByMail);

			}
		}

		// resPedRepo.saveAll(restaurantesPedidos);
		return new DTRespuesta("Balance de Ventas actualizado");
	}

	public Object getBalanceVentaByFecha(String fechaInicio, String fechaHasta, String mailFromJwt) {
		Optional<BalanceVentaDTO> balanceByMailOp = balanceVentasRepo.findById(mailFromJwt);
		if (balanceByMailOp.isPresent()) {
			String[] fecha = fechaInicio.split("-");
			Integer dia = Integer.valueOf(fecha[2]);
			Integer mes = Integer.valueOf(fecha[1]);
			Integer anho = Integer.valueOf(fecha[0]);
			LocalDate inicio = LocalDate.of(anho, mes, dia);

			String[] fecha1 = fechaHasta.split("-");
			Integer dia1 = Integer.valueOf(fecha1[2]);
			Integer mes1 = Integer.valueOf(fecha1[1]);
			Integer anho1 = Integer.valueOf(fecha1[0]);
			LocalDate fin = LocalDate.of(anho1, mes1, dia1);

			Double totalPeriodo = (double) 0;

			Set<FechaidPedidoMontoDTO> lista = new HashSet<>();

			for (FechaidPedidoMontoDTO entry : balanceByMailOp.get().getListaPedidos()) {

				if (entry.getFecha().equals(inicio) ||  entry.getFecha().equals(fin) || (entry.getFecha().isAfter(inicio) && entry.getFecha().isBefore(fin))) {

					lista.add(entry);
					for (IdPedidoMontoDTO entry2 : entry.getPedidos()) {
						totalPeriodo = totalPeriodo + Double.valueOf(entry2.getMonto());
					}
				}

			}

			if (lista.size() > 0) {
				return new BalanceByFechaDTO(lista, totalPeriodo.toString());
			} else {

				return "El restaurante no tuvo ventas entre esas fechas.";
			}

		} else
			return "Balance de Ventas no disponible";
	}

	public Object getEstado(String mailFromJwt) {
		Optional<Restaurante> restOp = restauranteRepo.findById(mailFromJwt);
		if (restOp.isPresent()) {
			String resp = restOp.get().getAbierto() ? "Abierto" : "cerrado";
			return new DTRespuesta(resp);
		}

		return new DTRespuesta("Restaurante no encontrado");
	}

	@Override
	public Map<String, Object> topProductos(int page, int size) {
		Sort sort = Sort.by(Sort.Order.desc("cantidad"));
		Pageable paging = PageRequest.of(page, size, sort);
		Map<String, Object> response = new HashMap<>();
		Page<DTProductoVendido> pageProductos;
		pageProductos = productosVendidosRepo.findAll(paging);
		List<DTProductoVendido> DTproductosVendidos = pageProductos.getContent();

		response.put("currentPage", pageProductos.getNumber());
		response.put("totalItems", pageProductos.getTotalElements());
		response.put("restaurantes", DTproductosVendidos);
		return response;
	}
}
