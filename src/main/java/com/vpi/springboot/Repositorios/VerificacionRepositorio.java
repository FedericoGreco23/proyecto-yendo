package com.vpi.springboot.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.TokenVerificacion;

@Repository
public interface VerificacionRepositorio extends JpaRepository<TokenVerificacion, Integer> {
	
	@Query("SELECT u FROM TokenVerificacion u WHERE u.token = :token")
	TokenVerificacion findByToken(@Param("token") String token);
}