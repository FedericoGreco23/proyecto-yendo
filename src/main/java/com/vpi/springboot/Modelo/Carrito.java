package com.vpi.springboot.Modelo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import com.vpi.springboot.Modelo.dto.DTProductoCarrito;

@Document(collection="carrito")
public class Carrito {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull(message = "mail no puede ser nulo")
	private String mail;
	@NotNull(message = "valoracion no puede ser nulo")
	private DTProductoCarrito productoCarrito;
	
	
	

	public Carrito() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Carrito(@NotNull(message = "mail no puede ser nulo") String mail,
			@NotNull(message = "valoracion no puede ser nulo") DTProductoCarrito productoCarrito) {
		super();
		this.mail = mail;
		this.productoCarrito = productoCarrito;
	}
	
	
	public int getId() {
		return id;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public DTProductoCarrito getProductoCarrito() {
		return productoCarrito;
	}
	public void setProductoCarrito(DTProductoCarrito productoCarrito) {
		this.productoCarrito = productoCarrito;
	}


}
