package com.ifma.barbearia.service;

import com.ifma.barbearia.dto.ResumoFinanceiroDto;

import java.time.LocalDate;

public interface IFinanceiroService {

    ResumoFinanceiroDto obterResumo(LocalDate inicio, LocalDate fim);

}
