package com.okavango.entity.dto.departamento;

import com.okavango.entity.Departamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DepartamentoMinDTO
{
    private Long id;

    @NotBlank(message = "não pode ser nulo")
    @Size(min = 4, message = "deve ter no mínimo {min} caracteres")
    private String titulo;

    public DepartamentoMinDTO(Departamento departamento)
    {
        this.id = departamento.getId();
        this.titulo = departamento.getTitulo();
    }
}
