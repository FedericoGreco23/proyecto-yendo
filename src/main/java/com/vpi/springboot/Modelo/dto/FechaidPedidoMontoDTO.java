package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FechaidPedidoMontoDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private LocalDate fecha;
	private Double totalDelDia;
	private List<IdPedidoMontoDTO> pedidos= new ArrayList<IdPedidoMontoDTO>();

	public FechaidPedidoMontoDTO() {
		// TODO Auto-generated constructor stub
	}



	public List<IdPedidoMontoDTO> getPedidos() {
		return pedidos;
	}



	public void setPedidos(List<IdPedidoMontoDTO> pedidos) {
		this.pedidos = pedidos;
	}


	public FechaidPedidoMontoDTO(LocalDate fecha, List<IdPedidoMontoDTO> pedidos, Double totalDelDia) {
		super();
		this.fecha = fecha;
		this.pedidos = pedidos;
		this.totalDelDia=totalDelDia;
	}



	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}



	public Double getTotalDelDia() {
		return totalDelDia;
	}



	public void setTotalDelDia(Double totalDelDia) {
		this.totalDelDia = totalDelDia;
	}

	

}
