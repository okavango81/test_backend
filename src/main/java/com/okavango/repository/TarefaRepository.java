package com.okavango.repository;

import com.okavango.entity.Tarefa;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long>
{
    
    // H2
//    @Query("SELECT t FROM Tarefa t WHERE t.responsavel IS NULL ORDER BY t.prazo ASC")
//    List<Tarefa> findTop3ByResponsavelIsNullOrderByPrazoAsc(Pageable pageable);

    // PostgreSQL
    @Query(value = "SELECT t FROM Tarefa t WHERE t.responsavel IS NULL ORDER BY t.prazo ASC")
    List<Tarefa> findTop3ByResponsavelIsNullOrderByPrazoAsc(Pageable pageable);

}
