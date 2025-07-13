package validadores;

import excecoes.*;
import model.*;

import java.util.HashMap;

public class ValidadorCreditosMin implements validadores.ValidadorPreRequisito {
    private int creditosMinimosExigidos;

    public ValidadorCreditosMin(int creditosMinimosExigidos) {
        this.creditosMinimosExigidos = creditosMinimosExigidos;
    }

    @Override
    public void verificarValidador(Aluno aluno, Disciplina disciplinaDesejada, HashMap<Disciplina, String> turmasRejeitadas) throws ValidacaoMatriculaException {
        if (aluno == null || disciplinaDesejada == null)
            throw new ValidacaoMatriculaException("Erro interno: Aluno ou disciplina de referência nulo para validação de créditos mínimos.");

        if (aluno.getCreditosAcumulados() < this.creditosMinimosExigidos) {
            String mensagem = "O aluno '" + aluno.getNome() + "' não possui os " + this.creditosMinimosExigidos +
                    " créditos mínimos acumulados para cursar '" + disciplinaDesejada.getNome() + "'. " +
                    "Créditos atuais: " + aluno.getCreditosAcumulados() + ".";
            turmasRejeitadas.put(disciplinaDesejada, mensagem);
            throw new  PreRequisitoNaoCumpridoException(mensagem);
        }
    }
}