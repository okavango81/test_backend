package com.okavango.controller;

import com.okavango.entity.Tarefa;
import com.okavango.entity.dto.tarefa.TarefaMinDTO;
import com.okavango.service.TarefaService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TarefaController
{
    private final TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefaMinDTO> salvarTarefa(@Valid @RequestBody TarefaMinDTO tarefaMinDTO)
    {
        return tarefaService.novaTarefa(tarefaMinDTO);
    }

    @PutMapping("/alocar/{id}")
    public ResponseEntity<TarefaMinDTO> alocarResponsavel(@RequestBody TarefaMinDTO tarefaMinDTO, @PathVariable Long id)
    {
        return tarefaService.alocarResponsavel(tarefaMinDTO, id);
    }

    @PutMapping("/finalizar/{id}")
    public ResponseEntity<TarefaMinDTO> finalizar(@RequestBody TarefaMinDTO tarefaMinDTO, @PathVariable Long id)
    {
        return tarefaService.finalizarTarefa(tarefaMinDTO, id);
    }

    @GetMapping("/pendentes")
    public List<Tarefa> tarefasSemResponsavel(){
        return tarefaService.top3TarefasSemResponsavel();
    }

}
