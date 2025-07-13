package model;
import validadores.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Disciplina {
    protected String nome;
    protected String codigo;
    protected int cargaHoraria;
    protected int credito;
    protected List< Disciplina > coRequisitos;
    protected ValidadorPreRequisito preRequisito;

    public Disciplina(String  codigo, String nome, int cargaHoraria, int credito){
        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.credito = credito;
        coRequisitos = new ArrayList<>();
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

    public void setValidadorPreRequisito(ValidadorPreRequisito validador) {
        this.preRequisito = validador;
    }

    public ValidadorPreRequisito getValidadorPreRequisito(){
            return preRequisito;
    }

    public int getCredito(){ return credito; }

}
