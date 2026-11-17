
package br.com.starlog;

import br.com.starlog.dao.ProdutoDAO;
import br.com.starlog.dao.ProdutoDAOSqlite;
import br.com.starlog.model.Produto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:starlog.db")) {

            ProdutoDAO produtoDAO = new ProdutoDAOSqlite(connection);

            System.out.println("========== CREATE ==========");

            Produto teclado = new Produto(
                    "Teclado Mecânico RGB",
                    259.90,
                    10
            );

            Produto mousepad = new Produto(
                    "Mousepad Speed XL",
                    89.90,
                    20
            );

            produtoDAO.salvar(teclado);
            produtoDAO.salvar(mousepad);

            System.out.println("Teclado cadastrado! ID: " + teclado.getId());
            System.out.println("Mousepad cadastrado! ID: " + mousepad.getId());


            System.out.println("\n========== READ ==========");

            exibirProdutos(produtoDAO.listarTodos());


            System.out.println("\n========== UPDATE ==========");

            teclado.setEstoque(15);
            teclado.setPreco(289.90);

            boolean atualizado = produtoDAO.atualizar(teclado);

            if (atualizado) {
                System.out.println("Produto atualizado com sucesso!");

                Optional<Produto> produtoAtualizado =
                        produtoDAO.buscarPorId(teclado.getId());

                produtoAtualizado.ifPresent(System.out::println);

            } else {
                System.out.println("Nenhum produto foi atualizado.");
            }


            System.out.println("\n========== DELETE ==========");

            boolean excluido = produtoDAO.excluir(mousepad.getId());

            if (excluido) {
                System.out.println("Mousepad excluído com sucesso!");
            } else {
                System.out.println("Mousepad não encontrado.");
            }

            boolean exclusaoInexistente = produtoDAO.excluir(888L);

            if (exclusaoInexistente) {
                System.out.println("Produto 888 excluído.");
            } else {
                System.out.println("Produto 888 não encontrado. Nenhuma exclusão realizada.");
            }


            System.out.println("\n========== FINAL STATE ==========");

            List<Produto> produtosFinais = produtoDAO.listarTodos();

            exibirProdutos(produtosFinais);

            System.out.println("\nTotal de produtos restantes: " + produtosFinais.size());

        } catch (SQLException e) {
            System.out.println("Erro de conexão com o banco de dados: " + e.getMessage());
        }
    }

    private static void exibirProdutos(List<Produto> produtos) {

        System.out.printf("%-5s %-25s %12s %10s%n",
                "ID", "NOME", "PREÇO", "ESTOQUE");

        System.out.println("----------------------------------------------------------");

        for (Produto produto : produtos) {
            System.out.printf("%-5d %-25s %12.2f %10d%n",
                    produto.getId(),
                    produto.getNome(),
                    produto.getPreco(),
                    produto.getEstoque());
        }
    }
}
