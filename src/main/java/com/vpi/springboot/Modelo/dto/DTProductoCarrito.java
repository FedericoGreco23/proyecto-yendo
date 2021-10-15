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

    public DTProductoCarrito(int producto, Integer cantidad) {
        this.idProducto = producto;
        this.cantidad = cantidad;
    }

    public int getProducto() {
        return idProducto;
    }

    public void setProducto(int producto) {
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
