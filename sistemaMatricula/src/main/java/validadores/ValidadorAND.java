package validadores;

import excecoes.*;
import model.*;

import java.util.HashMap;
import java.util.Map;

public class ValidadorAND implements validadores.ValidadorPreRequisito {
    private Disciplina preRequisito1;
    private Disciplina preRequisito2;

    public ValidadorAND(Disciplina preRequisito1, Disciplina preRequisito2) {
        this.preRequisito1 = preRequisito1;
        this.preRequisito2 = preRequisito2;
    }

    @Override
    public void verificarValidador(Aluno aluno, Disciplina disciplinaDesejada, HashMap<Disciplina, String> turmasRejeitadas) throws ValidacaoMatriculaException {
        if (aluno == null || preRequisito1 == null || preRequisito2 == null) {
            throw new ValidacaoMatriculaException("Erro interno: Aluno ou pré-requisitos nulos para validação AND.");
        }
        Map<Disciplina, Double> historicoAluno = aluno.getHistoricoAluno();
        boolean cumpriuPreRequisito1ComNota = false;
        boolean cumpriuPreRequisito2ComNota = false;
        if (historicoAluno.containsKey(preRequisito1) &&
                historicoAluno.get(preRequisito1) >= 60.0) {
            cumpriuPreRequisito1ComNota = true;
        }
        if (historicoAluno.containsKey(preRequisito2) &&
                historicoAluno.get(preRequisito2) >= 60.0) {
            cumpriuPreRequisito2ComNota = true;
        }
        if (!(cumpriuPreRequisito1ComNota && cumpriuPreRequisito2ComNota)) {
            String msg = "Para se matricular em '" + disciplinaDesejada.getNome() + "', o aluno deve ter cursado E APROVADO em ambas as disciplinas '";
            msg += preRequisito1.getNome() + "' E '" + preRequisito2.getNome() + "'.";
            if (!cumpriuPreRequisito1ComNota && !cumpriuPreRequisito2ComNota) {
                msg += " Nenhuma das duas disciplinas foi atendida.";
            } else if (!cumpriuPreRequisito1ComNota) {
                msg += " A disciplina '" + preRequisito1.getNome() + "' não foi atendida.";
            } else {
                msg += " A disciplina '" + preRequisito2.getNome() + "' não foi atendida.";
            }
            turmasRejeitadas.put(disciplinaDesejada, msg);
            throw new PreRequisitoNaoCumpridoException(msg);
        }
    }
}