package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;

public class BalanceByFechaDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Object BalanceByFechas;
	private String totalPeriodo;
	private String totalPeriodoEfectivo;
	private String totalPeriodoPaypal;

	public BalanceByFechaDTO() {
		// TODO Auto-generated constructor stub
	}



	public BalanceByFechaDTO(Object balanceByFechas, String totalPeriodo, String totalPeriodoEfectivo,
			String totalPeriodoPaypal) {
		super();
		BalanceByFechas = balanceByFechas;
		this.totalPeriodo = totalPeriodo;
		this.totalPeriodoEfectivo = totalPeriodoEfectivo;
		this.totalPeriodoPaypal = totalPeriodoPaypal;
	}



	public Object getLista() {
		return BalanceByFechas;
	}

	public void setLista(Object BalanceByFechas) {
		this.BalanceByFechas = BalanceByFechas;
	}

	public String getTotal() {
		return totalPeriodo;
	}

	public void setTotal(String totalPeriodo) {
		this.totalPeriodo = totalPeriodo;
	}

	public Object getBalanceByFechas() {
		return BalanceByFechas;
	}

	public void setBalanceByFechas(Object balanceByFechas) {
		BalanceByFechas = balanceByFechas;
	}

	public String getTotalPeriodo() {
		return totalPeriodo;
	}

	public void setTotalPeriodo(String totalPeriodo) {
		this.totalPeriodo = totalPeriodo;
	}

	public String getTotalPeriodoEfectivo() {
		return totalPeriodoEfectivo;
	}

	public void setTotalPeriodoEfectivo(String totalPeriodoEfectivo) {
		this.totalPeriodoEfectivo = totalPeriodoEfectivo;
	}

	public String getTotalPeriodoPaypal() {
		return totalPeriodoPaypal;
	}

	public void setTotalPeriodoPaypal(String totalPeriodoPaypal) {
		this.totalPeriodoPaypal = totalPeriodoPaypal;
	}
	
	

	
}
