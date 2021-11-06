package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.vpi.springboot.Modelo.Pedido;

public class DTPedido implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private LocalDateTime fecha;
	private Double costoTotal;
	private EnumEstadoPedido estadoPedido;
	private EnumMetodoDePago metodoDePago;
//	private int carrito;
	private String cliente;
	private Float calificacionCliente;
	private String restaurante;
//	private List<DTReclamo> reclamos = new ArrayList<>();
	private DTCarrito carrito;
	private String direccion;
	private String comentario;
	private Boolean pago;
	private String tiempoEspera;

	public DTPedido() {
		super();
	}

	public DTPedido(int id, LocalDateTime fecha, Double costoTotal, EnumEstadoPedido estadoPedidido,
			EnumMetodoDePago metodoDePago, Integer carrito) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.costoTotal = costoTotal;
		this.estadoPedido = estadoPedidido;
		this.metodoDePago = metodoDePago;
	}

	public DTPedido(Pedido ped) {
		super();
		this.id = ped.getId();
		this.fecha = ped.getFecha();
		this.costoTotal = ped.getCostoTotal();
		this.estadoPedido = ped.getEstadoPedido();
		this.metodoDePago = ped.getMetodoDePago();
		this.cliente = ped.getCliente().getMail();
		this.calificacionCliente = ped.getCliente().getCalificacionPromedio();
		this.restaurante = ped.getRestaurante().getNombre();
		this.comentario = ped.getComentario();
		this.direccion = ped.getDireccion();
		this.pago = ped.getPago();
		this.tiempoEspera = ped.getRestaurante().getTiempoEstimadoMinimo() + " a " + ped.getRestaurante().getTiempoEstimadoMaximo() + " minutos."; 
	}
	
	public DTPedido(Pedido ped, DTCarrito carrito) {
		super();
		this.id = ped.getId();
		this.fecha = ped.getFecha();
		this.costoTotal = ped.getCostoTotal();
		this.estadoPedido = ped.getEstadoPedido();
		this.metodoDePago = ped.getMetodoDePago();
		this.cliente = ped.getCliente().getMail();
		this.calificacionCliente = ped.getCliente().getCalificacionPromedio();
		this.carrito = carrito;
		this.restaurante = ped.getRestaurante().getNombre();
		this.comentario = ped.getComentario();
		this.direccion = ped.getDireccion();
		this.pago = ped.getPago();
	}
	
	public DTPedido(int id, LocalDateTime fecha, Double costoTotal, EnumEstadoPedido estadoPedidido,
			EnumMetodoDePago metodoDePago, Integer carrito, String direccion, String comentario) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.costoTotal = costoTotal;
		this.estadoPedido = estadoPedidido;
		this.metodoDePago = metodoDePago;
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

	public EnumEstadoPedido getEstadoPedido() {
		return estadoPedido;
	}

	public void setEstadoPedido(EnumEstadoPedido estadoPedido) {
		this.estadoPedido = estadoPedido;
	}

	public EnumMetodoDePago getMetodoDePago() {
		return metodoDePago;
	}

	public void setMetodoDePago(EnumMetodoDePago metodoDePago) {
		this.metodoDePago = metodoDePago;
	}

	public DTCarrito getCarrito() {
		return carrito;
	}

	public void setCarrito(DTCarrito carrito) {
		this.carrito = carrito;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(String restaurante) {
		this.restaurante = restaurante;
	}

//	public List<DTReclamo> getReclamos() {
//		return reclamos;
//	}
//
//	public void setReclamos(List<DTReclamo> reclamos) {
//		this.reclamos = reclamos;
//	}

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

	public Boolean getPago() {
		return pago;
	}

	public void setPago(Boolean pago) {
		this.pago = pago;
	}

	public Float getCalificacionCliente() {
		return calificacionCliente;
	}

	public void setCalificacionCliente(Float calificacionCliente) {
		this.calificacionCliente = calificacionCliente;
	}

	public String getTiempoEspera() {
		return tiempoEspera;
	}

	public void setTiempoEspera(String tiempoEspera) {
		this.tiempoEspera = tiempoEspera;
	}
}