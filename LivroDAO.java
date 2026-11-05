package model;

import conexao.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    public void cadastrar(Livro livro) {
    String sql = "INSERT INTO livros (titulo, autor, ano, quantidade, descricao) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, livro.getTitulo());
        stmt.setString(2, livro.getAutor());
        stmt.setInt(3, livro.getAno());
        stmt.setInt(4, livro.getQuantidade());
        stmt.setString(5, livro.getDescricao()); // <--- Envia a descrição
        stmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Erro ao cadastrar: " + e.getMessage());
    }
}

public List<Livro> listar() {
    String sql = "SELECT * FROM livros";
    List<Livro> livros = new ArrayList<>();
    try (Connection conn = Conexao.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            Livro livro = new Livro();
            livro.setId(rs.getInt("id"));
            livro.setTitulo(rs.getString("titulo"));
            livro.setAutor(rs.getString("autor"));
            livro.setAno(rs.getInt("ano"));
            livro.setQuantidade(rs.getInt("quantidade"));
            livro.setDescricao(rs.getString("descricao")); // <--- Lê a descrição da BD
            livros.add(livro);
        }
    } catch (SQLException e) {
        System.out.println("Erro ao listar: " + e.getMessage());
    }
    return livros;
}

    public void atualizar(Livro livro) {
    String sql = "UPDATE livros SET titulo = ?, autor = ?, ano = ?, quantidade = ?, descricao = ? WHERE id = ?";
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, livro.getTitulo());
        stmt.setString(2, livro.getAutor());
        stmt.setInt(3, livro.getAno());
        stmt.setInt(4, livro.getQuantidade());
        stmt.setString(5, livro.getDescricao()); // Garante que a descrição vai para o 5º parâmetro
        stmt.setInt(6, livro.getId());            // ID vai para o 6º parâmetro (WHERE id = ?)
        stmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Erro ao atualizar: " + e.getMessage());
    }
}

    public void deletar(int id) {
        String sql = "DELETE FROM livros WHERE id = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar: " + e.getMessage());
        }
    }
}