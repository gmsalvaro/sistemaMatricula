package validadores;

import excecoes.*;
import model.*;

import java.util.HashMap;

public interface ValidadorPreRequisito {
    void verificarValidador(Aluno aluno, Disciplina disciplinaDesejada, HashMap<Disciplina, String> turmasRejeitadas) throws ValidacaoMatriculaException;
}
