package validadores;
import modelo.Aluno;
import modelo.Disciplina;
import java.util.List;

public class ValidadorSimples implements ValidadorPreRequisito{
    protected List<Disciplina> Requisito; // acho que da pra colocar Disciplina sem list já que é uma só !!!!
    public ValidadorSimples(List <Disciplina> Requisito){
        this.Requisito = Requisito;
    }

    boolean verificaValidadorSimples(Aluno aluno){

    }

}
