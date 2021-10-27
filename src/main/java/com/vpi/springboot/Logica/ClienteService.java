package com.vpi.springboot.Logica;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.vpi.springboot.IdCompuestas.CalificacionRestauranteId;
import com.vpi.springboot.Modelo.Calificacion;
import com.vpi.springboot.Modelo.CalificacionRestaurante;
import com.vpi.springboot.Modelo.Carrito;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.Modelo.GeoLocalizacion;

import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Reclamo;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.LastDireccioClientenMongo;
import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.dto.DTBuscarRestaurante;
import com.vpi.springboot.Modelo.dto.DTCarrito;
import com.vpi.springboot.Modelo.dto.DTCliente;
import com.vpi.springboot.Modelo.dto.DTDireccion;
import com.vpi.springboot.Modelo.dto.DTListarRestaurante;
import com.vpi.springboot.Modelo.dto.DTPedido;
import com.vpi.springboot.Modelo.dto.DTPedidoParaAprobar;
import com.vpi.springboot.Modelo.dto.DTProducto;
import com.vpi.springboot.Modelo.dto.DTProductoCarrito;
import com.vpi.springboot.Modelo.dto.DTReclamo;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.EnumEstadoPedido;
import com.vpi.springboot.Modelo.dto.EnumEstadoReclamo;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;
import com.vpi.springboot.Repositorios.CalificacionRestauranteRepositorio;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.DireccionRepositorio;
import com.vpi.springboot.Repositorios.GeoLocalizacionRepositorio;

import com.vpi.springboot.Repositorios.MongoRepositorioCarrito;
import com.vpi.springboot.Repositorios.PedidoRepositorio;
import com.vpi.springboot.Repositorios.ProductoRepositorio;
import com.vpi.springboot.Repositorios.ReclamoRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.exception.CarritoException;
import com.vpi.springboot.exception.DireccionException;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.ReclamoException;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.PedidoException;
import com.vpi.springboot.Repositorios.mongo.UltimaDireccionRepositorio;

import com.vpi.springboot.exception.UsuarioException;
import org.springframework.beans.factory.ObjectFactory;

@Service
public class ClienteService implements ClienteServicioInterfaz {

	@Autowired
	ObjectFactory<HttpSession> httpSessionFactory;

	@Autowired
	private ClienteRepositorio userRepo;
	@Autowired
	private DireccionRepositorio dirRepo;
	@Autowired
	private GeoLocalizacionRepositorio geoRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private MongoRepositorioCarrito mongoRepo;
	@Autowired
	private ProductoRepositorio productoRepo;
	@Autowired
	private PedidoRepositorio pedidoRepo;
	@Autowired
	private RestauranteRepositorio restauranteRepo;
	@Autowired
	private CalificacionRestauranteRepositorio calRestauranteRepo;
	@Autowired
	private ReclamoRepositorio recRepo;
	@Autowired
	private NextSequenceService nextSequence;
	@Autowired
	private UltimaDireccionRepositorio ultimaDireccionRepo;
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	private DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");;

	@Override
	public List<DTDireccion> getDireccionCliente(String mail) throws UsuarioException {
		Optional<Cliente> optionalUser = userRepo.findById(mail);
		if (optionalUser.isPresent()) {
			Cliente cliente = optionalUser.get();
			List<DTDireccion> retorno = new ArrayList<DTDireccion>();
			if (cliente.getDirecciones() != null) {
				for (Direccion direccion : cliente.getDirecciones()) {
					retorno.add(new DTDireccion(direccion.getId(), direccion.getCalleNro(),
							direccion.getGeoLocalizacion()));
				}
				return retorno;
			} else {
				throw new UsuarioException("El usuario no tiene direcciones");
			}
		} else {
			throw new UsuarioException(UsuarioException.NotFoundException(mail));
		}
	}

	@Override
	public DTRespuesta altaCliente(Cliente usuario) throws UsuarioException, Exception {
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
	public List<Cliente> obtenerClientes() {
		Iterable<Cliente> usuario = userRepo.findAll();
		List<Cliente> clientes = new ArrayList<Cliente>();
		usuario.forEach(c -> clientes.add(c));

		return clientes;
	}

	@Override
	public DTRespuesta altaDireccion(DTDireccion direccion, String mail) throws UsuarioException {
		Optional<Cliente> optionalCliente = userRepo.findById(mail);
		if (optionalCliente.isPresent()) {
			Cliente cliente = optionalCliente.get();
			GeoLocalizacion geo = new GeoLocalizacion(direccion.getGeoLocalizacion().getLatitud(),
					direccion.getGeoLocalizacion().getLongitud());
			geoRepo.save(geo);
			Direccion dir = new Direccion(direccion.getCalleNro(), geo);
			if (mail != null) {
				dir.setCliente(cliente);
			}
			dirRepo.save(dir);
			if (cliente.getDirecciones() == null) {
				List<Direccion> direcciones = new ArrayList<Direccion>();
				direcciones.add(dir);
				cliente.setDirecciones(direcciones);
			} else {
				cliente.addDireccion(dir);
			}
			userRepo.save(cliente);

			// actualiza ultima direccion en mongo
			setUltimaDireccionSeleccionada(dir.getId(), mail);
			return new DTRespuesta("Dirección agregada.");
		} else {
			throw new UsuarioException(UsuarioException.NotFoundException(mail));
		}

	}

	@Override
	public DTRespuesta bajaCuenta(String mail) throws UsuarioException {
		Optional<Cliente> optionalCliente = userRepo.findById(mail);
		if (optionalCliente.isPresent()) {
			Cliente cliente = optionalCliente.get();
			if (cliente.getActivo() != false) {
				cliente.setActivo(false);
				userRepo.save(cliente);
				return new DTRespuesta("Cuenta dada de baja con éxito.");
			} else {
				throw new UsuarioException("El usuario " + mail + " ya esta inactivo");
			}
		} else {
			throw new UsuarioException("No existe usuario");
		}
	}

	@Override
	public DTRespuesta modificarDireccion(int id, DTDireccion nueva, String mail) throws UsuarioException {
		Optional<Cliente> optionalCliente = userRepo.findById(mail);
		if (optionalCliente.isPresent()) {
			Cliente cliente = optionalCliente.get();
			Optional<Direccion> optionalDireccion = dirRepo.findById(id);
			if (optionalDireccion.isPresent()) {
				Direccion dirNueva = optionalDireccion.get();
				dirNueva.setCalleNro(nueva.getCalleNro());
				dirNueva.setGeoLocalizacion(new GeoLocalizacion(nueva.getGeoLocalizacion()));
				dirRepo.save(dirNueva);

				// actualiza ultima direccion en mongo
				setUltimaDireccionSeleccionada(dirNueva.getId(), mail);
				return new DTRespuesta("Dirección modificada.");

			} else {
				throw new UsuarioException("No existe direccion");
			}
		} else {
			throw new UsuarioException("No existe cliente");
		}
	}

	@Override
	public DTRespuesta eliminarDireccion(Integer id, String mail) throws UsuarioException {
		Optional<Cliente> optionalCliente = userRepo.findById(mail);
		if (optionalCliente.isPresent()) {
			Cliente cliente = optionalCliente.get();
			Optional<Direccion> optionalDireccion = dirRepo.findById(id);
			if (optionalDireccion.isPresent()) {
				Direccion dir = optionalDireccion.get();
				dirRepo.delete(dir);

				// actualiza ultima direccion en mongo
				setUltimaDireccionSeleccionada(null, mail);
				return new DTRespuesta("Dirección eliminada.");
			} else {
				throw new UsuarioException("No existe direccion");
			}
		} else {
			throw new UsuarioException("No existe cliente");
		}
	}

	@Override
	public DTRespuesta agregarACarrito(int producto, int cantidad, String mail, String mailRestaurante)
			throws ProductoException {
		Optional<Producto> optionalProducto = productoRepo.findById(producto);
		if (optionalProducto.isPresent()) {
			DTProducto dtProducto = new DTProducto(optionalProducto.get());
			int descuento = dtProducto.getDescuento();
			double precioInicial = dtProducto.getPrecio();
			double precio = 0;
			if (descuento > 0) {
				precio = precioInicial - ((precioInicial * descuento) / 100);
				dtProducto.setPrecio(precio);
			}
			Carrito optionalCarrito = mongoRepo.findByMailAndActivo(mail, true);
			DTProductoCarrito dtPC = new DTProductoCarrito(dtProducto, cantidad);
			Restaurante restaurante = restauranteRepo.getById(mailRestaurante);
			Boolean existeProd = false;
			if (optionalCarrito != null) { // TIENE CARRITO ACTIVO
				for (DTProductoCarrito dtp : optionalCarrito.getProductoCarrito()) {
					if (dtp.getProducto().getId() == producto) {
						existeProd = true;
						dtp.setCantidad(cantidad + dtp.getCantidad());
					}
				}
				if (existeProd == false) {
					optionalCarrito.addProductoCarrito(dtPC);
				}
				mongoRepo.save(optionalCarrito);
			} else { // TIENE CARRITO INACTIVO O NO TIENE
				List<DTProductoCarrito> productos = new ArrayList<DTProductoCarrito>();
				productos.add(dtPC);
				Carrito carrito = new Carrito(restaurante.getCostoDeEnvio(), mail, mailRestaurante, productos, true);
				carrito.setId(nextSequence.getNextSequence("customSequences"));
				mongoRepo.save(carrito);
			}
			return new DTRespuesta("Carrito actualizado.");
		} else {
			throw new ProductoException(ProductoException.NotFoundExceptionId(producto));
		}
	}

	@Override
	public DTCarrito verCarrito(String mail) {
		Carrito optionalCarrito = mongoRepo.findByMailAndActivo(mail, true);
		DTCarrito carrito = new DTCarrito(optionalCarrito.getId(), optionalCarrito.getProductoCarrito(),
				optionalCarrito.getMailRestaurante(), optionalCarrito.getCostoEnvio());
		return carrito;
	}

	public String getUltimaDireccionSeleccionada(String mail) {
		Optional<LastDireccioClientenMongo> direccion = ultimaDireccionRepo.findById(mail);
		return direccion.isPresent() ? direccion.get().getIdDireccion().toString() : null;
	}

	public void setUltimaDireccionSeleccionada(Integer idDireccion, String mail) {
		LastDireccioClientenMongo actualDire = new LastDireccioClientenMongo();
		actualDire.setIdDireccion(idDireccion);
		actualDire.set_id(mail);

		ultimaDireccionRepo.save(actualDire);
	}

	@Override
	public DTPedido altaPedido(int idCarrito, EnumMetodoDePago pago, int idDireccion, String mail, String comentario)
			throws RestauranteException, CarritoException, DireccionException {
		Optional<Cliente> optionalCliente = userRepo.findById(mail);
		Cliente cliente = optionalCliente.get();
		Optional<Carrito> optionalCarrito = mongoRepo.findById(idCarrito);
		Carrito carrito = optionalCarrito.get();
		if (optionalCarrito.isPresent()) {
			Optional<Restaurante> optionalRestaurante = restauranteRepo.findById(carrito.getMailRestaurante());
			if (optionalRestaurante.isPresent()) {
				Restaurante restaurante = optionalRestaurante.get();
				Optional<Direccion> optionalDireccion = dirRepo.findById(idDireccion);
				if (optionalDireccion.isPresent()) {
					String direccion = optionalDireccion.get().getCalleNro();
					EnumEstadoPedido estado = EnumEstadoPedido.PROCESADO;
					double precioTotal = 0;
					for (DTProductoCarrito DTpc : carrito.getProductoCarrito()) {
						precioTotal = precioTotal + (DTpc.getProducto().getPrecio() * DTpc.getCantidad());
					}
					precioTotal = precioTotal + restaurante.getCostoDeEnvio();
					Pedido pedido = new Pedido(LocalDateTime.now(), precioTotal, estado, pago, idCarrito, direccion,
							restaurante, cliente, comentario);
					if(pago.equals(EnumMetodoDePago.PAYPAL)) {
						pedido.setPago(true);
					}else if(pago.equals(EnumMetodoDePago.EFECTIVO)) {
						pedido.setPago(false);
					}
					pedidoRepo.save(pedido);
					// AGREGAR PEDIDO AL RESTAURANTE
					if (restaurante.getPedidos() == null) {
						List<Pedido> pedidos = new ArrayList<Pedido>();
						pedidos.add(pedido);
						restaurante.setPedidos(pedidos);
					} else {
						restaurante.addPedido(pedido);
					}
					restauranteRepo.save(restaurante);
					// AGREGAR PEDIDO AL CLIENTE
					if (cliente.getPedidos() == null) {
						List<Pedido> pedidosCliente = new ArrayList<Pedido>();
						pedidosCliente.add(pedido);
						cliente.setPedidos(pedidosCliente);
					} else {
						cliente.addPedido(pedido);
					}
					userRepo.save(cliente);
					carrito.setActivo(false);
					mongoRepo.save(carrito);
					// return new DTRespuesta("Pedido agregado correctamente.");

					// notificamos al restaurante
					// Push notifications to front-end

					String base64EncodedEmail = Base64.getEncoder()
							.encodeToString(pedido.getRestaurante().getMail().getBytes(StandardCharsets.UTF_8));
					DTPedidoParaAprobar pedidoDT = new DTPedidoParaAprobar(pedido);
					pedidoDT.setComentario(comentario);
					pedidoDT.setDireccion(pedido.getDireccion());
					pedidoDT.setCarrito(carrito.getProductoCarrito());
					pedidoDT.setCliente(new DTCliente(pedido.getCliente()));

					simpMessagingTemplate.convertAndSend("/topic/" + base64EncodedEmail, pedidoDT);

					// System.out.println(base64EncodedEmail);

					// simpMessagingTemplate.convertAndSendToUser(base64EncodedEmail,
					// "/topic/pedido", pedidoDTO);

					return new DTPedido(pedido);
				} else {
					throw new DireccionException(DireccionException.NotFoundExceptionId(idDireccion));
				}
			} else {
				throw new RestauranteException(
						RestauranteException.NotFoundExceptionMail(carrito.getMailRestaurante()));
			}
		} else {
			throw new CarritoException(CarritoException.NotFoundExceptionId(idCarrito));

		}
	}

	@Override
	public DTRespuesta altaReclamo(int idPedido, String mailCliente, String comentario) throws ReclamoException {

		Optional<Pedido> pedidoOptional = pedidoRepo.findById(idPedido);
		LocalDateTime now = LocalDateTime.now();
		if (pedidoOptional.isPresent()) {

			Pedido pedido = pedidoOptional.get();

			if (pedido.getFecha().plusHours(24).isBefore(now)) {

				throw new ReclamoException(ReclamoException.TooOldPedido(idPedido));
			} else if (!pedido.getCliente().getMail().contains(mailCliente)) {

				throw new ReclamoException(ReclamoException.UserPedidoException(idPedido, mailCliente));
			}
			/**
			 * guarda el reclamo y envia mail
			 */

			List<Reclamo> reclamos = pedido.getReclamos();
			reclamos.add(new Reclamo(comentario, now, EnumEstadoReclamo.ENVIADO, ""));
			pedido.setReclamos(reclamos);
			pedidoRepo.save(pedido);
			return new DTRespuesta(
					"Reclamo ingresado con éxito. Le llegará un mail con la resulución. Disculpe las molestias.");
		} else {
			throw new ReclamoException(ReclamoException.PedidoNotFound(idPedido));
		}
	}

	@Override
	public DTRespuesta eliminarProductoCarrito(int idProducto, int cantABorrar, String mail) {
		Carrito optionalCarrito = mongoRepo.findByMailAndActivo(mail, true);
		List<DTProductoCarrito> dtp = optionalCarrito.getProductoCarrito();
		DTProductoCarrito dtpcBorrar = null;
		for (DTProductoCarrito dt : dtp) {
			if (dt.getProducto().getId() == idProducto && dt.getCantidad() == cantABorrar) {
				dtpcBorrar = dt;
			} else if (dt.getProducto().getId() == idProducto) {
				dt.setCantidad(dt.getCantidad() - cantABorrar);
			}
		}
		optionalCarrito.deleteDTProductoCarrito(dtpcBorrar);
		if (optionalCarrito.getProductoCarrito().isEmpty()) {
			mongoRepo.delete(optionalCarrito);
		} else
			mongoRepo.save(optionalCarrito);

		return new DTRespuesta("Producto eliminado con éxito");
	}

	@Override
	public DTRespuesta eliminarCarrito(int idCarrito, String mail) throws CarritoException {
		Carrito optionalCarrito = mongoRepo.findByMailAndActivo(mail, true);
		if (optionalCarrito != null) {
			mongoRepo.delete(optionalCarrito);
			return new DTRespuesta("Carrito eliminado con éxito");
		} else {
			throw new CarritoException(CarritoException.NotFoundExceptionId(idCarrito));
		}
	}

	// TODO ordenamiento: fecha, precio
	// TODO filtro: fecha, restaurante
	@Override
	public Map<String, Object> listarPedidos(int size, int page, String varRestaurante, String fecha, String sort,
			int order, String mailUsuario) throws UsuarioException {
		Optional<Cliente> optionalCliente = userRepo.findById(mailUsuario);
		if (!optionalCliente.isPresent())
			throw new UsuarioException(UsuarioException.NotFoundException(mailUsuario));
		Cliente cliente = optionalCliente.get();

		Map<String, Object> response = new HashMap<>();
		List<DTPedido> retorno = new ArrayList<>();
		Pageable paging;
		
		if (sort.equalsIgnoreCase("")) {
			paging = PageRequest.of(page, size);
		} else {
			if (order == 1)
				paging = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sort)));
			else
				paging = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sort)));
		}
		
		Page<Pedido> pagePedido;
		List<Pedido> pedidos = new ArrayList<>();

		if (!varRestaurante.equalsIgnoreCase("") || !fecha.equalsIgnoreCase("")) {
			// restaurante + fecha
			if (!varRestaurante.equalsIgnoreCase("") && !fecha.equalsIgnoreCase("")) {
				LocalDate ld = LocalDate.parse(fecha, DATEFORMATTER);
				LocalDateTime dateI = LocalDateTime.of(ld, LocalTime.of(00, 01));
				LocalDateTime dateF = LocalDateTime.of(ld, LocalTime.of(23, 59));
				
				pagePedido = pedidoRepo.findByRestauranteFecha(varRestaurante, dateI, dateF, cliente, paging);
			}

			// restaurante
			else if (!varRestaurante.equalsIgnoreCase("")) {
				pagePedido = pedidoRepo.findByRestaurante(varRestaurante, cliente, paging);
			}

			// fecha
			else if (!fecha.equalsIgnoreCase("")) {
				LocalDate ld = LocalDate.parse(fecha, DATEFORMATTER);
				LocalDateTime dateI = LocalDateTime.of(ld, LocalTime.of(00, 01));
				LocalDateTime dateF = LocalDateTime.of(ld, LocalTime.of(23, 59));
				
				pagePedido = pedidoRepo.findByFecha(dateI, dateF, cliente, paging);
			} else
				pagePedido = pedidoRepo.findAllByCliente(cliente, paging);
		} else {
			pagePedido = pedidoRepo.findAllByCliente(cliente, paging);
		}

		pedidos = pagePedido.getContent();
		response.put("currentPage", pagePedido.getNumber());
		response.put("totalItems", pagePedido.getTotalElements());
		
		for (Pedido p : pedidos) {
			retorno.add(new DTPedido(p));
		}
		
		response.put("pedidos", retorno);

		return response;
	}

	@Override
	public DTRespuesta calificarRestaurante(String mailCliente, String mailRestaurante, Calificacion calificacion)
			throws UsuarioException, RestauranteException {
		Optional<Cliente> optionalCliente = userRepo.findById(mailCliente);
		if (!optionalCliente.isPresent())
			throw new UsuarioException(UsuarioException.NotFoundException(mailCliente));
		Cliente cliente = optionalCliente.get();

		Optional<Restaurante> optionalRestaurante = restauranteRepo.findById(mailRestaurante);
		if (!optionalRestaurante.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionMail(mailRestaurante));
		}
		Restaurante restaurante = optionalRestaurante.get();

		calificacion.setFecha(LocalDateTime.now());
		CalificacionRestaurante calRestaurante = new CalificacionRestaurante(calificacion, cliente, restaurante);
		calRestauranteRepo.save(calRestaurante);

		// Calculamos la calificación del cliente y la guardamos
		List<CalificacionRestaurante> calificaciones = calRestauranteRepo.findByRestaurante(restaurante);
		float avg = 0;
		for (CalificacionRestaurante c : calificaciones) {
			avg += c.getPuntaje();
		}
		avg /= calificaciones.size();
		restaurante.setCalificacionPromedio(avg);
		restauranteRepo.save(restaurante);

		return new DTRespuesta("Restaurante " + mailRestaurante + " calificado correctamente");
	}

	@Override
	public DTRespuesta bajaCalificacionRestaurante(String mailCliente, String mailRestaurante)
			throws UsuarioException, RestauranteException {
		Optional<Cliente> optionalCliente = userRepo.findById(mailCliente);
		if (!optionalCliente.isPresent())
			throw new UsuarioException(UsuarioException.NotFoundException(mailCliente));
		Cliente cliente = optionalCliente.get();

		Optional<Restaurante> optionalRestaurante = restauranteRepo.findById(mailRestaurante);
		if (!optionalRestaurante.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionMail(mailRestaurante));
		}
		Restaurante restaurante = optionalRestaurante.get();

		Optional<CalificacionRestaurante> optionalCalificacion = calRestauranteRepo
				.findById(new CalificacionRestauranteId(cliente, restaurante));
		if (!optionalCalificacion.isPresent())
			throw new UsuarioException(UsuarioException.SinCalificacion(restaurante.getNombre()));
		CalificacionRestaurante calRestaurante = optionalCalificacion.get();
		calRestauranteRepo.delete(calRestaurante);

		List<CalificacionRestaurante> calificaciones = calRestauranteRepo.findByRestaurante(restaurante);
		float avg = 0f;
		if (calificaciones.size() > 0) {
			for (CalificacionRestaurante c : calificaciones) {
				avg += c.getPuntaje();
			}
		} else
			avg = 0.5f;

		restaurante.setCalificacionPromedio(avg);
		restauranteRepo.save(restaurante);

		return new DTRespuesta(
				"Calificacion de restaurante + " + restaurante.getNombre() + " eliminada correctamente.");
	}

	public Map<String, Object> listarReclamos(int size, int page, String restaurante, String sort, int order, String mailCliente)
			throws UsuarioException {
		Optional<Cliente> optionalCliente = userRepo.findById(mailCliente);
		if (!optionalCliente.isPresent())
			throw new UsuarioException(UsuarioException.NotFoundException(mailCliente));
		Cliente cliente = optionalCliente.get();

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

		if(!restaurante.equalsIgnoreCase("")) {
			pageReclamo = recRepo.findAllByClienteRestaurante(cliente, restaurante, paging);
		} else {
			pageReclamo = recRepo.findAllByCliente(cliente, paging);
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
	public DTPedido buscarPedidoRealizado(int numeroPedido) throws PedidoException {
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
}