package com.ifma.barbearia.controller;

import com.ifma.barbearia.DTOs.ClienteDto;
import com.ifma.barbearia.DTOs.ResponseDto;
import com.ifma.barbearia.constants.ClienteConstants;
import com.ifma.barbearia.services.IClienteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class ClienteController {

    private IClienteService iClienteService;

    @PostMapping("/criarCliente")
    public ResponseEntity<ResponseDto> criarCliente(@Valid @RequestBody ClienteDto clienteDto) {
        iClienteService.criarCliente(clienteDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(ClienteConstants.STATUS_201, ClienteConstants.MESSAGE_201));
    }

}
