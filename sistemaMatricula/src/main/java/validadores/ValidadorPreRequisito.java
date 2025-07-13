package validadores;

import excecoes.*;
import modelo.*;

import java.util.HashMap;

public interface ValidadorPreRequisito {
    void verificarValidador(Aluno aluno, Disciplina disciplinaAlvo, HashMap<Disciplina, String> turmasRejeitadas) throws ValidacaoMatriculaException;
}
