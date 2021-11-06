package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

public class BalanceByFechaDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Object BalanceByFechas;
	private String totalPeriodo;

	public BalanceByFechaDTO() {
		// TODO Auto-generated constructor stub
	}

	public BalanceByFechaDTO(Object BalanceByFechas, String totalPeriodo) {
		super();
		this.BalanceByFechas = BalanceByFechas;
		this.totalPeriodo = totalPeriodo;
	}

	public Object getLista() {
		return BalanceByFechas;
	}

	public void setLista(Object lista) {
		this.BalanceByFechas = lista;
	}

	public String getTotal() {
		return totalPeriodo;
	}

	public void setTotal(String total) {
		this.totalPeriodo = total;
	}

	
}
