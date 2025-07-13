package validadores;

import excecoes.*;
import modelo.*;
import java.util.List;

public class ValidadorCoRequisito  {

    public void validarCoRequisitos(Aluno aluno, Disciplina disciplinaAlvo) throws ValidacaoMatriculaException {
        List<Disciplina> coRequisitos = disciplinaAlvo.getCoRequisitos();
        if (aluno == null || coRequisitos == null) {
            throw new ValidacaoMatriculaException("Erro interno: Aluno ou lista de co-requisitos nula.");
        }

        if (coRequisitos.isEmpty()) {
            return;
        }

        for (Disciplina coRequisitoEsperado : coRequisitos) {
            boolean encontradoNoPlano = false;
            for (Turma turmaPlanejada : aluno.getPlanejamentoFuturo()) {
                if (turmaPlanejada.getDisciplina().equals(coRequisitoEsperado)) {
                    encontradoNoPlano = true;
                    break;
                }
            }
            if (!encontradoNoPlano) {
                String message = "O co-requisito '" + coRequisitoEsperado.getNome() + "' da disciplina '" +
                        disciplinaAlvo.getNome() + "' não foi selecionado no planejamento futuro do aluno.";
                throw new CoRequisitoNaoAtendidoException(message);
            }
        }
    }
}