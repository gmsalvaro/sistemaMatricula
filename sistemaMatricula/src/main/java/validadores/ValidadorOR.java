package validadores;

import excecoes.*;
import model.*;

import java.util.HashMap;
import java.util.Map;

public class ValidadorOR implements validadores.ValidadorPreRequisito {
    private Disciplina preRequisito1;
    private Disciplina preRequisito2;

    public ValidadorOR(Disciplina preRequisito1, Disciplina preRequisito2) {
        this.preRequisito1 = preRequisito1;
        this.preRequisito2 = preRequisito2;
    }

    @Override
    public void verificarValidador(Aluno aluno, Disciplina disciplinaDesejada, HashMap<Disciplina, String> turmasRejeitadas) throws ValidacaoMatriculaException {
        if (aluno == null || preRequisito1 == null || preRequisito2 == null)
            throw new ValidacaoMatriculaException("Erro interno: Aluno ou pré-requisitos nulos para validação OR.");

        Map<Disciplina, Double> historicoAluno = aluno.getHistoricoAluno();
        boolean cumpriuPreRequisito1 = false;
        boolean cumpriuPreRequisito2 = false;

        if (historicoAluno.containsKey(preRequisito1) && historicoAluno.get(preRequisito1) >= 60.0)
            cumpriuPreRequisito1 = true;

        if (historicoAluno.containsKey(preRequisito2) && historicoAluno.get(preRequisito2) >= 60.0)
            cumpriuPreRequisito2 = true;

        if (cumpriuPreRequisito1 || cumpriuPreRequisito2)
            return;

        else {
            String mensagem = "Para '" + disciplinaDesejada.getNome() + "', é necessário ter cursado E APROVADO em '" +
                    preRequisito1.getNome() + "' OU '" + preRequisito2.getNome() + "'.";
            mensagem += " Nenhum dos pré-requisitos foi atendido.";
            turmasRejeitadas.put(disciplinaDesejada, mensagem);
            throw new PreRequisitoNaoCumpridoException(mensagem);
        }
    }
}
