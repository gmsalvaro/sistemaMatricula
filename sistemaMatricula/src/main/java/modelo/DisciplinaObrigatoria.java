package modelo;

import java.util.List;

public class DisciplinaObrigatoria extends Disciplina {
    protected String nome;
    protected String codigo;
    protected int cargaHoraria;

    public DisciplinaObrigatoria(String codigo, String nome, int cargaHoraria){
        super(nome, codigo, cargaHoraria);
    }

    public int getPrecendencia(){
        return 3;
    }
}
