package com.vpi.springboot.Modelo;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.vpi.springboot.Modelo.dto.DTProductoCarrito;

@Document(collection="carrito")
public class Carrito {
	@Id
	private long id;
	
	@NotNull(message = "mail no puede ser nulo")
	private String mail;
	@NotNull(message = "mail del restaurante no puede ser nulo")
	private String mailRestaurante;
	@NotNull(message = "valoracion no puede ser nulo")
	private List <DTProductoCarrito> productoCarrito = new ArrayList<>();

	private boolean activo;
	
	
	

	public Carrito() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Carrito(@NotNull(message = "mail no puede ser nulo") String mail,@NotNull(message = "mail del restaurante no puede ser nulo") String mailRestaurante,
			@NotNull(message = "valoracion no puede ser nulo") List<DTProductoCarrito> productoCarrito, boolean activo) {
		super();
		this.mail = mail;
		this.mailRestaurante = mailRestaurante;
		this.productoCarrito = productoCarrito;
		this.activo = activo;
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long l) {
		this.id = l;
	}


	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getMailRestaurante() {
		return mailRestaurante;
	}


	public void setMailRestaurante(String mailRestaurante) {
		this.mailRestaurante = mailRestaurante;
	}


	public List<DTProductoCarrito> getProductoCarrito() {
		return productoCarrito;
	}
	public void setProductoCarrito(List<DTProductoCarrito> productoCarrito) {
		this.productoCarrito = productoCarrito;
	}
	
	public void addProductoCarrito(DTProductoCarrito pc) {
		this.productoCarrito.add(pc);
	} 


	public boolean isActivo() {
		return activo;
	}


	public void setActivo(boolean activo) {
		this.activo = activo;
	}


}
