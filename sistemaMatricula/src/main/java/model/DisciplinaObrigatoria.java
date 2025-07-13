package model;

public class DisciplinaObrigatoria extends model.Disciplina {
    public DisciplinaObrigatoria(String nome, String codigo, int cargaHoraria, int credito){
        super(nome, codigo, cargaHoraria, credito);
    }
    @Override
    public int getPrecedencia(){
        return 3;
    }
}
