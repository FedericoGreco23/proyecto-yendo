package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

public class DTRespuesta implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String respuesta;

	public DTRespuesta() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public DTRespuesta(String respuesta) {
		super();
		this.respuesta = respuesta;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	
	
	
	
	

}
