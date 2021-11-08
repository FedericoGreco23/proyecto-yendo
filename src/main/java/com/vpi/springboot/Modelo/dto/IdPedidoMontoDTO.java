package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class IdPedidoMontoDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer idPedido;
	private Double monto;
	private String estado;
	private String metodoPago;
	private LocalDate fecha;
	
	
	public IdPedidoMontoDTO() {
		// TODO Auto-generated constructor stub
	}


	public IdPedidoMontoDTO(Integer idPedido, Double monto, String estado, String metodoPago, LocalDate fecha) {
		super();
		this.idPedido = idPedido;
		this.monto = monto;
		this.estado = estado;
		this.metodoPago = metodoPago;
		this.fecha=fecha;
	}


	public Integer getIdPedido() {
		return idPedido;
	}


	public void setIdPedido(Integer idPedido) {
		this.idPedido = idPedido;
	}


	public Double getMonto() {
		return monto;
	}


	public void setMonto(Double monto) {
		this.monto = monto;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public String getMetodoPago() {
		return metodoPago;
	}


	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}


	public LocalDate getFecha() {
		return fecha;
	}


	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	
	
	
	

}
