package validadores;

import excecoes.*;
import model.*;
import java.util.HashMap;
import java.util.List;

public class ValidadorCoRequisito  {

    public void validarCoRequisitos(Aluno aluno, Disciplina disciplinaDesejada, HashMap<Disciplina, String> turmasRejeitadas) throws ValidacaoMatriculaException {
        List<Disciplina> coRequisitos = disciplinaDesejada.getCoRequisitos();
        if (aluno == null || coRequisitos == null)
            throw new ValidacaoMatriculaException("Erro interno: Aluno ou lista de co-requisitos nula.");

        for (Disciplina coRequisitoEsperado : coRequisitos) {
            boolean encontradoCoRequisito = false;
            for (Turma turmaPlanejada : aluno.getPlanejamentoFuturo()) {
                if (turmaPlanejada.getDisciplina().equals(coRequisitoEsperado)) {
                    encontradoCoRequisito = true;
                    break;
                }
            }
            if (!encontradoCoRequisito) {
                String msg = "O co-requisito '" + coRequisitoEsperado.getNome() + "' da disciplina '" +
                        disciplinaDesejada.getNome() + "' não foi selecionado no planejamento futuro do aluno.";
                turmasRejeitadas.put(disciplinaDesejada, msg);
                throw new CoRequisitoNaoAtendidoException(msg);
            }
        }
    }
}