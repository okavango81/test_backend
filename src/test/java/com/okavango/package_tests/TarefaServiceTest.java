package com.okavango.package_tests;

import com.okavango.entity.Departamento;
import com.okavango.entity.Pessoa;
import com.okavango.entity.Tarefa;
import com.okavango.entity.dto.pessoa.PessoaMinDTO;
import com.okavango.entity.dto.tarefa.TarefaMinDTO;
import com.okavango.exception.DepartmentNotFoundException;
import com.okavango.exception.DifferentDepartmentsException;
import com.okavango.repository.DepartamentoRepository;
import com.okavango.repository.PessoaRepository;
import com.okavango.repository.TarefaRepository;
import com.okavango.service.TarefaService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TarefaServiceTest {

    @InjectMocks
    private TarefaService tarefaService;

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private DepartamentoRepository departamentoRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    public TarefaServiceTest() {
        MockitoAnnotations.openMocks(this);
    }


    //******************* nova tarefa com departamento existente ****************
    @Test
    void novaTarefa_DepartamentoExistente_DeveCriarTarefa() {

        Long departamentoId = 1L;
        // Crie um mock para Departamento
        Departamento departamentoMock = mock(Departamento.class);
        when(departamentoMock.getId()).thenReturn(departamentoId);

        Tarefa tarefaMock = new Tarefa("Nova tarefa", "Descrição detalhada", LocalDate.of(2024, 10, 10), 10, departamentoMock);

        // Criar um DTO
        TarefaMinDTO tarefaDTO = new TarefaMinDTO(tarefaMock);

        // Configurar o comportamento dos mocks
        when(departamentoRepository.findById(departamentoId)).thenReturn(Optional.of(departamentoMock));
        when(tarefaRepository.save(any(Tarefa.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        ResponseEntity<TarefaMinDTO> response = tarefaService.novaTarefa(tarefaDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(tarefaDTO.getTitulo(), response.getBody().getTitulo());
        assertEquals(tarefaDTO.getDescricao(), response.getBody().getDescricao());

        // Verifique se o método incrementarTarefas() foi chamado
        verify(departamentoMock, times(1)).incrementarTarefas();
        verify(tarefaRepository, times(1)).save(any(Tarefa.class));
    }

    //********* nova tarefa com departamento inexistente deve lançar exceção ****************
    @Test
    void novaTarefa_DepartamentoNaoExistente_DeveLancarException() {
        Long departamentoId = 1L;
        Departamento departamentoMock = new Departamento("Departamento Teste");
        Tarefa tarefaMock = new Tarefa("Nova tarefa", "Descrição detalhada", LocalDate.of(2024, 10, 10), 10, departamentoMock);
        TarefaMinDTO tarefaDTO = new TarefaMinDTO(tarefaMock);

        when(departamentoRepository.findById(departamentoId)).thenReturn(Optional.empty());

        // Execução do método e verificação
        assertThrows(DepartmentNotFoundException.class, () -> tarefaService.novaTarefa(tarefaDTO));
        verify(pessoaRepository, never()).save(any(Pessoa.class));
    }

    //*********** alocar responsável na tarefa de mesmo departamento ************************
    @Test
    void alocarResponsavel_TarefaEPessoaExistemNoMesmoDepartamento_DeveAlocarResponsavel() {
        Long tarefaId = 1L;
        Long pessoaId = 2L;
        Long departamentoId = 3L;  // Adicione uma ID para o Departamento

        Departamento departamento = new Departamento("Departamento Teste");
        departamento.setId(departamentoId); // Defina o ID do Departamento

        Tarefa tarefa = new Tarefa("Nova tarefa", "Descrição detalhada", LocalDate.of(2024, 10, 10), 10, departamento);
        tarefa.setId(tarefaId);

        Pessoa pessoa = new Pessoa("Responsável", departamento);
        pessoa.setId(pessoaId);

        TarefaMinDTO tarefaDTO = new TarefaMinDTO();
        tarefaDTO.setIdPessoa(pessoaId);

        when(tarefaRepository.findById(tarefaId)).thenReturn(Optional.of(tarefa));
        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.of(pessoa));
        when(tarefaRepository.save(any(Tarefa.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        ResponseEntity<TarefaMinDTO> response = tarefaService.alocarResponsavel(tarefaDTO, tarefaId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pessoaId, response.getBody().getIdPessoa());

        verify(tarefaRepository, times(1)).save(tarefa);
        verify(pessoaRepository, times(1)).save(pessoa);
    }


    // ************** alocar responsável em tarefa não encontrada **********************
    @Test
    void alocarResponsavel_TarefaNaoEncontrada_DeveLancarNoSuchElementException() {
        Long tarefaId = 1L;
        Long pessoaId = 2L;

        Pessoa pessoa = new Pessoa("Responsável", new Departamento("Departamento Teste"));
        pessoa.setId(pessoaId);

        TarefaMinDTO tarefaDTO = new TarefaMinDTO();
        tarefaDTO.setIdPessoa(pessoaId);

        // Configuração dos mocks
        when(tarefaRepository.findById(tarefaId)).thenReturn(Optional.empty());
        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.of(pessoa));

        // Execução e verificação da exceção
        assertThrows(NoSuchElementException.class, () -> tarefaService.alocarResponsavel(tarefaDTO, tarefaId));

        // Verifique interações esperadas
        verify(tarefaRepository, times(1)).findById(tarefaId);
        verify(pessoaRepository, times(1)).findById(pessoaId);
    }


    //********** pessoa e tarefa com departamentos diferentes deve lançar exceção **********************
    @Test
    void alocarResponsavel_TarefaEPessoaEstaoEmDepartamentosDiferentes_DeveLancarDifferentDepartmentsException() {
        Long tarefaId = 1L;
        Long pessoaId = 2L;
        Long departamentoTarefaId = 3L;
        Long departamentoPessoaId = 4L;

        Departamento departamentoTarefa = new Departamento("Departamento Tarefa");
        departamentoTarefa.setId(departamentoTarefaId);

        Departamento departamentoPessoa = new Departamento("Departamento Pessoa");
        departamentoPessoa.setId(departamentoPessoaId);

        Tarefa tarefa = new Tarefa("Nova tarefa", "Descrição detalhada", LocalDate.of(2024, 10, 10), 10, departamentoTarefa);
        tarefa.setId(tarefaId);

        Pessoa pessoa = new Pessoa("Responsável", departamentoPessoa);
        pessoa.setId(pessoaId);

        TarefaMinDTO tarefaDTO = new TarefaMinDTO();
        tarefaDTO.setIdPessoa(pessoaId);

        when(tarefaRepository.findById(tarefaId)).thenReturn(Optional.of(tarefa));
        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.of(pessoa));

        assertThrows(DifferentDepartmentsException.class, () -> tarefaService.alocarResponsavel(tarefaDTO, tarefaId));

        verify(tarefaRepository, times(1)).findById(tarefaId);
        verify(pessoaRepository, times(1)).findById(pessoaId);
    }


    //************** finalizar tarefa existente deve finalizar tarefa ***************
    @Test
    void finalizarTarefa_TarefaExistente_DeveFinalizarTarefa() {
        Long tarefaId = 1L;

        // Criar um objeto de tarefa com o ID
        Tarefa tarefa = new Tarefa("Nova tarefa", "Descrição detalhada", LocalDate.of(2024, 10, 10), 10, new Departamento("Departamento Teste"));
        tarefa.setId(tarefaId);

        // Criar um DTO para a tarefa
        TarefaMinDTO tarefaDTO = new TarefaMinDTO();
        tarefaDTO.setFinalizada(true);

        // Configurar o mock para retornar a tarefa quando buscar pelo ID
        when(tarefaRepository.findById(tarefaId)).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.save(any(Tarefa.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        // Chamar o método
        ResponseEntity<TarefaMinDTO> response = tarefaService.finalizarTarefa(tarefaDTO, tarefaId);

        // Verificar o status e o conteúdo da resposta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isFinalizada());

        // Verificar interações com os mocks
        verify(tarefaRepository, times(1)).findById(tarefaId);
        verify(tarefaRepository, times(1)).save(tarefa);
    }


    //************ finalizar tarefa não encontrada deve lançar exceção ***************
    @Test
    void finalizarTarefa_TarefaNaoEncontrada_DeveLancarNoSuchElementException() {
        Long tarefaId = 1L;

        // Criar um DTO para a tarefa
        TarefaMinDTO tarefaDTO = new TarefaMinDTO();
        tarefaDTO.setFinalizada(true);

        // Configurar o mock para retornar Optional.empty() quando buscar pelo ID
        when(tarefaRepository.findById(tarefaId)).thenReturn(Optional.empty());

        // Verificar se a exceção é lançada
        assertThrows(NoSuchElementException.class, () -> tarefaService.finalizarTarefa(tarefaDTO, tarefaId));

        // Verificar interações com os mocks
        verify(tarefaRepository, times(1)).findById(tarefaId);
        verifyNoMoreInteractions(tarefaRepository);
    }


    //************* retornar as tarefas mais antigas sem um responsável ******************
    @Test
    void top3TarefasSemResponsavel_DeveRetornarTop3TarefasSemResponsavel() {
        // Criar uma lista de tarefas sem responsável
        Long departamentoId = 1L;

        Tarefa tarefa1 = new Tarefa("Tarefa 1", "Descrição 1", LocalDate.of(2024, 9, 1), 5, new Departamento("Departamento Teste"));
        tarefa1.setId(1L);

        Tarefa tarefa2 = new Tarefa("Tarefa 2", "Descrição 2", LocalDate.of(2024, 10, 1), 10, new Departamento("Departamento Teste"));
        tarefa2.setId(2L);

        Tarefa tarefa3 = new Tarefa("Tarefa 3", "Descrição 3", LocalDate.of(2024, 11, 1), 15, new Departamento("Departamento Teste"));
        tarefa3.setId(3L);

        List<Tarefa> tarefasSemResponsavel = Arrays.asList(tarefa1, tarefa2, tarefa3);

        // Configurar o mock para retornar as tarefas quando o método é chamado
        when(tarefaRepository.findTop3ByResponsavelIsNullOrderByPrazoAsc(any(Pageable.class))).thenReturn(tarefasSemResponsavel);

        // Chamar o método
        List<Tarefa> result = tarefaService.top3TarefasSemResponsavel();

        // Verificar se o resultado é o esperado
        assertEquals(3, result.size());
        assertEquals(tarefa1, result.get(0));
        assertEquals(tarefa2, result.get(1));
        assertEquals(tarefa3, result.get(2));

        // Verificar interações com o mock
        verify(tarefaRepository, times(1)).findTop3ByResponsavelIsNullOrderByPrazoAsc(any(Pageable.class));
    }





}

