package validadores;
import modelo.Aluno;
import modelo.Disciplina;

public class ValidadorLogicoOR implements ValidadorPreRequisito {
    private Disciplina preRequisito1;
    private Disciplina preRequisito2;

    public ValidadorLogicoOR(Disciplina disciplina1, Disciplina disciplina2) {
        this.preRequisito1 = disciplina1;
        this.preRequisito2 = disciplina2;
    }

    @Override
    public boolean verificarValidador(Aluno aluno) {
        return aluno.verificaAprovado(preRequisito1) || aluno.verificaAprovado(preRequisito2);
    }

    @Override
    public String getMensagemErro(){
        return "Pelo menos um dos pré-requisitos (" + preRequisito1 + " , " + preRequisito2 + ") deve ser cumprido.";
    }
}

