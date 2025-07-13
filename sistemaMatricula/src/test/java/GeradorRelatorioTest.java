import model.*;
import service.GeradorRelatorio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeradorRelatorioTest {

    private GeradorRelatorio geradorRelatorio;
    private Aluno alunoTeste;
    private Disciplina introProg, calculo2, engSoft, graficos, fis2;
    private Turma turmaIntroProg, turmaCalculo2, turmaEngSoft, turmaGraficos, turmaFis2;

    @BeforeEach
    void setup() {
        geradorRelatorio = new GeradorRelatorio();

        alunoTeste = new Aluno("Roberto Carlos", "2024003", 40, 250);

        introProg = new DisciplinaObrigatoria("INP001", "Introdução à Programação", 60, 4);
        calculo2 = new DisciplinaObrigatoria("MAT002", "Cálculo II", 90, 4);
        engSoft = new DisciplinaEletiva("ENG001", "Engenharia de Software", 60, 4);
        graficos = new DisciplinaOptativa("CPG001", "Computação Gráfica", 30, 2);
        fis2 = new DisciplinaObrigatoria("FIS002", "Física II", 60, 4);

        turmaIntroProg = new Turma("T1-A", introProg, 30, "SEG/QUA 10:00-12:00");
        turmaCalculo2 = new Turma("T2-B", calculo2, 25, "TER/QUI 08:00-11:00");
        turmaEngSoft = new Turma("T3-C", engSoft, 40, "SEG/QUA 10:00-12:00");
        turmaGraficos = new Turma("T4-D", graficos, 35, "SEX 14:00-16:00");
        turmaFis2 = new Turma("T5-E", fis2, 20, "TER/QUI 08:00-11:00");
    }

    // --- Testes de Cenários ---

    @Test
    void testGerarRelatorio_AlunoComDisciplinasAceitasERejeitadas() {

        alunoTeste.adicionarTurmaAoPlanejamento(turmaIntroProg);
        alunoTeste.adicionarTurmaAoPlanejamento(turmaGraficos);
        alunoTeste.adicionarTurmaAoPlanejamento(turmaCalculo2);

        HashMap<Disciplina, String> disciplinasRejeitadas = new HashMap<>();
        disciplinasRejeitadas.put(fis2, "Conflito de horário irresolúvel entre 'Física II' e 'Cálculo II' (mesma prioridade).");
        disciplinasRejeitadas.put(engSoft, "Conflito de horário com 'Introdução à Programação' (maior prioridade). 'Engenharia de Software' rejeitada.");

        String relatorioGerado = geradorRelatorio.gerarRelatorioFinalAluno(alunoTeste, disciplinasRejeitadas);

        String expectedReport =
                "--- Relatório Final de Matrícula para: Roberto Carlos (Matrícula: 2024003) ---\n" +
                        "Carga Horária Máxima Permitida: 250h/semestre\n" +
                        "-----------------------------------------------------------------------------------------------------\n" +
                        "Disciplinas Aceitas no Planejamento:\n" +
                        "  - [ACEITA] Introdução à Programação (INP001) - Turma T1-A - Horário: SEG/QUA 10:00-12:00 - Carga Horária: 60h\n" +
                        "  - [ACEITA] Computação Gráfica (CPG001) - Turma T4-D - Horário: SEX 14:00-16:00 - Carga Horária: 30h\n" +
                        "  - [ACEITA] Cálculo II (MAT002) - Turma T2-B - Horário: TER/QUI 08:00-11:00 - Carga Horária: 90h\n" +
                        "  Carga Horária Total Aceita: 180h\n" +
                        "-----------------------------------------------------------------------------------------------------\n" +
                        "Disciplinas Rejeitadas na Simulação:\n" +

                        "  - [REJEITADA] Engenharia de Software (ENG001) - Motivo: Conflito de horário com 'Introdução à Programação' (maior prioridade). 'Engenharia de Software' rejeitada.\n" +
                        "  - [REJEITADA] Física II (FIS002) - Motivo: Conflito de horário irresolúvel entre 'Física II' e 'Cálculo II' (mesma prioridade).\n" +
                        "-----------------------------------------------------------------------------------------------------\n";

        assertEquals(expectedReport, relatorioGerado);
    }

    @Test
    void testGerarRelatorio_AlunoSemDisciplinasAceitas() {
        // Nenhuma disciplina adicionada ao planejamento do aluno
        HashMap<Disciplina, String> disciplinasRejeitadas = new HashMap<>();
        disciplinasRejeitadas.put(introProg, "Pré-requisito não cumprido."); // Usando as novas disciplinas

        String relatorioGerado = geradorRelatorio.gerarRelatorioFinalAluno(alunoTeste, disciplinasRejeitadas);

        String expectedReport =
                "--- Relatório Final de Matrícula para: Roberto Carlos (Matrícula: 2024003) ---\n" +
                        "Carga Horária Máxima Permitida: 250h/semestre\n" +
                        "-----------------------------------------------------------------------------------------------------\n" +
                        "Disciplinas Aceitas no Planejamento:\n" +
                        "  Nenhuma disciplina aceita.\n" +
                        "-----------------------------------------------------------------------------------------------------\n" +
                        "Disciplinas Rejeitadas na Simulação:\n" +
                        "  - [REJEITADA] Introdução à Programação (INP001) - Motivo: Pré-requisito não cumprido.\n" +
                        "-----------------------------------------------------------------------------------------------------\n";

        assertEquals(expectedReport, relatorioGerado);
    }

    @Test
    void testGerarRelatorio_AlunoSemDisciplinasRejeitadas() {
        // Simular matrículas aceitas no planejamento do aluno
        alunoTeste.adicionarTurmaAoPlanejamento(turmaIntroProg);
        alunoTeste.adicionarTurmaAoPlanejamento(turmaCalculo2); // Usando as novas turmas

        HashMap<Disciplina, String> disciplinasRejeitadas = new HashMap<>(); // Vazias

        String relatorioGerado = geradorRelatorio.gerarRelatorioFinalAluno(alunoTeste, disciplinasRejeitadas);

        String expectedReport =
                "--- Relatório Final de Matrícula para: Roberto Carlos (Matrícula: 2024003) ---\n" +
                        "Carga Horária Máxima Permitida: 250h/semestre\n" +
                        "-----------------------------------------------------------------------------------------------------\n" +
                        "Disciplinas Aceitas no Planejamento:\n" +
                        "  - [ACEITA] Introdução à Programação (INP001) - Turma T1-A - Horário: SEG/QUA 10:00-12:00 - Carga Horária: 60h\n" +
                        "  - [ACEITA] Cálculo II (MAT002) - Turma T2-B - Horário: TER/QUI 08:00-11:00 - Carga Horária: 90h\n" +
                        "  Carga Horária Total Aceita: 150h\n" +
                        "-----------------------------------------------------------------------------------------------------\n" +
                        "Disciplinas Rejeitadas na Simulação:\n" +
                        "  Nenhuma disciplina rejeitada.\n" +
                        "-----------------------------------------------------------------------------------------------------\n";

        assertEquals(expectedReport, relatorioGerado);
    }

    @Test
    void testGerarRelatorio_AlunoSemDisciplinasAceitasNemRejeitadas() {
        // Ambos os mapas vazios
        HashMap<Disciplina, String> disciplinasRejeitadas = new HashMap<>();

        String relatorioGerado = geradorRelatorio.gerarRelatorioFinalAluno(alunoTeste, disciplinasRejeitadas);

        String expectedReport =
                "--- Relatório Final de Matrícula para: Roberto Carlos (Matrícula: 2024003) ---\n" +
                        "Carga Horária Máxima Permitida: 250h/semestre\n" +
                        "-----------------------------------------------------------------------------------------------------\n" +
                        "Disciplinas Aceitas no Planejamento:\n" +
                        "  Nenhuma disciplina aceita.\n" +
                        "-----------------------------------------------------------------------------------------------------\n" +
                        "Disciplinas Rejeitadas na Simulação:\n" +
                        "  Nenhuma disciplina rejeitada.\n" +
                        "-----------------------------------------------------------------------------------------------------\n";

        assertEquals(expectedReport, relatorioGerado);
    }
}