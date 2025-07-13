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
    protected Map<Disciplina, Double> historicoAluno;
    protected List<Turma> planejamentoFuturo;

    public Aluno(String nome, String matricula, int creditoMax, int cargaHorariaMax) {
        this.nome = nome;
        this.matricula = matricula;
        this.cargaHorariaMax = cargaHorariaMax;
        this.historicoAluno = new HashMap<>();
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

    public Map<Disciplina, Double> getHistoricoAluno() {
        return historicoAluno;
    }

    public void adicionarDisciplinaCursada(Disciplina disciplina, double nota) {
        if (disciplina != null) {
            historicoAluno.put(disciplina, nota);
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
    public int getCargaHorariaAtualNoPlanejamento() {
        return cargaHorariaAcumulada;
    }

    public int getCreditosAcumulados() {
        return creditoAcumulado;
    }

}