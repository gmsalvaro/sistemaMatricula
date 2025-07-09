
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
import modelo.DisciplinaObrigatoria;
import modelo.Turma;
import Controle.SistemaMatricula;
import validadores.ValidadorPreRequisito;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import validadores.ValidadorSimples;

class SistemaMatriculaTest {

    @Test
    void tentarMatricularDisciplina() {

        // Criar disciplinas
        Disciplina prog1 = new DisciplinaObrigatoria("DCC001", "Programação I", 60);
        Disciplina estruturaDados = new DisciplinaObrigatoria("DCC002", "Estruturas de Dados", 60);

        // Definir pré-requisito
        estruturaDados.setValidadorPreRequisito(new ValidadorSimples(prog1));

        // Criar turma
        Turma turmaED = new Turma("T1-ED", estruturaDados, 2, "Sex 08h-12h");

        // Criar aluno com Prog I aprovada
        Aluno aluno = new Aluno("João", "2023001", 120);
        aluno.adicionarDisciplinaCursada(prog1, 75.0); // Aprovado

        // Sistema de matrícula
        SistemaMatricula sistema = new SistemaMatricula();

        // Testar matrícula
        String resultado = sistema.tentarMatricularDisciplina(aluno, turmaED);

        // Verificações
        assertEquals("ACEITA: Matrícula em 'Estruturas de Dados' realizada com sucesso.", resultado);
        assertTrue(aluno.getPlanejamentoFuturo().contains(turmaED));

    }


    @Test
    void gerarRelatorioFinalAluno() {
    }
}


