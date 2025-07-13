package controller;

import excecoes.MatriculaException;
import model.*;
import service.SistemaMatricula;

import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        {
            SistemaMatricula sistema = new SistemaMatricula();
            HashMap<Disciplina, String> turmasRejeitadas = new HashMap<>();

            Aluno aluno = new Aluno("Roberto Carlos", "2024003", 250);

            Disciplina introProg = new DisciplinaObrigatoria("INP001","Introdução à Programação", 60, 4);
            Disciplina calculo2 = new DisciplinaObrigatoria("MAT002", "Cálculo II", 90, 4);
            Disciplina engSoft = new DisciplinaEletiva("ENG001", "Engenharia de Software", 60, 4);
            Disciplina graficos = new DisciplinaOptativa("CPG001", "Computação Gráfica", 30, 2);
            Disciplina fis2 = new DisciplinaObrigatoria("FIS002","Física II", 60, 4);

            Turma turmaIntroProg = new Turma("T1-A", introProg,  30, "SEG/QUA 10:00-12:00");
            Turma turmaCalculo2 = new Turma("T2-B", calculo2, 25, "TER/QUI 08:00-11:00");
            Turma turmaEngSoft = new Turma("T3-C", engSoft, 40, "SEG/QUA 10:00-12:00");
            Turma turmaGraficos = new Turma("T4-D", graficos, 35, "SEX 14:00-16:00");
            Turma turmaFis2 = new Turma("T5-E", fis2, 20, "TER/QUI 08:00-11:00");

            List<Disciplina> todasDisciplinas = List.of(introProg, calculo2, engSoft, graficos, fis2);
            List<Turma> todasTurmas = List.of(turmaIntroProg, turmaCalculo2, turmaEngSoft, turmaGraficos, turmaFis2);

            List<Turma> turmasDesejadas = List.of(
                    turmaIntroProg,
                    turmaGraficos,
                    turmaCalculo2,
                    turmaEngSoft,
                    turmaFis2
            );
            for (Turma turma : turmasDesejadas) {
                try {
                    String resultado = sistema.tentarMatricularDisciplina(aluno, turma);
                    System.out.println(resultado);
                } catch (MatriculaException e) {
                    turmasRejeitadas.put(turma.getDisciplina(), e.getMessage());
                    System.out.println("REJEITADA: " + turma.getDisciplina().getNome() + " -> " + e.getMessage());
                }
            }
            System.out.println(" ");
            String relatorio = sistema.gerarRelatorioFinalAluno(aluno);
            System.out.println(relatorio);
        }
    }
}