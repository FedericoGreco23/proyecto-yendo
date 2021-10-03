package com.vpi.springboot.Repositorios;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.Direccion;



@Repository
public interface DireccionRepositorio extends JpaRepository<Direccion, Integer>{

}