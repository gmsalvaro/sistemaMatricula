package controll;

import excecoes.MatriculaException;
import excecoes.ValidacaoMatriculaException;
import excecoes.CargaHorariaExcedidaException;
import excecoes.CoRequisitoNaoAtendidoException;
import excecoes.ConflitoDeHorarioException;
import excecoes.PreRequisitoNaoCumpridoException;
import excecoes.TurmaCheiaException;
import excecoes.CreditosInsuficienteException;
import validadores.ValidadorCoRequisito;
import validadores.ValidadorCargaHoraria;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.Turma;
import validadores.ValidadorPreRequisito;

import java.util.List;

public class SistemaMatricula {
    private ResolveConflitoHorario resolveConflitoHorario;
    private GeradorRelatorio geradorRelatorio;

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
        validarVagas(turmaDesejada, disciplinaAtual);
        validarPreRequisito(aluno, disciplinaAtual);
        validadorCargaHoraria.validarCargaHoraria(aluno, disciplinaAtual);
        validadorCoRequisito.validarCoRequisitos(aluno, disciplinaAtual);
        resolveConflitoHorario.resolverConflitoHorario(aluno, turmaDesejada, disciplinaAtual);
        aluno.adicionarTurmaAoPlanejamento(turmaDesejada);
        turmaDesejada.matricularAluno();
        return "ACEITA: Matrícula em '" + disciplinaAtual.getNome() + "' realizada com sucesso.";
    }

    private void validarVagas(Turma turma, Disciplina disciplina) throws TurmaCheiaException {
        if (turma.isCheia()) {
            throw new TurmaCheiaException("Turma " + turma.getId() + " (" + disciplina.getNome() + ") está cheia.");
        }
    }

    private void validarPreRequisito(Aluno aluno, Disciplina disciplina) throws ValidacaoMatriculaException {
        ValidadorPreRequisito validador = disciplina.getValidadorPreRequisito();
        if (validador != null) {
            validador.verificarValidador(aluno, disciplina);
        }
    }

    public String gerarRelatorioFinalAluno(Aluno aluno) {
        return geradorRelatorio.gerarRelatorioFinalAluno(aluno);
    }
}