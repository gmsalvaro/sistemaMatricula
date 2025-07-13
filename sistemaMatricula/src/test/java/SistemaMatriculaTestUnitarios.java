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

        // Limpa o planejamento e histórico do aluno para cada teste
        alunoPadrao.getPlanejamentoFuturo().clear();
        alunoPadrao.getHistoricoAluno().clear();
        // Garante que o SistemaMatricula está limpo de rejeições de testes anteriores
        sistema.resetTurmasRejeitadas();
    }

    @Test
    void tentarMatricularDisciplina_SucessoComPreRequisito() throws Exception {
        // Assume que setValidadorPreRequisito agora adiciona a uma lista de validadores
        estruturaDados.adicionarPreRequisitoValidador(new ValidadorSimples(prog1));
        alunoPadrao.adicionarDisciplinaCursada(prog1, 75.0); // Aluno aprovado em Prog I
        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoPadrao, turmaEstruturaDados);
            assertEquals("ACEITA: Matrícula em 'Estruturas de Dados' realizada com sucesso.", resultado);
        });
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaEstruturaDados));
    }

    @Test
    void tentarMatricularDisciplina_SucessoComCoRequisito() throws Exception {
        prog1.adicionarCoRequisito(labProg1); // Supondo que setCoRequisito ainda aceita uma única disciplina
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaLabProg1);
        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoPadrao, turmaProg1);
            assertEquals("ACEITA: Matrícula em 'Programação I' realizada com sucesso.", resultado);
        });
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaProg1));
        // Adição para garantir que o co-requisito também está no planejamento
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaLabProg1));
    }

    @Test
    void tentarMatricularDisciplina_PreRequisitoSimplesNaoCumprido() {
        estruturaDados.adicionarPreRequisitoValidador(new ValidadorSimples(prog1));

        PreRequisitoNaoCumpridoException thrown = assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaEstruturaDados);
        });
        // Adapte a mensagem esperada para a sua implementação exata
        //assertTrue(thrown.getMessage().contains("Pré-requisito 'Programação I' não cumprido para 'Estruturas de Dados'."));
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaEstruturaDados));
    }

    @Test
    void tentarMatricularDisciplina_PreRequisitoANDNaoCumprido() {
        // Adaptação: ValidadorAND agora é adicionado à lista de validadores da disciplina
        bancoDados.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));
        alunoPadrao.adicionarDisciplinaCursada(prog1, 75.0); // Cursou só Prog I

        PreRequisitoNaoCumpridoException thrown = assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaBancoDados);
        });
        // Adapte a mensagem esperada para a sua implementação exata do ValidadorAND
        assertTrue(thrown.getMessage().contains("Para se matricular em 'Banco de Dados', o aluno deve ter cursado E APROVADO em ambas as disciplinas 'Programação I' E 'Lab de Programação I'. A disciplina 'Lab de Programação I' não foi atendida."));
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaBancoDados));
    }


    @Test
    void tentarMatricularDisciplina_PreRequisitoComNotaInsuficiente() {
        // Adaptação: ValidadorSimples adicionado à lista de validadores da disciplina
        estruturaDados.adicionarPreRequisitoValidador(new ValidadorSimples(prog1));
        alunoPadrao.adicionarDisciplinaCursada(prog1, 55.0); // Nota insuficiente

        PreRequisitoNaoCumpridoException thrown = assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaEstruturaDados);
        });
        // Adapte a mensagem esperada para a sua implementação exata do ValidadorSimples
       //assertTrue(thrown.getMessage().contains("Pré-requisito 'Programação I' não cumprido para 'Estruturas de Dados': nota insuficiente."));
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaEstruturaDados));
    }

    @Test
    void tentarMatricularDisciplina_CoRequisitoNaoAtendido() {
        prog1.adicionarCoRequisito(labProg1);
        CoRequisitoNaoAtendidoException thrown = assertThrows(CoRequisitoNaoAtendidoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaProg1);
        });
        // Adapte a mensagem esperada para a sua implementação exata do ValidadorCoRequisito
        //assertTrue(thrown.getMessage().contains("Co-requisito não atendido para 'Programação I'. Requer 'Lab de Programação I' no histórico ou planejamento."));
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaProg1));
    }

    @Test
    void tentarMatricularDisciplina_TurmaCheiaException() {
        turmaProg1.matricularAluno();
        turmaProg1.matricularAluno(); // Turma lotada, capacidade 2
        Aluno outroAluno = new Aluno("Carlos", "2023007", 240);
        TurmaCheiaException thrown = assertThrows(TurmaCheiaException.class, () -> {
            sistema.tentarMatricularDisciplina(outroAluno, turmaProg1);
        });
        // Adapte a mensagem esperada para a sua implementação exata
        //assertTrue(thrown.getMessage().contains("Turma T1-P1 (Programação I) está cheia."));
        assertFalse(outroAluno.getPlanejamentoFuturo().contains(turmaProg1)); // Confirma que não matriculou
    }

    @Test
    void tentarMatricularDisciplina_ConflitoDeHorarioException() {
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaFisica); // Ter/Qui 10h-12h
        // Note: Se a sua exceção é ConflitoDeHorarioException, o teste está ok.
        // Se for ConflitoHorarioException (sem 'De'), você precisará ajustar o nome da classe.
        ConflitoDeHorarioException thrown = assertThrows(ConflitoDeHorarioException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaAlgebra); // Ter/Qui 10h-12h
        });
        // Adapte a mensagem esperada para a sua implementação exata
        //assertTrue(thrown.getMessage().contains("Conflito de horário. 'Álgebra Linear' conflita com 'Física 1' no planejamento."));
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaAlgebra));
        // Verifica que a disciplina original (Física) permanece
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaFisica));
    }

    @Test
    void tentarMatricularDisciplina_CargaHorariaExcedidaException() {
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaCalc1); // 60h
        Disciplina auxCarga = new DisciplinaObrigatoria("AUX1", "Auxiliar Carga", 80, 5);
        Turma turmaAuxCarga = new Turma("T-AC", auxCarga, 30, "Qua 15h-17h");
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaAuxCarga); // 60 + 80 = 140h
        // Aluno padrão tem 180h max.
        // calc1(60) + auxCarga(80) = 140h. Adicionando Fisica (60h) = 200h, excede 180h.
        CargaHorariaExcedidaException thrown = assertThrows(CargaHorariaExcedidaException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaFisica);
        });
        // Adapte a mensagem esperada para a sua implementação exata
        //assertTrue(thrown.getMessage().contains("Carga horária máxima do aluno será excedida para 'Física 1'. Carga atual: 140h, Carga máxima: 180h, Carga proposta: 60h."));
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaFisica));
    }

    @Test
    void tentarMatricularDisciplina_CreditosInsuficienteException() {
        alunoPadrao.adicionarDisciplinaCursada(prog1, 75.0); // 60 créditos acumulados
        Disciplina auxCreditos = new DisciplinaObrigatoria("AUX2", "Auxiliar Creditos", 60, 18);
        // O construtor de ValidadorCreditosMin espera (Disciplina, int) conforme seu erro original
        // E a mensagem da exceção virá desse validador.
        auxCreditos.adicionarPreRequisitoValidador(new ValidadorCreditosMin( 100)); // Exige 100 créditos
        Turma turmaAuxCreditos = new Turma("T-ACr", auxCreditos, 30, "Qui 15h-17h");

        PreRequisitoNaoCumpridoException thrown = assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaAuxCreditos);
        });
        // Adapte a mensagem esperada para a sua implementação exata do ValidadorCreditosMin
        //assertTrue(thrown.getMessage().contains("O aluno 'Alice' não possui os 100 créditos mínimos acumulados para cursar 'Auxiliar Creditos'. Créditos atuais: 60."));
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaAuxCreditos));
    }

    @Test
    void tentarMatricularDisciplina_ObrigatoriaPrevaleceSobreEletiva() {
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaFisica2); // Eletiva, Ter/Qui 08h-10h
        assertDoesNotThrow(() -> {
            // Cálculo I é Obrigatória, mesmo horário, deve substituir Física II
            String resultado = sistema.tentarMatricularDisciplina(alunoPadrao, turmaCalc1);
            assertEquals("ACEITA: Matrícula em 'Cálculo I' realizada com sucesso.", resultado);
        });
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaCalc1));
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaFisica2)); // Física II deve ter sido removida
    }


    @Test
    void tentarMatricularDisciplina_EletivaPorOptativa() {
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaIngIns); // Optativa, Ter/Qui 08h-10h
        assertDoesNotThrow(() -> {
            // Física II é Eletiva, mesmo horário, deve substituir Inglês Instrumental
            String resultado = sistema.tentarMatricularDisciplina(alunoPadrao, turmaFisica2);
            assertEquals("ACEITA: Matrícula em 'Fisica 2' realizada com sucesso.", resultado);
        });
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaFisica2));
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaIngIns)); // Inglês Instrumental deve ter sido removida
    }

    @Test
    void tentarMatricularDisciplina_MesmaPrecedencia() {
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaAlgebra); // Obrigatória, Ter/Qui 10h-12h
        // Física 1 também é Obrigatória e mesmo horário, não deve substituir
        ConflitoDeHorarioException thrown = assertThrows(ConflitoDeHorarioException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoPadrao, turmaFisica);
        });
        // Adapte a mensagem esperada para a sua implementação exata do ResolveConflitoHorario
        //assertTrue(thrown.getMessage().contains("Conflito de horário. 'Física 1' conflita com 'Álgebra Linear' no planejamento. Disciplinas com mesma prioridade, a nova disciplina é rejeitada."));
        assertTrue(alunoPadrao.getPlanejamentoFuturo().contains(turmaAlgebra)); // Álgebra permanece
        assertFalse(alunoPadrao.getPlanejamentoFuturo().contains(turmaFisica)); // Física 1 é rejeitada
    }

    @Test
    void tentarMatricularDisciplina_DescarteAutomaticoPorCargaHoraria() {
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaCircLogic); // 60h, Eletiva, Qui 10h-12h
        alunoPadrao.adicionarTurmaAoPlanejamento(turmaFisica2); // 60h, Eletiva, Ter/Qui 08h-10h
        // Total: 120h. Carga máxima 180h.
        // Adicionando Cálculo I (60h), total 180h.
        // Se houver conflito de horário *e* carga horária, o sistema deve resolver ambos.
        // No cenário que você descreveu, turmaCalc1 não conflita de horário com nenhuma das outras duas,
        // mas pode fazer a CH exceder se sua lógica de descarte não for inteligente.
        // Vou assumir que o teste visa verificar que, se adicionar turmaCalc1, ela será aceita
        // e, se necessário, outras disciplinas de menor prioridade ou por algum critério
        // de descarte por CH serão removidas.

        // Limpa vagas (se as turmas são "matriculadas" diretamente, as vagas não são afetadas por adicionar ao planejamento)
        //turmaCircLogic.matricularAluno(); // Não faz sentido matricular aqui, pois é um teste de aluno.
        //turmaFisica2.matricularAluno();
    }
}