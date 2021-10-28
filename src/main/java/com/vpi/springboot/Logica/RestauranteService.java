package com.vpi.springboot.Logica;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.IdCompuestas.CalificacionClienteId;
import com.vpi.springboot.Modelo.Calificacion;
import com.vpi.springboot.Modelo.CalificacionCliente;
import com.vpi.springboot.Modelo.Carrito;
import com.vpi.springboot.Modelo.Categoria;
import com.vpi.springboot.Modelo.GeoLocalizacion;
import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Promocion;
import com.vpi.springboot.Modelo.Reclamo;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.DTCarrito;
import com.vpi.springboot.Modelo.dto.DTCliente;
import com.vpi.springboot.Modelo.dto.DTPedido;
import com.vpi.springboot.Modelo.dto.DTPedidoParaAprobar;
import com.vpi.springboot.Modelo.dto.DTProductoCarrito;
import com.vpi.springboot.Modelo.dto.DTProductoIdCantidad;
import com.vpi.springboot.Modelo.dto.DTPromocionConPrecio;
import com.vpi.springboot.Modelo.dto.DTReclamo;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.Modelo.dto.EnumEstadoPedido;
import com.vpi.springboot.Modelo.dto.EnumEstadoReclamo;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Repositorios.CalificacionClienteRepositorio;
import com.vpi.springboot.Repositorios.CategoriaRepositorio;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.GeoLocalizacionRepositorio;
import com.vpi.springboot.Repositorios.MongoRepositorioCarrito;
import com.vpi.springboot.Repositorios.PedidoRepositorio;
import com.vpi.springboot.Repositorios.ProductoRepositorio;
import com.vpi.springboot.Repositorios.PromocionRepositorio;
import com.vpi.springboot.Repositorios.ReclamoRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.exception.CategoriaException;
import com.vpi.springboot.exception.PedidoException;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.ReclamoException;
import com.vpi.springboot.exception.PromocionException;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;
import com.vpi.springboot.security.util.JwtUtil.keyInfoJWT;

@Service
public class RestauranteService implements RestauranteServicioInterfaz {

	@Autowired
	private RestauranteRepositorio restauranteRepo;
	@Autowired
	private GeoLocalizacionRepositorio geoRepo;
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
	private ReclamoRepositorio recRepo;
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

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

		System.out.println("Llega antes de pedidos");

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
					.encodeToString(pedido.getRestaurante().getMail().getBytes(StandardCharsets.UTF_8));

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
			proRepo.save(promocion);
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
					.encodeToString(pedido.getRestaurante().getMail().getBytes(StandardCharsets.UTF_8));

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

	public Map<String, Object> listarReclamos(int page, int size, String cliente, String sort, int order,
			String mailRestaurante) throws RestauranteException {
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

		if (!cliente.equalsIgnoreCase("")) {
			pageReclamo = recRepo.findAllByRestauranteCliente(cliente, restaurante, paging);
		} else {
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

	@Override
	public void cargarDatos() throws ProductoException, RestauranteException, CategoriaException, Exception {
		
		///////////////////DATOS///////////
		
		
		//CATEGORIIA
		Map<String, String> categoriaFotoMap= new HashMap<String, String>();
	    categoriaFotoMap.put("Bebidas", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/alcohol.jpeg");
        categoriaFotoMap.put("Comida oriental", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/china.jpg");
        categoriaFotoMap.put("Chivitos", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/chivitos.jpg");
        categoriaFotoMap.put("Empanadas", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/empanadas.jpg");
        categoriaFotoMap.put("Comida española", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/espanola.jpg");
        categoriaFotoMap.put("Hamburguesas", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/hamburguesa.jpg");
        categoriaFotoMap.put("Helados", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/helado.jpg");
        categoriaFotoMap.put("Comida italiana", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/italiana.jpg");
        categoriaFotoMap.put("Desayunos y Meriendas", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/meriendas.jpg");
        categoriaFotoMap.put("Comida mexicana", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/mexicana.jpg");
        categoriaFotoMap.put("Milanesas", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/milanesa.jpg");
        categoriaFotoMap.put("Parrillada", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/parrilla.jpg");
        categoriaFotoMap.put("Pasta", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/pasta.jpg");
        categoriaFotoMap.put("Pastelería", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/pasteleria.jpg");
        categoriaFotoMap.put("Picadas", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/picadas.jpg");
        categoriaFotoMap.put("Pizzas", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/pizzas.jpg");
        categoriaFotoMap.put("Postres", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/postre.jpg");
        categoriaFotoMap.put("Saludable", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/saludable.jpg");
        categoriaFotoMap.put("Comida vegana", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/vegano.jpg");
        categoriaFotoMap.put("Comida vegetariana", "https://grupo1-proyecto.s3.sa-east-1.amazonaws.com/categorias/vegetariano.jpg");
        
        categoriaFotoMap.forEach((k,v) -> catRepo.save(new Categoria(k,v)));
        
        
        
        //Productos
        //bebidas
        String bebidasString= "Cocoroco,Vodka Spirytus,Absenta,Chinchón seco especial,Licor de cocuy,Bacanora,Mezcal,Whisky,Orujo blanco,Fernet,Coñac,Cachaza,Grappa,Aquavit,Becherovka,Gin de Menorca,Vodka,Ron,Tequila,Ouzo,Bourbon,Brandy,Ginebra,Chinchón,Jägermeister,Caña,Pisco,Aguardiente,Palo,Tía María,Limoncello,Punsch,Cherry Heering,Pacharán,Ratafia,Vino de Oporto,Vodka Azul o Rojo,Vino de arroz,Vermut,Jerez,Mariete,Vino,Pelin,Pulque,Cerveza,Chinchón conga,Sidra";
		List<String> bebidasList= Arrays.asList(bebidasString.split(","));
		List<Producto> bebidasProductoList =new ArrayList<Producto>();
				for(String s:bebidasList) {
					Producto p0= new Producto(s, "la más rica "+s, 150, "https://www.xlsemanal.com/wp-content/uploads/sites/3/2018/05/bebidas.jpg", 10,true);
					p0.setCategoria(catRepo.findById("Bebidas").get());
					bebidasProductoList.add(p0);
		}
				
				
		        String orientalString= "Pekinés,Sopa Wan Tan,Mapo doufu,Rollitos de primavera,Zongzi,Pollo Gong Bao o Kung Pao,Jiaozi,Wan Tan Mee,Chow Mein,Huo Guo o Hot Pot";
				List<String> orientalList= Arrays.asList(bebidasString.split(","));
				List<Producto> orientalProductoList =new ArrayList<Producto>();
						for(String s:orientalList) {
							Producto p0= new Producto(s, "埃斯特柏拉圖"+s, 150, "https://dam.cocinafacil.com.mx/wp-content/uploads/2019/06/palillos.chinos.jpg", 10,true);
							p0.setCategoria(catRepo.findById("Comida oriental").get());
							orientalProductoList.add(p0);
				}
						
						/////seguir agregando
				
				
					Producto p= new Producto("Milanesa", "la mejor del condado", 200.0, "https://t2.rg.ltmcdn.com/es/images/4/9/8/img_milanesa_de_carne_11894_orig.jpg", 10, true);
					p.setCategoria(catRepo.findById("Chivitos").get());
			Producto p1= new Producto("Helado", "el mejor del condado", 100.0, "https://i.blogs.es/098b7c/helados1/1366_2000.jpg", 10, true);
			p1.setCategoria(catRepo.findById("Helados").get());
			Producto p2= new Producto("hamburguesa", "la mejor del condado", 250.0, "https://cdn.computerhoy.com/sites/navi.axelspringer.es/public/styles/1200/public/media/image/2020/08/hamburguesa-2028707.jpg?itok=ujl3qgM9", 10, true);
			p2.setCategoria(catRepo.findById("Hamburguesas").get());
			

			
			//////////RESTAURANTE/////////
		String dirString= "Andes 1180,Prof. Bacigalupi 2244,Jaime Cibils 2878,Durazno 2116 entre Salterain y Requena,Cabo Polonio 2107,Lucas Obes 896,Pedro Fco. Berro 773 esq.Jaime Zudánez (Pocitos),8 de Octubre 2619,Av. Lezica 5831 casi Yegros,Mataojo 1862,Grecia 3194 y México,Av. Américo Ricaldoni 2804,Francisco Echagoyen 4949,8 de Octubre 3390, esquina Propios,Av. Arocena 1919,Gil 1065,Fernández Crespo 2274,Av. Millán 3898,20 de Febrero 2510 ,Ledo Arroyo Torres s/n casi Hernani,Enriqueta Compte y Riqué 1287,28 de Febrero 1097, esq. Elías Regules,Presbítero José Barrales 2500,Camino Maldonado 81201, Ruta 8 Km 16.800,Dr. Joaquín Requena 3010,18 de Julio 2205 esq. Alejandro Beisso,Bulevar España 2772, Esquina Ellauri,Blandengues 2020 esq.Constitución,José Batlle y Ordoñez 1401 esq. Rivera,18 de Diciembre 1600,Carlos Roxlo 1611, esquina Paysandú,Ana María Rubens 2324 esq. Camino Carrasco,18 de Julio 2205 esq. Alejandro Beisso,José Enrique Rodó 1875 casi Eduardo Acevedo,Camino Castro 711,Vasconcellos s/n esq. Osvaldo Cruz,Ruperto Pérez Martínez 882 ,Matilde Pacheco 4160,Andes 1180,Prof. Bacigalupi 2244,Jaime Cibils 2878,Durazno 2116 entre Salterain y Requena,Cabo Polonio 2107,Lucas Obes 896,Pedro Fco. Berro 773 esq.Jaime Zudánez (Pocitos),8 de Octubre 2619,Av. Lezica 5831 casi Yegros,Mataojo 1862,Grecia 3194 y México,Av. Américo Ricaldoni 2804,Francisco Echagoyen 4949,8 de Octubre 3390, esquina Propios,Av. Arocena 1919,Gil 1065,Fernández Crespo 2274,Av. Millán 3898,20 de Febrero 2510 ,Ledo Arroyo Torres s/n casi Hernani,Enriqueta Compte y Riqué 1287,28 de Febrero 1097, esq. Elías Regules,Presbítero José Barrales 2500,Camino Maldonado 81201, Ruta 8 Km 16.800,Dr. Joaquín Requena 3010,18 de Julio 2205 esq. Alejandro Beisso,Bulevar España 2772, Esquina Ellauri,Blandengues 2020 esq.Constitución,José Batlle y Ordoñez 1401 esq. Rivera";
		List<String> direccionesList= Arrays.asList(dirString.split(","));
		//RESTAURANTES
		String restoString= "La Pasiva,Sushi Go,La Taberna del Diablo,Burger King,Empanadas Mafalda,Il Mondo della Pizza,Fans,D' La Ribera,El Hornito,Grido,Tiqui Taca,Don Koto,Empanadas La Barca,Heladería Las Delicias,Pizza Trouville,Pizza Piedra,Pizzería Rodelú,Grazie Italia,Subway,Food & Love,La Isla,Soprano's,Chiviteria Marcos,Pizzería Cervantes,Sushiapp,Felipe,Chivipizza,El Club de la Papa Frita,OMG Fried Chicken,Cremona - Dicomo Pasta,Premium,Fábrica de Pastas La Bolognesa,Freddo,Lehmeyun 100%,La Roca,El Noble,Fellini,Artico,Barbacoa,Billie Joe,Gelateria del Club,Los Tavarez,Pizzeria Papa Jorge,Sushi Time,Pastas Baccino,Sinestesia,Crêpas,Tropical Smoothies,Chajá Bistro,San Roque,McDonald's,Chesterhouse,La Cigale,Homeopatía Alemana,Supermercados,Farmacias,Farmashop,Nescafé Dolce Gusto,Crepez,I love Tacos,Porto Vanila,Laika,Heladería La Chicharra,Iberpark,Farmacia El Tunel,Chéntola Gelato Artesanal,Sbarro,Fabric Sushi,Al Dente Pastas Artesanales,Del Abuelo Helados Artesanales,Alberto's,Cuidate - Comida Saludable,El Horno de Juan,El Novillo Alegre,Heladería Facal,Hoy te Quiero,Asian Food,Mimoso Resto Bar,Donut City,Hong Kong - Comida China,Mr. Kebap's,Veggie Mafalda,Rudy,Pizza Club,Pizza's House,Axion,Ciudad Aventura,Tomato Gourmet,Futuro Refuerzos,Pizzabrossa,Mascotas,Devoto,Poked,Noah's,The Lab Coffee,26 Sushi,Sabores,The Paletas Factory,La Vienesa,Paparike,Magnum,La Chacha Empanadas,Mise en place,Bar La Cruz,Lehmeyun Pizza Turca Armenia,Almacén de Pizzas,BIGA - Pizza y Pasta,II Gufo,Heladeria Pecas,Gaucho Burger,Emporio Gastronómico,Chivitos lo de Pepe,Facal,La Boletería,Burger Club,McCafé - McDonald's,Green To Go,Flores,Miyagi Sushi,Cafeterías,Pizza Mania,Bebidas,Bao bao,Tiendas,Wing It";
		List<String> restaurantesList= Arrays.asList(restoString.split(","));

		
		
		
		
		for(String resto: restaurantesList) {
			try {
				
				Integer numero = (int)(Math.random()*19+1);
				Integer i = (int)(Math.random()*3+1);
				List<Producto> productosRandom =new ArrayList<Producto>();
				productosRandom.add(bebidasProductoList.get(numero));
				productosRandom.add(bebidasProductoList.get(numero/2));
				productosRandom.add(bebidasProductoList.get(numero+1));
				productosRandom.add(bebidasProductoList.get(numero+2));
				
				productosRandom.add(p);
				productosRandom.add(p1);
				productosRandom.add(p2);

				productosRandom.add(orientalProductoList.get(numero/2));
				productosRandom.add(orientalProductoList.get(numero/3));
				
				
				
				
				
				crearRestaurantesDePrueba(new Restaurante(resto.replace(" ", "")+"@"+resto.replace(" ", "")+".com", "123456", numero.toString(), null, false, true,
						resto, direccionesList.get(numero), Float.valueOf(5), EnumEstadoRestaurante.values()[i-1],
						null, null, null, 20,
						null, productosRandom, "LMMJSD", true));
			} catch (RestauranteException | CategoriaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
    
		

		
	}
	
	public DTRespuesta crearRestaurantesDePrueba(Restaurante rest) throws RestauranteException, CategoriaException {

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
		//rest.setEstado(EnumEstadoRestaurante.EN_ESPERA);
		//rest.setFechaApertura(null);
//		rest.setProductos(null);
//		rest.setReclamos(null);
//		rest.setPedidos(null);
		LocalTime maximo = LocalTime.of(0,50,0);
		LocalTime minimo = LocalTime.of(0, 20,0);
		rest.setTiempoEstimadoMaximo(maximo);
		rest.setTiempoEstimadoMinimo(minimo);
		rest.setHorarioApertura(LocalTime.of(10,0,0));
		rest.setHorarioCierre(LocalTime.of(20, 0, 0));
		
		
		rest.setAbierto(true);
		rest.setContrasenia(passwordEncoder.encode(rest.getContrasenia()));

		restauranteRepo.save(rest);
		return new DTRespuesta("Restaurante " + rest.getNombre() + " dado de alta correctamente.");
	}
}
