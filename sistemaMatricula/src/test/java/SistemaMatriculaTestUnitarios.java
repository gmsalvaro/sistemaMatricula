import service.*;
import excecoes.*;
import model.*;
import validadores.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SistemaMatriculaTestUnitarios {
    private SistemaMatricula sistema;

    private Aluno alunoPadrao;
    private Disciplina prog1;
    private Disciplina labProg1;
    private Disciplina estruturaDados;
    private Disciplina algebra;
    private Disciplina bancoDados;
    private Disciplina calc1;
    private Disciplina fisica;
    private Disciplina fisica2;
    private Disciplina inglesIns;
    private Disciplina circLogic;

    private Turma turmaProg1;
    private Turma turmaLabProg1;
    private Turma turmaEstruturaDados;
    private Turma turmaAlgebra;
    private Turma turmaBancoDados;
    private Turma turmaCalc1;
    private Turma turmaFisica;
    private Turma turmaFisica2;
    private Turma turmaIngIns;
    private Turma turmaCircLogic;


    @BeforeEach
    void setup() {
        sistema = new SistemaMatricula();

        alunoPadrao = new Aluno("Alice", "2023001", 180);

        prog1 = new DisciplinaObrigatoria("DCC001", "Programação I", 60, 4);
        labProg1 = new DisciplinaObrigatoria("DCC002", "Lab de Programação I", 30, 2);
        estruturaDados = new DisciplinaObrigatoria("DCC003", "Estruturas de Dados", 60, 4);
        algebra = new DisciplinaObrigatoria("MAT001", "Álgebra Linear", 60, 4);
        bancoDados = new DisciplinaObrigatoria("DCC004", "Banco de Dados", 60, 4);
        calc1 = new DisciplinaObrigatoria("MAT002", "Cálculo I", 60, 4);
        fisica = new DisciplinaObrigatoria("FIS001", "Física 1", 60, 4);
        fisica2 = new DisciplinaEletiva("FIS002", "Fisica 2", 60, 4);
        inglesIns = new DisciplinaOptativa("ENG101", "Ingles Instrumental", 45, 4 );
        circLogic = new DisciplinaEletiva("CEL001", "Circuitos Logicos", 60, 4);

        turmaProg1 = new Turma("T1-P1", prog1, 2, "Seg/Qua 08h-10h");
        turmaLabProg1 = new Turma("T1-LP1", labProg1, 30, "Seg/Qua 10h-12h");
        turmaEstruturaDados = new Turma("T1-ED", estruturaDados, 30, "Sex 08h-12h");
        turmaAlgebra = new Turma("T1-AL", algebra, 30, "Ter/Qui 10h-12h");
        turmaBancoDados = new Turma("T1-BD", bancoDados, 30, "Seg 14h-16h");
        turmaCalc1 = new Turma("T1-C1", calc1, 30, "Ter/Qui 08h-10h");
        turmaFisica = new Turma("T1-F1", fisica, 30, "Ter/Qui 10h-12h");
        turmaFisica2 = new Turma("T1-F2", fisica2, 30, "Ter/Qui 08h-10h");
        turmaIngIns = new Turma ("T1-II", inglesIns, 15, "Ter/Qui 08h-10h");
        turmaCircLogic = new Turma ("T1-CL", circLogic, 20, "Qui 10h-12h");

        alunoPadrao.getPlanejamentoFuturo().clear();
        alunoPadrao.getHistoricoAluno().clear();
        sistema.resetTurmasRejeitadas();
    }

    @Test
    void tentarMatricularDisciplina_SucessoComPreRequisitoSimples() throws Exception {
        estruturaDados.adicionarPreRequisitoValidador(new ValidadorSimples(prog1));
        alunoPadrao.adicionarDisciplinaCursada(prog1, 75.0); // Aluno aprovado em Prog I
        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoPadrao, turmaEstruturaDados);
            assertEquals("ACEITA: Matrícula em 'Estruturas de Dados' realizada com sucesso.", resultado);
        });
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaEstruturaDados));
    }

    @Test
    void tentarMatricularDisciplina_SucessoComPreRequisitoAND() throws Exception {
        estruturaDados.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));

        alunoPadrao.adicionarDisciplinaCursada(prog1, 75.0);
        alunoPadrao.adicionarDisciplinaCursada(labProg1, 75.0);
        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoPadrao, turmaEstruturaDados);
            assertEquals("ACEITA: Matrícula em 'Estruturas de Dados' realizada com sucesso.", resultado);
        });
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaEstruturaDados));
    }

    @Test
    void tentarMatricularDisciplina_SucessoComPreRequisitoOR() throws Exception {
        fisica2.adicionarPreRequisitoValidador(new ValidadorOR(calc1, fisica));

        alunoPadrao.adicionarDisciplinaCursada(calc1, 75.0);

        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoPadrao, turmaFisica2);
            assertEquals("ACEITA: Matrícula em 'Fisica 2' realizada com sucesso.", resultado);
        });
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaFisica2));
    }

    @Test
    void tentarMatricularDisciplina_CreditosSuficiente() {
        alunoPadrao.adicionarDisciplinaCursada(prog1, 75.0);
        alunoPadrao.adicionarDisciplinaCursada(calc1, 75.0);
        alunoPadrao.adicionarDisciplinaCursada(fisica, 75.0);
        alunoPadrao.adicionarDisciplinaCursada(fisica2, 75.0);
        alunoPadrao.adicionarDisciplinaCursada(labProg1, 75.0);

        estruturaDados.adicionarPreRequisitoValidador(new ValidadorCreditosMin(10));

        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoPadrao, turmaEstruturaDados);
            assertEquals("ACEITA: Matrícula em 'Estruturas de Dados' realizada com sucesso.", resultado);
        });
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaEstruturaDados));
    }

    @Test
    void tentarMatricularDisciplina_SucessoComCoRequisito() throws Exception {
        prog1.adicionarCoRequisito(labProg1);

        alunoPadrao.adicionarTurmaAoPlanejamento(turmaLabProg1);

        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoPadrao, turmaProg1);
            assertEquals("ACEITA: Matrícula em 'Programação I' realizada com sucesso.", resultado);
        });
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaProg1));
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaLabProg1));
    }

    @Test
    void tentarMatricularDisciplina_PreRequisitoSimplesNaoCumprido() {
        estruturaDados.adicionarPreRequisitoValidador(new ValidadorSimples(prog1));

        PreRequisitoNaoCumpridoException thrown = assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaEstruturaDados);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaEstruturaDados));
    }

    @Test
    void tentarMatricularDisciplina_PreRequisitoANDNaoCumprido() {
        bancoDados.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));
        alunoPadrao.adicionarDisciplinaCursada(prog1, 75.0); // Cursou só Prog I

        PreRequisitoNaoCumpridoException thrown = assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaBancoDados);
        });
        assertTrue(thrown.getMessage().contains("Para se matricular em 'Banco de Dados', o aluno deve ter cursado E APROVADO em ambas as disciplinas 'Programação I' E 'Lab de Programação I'. A disciplina 'Lab de Programação I' não foi atendida."));
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaBancoDados));
    }

    @Test
    void tentarMatricularDisciplina_PreRequisitoORNaoCumprido() throws Exception {
        fisica2.adicionarPreRequisitoValidador(new ValidadorOR(calc1, fisica));

        alunoPadrao.adicionarDisciplinaCursada(prog1, 75.0);
        alunoPadrao.adicionarDisciplinaCursada(labProg1, 75.0);

        PreRequisitoNaoCumpridoException thrown = assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaFisica2);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaFisica2));
    }

    @Test
    void tentarMatricularDisciplina_PreRequisitoComNotaInsuficiente() {
        estruturaDados.adicionarPreRequisitoValidador(new ValidadorSimples(prog1));
        alunoPadrao.adicionarDisciplinaCursada(prog1, 55.0); // Nota insuficiente
        PreRequisitoNaoCumpridoException thrown = assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaEstruturaDados);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaEstruturaDados));
    }

    @Test
    void tentarMatricularDisciplina_CoRequisitoNaoAtendido() {
        prog1.adicionarCoRequisito(labProg1);
        CoRequisitoNaoAtendidoException thrown = assertThrows(CoRequisitoNaoAtendidoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaProg1);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaProg1));
    }

    @Test
    void tentarMatricularDisciplina_TurmaCheiaException() {
        turmaProg1.matricularAluno();
        turmaProg1.matricularAluno();
        Aluno outroAluno = new Aluno("Carlos", "2023007", 240);
        TurmaCheiaException thrown = assertThrows(TurmaCheiaException.class, () -> {
            sistema.tentarMatricularDisciplina(outroAluno, turmaProg1);
        });
        assertFalse(outroAluno.getPlanejamentoFuturo().contains(turmaProg1));
    }

    @Test
    void tentarMatricularDisciplina_ConflitoDeHorarioException() {
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaFisica);

        ConflitoDeHorarioException thrown = assertThrows(ConflitoDeHorarioException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaAlgebra);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaAlgebra));
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaFisica));
    }

    @Test
    void tentarMatricularDisciplina_CargaHorariaExcedidaException() {
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaCalc1);
        Disciplina auxCarga = new DisciplinaObrigatoria("AUX1", "Auxiliar Carga", 80, 5);
        Turma turmaAuxCarga = new Turma("T-AC", auxCarga, 30, "Qua 15h-17h");
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaAuxCarga);
        CargaHorariaExcedidaException thrown = assertThrows(CargaHorariaExcedidaException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaFisica);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaFisica));
    }

    @Test
    void tentarMatricularDisciplina_CreditosInsuficiente() {
        alunoPadrao.adicionarDisciplinaCursada(prog1, 75.0);
        Disciplina auxCreditos = new DisciplinaObrigatoria("Auxiliar Creditos","AUX2", 60, 18);
        auxCreditos.adicionarPreRequisitoValidador(new ValidadorCreditosMin( 20));
        Turma turmaAuxCreditos = new Turma("Aux", auxCreditos, 30, "Qui 15h-17h");

        PreRequisitoNaoCumpridoException thrown = assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaAuxCreditos);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaAuxCreditos));
    }

    @Test
    void tentarMatricularDisciplina_ObrigatoriaPrevaleceSobreEletiva() {
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaFisica2);
        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoPadrao, turmaCalc1);
            assertEquals("ACEITA: Matrícula em 'Cálculo I' realizada com sucesso.", resultado);
        });
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaCalc1));
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaFisica2));
    }


    @Test
    void tentarMatricularDisciplina_EletivaPorOptativa() {
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaIngIns);
        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoPadrao, turmaFisica2);
            assertEquals("ACEITA: Matrícula em 'Fisica 2' realizada com sucesso.", resultado);
        });
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaFisica2));
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaIngIns));
    }

    @Test
    void tentarMatricularDisciplina_MesmaPrecedencia() {
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaAlgebra);
        ConflitoDeHorarioException thrown = assertThrows(ConflitoDeHorarioException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaFisica);
        });

        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaAlgebra));
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaFisica));
    }

    //@Test
//    void tentarMatricularDisciplina_DescarteAutomaticoPorCargaHoraria() {
//        alunoPadrao.adicionarTurmaAoPlanejamento(turmaCircLogic);
//        alunoPadrao.adicionarTurmaAoPlanejamento(turmaFisica2);
//
//    }
}