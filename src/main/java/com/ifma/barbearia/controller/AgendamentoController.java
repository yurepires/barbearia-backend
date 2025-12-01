package com.ifma.barbearia.controller;

import com.ifma.barbearia.DTOs.AgendamentoDto;
import com.ifma.barbearia.DTOs.ResponseDto;
import com.ifma.barbearia.constants.AgendamentoConstants;
import com.ifma.barbearia.services.IAgendamentoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/api/agendamento", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class AgendamentoController {

    private IAgendamentoService iAgendamentoService;

    @PostMapping("/criarAgendamento")
    public ResponseEntity<ResponseDto> criarAgendamento(@Valid @RequestBody AgendamentoDto agendamentoDto) {
        iAgendamentoService.criarAgendamento(agendamentoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(AgendamentoConstants.STATUS_201, AgendamentoConstants.MESSAGE_201));
    }

    @GetMapping("/buscarAgendamento")
    public ResponseEntity<AgendamentoDto> buscarAgendamento(@RequestParam Long agendamentoId) {
        AgendamentoDto agendamentoDto = iAgendamentoService.buscarAgendamento(agendamentoId);
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoDto);
    }

    @GetMapping("buscarTodosAgendamentos")
    public ResponseEntity<List<AgendamentoDto>> buscarTodosAgendamentos() {
        List<AgendamentoDto> agendamentoDtoList = iAgendamentoService.buscarTodosAgendamentos();
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoDtoList);
    }

    @GetMapping("/buscarPorCliente")
    public ResponseEntity<List<AgendamentoDto>> buscarPorCliente(@RequestParam Long clienteId) {
        List<AgendamentoDto> agendamentoDtoList = iAgendamentoService.buscarAgendamentosPorCliente(clienteId);
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoDtoList);
    }

    @GetMapping("/buscarPorIntervaloDeDatas")
    public ResponseEntity<List<AgendamentoDto>> buscarPorIntervaloDeDatas(@RequestParam LocalDate inicio, @RequestParam LocalDate fim) {
        List<AgendamentoDto> agendamentoDtoList = iAgendamentoService.buscarAgendamentosPorIntervaloDeDatas(inicio, fim);
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoDtoList);
    }

    @PutMapping("/atualizarAgendamento")
    public ResponseEntity<ResponseDto> atualizarAgendamento(@Valid @RequestBody AgendamentoDto agendamentoDto) {
        boolean atualizado = iAgendamentoService.atualizarAgendamento(agendamentoDto);
        if (atualizado)
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(AgendamentoConstants.STATUS_200, AgendamentoConstants.MESSAGE_200));
        else
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(AgendamentoConstants.STATUS_417, AgendamentoConstants.MESSAGE_417_UPDATE));
    }

    @PatchMapping("/cancelarAgendamento")
    public ResponseEntity<ResponseDto> cancelarAgendamento(@RequestParam Long agendamentoId) {
        boolean cancelado = iAgendamentoService.cancelarAgendamento(agendamentoId);
        if (cancelado) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(AgendamentoConstants.STATUS_200, AgendamentoConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(AgendamentoConstants.STATUS_417, AgendamentoConstants.MESSAGE_417_DELETE));
        }
    }

    @PatchMapping("/concluirAgendamento")
    public ResponseEntity<ResponseDto> concluirAgendamento(@RequestParam Long agendamentoId) {
        iAgendamentoService.concluirAgendamento(agendamentoId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(AgendamentoConstants.STATUS_200, AgendamentoConstants.MESSAGE_200));
    }

}
