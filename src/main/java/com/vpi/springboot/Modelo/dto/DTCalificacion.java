package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.vpi.springboot.Modelo.Calificacion;

public class DTCalificacion implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private int puntaje;
	private String comentario;
	private String foto;
	private LocalDateTime fecha;

	public DTCalificacion() {
		super();
	}

	public DTCalificacion(int id, int puntaje, String comentario, String foto, LocalDateTime fecha) {
		super();
		this.id = id;
		this.puntaje = puntaje;
		this.comentario = comentario;
		this.foto = foto;
		this.fecha = fecha;
	}

	public DTCalificacion(Calificacion cal) {
		super();
		this.id = cal.getId();
		this.puntaje = cal.getPuntaje();
		this.comentario = cal.getComentario();
		this.foto = cal.getFoto();
		this.fecha = cal.getFecha();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPuntaje() {
		return puntaje;
	}

	public void setPuntaje(int puntaje) {
		this.puntaje = puntaje;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
}