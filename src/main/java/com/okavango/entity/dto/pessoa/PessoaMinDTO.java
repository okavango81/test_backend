package com.okavango.entity.dto.pessoa;

import com.okavango.entity.Pessoa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
public class PessoaMinDTO
{
    private Long id;

    @NotBlank(message = "não pode ser nulo")
    @Size(min = 3, message = "deve ter no mínimo {min} caracteres")
    private String nome;

    private Long idDepartamento;

    public PessoaMinDTO(Pessoa pessoa)
    {
        this.id = pessoa.getId();
        this.nome = pessoa.getNome();
        this.idDepartamento = pessoa.getDepartamento().getId();
    }
}
