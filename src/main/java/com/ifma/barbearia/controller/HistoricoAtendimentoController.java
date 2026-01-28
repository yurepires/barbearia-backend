package com.ifma.barbearia.controller;

import com.ifma.barbearia.dto.ErrorResponseDto;
import com.ifma.barbearia.dto.HistoricoAtendimentoDto;
import com.ifma.barbearia.mapper.HistoricoAtendimentoMapper;
import com.ifma.barbearia.service.IHistoricoAtendimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Histórico de Atendimentos REST API", description = "REST APIs para Buscar detalhes de histórico de atendimentos")
@RestController
@RequestMapping(path = "/api/historicoAtendimento", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class HistoricoAtendimentoController {

    private final IHistoricoAtendimentoService iHistoricoAtendimentoService;
    private final HistoricoAtendimentoMapper historicoAtendimentoMapper;

    @Operation(
            summary = "Buscar detalhes de todos os históricos de atendimento registrados",
            description = "REST API para buscar detalhes de todos os históricos de atendimentos"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/listarTodos")
    public List<HistoricoAtendimentoDto> listarTodos() {
        return iHistoricoAtendimentoService.listarTodos().stream()
                .map(historicoAtendimentoMapper::toDto)
                .toList();
    }

    @Operation(
            summary = "Buscar detalhes de todo o histórico de atendimento de um cliente",
            description = "REST API para buscar detalhes de todo o histórico de atendimento de um cliente pelo email"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/listarPorCliente")
    public List<HistoricoAtendimentoDto> historicoAtendimentoPorCliente(@RequestParam String clienteEmail) {
        return iHistoricoAtendimentoService.listarPorCliente(clienteEmail)
                .stream()
                .map(historicoAtendimentoMapper::toDto)
                .toList();
    }

    @Operation(
            summary = "Buscar detalhes de todo o histórico de atendimento de um barbeiro",
            description = "REST API para buscar detalhes de todo o histórico de atendimento de um barbeiro pelo email"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/listarPorBarbeiro")
    public List<HistoricoAtendimentoDto> historicoAtendimentoPorBarbeiro(@RequestParam String barbeiroEmail) {
        return iHistoricoAtendimentoService.listarPorBarbeiro(barbeiroEmail)
                .stream()
                .map(historicoAtendimentoMapper::toDto)
                .toList();
    }

    @Operation(
            summary = "Buscar detalhes de todo o histórico de atendimento de um serviço",
            description = "REST API para buscar detalhes de todo o histórico de atendimento de um serviço pelo ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/listarPorServico")
    public List<HistoricoAtendimentoDto> historicoAtendimentoPorServico(@RequestParam Long servicoId) {
        return iHistoricoAtendimentoService.listarPorServico(servicoId).stream()
                .map(historicoAtendimentoMapper::toDto)
                .toList();
    }

    @Operation(
            summary = "Buscar detalhes de todo o histórico de atendimento por um intervalo de datas",
            description = "REST API para buscar detalhes de todo o histórico de atendimento por um intervalo de datas"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/listarPorIntervaloDeData")
    public List<HistoricoAtendimentoDto> listarPorIntervaloDeData(@RequestParam LocalDate inicio,
                                                                  @RequestParam LocalDate fim) {
        return iHistoricoAtendimentoService.listarPorIntervaloDeDatas(inicio, fim).stream()
                .map(historicoAtendimentoMapper::toDto)
                .toList();
    }

}
