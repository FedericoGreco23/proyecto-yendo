package com.vpi.springboot.Repositorios;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpi.springboot.Modelo.Categoria;



@Repository
public interface CategoriaRepositorio extends JpaRepository<Categoria, Integer>{

}