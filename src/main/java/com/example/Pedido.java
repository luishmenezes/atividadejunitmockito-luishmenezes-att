package com.example;

public class Pedido {
    private int id;
    private String produto;
    private int quantidade;
    private double precoUnitario;

    public Pedido(int id, String produto, int quantidade, double precoUnitario) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public int getId() { return id; }
    public String getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public double getPrecoUnitario() { return precoUnitario; }

    public double calcularTotal() {
        return quantidade * precoUnitario;
    }
}
