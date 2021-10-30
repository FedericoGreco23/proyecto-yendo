package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="RestaurantePedido")
public class DTRestaurantePedido implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String _id;
	private String nombre;
	private BigInteger cantPedidos;
	
	
	public DTRestaurantePedido() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DTRestaurantePedido(String mailRestaurante, BigInteger cantPedidos) {
		super();
		this._id = mailRestaurante;
		this.cantPedidos = cantPedidos;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMailRestaurante() {
		return _id;
	}

	public void setMailRestaurante(String mailRestaurante) {
		this._id = mailRestaurante;
	}

	public BigInteger getCantPedidos() {
		return cantPedidos;
	}

	public void setCantPedidos(BigInteger cantPedidos) {
		this.cantPedidos = cantPedidos;
	}
	
}
