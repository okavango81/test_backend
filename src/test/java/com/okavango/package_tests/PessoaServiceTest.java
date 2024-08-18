package com.okavango.package_tests;


import com.okavango.entity.Departamento;
import com.okavango.entity.Pessoa;
import com.okavango.entity.dto.pessoa.PessoaMinDTO;
import com.okavango.entity.dto.pessoa.PessoaTotalHorasGastasDTO;
import com.okavango.exception.DepartmentNotFoundException;
import com.okavango.repository.DepartamentoRepository;
import com.okavango.repository.PessoaRepository;
import com.okavango.service.PessoaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PessoaServiceTest
{
    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private DepartamentoRepository departamentoRepository;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    //*****************salvar pessoa*************************
    @Test
    void novaPessoa_DepartamentoExistente_DeveCriarPessoa()
    {
        // Configuração do mock
        Long departamentoId = 1L;
        Departamento departamentoMock = new Departamento("Departamento Teste");
        departamentoMock.setId(departamentoId); // Certifique-se de que o ID esteja configurado

        Pessoa pessoaMock = new Pessoa("Pessoa Teste", departamentoMock);

        // Criação do DTO a partir da Pessoa
        PessoaMinDTO pessoaMinDTO = new PessoaMinDTO(pessoaMock);

        // Simulação dos repositórios
        when(departamentoRepository.findById(departamentoId)).thenReturn(Optional.of(departamentoMock));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaMock);

        // Execução do método
        ResponseEntity<PessoaMinDTO> response = pessoaService.novaPessoa(pessoaMinDTO);

        // Verificações
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(pessoaMinDTO.getNome(), response.getBody().getNome());
        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
    }

    //************salvar pessoa sem um departamento existente************
    @Test
    void novaPessoa_DepartamentoInexistente_DeveLancarDepartmentNotFoundException()
    {
        // Configuração do mock
        Long departamentoId = 1L;
        Departamento departamentoMock = new Departamento("Departamento Teste");
        Pessoa pessoaMock = new Pessoa("Pessoa Teste", departamentoMock);
        PessoaMinDTO pessoaMinDTO = new PessoaMinDTO(pessoaMock);

        when(departamentoRepository.findById(departamentoId)).thenReturn(Optional.empty());

        // Execução do método e verificação
        assertThrows(DepartmentNotFoundException.class, () -> pessoaService.novaPessoa(pessoaMinDTO));
        verify(pessoaRepository, never()).save(any(Pessoa.class));
    }

    //*************** atualizar pessoa *******************************
    @Test
    void atualizarPessoa_PessoaEDepartamentoExistente_DeveAtualizarPessoa()
    {
        // Configuração do mock
        Long pessoaId = 1L;
        Long departamentoId = 2L;
        Departamento departamentoAntigo = new Departamento("Departamento Antigo");
        departamentoAntigo.setId(departamentoId); // Configura o ID
        Pessoa pessoaMock = new Pessoa("Pessoa Antiga", departamentoAntigo);

        Departamento departamentoNovo = new Departamento("Departamento Novo");
        departamentoNovo.setId(departamentoId); // Configura o ID

        // Criação do DTO a partir da Pessoa
        PessoaMinDTO pessoaMinDTO = new PessoaMinDTO(pessoaMock);
        pessoaMinDTO.setNome("Pessoa Atualizada"); // Atualiza o nome no DTO

        // Simula o retorno dos repositórios
        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.of(pessoaMock));
        when(departamentoRepository.findById(departamentoId)).thenReturn(Optional.of(departamentoNovo));

        // Execução do método
        ResponseEntity<PessoaMinDTO> response = pessoaService.atualizarPessoa(pessoaId, pessoaMinDTO);

        // Verificações
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pessoaMinDTO.getNome(), response.getBody().getNome());
        assertEquals(departamentoId, response.getBody().getIdDepartamento());
        verify(pessoaRepository, times(1)).findById(pessoaId);
        verify(departamentoRepository, times(1)).findById(departamentoId);
    }

    //******************** atualizar pessoa inexistente******************
    @Test
    void atualizarPessoa_PessoaInexistente_DeveLancarNoSuchElementException()
    {
        // Configuração do mock
        Long pessoaId = 1L;
        Long departamentoId = 2L;
        Pessoa pessoaMock = new Pessoa("Pessoa Antiga", new Departamento("Departamento Antigo"));
        PessoaMinDTO pessoaMinDTO = new PessoaMinDTO(pessoaMock);
        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.empty());
        when(departamentoRepository.findById(departamentoId)).thenReturn(Optional.of(new Departamento("Departamento Teste")));

        // Execução do método e verificação
        assertThrows(NoSuchElementException.class, () -> pessoaService.atualizarPessoa(pessoaId, pessoaMinDTO));
        verify(pessoaRepository, times(1)).findById(pessoaId);
        verify(departamentoRepository, never()).save(any(Departamento.class));
    }


    //******************** remover pessoa existente *********************
    @Test
    void removerPessoa_PessoaExistente_DeveRemoverPessoa()
    {
        // Configuração do mock
        Long pessoaId = 1L;
        Departamento departamentoMock = mock(Departamento.class); // Cria um mock para o Departamento
        Pessoa pessoaMock = new Pessoa("Pessoa Teste", departamentoMock);

        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.of(pessoaMock));
        when(departamentoRepository.findById(departamentoMock.getId())).thenReturn(Optional.of(departamentoMock));

        // Execução do método
        ResponseEntity<Void> response = pessoaService.removerPessoa(pessoaId);

        // Verificações
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(pessoaRepository, times(1)).delete(pessoaMock);
        verify(departamentoMock, times(1)).decrementarPessoas(); // Verifica o mock
    }


    //****************** listar pessoas *****************************
    @Test
    void listarPessoas_DeveRetornarListaDePessoas()
    {
        // Configuração do mock
        Departamento departamentoMock = new Departamento("Departamento Teste");
        Pessoa pessoa1 = new Pessoa("Pessoa 1", departamentoMock);
        Pessoa pessoa2 = new Pessoa("Pessoa 2", departamentoMock);

        when(pessoaRepository.findAll()).thenReturn(Arrays.asList(pessoa1, pessoa2));

        // Execução do método
        ResponseEntity<List<PessoaTotalHorasGastasDTO>> response = pessoaService.listarPessoas();

        // Verificações
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }


    //**************** buscar por id pessoa existente ***************
    @Test
    void buscarPorId_PessoaExistente_DeveRetornarPessoa()
    {
        // Configuração do mock
        Long pessoaId = 1L;
        Pessoa pessoaMock = new Pessoa("Pessoa Teste", new Departamento("Departamento Teste"));
        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.of(pessoaMock));

        // Execução do método
        ResponseEntity<Pessoa> response = pessoaService.buscarPorId(pessoaId);

        // Verificações
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pessoaMock, response.getBody());
    }


    //**************** buscar por id pessoa inexistente deve lançar exceção *****************
    @Test
    void buscarPorId_PessoaInexistente_DeveLancarNoSuchElementException()
    {
        // Configuração do mock
        Long pessoaId = 1L;
        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.empty());

        // Execução do método e verificação
        assertThrows(NoSuchElementException.class, () -> pessoaService.buscarPorId(pessoaId));
    }

}

