package validadores;
import modelo.Aluno;
import modelo.Disciplina;

public class ValidadorSimples implements ValidadorPreRequisito{
    private Disciplina disciplina1;

    public ValidadorSimples(Disciplina disciplina1, Disciplina disciplina2) {
        this.disciplina1 = disciplina1;
    }

    @Override
    public boolean verificarValidador(Aluno aluno) {
        return aluno.verificaAprovado(disciplina1);
    }
}
