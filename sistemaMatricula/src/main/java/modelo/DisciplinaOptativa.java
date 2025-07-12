package modelo;

public class DisciplinaOptativa extends modelo.Disciplina {
    protected String nome;
    protected String codigo;
    protected int cargaHoraria;

    public DisciplinaOptativa(String nome, String codigo, int cargaHoraria, int numCredito){
        super(nome, codigo, cargaHoraria, numCredito);
    }

    @Override
    public int getPrecedencia(){
        return 1;
    }
}
