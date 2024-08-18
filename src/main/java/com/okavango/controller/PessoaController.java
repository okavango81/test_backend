package com.okavango.controller;

import com.okavango.entity.Pessoa;
import com.okavango.entity.dto.pessoa.PessoaMinDTO;
import com.okavango.entity.dto.pessoa.PessoaTotalHorasGastasDTO;
import com.okavango.service.PessoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pessoas")
@RequiredArgsConstructor
public class PessoaController
{
    private final PessoaService pessoaService;

    @PostMapping
    public ResponseEntity<PessoaMinDTO> registrar(@Valid @RequestBody PessoaMinDTO pessoaMinDTO)
    {
        return pessoaService.novaPessoa(pessoaMinDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaMinDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PessoaMinDTO pessoaMinDTO)
    {
        return pessoaService.atualizarPessoa(id, pessoaMinDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id)
    {
        return pessoaService.removerPessoa(id);
    }

    @GetMapping
    public ResponseEntity<List<PessoaTotalHorasGastasDTO>> listar()
    {
        return pessoaService.listarPessoas();
    }

    @GetMapping("/gastos")
    public ResponseEntity<List<Pessoa>> pesquisar(String param)
    {
        return pessoaService.search(param);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> buscar(@PathVariable Long id)
    {
        return pessoaService.buscarPorId(id);
    }


}
