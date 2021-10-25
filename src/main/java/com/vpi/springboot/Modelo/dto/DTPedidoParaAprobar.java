package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.vpi.springboot.Modelo.Pedido;

public class DTPedidoParaAprobar implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private LocalDateTime fecha;
	private Double costoTotal;
	private EnumEstadoPedido estadoPedidido;
	private EnumMetodoDePago metodoDePago;
	private List<DTProductoCarrito> carrito;
	private DTCliente cliente;
	private String restaurante;
	private List<DTReclamo> reclamos = new ArrayList<>();
	private String direccion;
	private String comentario;

	public DTPedidoParaAprobar() {
		super();
	}

	public DTPedidoParaAprobar(int id, LocalDateTime fecha, Double costoTotal, EnumEstadoPedido estadoPedidido,
			EnumMetodoDePago metodoDePago, List<DTProductoCarrito> carrito) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.costoTotal = costoTotal;
		this.estadoPedidido = estadoPedidido;
		this.metodoDePago = metodoDePago;
		this.carrito = carrito;
	}

	public DTPedidoParaAprobar(Pedido ped) {
		super();
		this.id = ped.getId();
		this.fecha = ped.getFecha();
		this.costoTotal = ped.getCostoTotal();
		this.estadoPedidido = ped.getEstadoPedido();
		this.metodoDePago = ped.getMetodoDePago();
		this.cliente = new DTCliente(ped.getCliente());
		this.restaurante = ped.getRestaurante().getNombre();
	}
	
	public DTPedidoParaAprobar(int id, LocalDateTime fecha, Double costoTotal, EnumEstadoPedido estadoPedidido,
			EnumMetodoDePago metodoDePago, List<DTProductoCarrito> carrito, String direccion, String comentario) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.costoTotal = costoTotal;
		this.estadoPedidido = estadoPedidido;
		this.metodoDePago = metodoDePago;
		this.carrito = carrito;
		this.direccion = direccion;
		this.comentario = comentario;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Double getCostoTotal() {
		return costoTotal;
	}

	public void setCostoTotal(Double costoTotal) {
		this.costoTotal = costoTotal;
	}

	public EnumEstadoPedido getEstadoPedidido() {
		return estadoPedidido;
	}

	public void setEstadoPedidido(EnumEstadoPedido estadoPedidido) {
		this.estadoPedidido = estadoPedidido;
	}

	public EnumMetodoDePago getMetodoDePago() {
		return metodoDePago;
	}

	public void setMetodoDePago(EnumMetodoDePago metodoDePago) {
		this.metodoDePago = metodoDePago;
	}

	public List<DTProductoCarrito> getCarrito() {
		return carrito;
	}

	public void setCarrito(List<DTProductoCarrito> list) {
		this.carrito = list;
	}

	public DTCliente getCliente() {
		return cliente;
	}

	public void setCliente(DTCliente cliente) {
		this.cliente = cliente;
	}

	public String getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(String restaurante) {
		this.restaurante = restaurante;
	}

	public List<DTReclamo> getReclamos() {
		return reclamos;
	}

	public void setReclamos(List<DTReclamo> reclamos) {
		this.reclamos = reclamos;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
}