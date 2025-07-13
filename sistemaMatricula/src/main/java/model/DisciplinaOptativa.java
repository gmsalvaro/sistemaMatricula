package model;

public class DisciplinaOptativa extends model.Disciplina {
    public DisciplinaOptativa(String nome, String codigo, int cargaHoraria, int credito){
        super(nome, codigo, cargaHoraria, credito);
    }
    @Override
    public int getPrecedencia(){
        return 1;
    }
}
