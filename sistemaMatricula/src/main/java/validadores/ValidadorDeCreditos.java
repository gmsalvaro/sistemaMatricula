package validadores;

import modelo.Aluno;
import modelo.Disciplina;

public abstract class ValidadorDeCreditos implements ValidadorPreRequisito{
    private Disciplina disciplina1;
    private int numCredito;

    public void ValidadorLogicoDeCreditos(Disciplina disciplina1, int numCredito) {
        this.disciplina1 = disciplina1;
        this.numCredito = numCredito;
    }

    @Override
    public boolean verificarValidador(Aluno aluno) {
        return aluno.getCreditos() >= numCredito;
    }



}
