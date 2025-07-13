package validadores;

import excecoes.*;
import modelo.*;

import java.util.HashMap;

public class ValidadorSimples implements validadores.ValidadorPreRequisito {
    private Disciplina preRequisito;

    public ValidadorSimples(Disciplina preRequisito) {
        this.preRequisito = preRequisito;
    }

    @Override
    public void verificarValidador(Aluno aluno, Disciplina disciplinaAlvo, HashMap<Disciplina, String> turmasRejeitadas) throws ValidacaoMatriculaException {
        if (aluno == null || preRequisito == null) {
            throw new ValidacaoMatriculaException("Erro interno: Aluno ou pré-requisito nulo para validação simples.");
        }
        if (!aluno.getDisciplinasCursadas().containsKey(preRequisito)) {
            turmasRejeitadas.put(preRequisito, "Pré-requisito '" + preRequisito.getNome() + "' não cursado pelo aluno para '"+ disciplinaAlvo.getNome() +"'." );
            throw new PreRequisitoNaoCumpridoException(
                    "Pré-requisito '" + preRequisito.getNome() + "' não cursado pelo aluno para '"+ disciplinaAlvo.getNome() +"'."
            );
        }
        if (aluno.getDisciplinasCursadas().get(preRequisito) < 60.0) {
            turmasRejeitadas.put(preRequisito, "Nota insuficiente em '" + preRequisito.getNome() + "' para cursar '" + disciplinaAlvo.getNome() + "' (necessário >= 60.0, obtido: " + aluno.getDisciplinasCursadas().get(preRequisito) + ").");
            throw new PreRequisitoNaoCumpridoException(
                    "Nota insuficiente em '" + preRequisito.getNome() + "' para cursar '" + disciplinaAlvo.getNome() + "' (necessário >= 60.0, obtido: " + aluno.getDisciplinasCursadas().get(preRequisito) + ")."
            );
        }
    }
}