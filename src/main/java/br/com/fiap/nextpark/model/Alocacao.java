package br.com.fiap.nextpark.model;

import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Alocacao {

    @Id
    private long id;
    private Moto moto;
    private Vaga vaga;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    

}
