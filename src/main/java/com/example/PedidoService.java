package com.example;

import java.util.List;
import java.util.Random;

public class PedidoService {
    private PedidoRepository pedidoRepo;
    private ClienteRepository clienteRepo;

    public PedidoService(PedidoRepository pedidoRepo, ClienteRepository clienteRepo) {
        this.pedidoRepo = pedidoRepo;
        this.clienteRepo = clienteRepo;
    }

    public Pedido criarPedido(String cpf, String produto, int quantidade, double precoUnitario) {
        if (quantidade <= 0 || precoUnitario <= 0) throw new IllegalArgumentException("Valores inválidos");
        Cliente cliente = clienteRepo.buscarPorCpf(cpf).orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        Pedido pedido = new Pedido(new Random().nextInt(1000), produto, quantidade, precoUnitario);
        return pedidoRepo.salvar(pedido);
    }

    public List<Pedido> listarPedidosCliente(String cpf) {
        return pedidoRepo.listarPedidosPorCliente(cpf);
    }

    public boolean cancelarPedido(int id) {
        return pedidoRepo.removerPedido(id);
    }

    public Cliente cadastrarCliente(String cpf, String nome, String email) {
        return clienteRepo.salvar(new Cliente(cpf, nome, email));
    }
}