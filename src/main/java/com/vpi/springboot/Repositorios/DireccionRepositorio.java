package com.vpi.springboot.Repositorios;


import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;



@Transactional
public interface DireccionRepositorio extends JpaRepository<Direccion, Integer>{
	
	  @Query("select d from Direccion d where d.calleNro = :calleNro and d.cliente = :cliente")
	  Optional<Direccion> findByStreetNumberandMail(@Param("calleNro") String calleNro,
	                                 @Param("cliente") Cliente cliente);
	
}