package validadores;

import modelo.Aluno;
import java.util.Arrays;
import java.util.List;

public class ValidadorAND implements ValidadorPreRequisito {
    private List<ValidadorPreRequisito> validadores;
    private String mensagemErro;

    public ValidadorAND(ValidadorPreRequisito... validadores) { // Varargs para facilitar a criação
        this.validadores = Arrays.asList(validadores);
    }

    @Override
    public boolean verificarValidador(Aluno aluno) {
        StringBuilder falhas = new StringBuilder();
        boolean todosValidos = true;

        for (ValidadorPreRequisito validador : validadores) {
            if (!validador.verificarValidador(aluno)) {
                todosValidos = false;
                falhas.append(" (AND) ").append(validador.getMensagemErro());
            }
        }
        mensagemErro = "Requisitos AND não atendidos:" + falhas.toString();
        return todosValidos;
    }

    @Override
    public String getMensagemErro() {
        return mensagemErro;
    }
}