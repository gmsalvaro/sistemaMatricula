package validadores;

import excecoes.*;
import modelo.*;

import java.util.HashMap;


public class ValidadorCreditosMin implements validadores.ValidadorPreRequisito {
    private int creditosMinimosExigidos;
    private Disciplina disciplinaReferencia;

    public ValidadorCreditosMin(Disciplina disciplinaReferencia, int creditosMinimosExigidos) {
        this.disciplinaReferencia = disciplinaReferencia;
        this.creditosMinimosExigidos = creditosMinimosExigidos;
    }

    @Override
    public void verificarValidador(Aluno aluno, Disciplina disciplinaAlvo, HashMap<Disciplina, String> turmasRejeitadas) throws ValidacaoMatriculaException {
        // Validação inicial para evitar NullPointerException
        if (aluno == null || disciplinaReferencia == null) {
            throw new ValidacaoMatriculaException("Erro interno: Aluno ou disciplina de referência nulo para validação de créditos mínimos.");
        }

        if (aluno.getCreditosAcumulados() < this.creditosMinimosExigidos) {
            String mensagem = "O aluno '" + aluno.getNome() + "' não possui os " + this.creditosMinimosExigidos +
                    " créditos mínimos acumulados para cursar '" + disciplinaReferencia.getNome() + "'. " +
                    "Créditos atuais: " + aluno.getCreditosAcumulados() + ".";
            turmasRejeitadas.put(disciplinaAlvo, mensagem);
            throw new  PreRequisitoNaoCumpridoException(mensagem);
        }
    }
}