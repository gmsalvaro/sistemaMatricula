import controll.SistemaMatricula;
import excecoes.CargaHorariaExcedidaException;
import excecoes.CoRequisitoNaoAtendidoException;
import excecoes.ConflitoDeHorarioException;
import excecoes.CreditosInsuficienteException;
import excecoes.PreRequisitoNaoCumpridoException;
import excecoes.TurmaCheiaException;
import validadores.ValidadorAND;
import validadores.ValidadorOR;
import validadores.ValidadorCargaHoraria;
import validadores.ValidadorCoRequisito;
import validadores.ValidadorCoRequisito;
import validadores.ValidadorSimples;
import validadores.ValidadorAND;
import validadores.ValidadorPreRequisito;

import validadores.ValidadorPreRequisito;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.DisciplinaObrigatoria; // Supondo que você usa essa classe
import modelo.Turma;
import org.junit.jupiter.api.BeforeEach; // Importar BeforeEach
import org.junit.jupiter.api.Test;
import validadores.*;
import validadores.ValidadorAND;
import validadores.ValidadorCoRequisito;
import validadores.ValidadorOR;
import validadores.ValidadorSimples;

import java.util.Arrays; // Para Arrays.asList
import java.util.List;
import java.util.ArrayList; // Para ArrayList

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SistemaMatriculaTest {

    private SistemaMatricula sistema;
    private Aluno alunoPadrao;

    // Disciplinas
    private Disciplina prog1;
    private Disciplina labProg1;
    private Disciplina estruturaDados;
    private Disciplina algebra;
    private Disciplina bancoDados;
    private Disciplina calc1;
    private Disciplina fisica;

    // Turmas
    private Turma turmaProg1;
    private Turma turmaLabProg1;
    private Turma turmaEstruturaDados;
    private Turma turmaAlgebra;
    private Turma turmaBancoDados;
    private Turma turmaCalc1;
    private Turma turmaFisica;


    @BeforeEach // Este método será executado antes de CADA teste
    void setup() {
        sistema = new SistemaMatricula();

        // Inicialização do aluno padrão com limites razoáveis para os testes
        alunoPadrao = new Aluno("Alice", "2023001", 120, 240); // Ex: 120 créditos máx, 240h carga horária máx
        alunoPadrao.setCreditoMax(24); // Definindo um limite de créditos mais apertado para testes específicos
        alunoPadrao.setCargaHorariaMax(180); // Definindo uma carga horária mais apertada para testes específicos


        // Inicialização das Disciplinas (código, nome, cargaHoraria, créditos)
        prog1 = new DisciplinaObrigatoria("DCC001", "Programação I", 60, 4);
        labProg1 = new DisciplinaObrigatoria("DCC002", "Lab de Programação I", 30, 2);
        estruturaDados = new DisciplinaObrigatoria("DCC003", "Estruturas de Dados", 60, 4);
        algebra = new DisciplinaObrigatoria("MAT001", "Álgebra Linear", 60, 4);
        bancoDados = new DisciplinaObrigatoria("DCC004", "Banco de Dados", 60, 4);
        calc1 = new DisciplinaObrigatoria("MAT002", "Cálculo I", 60, 4);
        fisica = new DisciplinaObrigatoria("FIS001", "Física Geral", 60, 4);

        // Inicialização das Turmas (nome da turma, disciplina, capacidade, horario)
        // Ajustei capacidades para facilitar testes de turma cheia e horários para conflito
        turmaProg1 = new Turma("T1-P1", prog1, 2, "Seg/Qua 08h-10h"); // Capacidade 2 para teste de cheia
        turmaLabProg1 = new Turma("T1-LP1", labProg1, 30, "Seg/Qua 08h-10h"); // Mesmo horário da Prog1 para conflito
        turmaEstruturaDados = new Turma("T1-ED", estruturaDados, 30, "Sex 08h-12h");
        turmaAlgebra = new Turma("T1-AL", algebra, 30, "Ter/Qui 10h-12h");
        turmaBancoDados = new Turma("T1-BD", bancoDados, 30, "Seg 14h-16h");
        turmaCalc1 = new Turma("T1-C1", calc1, 30, "Ter/Qui 08h-10h");
        turmaFisica = new Turma("T1-F1", fisica, 30, "Ter/Qui 10h-12h"); // Conflita com Algebra
    }

    // --- Testes de Sucesso ---

    @Test
    void tentarMatricularDisciplina_SucessoComPreRequisito() throws Exception {
        // Cenário: Aluno possui o pré-requisito e tenta matricular disciplina
        estruturaDados.setValidadorPreRequisito((validadores.ValidadorPreRequisito) new ValidadorSimples(prog1));
        alunoPadrao.adicionarDisciplinaCursada(prog1, 75.0); // Aluno aprovado em Prog I

        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoPadrao, turmaEstruturaDados);
            assertEquals("ACEITA: Matrícula em 'Estruturas de Dados' realizada com sucesso.", resultado);
        });
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaEstruturaDados));
    }

    @Test
    void tentarMatricularDisciplina_SucessoComCoRequisito() throws Exception {

        prog1.setCoRequisito(labProg1);
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaLabProg1);

        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoPadrao, turmaProg1);
            assertEquals("ACEITA: Matrícula em 'Programação I' realizada com sucesso.", resultado);
        });
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaProg1));
    }

    // --- Testes de Falha (Exceções) ---

    @Test
    void tentarMatricularDisciplina_PreRequisitoSimplesNaoCumprido() {
        // Cenário: Estruturas de Dados exige Programação I. Aluno não cursou Prog I.
        estruturaDados.setValidadorPreRequisito(new ValidadorSimples(prog1));

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaEstruturaDados);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaEstruturaDados));
    }

    @Test
    void tentarMatricularDisciplina_PreRequisitoANDNaoCumprido() {
        // Cenário: Banco de Dados exige Prog1 E LabProg1. Aluno só tem Prog1.
         List<validadores.ValidadorPreRequisito> validadores = new ArrayList<>();
         ValidadorAND validadorAND = new ValidadorAND(prog1,labProg1);
        bancoDados.setValidadorPreRequisito(validadorAND);
        alunoPadrao.adicionarDisciplinaCursada(prog1, 75.0); // Cursou só Prog I

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaBancoDados);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaBancoDados));
    }

//    void tentarMatricularDisciplina_PreRequisitoORNaoCumprido() {
//        // Cenário: Estruturas de Dados exige Prog1 OU Algebra. Aluno não tem nenhuma.
//        estruturaDados.setValidadorPreRequisito(new ValidadorOR(
//                new ValidadorSimples(prog1),
//                new ValidadorSimples(algebra)
//        ));
//
//        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
//            sistema.tentarMatriculaDisciplina(alunoPadrao, turmaEstruturaDados);
//        });
//        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaEstruturaDados));
//    }

    @Test
    void tentarMatricularDisciplina_PreRequisitoComNotaInsuficiente() {
        // Cenário: Estruturas de Dados exige Programação I. Aluno cursou Prog I, mas reprovou.
        ValidadorSimples validadorSimples = new ValidadorSimples(prog1);
        estruturaDados.setValidadorPreRequisito((ValidadorPreRequisito) validadorSimples);
        alunoPadrao.adicionarDisciplinaCursada(prog1, 55.0); // Reprovado (nota < 60, assumindo)

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaEstruturaDados);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaEstruturaDados));
    }


    @Test
    void tentarMatricularDisciplina_CoRequisitoNaoAtendido() {
        // Cenário: Programação I tem Lab de Programação I como co-requisito.
        // Aluno não tem Lab Prog I no planejamento futuro nem cursado.

        prog1.setCoRequisito(labProg1);
        assertThrows(CoRequisitoNaoAtendidoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaProg1);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaProg1));
    }

    @Test
    void tentarMatricularDisciplina_TurmaCheiaException() {
        // Cenário: Turma de Programação I tem capacidade 2. Matriculamos 2 alunos.
        // Um terceiro aluno tenta matricular.
        turmaProg1.matricularAluno(); // Simula matrícula do aluno 1
        turmaProg1.matricularAluno(); // Simula matrícula do aluno 2 (turma cheia)

        // Tenta matricular um novo aluno em uma turma cheia
        Aluno outroAluno = new Aluno("Carlos", "2023007", 120, 240);

        assertThrows(TurmaCheiaException.class, () -> {
            sistema.tentarMatricularDisciplina(outroAluno, turmaProg1);
        });
        assertFalse(outroAluno.getPlanejamentoFuturo().contains(turmaProg1)); // Confirma que não matriculou
    }

    @Test
    void tentarMatricularDisciplina_ConflitoDeHorarioException() {
        // Cenário: Aluno já matriculado em Lab Prog I (Seg/Qua 08h-10h).
        // Tenta matricular em Prog I (também Seg/Qua 08h-10h).
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaLabProg1);

        assertThrows(ConflitoDeHorarioException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaProg1);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaProg1));
    }

    @Test
    void tentarMatricularDisciplina_CargaHorariaExcedidaException() {
        // Cenário: Aluno tem carga horária máxima de 180h.
        // Já tem Cálculo I (60h). Tenta matricular Física Geral (60h)
        // e outra disciplina auxiliar (80h) para exceder o limite.
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaCalc1); // Carga atual: 60h

        // Adiciona uma disciplina auxiliar para chegar perto do limite
        Disciplina auxCarga = new DisciplinaObrigatoria("AUX1", "Auxiliar Carga", 80, 5);
        Turma turmaAuxCarga = new Turma("T-AC", auxCarga, 30, "Qua 15h-17h");
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaAuxCarga); // Carga atual: 60 + 80 = 140h

        // Tenta matricular Física Geral (60h). Total seria 140 + 60 = 200h, excede 180h.
        assertThrows(CargaHorariaExcedidaException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaFisica);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaFisica));
    }

    @Test
    void tentarMatricularDisciplina_CreditosInsuficienteException() {
        // Cenário: Aluno tem limite de créditos de 24.
        // Já tem Cálculo I (4 créditos).
        // Adiciona uma disciplina auxiliar (18 créditos). Total 4 + 18 = 22.
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaCalc1); // Créditos atuais: 4

        // Adiciona uma disciplina auxiliar para chegar perto do limite
        Disciplina auxCreditos = new DisciplinaObrigatoria("AUX2", "Auxiliar Creditos", 30, 18);
        Turma turmaAuxCreditos = new Turma("T-ACr", auxCreditos, 30, "Qui 15h-17h");
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaAuxCreditos); // Créditos atuais: 4 + 18 = 22

        // Tenta matricular Álgebra Linear (4 créditos). Total seria 22 + 4 = 26, excede 24.
        assertThrows(CreditosInsuficienteException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaAlgebra);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaAlgebra));
    }
}
