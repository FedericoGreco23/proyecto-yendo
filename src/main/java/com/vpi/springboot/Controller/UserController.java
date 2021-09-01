package com.vpi.springboot.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpi.springboot.Modelo.Usuario;

import com.vpi.springboot.Repository.UserRepository;
import com.vpi.springboot.exception.ResourceNotFoundException;

@RestController 
@RequestMapping("api/pruebas/")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;

	@GetMapping("/user")
	public List<Usuario> getAllUser() {
		return userRepo.findAll();
	}

//	@GetMapping("/Users/{id}")
//	public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long UserId)
//			throws ResourceNotFoundException {
//		User User = userRepo.findById(UserId)
//				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + UserId));
//		return ResponseEntity.ok().body(User);
//	}

	@PostMapping("/users")
	public Usuario createUser(@RequestBody Usuario user) {
		return userRepo.save(user);
	}

//	@PutMapping("/Users/{id}")
//	public ResponseEntity<User> updateUser(@PathVariable(value = "id") String cedula,
//			@Valid @RequestBody User UserDetails) throws ResourceNotFoundException {
//		User User = userRepo.findById(cedula)
//				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + cedula));
//
//		User.setNombre(UserDetails.getNombre());
//		User.setCedula(UserDetails.getCedula());
//		final User updatedUser = userRepo.save(User);
//		return ResponseEntity.ok(updatedUser);
//	}

	@DeleteMapping("/Users/{id}")
	public Map<String, Boolean> deleteUser(@PathVariable(value = "id") String cedula)
			throws ResourceNotFoundException {
		Usuario User = userRepo.findById(cedula)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this cedula :: " + cedula));

		userRepo.delete(User);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

}
