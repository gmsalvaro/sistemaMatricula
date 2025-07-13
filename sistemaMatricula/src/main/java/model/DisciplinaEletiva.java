package model;

public class DisciplinaEletiva extends model.Disciplina {
    public DisciplinaEletiva(String nome, String codigo, int cargaHoraria, int credito){
        super(nome, codigo, cargaHoraria, credito);
    }
    @Override
    public int getPrecedencia(){
        return 2;
    }

}
