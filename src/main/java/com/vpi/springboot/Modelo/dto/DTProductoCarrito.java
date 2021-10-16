package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

import com.vpi.springboot.Modelo.Producto;

public class DTProductoCarrito implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int idProducto;
    private Integer cantidad;

    public DTProductoCarrito(int idProducto, Integer cantidad) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public int getidProducto() {
        return idProducto;
    }

    public void setidProducto(int producto) {
        this.idProducto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "DTProductoCarrito{" +
                "producto=" + idProducto +
                ", cantidad=" + cantidad +
                '}';
    }
}
