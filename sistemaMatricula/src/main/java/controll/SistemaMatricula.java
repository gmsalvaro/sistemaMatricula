package controll;

import excecoes.*;
import modelo.*;
import validadores.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SistemaMatricula {
    private ResolveConflitoHorario resolveConflitoHorario;
    private GeradorRelatorio geradorRelatorio;
    private HashMap<Disciplina, String> turmasRejeitadas = new HashMap<>();
    private ValidadorCargaHoraria validadorCargaHoraria;
    private ValidadorCoRequisito validadorCoRequisito;

    public SistemaMatricula() {
        this.resolveConflitoHorario = new ResolveConflitoHorario();
        this.geradorRelatorio = new GeradorRelatorio();
        this.validadorCargaHoraria = new ValidadorCargaHoraria();
        this.validadorCoRequisito = new ValidadorCoRequisito();
    }

    public String tentarMatricularDisciplina(Aluno aluno, Turma turmaDesejada)
            throws MatriculaException {
        Disciplina disciplinaAtual = turmaDesejada.getDisciplina();
        validarVagas(turmaDesejada, disciplinaAtual, turmasRejeitadas);
        validarPreRequisito(aluno, disciplinaAtual, turmasRejeitadas);
        validadorCargaHoraria.validarCargaHoraria(aluno, disciplinaAtual, turmaDesejada, turmasRejeitadas);
        validadorCoRequisito.validarCoRequisitos(aluno, disciplinaAtual, turmaDesejada, turmasRejeitadas);
        resolveConflitoHorario.resolverConflitoHorario(aluno, turmaDesejada, disciplinaAtual, turmasRejeitadas);
        turmaDesejada.matricularAluno();
        aluno.adicionarTurmaAoPlanejamento(turmaDesejada);
        return "ACEITA: Matrícula em '" + disciplinaAtual.getNome() + "' realizada com sucesso.";
    }

    private void validarVagas(Turma turma, Disciplina disciplina, HashMap<Disciplina, String> turmasRejeitadas) throws TurmaCheiaException {
        if (turma.isCheia()) {
            turmasRejeitadas.put(disciplina,"Turma " + turma.getId() + " (" + disciplina.getNome() + ") está cheia.");
            throw new TurmaCheiaException("Turma " + turma.getId() + " (" + disciplina.getNome() + ") está cheia.");
        }
    }

    private void validarPreRequisito(Aluno aluno, Disciplina disciplina, HashMap<Disciplina, String> turmasRejeitadas) throws ValidacaoMatriculaException {
        ValidadorPreRequisito validador = disciplina.getValidadorPreRequisito();
        if (validador != null) {
            validador.verificarValidador(aluno, disciplina, turmasRejeitadas);
        }
    }

    public String gerarRelatorioFinalAluno(Aluno aluno) {
        return geradorRelatorio.gerarRelatorioFinalAluno(aluno, turmasRejeitadas);
    }
}