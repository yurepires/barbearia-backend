package com.ifma.barbearia.controller;

import com.ifma.barbearia.DTOs.AuthRequest;
import com.ifma.barbearia.DTOs.AuthResponse;
import com.ifma.barbearia.DTOs.ErrorResponseDto;
import com.ifma.barbearia.entities.AdmUser;
import com.ifma.barbearia.security.JwtUtil;
import com.ifma.barbearia.services.IAdmUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Admin Auth REST API",
        description = "REST API para login de administrador"
)
@RestController
@RequestMapping(path = "/api/admin/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final IAdmUserService admUserService;

    public AuthController(IAdmUserService admUserService) {
        this.admUserService = admUserService;
    }

    @Operation(
            summary = "Logar como administrador",
            description = "REST API para logar como administrador"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PostMapping("login")
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
