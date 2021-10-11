package com.vpi.springboot.Modelo;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.vpi.springboot.Modelo.dto.DTCliente;

@Entity
public class Cliente extends Usuario{

	@Column(unique=true)
    private String nickname;
    private Float calificacionPromedio;
    private Float saldoBono;
    private String nombre;
    private String apellido;
    private String tokenDispositivo;
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Direccion> direcciones;
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Pedido> pedidos;


    
    
    public Cliente() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Cliente(String mail, String contrasenia, String telefono, String foto, Boolean bloqueado, 
			Boolean activo, LocalDate fechaCreacion, String nickname, Float calificacionPromedio, Float saldoBono, String nombre, 
			String apellido, String tokenDispositivo) {
		super(mail, contrasenia, telefono, foto, bloqueado, activo, fechaCreacion);
		// TODO Auto-generated constructor stub
        this.nickname = nickname;
        this.calificacionPromedio = calificacionPromedio;
        this.saldoBono = saldoBono;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tokenDispositivo = tokenDispositivo;
	}
	
	//Funcion para pasar a DT de buscarUsuario y otros
	public DTCliente getDatos() {
		return new DTCliente(this.getMail(), this.getTelefono(), this.getFoto(), this.getBloqueado(), this.getActivo(), this.getFechaCreacion(), 
				this.getNickname(), this.getCalificacionPromedio(), this.getNombre(), this.getApellido());
	}

	
//----------------------GETTERS Y SETTERS---------------------------------------------------------
	

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Float getCalificacionPromedio() {
        return calificacionPromedio;
    }

    public void setCalificacionPromedio(Float calificacionPromedio) {
        this.calificacionPromedio = calificacionPromedio;
    }

    public Float getSaldoBono() {
        return saldoBono;
    }

    public void setSaldoBono(Float saldoBono) {
        this.saldoBono = saldoBono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTokenDispositivo() {
        return tokenDispositivo;
    }

    public void setTokenDispositivo(String tokenDispositivo) {
        this.tokenDispositivo = tokenDispositivo;
    }

	public List<Direccion> getDirecciones() {
		return direcciones;
	}

	public void setDirecciones(List<Direccion> direcciones) {
		this.direcciones = direcciones;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}


    
    
}
