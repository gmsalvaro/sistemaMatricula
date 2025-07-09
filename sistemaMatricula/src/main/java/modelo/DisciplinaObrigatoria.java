package modelo;
import modelo.Disciplina;

import java.util.List;

public class DisciplinaObrigatoria extends Disciplina {
    protected String nome;
    protected String codigo;
    protected int cargaHoraria;

    public DisciplinaObrigatoria(String nome, String codigo, int cargaHoraria){
        super(nome, codigo, cargaHoraria);
    }
    @Override
    public int getPrecedencia(){
        return 3;
    }
}
