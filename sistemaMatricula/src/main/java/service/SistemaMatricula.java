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
            throws MatriculaException { // A exceção pode ser mais específica aqui
        Disciplina disciplinaAtual = turmaDesejada.getDisciplina();

        // 1. Validação de Vagas
        validarVagas(turmaDesejada, disciplinaAtual, turmasRejeitadas);

        // 2. Validação de Pré-Requisito
        validarPreRequisito(aluno, disciplinaAtual, turmasRejeitadas); // Este método chamará os validadores

        // 3. Validação de Carga Horária
        validadorCargaHoraria.validarCargaHoraria(aluno, disciplinaAtual, turmasRejeitadas);

        // 4. Validação de Co-Requisitos
        validadorCoRequisito.validarCoRequisitos(aluno, disciplinaAtual, turmasRejeitadas);

        // 5. Resolução de Conflito de Horário
        // A lógica aqui deve lançar ConflitoHorarioException com uma mensagem clara
        resolveConflitoHorario.resolverConflitoHorario(aluno, turmaDesejada, disciplinaAtual, turmasRejeitadas);

        // Se tudo passou, matricular o aluno
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