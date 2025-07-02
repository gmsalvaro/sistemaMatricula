package modelo;

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

    public int getCreditos() {
        int totalCreditos = 0;
        for (Map.Entry<Disciplina, Double> entry : disciplinasCursadas.entrySet()) {
            Disciplina d = entry.getKey();
            if(verificaAprovado(d))
                totalCreditos += d.getPrecendencia();
        }
        return totalCreditos;
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
         if(disciplinasCursadas.get(disciplina) < 60){
             //disparar a execção
         }
         return true;
    }

    //calcular horarioMax também


//    public void calcularCreditos(int numCredito) {
//        int numCreditos = 0;
//        for (Map.Entry<Disciplina, Double> entry : disciplinasCursadas.entrySet()) {
//            Disciplina d = entry.getKey();
//            if(verificaAprovado(d))
//                numCreditos += d.getPrecendencia();
//        }
//         this.numCredito = numCreditos;
//    }

}
