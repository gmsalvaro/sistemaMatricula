package validadores;
import modelo.Aluno;
import modelo.Disciplina;
import excecoes.ValidacaoMatriculaException;

public interface ValidadorPreRequisito {
    void verificarValidador(Aluno aluno, Disciplina disciplinaAlvo) throws ValidacaoMatriculaException;
}
