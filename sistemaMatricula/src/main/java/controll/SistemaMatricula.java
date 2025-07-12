package controll;
import controll.ResolveConflitoHorario;
import controll.GeradorRelatorio;
import excecoes.CargaHorariaExcedidaException;
import excecoes.CoRequisitoNaoAtendidoException;
import excecoes.ConflitoDeHorarioException;
import excecoes.CreditosInsuficienteException;
import excecoes.PreRequisitoNaoCumpridoException;
import excecoes.TurmaCheiaException;
import validadores.ValidadorCreditos;
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

    public SistemaMatricula() {
        this.resolveConflitoHorario = new ResolveConflitoHorario();
        this.geradorRelatorio = new GeradorRelatorio();
    }

    public String tentarMatricularDisciplina(Aluno aluno, Turma turmaDesejada)
            throws PreRequisitoNaoCumpridoException, CoRequisitoNaoAtendidoException,
            TurmaCheiaException, ConflitoDeHorarioException, CargaHorariaExcedidaException, CreditosInsuficienteException {

        Disciplina disciplinaAtual = turmaDesejada.getDisciplina();
        validarVagas(turmaDesejada, disciplinaAtual);
        validarPreRequisito(aluno, disciplinaAtual);
        validarCargaHoraria(aluno, disciplinaAtual);
        //validarCreditosMaximos(aluno, disciplinaAtual);
        validarCoRequisitos(aluno, disciplinaAtual.getCoRequisitos());
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

    private void validarCoRequisitos(Aluno aluno, List<Disciplina> disciplina) throws CoRequisitoNaoAtendidoException {
            ValidadorCoRequisito validadorCoRequisito = new ValidadorCoRequisito(aluno, disciplina);
        if(!validadorCoRequisito.validarCoRequisitos()){
            throw new CoRequisitoNaoAtendidoException(
                    "Co-requisito   de  não presente no planejamento atual do aluno."
            );

        }
    }

    private void validarPreRequisito(Aluno aluno, Disciplina disciplina) throws PreRequisitoNaoCumpridoException {
        ValidadorPreRequisito validador = disciplina.getValidadorPreRequisito();
        if (validador != null) {
            if (!validador.verificarValidador(aluno)) {
                throw new PreRequisitoNaoCumpridoException(validador.getMensagemErro());
            }
        }
    }

    private void validarCargaHoraria(Aluno aluno, Disciplina disciplina) throws CargaHorariaExcedidaException {
        ValidadorCargaHoraria validadorCargaHoraria = new ValidadorCargaHoraria(aluno, disciplina);
        if (!validadorCargaHoraria.validarCargaHoraria()) {
            throw new CargaHorariaExcedidaException(
                    "Carga horária máxima (" + aluno.getCargaHoraria() + "h) excedida ao adicionar '" + disciplina.getNome() + "'.");
        }
    }

    private void validarCreditosMaximos(Aluno aluno, Disciplina disciplina) throws excecoes.CreditosInsuficienteException {
        ValidadorCreditos validador = new ValidadorCreditos(aluno, disciplina);
        if (!validador.verificarQtdCredito(aluno, disciplina)) {
            throw new excecoes.CreditosInsuficienteException(
                    "Créditos máximos (" + aluno.getCreditoMax() + " créditos) excedidos ao adicionar '" + disciplina.getNome() + "'.");
        }
    }

    public String gerarRelatorioFinalAluno(Aluno aluno) {
        return geradorRelatorio.gerarRelatorioFinalAluno(aluno);
    }
}