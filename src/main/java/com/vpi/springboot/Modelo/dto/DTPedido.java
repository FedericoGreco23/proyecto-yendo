package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.vpi.springboot.Modelo.Pedido;

public class DTPedido implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private LocalDateTime fecha;
	private Double costoTotal;
	private EnumEstadoPedido estadoPedidido;
	private EnumMetodoDePago metodoDePago;
	private int carrito;
	private DTCliente cliente;
	private DTRestaurante restaurante;
	private List<DTReclamo> reclamos;

	public DTPedido() {
		super();
	}

	public DTPedido(int id, LocalDateTime fecha, Double costoTotal, EnumEstadoPedido estadoPedidido,
			EnumMetodoDePago metodoDePago, Integer carrito) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.costoTotal = costoTotal;
		this.estadoPedidido = estadoPedidido;
		this.metodoDePago = metodoDePago;
		this.carrito = carrito;
	}
	
	public DTPedido(Pedido ped) {
		super();
		this.id = ped.getId();
		this.fecha = ped.getFecha();
		this.costoTotal = ped.getCostoTotal();
		this.estadoPedidido = ped.getEstadoPedido();
		this.metodoDePago = ped.getMetodoDePago();
		this.carrito = ped.getCarrito();
		this.cliente = new DTCliente(ped.getCliente());
		this.restaurante = new DTRestaurante(ped.getRestaurante());
		
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

	public int getCarrito() {
		return carrito;
	}

	public void setCarrito(int carrito) {
		this.carrito = carrito;
	}

	public DTCliente getCliente() {
		return cliente;
	}

	public void setCliente(DTCliente cliente) {
		this.cliente = cliente;
	}

	public DTRestaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(DTRestaurante restaurante) {
		this.restaurante = restaurante;
	}

	public List<DTReclamo> getReclamos() {
		return reclamos;
	}

	public void setReclamos(List<DTReclamo> reclamos) {
		this.reclamos = reclamos;
	}
}
