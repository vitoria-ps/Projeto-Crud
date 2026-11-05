package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {
    public static Connection conectar() {
        Connection conexao = null;
        try {
            Class.forName("org.sqlite.JDBC");

            String url = "jdbc:sqlite:biblioteca.db";
            conexao = DriverManager.getConnection(url);
            
            // Cria a tabela automaticamente se ela não existir
            criarTabelas(conexao);
            
        } catch (ClassNotFoundException e) {
            System.out.println("Driver do SQLite não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return conexao;
    }

    private static void criarTabelas(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS livros (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "titulo TEXT NOT NULL, " +
                     "autor TEXT NOT NULL, " +
                     "ano INTEGER, " +
                     "quantidade INTEGER NOT NULL, " +
                     "descricao TEXT)"; // Novo campo na base de dados
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar a tabela: " + e.getMessage());
        }
    }
}