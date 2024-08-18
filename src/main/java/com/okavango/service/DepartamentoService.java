package com.okavango.service;

import com.okavango.entity.Departamento;
import com.okavango.entity.dto.departamento.DepartamentoMinDTO;
import com.okavango.repository.DepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartamentoService
{
    private final DepartamentoRepository departamentoRepository;

    // adicionar departamento
    @Transactional
    public ResponseEntity<DepartamentoMinDTO> novoDepartamento(DepartamentoMinDTO departamentoMinDTO){
        Departamento departamento = new Departamento(departamentoMinDTO.getTitulo());
        departamentoRepository.save(departamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DepartamentoMinDTO(departamento));
    }

    // listar todos
    public ResponseEntity<List<Departamento>> departamentos(){
        return ResponseEntity.status(HttpStatus.OK).body(departamentoRepository.findAll());
    }

    // buscar departamento por id
    @Transactional(readOnly = true)
    public ResponseEntity<DepartamentoMinDTO> buscarPorId(Long id)
    {
        Optional<Departamento> departamento = departamentoRepository.findById(id);
        return ResponseEntity.ok().body(new DepartamentoMinDTO(departamento.get()));
    }
}
