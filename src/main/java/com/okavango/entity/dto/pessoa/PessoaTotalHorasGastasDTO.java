package com.okavango.entity.dto.pessoa;

import com.okavango.entity.Departamento;
import com.okavango.entity.Pessoa;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaTotalHorasGastasDTO
{
    private Long id;
    private String nome;
    private Departamento departamento;
    private int totalHorasGastasNasTarefas;

    public PessoaTotalHorasGastasDTO(Pessoa pessoa)
    {
        this.id = pessoa.getId();
        this.nome = pessoa.getNome();
        this.departamento = pessoa.getDepartamento();
        this.totalHorasGastasNasTarefas = pessoa.getHorasAtribuidas();
    }
}
