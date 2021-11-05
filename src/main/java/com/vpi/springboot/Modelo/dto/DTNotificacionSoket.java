package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

public class DTNotificacionSoket implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Object respuesta;
	private String type;
	public DTNotificacionSoket() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DTNotificacionSoket(Object respuesta, String type) {
		super();
		this.respuesta = respuesta;
		this.type = type;
	}
	public Object getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(Object respuesta) {
		this.respuesta = respuesta;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

}
