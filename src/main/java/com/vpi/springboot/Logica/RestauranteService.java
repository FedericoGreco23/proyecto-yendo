package com.vpi.springboot.Logica;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Carrito;
import com.vpi.springboot.Modelo.Categoria;
import com.vpi.springboot.Modelo.GeoLocalizacion;
import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Promocion;
import com.vpi.springboot.Modelo.Reclamo;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.DTCarrito;
import com.vpi.springboot.Modelo.dto.DTPedido;
import com.vpi.springboot.Modelo.dto.DTProductoCarrito;
import com.vpi.springboot.Modelo.dto.DTProductoIdCantidad;
import com.vpi.springboot.Modelo.dto.DTPromocionConPrecio;
import com.vpi.springboot.Modelo.dto.DTRespuesta;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.Modelo.dto.EnumEstadoPedido;
import com.vpi.springboot.Modelo.dto.EnumEstadoReclamo;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Repositorios.CategoriaRepositorio;
import com.vpi.springboot.Repositorios.GeoLocalizacionRepositorio;
import com.vpi.springboot.Repositorios.MongoRepositorioCarrito;
import com.vpi.springboot.Repositorios.PedidoRepositorio;
import com.vpi.springboot.Repositorios.ProductoRepositorio;
import com.vpi.springboot.Repositorios.PromocionRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.exception.CategoriaException;
import com.vpi.springboot.exception.PedidoException;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.ReclamoException;
import com.vpi.springboot.exception.PromocionException;
import com.vpi.springboot.exception.RestauranteException;
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

	private DTCarrito verCarrito(int id) {
		Optional<Carrito> optionalCarrito = mongoRepo.findById(id);
		if (optionalCarrito.isPresent()) {
			Carrito carrito = optionalCarrito.get();
			DTCarrito dt = new DTCarrito(carrito.getId(), carrito.getProductoCarrito(), carrito.getMailRestaurante(),carrito.getCostoEnvio());
			return dt;
		}

		return null;
	}

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

	public DTRespuesta altaMenu(Producto menu, String varRestaurante)
			throws ProductoException, RestauranteException, CategoriaException, Exception {
		Optional<Restaurante> optionalRestaurante = resRepo.findById(varRestaurante);
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
				resRepo.save(restaurante);
				return new DTRespuesta("Menú agregado correctamente.");
			} else {
				throw new ProductoException(ProductoException.ProductoYaExiste(menu.getNombre()));
			}
		} catch (Exception e) {
			throw new ProductoException(ProductoException.ProductoYaExiste(menu.getNombre()));
		}
	}

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

	public DTRespuesta modificarMenu(Producto menu) throws ProductoException {
		if (menu.getRestaurante() != null) {
			throw new ProductoException("No puede cambiar el restaurante de un menú.");
		}

		Optional<Producto> optionalProducto = proRepo.findById(menu.getId());
		if (!optionalProducto.isPresent()) {
			throw new ProductoException(ProductoException.NotFoundExceptionId(menu.getId()));
		}

		Producto producto = optionalProducto.get();

		producto.setDescripcion(menu.getDescripcion());
		producto.setPrecio(menu.getPrecio());
		producto.setFoto(menu.getFoto());
		producto.setDescuento(menu.getDescuento());
		producto.setActivo(menu.isActivo());

		proRepo.save(producto);
		return new DTRespuesta("Menú modificado correctamente.");
	}

	public Map<String, Object> listarPedidos(int page, int size, String varRestaurante) throws RestauranteException {
		Optional<Restaurante> optionalRestaurante = resRepo.findById(varRestaurante);
		if (!optionalRestaurante.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionNombre(varRestaurante));
		}
		Restaurante restaurante = optionalRestaurante.get();

		Map<String, Object> response = new HashMap<>();
		Pageable paging = PageRequest.of(page, size);
		Page<Pedido> pagePedido = pedidoRepo.findAllByRestaurante(restaurante, paging);
		List<Pedido> pedidos = pagePedido.getContent();
		List<DTPedido> retorno = new ArrayList<>();

		response.put("currentPage", pagePedido.getNumber());
		response.put("totalItems", pagePedido.getTotalElements());

		for (Pedido p : pedidos) {
			retorno.add(new DTPedido(p));
		}

		response.put("pedidos", pedidos);
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

	public void altaPromocion(DTPromocionConPrecio promocion, String mail) throws Exception {

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

				Producto prod = productoRepo.findByIdAndRest(entry.getId(), mail);

				for (int i = 0; i < entry.getCantidad(); i++) {
					List<Producto> prodList = promocionNueva.getProductos();
					prodList.add(prod);
					promocionNueva.setProductos(prodList);
				}
			}
			promoRepo.save(promocionNueva);

		} catch (Exception e) {
			throw new Exception();
		}

	}
	
	@Override
	public DTRespuesta rechazarPedido(int idPedido) throws PedidoException{
		Optional<Pedido> optionalPedido = pedidoRepo.findById(idPedido);
		if (optionalPedido.isPresent()) {
			Pedido pedido = optionalPedido.get();
			pedido.setEstadoPedido(EnumEstadoPedido.RECHAZADO);
			pedidoRepo.save(pedido);
			return new DTRespuesta("Pedido " + idPedido + " rechazado.");
		} else {
			throw new PedidoException(PedidoException.NotFoundExceptionId(idPedido));
		}
	}

}
