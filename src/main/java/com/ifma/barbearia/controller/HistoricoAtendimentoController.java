package com.ifma.barbearia.controller;

import com.ifma.barbearia.DTOs.HistoricoAtendimentoDto;
import com.ifma.barbearia.entities.HistoricoAtendimento;
import com.ifma.barbearia.mappers.HistoricoAtendimentoMapper;
import com.ifma.barbearia.services.IHistoricoAtendimentoService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/api/historicoAtendimento", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class HistoricoAtendimentoController {

    private IHistoricoAtendimentoService iHistoricoAtendimentoService;

    @GetMapping("/listarTodos")
    public List<HistoricoAtendimentoDto> listarTodos() {
        return iHistoricoAtendimentoService.listarTodos().stream()
                .map((HistoricoAtendimento historicoAtendimento) -> HistoricoAtendimentoMapper.mapToHistoricoAtendimentoDto(historicoAtendimento, new HistoricoAtendimentoDto()))
                .toList();
    }

    @GetMapping("/listarPorCliente")
    public List<HistoricoAtendimentoDto> historicoAtendimentoPorCliente(@RequestParam Long clienteId) {
        return iHistoricoAtendimentoService.listarPorCliente(clienteId)
                .stream().map((HistoricoAtendimento historicoAtendimento) -> HistoricoAtendimentoMapper.mapToHistoricoAtendimentoDto(historicoAtendimento, new HistoricoAtendimentoDto()))
                .toList();
    }

    @GetMapping("/listarPorBarbeiro")
    public List<HistoricoAtendimentoDto> historicoAtendimentoPorBarbeiro(@RequestParam Long barbeiroId) {
        return iHistoricoAtendimentoService.listarPorBarbeiro(barbeiroId)
                .stream().map((HistoricoAtendimento historicoAtendimento) -> HistoricoAtendimentoMapper.mapToHistoricoAtendimentoDto(historicoAtendimento, new HistoricoAtendimentoDto()))
                .toList();
    }

    @GetMapping("/listarPorServico")
    public List<HistoricoAtendimentoDto> historicoAtendimentoPorServico(@RequestParam Long servicoId) {
        return iHistoricoAtendimentoService.listarPorServico(servicoId).stream()
                .map((HistoricoAtendimento historicoAtendimento) -> HistoricoAtendimentoMapper.mapToHistoricoAtendimentoDto(historicoAtendimento, new HistoricoAtendimentoDto()))
                .toList();
    }

    @GetMapping("/listarPorIntervaloDeData")
    public List<HistoricoAtendimentoDto> listarPorIntervaloDeData(@RequestParam LocalDate inicio, @RequestParam LocalDate fim) {
        return iHistoricoAtendimentoService.listarPorIntervaloDeDatas(inicio, fim).stream()
                .map((HistoricoAtendimento historicoAtendimento) -> HistoricoAtendimentoMapper.mapToHistoricoAtendimentoDto(historicoAtendimento, new HistoricoAtendimentoDto()))
                .toList();
    }

}
