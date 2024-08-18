package com.okavango.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Departamento
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private int totalPessoas;
    private int totalTarefas;

    @OneToMany(mappedBy = "departamento")
    @JsonIgnore
    private List<Pessoa> composto = new ArrayList<>();

    @OneToMany(mappedBy = "departamentoAssociado")
    @JsonIgnore
    private List<Tarefa> tarefasAssociadas = new ArrayList<>();

    public Departamento(String titulo){
        this.titulo = titulo;
    }

    // incrementar total de pessoas
    public void incrementarPessoas()
    {
        this.totalPessoas++;
    }
    // decrementar total de pessoas
    public void decrementarPessoas(){
        this.totalPessoas--;
    }

    // incrementar tarefas
    public void incrementarTarefas(){
        this.totalTarefas++;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Departamento that = (Departamento) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(id);
    }
}
