package com.okavango.service;

import com.okavango.entity.Departamento;
import com.okavango.entity.Pessoa;
import com.okavango.entity.dto.pessoa.PessoaMinDTO;
import com.okavango.entity.dto.pessoa.PessoaTotalHorasGastasDTO;
import com.okavango.exception.DepartmentNotFoundException;
import com.okavango.repository.DepartamentoRepository;
import com.okavango.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PessoaService
{
    private final PessoaRepository pessoaRepository;
    private final DepartamentoRepository departamentoRepository;

    // adicionar pessoa
    @Transactional
    public ResponseEntity<PessoaMinDTO> novaPessoa(PessoaMinDTO pessoaMinDTO)
    {
        Optional<Departamento> departamento = departamentoRepository.findById(pessoaMinDTO.getIdDepartamento());

        if (departamento.isPresent())
        {
            Pessoa pessoa = new Pessoa(pessoaMinDTO.getNome(), departamento.get());
            departamento.get().incrementarPessoas();
            pessoaRepository.save(pessoa);
            return ResponseEntity.status(HttpStatus.CREATED).body(new PessoaMinDTO(pessoa));
        } else
        {
            throw new DepartmentNotFoundException();
        }
    } //***

    // alterar pessoa
    @Transactional
    public ResponseEntity<PessoaMinDTO> atualizarPessoa(Long id, PessoaMinDTO pessoaMinDTO)
    {
        Optional<Departamento> departamento = departamentoRepository.findById(pessoaMinDTO.getIdDepartamento());
        Optional<Pessoa> pessoa = pessoaRepository.findById(id);

        if (pessoa.isPresent())
        {
            if (departamento.isPresent())
            {
                pessoa.get().setNome(pessoaMinDTO.getNome());
                pessoa.get().setDepartamento(departamento.get());
                return ResponseEntity.ok().body(new PessoaMinDTO(pessoa.get()));

            } else
            {
                throw new DepartmentNotFoundException();
            }

        } else
        {
            throw new NoSuchElementException();
        }

    } //***

    // remover pessoa
    @Transactional
    public ResponseEntity<Void> removerPessoa(Long id)
    {
        Optional<Pessoa> pessoa = pessoaRepository.findById(id);
        Optional<Departamento> departamento = departamentoRepository.findById(pessoa.get().getDepartamento().getId());

        if (pessoa.isPresent())
        {
            departamento.get().decrementarPessoas();
            pessoaRepository.delete(pessoa.get());
            return ResponseEntity.noContent().build();
        } else
        {
            throw new NoSuchElementException();
        }

    } //***


    // listar todas as pessoas
    public ResponseEntity<List<PessoaTotalHorasGastasDTO>> listarPessoas()
    {
        return ResponseEntity.ok().body(pessoaRepository.findAll().stream().map(PessoaTotalHorasGastasDTO::new).collect(Collectors.toList()));
    } //***


    // pesquisar
    @Transactional(readOnly = true)
    public ResponseEntity<List<Pessoa>> search(String param)
    {
        return ResponseEntity.ok().body(pessoaRepository.findByAnyCriteria(param));
    } //***


    // buscar pessoa por id
    @Transactional(readOnly = true)
    public ResponseEntity<Pessoa> buscarPorId(Long id)
    {
        Optional<Pessoa> pessoa = pessoaRepository.findById(id);
        return ResponseEntity.ok().body(pessoa.get());
    } //***

}
