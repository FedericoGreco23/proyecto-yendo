package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

public class IdPedidoMontoDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer idPedido;
	private Double monto;
	private String estado;
	private String metodoPago;
	
	
	public IdPedidoMontoDTO() {
		// TODO Auto-generated constructor stub
	}


	public IdPedidoMontoDTO(Integer idPedido, Double monto, String estado, String metodoPago) {
		super();
		this.idPedido = idPedido;
		this.monto = monto;
		this.estado = estado;
		this.metodoPago = metodoPago;
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
	
	
	
	

}
