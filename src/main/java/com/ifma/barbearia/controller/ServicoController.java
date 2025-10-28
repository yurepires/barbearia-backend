package com.ifma.barbearia.controller;

import com.ifma.barbearia.DTOs.ServicoDto;
import com.ifma.barbearia.DTOs.ResponseDto;
import com.ifma.barbearia.constants.ServicoConstants;
import com.ifma.barbearia.services.IServicoService;
import jakarta.validation.Valid;
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
public class ServicoController {

    private IServicoService iServicoService;

    @PostMapping("/criarServico")
    public ResponseEntity<ResponseDto> criarServico(@Valid @RequestBody ServicoDto servicoDto) {
        iServicoService.criarServico(servicoDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(ServicoConstants.STATUS_201, ServicoConstants.MESSAGE_201));
    }

    @GetMapping("/buscarServico")
    public ResponseEntity<ServicoDto> buscarServico(@RequestParam Long servicoId) {
        ServicoDto servicoDto = iServicoService.buscarServico(servicoId);
        return ResponseEntity.status(HttpStatus.OK).body(servicoDto);
    }

    @GetMapping("/buscarTodosServicos")
    public ResponseEntity<List<ServicoDto>> buscarTodosServicos() {
        List<ServicoDto> servicoDtos = iServicoService.buscarTodosServicos();
        return ResponseEntity.status(HttpStatus.OK).body(servicoDtos);
    }

    @PutMapping("/atualizarServico")
    public ResponseEntity<ResponseDto> atualizarServico(@Valid @RequestBody ServicoDto servicoDto) {
        boolean atualizado = iServicoService.atualizarServico(servicoDto);
        if (atualizado) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(ServicoConstants.STATUS_200, ServicoConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(ServicoConstants.STATUS_417, ServicoConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/deletarServico")
    public ResponseEntity<ResponseDto> deletarServico(@RequestParam Long servicoId) {
        boolean deletado = iServicoService.deletarServico(servicoId);
        if (deletado) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(ServicoConstants.STATUS_200, ServicoConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(ServicoConstants.STATUS_417, ServicoConstants.MESSAGE_417_DELETE));
        }
    }
}