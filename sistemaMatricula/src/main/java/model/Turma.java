package model;

import java.util.ArrayList;
import java.util.List;

public class Turma {
    private String id;
    private Disciplina disciplina;
    private int capacidadeMax;
    private int numMatriculados;
    private String horario;
    protected List<Aluno> alunosTurma;

    public Turma(String id, Disciplina disciplina, int capacidadeMax, String horario) {
        this.id = id;
        this.disciplina = disciplina;
        this.capacidadeMax = capacidadeMax;
        this.horario = horario;
        this.numMatriculados = 0;
        this.alunosTurma = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public int getCapacidadeMax() {
        return capacidadeMax;
    }

    public int getNumMatriculados() { return numMatriculados; }

    public String getHorario() {
        return horario;
    }

    public boolean verificaCheio() {
        return numMatriculados >= capacidadeMax;
    }

    public void matricularAluno() {
        this.numMatriculados++;
    }

    public void desmatricularAluno() {
        if (this.numMatriculados > 0) {
            this.numMatriculados--;
        }
    }
}
