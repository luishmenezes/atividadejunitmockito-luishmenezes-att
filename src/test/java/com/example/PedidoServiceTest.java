package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

public class PedidoServiceTest {
    @Mock
    private PedidoRepository pedidoRepo;

    @Mock
    private ClienteRepository clienteRepo;

    private PedidoService pedidoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        pedidoService = new PedidoService(pedidoRepo, clienteRepo);
    }

    @Test
    void criarPedido_comSucesso() {
        String cpf = "12345678900";
        String produto = "Produto A";
        int quantidade = 5;
        double precoUnitario = 10.0;

        Cliente cliente = new Cliente(cpf, "Fulano", "fulano@email.com");
        // Usamos Answer para capturar o Pedido salvo, já que o id é random
        when(clienteRepo.buscarPorCpf(cpf)).thenReturn(Optional.of(cliente));
        when(pedidoRepo.salvar(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pedido resultado = pedidoService.criarPedido(cpf, produto, quantidade, precoUnitario);

        assertNotNull(resultado);
        assertEquals(produto, resultado.getProduto());
        assertEquals(quantidade, resultado.getQuantidade());
        assertEquals(precoUnitario, resultado.getPrecoUnitario());

        verify(clienteRepo).buscarPorCpf(cpf);
        verify(pedidoRepo).salvar(any(Pedido.class));
    }

    @Test
    void criarPedido_comQuantidadeInvalida_deveLancarExcecao() {
        String cpf = "12345678900";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            pedidoService.criarPedido(cpf, "Produto A", 0, 10.0);
        });

        assertEquals("Valores inválidos", thrown.getMessage());

        verify(clienteRepo, never()).buscarPorCpf(anyString());
        verify(pedidoRepo, never()).salvar(any());
    }

    @Test
    void criarPedido_comPrecoUnitarioInvalido_deveLancarExcecao() {
        String cpf = "12345678900";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            pedidoService.criarPedido(cpf, "Produto A", 2, 0);
        });

        assertEquals("Valores inválidos", thrown.getMessage());

        verify(clienteRepo, never()).buscarPorCpf(anyString());
        verify(pedidoRepo, never()).salvar(any());
    }

    @Test
    void criarPedido_comClienteInexistente_deveLancarExcecao() {
        String cpf = "99999999999";

        when(clienteRepo.buscarPorCpf(cpf)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            pedidoService.criarPedido(cpf, "Produto A", 3, 10.0);
        });

        assertEquals("Cliente não encontrado", thrown.getMessage());

        verify(clienteRepo).buscarPorCpf(cpf);
        verify(pedidoRepo, never()).salvar(any());
    }

    @Test
    void listarPedidosCliente_comSucesso() {
        String cpf = "12345678900";
        List<Pedido> pedidos = List.of(
                new Pedido(1, "Produto A", 2, 15.0),
                new Pedido(2, "Produto B", 1, 25.0)
        );

        when(pedidoRepo.listarPedidosPorCliente(cpf)).thenReturn(pedidos);

        List<Pedido> resultado = pedidoService.listarPedidosCliente(cpf);

        assertEquals(2, resultado.size());
        verify(pedidoRepo).listarPedidosPorCliente(cpf);
    }

    @Test
    void cancelarPedido_comSucesso() {
        int id = 1;
        when(pedidoRepo.removerPedido(id)).thenReturn(true);

        boolean resultado = pedidoService.cancelarPedido(id);

        assertTrue(resultado);
        verify(pedidoRepo).removerPedido(id);
    }

    @Test
    void cancelarPedido_inexistente_deveRetornarFalse() {
        int id = 99;
        when(pedidoRepo.removerPedido(id)).thenReturn(false);

        boolean resultado = pedidoService.cancelarPedido(id);

        assertFalse(resultado);
        verify(pedidoRepo).removerPedido(id);
    }

    @Test
    void cadastrarCliente_comSucesso() {
        String cpf = "12345678900";
        String nome = "Fulano";
        String email = "fulano@email.com";
        Cliente cliente = new Cliente(cpf, nome, email);

        when(clienteRepo.salvar(any(Cliente.class))).thenReturn(cliente);

        Cliente resultado = pedidoService.cadastrarCliente(cpf, nome, email);

        assertNotNull(resultado);
        assertEquals(cpf, resultado.getCpf());
        assertEquals(nome, resultado.getNome());
        assertEquals(email, resultado.getEmail());

        verify(clienteRepo).salvar(any(Cliente.class));
    }

    @Test
    void criarPedido_calculoTotalCorreto() {
        String cpf = "12345678900";
        String produto = "Monitor";
        int quantidade = 2;
        double precoUnitario = 500.0;

        Cliente cliente = new Cliente(cpf, "João", "joao@email.com");
        when(clienteRepo.buscarPorCpf(cpf)).thenReturn(Optional.of(cliente));
        when(pedidoRepo.salvar(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pedido pedido = pedidoService.criarPedido(cpf, produto, quantidade, precoUnitario);


        assertEquals(1000.0, pedido.calcularTotal(), 0.0001);

        verify(clienteRepo).buscarPorCpf(cpf);
        verify(pedidoRepo).salvar(any(Pedido.class));
    }
}