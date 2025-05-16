package com.example;

import java.util.*;

public interface PedidoRepository {
    Pedido salvar(Pedido pedido);
    List<Pedido> listarPedidosPorCliente(String cpf);
    boolean removerPedido(int id);
    Optional<Pedido> buscarPorId(int id);
}
