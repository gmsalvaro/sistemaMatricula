package validadores;

import modelo.Aluno;
import modelo.Disciplina;
import modelo.Turma;

public class ValidadorCoRequisito  {
    private Disciplina disciplina;
    private Aluno aluno;


    public ValidadorCoRequisito(Aluno aluno, Disciplina disciplina) {
        this.disciplina = disciplina;
        this.aluno = aluno;
    }

    public boolean validarCoRequisitos(){
        for (Disciplina coRequisito : disciplina.getCoRequisitos()) {
            for(Turma turmaPlanejada : aluno.getPlanejamentoFuturo()){
                if(turmaPlanejada.getDisciplina().equals(coRequisito))
                    return true;
            }
        }
        return  false;
    }
}