package com.vpi.springboot.Modelo.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaBalanceVentaDTO {
	
	private Map<String,BalanceByFechaDTO> balances = new HashMap<String,BalanceByFechaDTO>();

	public ListaBalanceVentaDTO() {
		// TODO Auto-generated constructor stub
	}

	public ListaBalanceVentaDTO(Map<String, BalanceByFechaDTO> balances) {
		super();
		this.balances = balances;
	}

	public Map<String, BalanceByFechaDTO> getBalances() {
		return balances;
	}

	public void setBalances(Map<String, BalanceByFechaDTO> balances) {
		this.balances = balances;
	}

	
	

}
