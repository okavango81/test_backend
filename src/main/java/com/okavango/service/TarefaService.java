package com.okavango.service;

import com.okavango.entity.Departamento;
import com.okavango.entity.Pessoa;
import com.okavango.entity.Tarefa;
import com.okavango.entity.dto.tarefa.TarefaMinDTO;
import com.okavango.exception.DepartmentNotFoundException;
import com.okavango.exception.DifferentDepartmentsException;
import com.okavango.exception.PersonNotFoundException;
import com.okavango.repository.DepartamentoRepository;
import com.okavango.repository.PessoaRepository;
import com.okavango.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TarefaService
{
    private final TarefaRepository tarefaRepository;
    private final DepartamentoRepository departamentoRepository;
    private final PessoaRepository pessoaRepository;

    // salvar tarefa
    @Transactional
    public ResponseEntity<TarefaMinDTO> novaTarefa(TarefaMinDTO t)
    {
        Optional<Departamento> departamento = departamentoRepository.findById(t.getIdDepartamento());

        if (departamento.isPresent())
        {
            // incrementa a tarefa no total de departamento
            departamento.get().incrementarTarefas();

            Tarefa tarefa = new Tarefa(t.getTitulo(), t.getDescricao(), t.getPrazo(), t.getDuracao(), departamento.get());
            tarefaRepository.save(tarefa);
            return ResponseEntity.status(HttpStatus.CREATED).body(new TarefaMinDTO(tarefa));
        } else
        {
            throw new DepartmentNotFoundException();
        }
    }


    // alocar pessoa na tarefa
    @Transactional
    public ResponseEntity<TarefaMinDTO> alocarResponsavel(TarefaMinDTO tarefaMinDTO, Long id)
    {
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(tarefaMinDTO.getIdPessoa());

        if (tarefaOptional.isPresent() && pessoaOptional.isPresent())
        {
            Tarefa tarefa = tarefaOptional.get();
            Pessoa pessoa = pessoaOptional.get();

            if (pessoa.getDepartamento().getId().equals(tarefa.getDepartamentoAssociado().getId()))
            {
                tarefa.setResponsavel(pessoa);
                pessoa.adicionarHoras(tarefa.getDuracao());
                pessoa.adicionarTarefas();
                pessoa.mediaHoras();

                tarefaRepository.save(tarefa);
                pessoaRepository.save(pessoa);
                return ResponseEntity.status(HttpStatus.OK).body(new TarefaMinDTO(tarefa));

            } else
            {
                throw new DifferentDepartmentsException();
            }
        } else
        {
            if (!tarefaOptional.isPresent())
            {
                throw new NoSuchElementException();

            } else
            {
                throw new PersonNotFoundException();
            }
        }
    }


    @Transactional
    public ResponseEntity<TarefaMinDTO> finalizarTarefa(TarefaMinDTO tarefaMinDTO, Long id)
    {
        // Buscar a tarefa
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);

        if (tarefaOptional.isPresent())
        {
            Tarefa tarefa = tarefaOptional.get();
            tarefa.setFinalizada(tarefaMinDTO.isFinalizada());
            tarefaRepository.save(tarefa);
            return ResponseEntity.status(HttpStatus.OK).body(new TarefaMinDTO(tarefa));

        } else
        {
            throw new NoSuchElementException();
        }
    }


    public List<Tarefa> top3TarefasSemResponsavel() {
        //Pageable pageable = PageRequest.of(0, 3);
        Pageable pageable = PageRequest.of(0, 3, Sort.by("prazo").ascending());
        return tarefaRepository.findTop3ByResponsavelIsNullOrderByPrazoAsc(pageable);
    }//*********

}
