package com.example;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    Cliente salvar(Cliente cliente);
    Optional<Cliente> buscarPorCpf(String cpf);
    List<Cliente> listarTodos();
}
