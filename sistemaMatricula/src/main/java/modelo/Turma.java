package modelo;
import java.util.List;

public class Turma {
    private String id;
    private Disciplina disciplina;
    private int capacidadeMax;
    private int numMatriculados;
    private String diaSemana;
    private String horario;
    protected List<Aluno> turmaAlunos;

    public Turma(String id, Disciplina disciplina, int capacidadeMaxima, String horario){
        this.id = id;
        this.disciplina = disciplina;
        this.capacidadeMax = capacidadeMaxima;
        this.horario = horario;
        this.numMatriculados = 0;
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

    public int getNumMatriculados() {
        return numMatriculados;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public String getHorario() {
        return horario;
    }

    public void matriculaAluno(Disciplina disciplina, Aluno aluno){
        if(numMatriculados < capacidadeMax) {
            turmaAlunos.add(aluno);
            numMatriculados++;
        } else {
            //execpional;
        }


    }

}
