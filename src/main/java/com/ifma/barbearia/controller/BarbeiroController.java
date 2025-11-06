package com.ifma.barbearia.controller;

import com.ifma.barbearia.DTOs.BarbeiroDto;
import com.ifma.barbearia.DTOs.ResponseDto;
import com.ifma.barbearia.constants.BarbeiroConstants;
import com.ifma.barbearia.services.IBarbeiroService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class BarbeiroController {

    private IBarbeiroService iBarbeiroService;

    @PostMapping("/criarBarbeiro")
    public ResponseEntity<ResponseDto> criarBarbeiro(@Valid @RequestBody BarbeiroDto barbeiroDto) {
        iBarbeiroService.criarBarbeiro(barbeiroDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(BarbeiroConstants.STATUS_201, BarbeiroConstants.MESSAGE_201));
    }

    @GetMapping("/buscarBarbeiro")
    public ResponseEntity<BarbeiroDto> buscarBarbeiro(
            @RequestParam @Email(message = "Por favor, insira um endereço de email válido!") String email) {
        BarbeiroDto barbeiroDto = iBarbeiroService.buscarBarbeiro(email);
        return ResponseEntity.status(HttpStatus.OK).body(barbeiroDto);
    }

    @GetMapping("/buscarTodosBarbeiros")
    public ResponseEntity<List<BarbeiroDto>> buscarTodosBarbeiros() {
        List<BarbeiroDto> barbeiroDtos = iBarbeiroService.buscarTodosBarbeiros();
        return ResponseEntity.status(HttpStatus.OK).body(barbeiroDtos);
    }

    @PutMapping("/atualizarBarbeiro")
    public ResponseEntity<ResponseDto> atualizarBarbeiro(@Valid @RequestBody BarbeiroDto barbeiroDto) {
        boolean atualizado = iBarbeiroService.atualizarBarbeiro(barbeiroDto);
        if (atualizado) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(BarbeiroConstants.STATUS_200, BarbeiroConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(BarbeiroConstants.STATUS_417, BarbeiroConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/deletarBarbeiro")
    public ResponseEntity<ResponseDto> deletarBarbeiro(
            @RequestParam @Email(message = "Por favor, insira um endereço de email válido!") String email) {
        boolean deletado = iBarbeiroService.deletarBarbeiro(email);
        if (deletado) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(BarbeiroConstants.STATUS_200, BarbeiroConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(BarbeiroConstants.STATUS_417, BarbeiroConstants.MESSAGE_417_DELETE));
        }
    }

}
