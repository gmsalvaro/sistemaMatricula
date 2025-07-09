package Controle;

import Controle.GeradorRelatorio;
import Controle.ResolveConflitoHorario;
import excecoes.CargaHorariaExcedidaException;
import excecoes.CoRequisitoNaoAtendidoException;
import excecoes.ConflitoDeHorarioException;
import excecoes.MatriculaException;
import excecoes.PreRequisitoNaoCumpridoException;
import excecoes.TurmaCheiaException;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.Turma;
import validadores.ValidadorPreRequisito;

public class SistemaMatricula {
    private ResolveConflitoHorario resolveConflitoHorario;
    private GeradorRelatorio geradorRelatorio;

    public SistemaMatricula() {
        this.resolveConflitoHorario = new ResolveConflitoHorario();
        this.geradorRelatorio = new GeradorRelatorio();
    }

    /*
    public String tentarMatricularDisciplina(Aluno aluno, Turma turmaDesejada) {
        if (aluno == null || turmaDesejada == null) {
            return "REJEITADA: Aluno ou turma não podem ser nulos.";
        }

        Disciplina disciplinaAtual = turmaDesejada.getDisciplina();

        try {
            validarVagas(turmaDesejada, disciplinaAtual);
            validarPreRequisito(aluno, disciplinaAtual); // Usa os validadores AND/OR/Simples
            validarCoRequisitos(aluno, disciplinaAtual);
            validarCargaHoraria(aluno, disciplinaAtual); // Valida a carga horária MÁXIMA por semestre

            resolveConflitoHorario.resolverConflitoHorario(aluno, turmaDesejada, disciplinaAtual);

            aluno.adicionarTurmaAoPlanejamento(turmaDesejada);
            turmaDesejada.matricularAluno();
            return "ACEITA: Matrícula em '" + disciplinaAtual.getNome() + "' realizada com sucesso.";


            // Implementaçao errada, esta matando o codigo na execuçao do mesmo!

        } catch (PreRequisitoNaoCumpridoException e) {
            throw new RuntimeException(e);
        } catch (CoRequisitoNaoAtendidoException e) {
            throw new RuntimeException(e);
        } catch (TurmaCheiaException e) {
            throw new RuntimeException(e);
        } catch (ConflitoDeHorarioException e) {
            throw new RuntimeException(e);
        } catch (CargaHorariaExcedidaException e) {
            throw new RuntimeException(e);
        }
    }
     */

    public String tentarMatricularDisciplina(Aluno aluno, Turma turmaDesejada)
            throws PreRequisitoNaoCumpridoException, CoRequisitoNaoAtendidoException,
            TurmaCheiaException, ConflitoDeHorarioException, CargaHorariaExcedidaException {
        if (aluno == null || turmaDesejada == null) {
            return "REJEITADA: Aluno ou turma não podem ser nulos.";
        }

        Disciplina disciplinaAtual = turmaDesejada.getDisciplina();

        validarVagas(turmaDesejada, disciplinaAtual);
        validarPreRequisito(aluno, disciplinaAtual);
        validarCoRequisitos(aluno, disciplinaAtual);
        validarCargaHoraria(aluno, disciplinaAtual);

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

    private void validarPreRequisito(Aluno aluno, Disciplina disciplina) throws PreRequisitoNaoCumpridoException {
        ValidadorPreRequisito validador = disciplina.getValidadorPreRequisito();
        if (validador != null) {
            if (!validador.verificarValidador(aluno)) {
                throw new PreRequisitoNaoCumpridoException(validador.getMensagemErro());

            }
        }
    }

    private void validarCoRequisitos(Aluno aluno, Disciplina disciplina) throws CoRequisitoNaoAtendidoException {
        for (Disciplina coRequisito : disciplina.getCoRequisitos()) {
            boolean coRequisitoNoPlano = aluno.getPlanejamentoFuturo().stream()
                    .anyMatch(t -> t.getDisciplina().equals(coRequisito));
            if (!coRequisitoNoPlano) {
                throw new CoRequisitoNaoAtendidoException(
                        "Co-requisito '" + coRequisito.getNome() + "' de '" + disciplina.getNome() + "' não presente no planejamento atual do aluno."
                );
            }
        }
    }

    private void validarCargaHoraria(Aluno aluno, Disciplina disciplina) throws CargaHorariaExcedidaException {
        int cargaHorariaAcumuladaDoAluno = aluno.getPlanejamentoFuturo().stream()
                .mapToInt(t -> t.getDisciplina().getCargaHoraria())
                .sum();
        int novaCargaHoraria = cargaHorariaAcumuladaDoAluno + disciplina.getCargaHoraria();
        if (novaCargaHoraria > aluno.getCargaHorariaMax()) {
            throw new CargaHorariaExcedidaException(
                    "Carga horária máxima (" + aluno.getCargaHorariaMax() + "h) excedida ao adicionar '" + disciplina.getNome() + "'.");
        }
    }

    public String gerarRelatorioFinalAluno(Aluno aluno) {
        return geradorRelatorio.gerarRelatorioFinalAluno(aluno);
    }
}