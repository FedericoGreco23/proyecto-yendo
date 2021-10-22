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
				promocion.getFoto(), promocion.getDescuento(), promocion.isActivo(),
				promocion.getRestaurante().getNombre());

		Map<String, Integer> items = new HashMap<>();
		for (Producto p : promocion.getProductos()) {
			if (items.containsKey(p.getNombre())) {
				items.put(p.getNombre(), items.get(p.getNombre()) + 1);
			} else {
				items.put(p.getNombre(), 1);
			}

			String nombreProducto;
			String nombreAnterior = "";
			if (items.get(p.getNombre()) == 1) {
				nombreProducto = p.getNombre();
			} else if (items.get(p.getNombre()) == 2){
				nombreAnterior = p.getNombre() + " x" + items.get(p.getNombre());
				nombreProducto = p.getNombre();
			} else {
				nombreAnterior = p.getNombre() + " x" + items.get(p.getNombre());
				nombreProducto = p.getNombre() + " x" + (items.get(p.getNombre()) - 1);
			}

			if (!this.productos.contains(nombreProducto)) {
				this.productos.add(nombreProducto);
			} else {
				this.productos.remove(nombreProducto);
				this.productos.add(nombreAnterior);
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