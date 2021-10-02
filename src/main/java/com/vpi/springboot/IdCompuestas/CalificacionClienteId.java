package com.vpi.springboot.IdCompuestas;

import java.io.Serializable;

public class CalificacionClienteId implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String mailRestaurante;
	private String mailCliente;
	
	
	public CalificacionClienteId() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getMailRestaurante() {
		return mailRestaurante;
	}


	public void setMailRestaurante(String mailRestaurante) {
		this.mailRestaurante = mailRestaurante;
	}


	public String getMailCliente() {
		return mailCliente;
	}


	public void setMailCliente(String mailCliente) {
		this.mailCliente = mailCliente;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mailCliente == null) ? 0 : mailCliente.hashCode());
		result = prime * result + ((mailRestaurante == null) ? 0 : mailRestaurante.hashCode());
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
		if (mailCliente == null) {
			if (other.mailCliente != null)
				return false;
		} else if (!mailCliente.equals(other.mailCliente))
			return false;
		if (mailRestaurante == null) {
			if (other.mailRestaurante != null)
				return false;
		} else if (!mailRestaurante.equals(other.mailRestaurante))
			return false;
		return true;
	}
	
	
	
	
	

}
