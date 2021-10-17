package com.vpi.springboot.Logica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Categoria;
import com.vpi.springboot.Modelo.GeoLocalizacion;
import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.DTPedido;
import com.vpi.springboot.Modelo.dto.DTRestaurante;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Repositorios.CategoriaRepositorio;
import com.vpi.springboot.Repositorios.GeoLocalizacionRepositorio;
import com.vpi.springboot.Repositorios.PedidoRepositorio;
import com.vpi.springboot.Repositorios.ProductoRepositorio;
import com.vpi.springboot.Repositorios.PromocionRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.exception.CategoriaException;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.RestauranteException;

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

	@Override
	public void altaRestaurante(Restaurante rest) throws RestauranteException {

		//Seccion verificar que nombreRestaurante o restauranteMail no exista ya
		Optional<Restaurante> busquedaMail = restauranteRepo.findById(rest.getMail());
		Restaurante busquedaNombre = null;
		busquedaNombre = restauranteRepo.existeRestauranteNombre(rest.getNombre());
		if (busquedaMail.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionMail(rest.getMail()));
		} else if (busquedaNombre != null) {
			throw new RestauranteException(RestauranteException.RestauranteYaExiste(rest.getNombre()));
		}
		//Fin de seccion

		rest.setActivo(true);
		rest.setBloqueado(false);
		rest.setCalificacionPromedio(5.0f);
		rest.setFechaCreacion(LocalDate.now());
		rest.setEstado(EnumEstadoRestaurante.EN_ESPERA);
		rest.setFechaApertura(null);
		rest.setProductos(null);
		rest.setReclamos(null);
		rest.setPedidos(null);
		rest.setAbierto(false);

		restauranteRepo.save(rest);
	}

	public void altaMenu(Producto menu, String varRestaurante)
			throws ProductoException, RestauranteException, CategoriaException, Exception {
		Optional<Restaurante> optionalRestaurante = resRepo.findById(varRestaurante);
		if (!optionalRestaurante.isPresent()) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionNombre(varRestaurante));
		}
		Restaurante restaurante = optionalRestaurante.get();

		List<Categoria> categorias = new ArrayList<>();
		for(Categoria c: menu.getCategorias()) {
			Optional<Categoria> optionalCategoria = catRepo.findById(c.getNombre());
			if(!optionalCategoria.isPresent())
				throw new CategoriaException(CategoriaException.NotFoundException(c.getNombre()));
			categorias.add(c);
		}

		// La query tira una excepción si retorna más de una tupla
		try {
			if (proRepo.findByNombre(menu.getNombre(), restaurante) == null) {
				menu.setCategorias(categorias);
				menu.setRestaurante(restaurante);
				restaurante.addProducto(menu);
				resRepo.save(restaurante);
			} else {
				throw new ProductoException(ProductoException.ProductoYaExiste(menu.getNombre()));
			}
		} catch (Exception e) {
			throw new ProductoException(ProductoException.ProductoYaExiste(menu.getNombre()));
		}
	}

	public void bajaMenu(int id) throws ProductoException {
		Optional<Producto> optionalProducto = proRepo.findById(id);
		if (optionalProducto.isPresent()) {
			Producto producto = optionalProducto.get();
			proRepo.delete(producto);
		} else {
			throw new ProductoException(ProductoException.NotFoundExceptionId(id));
		}
	}

	public void modificarMenu(Producto menu) throws ProductoException {
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
		producto.setCategorias(menu.getCategorias());

		proRepo.save(producto);
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
	public void abrirRestaurante(String mail) {
		Optional<Restaurante> restaurante = restauranteRepo.findById(mail);
		restaurante.get().setAbierto(true);
		restauranteRepo.save(restaurante.get());
	}

	@Override
	public void cerrarRestaurante(String mail) {
		Optional<Restaurante> restaurante = restauranteRepo.findById(mail);
		restaurante.get().setAbierto(false);
		restauranteRepo.save(restaurante.get());
	}
}
