package validadores;

import modelo.Aluno;
import modelo.Disciplina;
import excecoes.ValidacaoMatriculaException;
import excecoes.PreRequisitoNaoCumpridoException;
import java.util.Map;

public class ValidadorOR implements validadores.ValidadorPreRequisito {
    private Disciplina preRequisito1;
    private Disciplina preRequisito2;

    public ValidadorOR(Disciplina preRequisito1, Disciplina preRequisito2) {
        this.preRequisito1 = preRequisito1;
        this.preRequisito2 = preRequisito2;
    }

    @Override
    public void verificarValidador(Aluno aluno, Disciplina disciplinaAlvo) throws ValidacaoMatriculaException {
        // Validação inicial para evitar NullPointerException
        if (aluno == null || preRequisito1 == null || preRequisito2 == null) {
            throw new ValidacaoMatriculaException("Erro interno: Aluno ou pré-requisitos nulos para validação OR.");
        }

        Map<Disciplina, Double> disciplinasCursadasMap = aluno.getDisciplinasCursadas();
        boolean cumpriuPreRequisito1ComNota = false;
        boolean cumpriuPreRequisito2ComNota = false;

        if (disciplinasCursadasMap.containsKey(preRequisito1) &&
                disciplinasCursadasMap.get(preRequisito1) >= 60.0) {
            cumpriuPreRequisito1ComNota = true;
        }

        // Verifica o segundo pré-requisito (se cursou e se a nota é suficiente)
        if (disciplinasCursadasMap.containsKey(preRequisito2) &&
                disciplinasCursadasMap.get(preRequisito2) >= 60.0) {
            cumpriuPreRequisito2ComNota = true;
        }

        if (cumpriuPreRequisito1ComNota || cumpriuPreRequisito2ComNota) {
            return;
        } else {
            String mensagem = "Para '" + disciplinaAlvo.getNome() + "', é necessário ter cursado E APROVADO em '" +
                    preRequisito1.getNome() + "' OU '" + preRequisito2.getNome() + "'.";

            if (!cumpriuPreRequisito1ComNota && !cumpriuPreRequisito2ComNota) {
                mensagem += " Nenhum dos pré-requisitos foi atendido.";
            } else if (!cumpriuPreRequisito1ComNota) {
                mensagem += " '" + preRequisito1.getNome() + "' não foi atendido.";
            } else { // !cumpriuPreRequisito2ComNota
                mensagem += " '" + preRequisito2.getNome() + "' não foi atendido.";
            }
            throw new PreRequisitoNaoCumpridoException(mensagem);
        }
    }
}
