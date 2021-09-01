package com.vpi.springboot.Repository;

import com.vpi.springboot.Modelo.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<Usuario, String>{

}
