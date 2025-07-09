package validadores;
import validadores.ValidadorPreRequisito;

import modelo.Aluno;
import java.util.Arrays;
import java.util.List;

public class ValidadorOR implements ValidadorPreRequisito {
    private List<ValidadorPreRequisito> validadores;
    private String mensagemErro;

    public ValidadorOR(ValidadorPreRequisito... validadores) {
        this.validadores = Arrays.asList(validadores);
    }

    @Override
    public boolean verificarValidador(Aluno aluno) {
        StringBuilder sucessos = new StringBuilder();
        StringBuilder falhasTotal = new StringBuilder();
        boolean algumValido = false;

        for (ValidadorPreRequisito validador : validadores) {
            if (validador.verificarValidador(aluno)) {
                algumValido = true;
                sucessos.append(" (OR) ").append(validador.getMensagemErro());
                break;
            } else {
                falhasTotal.append(" (OR) ").append(validador.getMensagemErro());
            }
        }

        if (!algumValido) {
            mensagemErro = "Nenhum dos requisitos OR foi atendido:" + falhasTotal.toString();
        } else {
            mensagemErro = "Um dos requisitos OR foi atendido: " + sucessos.toString();
        }

        return algumValido;
    }

    @Override
    public String getMensagemErro() {
        return mensagemErro;
    }
}