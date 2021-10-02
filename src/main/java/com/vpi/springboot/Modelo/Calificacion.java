package com.vpi.springboot.Modelo;

import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



@MappedSuperclass
public class Calificacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int puntaje;
	private String comentario;
	private String foto;
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime fecha;
		
	public Calificacion() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public Calificacion(int id, int puntaje, String comentario, String foto, LocalDateTime fecha) {
		super();
		this.id = id;
		this.puntaje = puntaje;
		this.comentario = comentario;
		this.foto = foto;
		this.fecha = fecha;
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
