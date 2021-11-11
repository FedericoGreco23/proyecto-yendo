package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

public class GetEstadoResponseDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String estado;
	private String abiertoCerrado;
	private Integer cantMenus;
	public GetEstadoResponseDTO(String estado, String abiertoCerrado, Integer cantMenus) {
		super();
		this.estado = estado;
		this.abiertoCerrado = abiertoCerrado;
		this.cantMenus = cantMenus;
	}
	public GetEstadoResponseDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}


	public String getAbiertoCerrado() {
		return abiertoCerrado;
	}
	public void setAbiertoCerrado(String abiertoCerrado) {
		this.abiertoCerrado = abiertoCerrado;
	}
	public Integer getCantMenus() {
		return cantMenus;
	}
	public void setCantMenus(Integer cantMenus) {
		this.cantMenus = cantMenus;
	}
	
	
	


	
}
