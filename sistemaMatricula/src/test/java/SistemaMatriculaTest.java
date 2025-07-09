import Controle.SistemaMatricula;
import excecoes.PreRequisitoNaoCumpridoException;
import excecoes.CoRequisitoNaoAtendidoException;
import excecoes.TurmaCheiaException;
import excecoes.ConflitoDeHorarioException;
import excecoes.CargaHorariaExcedidaException;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.DisciplinaObrigatoria;
import modelo.Turma;
import org.junit.jupiter.api.Test;
import validadores.ValidadorAND;
import validadores.ValidadorOR;
import validadores.ValidadorSimples;

import static org.junit.jupiter.api.Assertions.*;


class SistemaMatriculaTest {

    @Test
    void tentarMatricularDisciplina() throws CoRequisitoNaoAtendidoException, PreRequisitoNaoCumpridoException,TurmaCheiaException, ConflitoDeHorarioException,CargaHorariaExcedidaException {



        // criaremos as disciplinas
        Disciplina prog1 = new DisciplinaObrigatoria("DCC001", "Programação I", 60);
        Disciplina estruturaDados = new DisciplinaObrigatoria("DCC002", "Estruturas de Dados", 60);

          // indicar quem e o preRequisito ou CoRequisito
        estruturaDados.setValidadorPreRequisito(new ValidadorSimples(prog1));

         // turma
        Turma turmaED = new Turma("T1-ED", estruturaDados, 2, "Sex 08h-12h");

         // aluno e disciplina cursada anteriormente
        Aluno aluno = new Aluno("João", "2023001", 120);
        aluno.adicionarDisciplinaCursada(prog1, 75.0); // Aprovado

        SistemaMatricula sistema = new SistemaMatricula();

       //Esta implementaçao pode seguir o assertThrows? se sim tentar para manter um padrao de entendimento
        // no codigo do trabalho
        String resultado = sistema.tentarMatricularDisciplina(aluno, turmaED);
        assertEquals("ACEITA: Matrícula em 'Estruturas de Dados' realizada com sucesso.", resultado);
        assertTrue(aluno.getPlanejamentoFuturo().contains(turmaED));
    }

    /*

    //Codigo esta quebrando nas excecoes(Dica do Vitor) !!!!!!



    @Test
    void tentarMatricularDisciplinaPreReq(){

        Disciplina prog1 = new DisciplinaObrigatoria("DCC001", "Programação I", 60);
        Disciplina estruturaDados = new DisciplinaObrigatoria("DCC002", "Estruturas de Dados", 60);

        estruturaDados.setValidadorPreRequisito(new ValidadorSimples(prog1));

        Turma turmaED = new Turma("T1-ED", estruturaDados, 2, "Sex 08h-12h");

        Aluno aluno = new Aluno("João", "2023001", 120);
        aluno.adicionarDisciplinaCursada(prog1, 57.0); // Reprovado

        SistemaMatricula sistema = new SistemaMatricula();

        String resultado = sistema.tentarMatricularDisciplina(aluno, turmaED);

        assertEquals("ACEITA: Matrícula em 'Estruturas de Dados' realizada com sucesso.", resultado);
        assertTrue(aluno.getPlanejamentoFuturo().contains(turmaED));
    }
    */

    @Test
    public void testPreRequisitoSimplesNaoCumprido() throws Exception {
        Disciplina prog1 = new DisciplinaObrigatoria("DCC001", "Programação I", 60);
        Disciplina estruturaDados = new DisciplinaObrigatoria("DCC002", "Estruturas de Dados", 60);
        estruturaDados.setValidadorPreRequisito(new ValidadorSimples(prog1));

        Turma turmaED = new Turma("T1-ED", estruturaDados, 2, "Sex 08h-12h");

        Aluno aluno = new Aluno("Maria", "2023002", 120); // Não cursou Prog I

        SistemaMatricula sistema = new SistemaMatricula();


        // como funciona isto: o AssertThrows vai testar se uma exceção foi realmente lançada no teste
        // sendo o primeiro paramentro a exceçao que esperamos que venha no caso e a pre requisito
        // e o segundo paramentro e as linhas de codigo que vao gerar a exceção
        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(aluno, turmaED);
        });
    }

    @Test
    public void testPreRequisitoANDNaoCumprido() throws Exception {
        Disciplina prog1 = new DisciplinaObrigatoria("DCC001", "Programação I", 60);
        Disciplina labProg1 = new DisciplinaObrigatoria("DCC002", "Lab de Programação I", 30);
        Disciplina estruturaDados = new DisciplinaObrigatoria("DCC003", "Estruturas de Dados", 60);


        estruturaDados.setValidadorPreRequisito(new ValidadorAND(
                new ValidadorSimples(prog1),
                new ValidadorSimples(labProg1)
        ));

        Turma turmaED = new Turma("T1-ED", estruturaDados, 2, "Sex 08h-12h");

        Aluno aluno = new Aluno("Pedro", "2023003", 120);
        aluno.adicionarDisciplinaCursada(prog1, 75.0); // cursou só Prog I

        SistemaMatricula sistema = new SistemaMatricula();

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(aluno, turmaED);
        });
    }


    @Test
    public void testPreRequisitoORNaoCumprido() throws Exception {
        Disciplina prog1 = new DisciplinaObrigatoria("DCC001", "Programação I", 60);
        Disciplina algebra = new DisciplinaObrigatoria("MAT001", "Álgebra Linear", 60);
        Disciplina bancoDados = new DisciplinaObrigatoria("DCC004", "Banco de Dados", 60);



        bancoDados.setValidadorPreRequisito(new ValidadorOR(
                new ValidadorSimples(prog1),
                new ValidadorSimples(algebra)
        ));

        Turma turmaBD = new Turma("T1-BD", bancoDados, 2, "Seg 08h-10h");
        Aluno aluno = new Aluno("Lucas", "2023004", 120); // não cursou nenhuma
        SistemaMatricula sistema = new SistemaMatricula();

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(aluno, turmaBD);
        });
    }



    @Test
    public void testPreRequisitoComNotaInsuficiente() throws Exception {
        Disciplina prog1 = new DisciplinaObrigatoria("DCC001", "Programação I", 60);
        Disciplina estruturaDados = new DisciplinaObrigatoria("DCC002", "Estruturas de Dados", 60);

        estruturaDados.setValidadorPreRequisito(new ValidadorSimples(prog1));

        Turma turmaED = new Turma("T1-ED", estruturaDados, 2, "Sex 08h-12h");

        Aluno aluno = new Aluno("Ana", "2023005", 120);
        aluno.adicionarDisciplinaCursada(prog1, 55.0); // cursou, mas reprovada
        SistemaMatricula sistema = new SistemaMatricula();

        assertThrows(PreRequisitoNaoCumpridoException.class, () -> {
            sistema.tentarMatricularDisciplina(aluno, turmaED);
        });
    }


    @Test
    public void testCoRequisitoNaoAtendido() throws Exception {


        Disciplina prog1 = new DisciplinaObrigatoria("DCC001", "Programação I", 60);
        Disciplina labProg1 = new DisciplinaObrigatoria("DCC002", "Lab de Programação I", 30);

        prog1.adicionarCoRequisito(labProg1);

        Turma turmaP1 = new Turma("T1-P1", prog1, 2, "Seg/Qua 08h-10h");
        Aluno aluno = new Aluno("Lucas", "2023006", 120);
        SistemaMatricula sistema = new SistemaMatricula();


        assertThrows(CoRequisitoNaoAtendidoException.class, () -> {
            sistema.tentarMatricularDisciplina(aluno, turmaP1);
        });
    }

    @Test
    public void testTurmaCheiaException() throws Exception {

        Disciplina calc1 = new DisciplinaObrigatoria("MAT001", "Cálculo I", 60);


        Turma turma = new Turma("T1-C1", calc1, 1, "Seg/Qua 08h-10h");

         //a turma estará cheia agora
        turma.matricularAluno();
        Aluno aluno = new Aluno("Lucas", "2023007", 120);
        SistemaMatricula sistema = new SistemaMatricula();

        assertThrows(TurmaCheiaException.class, () -> {
            sistema.tentarMatricularDisciplina(aluno, turma);
        });
    }

    @Test
    public void testCargaHorariaExcedidaException() throws Exception {

        Disciplina calc1 = new DisciplinaObrigatoria("MAT001", "Cálculo I", 60);
        Disciplina fisica = new DisciplinaObrigatoria("FIS001", "Física Geral", 60);

        Turma turmaF1 = new Turma("T1-F1", fisica, 2, "Ter/Qui 10h-12h");
        Aluno aluno = new Aluno("Lucas", "2023008", 100);
        aluno.adicionarTurmaAoPlanejamento(new Turma("T1-C1", calc1, 2, "Seg/Qua 08h-10h"));
        SistemaMatricula sistema = new SistemaMatricula();


        assertThrows(CargaHorariaExcedidaException.class, () -> {
            sistema.tentarMatricularDisciplina(aluno, turmaF1);
        });
    }








}

