[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/ZHKJT9z4)
# Sistema de Gerenciamento de Pedidos

Este projeto simula um sistema simples de gerenciamento de pedidos, com o objetivo de aplicar testes unitários e de integração utilizando **JUnit 5** e **Mockito**.

---

## Objetivo

Os alunos deverão escrever **testes unitários** e **testes de integração** simulando repositórios com o Mockito. O foco está em aplicar boas práticas de testes em serviços com múltiplas dependências.

---

### Exemplo de estrutura:


## Parte 1 — Testes Unitários (foco em classes isoladas)

**Classe-alvo**: `PedidoService`

### Casos obrigatórios:

- ✅ Criar pedido com sucesso
- ❌ Criar pedido com quantidade inválida (deve lançar `IllegalArgumentException`)
- ❌ Criar pedido com cliente inexistente (`Optional.empty()` no mock)
- ✅ Listar pedidos por CPF
- ✅ Cancelar pedido com sucesso (`removerPedido` retorna `true`)
- ❌ Cancelar pedido inexistente (`removerPedido` retorna `false`)
- ✅ Cadastrar cliente com sucesso (`clienteRepo.salvar()`)

---

## Parte 2 — Testes Unitários com mais de uma classe

**Combinação**: `PedidoService` + `Pedido`

### Exemplo:

```java
Pedido pedido = pedidoService.criarPedido("12345678900", "Monitor", 2, 500.0);
assertEquals(1000.0, pedido.calcularTotal());
```

---

## Parte 3 — Testes de Integração (simulados com mocks)

Os testes de integração devem verificar **a interação entre as classes**, simulando repositórios reais com mocks.

### Exemplos de testes:

- Integração entre `PedidoService`, `ClienteRepository` e `PedidoRepository` ao criar pedido
- **Verificar** se `buscarPorCpf()` e `salvar()` são chamados corretamente
- Cancelamento de pedido com `verify(...)`
- Lançamento de exceção quando cliente não é encontrado

---

## 🛠️ Recomendações Gerais

- Use `@BeforeEach` para inicializar variáveis comuns
- Utilize: `when(...)`, `doReturn(...)`, `verify(...)`, `assertThrows(...)`, `assertEquals(...)`
- Aplique `@DisplayName` para tornar os testes mais legíveis
- Separe testes unitários e de integração em arquivos diferentes
- Cubra tanto **casos de sucesso** quanto **casos de falha**

---

## 📂 Estrutura do Projeto

```plaintext
src/
├── main/
│   └── java/
│       ├── Pedido.java
│       ├── Cliente.java
│       ├── PedidoRepository.java
│       ├── ClienteRepository.java
│       └── PedidoService.java
├── test/
│   └── java/
│       ├── PedidoServiceTest.java
│       └── ClienteRepositoryTest.java (opcional)
```

---

## Entrega

Clone o projeto, implemente os testes no diretório `test/`, rode com JUnit e Mockito, dê o push no mesmo repositório.