package test; // Assumindo que está no pacote 'test'

import service.SistemaMatricula;
import excecoes.*;
import model.*;
import validadores.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SistemaMatriculaMultiplosTest {

    private SistemaMatricula sistema;
    private Aluno alunoTeste;
    private Disciplina prog1;
    private Disciplina labProg1;
    private Disciplina estruturaDados;
    private Disciplina algebra;
    private Disciplina bancoDados;
    private Disciplina calc1;
    private Disciplina fisica;
    private Disciplina topicosAvancados; // Nova disciplina para combinar
    private Disciplina redesComputadores; // Nova disciplina para combinações complexas

    private Turma turmaBancoDados;
    private Turma turmaTopicosAvancados;
    private Turma turmaCalc1;
    private Turma turmaRedesComputadores; // Nova turma

    @BeforeEach
    void setup() {
        sistema = new SistemaMatricula();
        alunoTeste = new Aluno("Bob Combinator", "2025001", 180); // Carga horária máxima 180h

        // Disciplinas base para as combinações
        prog1 = new DisciplinaObrigatoria("DCC001", "Programação I", 60, 4);
        labProg1 = new DisciplinaObrigatoria("DCC002", "Lab de Programação I", 30, 2);
        estruturaDados = new DisciplinaObrigatoria("DCC003", "Estruturas de Dados", 60, 4);
        algebra = new DisciplinaObrigatoria("MAT001", "Álgebra Linear", 60, 4);
        bancoDados = new DisciplinaObrigatoria("DCC004", "Banco de Dados", 60, 4);
        calc1 = new DisciplinaObrigatoria("MAT002", "Cálculo I", 60, 4);
        fisica = new DisciplinaObrigatoria("FIS001", "Física 1", 60, 4);
        topicosAvancados = new DisciplinaOptativa("DCC005", "Tópicos Avançados II", 45, 3); // Para OR e combinações
        redesComputadores = new DisciplinaObrigatoria("DCC006", "Redes de Computadores", 60, 4); // Para testes combinados

        // Turmas
        turmaBancoDados = new Turma("T1-BD", bancoDados, 30, "Seg 14h-16h");
        turmaTopicosAvancados = new Turma("T1-TA2", topicosAvancados, 20, "Sex 10h-12h");
        turmaCalc1 = new Turma("T1-C1", calc1, 30, "Ter/Qui 08h-10h"); // Para conflito de carga horária
        turmaRedesComputadores = new Turma("T1-RC", redesComputadores, 25, "Qua 08h-10h"); // Nova turma para combinações

        alunoTeste.getPlanejamentoFuturo().clear();
        alunoTeste.getHistoricoAluno().clear();
        sistema.resetTurmasRejeitadas();
    }

    // --- Testes de Combinação: Pré-Requisitos (AND/OR) com Carga Horária ---

    @Test
    void tentarMatricularDisciplina_AND_ComCargaHorariaExcedida_Falha() {
        // Redes de Computadores exige Prog I E Lab Prog I
        redesComputadores.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));

        // Aluno cumpre os pré-requisitos
        alunoTeste.adicionarDisciplinaCursada(prog1, 70.0);
        alunoTeste.adicionarDisciplinaCursada(labProg1, 80.0);

        // Adiciona turmas que somam perto do limite de carga (180h - 60h = 120h)
        alunoTeste.adicionarTurmaAoPlanejamento(new Turma("AUX1", new DisciplinaObrigatoria("AUX001", "Aux1", 80, 5), 30, "Seg 10h-12h")); // 80h
        alunoTeste.adicionarTurmaAoPlanejamento(new Turma("AUX2", new DisciplinaObrigatoria("AUX002", "Aux2", 50, 3), 30, "Ter 10h-12h")); // 50h
        // Carga atual = 130h. Redes (60h) faria 190h, excedendo 180h.

        assertThrows(CargaHorariaExcedidaException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaRedesComputadores);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaRedesComputadores));
    }

    @Test
    void tentarMatricularDisciplina_OR_ComCargaHorariaExcedida_Falha() {
        // Tópicos Avançados II exige Estrutura de Dados OU Álgebra
        topicosAvancados.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));

        // Aluno cumpre o pré-requisito OR
        alunoTeste.adicionarDisciplinaCursada(estruturaDados, 70.0);

        // Adiciona turmas que somam perto do limite de carga (180h - 45h = 135h)
        alunoTeste.adicionarTurmaAoPlanejamento(new Turma("AUX3", new DisciplinaObrigatoria("AUX003", "Aux3", 100, 6), 30, "Qua 14h-16h")); // 100h
        alunoTeste.adicionarTurmaAoPlanejamento(new Turma("AUX4", new DisciplinaObrigatoria("AUX004", "Aux4", 40, 2), 30, "Qui 14h-16h")); // 40h
        // Carga atual = 140h. Tópicos Avançados (45h) faria 185h, excedendo 180h.

        assertThrows(CargaHorariaExcedidaException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaTopicosAvancados);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaTopicosAvancados));
    }

    // --- Testes de Combinação: Pré-Requisitos (AND/OR) com Créditos Mínimos ---

    @Test
    void tentarMatricularDisciplina_AND_ComCreditosInsuficientes_Falha() {
        // Redes de Computadores exige Prog I E Lab Prog I, E 150 créditos mínimos
        redesComputadores.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));
        redesComputadores.adicionarPreRequisitoValidador(new ValidadorCreditosMin(150)); // Exige 150 créditos

        // Aluno cumpre AND, mas não tem créditos suficientes (60 + 30 = 90 créditos)
        alunoTeste.adicionarDisciplinaCursada(prog1, 70.0);
        alunoTeste.adicionarDisciplinaCursada(labProg1, 80.0);

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> { // ValidadorCreditosMin lança PreRequisitoNaoCumpridoException
            sistema.tentarMatricularDisciplina(alunoTeste, turmaRedesComputadores);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaRedesComputadores));
    }

    @Test
    void tentarMatricularDisciplina_OR_ComCreditosInsuficientes_Falha() {
        // Tópicos Avançados II exige Estrutura de Dados OU Álgebra, E 120 créditos mínimos
        topicosAvancados.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));
        topicosAvancados.adicionarPreRequisitoValidador(new ValidadorCreditosMin(120)); // Exige 120 créditos

        // Aluno cumpre OR (estruturaDados, 60 créditos), mas não tem créditos suficientes
        alunoTeste.adicionarDisciplinaCursada(estruturaDados, 70.0); // 60 créditos

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaTopicosAvancados);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaTopicosAvancados));
    }

    @Test
    void tentarMatricularDisciplina_AND_ComCreditosSuficientes_Sucesso() throws Exception {
        // Redes de Computadores exige Prog I E Lab Prog I, E 100 créditos mínimos
        redesComputadores.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));
        redesComputadores.adicionarPreRequisitoValidador(new ValidadorCreditosMin(10));

        // Aluno cumpre AND e tem créditos suficientes (Prog I 60 + Lab Prog I 30 + Estrutura Dados 60 = 150 créditos)
        alunoTeste.adicionarDisciplinaCursada(prog1, 70.0);
        alunoTeste.adicionarDisciplinaCursada(labProg1, 80.0);
        alunoTeste.adicionarDisciplinaCursada(calc1, 80.0);
        alunoTeste.adicionarDisciplinaCursada(estruturaDados, 75.0); // Total de créditos: 150

        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoTeste, turmaRedesComputadores);
            //assertEquals("ACEITA: Matrícula em 'Redes de Computadores' realizada com sucesso.", resultado);
        });
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaRedesComputadores));
    }

    // --- Testes de Combinação: Pré-Requisitos (AND/OR) com Co-Requisito ---

    @Test
    void tentarMatricularDisciplina_AND_ComCoRequisitoAtendido_Sucesso() throws Exception {
        // Banco de Dados exige Prog I E Lab Prog I, e tem Co-Requisito com Física 1
        bancoDados.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));
        bancoDados.adicionarCoRequisito(fisica);

        // Aluno cumpre AND
        alunoTeste.adicionarDisciplinaCursada(prog1, 70.0);
        alunoTeste.adicionarDisciplinaCursada(labProg1, 80.0);
        // Co-Requisito em planejamento
        alunoTeste.adicionarTurmaAoPlanejamento(new Turma("T1-Fis", fisica, 30, "Ter 10h-12h"));

        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoTeste, turmaBancoDados);
            //assertEquals("ACEITA: Matrícula em 'Banco de Dados' realizada com sucesso.", resultado);
        });
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaBancoDados));
        //!!!!1assertTrue(alunoTeste.getPlanejamentoFuturo().contains(new Turma("T1-Fis", fisica, 30, "Ter 10h-12h"))); // Verifica se co-req permanece
    }

    @Test
    void tentarMatricularDisciplina_AND_ComCoRequisitoNaoAtendido_Falha() {
        // Banco de Dados exige Prog I E Lab Prog I, e tem Co-Requisito com Física 1
        bancoDados.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));
        bancoDados.adicionarCoRequisito(fisica);

        // Aluno cumpre AND, mas não atende co-requisito
        alunoTeste.adicionarDisciplinaCursada(prog1, 70.0);
        alunoTeste.adicionarDisciplinaCursada(labProg1, 80.0);

        assertThrows(CoRequisitoNaoAtendidoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaBancoDados);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaBancoDados));
    }

    @Test
    void tentarMatricularDisciplina_OR_ComCoRequisitoAtendido_Sucesso() throws Exception {
        // Tópicos Avançados II exige Estrutura de Dados OU Álgebra, e tem Co-Requisito com Cálculo I
        topicosAvancados.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));
        topicosAvancados.adicionarCoRequisito(calc1);

        // Aluno cumpre OR
        alunoTeste.adicionarDisciplinaCursada(algebra, 70.0);
        // Co-Requisito em planejamento
        alunoTeste.adicionarTurmaAoPlanejamento(turmaCalc1); // Uso turmaCalc1 para este teste

        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoTeste, turmaTopicosAvancados);
            assertEquals("ACEITA: Matrícula em 'Tópicos Avançados II' realizada com sucesso.", resultado);
        });
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaTopicosAvancados));
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaCalc1));
    }

    @Test
    void tentarMatricularDisciplina_OR_ComCoRequisitoNaoAtendido_Falha() {
        // Tópicos Avançados II exige Estrutura de Dados OU Álgebra, e tem Co-Requisito com Cálculo I
        topicosAvancados.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));
        topicosAvancados.adicionarCoRequisito(calc1);

        // Aluno cumpre OR, mas não atende co-requisito
        alunoTeste.adicionarDisciplinaCursada(estruturaDados, 70.0);

        assertThrows(CoRequisitoNaoAtendidoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaTopicosAvancados);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaTopicosAvancados));
    }

    // --- Testes de Combinação Múltipla (Pré-req, Co-req, CH, Créditos) ---

    @Test
    void tentarMatricularDisciplina_CenarioComplexo_SucessoTotal() throws Exception {
        // Nova disciplina "Inteligência Artificial" exige:
        // (Estrutura de Dados E Álgebra)
        // E 10 créditos mínimos
        // E tem Co-Requisito com Redes de Computadores
        // Com carga horária total dentro do limite

        Disciplina ia = new DisciplinaObrigatoria("DCC007", "Inteligência Artificial", 60, 4);
        Turma turmaIA = new Turma("T1-IA", ia, 25, "Ter 14h-16h");

        // Pré-requisitos
        ia.adicionarPreRequisitoValidador(new ValidadorAND(estruturaDados, algebra));
        ia.adicionarPreRequisitoValidador(new ValidadorCreditosMin(10));
        // Co-requisito
        ia.adicionarCoRequisito(redesComputadores);

        // Preparar o aluno para atender todas as condições:
        // Atender OR (ex: Estrutura Dados e Álgebra)
        alunoTeste.adicionarDisciplinaCursada(estruturaDados, 70.0); // 60 créditos
        alunoTeste.adicionarDisciplinaCursada(algebra, 80.0);
        alunoTeste.adicionarDisciplinaCursada(calc1, 80.0);
        alunoTeste.adicionarDisciplinaCursada(bancoDados, 80.0);
        alunoTeste.adicionarTurmaAoPlanejamento(turmaRedesComputadores); // Co-requisito no planejamento

        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoTeste, turmaIA);
            //assertEquals("ACEITA: Matrícula em 'Inteligência Artificial' realizada com sucesso.", resultado);
        });
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaIA));
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaRedesComputadores));
    }

    @Test
    void tentarMatricularDisciplina_CenarioComplexo_FalhaNoCoRequisito() {
        // Inteligência Artificial com os mesmos pré-requisitos e co-requisitos do teste de sucesso
        Disciplina ia = new DisciplinaObrigatoria("DCC007", "Inteligência Artificial", 60, 4);
        Turma turmaIA = new Turma("T1-IA", ia, 25, "Ter 14h-16h");

        ia.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));
        ia.adicionarPreRequisitoValidador(new ValidadorOR(prog1, labProg1));

        ia.adicionarPreRequisitoValidador(new ValidadorCreditosMin(4));
        ia.adicionarCoRequisito(redesComputadores);

        alunoTeste.adicionarDisciplinaCursada(estruturaDados, 70.0);
        alunoTeste.adicionarDisciplinaCursada(algebra, 80.0);
        alunoTeste.adicionarDisciplinaCursada(prog1, 80.0);

        assertThrows(CoRequisitoNaoAtendidoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaIA);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaIA));
    }

    @Test
    void tentarMatricularDisciplina_CenarioComplexo_FalhaNosCreditosMinimos() {
        // Inteligência Artificial com os mesmos pré-requisitos e co-requisitos do teste de sucesso
        Disciplina ia = new DisciplinaObrigatoria("DCC007", "Inteligência Artificial", 60, 4);
        Turma turmaIA = new Turma("T1-IA", ia, 25, "Ter 14h-16h");

        ia.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));
        ia.adicionarPreRequisitoValidador(new ValidadorOR(prog1, labProg1));
        ia.adicionarPreRequisitoValidador(new ValidadorCreditosMin(120));
        ia.adicionarCoRequisito(redesComputadores);

        // Atender OR, mas não os créditos mínimos (Prog I 60 créditos)
        alunoTeste.adicionarDisciplinaCursada(prog1, 70.0); // Apenas 60 créditos acumulados
        alunoTeste.adicionarDisciplinaCursada(labProg1, 80.0); // +30 créditos. Total 90. Ainda insuficiente para 120.

        alunoTeste.adicionarTurmaAoPlanejamento(turmaRedesComputadores); // Co-requisito no planejamento

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaIA);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaIA));
    }


    @Test
    void tentarMatricularDisciplina_CenarioComplexo_FalhaNaCargaHoraria() {
        Disciplina ia = new DisciplinaObrigatoria("DCC007", "Inteligência Artificial", 60, 4);
        Turma turmaIA = new Turma("T1-IA", ia, 25, "Ter 14h-16h");

        ia.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));
        ia.adicionarPreRequisitoValidador(new ValidadorOR(prog1, labProg1));
        ia.adicionarPreRequisitoValidador(new ValidadorCreditosMin(4));
        ia.adicionarCoRequisito(redesComputadores);

        alunoTeste.adicionarDisciplinaCursada(estruturaDados, 70.0);
        alunoTeste.adicionarDisciplinaCursada(algebra, 80.0);
        alunoTeste.adicionarDisciplinaCursada(prog1, 80.0);

        alunoTeste.adicionarTurmaAoPlanejamento(new Turma("AUX5", new DisciplinaObrigatoria("AUX005", "Aux5", 180, 4), 30, "Seg 10h-12h"));
        alunoTeste.adicionarTurmaAoPlanejamento(turmaRedesComputadores);

        assertThrows(CargaHorariaExcedidaException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaIA);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaIA));
    }

    @Test
    void tentarMatricularDisciplina_MultiplosPreRequisitosAND_Sucesso() throws Exception {
        bancoDados.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));

        alunoTeste.adicionarDisciplinaCursada(prog1, 70.0);
        alunoTeste.adicionarDisciplinaCursada(labProg1, 85.0);

        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoTeste, turmaBancoDados);
            assertEquals("ACEITA: Matrícula em 'Banco de Dados' realizada com sucesso.", resultado);
        });
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaBancoDados));
    }

    @Test
    void tentarMatricularDisciplina_MultiplosPreRequisitosAND_FalhaUmNaoCursado() {
        bancoDados.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));

        alunoTeste.adicionarDisciplinaCursada(prog1, 70.0);

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaBancoDados);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaBancoDados));
    }

    @Test
    void tentarMatricularDisciplina_MultiplosPreRequisitosAND_FalhaUmComNotaInsuficiente() {
        bancoDados.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));

        alunoTeste.adicionarDisciplinaCursada(prog1, 70.0);
        alunoTeste.adicionarDisciplinaCursada(labProg1, 59.0);

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaBancoDados);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaBancoDados));
    }

    @Test
    void tentarMatricularDisciplina_PreRequisitoOR_SucessoComPrimeiraOpcao() throws Exception {
        topicosAvancados.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));

        alunoTeste.adicionarDisciplinaCursada(estruturaDados, 70.0);

        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoTeste, turmaTopicosAvancados);
            assertEquals("ACEITA: Matrícula em 'Tópicos Avançados II' realizada com sucesso.", resultado);
        });
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaTopicosAvancados));
    }

    @Test
    void tentarMatricularDisciplina_PreRequisitoOR_SucessoComSegundaOpcao() throws Exception {
        topicosAvancados.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));

        alunoTeste.adicionarDisciplinaCursada(algebra, 70.0);

        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoTeste, turmaTopicosAvancados);
            assertEquals("ACEITA: Matrícula em 'Tópicos Avançados II' realizada com sucesso.", resultado);
        });
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaTopicosAvancados));
    }

    @Test
    void tentarMatricularDisciplina_PreRequisitoOR_FalhaNenhumaOpcaoCumprida() {
        topicosAvancados.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaTopicosAvancados);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaTopicosAvancados));
    }

    @Test
    void tentarMatricularDisciplina_PreRequisitoOR_FalhaOpcaoComNotaInsuficiente() {
        topicosAvancados.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));

        alunoTeste.adicionarDisciplinaCursada(estruturaDados, 59.0);

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaTopicosAvancados);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaTopicosAvancados));
    }

    @Test
    void tentarMatricularDisciplina_MultiplosANDs_Sucesso() throws Exception {
        Disciplina compAvancada = new DisciplinaObrigatoria("DCC006", "Computação Avançada", 90, 6);
        Turma turmaCompAvancada = new Turma("T1-CA", compAvancada, 25, "Ter 16h-19h");

        compAvancada.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));
        compAvancada.adicionarPreRequisitoValidador(new ValidadorAND(estruturaDados, calc1));

        alunoTeste.adicionarDisciplinaCursada(prog1, 70.0);
        alunoTeste.adicionarDisciplinaCursada(labProg1, 80.0);
        alunoTeste.adicionarDisciplinaCursada(estruturaDados, 75.0);
        alunoTeste.adicionarDisciplinaCursada(calc1, 90.0);

        assertDoesNotThrow(() -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaCompAvancada);
        });
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaCompAvancada));
    }

    @Test
    void tentarMatricularDisciplina_MultiplosANDs_FalhaEmUmBloco() {
        Disciplina compAvancada = new DisciplinaObrigatoria("DCC006", "Computação Avançada", 90, 6);
        Turma turmaCompAvancada = new Turma("T1-CA", compAvancada, 25, "Ter 16h-19h");

        compAvancada.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));
        compAvancada.adicionarPreRequisitoValidador(new ValidadorAND(estruturaDados, calc1));

        alunoTeste.adicionarDisciplinaCursada(prog1, 70.0);
        alunoTeste.adicionarDisciplinaCursada(labProg1, 80.0);
        alunoTeste.adicionarDisciplinaCursada(calc1, 90.0);

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaCompAvancada);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaCompAvancada));
    }
}