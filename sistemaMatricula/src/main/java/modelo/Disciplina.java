package modelo;
import validadores.ValidadorPreRequisito;

import java.util.ArrayList;
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
        this.coRequisitos = new ArrayList<>();
    }

    public abstract int getPrecedencia();

    public String getCodigo(){
        return codigo;
    }
    public int getCargaHoraria() {
        return this.cargaHoraria;
    }

    public String getNome(){
        return nome;
    }

    public List<Disciplina> getCoRequisitos() {
        return coRequisitos;
    }

    public void setCoRequisito(Disciplina coRequisito) {
        this.coRequisitos.add(coRequisito);
    }

    public void adicionarCoRequisito(Disciplina disciplina){
        coRequisitos.add(disciplina);
    }

    public void setValidadorPreRequisito(ValidadorPreRequisito validador) {
        this.preRequisito = validador;
    }

    public ValidadorPreRequisito getValidadorPreRequisito(){
            return preRequisito;
    }

}
