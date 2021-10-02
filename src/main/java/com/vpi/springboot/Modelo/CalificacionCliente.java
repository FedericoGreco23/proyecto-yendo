package com.vpi.springboot.Modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CalificacionCliente extends Calificacion implements Serializable {
	private static final long serialVersionUID = 1L;

	private Restaurante restaurante;
	private Cliente cliente;
	
	public CalificacionCliente() {
		super();
		// TODO Auto-generated constructor stub
	}
}