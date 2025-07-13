package model;


import validadores.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Disciplina {
    protected String nome;
    protected String codigo;
    protected int cargaHoraria;
    protected int credito;
    protected List<Disciplina> coRequisitos;
    protected List<ValidadorPreRequisito> preRequisitosValidadores;

    public Disciplina(String codigo, String nome, int cargaHoraria, int credito){
        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.credito = credito;
        this.coRequisitos = new ArrayList<>(); // Inicializa a lista de co-requisitos
        this.preRequisitosValidadores = new ArrayList<>();
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

    public int getCredito(){
        return credito;
    }

    public List<Disciplina> getCoRequisitos() {
        return coRequisitos;
    }
    public List<ValidadorPreRequisito> getPreRequisitosValidadores() {
        return preRequisitosValidadores;
    }

    public void adicionarCoRequisito(Disciplina coRequisito) { // Renomeado para clareza
        if (coRequisito != null) {
            this.coRequisitos.add(coRequisito);
        }
    }

    public void adicionarPreRequisitoValidador(ValidadorPreRequisito validador) {
        if (validador != null) {
            this.preRequisitosValidadores.add(validador);
        }
    }
}