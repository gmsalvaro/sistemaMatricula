package validadores;

import modelo.Disciplina;
import modelo.Aluno;
import excecoes.ValidacaoMatriculaException;
import excecoes.PreRequisitoNaoCumpridoException;
import java.util.Map;

public class ValidadorAND implements validadores.ValidadorPreRequisito {
    private Disciplina preRequisito1;
    private Disciplina preRequisito2;

    public ValidadorAND(Disciplina preRequisito1, Disciplina preRequisito2) {
        this.preRequisito1 = preRequisito1;
        this.preRequisito2 = preRequisito2;
    }

    @Override
    public void verificarValidador(Aluno aluno, Disciplina disciplinaAlvo) throws ValidacaoMatriculaException {
        if (aluno == null || preRequisito1 == null || preRequisito2 == null) {
            throw new ValidacaoMatriculaException("Erro interno: Aluno ou pré-requisitos nulos para validação AND.");
        }

        Map<Disciplina, Double> disciplinasCursadasMap = aluno.getDisciplinasCursadas();
        boolean cumpriuPreRequisito1ComNota = false;
        boolean cumpriuPreRequisito2ComNota = false;

        if (disciplinasCursadasMap.containsKey(preRequisito1) &&
                disciplinasCursadasMap.get(preRequisito1) >= 60.0) {
            cumpriuPreRequisito1ComNota = true;
        }

        if (disciplinasCursadasMap.containsKey(preRequisito2) &&
                disciplinasCursadasMap.get(preRequisito2) >= 60.0) {
            cumpriuPreRequisito2ComNota = true;
        }

        if (!(cumpriuPreRequisito1ComNota && cumpriuPreRequisito2ComNota)) {
            String message = "Para se matricular em '" + disciplinaAlvo.getNome() + "', o aluno deve ter cursado E APROVADO em ambas as disciplinas '";
            message += preRequisito1.getNome() + "' E '" + preRequisito2.getNome() + "'.";

            if (!cumpriuPreRequisito1ComNota && !cumpriuPreRequisito2ComNota) {
                message += " Nenhuma das duas disciplinas foi atendida.";
            } else if (!cumpriuPreRequisito1ComNota) {
                message += " A disciplina '" + preRequisito1.getNome() + "' não foi atendida.";
            } else {
                message += " A disciplina '" + preRequisito2.getNome() + "' não foi atendida.";
            }

            throw new PreRequisitoNaoCumpridoException(message);
        }
    }
}