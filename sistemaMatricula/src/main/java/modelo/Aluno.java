package modelo;

import java.util.*;


public class Aluno {

    protected String nome;
    protected String matricula;
    protected int cargaHoraria;

    //Como é feito: o Map utiliza o HashMap para poder armazenar os valores Map<V, K> sendo
    // V = Nome , K = Nota na disciplina, V= Algoritmos , K = 87 Por exemplo.
    protected Map<Disciplina, Integer> disciplinasCursadas;

    //Planejamento futuro nao entendi como implementar (Obs: turmas futuras)
    protected List<Turma> planejamentoFuturo;


// COnstrutor do Aluno
     Aluno(String  nome, String matricula, int cargaHoraria){
        this.nome = nome;
        this.matricula = matricula;
        this.cargaHoraria = cargaHoraria;
        this.disciplinasCursadas = new HashMap<>();
        //List n sei prosseguir
    }


    //Getters
    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public Map<Disciplina, Integer> getDisciplinasCursadas() {
        return disciplinasCursadas;
    }



    //Metodos:

    public void adicionarDisciplinaCursada(Disciplina disciplina, int nota) {
        disciplinasCursadas.put(disciplina, nota);
    }

    //Criar verificador se o aluno passou? ou utiliza os validadores?
    public boolean passouDisciplina(Disciplina disciplina){
         return disciplinasCursadas.get(disciplina) >= 60;
    }

}
