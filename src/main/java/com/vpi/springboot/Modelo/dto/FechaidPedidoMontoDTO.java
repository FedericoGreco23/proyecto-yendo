package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FechaidPedidoMontoDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private LocalDate fecha;
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


	public FechaidPedidoMontoDTO(LocalDate fecha, List<IdPedidoMontoDTO> pedidos) {
		super();
		this.fecha = fecha;
		this.pedidos = pedidos;
	}



	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}


}
