import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class Janela extends JFrame {
    private JPanel TelaPrincipal;
    private JPanel Telas;
    private JPanel Cadastro;
    private JPanel Login;
    private JTextField tfMatricula;
    private JButton loginButton;
    private JButton cadastrarButton;
    private JPanel TelaInicial;
    private JButton btVoltar;
    protected JTextField tfCMatri;
    protected JTextField tfCEmail;
    protected JTextField tfCNome;
    protected JTextField tfCcpf;
    private JButton enviarButton;
    private JButton pagamentoButton;
    private JButton perfilButton;
    private JButton cardapioButton;
    private JPanel Perfil;
    private JButton sairButton;
    private JButton voltarButton1;
    private JLabel nomeAluno;
    private JLabel emailAluno;
    private JLabel matriculaAluno;
    private JLabel nomeAlun;
    private JPasswordField jpfCPF;
    private JLabel ImagemLogin;
    private JLabel ImagemCadas;
    private JLabel ImagemPer;
    private JPanel Cardapio;
    private JPanel Agendamento;
    private JButton agendamentobutton;
    private JButton voltartelainicial;
    private JButton agendarAlmocoButton;
    private JPanel AgendarAlmoco;
    private String matriculaLogado;
    private JComboBox<String> day;
    private JComboBox<String> month;
    private JComboBox<String> year;
    private JButton VoltaragendamentoButton;
    private JButton ConfirmarAgendamentobutton;
    private JButton SeusagendamentosButton;
    private JButton voltarAgendamentoButton;
    private JPanel SeusAgenamentos;
    private JTable tabeladeagendamento;
    private JComboBox<String> TipodeRefeicaoAgendar;
    private JButton voltartelainicialButton;
    private JComboBox<String> HorarioRefeicao;
    private JButton editarButton;
    private JButton excluirButton;
    private JLabel ImagemCardapio;
    private JButton downloadDoCardapioButton;


    public Janela() {
        setContentPane(TelaPrincipal);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("REUNIF");
        ImageIcon icone1 = new ImageIcon("Imagens/Logo.png");
        setIconImage(icone1.getImage());
        setResizable(false);
        setSize(700, 650);
        setLocationRelativeTo(null);
        configurarButtons();
        imagensCon();
        configurarComboBox();
        setVisible(true);
    }

    //Imagens nas partes do login, cadastro, perfil e calendário.
    public void imagensCon(){
        //Colocar a Imagem na tela e ajeitar o tamnho dela.
        ImageIcon imagemIcon = new ImageIcon(getClass().getResource("/logo.png"));
        Image imagem = imagemIcon.getImage();
        Image ri = imagem.getScaledInstance(350, 350, java.awt.Image.SCALE_SMOOTH);
        ImagemLogin.setIcon(new ImageIcon(ri));

        ImageIcon imagemIcon2 = new ImageIcon(getClass().getResource("/logo.png"));
        Image imagem2 = imagemIcon2.getImage();
        Image ri2 = imagem2.getScaledInstance(250, 250, java.awt.Image.SCALE_SMOOTH);
        ImagemCadas.setIcon(new ImageIcon(ri2));

        //foto de perfil
        ImageIcon imagemIcon3 = new ImageIcon(getClass().getResource("/FotoPerfil.png"));
        Image imagem3 = imagemIcon3.getImage();
        Image ri3 = imagem3.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
        ImagemPer.setIcon(new ImageIcon(ri3));

        //foto do cardápio
        ImageIcon imageIcon4 = new ImageIcon(getClass().getResource("/CARDAPIO-SEMANA.jpg"));
        Image imagem4 = imageIcon4.getImage();
        Image ri4 = imagem4.getScaledInstance(650,490, java.awt.Image.SCALE_SMOOTH);
        ImagemCardapio.setIcon(new ImageIcon(ri4));

    }

    //Parte da configuração dos JComboBox.
    public void configurarComboBox(){
        RefeicaoAgendado();
        ConfigurarData();

    }

    //Configuração dos nomes da refeição.
    public void  RefeicaoAgendado(){
        TipodeRefeicaoAgendar.addItem("");
        TipodeRefeicaoAgendar.addItem("Café da Manhã");
        TipodeRefeicaoAgendar.addItem("Almoço");
        TipodeRefeicaoAgendar.addItem("Janta");

        HorarioRefeicao.addItem("");
        HorarioRefeicao.addItem("06:45 às 08:00");
        HorarioRefeicao.addItem("11:45 às 13:00");
        HorarioRefeicao.addItem("18:30 às 19:30");

        HorarioRefeicao.setEditable(false);
        HorarioRefeicao.setEnabled(false);

        TipodeRefeicaoAgendar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarHorarioRefeicao();
            }
        });
    }

    //Configuração das datas
    public void ConfigurarData(){
        String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

        for(String mes : meses){
            month.addItem(mes);
        }

        for (int i = 2025; i <2030 ; i++) {
            year.addItem(String.valueOf(i));
        }
        month.addActionListener(e -> atualizarDias());
        year.addActionListener(e -> atualizarDias());

        atualizarDias();

    }

    //Configuração dos dias
    private void atualizarDias(){
        day.removeAllItems();

        int mesSelecionada = month.getSelectedIndex() + 1;
        int anoSelecionada = year.getSelectedItem() != null
                ? Integer.parseInt((String) year.getSelectedItem())
                : LocalDate.now().getYear();

        int diasMeses = DiasdoMeses(mesSelecionada, anoSelecionada);

        for (int i = 1; i <= diasMeses; i++) {
            day.addItem(String.valueOf(i));
        }
    }

    //Configuração dos meses.
    private int DiasdoMeses(int mes, int ano) {
        int[] diasporMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if (mes == 2 && ((ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0))) {
            return 29;
        }
        return diasporMes[mes -1];
    }

    //Função que controla todos os botões.
    public void configurarButtons(){
        CardLayout cl = ((CardLayout) Telas.getLayout());
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(Telas, "cadastro");
            }
        });
        btVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(Telas, "login");
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(Telas, "login");
            }
        });
        perfilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoPerfil();
            }
        });
        voltarButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(Telas, "telaInicial");
            }
        });
        enviarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarDados();
            }
        });
        //botão que vai para o site do pagamento.
        pagamentoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://pagtesouro.ifrs.edu.br/unidades?returnPath=/pagamento/novo"));
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        agendamentobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(Telas, "agendamento");
            }
        });
        voltartelainicial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(Telas, "telaInicial");
            }
        });
        cardapioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(Telas, "cardapio");
            }
        });
        agendarAlmocoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(Telas, "agendaralmoco");
            }
        });

        ConfirmarAgendamentobutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agendarRefeicao();
            }
        });
        VoltaragendamentoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(Telas, "agendamento");
            }
        });
        SeusagendamentosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarAgendamentos();
                cl.show(Telas, "seusagenamentos");
            }
        });
        voltarAgendamentoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(Telas, "agendamento");
            }
        });
        voltartelainicialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(Telas, "telaInicial");
            }
        });
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirAgendamento();
            }
        });
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarAgendamento();
            }
        });
        downloadDoCardapioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setDialogTitle("Salvar imagem do cardápio");
                jfc.setSelectedFile(new File("Cardapio-Semana.jpg"));

                int userSelicionar = jfc.showSaveDialog(Janela.this);
                if(userSelicionar == JFileChooser.APPROVE_OPTION){
                    try{
                        File destino = jfc.getSelectedFile();

                        File origem = new File("Imagens/CARDAPIO-SEMANA.jpg");
                        if (!origem.exists()) {
                            JOptionPane.showMessageDialog(Janela.this, "Imagem não encontrada!", "ERRO", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        Files.copy(origem.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        JOptionPane.showMessageDialog(Janela.this, "Imagem salva com sucesso em:\n" + destino.getAbsolutePath());
                    }catch (IOException ex){
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(Janela.this, "Erro ao salvar imagem!", "ERRO", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

    }

    //Função que salva as informações no banco de dados.
    public void enviarDados() {
        String nome = tfCNome.getText().trim();
        String matricula = tfCMatri.getText().trim();
        String cpf = tfCcpf.getText().trim();
        String email = tfCEmail.getText().trim();

        if(nome.isEmpty() || matricula.isEmpty() || cpf.isEmpty() || email.isEmpty()){
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios", "ERRO", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!conexao.validarCPF(cpf)){
            JOptionPane.showMessageDialog(this, "CPF inválido.", "ERRO", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!conexao.validarEmail(email, matricula, nome)){
            JOptionPane.showMessageDialog(this, "O e-mail deve estar no formato institucional:", "ERRO", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!conexao.validarMatricula( matricula )){
            JOptionPane.showMessageDialog(this,"Matrícula inválida", "ERRO", JOptionPane.ERROR_MESSAGE);
        }

        String url = "jdbc:sqlite:base.db";
        //Verificar se o cpf já está castrado.
        try(Connection con = DriverManager.getConnection(url)){
            String CPFD = "SELECT COUNT(*) FROM tabela_Alunos WHERE cpf = ?";
            try(PreparedStatement pstmt = con.prepareStatement(CPFD)){
                pstmt.setString(1, cpf);
                try(ResultSet rs = pstmt.executeQuery()){
                    if(rs.next() && rs.getInt(1) > 0){
                        JOptionPane.showMessageDialog(this, "CPF já cadastrado. Tente outro CPF.", "ERRO", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            //Verificar se a matrícula já existe.
            String verificarMatricula = "SELECT COUNT(*) FROM tabela_Alunos WHERE matricula = ?";
            try(PreparedStatement pstmt = con.prepareStatement(verificarMatricula)){
                pstmt.setString(1, matricula);
                try(ResultSet rs = pstmt.executeQuery()){
                    if(rs.next() && rs.getInt(1) > 0){
                        JOptionPane.showMessageDialog(this, "Matrícula já cadastrada. Tente outra Matrícula. ", "ERRO", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }catch (SQLException e){
                    JOptionPane.showMessageDialog(this,"Erro ao verificar a matrícula" + e.getMessage());
                    return;
                }

            }

            String sql = "INSERT INTO tabela_Alunos(nome, matricula, cpf, email) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, nome);
                pstmt.setString(2, matricula);
                pstmt.setString(3, cpf);
                pstmt.setString(4, email);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Cadastro finalizado com sucesso!");
                tfCNome.setText("");
                tfCMatri.setText("");
                tfCcpf.setText("");
                tfCEmail.setText("");
            }catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar os dados:" + e.getMessage());
                }
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, "Erro de coneção:" + e.getMessage());
        }
    }

    //Função que faz o login com as informações do banco de dados.
    public void realizarLogin() {
        String matricula = tfMatricula.getText();
        String cpf = jpfCPF.getText();
        String url = "jdbc:sqlite:base.db";

        if(!conexao.validarCPF(cpf)){
            JOptionPane.showMessageDialog(this,"CPF inválido.", "ERRO", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DriverManager.getConnection(url)) {
            String sql = "SELECT * FROM tabela_Alunos WHERE matricula = ? AND cpf = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, matricula);
                pstmt.setString(2, cpf);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String nome = rs.getString("nome");
                        nomeAlun.setText(nome);
                        matriculaLogado = matricula;
                        Login.setVisible(false);
                        TelaInicial.setVisible(true);
                        tfMatricula.setText("");
                        jpfCPF.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Dados inválidos. Tente novamente");
                    }
                }
            } catch (SQLException e) {
                if(e.getMessage().contains("UNIQUE constraint failed: tabela_Alunos.cpf")){
                    JOptionPane.showMessageDialog(this, "O CPF informado já está cadastrado.", "ERRO", JOptionPane.ERROR_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(this, "Erro ao validar login:" + e.getMessage());}
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro de conexão:" + e.getMessage());
        }
    }

    //Função para colocar as informações dos alunos na tela.
    public void infoPerfil(){
        String url = "jdbc:sqlite:base.db";

        try(Connection con = DriverManager.getConnection(url)){
            String sql = "SELECT nome, email, matricula FROM tabela_Alunos WHERE matricula = ?";
            try(PreparedStatement pstmt = con.prepareStatement(sql)){
                pstmt.setString(1,matriculaLogado);
                try(ResultSet rs = pstmt.executeQuery()){
                    if(rs.next()){
                        nomeAluno.setText(rs.getString("nome"));
                        emailAluno.setText(rs.getString("email"));
                        matriculaAluno.setText(rs.getString("matricula"));
                        TelaInicial.setVisible(false);
                        Perfil.setVisible(true);
                    }else{
                        JOptionPane.showMessageDialog(this, "Nenhum aluno foi encontrado");
                    }
                }
            }catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao encontrar aluno: " + e.getMessage());
            }
        }catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro de conexão: " + e.getMessage());
        }
    }

    //Fazer o agendamento das refeições.
    public void agendarRefeicao(){
        try {
            int day = Integer.parseInt((String) this.day.getSelectedItem());
            int month = conexao.ConfiguracaoMeses((String) this.month.getSelectedItem());
            if (month == -1){
                JOptionPane.showMessageDialog(this, "Mês inválido. Por favor, selecione um mês válido.", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int year = Integer.parseInt((String) this.year.getSelectedItem());
            LocalDate data = LocalDate.of(year, month, day);

            if (data.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Não é possível agendar para uma data no passao. ", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (data.getDayOfWeek() == DayOfWeek.SATURDAY || data.getDayOfWeek() == DayOfWeek.SUNDAY) {
                JOptionPane.showMessageDialog(this, "Não é possível agendar refeições aos sábados e domingos.", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String dataFormatadaBD = data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String dataFormatadaExibida = data.format(DateTimeFormatter.ofPattern("yyyy-MMMM-dd", Locale.forLanguageTag("pt-BR")));
            String tipoRefeicao = (String) TipodeRefeicaoAgendar.getSelectedItem();
            String horario = (String) HorarioRefeicao.getSelectedItem();

            if(tipoRefeicao == null || tipoRefeicao.trim().isEmpty() ){
                JOptionPane.showMessageDialog(this, "Selecione um tipo de refeição antes de continuar.", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (horario == null || horario.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um horário para a refeição.", "ERRO", JOptionPane.ERROR_MESSAGE);
            }

            String url = "jdbc:sqlite:base.db";
            try (Connection con = DriverManager.getConnection(url)) {
                String verificarAgendamento = "SELECT COUNT(*) FROM agendamentos WHERE data = ? AND matricula = ?";
                try (PreparedStatement pstmt = con.prepareStatement(verificarAgendamento)) {
                    pstmt.setString(1, dataFormatadaBD);
                    pstmt.setString(2, matriculaLogado);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) >= 3) {
                            JOptionPane.showMessageDialog(this, "Você já agendou esse tipo de refeição para essa data.", "ERRO", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                String verificarTipoRefeicao = "SELECT COUNT(*) FROM agendamentos WHERE data = ? AND matricula = ? AND tipo_refeicao = ?";
                try(PreparedStatement pstmt = con.prepareStatement(verificarTipoRefeicao)) {
                    pstmt.setString(1, dataFormatadaBD);
                    pstmt.setString(2, matriculaLogado);
                    pstmt.setString(3, tipoRefeicao);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            JOptionPane.showMessageDialog(this,"Você já agendou esse tipo de refeição para essa data.", "ERRO", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                String sql = "INSERT INTO agendamentos(data, tipo_refeicao, horario, matricula) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setString(1, dataFormatadaBD);
                    pstmt.setString(2, tipoRefeicao);
                    pstmt.setString(3, horario);
                    pstmt.setString(4, matriculaLogado);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, tipoRefeicao + " agendado com sucesso, das " + horario + " para " + dataFormatadaExibida + "!");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Erro ao agendar as refeicões: " + e.getMessage());
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro de conexão:" + e.getMessage());
            }
        }catch (DateTimeParseException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Data inválida. Por favor, selecione uma data valida.", "ERRO", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Função para listar o agendamento das refeições.
    public void listarAgendamentos(){
        String url = "jdbc:sqlite:base.db";

        try(Connection con = DriverManager.getConnection(url)){
            String sql = "SELECT data, tipo_refeicao, horario FROM agendamentos WHERE matricula = ? ORDER BY data ASC";
            try(PreparedStatement pstmt = con.prepareStatement(sql)){
                pstmt.setString(1,matriculaLogado);
                try(ResultSet rs = pstmt.executeQuery()){
                    DefaultTableModel model = new DefaultTableModel(new String[]{"Data", "Tipo_Refeição", "Horário"}, 0){
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };

                    while (rs.next()){
                        String data = rs.getString("data");
                        String tipoRefeicao = rs.getString("tipo_refeicao");
                        String horario = rs.getString("horario");

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate ld = LocalDate.parse(data);
                        String dataFormatada = ld.format(dtf);
                        model.addRow(new Object[]{data, tipoRefeicao, horario});
                    }
                    tabeladeagendamento.setModel(model);
                    tabeladeagendamento.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                }
            }
        }catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar agendamentos: " + e.getMessage());
        }
    }

    //Função para excluir um agendamanto da tabela.
    public void excluirAgendamento(){
        int linhaSelecionada = tabeladeagendamento.getSelectedRow();
        if(linhaSelecionada == -1){
            JOptionPane.showMessageDialog(this, "Selecione um agendamento para excluir.", "ERRO", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String dataSelecionada = (String) tabeladeagendamento.getValueAt(linhaSelecionada, 0);
        String tipoRefeicaoSelecionada = (String) tabeladeagendamento.getValueAt(linhaSelecionada, 1);
        String horarioSelecionado = (String) tabeladeagendamento.getValueAt(linhaSelecionada, 2);

        int confirmar = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o agendamento de " + tipoRefeicaoSelecionada + " no dia " + dataSelecionada + " às " + horarioSelecionado + "?", "Confirmação",JOptionPane.YES_NO_OPTION);

        if(confirmar == JOptionPane.YES_NO_OPTION){
            String url = "jdbc:sqlite:base.db";
            try(Connection con = DriverManager.getConnection(url)){
                String sql = "DELETE FROM agendamentos WHERE data = ? AND tipo_refeicao = ? AND horario = ? AND matricula = ?";
                try(PreparedStatement pstmt = con.prepareStatement(sql)){
                    pstmt.setString(1, dataSelecionada);
                    pstmt.setString(2, tipoRefeicaoSelecionada);
                    pstmt.setString(3, horarioSelecionado);
                    pstmt.setString(4,matriculaLogado);

                    int linhaAfetadas = pstmt.executeUpdate();
                    if(linhaAfetadas > 0){
                        JOptionPane.showMessageDialog(this, "Agendamento excluído com sucesso!", "Sucesso",JOptionPane.INFORMATION_MESSAGE);
                        listarAgendamentos();;
                    }else{
                        JOptionPane.showMessageDialog(this, "Não foi possível excluir o agendamento. Tente novamente", "ERRO", JOptionPane.ERROR_MESSAGE);
                    }
                }catch (SQLException e){
                    JOptionPane.showMessageDialog(this, "Erro ao excluir o agendamento: " + e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
                }
            }catch (SQLException e){
                JOptionPane.showMessageDialog(this, "Erro ao acessar o banco de dados:  " + e.getMessage());
            }
        }
    }

    //Função para edidar uma refeição que errou.
    public void editarAgendamento(){
        int linhaSelecionada = tabeladeagendamento.getSelectedRow();
        if(linhaSelecionada == -1){
            JOptionPane.showMessageDialog(this,"Selecione um agendamento para editar.", "ERRO", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String dataSelecionada = (String) tabeladeagendamento.getValueAt(linhaSelecionada, 0);
        String tipoReficaoSelecionada = (String) tabeladeagendamento.getValueAt(linhaSelecionada, 1);
        String horarioSelecionada = (String) tabeladeagendamento.getValueAt(linhaSelecionada, 2);

        String[] opcoesRefeicao = {"Café da Manhã", "Almoço", "Janta"};
        String novoTipoRefeicao = (String) JOptionPane.showInputDialog(this, "Selecione o novo tipo de refeição:", "Editar Refeição", JOptionPane.QUESTION_MESSAGE, null, opcoesRefeicao, tipoReficaoSelecionada);

        if(novoTipoRefeicao == null || novoTipoRefeicao.equals(tipoReficaoSelecionada)){
            JOptionPane.showMessageDialog(this,"Edição cancelada ou sem alterações.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String novoHorario = switch (novoTipoRefeicao){
            case "Café da Manhã" -> "06:45 às 08:00";
            case "Almoço" -> "11:45 às 13:00";
            case "Janta" -> "18:30 às 19:30";
            default -> horarioSelecionada;
        };

        if (novoHorario == null || novoHorario.isBlank()){
            JOptionPane.showMessageDialog(this, "Edição cancelada ou sem alterações no horário.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }


        String url = "jdbc:sqlite:base.db";
        try(Connection con = DriverManager.getConnection(url)){
            String sql = "UPDATE agendamentos SET tipo_refeicao = ?, horario = ? WHERE data = ? AND horario = ? AND matricula = ?";
            try(PreparedStatement pstmt = con.prepareStatement(sql)){
                pstmt.setString(1, novoTipoRefeicao);
                pstmt.setString(2,novoHorario);
                pstmt.setString(3, dataSelecionada);
                pstmt.setString(4,horarioSelecionada);
                pstmt.setString(5,matriculaLogado);

                int linhaAfetadas = pstmt.executeUpdate();
                if(linhaAfetadas > 0){
                    JOptionPane.showMessageDialog(this,"Agendamento editado com sucesso!", "Sucesso",JOptionPane.INFORMATION_MESSAGE);
                    listarAgendamentos();
                }else{
                    JOptionPane.showMessageDialog(this, "Não foi possível editar o agendamento. Tente novamente", "ERRO", JOptionPane.ERROR_MESSAGE);
                }
            }
        }catch (SQLException e){
            JOptionPane.showMessageDialog(this,"Erro ao acessar o banco de dados:  " + e.getMessage());
        }

    }

    //Função para preencher automaticamente o horário.
    public void atualizarHorarioRefeicao(){
        String tipoRefeicao = (String) TipodeRefeicaoAgendar.getSelectedItem();

        if(tipoRefeicao == null || tipoRefeicao.trim().isEmpty()){
            HorarioRefeicao.setEnabled(false);
            HorarioRefeicao.setSelectedItem("");
        } else if ("Café da Manhã".equals(tipoRefeicao)){
            HorarioRefeicao.setSelectedItem("06:45 às 08:00");
            HorarioRefeicao.setEnabled(false);
        } else if ("Almoço".equals(tipoRefeicao)) {
            HorarioRefeicao.setSelectedItem("11:45 às 13:00");
            HorarioRefeicao.setEnabled(false);
        } else if ("Janta".equals(tipoRefeicao)) {
            HorarioRefeicao.setSelectedItem("18:30 às 19:30");
            HorarioRefeicao.setEnabled(false);
        }else{
            HorarioRefeicao.setEditable(false);
            HorarioRefeicao.setSelectedItem("");
        }
    }
}