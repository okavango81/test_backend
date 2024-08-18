package com.okavango.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Pessoa
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "idDepartamento")
    private Departamento departamento = new Departamento();

    @OneToMany(mappedBy = "responsavel")
    @JsonIgnore
    private List<Tarefa> tarefasAtribuidas = new ArrayList<>();

    @Column(name = "total_horas_tarefas")
    private int horasAtribuidas;

    @Column(name = "total_tarefas")
    private int quantidadeDeTarefas;

    @Column(name = "media_horas_gastas")
    private int mediaDeHoras;


    public Pessoa(String nome, Departamento departamento)
    {
        this.nome = nome;
        this.departamento = departamento;
    }

    // soma as horas a cada tarefa atribuída
    public void adicionarHoras(int horas){
       horasAtribuidas += horas;
    }

    // soma as tarefas atribuídas
    public void adicionarTarefas(){
        quantidadeDeTarefas++;
    }

    // atribui a média de horas gastas
    public void mediaHoras(){
        setMediaDeHoras(horasAtribuidas / quantidadeDeTarefas);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pessoa pessoa = (Pessoa) o;
        return Objects.equals(id, pessoa.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(id);
    }
}
