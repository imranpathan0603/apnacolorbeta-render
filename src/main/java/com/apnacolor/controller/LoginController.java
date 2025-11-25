package com.apnacolor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication; // ✅ Correct package
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.apnacolor.entity.User;
import com.apnacolor.repository.UserRepository;
import com.apnacolor.request.LoginRequest;
import com.apnacolor.request.LoginResponse;
import com.apnacolor.request.SignupRequest;
import com.apnacolor.request.UserDto;
import com.apnacolor.services.UserService;
import com.apnacolor.services.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

//import com.apnacolor.securityconfig.JwtUtil;

@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

	private final UserServiceImpl userServiceImpl;

	private final ProductController productController;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	LoginController(ProductController productController, UserServiceImpl userServiceImpl) {
		this.productController = productController;
		this.userServiceImpl = userServiceImpl;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String role = userDetails.getAuthorities().iterator().next().getAuthority();

			// Get user from DB to fetch ID
			User user = userRepository.findByUsername(userDetails.getUsername());
			Long userId = user.getId(); // Assumes your User entity has getId()
			System.out.println("user id: " + userId);

			LoginResponse response = new LoginResponse("Welcome", userDetails.getUsername(), role, userId);
			System.out.println("Response object: " + new ObjectMapper().writeValueAsString(response));
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
		if (userRepository.findByUsername(signupRequest.getUsername()) != null) {
			return ResponseEntity.ok("user allready exist");
		}

		User user = new User();
		user.setUsername(signupRequest.getUsername());
		user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
		user.setContactNo(signupRequest.getContactNo());
		user.setEmail(signupRequest.getEmail());
		user.setAddress(signupRequest.getAddress());
		user.setRole(signupRequest.getRole() != null ? signupRequest.getRole() : "CUSTOMER");

		userRepository.save(user);
		return ResponseEntity.ok("User registered successfully");
	}

//	for a amin to get all users 
	// In UserController.java
	@GetMapping("/all")
	public List<User> getAllUsers() {
		return userService.getAllUsers(); // returns List<User>
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
		UserDto user = userService.getUserById(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);
	}

//	delete
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		boolean isDeleted = userService.deleteUserById(id);
		if (isDeleted) {
			return ResponseEntity.ok("User deleted successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
	}

}