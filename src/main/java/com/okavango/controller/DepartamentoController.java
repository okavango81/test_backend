package com.okavango.controller;

import com.okavango.entity.Departamento;
import com.okavango.entity.dto.departamento.DepartamentoMinDTO;
import com.okavango.service.DepartamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departamentos")
@RequiredArgsConstructor
public class DepartamentoController
{
    private final DepartamentoService departamentoService;

    @PostMapping
    public ResponseEntity<DepartamentoMinDTO> salvarDepartamento(@Valid @RequestBody DepartamentoMinDTO departamentoMinDTO)
    {
        return departamentoService.novoDepartamento(departamentoMinDTO);
    }

    @GetMapping
    public ResponseEntity<List<Departamento>> listarDepartamento()
    {
        return departamentoService.departamentos();
    }
}
