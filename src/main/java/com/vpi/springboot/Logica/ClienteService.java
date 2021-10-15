package com.vpi.springboot.Logica;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.Carrito;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.Modelo.GeoLocalizacion;
import com.vpi.springboot.Modelo.dto.DTDireccion;
import com.vpi.springboot.Modelo.dto.DTProducto;
import com.vpi.springboot.Modelo.dto.DTProductoCarrito;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.DireccionRepositorio;
import com.vpi.springboot.Repositorios.GeoLocalizacionRepositorio;
import com.vpi.springboot.Repositorios.MongoRepositorio;
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
	private MongoRepositorio mongoRepo;

	private static final int iterations = 20 * 1000;
	private static final int saltLen = 32;
	private static final int desiredKeyLen = 256;

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

	// METODO PARA HASHEAR CONTRASEÑA
	private static String hash(String password, byte[] salt) throws Exception {
		if (password == null || password.length() == 0)
			throw new IllegalArgumentException("Contraseña vacia.");
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		SecretKey key = f.generateSecret(new PBEKeySpec(password.toCharArray(), salt, iterations, desiredKeyLen));
		return Base64.getEncoder().encodeToString(key.getEncoded());

	}
	// --------------------------------------

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
			} else {
				throw new UsuarioException("No existe direccion");
			}
		} else {
			throw new UsuarioException("No existe cliente");
		}
	}
	
	/*public void agregarACarrito(DTProducto p) {
		HttpSession session = httpSessionFactory.getObject();
		if(session.getAttribute("carrito") == null) {
			List<DTProducto> carrito = new ArrayList<DTProducto>();
			carrito.add(p);
			session.setAttribute("carrito", carrito);
		}else {
			List<DTProducto> carrito = (List<DTProducto>) httpSessionFactory.getObject().getAttribute("carrito");
			carrito.add(p);
			session.setAttribute("carrito", carrito);
		}
	}*/
	
	public void agregarACarrito(DTProductoCarrito p, String mail) {
		Carrito carrito = new Carrito(mail, p);
		mongoRepo.save(carrito);
		
		
	}
	
}
