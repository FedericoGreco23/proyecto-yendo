package com.vpi.springboot.Modelo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToOne;

import com.vpi.springboot.IdCompuestas.CalificacionRestauranteId;

@Entity
@IdClass(CalificacionRestauranteId.class)
public class CalificacionRestaurante extends Calificacion {

	@Id
	@OneToOne
	private Restaurante restaurante;
	@Id
	@OneToOne
	private Cliente cliente;

	public CalificacionRestaurante() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CalificacionRestaurante(int id, int puntaje, String comentario, String foto, LocalDateTime fecha) {
		super(id, puntaje, comentario, foto, fecha);
		// TODO Auto-generated constructor stub
	}
	
	public CalificacionRestaurante(int id, int puntaje, String comentario, String foto, LocalDateTime fecha, Restaurante restaurante, Cliente cliente) {
		super(id, puntaje, comentario, foto, fecha);
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