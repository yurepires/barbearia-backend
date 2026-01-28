package com.ifma.barbearia.controller;

import com.ifma.barbearia.dto.AgendamentoDto;
import com.ifma.barbearia.dto.ErrorResponseDto;
import com.ifma.barbearia.dto.ResponseDto;
import com.ifma.barbearia.constants.AgendamentoConstants;
import com.ifma.barbearia.constants.CommonConstants;
import com.ifma.barbearia.service.IAgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(
        name = "Agendamentos REST API",
        description = "REST APIs para criar, atualizar, buscar, concluir e cancelar agendamentos"
)
@RestController
@RequestMapping(path = "/api/agendamento", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class AgendamentoController {

    private IAgendamentoService agendamentoService;

    @Operation(
            summary = "Criar Agendamento",
            description = "REST API para criar agendamento"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PostMapping("/criarAgendamento")
    public ResponseEntity<ResponseDto> criarAgendamento(@Valid @RequestBody AgendamentoDto agendamentoDto) {
        agendamentoService.criarAgendamento(agendamentoDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(CommonConstants.STATUS_201, AgendamentoConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Buscar detalhes de agendamento",
            description = "REST API para buscar detalhes de agendamento pelo id fornecido"
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
    @GetMapping("/buscarAgendamento")
    public ResponseEntity<AgendamentoDto> buscarAgendamento(@RequestParam Long agendamentoId) {
        AgendamentoDto agendamentoDto = agendamentoService.buscarAgendamento(agendamentoId);
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoDto);
    }

    @Operation(
            summary = "Buscar todos os agendamentos",
            description = "REST API para buscar detalhes de todos os agendamentos"
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
    @GetMapping("buscarTodosAgendamentos")
    public ResponseEntity<List<AgendamentoDto>> buscarTodosAgendamentos() {
        List<AgendamentoDto> agendamentoDtoList = agendamentoService.buscarTodosAgendamentos();
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoDtoList);
    }

    @Operation(
            summary = "Buscar detalhes de agendamento pelo cliente",
            description = "REST API para buscar detalhes de agendamento pelo ID do cliente"
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
    @GetMapping("/buscarPorCliente")
    public ResponseEntity<List<AgendamentoDto>> buscarPorCliente(@RequestParam String clienteEmail) {
        List<AgendamentoDto> agendamentoDtoList = agendamentoService.buscarAgendamentosPorCliente(clienteEmail);
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoDtoList);
    }

    @Operation(
            summary = "Buscar agendamentos por um intervalo de datas",
            description = "REST API para buscar detalhes de agendamentos por um intervalo de datas"
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
    @GetMapping("/buscarPorIntervaloDeDatas")
    public ResponseEntity<List<AgendamentoDto>> buscarPorIntervaloDeDatas(@RequestParam LocalDate inicio, @RequestParam LocalDate fim) {
        List<AgendamentoDto> agendamentoDtoList = agendamentoService.buscarAgendamentosPorIntervaloDeDatas(inicio, fim);
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoDtoList);
    }

    @Operation(
            summary = "Atualizar detalhes de agendamento",
            description = "REST API para atualizar detalhes de agendamento pelo ID fornecido"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PutMapping("/atualizarAgendamento")
    public ResponseEntity<ResponseDto> atualizarAgendamento(@Valid @RequestBody AgendamentoDto agendamentoDto) {
        boolean atualizado = agendamentoService.atualizarAgendamento(agendamentoDto);
        if (atualizado)
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto(CommonConstants.STATUS_200, CommonConstants.MESSAGE_200));
        else
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(
                    CommonConstants.STATUS_417, CommonConstants.MESSAGE_417_UPDATE));
    }

    @Operation(
            summary = "Cancelar agendamento",
            description = "REST API para cancelar um agendamento pelo ID fornecido"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PatchMapping("/cancelarAgendamento")
    public ResponseEntity<ResponseDto> cancelarAgendamento(@RequestParam Long agendamentoId) {
        boolean cancelado = agendamentoService.cancelarAgendamento(agendamentoId);
        if (cancelado) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto(CommonConstants.STATUS_200, CommonConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(
                    CommonConstants.STATUS_417, CommonConstants.MESSAGE_417_DELETE));
        }
    }

    @Operation(
            summary = "Concluir agendamento",
            description = "REST API para concluir um agendamento, registrando o pagamento e histórico de atendimento"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PatchMapping("/concluirAgendamento")
    public ResponseEntity<ResponseDto> concluirAgendamento(
            @RequestParam @NotNull(message = "ID do agendamento não pode ser nulo") Long agendamentoId,
            @RequestParam @NotEmpty(message = "Forma de pagamento não pode ser vazia") String formaPagamento) {
        agendamentoService.concluirAgendamento(agendamentoId, formaPagamento);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(CommonConstants.STATUS_200, CommonConstants.MESSAGE_200));
    }

}
