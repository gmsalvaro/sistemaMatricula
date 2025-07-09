package validadores;
import validadores.ValidadorPreRequisito;

import modelo.Aluno;
import modelo.Disciplina;

public class ValidadorSimples implements ValidadorPreRequisito {
    private Disciplina preRequisito;
    private String mensagemErro = " "; // Para armazenar a mensagem específica

    public ValidadorSimples(Disciplina preRequisito) {
        this.preRequisito = preRequisito;
    }

    @Override
    public boolean verificarValidador(Aluno aluno) {
        //Usar excepcion depois !
        if (aluno == null || preRequisito == null) {
            mensagemErro = "Erro interno: Aluno ou pré-requisito nulo para validação simples.";
            return false;
        }

        if (!aluno.getDisciplinasCursadas().containsKey(preRequisito)) {
            mensagemErro = "Pré-requisito '" + preRequisito.getNome() + "' não cursado.";
            return false;
        }

        if (aluno.getDisciplinasCursadas().get(preRequisito) < 60.0) {
            mensagemErro = "Nota insuficiente em '" + preRequisito.getNome() + "' (necessário >= 60.0).";
            return false;
        }

        return true; // Pré-requisito simples cumprido
    }

    @Override
    public String getMensagemErro() {
        return mensagemErro;
    }
}