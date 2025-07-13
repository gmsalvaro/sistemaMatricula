package validadores;

import excecoes.*;
import modelo.*;

public interface ValidadorPreRequisito {
    void verificarValidador(Aluno aluno, Disciplina disciplinaAlvo) throws ValidacaoMatriculaException;
}
