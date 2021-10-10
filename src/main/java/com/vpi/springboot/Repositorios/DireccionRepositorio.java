package com.vpi.springboot.Repositorios;


import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.Direccion;



@Transactional
public interface DireccionRepositorio extends JpaRepository<Direccion, Integer>{

}