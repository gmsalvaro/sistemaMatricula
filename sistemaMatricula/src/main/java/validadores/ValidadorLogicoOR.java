package validadores;
import modelo.Aluno;
import modelo.Disciplina;

public class ValidadorLogicoOR implements ValidadorPreRequisito {
    private Disciplina disciplina1;
    private Disciplina disciplina2;

    public ValidadorLogicoOR(Disciplina disciplina1, Disciplina disciplina2) {
        this.disciplina1 = disciplina1;
        this.disciplina2 = disciplina2;
    }

    @Override
    public boolean verificarValidador(Aluno aluno) {
        return aluno.verificaAprovado(disciplina1) || aluno.verificaAprovado(disciplina2);
    }
}

