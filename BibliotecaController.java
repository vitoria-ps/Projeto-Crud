package controller;

import model.Livro;
import model.LivroDAO;
import java.util.List;

public class BibliotecaController {
    private LivroDAO livroDAO = new LivroDAO();

    public void cadastrarLivro(String titulo, String autor, int ano, int quantidade, String descricao) {
        Livro livro = new Livro(titulo, autor, ano, quantidade, descricao);
        livroDAO.cadastrar(livro);
    }

    public List<Livro> listarLivros() {
        return livroDAO.listar();
    }
}