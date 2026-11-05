package view;

import model.Livro;
import model.LivroDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BibliotecaView extends JFrame {
    private JTextField txtId, txtTitulo, txtAutor, txtAno, txtQuantidade;
    private JTextArea txtDescricao; // Campo multilinha para a descrição
    private JTable tabelaLivros;
    private DefaultTableModel modeloTabela;
    private LivroDAO livroDAO;

    public BibliotecaView() {
        // Aplica o visual moderno do sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        livroDAO = new LivroDAO();

        setTitle("Sistema de Biblioteca - CRUD Java Swing");
        setSize(750, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PAINEL DE FORMULÁRIO (SUPERIOR) ---
        JPanel painelForm = new JPanel(new GridLayout(6, 2, 5, 5));
        painelForm.setBorder(BorderFactory.createTitledBorder("Dados do Livro"));
        painelForm.setBorder(BorderFactory.createCompoundBorder(
            painelForm.getBorder(), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        txtId = new JTextField();
        txtId.setEnabled(false); 
        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtAno = new JTextField();
        txtQuantidade = new JTextField();
        
        txtDescricao = new JTextArea(3, 20);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);

        painelForm.add(new JLabel(" ID (Automático):"));
        painelForm.add(txtId);
        painelForm.add(new JLabel(" Título:"));
        painelForm.add(txtTitulo);
        painelForm.add(new JLabel(" Autor:"));
        painelForm.add(txtAutor);
        painelForm.add(new JLabel(" Ano:"));
        painelForm.add(txtAno);
        painelForm.add(new JLabel(" Quantidade:"));
        painelForm.add(txtQuantidade);
        painelForm.add(new JLabel(" Descrição:"));
        painelForm.add(scrollDescricao);

        add(painelForm, BorderLayout.NORTH);

        // --- TABELA DE LIVROS (CENTRO) ---
        modeloTabela = new DefaultTableModel(new String[]{"ID", "Título", "Autor", "Ano", "Qtd", "Descrição"}, 0);
        tabelaLivros = new JTable(modeloTabela);
        add(new JScrollPane(tabelaLivros), BorderLayout.CENTER);

        // --- PAINEL DE BOTÕES (INFERIOR) ---
        JPanel painelBotoes = new JPanel();

        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnAtualizar = new JButton("Editar");
        JButton btnDeletar = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnLimpar);

        add(painelBotoes, BorderLayout.SOUTH);

        // --- EVENTOS DOS BOTÕES ---
        
        btnCadastrar.addActionListener(e -> {
            try {
                Livro l = new Livro(
                    txtTitulo.getText(),
                    txtAutor.getText(),
                    Integer.parseInt(txtAno.getText()),
                    Integer.parseInt(txtQuantidade.getText()),
                    txtDescricao.getText()
                );
                livroDAO.cadastrar(l);
                carregarTabela();
                limparCampos();
                JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro nos dados inseridos: " + ex.getMessage());
            }
        });

        btnAtualizar.addActionListener(e -> {
    try {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um livro na tabela para editar.");
            return;
        }
        Livro l = new Livro();
        l.setId(Integer.parseInt(txtId.getText()));
        l.setTitulo(txtTitulo.getText());
        l.setAutor(txtAutor.getText());
        l.setAno(Integer.parseInt(txtAno.getText()));
        l.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
        l.setDescricao(txtDescricao.getText());

        livroDAO.atualizar(l);
        carregarTabela();
        limparCampos();
        JOptionPane.showMessageDialog(this, "Livro atualizado com sucesso!");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
    }
    });

        btnDeletar.addActionListener(e -> {
            try {
                if (txtId.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Selecione um livro na tabela para excluir.");
                    return;
                }
                int id = Integer.parseInt(txtId.getText());
                livroDAO.deletar(id);
                carregarTabela();
                limparCampos();
                JOptionPane.showMessageDialog(this, "Livro excluído com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
            }
        });

        btnLimpar.addActionListener(e -> limparCampos());

        tabelaLivros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabelaLivros.getSelectedRow();
                if (linha != -1) {
                    txtId.setText(modeloTabela.getValueAt(linha, 0).toString());
                    txtTitulo.setText(modeloTabela.getValueAt(linha, 1).toString());
                    txtAutor.setText(modeloTabela.getValueAt(linha, 2).toString());
                    txtAno.setText(modeloTabela.getValueAt(linha, 3).toString());
                    txtQuantidade.setText(modeloTabela.getValueAt(linha, 4).toString());
                    Object desc = modeloTabela.getValueAt(linha, 5);
                    txtDescricao.setText(desc != null ? desc.toString() : "");
                }
            }
        });

        carregarTabela();
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<Livro> livros = livroDAO.listar();
        for (Livro l : livros) {
            modeloTabela.addRow(new Object[]{
                l.getId(), l.getTitulo(), l.getAutor(), l.getAno(), l.getQuantidade(), l.getDescricao()
            });
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtAno.setText("");
        txtQuantidade.setText("");
        txtDescricao.setText("");
        tabelaLivros.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BibliotecaView().setVisible(true);
        });
    }
}