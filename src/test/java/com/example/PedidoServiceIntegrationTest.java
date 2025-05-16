package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PedidoServiceIntegrationTest {
    @Mock
    private ClienteRepository clienteRepo;

    @Mock
    private PedidoRepository pedidoRepo;

    private PedidoService pedidoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        pedidoService = new PedidoService(pedidoRepo, clienteRepo);
    }

    @Test
    void criarPedido_verificaInteracoesRepositorios() {
        String cpf = "12345678900";
        String produto = "Teclado";
        int quantidade = 3;
        double precoUnitario = 150.0;

        Cliente cliente = new Cliente(cpf, "Maria", "maria@email.com");

        when(clienteRepo.buscarPorCpf(cpf)).thenReturn(Optional.of(cliente));
        when(pedidoRepo.salvar(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pedido pedidoCriado = pedidoService.criarPedido(cpf, produto, quantidade, precoUnitario);


        verify(clienteRepo, times(1)).buscarPorCpf(cpf);


        verify(pedidoRepo, times(1)).salvar(any(Pedido.class));

        // Valida os dados do pedido criado
        assertEquals(produto, pedidoCriado.getProduto());
        assertEquals(quantidade, pedidoCriado.getQuantidade());
        assertEquals(precoUnitario, pedidoCriado.getPrecoUnitario());
    }

    @Test
    void cancelarPedido_verificaChamadaRemoverPedido() {
        int idPedido = 10;
        when(pedidoRepo.removerPedido(idPedido)).thenReturn(true);

        boolean resultado = pedidoService.cancelarPedido(idPedido);

        assertTrue(resultado);


        verify(pedidoRepo, times(1)).removerPedido(idPedido);
    }

    @Test
    void criarPedido_clienteNaoEncontrado_lancaExcecao() {
        String cpfInexistente = "00000000000";

        when(clienteRepo.buscarPorCpf(cpfInexistente)).thenReturn(Optional.empty());

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
            pedidoService.criarPedido(cpfInexistente, "Mouse", 1, 50.0);
        });

        assertEquals("Cliente não encontrado", excecao.getMessage());


        verify(clienteRepo, times(1)).buscarPorCpf(cpfInexistente);
        verify(pedidoRepo, never()).salvar(any());
    }
}
