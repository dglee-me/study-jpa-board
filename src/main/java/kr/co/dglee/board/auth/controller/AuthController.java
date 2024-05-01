package kr.co.dglee.board.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.dglee.board.auth.service.AuthService;
import kr.co.dglee.board.user.dto.LoginDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/auth/login")
	public ResponseEntity login(@RequestBody LoginDTO user) {
		return ResponseEntity.ok("Login Success");
	}
}
