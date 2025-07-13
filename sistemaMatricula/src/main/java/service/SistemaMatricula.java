package service;

import excecoes.*;
import model.*;
import validadores.*;
import java.util.HashMap;
import java.util.List;

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

        validadorCargaHoraria.validarCargaHoraria(aluno, disciplinaAtual, turmasRejeitadas);

        validadorCoRequisito.validarCoRequisitos(aluno, disciplinaAtual, turmasRejeitadas);

        resolveConflitoHorario.resolverConflitoHorario(aluno, turmaDesejada, disciplinaAtual, turmasRejeitadas);

        turmaDesejada.matricularAluno();
        aluno.adicionarTurmaAoPlanejamento(turmaDesejada);
        return "ACEITA: Matrícula em '" + disciplinaAtual.getNome() + "' realizada com sucesso.";
    }

    private void validarVagas(Turma turma, Disciplina disciplina, HashMap<Disciplina, String> turmasRejeitadas) throws TurmaCheiaException {
        if (turma.verificaCheio()) {
            String mensagem = "Turma cheia.";
            turmasRejeitadas.put(disciplina, mensagem);
            throw new TurmaCheiaException(mensagem);
        }
    }

    private void validarPreRequisito(Aluno aluno, Disciplina disciplina, HashMap<Disciplina, String> turmasRejeitadas) throws ValidacaoMatriculaException {
        List<ValidadorPreRequisito> validadores = disciplina.getPreRequisitosValidadores();
        if (!validadores.isEmpty()) {
            for(ValidadorPreRequisito validadorPreRequisito : validadores) {
                validadorPreRequisito.verificarValidador(aluno, disciplina, turmasRejeitadas);
            }
        }
    }

    public String gerarRelatorioFinalAluno(Aluno aluno) {
        return geradorRelatorio.gerarRelatorioFinalAluno(aluno, turmasRejeitadas);
    }

    public void resetTurmasRejeitadas() {
        this.turmasRejeitadas.clear();
    }
}