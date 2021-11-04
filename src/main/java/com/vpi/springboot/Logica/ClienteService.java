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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Service;

import com.vpi.springboot.IdCompuestas.CalificacionRestauranteId;
import com.vpi.springboot.Modelo.Calificacion;
import com.vpi.springboot.Modelo.CalificacionCliente;
import com.vpi.springboot.Modelo.CalificacionRestaurante;
import com.vpi.springboot.Modelo.Carrito;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.Modelo.GeoLocalizacion;

import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Reclamo;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.TokenVerificacion;
import com.vpi.springboot.Modelo.LastDireccioClientenMongo;
import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.dto.DTCalificacionCliente;
import com.vpi.springboot.Modelo.dto.DTCalificacionRestaurante;
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
import com.vpi.springboot.Repositorios.CalificacionClienteRepositorio;
import com.vpi.springboot.Repositorios.CalificacionRestauranteRepositorio;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.DireccionRepositorio;
import com.vpi.springboot.Repositorios.GeoLocalizacionRepositorio;

import com.vpi.springboot.Repositorios.MongoRepositorioCarrito;
import com.vpi.springboot.Repositorios.PedidoRepositorio;
import com.vpi.springboot.Repositorios.ProductoRepositorio;
import com.vpi.springboot.Repositorios.ReclamoRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.Repositorios.VerificacionRepositorio;
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
	private CalificacionClienteRepositorio calClienteRepo;
	@Autowired
	private ReclamoRepositorio recRepo;
	@Autowired
	private VerificacionRepositorio tokenRepo;
	@Autowired
	private UltimaDireccionRepositorio ultimaDireccionRepo;
	@Autowired
	private NextSequenceService nextSequence;
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	private MailService mailSender;

	private DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private String getMailVerificacion(String link) {
		return "<html>\r\n"
				+ "<body style=\"background-color: #f4f4f4; margin: 0 !important; padding: 0 !important;\">\r\n"
				+ "    <div style=\"display: none; font-size: 1px; color: #fefefe; line-height: 1px; font-family: 'Lato', Helvetica, Arial, sans-serif; max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden;\"> We're thrilled to have you here! Get ready to dive into your new account. </div>\r\n"
				+ "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + "        <tr>\r\n"
				+ "            <td bgcolor=\"#FFA73B\" align=\"center\">\r\n"
				+ "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td align=\"center\" valign=\"top\" style=\"padding: 40px 10px 40px 10px;\"> </td>\r\n"
				+ "                    </tr>\r\n" + "                </table>\r\n" + "            </td>\r\n"
				+ "        </tr>\r\n" + "        <tr>\r\n"
				+ "            <td bgcolor=\"#FFA73B\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\r\n"
				+ "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td bgcolor=\"#ffffff\" align=\"center\" valign=\"top\" style=\"padding: 40px 20px 20px 20px; border-radius: 4px 4px 0px 0px; color: #111111; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 48px; font-weight: 400; letter-spacing: 4px; line-height: 48px;\">\r\n"
				+ "                            <h1 style=\"font-size: 48px; font-weight: 400; margin: 2;\">¡Bienvenido a Yendo!</h1>\r\n"
				+ "                        </td>\r\n" + "                    </tr>\r\n" + "                </table>\r\n"
				+ "            </td>\r\n" + "        </tr>\r\n" + "        <tr>\r\n"
				+ "            <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\r\n"
				+ "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 20px 30px 40px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\r\n"
				+ "                            <p style=\"margin: 0;\">Para poder comenzar a usar nuestros servicios, por favor verifique su cuenta.</p>\r\n"
				+ "                        </td>\r\n" + "                    </tr>\r\n" + "                    <tr>\r\n"
				+ "                        <td bgcolor=\"#ffffff\" align=\"left\">\r\n"
				+ "                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n"
				+ "                                <tr>\r\n"
				+ "                                    <td bgcolor=\"#ffffff\" align=\"center\" style=\"padding: 20px 30px 60px 30px;\">\r\n"
				+ "                                        <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td align=\"center\" style=\"border-radius: 3px;\" bgcolor=\"#FFA73B\"><a href=\""
				+ link
				+ "\" target=\"\" style=\"font-size: 20px; font-family: Helvetica, Arial, sans-serif; color: #ffffff; text-decoration: none; color: #ffffff; text-decoration: none; padding: 15px 25px; border-radius: 2px; border: 1px solid #FFA73B; display: inline-block;\">Confirmar Cuenta</a></td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                        </table>\r\n"
				+ "                                    </td>\r\n" + "                                </tr>\r\n"
				+ "                            </table>\r\n" + "                        </td>\r\n"
				+ "                    </tr>\r\n" + "                    <tr>\r\n"
				+ "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 0px 30px 20px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\r\n"
				+ "                            <p style=\"margin: 0;\">En caso de tener alguna consulta, por favor acudir a nuestro centro de atención al cliente.</p>\r\n"
				+ "                        </td>\r\n" + "                    </tr>\r\n" + "                    <tr>\r\n"
				+ "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 0px 30px 40px 30px; border-radius: 0px 0px 4px 4px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\r\n"
				+ "                            <p style=\"margin: 0;\">Saludos,<br> Yendo team</p>\r\n"
				+ "                        </td>\r\n" + "                    </tr>\r\n" + "                </table>\r\n"
				+ "            </td>\r\n" + "        </tr>\r\n" + "    </table>\r\n" + "</body>\r\n" + "</html>";
	}

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
			usuario.setVerificado(false);
			usuario.setSaldoBono(0.0f);
			usuario.setCalificacionPromedio(5.0f);
			usuario.setFechaCreacion(LocalDate.now());
			usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));

			try {
				userRepo.save(usuario);
			} catch (Exception e) {
				throw new UsuarioException("Ya existe un usuario con el nickname " + usuario.getNickname());
			}

			try {
				TokenVerificacion token = new TokenVerificacion(usuario);
				tokenRepo.save(token);

				String to = usuario.getMail();
				String body = getMailVerificacion("https://www.youtube.com/");
				String topic = "Verificación de usuario " + usuario.getNickname() + ".";
				mailSender.sendMail(to, body, topic);
			} catch (Exception e) {
				return new DTRespuesta("Error: " + e.getMessage());
			}

			return new DTRespuesta(
					"Cliente dado de alta con éxito. Se ha enviado un mail para confirmación del usuario.");

		} else {
			throw new UsuarioException("Mail, nickname y contraseña son campos obligatorios");
		}
	}

	private boolean emailExist(String mail) {
		return userRepo.findById(mail).isPresent();
	}

	/*
	 * @Override public List<Cliente> obtenerClientes() { Iterable<Cliente> usuario
	 * = userRepo.findAll(); List<Cliente> clientes = new ArrayList<Cliente>();
	 * usuario.forEach(c -> clientes.add(c));
	 *
	 * return clientes; }
	 */

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

				//Elimina las calificaciones
				List<CalificacionCliente> calificacionesCliente = calClienteRepo.findByCliente(cliente);
				for(CalificacionCliente c : calificacionesCliente)
					calClienteRepo.delete(c);

				List<CalificacionRestaurante> calificacionesRestaurante = calRestauranteRepo.findByCliente(cliente);
				for(CalificacionRestaurante c : calificacionesRestaurante)
					calRestauranteRepo.delete(c);

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
			// Cliente cliente = optionalCliente.get();
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
			// Cliente cliente = optionalCliente.get();
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
			throws ProductoException, RestauranteException {
		Restaurante restaurante = restauranteRepo.getById(mailRestaurante);
		if(restaurante.getAbierto() == true) {
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
				//Restaurante restaurante = restauranteRepo.getById(mailRestaurante);
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
		}else {
			throw new RestauranteException("El restaurante esta cerrado.");
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
					if (pago.equals(EnumMetodoDePago.PAYPAL)) {
						pedido.setPago(true);
					} else if (pago.equals(EnumMetodoDePago.EFECTIVO)) {
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

			Restaurante restaurante = pedido.getRestaurante();
			// reclamo
			List<Reclamo> reclamos = pedido.getReclamos();
			Reclamo reclamo = new Reclamo(comentario, now, EnumEstadoReclamo.ENVIADO, "");
			reclamo.setPedido(pedido);
			reclamo.setRestaurante(restaurante);
			reclamos.add(reclamo);

			// pedido
			pedido.setReclamos(reclamos);

			// restaurante
			List<Reclamo> reclamosResto = restaurante.getReclamos();
			restaurante.setReclamos(reclamosResto);

			// pedidoRepo.save(pedido);
			recRepo.save(reclamo);
			// restauranteRepo.save(restaurante);
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

		List<Pedido> pedidos = pedidoRepo.findByClienteRestaurante(cliente, restaurante);
		if (pedidos.size() == 0)
			throw new UsuarioException(UsuarioException.SinPedido(mailRestaurante));

		if(calificacion.getPuntaje() > 5)
			calificacion.setPuntaje(5);
		else if (calificacion.getPuntaje() < 1)
			calificacion.setPuntaje(1);

		calificacion.setFecha(LocalDateTime.now());
		CalificacionRestaurante calRestaurante = new CalificacionRestaurante(calificacion, cliente, restaurante);
		calRestauranteRepo.save(calRestaurante);

		// Calculamos la calificación del restaurante y la guardamos
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

		List<Pedido> pedidos = pedidoRepo.findByClienteRestaurante(cliente, restaurante);
		if (pedidos.size() == 0)
			throw new UsuarioException(UsuarioException.SinPedido(mailRestaurante));

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
			avg = 5.0f;

		restaurante.setCalificacionPromedio(avg);
		restauranteRepo.save(restaurante);

		return new DTRespuesta(
				"Calificacion de restaurante + " + restaurante.getNombre() + " eliminada correctamente.");
	}

	@Override
	public Map<String, Object> listarReclamos(int size, int page, String estado, String fecha, String restaurante, String sort, int order,
			String mailCliente) throws UsuarioException {
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

		if (!restaurante.equalsIgnoreCase("") || !estado.equalsIgnoreCase("") || !fecha.equalsIgnoreCase("")) {
			// restaurante + estado + fecha
			if (!restaurante.equalsIgnoreCase("") && !estado.equalsIgnoreCase("") && !fecha.equalsIgnoreCase("")) {
				EnumEstadoReclamo estadoReclamo = EnumEstadoReclamo.valueOf(estado.toUpperCase());
				LocalDate ld = LocalDate.parse(fecha, DATEFORMATTER);
				LocalDateTime dateI = LocalDateTime.of(ld, LocalTime.of(00, 00));
				LocalDateTime dateF = LocalDateTime.of(ld, LocalTime.of(23, 59));
				pageReclamo = recRepo.findAllByClienteRestauranteEstadoFecha(cliente, restaurante, estadoReclamo, dateI,
						dateF, paging);
			}

			// restaurante + estado
			else if (!restaurante.equalsIgnoreCase("") && !estado.equalsIgnoreCase("")) {
				EnumEstadoReclamo estadoReclamo = EnumEstadoReclamo.valueOf(estado.toUpperCase());
				pageReclamo = recRepo.findAllByClienteRestauranteEstado(cliente, restaurante, estadoReclamo, paging);
			}

			// restaurante + fecha
			else if (!restaurante.equalsIgnoreCase("") && !fecha.equalsIgnoreCase("")) {
				LocalDate ld = LocalDate.parse(fecha, DATEFORMATTER);
				LocalDateTime dateI = LocalDateTime.of(ld, LocalTime.of(00, 00));
				LocalDateTime dateF = LocalDateTime.of(ld, LocalTime.of(23, 59));
				pageReclamo = recRepo.findAllByClienteRestauranteFecha(cliente, restaurante, dateI, dateF, paging);
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
				pageReclamo = recRepo.findAllByClienteEstadoFecha(cliente, estadoReclamo, dateI,
						dateF, paging);
			}

			// restaurante
			else if (!restaurante.equalsIgnoreCase("")) {
				pageReclamo = recRepo.findAllByClienteRestaurante(cliente, restaurante, paging);
			}

			// estado
			else if (!estado.equalsIgnoreCase("")) {
				EnumEstadoReclamo estadoReclamo = EnumEstadoReclamo.valueOf(estado.toUpperCase());
				pageReclamo = recRepo.findAllByClienteEstado(cliente, estadoReclamo, paging);
			}

			// fecha
			else if (!fecha.equalsIgnoreCase("")) {
				LocalDate ld = LocalDate.parse(fecha, DATEFORMATTER);
				LocalDateTime dateI = LocalDateTime.of(ld, LocalTime.of(00, 00));
				LocalDateTime dateF = LocalDateTime.of(ld, LocalTime.of(23, 59));
				pageReclamo = recRepo.findAllByClienteFecha(cliente, dateI, dateF, paging);

			} else { //genérico
				pageReclamo = recRepo.findAllByCliente(cliente, paging);
			}
		} else { //genérico
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
				Optional<Carrito> optionalCarrito = mongoRepo.findById(pedido.getCarrito());
				if (optionalCarrito.isPresent())
					DTpedido = new DTPedido(pedido, new DTCarrito(optionalCarrito.get()));
				else
					DTpedido = new DTPedido(pedido);
			}
		} else {
			throw new PedidoException(PedidoException.NotValidId());
		}

		return DTpedido;
	}

	//ConsultarCalificacionesCliente
	@Override
	public Map<String, Object> consultarCalificacion(int page, int size, String sort, int order, String mailCliente) {
		Map<String, Object> response = new HashMap<>();
		//List<DTCalificacionRestaurante> DTCalificacionesRestaurante = new ArrayList<DTCalificacionRestaurante>();
		//List<CalificacionRestaurante> calificacionRestaurantes = new ArrayList<CalificacionRestaurante>();
		List<DTCalificacionCliente> DTCalificacionesCliente = new ArrayList<DTCalificacionCliente>();
		List<CalificacionCliente> calificacionClientes = new ArrayList<CalificacionCliente>();

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

		Optional<Cliente> optionalCliente = userRepo.findById(mailCliente);
		Cliente cliente = optionalCliente.get();

		Page<CalificacionCliente> pageCalificacion;
		pageCalificacion = calClienteRepo.consultarCalificacion(cliente, paging);
		calificacionClientes = pageCalificacion.getContent();
		int pagina = pageCalificacion.getNumber();
		long totalElements = pageCalificacion.getTotalElements();

		for (CalificacionCliente c : calificacionClientes) {
			DTCalificacionesCliente.add(new DTCalificacionCliente(c));
		}

		response.put("currentPage", pagina);
		response.put("totalItems", totalElements);
		response.put("calificacionGlobal", cliente.getCalificacionPromedio());
		response.put("restaurantes", DTCalificacionesCliente);

		return response;
	}

	//Listo los restaurantes abiertos cercanos a la direccion activa del usuario
	@Override
	public Map<String, Object> listarRestaurantesPorZona(int page, int size, int horarioApertura, String nombre,
			String categoria, String sort, int order, int idDireccion) throws UsuarioException {
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
				pageRestaurante = restauranteRepo.listarRestauranteDesdeClientePorNombreYCategoria(nombre, categoria,
						EnumEstadoRestaurante.ACEPTADO, paging);
			} else {
				// Aplico solo nombre
				pageRestaurante = restauranteRepo.buscarRestaurantesPorEstadoNoBloqueadosYActivosPorNombre(nombre,
						EnumEstadoRestaurante.ACEPTADO, paging);
			}
		}
		else if (!categoria.equalsIgnoreCase("")) {
			// Aplico solo categoria
			pageRestaurante = restauranteRepo.listarRestauranteDesdeClientePorCategoria(categoria,
					EnumEstadoRestaurante.ACEPTADO, paging);
		} else {
			// No aplico ni categoria ni nombre
			pageRestaurante = restauranteRepo.buscarRestaurantesPorEstadoNoBloqueadosYActivos(EnumEstadoRestaurante.ACEPTADO,
					paging);
		}

		restaurantes = pageRestaurante.getContent();
		int pagina = pageRestaurante.getNumber();
		long totalElements = pageRestaurante.getTotalElements();

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

		// Si el horarioApertura en el filtro es menor o igual que el horarioApertura
		// del restaurante se muestra
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

		// response.put("currentPage", pageRestaurante.getNumber());
		// response.put("totalItems", pageRestaurante.getTotalElements());
		response.put("currentPage", pagina);
		response.put("totalItems", totalElements);
		response.put("restaurantes", DTListarRestaurantes);

		return response;
	}

	@Override
	public DTCalificacionRestaurante getCalificacionRestaurante(String mailCliente, String mailRestaurante)
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

		CalificacionRestaurante calRestaurante = calRestauranteRepo.findByClienteRestaurante(cliente, restaurante);
		if(calRestaurante == null)
			return null;
//			throw new UsuarioException(UsuarioException.SinCalificacion(mailRestaurante));
		return new DTCalificacionRestaurante(calRestaurante);
	}
	
	@Override
	public DTRespuesta setToken(String token, String mailCliente) {
		Optional<Cliente> optionalCliente = userRepo.findById(mailCliente);
		Cliente cliente = optionalCliente.get();
		if (cliente.getTokenDispositivo() == null || !cliente.getTokenDispositivo().equalsIgnoreCase(token)) {
			cliente.setTokenDispositivo(token);
			userRepo.save(cliente);
			return new DTRespuesta("Token guardado.");
		}
		return new DTRespuesta("Token no guardado, es igual al existente");
	}
}
