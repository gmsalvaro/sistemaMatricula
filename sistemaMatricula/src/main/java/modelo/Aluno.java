package modelo;

import modelo.Disciplina;
import modelo.Turma;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Aluno {
    protected String nome;
    protected String matricula;
    protected int cargaHorariaMax;
    protected Map<Disciplina, Double> disciplinasCursadas;
    protected List<Turma> planejamentoFuturo;

    public Aluno(String nome, String matricula, int cargaHorariaMax) {
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
}