package com.vpi.springboot.Modelo;

import java.time.LocalDateTime;

public class Reclamo {

	private String id;
	private String comentario;
	private LocalDateTime fecha;
	//private enumEstadoReclamo estado;
	private String resolucion;
	
	public Reclamo(String id, String comentario, LocalDateTime fecha, String resolucion) {
		this.id = id;
		this.comentario = comentario;
		this.fecha = fecha;
		this.resolucion = resolucion;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public LocalDateTime getFecha() {
		return fecha;
	}
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
	public String getResolucion() {
		return resolucion;
	}
	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}
}

