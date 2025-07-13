package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Aluno {
    protected String nome;
    protected String matricula;
    protected int cargaHorariaMax;
    protected int cargaHorariaAcumulada = 0;
    protected int creditoAcumulado = 0;
    protected Map<Disciplina, Double> disciplinasCursadas;
    protected List<Turma> planejamentoFuturo;

    public Aluno(String nome, String matricula, int creditoMax, int cargaHorariaMax) {
        this.nome = nome;
        this.matricula = matricula;
        this.cargaHorariaMax = cargaHorariaMax;
        this.disciplinasCursadas = new HashMap<>();
        this.planejamentoFuturo = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public int getCargaHorariaMax() {
        return cargaHorariaMax;
    }

    public List<Turma> getPlanejamentoFuturo() {
        return planejamentoFuturo;
    }

    public Map<Disciplina, Double> getDisciplinasCursadas() {
        return disciplinasCursadas; // Retorna o Map
    }

    public void adicionarDisciplinaCursada(Disciplina disciplina, double nota) {
        if (disciplina != null) {
            disciplinasCursadas.put(disciplina, nota);
            if(nota >= 60)
                creditoAcumulado += disciplina.getCredito();
        }
    }

    public void adicionarTurmaAoPlanejamento(Turma turma) {
        if (turma != null && !this.planejamentoFuturo.contains(turma)) {
            this.planejamentoFuturo.add(turma);
            cargaHorariaAcumulada += turma.getDisciplina().cargaHoraria;
        }
    }

    public void removerTurmaDoPlanejamento(Turma turma) {
        if (turma != null) {
            this.planejamentoFuturo.remove(turma);
            cargaHorariaAcumulada -= turma.getDisciplina().cargaHoraria;
        }
    }
    public int getCargaHorariaAtualNoPlanejamento() { // Novo método
//        int cargaHorariaAtual = 0;
//        for (Turma turma : planejamentoFuturo) {
//            cargaHorariaAtual += turma.getDisciplina().getCargaHoraria(); // Soma carga horária
//        }
//        return cargaHorariaAtual;
        return cargaHorariaAcumulada;
    }

    public int getCreditosAcumulados() {
        return creditoAcumulado;
    }

}