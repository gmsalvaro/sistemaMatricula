package modelo;

public class DisciplinaEletiva extends modelo.Disciplina {
    protected String nome;
    protected String codigo;
    int numCredito;
    protected int cargaHoraria;

    public DisciplinaEletiva(String nome, String codigo, int cargaHoraria, int numCredito){
        super(nome, codigo, cargaHoraria, numCredito);
    }
    @Override
    public int getPrecedencia(){
        return 2;
    }

}
