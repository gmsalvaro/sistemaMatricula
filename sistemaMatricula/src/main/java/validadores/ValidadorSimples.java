package validadores;
import modelo.Aluno;
import modelo.Disciplina;

public class ValidadorSimples implements ValidadorPreRequisito{
    private final Disciplina preRequisito;

    public ValidadorSimples(Disciplina disciplina1) {
        this.preRequisito = disciplina1;
    }

    @Override
    public boolean verificarValidador(Aluno aluno) {
        return aluno.verificaAprovado(preRequisito);
    }


    @Override
    public String getMensagemErro(){
        return "Todos os pré-requisitos (" + preRequisito + ") devem ser cumpridos.";
    }


}
