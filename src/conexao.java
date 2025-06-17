import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.Map;

public class conexao extends Janela {

    //Faz a verificação se o cpf e valido.
    public static boolean validarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        try {
            int[] pesos1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] pesos2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

            int soma1 = 0, soma2 = 0;
            for (int i = 0; i < 9; i++) {
                int digito = Character.getNumericValue(cpf.charAt(i));
                soma1 += digito * pesos1[i];
                soma2 += digito * pesos2[i];
            }
            int dv1 = (soma1 * 10 % 11) % 10;
            soma2 += dv1 * pesos2[9];
            int dv2 = (soma2 * 10 % 11) % 10;

            return cpf.equals(cpf.substring(0, 9) + dv1 + dv2);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //Fornecer um tipo de email para cadastrar.
    public static boolean validarEmail(String email, String matricula, String nome) {
        String emailMatri = matricula + "@aluno.sertao.ifrs.edu.br";
        String emailNome = nome.toLowerCase().replaceAll("\\s+", ".") + "@sertao.ifrs.edu.br";
        return email.equals(emailMatri) || email.equals(emailNome);
    }

    //A matrícula só com números.
    public static boolean validarMatricula(String matricula) {
        return matricula.matches("\\d+");
    }

    //Configura os nomes dos meses.
    public static int ConfiguracaoMeses (String nomeMeses){
        Map<String, Integer> mesesMap = new HashMap<>();
        mesesMap.put("Janeiro", 1);
        mesesMap.put("Fevereiro", 2);
        mesesMap.put("Março", 3);
        mesesMap.put("Abril", 4);
        mesesMap.put("Maio", 5);
        mesesMap.put("Junho", 6);
        mesesMap.put("Julho", 7);
        mesesMap.put("Agosto", 8);
        mesesMap.put("Setembro", 9);
        mesesMap.put("Outubro", 10);
        mesesMap.put("Novembro", 11);
        mesesMap.put("Dezembro", 12);

        return mesesMap.getOrDefault(nomeMeses, -1);
    }
}

