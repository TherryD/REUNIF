import javax.swing.*;
import java.sql.*;

public class base extends Janela{

    //Cria o banco de dados.
    protected static void CriarBD() throws SQLException {
        Connection con = null;
        Statement stmt = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:base.db");
            stmt = con.createStatement();
            stmt.setQueryTimeout(30);

            tabelaAlunos(stmt);
            TabelaAgendamentos(stmt);
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao criar o banco de dados ou as tabelas" + e.getMessage());
            throw e;
        }finally {
            if(stmt != null){
                stmt.close();
            }
            if(con != null){
                con.close();
            }
        }
    }

    //Cria a tabela alunos.
    protected static void tabelaAlunos(Statement stmt) {
        String sql = "CREATE TABLE IF NOT EXISTS tabela_alunos (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " nome TEXT NOT NULL,\n"
                + " email TEXT NOT NULL,\n"
                + " matricula INT NOT NULL UNIQUE,\n"
                + " cpf TEXT NOT NULL UNIQUE\n"
                + ");";
        try{
            stmt.execute(sql);
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao criar a tabela de alunos");
        }
    }

    //Cria a tabela agendamentos.
    protected static void TabelaAgendamentos(Statement stmt) {
        String sql = "CREATE TABLE IF NOT EXISTS agendamentos (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " data TEXT NOT NULL,\n"
                + " tipo_refeicao TEXT NOT NULL,\n"
                + "horario TEXT NOT NULL,\n"
                + " matricula INT NOT NULL,\n"
                + "FOREIGN KEY (matricula) REFERENCES tabela_alunos(matricula)\n"
                + "UNIQUE (matricula, data, tipo_refeicao)\n"
                + ");";

        try{stmt.execute(sql);
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao criar a tabela de agendamentos");
        }
    }
}



