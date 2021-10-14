package com.vpi.springboot.Logica;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Repositorios.ProductoRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.RestauranteException;

@Service
public class RestauranteService implements RestauranteServicioInterfaz {

	@Autowired
	private ProductoRepositorio proRepo;

	@Autowired
	private RestauranteRepositorio resRepo;

	public void altaMenu(Producto menu, String varRestaurante)
			throws ProductoException, RestauranteException, Exception {
		Restaurante restaurante = resRepo.findByNombre(varRestaurante);
		if (restaurante == null) {
			throw new RestauranteException(RestauranteException.NotFoundExceptionNombre(varRestaurante));
		}

		// La query tira una excepción si retorna más de una tupla
		try {
			if (proRepo.findByNombre(menu.getNombre(), restaurante) != null)
				throw new ProductoException(ProductoException.ProductoYaExiste(menu.getNombre()));
		} catch (Exception e) {
			throw new ProductoException(ProductoException.ProductoYaExiste(menu.getNombre()));
		}

		menu.setRestaurante(restaurante);
		restaurante.addProducto(menu);
		resRepo.save(restaurante);
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
}