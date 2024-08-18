package com.okavango.entity.dto.tarefa;

import com.okavango.entity.Tarefa;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TarefaMinDTO
{
    private Long id;

    @NotBlank(message = "não pode ser nulo")
    @Size(min = 4, message = "deve ter no mínimo {min} caracteres")
    private String titulo;

    @NotBlank(message = "não pode ser nulo")
    @Size(min = 10, message = "deve ter no mínimo {min} caracteres")
    private String descricao;

    private LocalDate prazo;

    //@NotBlank(message = "não pode ser nulo")
    private Long idDepartamento;

    @NotNull(message = "não pode ser nulo")
    @Min(value = 1, message = "deve ser maior que 0 (zero)")
    private int duracao;

    private Long idPessoa;

    private boolean finalizada;

    public TarefaMinDTO(Tarefa tarefa)
    {
        this.id = tarefa.getId();
        this.titulo = tarefa.getTitulo();
        this.descricao = tarefa.getDescricao();
        this.prazo = tarefa.getPrazo();
        this.idDepartamento = tarefa.getDepartamentoAssociado().getId();
        this.duracao = tarefa.getDuracao();
        this.idPessoa = tarefa.getResponsavel() != null ? tarefa.getResponsavel().getId() : null;
        this.finalizada = tarefa.isFinalizada();
    }

}
