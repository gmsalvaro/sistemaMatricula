package modelo;

import excecoes.ValidacaoAprovado;

import java.sql.SQLOutput;
import java.util.*;


public class Aluno {
    protected String nome;
    protected String matricula;
    protected int cargaHoraria;
    protected Map<Disciplina, Double> disciplinasCursadas;
    protected int numCredito;
    protected List<Turma> planejamentoFuturo;

     Aluno(String  nome, String matricula){
        this.nome = nome;
        this.matricula = matricula;
        this.disciplinasCursadas = new HashMap<>();

    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public Map<Disciplina, Double> getDisciplinasCursadas() {
        return disciplinasCursadas; // returna o Map
    }

    //Metodos:

    public void adicionarDisciplinaCursada(Disciplina disciplina, double nota) {
        disciplinasCursadas.put(disciplina, nota);
    }

    public boolean verificaAprovado(Disciplina disciplina){
         double nota = disciplinasCursadas.get(disciplina);
         return nota >= 60;
    }

}
