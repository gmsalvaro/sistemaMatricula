package modelo;
import modelo.Disciplina;

public class DisciplinaEletiva extends Disciplina{
    protected String nome;
    protected String codigo;
    protected int cargaHoraria;

    public DisciplinaEletiva(String codigo, String nome, int cargaHoraria){
        super(nome, codigo, cargaHoraria);
    }
    @Override
    public int getPrecedencia(){
        return 2;
    }

}
