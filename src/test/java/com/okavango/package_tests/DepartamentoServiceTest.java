package com.okavango.package_tests;

import com.okavango.entity.Departamento;
import com.okavango.entity.dto.departamento.DepartamentoMinDTO;
import com.okavango.repository.DepartamentoRepository;
import com.okavango.service.DepartamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DepartamentoServiceTest
{

    @Mock //Cria um mock para DepartamentoRepository
    private DepartamentoRepository departamentoRepository;

    @InjectMocks //Cria uma instância de DepartamentoService e injeta os mocks necessários
    private DepartamentoService departamentoService;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    //******************** salvar departamento ********************************
    @Test
    void testNovoDepartamento()
    {

        // Configuração do mock
        Departamento departamentoMock = new Departamento("Departamento Teste");
        DepartamentoMinDTO departamentoMinDTO = new DepartamentoMinDTO(departamentoMock);

        //Define o comportamento esperado para o mock
        when(departamentoRepository.save(any(Departamento.class))).thenReturn(departamentoMock);

        // Execução do método
        ResponseEntity<DepartamentoMinDTO> response = departamentoService.novoDepartamento(departamentoMinDTO);

        // Verificações
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(departamentoMinDTO.getTitulo(), response.getBody().getTitulo());

        // Verifica se o método save foi chamado uma vez
        verify(departamentoRepository, times(1)).save(any(Departamento.class));
    }


    //************** buscar por id inexistente deve lançar exceção ************
    @Test
    void buscarPorId_DepartamentoInexistente_DeveLancarNoSuchElementException()
    {
        // Configuração do mock
        Long pessoaId = 1L;
        when(departamentoRepository.findById(0L)).thenReturn(Optional.empty());

        // Execução do método e verificação
        assertThrows(NoSuchElementException.class, () -> departamentoService.buscarPorId(pessoaId));
    }


    //******************** buscar por id existente **************************
    @Test
    void testBuscarPorIdExistente_DeveRetornarDepartamento()
    {

        // Configuração do mock
        Long departamentoId = 1L;
        Departamento departamentoMock = new Departamento("Departamento Teste");

        //Define o comportamento esperado para o mock
        when(departamentoRepository.findById(departamentoId)).thenReturn(Optional.of(departamentoMock));

        // Execução do método
        ResponseEntity<DepartamentoMinDTO> response = departamentoService.buscarPorId(departamentoId);

        // Verificações
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(departamentoMock.getTitulo(), response.getBody().getTitulo());

        //Verifica se o método findById foi chamado uma vez com o ID correto
        verify(departamentoRepository, times(1)).findById(departamentoId);
    }

}

