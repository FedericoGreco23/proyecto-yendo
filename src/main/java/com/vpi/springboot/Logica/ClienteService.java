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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.Modelo.GeoLocalizacion;
import com.vpi.springboot.Modelo.dto.DTDireccion;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.DireccionRepositorio;
import com.vpi.springboot.Repositorios.GeoLocalizacionRepositorio;
import com.vpi.springboot.exception.UsuarioException;

@Service
public class ClienteService implements ClienteServicioInterfaz {

	@Autowired
	private ClienteRepositorio userRepo;
	@Autowired
	private DireccionRepositorio dirRepo;
	@Autowired
	private GeoLocalizacionRepositorio geoRepo;
	
	private static final int iterations = 20 * 1000;
	private static final int saltLen = 32;
	private static final int desiredKeyLen = 256;

	@Override
	public List<DTDireccion> getDireccionCliente(String mail) throws UsuarioException {
		Optional<Cliente> optionalUser = userRepo.findById(mail);
		if (optionalUser.isPresent()) {
			Cliente cliente = optionalUser.get();
			List<DTDireccion> retorno = new ArrayList<DTDireccion>();
			if(cliente.getDirecciones() != null) {
				for (Direccion direccion : cliente.getDirecciones()) {
					retorno.add(new DTDireccion(direccion.getId(), direccion.getCalleNro(), direccion.getGeoLocalizacion()));
				}
				return retorno;
			}else {
				throw new UsuarioException("El usuario no tiene direcciones");
			}
		} else {
			throw new UsuarioException(UsuarioException.NotFoundException(mail));
		}
	}

	@Override
	public void altaCliente(Cliente usuario) throws UsuarioException, Exception {
		Optional<Cliente> optionalUser = userRepo.findById(usuario.getMail());
		if(optionalUser.isPresent()) {
			throw new UsuarioException(UsuarioException.UsuarioYaExiste());
		}else {
			String mail = usuario.getMail();
			if(mail.contains("@") && mail.contains(".com")) {
				String nick = usuario.getNickname();
				if(nick != null) {
					usuario.setActivo(true);
					usuario.setBloqueado(false);
					usuario.setSaldoBono(0.0f);
					usuario.setCalificacionPromedio(5.0f);
					usuario.setFechaCreacion(LocalDate.now());
					byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
					//String contrasenia = Base64.getEncoder().encodeToString(salt) + "$" + hash(usuario.getContrasenia(), salt);
					//usuario.setContrasenia(contrasenia);
					userRepo.save(usuario);
				}else {
					throw new UsuarioException("Debe ingresar nickname");
				}
			}else
				throw new UsuarioException("Tiene que introducir un mail válido.");
		}

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
		if(optionalCliente.isPresent()) {
			Cliente cliente = optionalCliente.get();
			GeoLocalizacion geo = new GeoLocalizacion(direccion.getGeoLocalizacion().getLatitud(), direccion.getGeoLocalizacion().getLongitud());
			geoRepo.save(geo);
			Direccion dir = new Direccion(direccion.getCalleNro(),geo);	
			if(mail != null) {
				dir.setCliente(cliente);
			}
			dirRepo.save(dir);
			if(cliente.getDirecciones() == null) {
				List<Direccion> direcciones = new ArrayList<Direccion>();
				direcciones.add(dir);
				cliente.setDirecciones(direcciones);
			}else {
				cliente.addDireccion(dir);
			}
			userRepo.save(cliente);
		}else {
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
		if(optionalCliente.isPresent()) {
			Cliente cliente = optionalCliente.get();
			Optional<Direccion> optionalDireccion = dirRepo.findById(id);
			if(optionalDireccion.isPresent()) {
				Direccion dirNueva = optionalDireccion.get();
				dirNueva.setCalleNro(nueva.getCalleNro());
				dirNueva.setGeoLocalizacion(new GeoLocalizacion(nueva.getGeoLocalizacion()));
				dirRepo.save(dirNueva);
			}else {
				throw new UsuarioException("No existe direccion");
			}
		}else {
			throw new UsuarioException("No existe cliente");
		}
	}
	
	/*@Override
	public void eliminarDireccion(Direccion direccion, String mail) throws UsuarioException {
		Optional<Cliente> optionalCliente = userRepo.findById(mail);
		if(optionalCliente.isPresent()) {
			Cliente cliente = optionalCliente.get();
			Optional<Direccion> optionalDireccion = dirRepo.findByStreetNumberandMail(direccion.getCalleNro(), cliente);
			if(optionalDireccion.isPresent()) {
				Direccion dir = optionalDireccion.get();
				dirRepo.delete(dir);
			}else {
				throw new UsuarioException("No existe direccion");
			}
		}else {
			throw new UsuarioException("No existe cliente");
		}
	}*/
	
	@Override
	public void eliminarDireccion(Integer id, String mail) throws UsuarioException {
		Optional<Cliente> optionalCliente = userRepo.findById(mail);
		if(optionalCliente.isPresent()) {
			Cliente cliente = optionalCliente.get();
			Optional<Direccion> optionalDireccion = dirRepo.findById(id);
			if(optionalDireccion.isPresent()) {
				Direccion dir = optionalDireccion.get();
				dirRepo.delete(dir);
			}else {
				throw new UsuarioException("No existe direccion");
			}
		}else {
			throw new UsuarioException("No existe cliente");
		}
	}
}
