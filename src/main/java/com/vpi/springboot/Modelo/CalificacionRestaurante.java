package com.vpi.springboot.Modelo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.vpi.springboot.IdCompuestas.CalificacionRestauranteId;

@Entity
@IdClass(CalificacionRestauranteId.class)
public class CalificacionRestaurante extends Calificacion {

	@Id
	@ManyToOne
	@JoinColumn(name = "restaurante")
	private Restaurante restaurante;
	@Id
	@ManyToOne 
	@JoinColumn(name = "cliente")
	private Cliente cliente;

	public CalificacionRestaurante() {
		super();
	}

	public CalificacionRestaurante(int puntaje, String comentario, String foto, LocalDateTime fecha) {
		super(puntaje, comentario, foto, fecha);
	}
	
	public CalificacionRestaurante(int puntaje, String comentario, String foto, LocalDateTime fecha, Restaurante restaurante, Cliente cliente) {
		super(puntaje, comentario, foto, fecha);
		this.restaurante = restaurante;
		this.cliente = cliente;
	}
	
	public CalificacionRestaurante(Calificacion calificacion, Cliente cliente, Restaurante restaurante) {
		super(calificacion.getPuntaje(), calificacion.getComentario(), calificacion.getFoto(), calificacion.getFecha());
		this.restaurante = restaurante;
		this.cliente = cliente;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}