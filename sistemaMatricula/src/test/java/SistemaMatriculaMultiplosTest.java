import service.*;
import excecoes.*;
import model.*;
import validadores.*;
import org.junit.jupiter.api.*;
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
    private Disciplina topicosAvancados;
    private Disciplina redesComputadores;
    private Disciplina ia;
    private Disciplina fisica2;
    private Disciplina calc2;
    private Disciplina compAvancada;


    private Turma turmaBancoDados;
    private Turma turmaTopicosAvancados;
    private Turma turmaCalc1;
    private Turma turmaRedesComputadores;
    private Turma turmaFisica;
    private Turma turmaIA;
    private Turma turmaFisica2;
    private Turma turmaCalc2;
    private Turma turmaCompAvancada;

    @BeforeEach
    void setup() {
        sistema = new SistemaMatricula();
        alunoTeste = new Aluno("Julio", "2025001", 180); // Carga horária máxima 180h

        //Disciplinas
        prog1 = new DisciplinaObrigatoria("DCC001", "Programação I", 60, 4);
        labProg1 = new DisciplinaObrigatoria("DCC002", "Lab de Programação I", 30, 2);
        estruturaDados = new DisciplinaObrigatoria("DCC003", "Estruturas de Dados", 60, 4);
        algebra = new DisciplinaObrigatoria("MAT001", "Álgebra Linear", 60, 4);
        bancoDados = new DisciplinaObrigatoria("DCC004", "Banco de Dados", 60, 4);
        calc1 = new DisciplinaObrigatoria("MAT002", "Cálculo I", 60, 4);
        fisica = new DisciplinaObrigatoria("FIS001", "Física 1", 60, 4);
        topicosAvancados = new DisciplinaOptativa("DCC005", "Tópicos Avançados II", 45, 3);
        redesComputadores = new DisciplinaObrigatoria("DCC006", "Redes de Computadores", 60, 4);
        ia = new DisciplinaObrigatoria("DCC007", "Inteligência Artificial", 60, 4);
        fisica2 = new DisciplinaObrigatoria("FIS001", "Fisica II", 60, 4);
        calc2 = new DisciplinaObrigatoria("MAT003", "CÁlculo II", 60, 4);
        compAvancada = new DisciplinaObrigatoria("DCC006", "Computação Avançada", 90, 6);


        //Turmas
        turmaBancoDados = new Turma("T1-BD", bancoDados, 30, "Seg 14h-16h");
        turmaTopicosAvancados = new Turma("T1-TA2", topicosAvancados, 20, "Sex 10h-12h");
        turmaCalc1 = new Turma("T1-C1", calc1, 30, "Ter/Qui 08h-10h"); // Para conflito de carga horária
        turmaRedesComputadores = new Turma("T1-RC", redesComputadores, 25, "Qua 08h-10h"); // Nova turma para combinações
        turmaFisica = new Turma("T1-F1", fisica, 30, "Ter/Qui 10h-12h");
        turmaFisica2 = new Turma("T1-F2", fisica2, 30, "Ter/Qui 08h-10h");
        turmaIA = new Turma("T1-IA", ia, 25, "Ter 14h-16h");
        turmaCalc2 = new Turma("T1-C2", calc2, 20, "Ter 14h-16h");
        turmaCompAvancada = new Turma("T1-CA", compAvancada, 25, "Ter 16h-19h");

        alunoTeste.getPlanejamentoFuturo().clear();
        alunoTeste.getHistoricoAluno().clear();
        sistema.resetTurmasRejeitadas();
    }






    @Test
    void tentarMatricularDisciplina_AND_ComCargaHorariaExcedida_Falha() {
        redesComputadores.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));

        alunoTeste.adicionarDisciplinaCursada(prog1, 70.0);
        alunoTeste.adicionarDisciplinaCursada(labProg1, 80.0);

        alunoTeste.adicionarTurmaAoPlanejamento(new Turma("AUX1", new DisciplinaObrigatoria("AUX001", "Aux1", 80, 5), 30, "Seg 10h-12h")); // 80h
        alunoTeste.adicionarTurmaAoPlanejamento(new Turma("AUX2", new DisciplinaObrigatoria("AUX002", "Aux2", 50, 3), 30, "Ter 10h-12h")); // 50h

        assertThrows(CargaHorariaExcedidaException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaRedesComputadores);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaRedesComputadores));
    }

    @Test
    void tentarMatricularDisciplina_OR_ComCargaHorariaExcedida_Falha() {
        topicosAvancados.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));

        alunoTeste.adicionarDisciplinaCursada(estruturaDados, 70.0);

        alunoTeste.adicionarTurmaAoPlanejamento(new Turma("AUX3", new DisciplinaObrigatoria("AUX003", "Aux3", 100, 6), 30, "Qua 14h-16h")); // 100h
        alunoTeste.adicionarTurmaAoPlanejamento(new Turma("AUX4", new DisciplinaObrigatoria("AUX004", "Aux4", 40, 2), 30, "Qui 14h-16h")); // 40h

        assertThrows(CargaHorariaExcedidaException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaTopicosAvancados);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaTopicosAvancados));
    }



    @Test
    void tentarMatricularDisciplina_AND_ComCreditosInsuficientes_Falha() {

        redesComputadores.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));
        redesComputadores.adicionarPreRequisitoValidador(new ValidadorCreditosMin(150)); // Exige 150 créditos


        alunoTeste.adicionarDisciplinaCursada(prog1, 70.0);
        alunoTeste.adicionarDisciplinaCursada(labProg1, 80.0);

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaRedesComputadores);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaRedesComputadores));
    }

    @Test
    void tentarMatricularDisciplina_OR_ComCreditosInsuficientes_Falha() {

        topicosAvancados.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));
        topicosAvancados.adicionarPreRequisitoValidador(new ValidadorCreditosMin(120));

        alunoTeste.adicionarDisciplinaCursada(estruturaDados, 70.0);

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaTopicosAvancados);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaTopicosAvancados));
    }

    @Test
    void tentarMatricularDisciplina_AND_ComCreditosSuficientes_Sucesso() {

        redesComputadores.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));
        redesComputadores.adicionarPreRequisitoValidador(new ValidadorCreditosMin(10));


        alunoTeste.adicionarDisciplinaCursada(prog1, 70.0);
        alunoTeste.adicionarDisciplinaCursada(labProg1, 80.0);
        alunoTeste.adicionarDisciplinaCursada(calc1, 80.0);
        alunoTeste.adicionarDisciplinaCursada(estruturaDados, 75.0); // Total de créditos: 150

        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoTeste, turmaRedesComputadores);
            assertEquals("ACEITA: Matrícula em 'Redes de Computadores' realizada com sucesso.", resultado);
        });
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaRedesComputadores));
    }



    @Test
    void tentarMatricularDisciplina_AND_ComCoRequisitoAtendido_Sucesso() {

        bancoDados.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));
        bancoDados.adicionarCoRequisito(fisica);


        alunoTeste.adicionarDisciplinaCursada(prog1, 70.0);
        alunoTeste.adicionarDisciplinaCursada(labProg1, 80.0);

        alunoTeste.adicionarTurmaAoPlanejamento(turmaFisica);


        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoTeste, turmaBancoDados);
            assertEquals("ACEITA: Matrícula em 'Banco de Dados' realizada com sucesso.", resultado);
        });
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaBancoDados));
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaFisica));
    }

    @Test
    void tentarMatricularDisciplina_AND_ComCoRequisitoNaoAtendido_Falha() {

        bancoDados.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));
        bancoDados.adicionarCoRequisito(fisica);


        alunoTeste.adicionarDisciplinaCursada(prog1, 70.0);
        alunoTeste.adicionarDisciplinaCursada(labProg1, 80.0);

        assertThrows(CoRequisitoNaoAtendidoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaBancoDados);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaBancoDados));
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaFisica));
    }

    @Test
    void tentarMatricularDisciplina_OR_ComCoRequisitoAtendido_Sucesso() {

        topicosAvancados.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));
        topicosAvancados.adicionarCoRequisito(calc1);


        alunoTeste.adicionarDisciplinaCursada(algebra, 70.0);
        alunoTeste.adicionarTurmaAoPlanejamento(turmaCalc1);

        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoTeste, turmaTopicosAvancados);
            assertEquals("ACEITA: Matrícula em 'Tópicos Avançados II' realizada com sucesso.", resultado);
        });
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaTopicosAvancados));
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaCalc1));
    }

    @Test
    void tentarMatricularDisciplina_OR_ComCoRequisitoNaoAtendido_Falha() {

        topicosAvancados.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));
        topicosAvancados.adicionarCoRequisito(calc1);

        alunoTeste.adicionarDisciplinaCursada(estruturaDados, 70.0);

        assertThrows(CoRequisitoNaoAtendidoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaTopicosAvancados);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaTopicosAvancados));
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaCalc1));
    }


    // caso com maior quantidade de Requisitos:
    @Test
    void tentarMatricularDisciplina_VariosTestesComSucesso() {

        //Disciplina IA - Inteligencia Artificial

        ia.adicionarPreRequisitoValidador(new ValidadorAND(estruturaDados, algebra));
        ia.adicionarPreRequisitoValidador(new ValidadorCreditosMin(10));

        ia.adicionarCoRequisito(redesComputadores);

        alunoTeste.adicionarDisciplinaCursada(estruturaDados, 70.0);
        alunoTeste.adicionarDisciplinaCursada(algebra, 80.0);
        alunoTeste.adicionarDisciplinaCursada(calc1, 80.0);
        alunoTeste.adicionarDisciplinaCursada(bancoDados, 80.0);

        alunoTeste.adicionarTurmaAoPlanejamento(turmaRedesComputadores);

        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoTeste, turmaIA);
            assertEquals("ACEITA: Matrícula em 'Inteligência Artificial' realizada com sucesso.", resultado);
        });
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaIA));
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaRedesComputadores));
    }

    @Test
    void tentarMatricularDisciplina_CenarioComplexo_FalhaNoCoRequisito() {

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
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaRedesComputadores));
    }

    @Test
    void tentarMatricularDisciplina_CenarioComplexo_FalhaNosCreditosMinimos() {


        ia.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));
        ia.adicionarPreRequisitoValidador(new ValidadorOR(prog1, labProg1));
        ia.adicionarPreRequisitoValidador(new ValidadorCreditosMin(120));
        ia.adicionarCoRequisito(redesComputadores);


        alunoTeste.adicionarDisciplinaCursada(prog1, 70.0);
        alunoTeste.adicionarDisciplinaCursada(labProg1, 80.0);

        alunoTeste.adicionarTurmaAoPlanejamento(turmaRedesComputadores);

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaIA);
        });
        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaIA));
    }


    @Test
    void tentarMatricularDisciplina_CenarioComplexo_FalhaNaCargaHoraria() {

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
    void tentarMatricularDisciplina_PreRequisitoOR_SucessoComPrimeiraOpcao()  {
        topicosAvancados.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));

        alunoTeste.adicionarDisciplinaCursada(estruturaDados, 70.0);

        assertDoesNotThrow(() -> {
            String resultado = sistema.tentarMatricularDisciplina(alunoTeste, turmaTopicosAvancados);
            assertEquals("ACEITA: Matrícula em 'Tópicos Avançados II' realizada com sucesso.", resultado);
        });
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaTopicosAvancados));
    }

    @Test
    void tentarMatricularDisciplina_PreRequisitoOR_SucessoComSegundaOpcao() {
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
    void tentarMatricularDisciplina_MultiplosANDs_Sucesso()  {

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


    @Test
    void tentarMatricularDisciplina_CenarioComplexo_And_e_OR_Sucesso() {

        ia.adicionarPreRequisitoValidador(new ValidadorOR(estruturaDados, algebra));
        ia.adicionarPreRequisitoValidador(new ValidadorAND(prog1, labProg1));

        ia.adicionarCoRequisito(redesComputadores);
        prog1.adicionarCoRequisito(labProg1);

        alunoTeste.adicionarDisciplinaCursada(estruturaDados, 70.0);
        alunoTeste.adicionarDisciplinaCursada(labProg1, 90.0);
        alunoTeste.adicionarDisciplinaCursada(prog1, 80.0);

        alunoTeste.adicionarTurmaAoPlanejamento(turmaRedesComputadores);

        assertDoesNotThrow(() -> {
            sistema.tentarMatricularDisciplina(alunoTeste, turmaIA);
        });
        assertTrue(alunoTeste.getPlanejamentoFuturo().contains(turmaIA));
    }


    @Test
    void tentarMatricularDisciplina_CenarioComplexo_And_e_OR_Falha() {

        fisica2.adicionarPreRequisitoValidador(new ValidadorOR(calc1, topicosAvancados));
        fisica2.adicionarPreRequisitoValidador(new ValidadorAND(fisica, algebra));

        algebra.adicionarCoRequisito(calc1);
        fisica2.adicionarCoRequisito(calc2);

        alunoTeste.adicionarDisciplinaCursada(calc1, 70.0);
        alunoTeste.adicionarDisciplinaCursada(fisica, 90.0);
        alunoTeste.adicionarDisciplinaCursada(algebra, 80.0);


        assertThrows(CoRequisitoNaoAtendidoException.class, () ->{
           sistema.tentarMatricularDisciplina(alunoTeste, turmaFisica2);
        });

        assertFalse(alunoTeste.getPlanejamentoFuturo().contains(turmaFisica2));
    }





}
