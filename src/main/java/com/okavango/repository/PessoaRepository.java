package com.okavango.repository;

import com.okavango.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>
{

   /* H2
    @Query("select p from Pessoa p where " +
            "(lower(p.nome) like concat('%', lower(:param), '%') or " +
            "exists (select t from Tarefa t where t.responsavel = p and " +
            "str(t.prazo) like concat('%', :param, '%')))")
    List<Pessoa> findByAnyCriteria(String param);
  */  
     //PostgreSQL
    @Query("select p from Pessoa p where " +
            "(lower(p.nome) like concat('%', lower(:param), '%') or " +
            "exists (select t from Tarefa t where t.responsavel = p and " +
            "to_char(t.prazo, 'YYYY-MM-DD') like concat('%', :param, '%')))")
    List<Pessoa> findByAnyCriteria(String param);
}
