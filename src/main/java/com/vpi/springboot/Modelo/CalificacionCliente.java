package com.vpi.springboot.Modelo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToOne;

import com.vpi.springboot.IdCompuestas.CalificacionClienteId;

@Entity
@IdClass(CalificacionClienteId.class)
public class CalificacionCliente extends Calificacion {

	@Id
	@OneToOne
	private Restaurante restaurante;
	@Id
	@OneToOne
	private Cliente cliente;
	
	
	public CalificacionCliente() {
		super();
		// TODO Auto-generated constructor stub
	}
	
		
	public CalificacionCliente(int id, int puntaje, String comentario, String foto, LocalDateTime fecha) {
		super(id, puntaje, comentario, foto, fecha);
		// TODO Auto-generated constructor stub
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