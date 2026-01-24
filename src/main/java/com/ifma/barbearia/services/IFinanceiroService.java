package com.ifma.barbearia.services;

import com.ifma.barbearia.DTOs.ResumoFinanceiroDto;

import java.time.LocalDate;

public interface IFinanceiroService {

    ResumoFinanceiroDto obterResumo(LocalDate inicio, LocalDate fim);

}
