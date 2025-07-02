package modelo;

import java.util.*;


public class Aluno {

    protected String nome;
    protected String matricula;
    protected int cargaHoraria;
    protected Map<Disciplina, Double> disciplinasCursadas;
    protected List<Turma> planejamentoFuturo;

     Aluno(String  nome, String matricula, int cargaHoraria){
        this.nome = nome;
        this.matricula = matricula;
        this.cargaHoraria = cargaHoraria;
        this.disciplinasCursadas = new HashMap<>();
        //List n sei prosseguir
    }

    public String getNome() {
        return nome;
    }

    public int getCreditos() {
        int totalCreditos = 0;
        for (Map.Entry<Disciplina, Double> entry : disciplinasCursadas.entrySet()) {
            Disciplina d = entry.getKey();
            totalCreditos += d.getPrecendencia(); // ou getPrecendencia() se for isso que você quer
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




//    public boolean verificaPreRequisito(Disciplina disciplina){
//         // encontrar com o mapa a disciplina associada ou associadas e verificar ;
//
//    }


}
