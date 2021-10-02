package com.vpi.springboot.Modelo;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="ADMIN")
public class Admin extends Usuario{
	private static final long serialVersionUID = 1L;
	
	private String nickname;

	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Admin(String mail, String contrasenia, String telefono, String foto, Boolean bloqueado, 
			Boolean activo, String nickname) {
		super(mail, contrasenia, telefono, foto, bloqueado, activo);
		this.nickname = nickname;
	}
	
	
//----------------------GETTERS Y SETTERS---------------------------------------------------------

	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
