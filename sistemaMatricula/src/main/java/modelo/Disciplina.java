package modelo;
import validadores.ValidadorPreRequisito;
import java.util.List;

public abstract class Disciplina {
    protected String nome;
    protected String codigo;
    protected int cargaHoraria;
    protected List< Disciplina > coRequisitos;
    protected ValidadorPreRequisito preRequisito; // Apenas uma condição

    public Disciplina(String  nome, String codigo, int cargaHoraria){
        this.nome = nome;
        this.codigo = codigo;
        this.cargaHoraria = cargaHoraria;
    }

    abstract int getPrecendencia();

    public int getCargaHoraria() {
        return this.cargaHoraria;
    }

    public List<Disciplina> getCoRequisitos() {
        return coRequisitos;
    }


    protected void adicionarPreRequisito(Disciplina disciplina){



    }
    protected void adicionarCoRequisito(Disciplina disciplina){
        //Inserir logica do verificador depois;
        coRequisitos.add(disciplina);
    }

}
