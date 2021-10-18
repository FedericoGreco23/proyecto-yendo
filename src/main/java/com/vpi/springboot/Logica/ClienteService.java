package com.vpi.springboot.Logica;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.Carrito;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.Modelo.GeoLocalizacion;

import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.LastDireccioClientenMongo;
import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.dto.DTCarrito;
import com.vpi.springboot.Modelo.dto.DTDireccion;
import com.vpi.springboot.Modelo.dto.DTProducto;
import com.vpi.springboot.Modelo.dto.DTProductoCarrito;
import com.vpi.springboot.Modelo.dto.EnumEstadoPedido;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.DireccionRepositorio;
import com.vpi.springboot.Repositorios.GeoLocalizacionRepositorio;

import com.vpi.springboot.Repositorios.MongoRepositorioCarrito;
import com.vpi.springboot.Repositorios.PedidoRepositorio;
import com.vpi.springboot.Repositorios.ProductoRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.exception.CarritoException;
import com.vpi.springboot.exception.DireccionException;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.RestauranteException;
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
	private NextSequenceService nextSequence;

	@Autowired
	private UltimaDireccionRepositorio ultimaDireccionRepo;

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
	public void altaCliente(Cliente usuario) throws UsuarioException, Exception {

		if (emailExist(usuario.getMail())) {
			throw new UsuarioException(UsuarioException.UsuarioYaExiste(usuario.getMail()));
		}
		Cliente user = new Cliente();
		String mail = usuario.getMail();

		if (mail != null && !mail.isEmpty() && usuario.getNickname() != null) {
			usuario.setActivo(true);
			usuario.setBloqueado(false);
			usuario.setSaldoBono(0.0f);
			usuario.setCalificacionPromedio(5.0f);
			usuario.setFechaCreacion(LocalDate.now());
			// byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
			// String contrasenia = Base64.getEncoder().encodeToString(salt) + "$" +
			// hash(usuario.getContrasenia(), salt);
			// usuario.setContrasenia(contrasenia);

			/**
			 * se carga contraseña encode
			 */
			usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
			try {

				userRepo.save(usuario);
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
	public void altaDireccion(DTDireccion direccion, String mail) throws UsuarioException {
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
		} else {
			throw new UsuarioException(UsuarioException.NotFoundException(mail));
		}

	}

	@Override
	public void bajaCuenta(String mail) throws UsuarioException {
		Optional<Cliente> optionalCliente = userRepo.findById(mail);
		if (optionalCliente.isPresent()) {
			Cliente cliente = optionalCliente.get();
			if (cliente.getActivo() != false) {
				cliente.setActivo(false);
				userRepo.save(cliente);
			} else {
				throw new UsuarioException("El usuario " + mail + " ya esta inactivo");
			}
		} else {
			throw new UsuarioException("No existe usuario");
		}
	}

	@Override
	public void modificarDireccion(int id, DTDireccion nueva, String mail) throws UsuarioException {
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
			} else {
				throw new UsuarioException("No existe direccion");
			}
		} else {
			throw new UsuarioException("No existe cliente");
		}
	}

	/*
	 * @Override public void eliminarDireccion(Direccion direccion, String mail)
	 * throws UsuarioException { Optional<Cliente> optionalCliente =
	 * userRepo.findById(mail); if(optionalCliente.isPresent()) { Cliente cliente =
	 * optionalCliente.get(); Optional<Direccion> optionalDireccion =
	 * dirRepo.findByStreetNumberandMail(direccion.getCalleNro(), cliente);
	 * if(optionalDireccion.isPresent()) { Direccion dir = optionalDireccion.get();
	 * dirRepo.delete(dir); }else { throw new
	 * UsuarioException("No existe direccion"); } }else { throw new
	 * UsuarioException("No existe cliente"); } }
	 */

	@Override
	public void eliminarDireccion(Integer id, String mail) throws UsuarioException {
		Optional<Cliente> optionalCliente = userRepo.findById(mail);
		if (optionalCliente.isPresent()) {
			Cliente cliente = optionalCliente.get();
			Optional<Direccion> optionalDireccion = dirRepo.findById(id);
			if (optionalDireccion.isPresent()) {
				Direccion dir = optionalDireccion.get();
				dirRepo.delete(dir);

				// actualiza ultima direccion en mongo
				setUltimaDireccionSeleccionada(null, mail);
			} else {
				throw new UsuarioException("No existe direccion");
			}
		} else {
			throw new UsuarioException("No existe cliente");
		}
	}


	@Override
	public void agregarACarrito(int producto,int cantidad, String mail) throws ProductoException {
		Optional<Producto> optionalProducto = productoRepo.findById(producto);
		if (optionalProducto.isPresent()) {
			DTProducto dtProducto = new DTProducto(optionalProducto.get());
			Carrito optionalCarrito = mongoRepo.findByMailAndActivo(mail, true);
			DTProductoCarrito dtPC = new DTProductoCarrito(dtProducto, cantidad);
			if (optionalCarrito != null) { // TIENE CARRITO ACTIVO
				optionalCarrito.addProductoCarrito(dtPC);
				mongoRepo.save(optionalCarrito);
			} else { // TIENE CARRITO INACTIVO O NO TIEN
				List<DTProductoCarrito> productos = new ArrayList<DTProductoCarrito>();
				productos.add(dtPC);
				Carrito carrito = new Carrito(mail, productos, true);
				carrito.setId(nextSequence.getNextSequence("customSequences"));
				mongoRepo.save(carrito);
			}
		} else {
			throw new ProductoException(ProductoException.NotFoundExceptionId(producto));

		}
	}

	@Override
	public DTCarrito verCarrito(String mail) {
		Carrito optionalCarrito = mongoRepo.findByMailAndActivo(mail, true);
		DTCarrito carrito = new DTCarrito(optionalCarrito.getId(), optionalCarrito.getProductoCarrito());
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
	public void altaPedido(String mailRestaurante, int idCarrito, EnumMetodoDePago pago, int idDireccion, String mail, String comentario) throws RestauranteException, CarritoException, DireccionException{
		Optional<Cliente> optionalCliente = userRepo.findById(mail);
		Cliente cliente = optionalCliente.get();
		Optional<Restaurante> optionalRestaurante = restauranteRepo.findById(mailRestaurante);
		if(optionalRestaurante.isPresent()) {
			Restaurante restaurante = optionalRestaurante.get();
			Optional<Carrito> optionalCarrito = mongoRepo.findById(idCarrito);
			if(optionalCarrito.isPresent()) {
			   Optional<Direccion> optionalDireccion = dirRepo.findById(idDireccion);
			   if(optionalDireccion.isPresent()) {
				   	String direccion = optionalDireccion.get().toString();
					Carrito carrito = optionalCarrito.get();
					EnumEstadoPedido estado =EnumEstadoPedido.PROCESADO;
					double precioTotal = 0;
					for (DTProductoCarrito DTpc : carrito.getProductoCarrito()) {
						if(DTpc.getProducto().getDescuento() != 0) {
							precioTotal = (precioTotal + ((DTpc.getProducto().getPrecio()) - ((DTpc.getProducto().getDescuento()/100) * DTpc.getProducto().getPrecio()))) * DTpc.getCantidad();
						}else {
							precioTotal = (precioTotal + DTpc.getProducto().getPrecio()) * DTpc.getCantidad() ;
						}
					}
					Pedido pedido = new Pedido(LocalDateTime.now(),precioTotal,estado,pago, idCarrito, direccion, restaurante, cliente, comentario);
					pedidoRepo.save(pedido);
					//AGREGAR PEDIDO AL RESTAURANTE
					if (restaurante.getPedidos() == null) {
						List<Pedido> pedidos = new ArrayList<Pedido>();
						pedidos.add(pedido);
						restaurante.setPedidos(pedidos);
					} else {
						restaurante.addPedido(pedido);
					}
					restauranteRepo.save(restaurante);
					//AGREGAR PEDIDO AL CLIENTE
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
			   }else {
				   throw new DireccionException(DireccionException.NotFoundExceptionId(idDireccion));
			   }
			}else {
				throw new CarritoException(CarritoException.NotFoundExceptionId(idCarrito));
			}
		}else {
			throw new RestauranteException(RestauranteException.NotFoundExceptionMail(mailRestaurante));
		}
	}
}
