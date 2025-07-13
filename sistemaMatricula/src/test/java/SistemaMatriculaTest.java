import service.*;
import excecoes.*;
import model.*;
import validadores.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SistemaMatriculaTest {
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
    private Disciplina circLogic;
    private Disciplina inglesIns;

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

        alunoPadrao = new Aluno("Alice", "2023001", 24, 180);

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
    }

    @Test
    void tentarMatricularDisciplina_SucessoComPreRequisito() throws Exception {
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

    @Test
    void tentarMatricularDisciplina_PreRequisitoSimplesNaoCumprido() {
        estruturaDados.setValidadorPreRequisito(new ValidadorSimples(prog1));

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaEstruturaDados);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaEstruturaDados));
    }

    @Test
    void tentarMatricularDisciplina_PreRequisitoANDNaoCumprido() {
        List<validadores.ValidadorPreRequisito> validadores = new ArrayList<>();
        ValidadorAND validadorAND = new ValidadorAND(prog1, labProg1);
        bancoDados.setValidadorPreRequisito(validadorAND);
        alunoPadrao.adicionarDisciplinaCursada(prog1, 75.0); // Cursou só Prog I
        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaBancoDados);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaBancoDados));
    }


    @Test
    void tentarMatricularDisciplina_PreRequisitoComNotaInsuficiente() {
        ValidadorSimples validadorSimples = new ValidadorSimples(prog1);
        estruturaDados.setValidadorPreRequisito((ValidadorPreRequisito) validadorSimples);
        alunoPadrao.adicionarDisciplinaCursada(prog1, 55.0);
        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaEstruturaDados);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaEstruturaDados));
    }

    @Test
    void tentarMatricularDisciplina_CoRequisitoNaoAtendido() {
        prog1.setCoRequisito(labProg1);
        assertThrows(CoRequisitoNaoAtendidoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaProg1);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaProg1));
    }

    @Test
    void tentarMatricularDisciplina_TurmaCheiaException() {
        turmaProg1.matricularAluno();
        turmaProg1.matricularAluno();
        Aluno outroAluno = new Aluno("Carlos", "2023007", 120, 240);
        assertThrows(TurmaCheiaException.class, () -> {
            sistema.tentarMatricularDisciplina(outroAluno, turmaProg1);
        });
        assertFalse(outroAluno.getPlanejamentoFuturo().contains(turmaProg1)); // Confirma que não matriculou
    }

    @Test
    void tentarMatricularDisciplina_ConflitoDeHorarioException() {
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaFisica);
        assertThrows(ConflitoDeHorarioException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaAlgebra);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaAlgebra));
    }

    @Test
    void tentarMatricularDisciplina_CargaHorariaExcedidaException() {
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaCalc1);
        Disciplina auxCarga = new DisciplinaObrigatoria("AUX1", "Auxiliar Carga", 80, 5);
        Turma turmaAuxCarga = new Turma("T-AC", auxCarga, 30, "Qua 15h-17h");
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaAuxCarga);
        assertThrows(CargaHorariaExcedidaException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaFisica);
        });
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaFisica));
    }

    @Test
    void tentarMatricularDisciplina_CreditosInsuficienteException() {
        alunoPadrao.adicionarDisciplinaCursada(prog1, 75.0);
        Disciplina auxCreditos = new DisciplinaObrigatoria("AUX2", "Auxiliar Creditos", 60, 18);
        validadores.ValidadorCreditosMin validadorCreditosMin = new validadores.ValidadorCreditosMin(auxCreditos, 10);
        auxCreditos.setValidadorPreRequisito(validadorCreditosMin);
        Turma turmaAuxCreditos = new Turma("T-ACr", auxCreditos, 30, "Qui 15h-17h");
        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
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
        assertThrows(ConflitoDeHorarioException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaFisica); // Ter/Qui 10h-12h
        });
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaAlgebra));
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaFisica));
    }

    @Test
    void tentarMatricularDisciplina_DescarteAutomaticoPorCargaHoraria(){
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaCircLogic);
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaFisica2);
        turmaCircLogic.matricularAluno();
        turmaFisica2.matricularAluno();

        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoPadrao, turmaCalc1);
            assertEquals("ACEITA: Matrícula em 'Cálculo I' realizada com sucesso.", resultado);
        });
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaCalc1));
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaCircLogic));
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaFisica2));

    }

}

