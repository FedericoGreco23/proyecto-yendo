package com.vpi.springboot.IdCompuestas;

import java.io.Serializable;

public class CalificacionClienteId implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String restaurante;
	private String cliente;
	
	
	public CalificacionClienteId() {
		super();
		// TODO Auto-generated constructor stub
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


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
		result = prime * result + ((restaurante == null) ? 0 : restaurante.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CalificacionClienteId other = (CalificacionClienteId) obj;
		if (cliente == null) {
			if (other.cliente != null)
				return false;
		} else if (!cliente.equals(other.cliente))
			return false;
		if (restaurante == null) {
			if (other.restaurante != null)
				return false;
		} else if (!restaurante.equals(other.restaurante))
			return false;
		return true;
	}



	
	
	

}
