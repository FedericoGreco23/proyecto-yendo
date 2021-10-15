package com.vpi.springboot.Modelo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DTCarrito implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private List<DTProductoCarrito> dtProductoCarritoList= new ArrayList<DTProductoCarrito>();

  
	
	
	 public DTCarrito(List<DTProductoCarrito> dtProductoCarritoList) {
	        this.dtProductoCarritoList = dtProductoCarritoList;
	    }

    public List<DTProductoCarrito> getDtProductoCarritoList() {
        return dtProductoCarritoList;
    }

    public void setDtProductoCarritoList(List<DTProductoCarrito> dtProductoCarritoList) {
        this.dtProductoCarritoList = dtProductoCarritoList;
    }

    @Override
    public String toString() {
        return "DTCarrito{" +
                "dtProductoCarritoList=" + dtProductoCarritoList +
                '}';
    }
}
