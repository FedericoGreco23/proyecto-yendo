package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

public class PedidoMonto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Double monto;
	public PedidoMonto(Integer id, Double monto) {
		super();
		this.id = id;
		this.monto = monto;
	}
	public PedidoMonto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	
	

}
