package com.vpi.springboot.Modelo;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="productos")
public class Pedidos {
	
	@Id
	private String id;
	private String comida;
	private int valoracion;
	private Date createdAt;
	private Date updatedAt;

}
