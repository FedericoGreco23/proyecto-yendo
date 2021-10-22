package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Promocion;

public class DTPromocion extends DTProducto implements Serializable {
	private static final long serialVersionUID = 1L;

	// No tiene mucho sentido poner el DT aca
	private List<String> productos = new ArrayList<>();

	public DTPromocion() {
		super();
	}

	public DTPromocion(Promocion promocion) {
		super(promocion.getId(), promocion.getNombre(), promocion.getDescripcion(), promocion.getPrecio(),
				promocion.getFoto(), promocion.getDescuento(), promocion.isActivo(), promocion.getRestaurante().getNombre());
		
		Map<String, Integer> duplicateMap = new HashMap<>();
		for (Producto p : promocion.getProductos()) {
			if(!this.productos.contains(p.getNombre()))
				this.productos.add(p.getNombre());
			else {
				if(duplicateMap.containsKey(p.getNombre())) {
					duplicateMap.put(p.getNombre(), duplicateMap.get(p.getNombre()) + 1);
				} else {
					duplicateMap.put(p.getNombre(), 2);
				}
				
				String nombreProducto = p.getNombre() + " x" + duplicateMap.get(p.getNombre());
				this.productos.remove(p.getNombre());
				this.productos.add(nombreProducto);
			}
		}
	}

	public DTPromocion(List<String> productos) {
		this.productos = productos;
	}

	public List<String> getProductos() {
		return productos;
	}

	public void setProductos(List<String> productos) {
		this.productos = productos;
	}
}