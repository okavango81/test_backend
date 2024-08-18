package com.okavango.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tarefa
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String titulo;

    @Lob
    private String descricao;

    private LocalDate prazo;

    @ManyToOne
    @JoinColumn(name = "idDepartamento")
    private Departamento departamentoAssociado = new Departamento();

    private int duracao;

    @ManyToOne
    @JoinColumn(name = "idPessoa")
    private Pessoa responsavel;

    private boolean finalizada = false;

    public Tarefa(String titulo, String descricao, LocalDate prazo, int duracao, Departamento departamento)
    {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prazo = prazo;
        this.duracao = duracao;
        this.departamentoAssociado.setId(departamento.getId());
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarefa tarefa = (Tarefa) o;
        return Objects.equals(id, tarefa.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(id);
    }
}
