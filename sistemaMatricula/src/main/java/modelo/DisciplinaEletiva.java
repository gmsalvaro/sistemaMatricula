package modelo;

public class DisciplinaEletiva extends Disciplina{
    protected String nome;
    protected String codigo;
    protected int cargaHoraria;

    public DisciplinaEletiva(String codigo, String nome, int cargaHoraria){
        super(nome, codigo, cargaHoraria);
    }
    public int getPrecendencia(){
        return 2;
    }
}
