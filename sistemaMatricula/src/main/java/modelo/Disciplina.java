package modelo;
import validadores.ValidadorPreRequisito;
import java.util.List;

public abstract class Disciplina {
    protected String nome;
    protected String codigo;
    protected int cargaHoraria;
    protected List< Disciplina > coRequisitos;
//    protected List<ValidadorPreRequisito>preRequisitos; Não entendi como vai funcionar o ValidadorList

    public Disciplina(String  nome, String codigo, int cargaHoraria){
        this.nome = nome;
        this.codigo = codigo;
        this.cargaHoraria = cargaHoraria;
    }
     // Funções basicas;
    abstract int getPrecendencia();

    protected void adicionarPreRequisito(Disciplina disciplina){
        //Inserir logica do verificador depois;

    }
    protected void adicionarCoRequisito(Disciplina disciplina){
        //Inserir logica do verificador depois;
        coRequisitos.add(disciplina);
    }

}
