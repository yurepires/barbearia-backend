package com.ifma.barbearia.controller;

import com.ifma.barbearia.DTOs.AuthRequest;
import com.ifma.barbearia.DTOs.AuthResponse;
import com.ifma.barbearia.entities.AdmUser;
import com.ifma.barbearia.security.JwtUtil;
import com.ifma.barbearia.services.IAdmUserService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final IAdmUserService admUserService;
    private final JwtUtil jwtUtil;

    public AuthController(IAdmUserService admUserService, JwtUtil jwtUtil) {
        this.admUserService = admUserService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("admin/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        AdmUser user = admUserService.findByUsername(authRequest.getUsername());

        if (user != null && admUserService.passwordMatches(authRequest.getPassword(), user.getPassword())
                && "ADM".equals(user.getRole())) {
            String token = JwtUtil.generateToken(user.getUsername(), user.getRole());
            return ResponseEntity.ok(new AuthResponse(token));
        }

        return ResponseEntity.status(401).body("Usuário ou senha inválidos");
    }
}
