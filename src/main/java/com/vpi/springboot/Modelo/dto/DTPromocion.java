package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Promocion;

public class DTPromocion extends DTProducto implements Serializable {
	private static final long serialVersionUID = 1L;

	// No tiene mucho sentido poner el DT aca
	private List<DTProductoPromocion> productos = new ArrayList<>();

	public DTPromocion() {
		super();
	}

	public DTPromocion(Promocion promocion) {
		super(promocion.getId(), promocion.getNombre(), promocion.getDescripcion(), promocion.getPrecio(),
				promocion.getFoto(), promocion.getDescuento(), promocion.isActivo(),
				promocion.getRestaurante().getNombre());
		
		

		Map<Integer, DTProductoPromocion> items = new HashMap<>();
		for (Producto p : promocion.getProductos()) {
			if (items.containsKey(p.getId())) {
				items.get(p.getId()).addCantidad();
			} else {
				items.put(p.getId(), new DTProductoPromocion(p));
			}
		}
		
		List<DTProductoPromocion> retorno = new ArrayList<>();
		retorno = items.entrySet().stream().map(e -> (e.getValue()))
				.collect(Collectors.toList());
		this.productos = retorno;
	}

	public DTPromocion(List<DTProductoPromocion> productos) {
		this.productos = productos;
	}

	public List<DTProductoPromocion> getProductos() {
		return productos;
	}

	public void setProductos(List<DTProductoPromocion> productos) {
		this.productos = productos;
	}
}