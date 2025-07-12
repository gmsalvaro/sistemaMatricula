package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.Disciplina;
import modelo.Turma;

public class Aluno {
    protected String nome;
    protected String matricula;
    protected int cargaHorariaMax;
    protected int numCredito = 0;
    protected int creditoMax = 0;
    protected Map<Disciplina, Double> disciplinasCursadas;
    protected List<Turma> planejamentoFuturo;

    public Aluno(String nome, String matricula, int creditoMax) {
        this.nome = nome;
        this.matricula = matricula;
        this.creditoMax = creditoMax;
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
        }
    }

    public boolean cumpriuPreRequisito(Disciplina disciplinaPreRequisito) {
        return disciplinasCursadas.containsKey(disciplinaPreRequisito) &&
                disciplinasCursadas.get(disciplinaPreRequisito) >= 60.0;
    }

    public void adicionarTurmaAoPlanejamento(Turma turma) {
        if (turma != null && !this.planejamentoFuturo.contains(turma)) {
            this.planejamentoFuturo.add(turma);
        }
    }

    public void removerTurmaDoPlanejamento(Turma turma) {
        if (turma != null) {
            this.planejamentoFuturo.remove(turma);
        }
    }
        public int getCargaHorariaAtualNoPlanejamento() { // Novo método
        int cargaHorariaAtual = 0;
        for (Turma turma : planejamentoFuturo) {
            cargaHorariaAtual += turma.getDisciplina().getCargaHoraria(); // Soma carga horária
        }
        return cargaHorariaAtual;
    }

    public int getCargaHorariaM(){
        return cargaHorariaMax;
    }

    public int getCreditoMax(){
        return creditoMax;
    }

    public int getCreditosAcumulados() {
        int creditosAcumulados = 0;
        for (Map.Entry<Disciplina, Double> d : disciplinasCursadas.entrySet()) {
            if (d.getValue() >= 60.0) {
                creditosAcumulados += d.getKey().getNumCredito();
            }
        }
        return creditosAcumulados;
    }

}