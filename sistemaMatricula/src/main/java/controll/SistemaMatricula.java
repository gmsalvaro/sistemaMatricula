package controll;
import excecoes.CargaHorariaExcedidaException;
import excecoes.CoRequisitoNaoAtendidoException;
import excecoes.ConflitoDeHorarioException;
import excecoes.CreditosInsuficienteException;
import excecoes.PreRequisitoNaoCumpridoException;
import excecoes.TurmaCheiaException;
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
        validarCoRequisitos(aluno, disciplinaAtual.getCoRequisitos());


        resolveConflitoHorario.resolverConflitoHorario(aluno, turmaDesejada, disciplinaAtual);

        turmaDesejada.matricularAluno();
        aluno.adicionarTurmaAoPlanejamento(turmaDesejada);

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
    public void validarCreditosMin(Aluno aluno, Disciplina disciplina) throws CreditosInsuficienteException{
        throw new CreditosInsuficienteException("dsdasd");
    }


    private void validarCargaHoraria(Aluno aluno, Disciplina disciplina) throws CargaHorariaExcedidaException {
        ValidadorCargaHoraria validadorCargaHoraria = new ValidadorCargaHoraria(aluno, disciplina);
        if (!validadorCargaHoraria.validarCargaHoraria()) {
            throw new CargaHorariaExcedidaException(
                    "Carga horária máxima (" + aluno.getCargaHoraria() + "h) excedida ao adicionar '" + disciplina.getNome() + "'.");
        }
    }

    public String gerarRelatorioFinalAluno(Aluno aluno) {
        return geradorRelatorio.gerarRelatorioFinalAluno(aluno);
    }
}