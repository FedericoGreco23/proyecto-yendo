package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.vpi.springboot.Modelo.Calificacion;
import com.vpi.springboot.Modelo.CalificacionCliente;
import com.vpi.springboot.Modelo.CalificacionRestaurante;

public class DTCalificacionRestaurante extends DTCalificacion implements Serializable {
	private static final long serialVersionUID = 1L;

	private String restaurante;
	private String cliente;
	private String fotoCliente;

	public DTCalificacionRestaurante() {
		super();
	}

	public DTCalificacionRestaurante(int id, int puntaje, String comentario, String foto, LocalDateTime fecha,
			String restaurante, String cliente) {
		super(id, puntaje, comentario, foto, fecha);
		this.restaurante = restaurante;
		this.cliente = cliente;
	}
	
	//Este uso para consultarCalificacion del restaurante
	public DTCalificacionRestaurante(CalificacionRestaurante c) {
		super(c.getPuntaje(), c.getComentario(), c.getFoto(), c.getFecha());
		this.cliente = c.getCliente().getNickname();
		this.fotoCliente = c.getCliente().getFoto();
	}

	public DTCalificacionRestaurante(CalificacionCliente cal) {
		super(cal.getId(), cal.getPuntaje(), cal.getComentario(), cal.getFoto(), cal.getFecha());
		this.restaurante = cal.getRestaurante().getNombre();
		this.cliente = cal.getCliente().getNombre();
	}
	
	public DTCalificacionRestaurante(Calificacion cal, String restaurante, String cliente) {
		super(cal.getId(), cal.getPuntaje(), cal.getComentario(), cal.getFoto(), cal.getFecha());
		this.restaurante = restaurante;
		this.cliente = cliente;
	}

	public String getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(String restaurante) {
		this.restaurante = restaurante;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getFotoCliente() {
		return fotoCliente;
	}

	public void setFotoCliente(String fotoCliente) {
		this.fotoCliente = fotoCliente;
	}
}