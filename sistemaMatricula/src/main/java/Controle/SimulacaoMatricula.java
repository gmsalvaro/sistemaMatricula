package Controle;

import Controle.SistemaMatricula;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.DisciplinaObrigatoria;
import modelo.DisciplinaEletiva;
import modelo.Turma;
import validadores.ValidadorOR;
import validadores.ValidadorSimples;
// import validadores.ValidadorCargaHorariaAcumulada; // Removido

public class SimulacaoMatricula {

    public static void main(String[] args) throws excecoes.CoRequisitoNaoAtendidoException, excecoes.PreRequisitoNaoCumpridoException, excecoes.TurmaCheiaException, excecoes.ConflitoDeHorarioException, excecoes.CargaHorariaExcedidaException, excecoes.CreditosInsuficienteException {
        System.out.println("--- Iniciando Simulação de Matrícula ---");

        // 1. Criar Disciplinas
        Disciplina prog1 = new DisciplinaObrigatoria("DCC001", "Programação I", 60, 2);
        Disciplina labProg1 = new DisciplinaObrigatoria("DCC002", "Laboratório de Programação I", 30, 2);
        Disciplina calc1 = new DisciplinaObrigatoria("MAT001", "Cálculo I", 60,2);
        Disciplina algebra = new DisciplinaObrigatoria("MAT002", "Álgebra Linear", 60, 2);
        Disciplina fisica = new DisciplinaObrigatoria("FIS001", "Física Geral", 60, 2);
        Disciplina estruturaDados = new DisciplinaObrigatoria("DCC003", "Estruturas de Dados", 60, 2);
        Disciplina bancoDados = new DisciplinaObrigatoria("DCC004", "Banco de Dados", 60, 2);
        Disciplina redes = new DisciplinaEletiva("RED001", "Redes de Computadores", 60, 2); // Eletiva, precedência 2
        Disciplina etica = new DisciplinaObrigatoria("HUM001", "Ética", 30, 2); // Precedência 1

        // 2. Definir Pré-requisitos e Co-requisitos
        // Estruturas de Dados: Agora só exige Programação I APROVADA
        estruturaDados.setValidadorPreRequisito(
                new ValidadorSimples(prog1)
        );

        // Banco de Dados: Precisa de Estruturas de Dados OU Álgebra Linear APROVADA
        bancoDados.setValidadorPreRequisito(
                new ValidadorOR(
                        new ValidadorSimples(estruturaDados),
                        new ValidadorSimples(algebra)
                )
        );

        // Prog I tem Lab Prog I como co-requisito (deve ser matriculada junto ou já estar no plano)
        prog1.adicionarCoRequisito(labProg1);

        // 3. Criar Turmas
        Turma turmaProg1_T1 = new Turma("T1-P1", prog1, 2, "Seg/Qua 08h-10h");
        Turma turmaLabProg1_T1 = new Turma("T1-LP1", labProg1, 2, "Seg/Qua 10h-12h"); // Horário diferente
        Turma turmaLabProg1_T2 = new Turma("T2-LP1", labProg1, 1, "Seg/Qua 08h-10h"); // Horário igual a Prog1_T1
        Turma turmaCalc1_T1 = new Turma("T1-C1", calc1, 3, "Ter/Qui 14h-16h");
        Turma turmaAlgebra_T1 = new Turma("T1-AL", algebra, 2, "Ter/Qui 14h-16h"); // Horário igual a Calc1_T1
        Turma turmaEstrutura_T1 = new Turma("T1-ED", estruturaDados, 1, "Sex 08h-12h");
        Turma turmaBancoDados_T1 = new Turma("T1-BD", bancoDados, 2, "Qua/Sex 14h-16h");
        Turma turmaFisica_T1 = new Turma("T1-F1", fisica, 2, "Seg/Qua 10h-12h"); // Horário igual a LabProg1_T1
        Turma turmaRedes_T1 = new Turma("T1-RED", redes, 2, "Ter/Qui 10h-12h");
        Turma turmaEtica_T1 = new Turma("T1-ET", etica, 2, "Sex 14h-16h");


        // 4. Criar Alunos
        Aluno alunoJoao = new Aluno("João Silva", "2023001", 120); // Max 120h por semestre
        Aluno alunoMaria = new Aluno("Maria Souza", "2023002", 90); // Max 90h por semestre
        Aluno alunoPedro = new Aluno("Pedro Santos", "2023003", 150); // Max 150h por semestre

        // 5. Adicionar Disciplinas Cursadas ao Histórico dos Alunos
        // João:
        alunoJoao.adicionarDisciplinaCursada(prog1, 75.0); // Aprovado em Prog I (60h)
        alunoJoao.adicionarDisciplinaCursada(calc1, 80.0);  // Aprovado em Cálculo I (60h)
        alunoJoao.adicionarDisciplinaCursada(labProg1, 70.0); // Aprovado em Lab Prog I (30h)
        alunoJoao.adicionarDisciplinaCursada(fisica, 50.0); // Reprovado em Física

        // Maria:
        alunoMaria.adicionarDisciplinaCursada(calc1, 65.0); // Aprovada em Cálculo I (60h)
        alunoMaria.adicionarDisciplinaCursada(algebra, 70.0); // Aprovada em Álgebra Linear (60h)
        alunoMaria.adicionarDisciplinaCursada(prog1, 55.0); // Reprovada em Prog I

        // Pedro:
        alunoPedro.adicionarDisciplinaCursada(calc1, 90.0); // Aprovado (60h)
        alunoPedro.adicionarDisciplinaCursada(prog1, 88.0); // Aprovado (60h)
        alunoPedro.adicionarDisciplinaCursada(fisica, 72.0); // Aprovado (60h)

        // 6. Criar Sistema de Matrícula
        SistemaMatricula sistema = new SistemaMatricula();

        System.out.println("\n--- Tentativas de Matrícula ---");

        // --- Teste 1: João tenta matricular Estruturas de Dados ---
        // Pré-req: Prog I APROVADA (OK).
        // Resultado Esperado: ACEITA
        System.out.println("\nJoão tenta matricular Estruturas de Dados (Turma T1-ED):");
        System.out.println(sistema.tentarMatricularDisciplina(alunoJoao, turmaEstrutura_T1));

        // --- Teste 2: Maria tenta matricular Banco de Dados ---
        // Pré-req: Estruturas de Dados OU Álgebra Linear APROVADA.
        // Maria: Álgebra Linear (70.0 - OK). Estruturas de Dados (não cursou).
        // Resultado Esperado: ACEITA (por Álgebra Linear)
        System.out.println("\nMaria tenta matricular Banco de Dados (Turma T1-BD):");
        System.out.println(sistema.tentarMatricularDisciplina(alunoMaria, turmaBancoDados_T1));

        // --- Teste 3: Pedro tenta matricular Estruturas de Dados ---
        // Pré-req: Prog I APROVADA (OK).
        // Turma T1-ED tem capacidade 1. João já pegou.
        // Resultado Esperado: REJEITADA por Turma Cheia
        System.out.println("\nPedro tenta matricular Estruturas de Dados (Turma T1-ED - já cheia):");
        System.out.println(sistema.tentarMatricularDisciplina(alunoPedro, turmaEstrutura_T1));


        // --- Teste 4: João tenta matricular Cálculo I (carga horária do semestre) ---
        // Plano de João atual: Estruturas de Dados (60h). Carga máxima: 120h.
        // Cálculo I: 60h. Total: 60 + 60 = 120h.
        // Resultado Esperado: ACEITA
        System.out.println("\nJoão tenta matricular Cálculo I (Turma T1-C1):");
        System.out.println(sistema.tentarMatricularDisciplina(alunoJoao, turmaCalc1_T1));


        // --- Teste 5: João tenta matricular Ética (carga horária máxima do semestre excedida) ---
        // João: ED (60h), C1 (60h). Total 120h. Carga máxima 120h.
        // Tenta matricular Ética (30h).
        // Resultado Esperado: REJEITADA por Carga Horária Excedida (120+30 = 150 > 120)
        System.out.println("\nJoão tenta matricular Ética (Turma T1-ET):");
        System.out.println(sistema.tentarMatricularDisciplina(alunoJoao, turmaEtica_T1));


        // --- Teste 6: João tenta matricular Programação I (já cursou e aprovou, mas vai tentar de novo) ---
        // Assumindo que não há regra contra refazer (e nem pré-requisito).
        // Turma T1-P1 tem capacidade 2.
        // Resultado Esperado: ACEITA
        System.out.println("\nJoão tenta matricular Programação I novamente (Turma T1-P1):");
        System.out.println(sistema.tentarMatricularDisciplina(alunoJoao, turmaProg1_T1));


        // --- Teste 7: João tenta matricular Lab Prog I (T2-LP1) - Conflito de Horário e Co-requisito ---
        // Prog I (T1-P1) e Lab Prog I (T2-LP1) têm o mesmo horário: Seg/Qua 08h-10h.
        // Prog I tem precedência 1, Lab Prog I tem precedência 1.
        // João já tem Prog I no plano (adicionada no teste 6).
        // João já cursou Lab Prog I e foi aprovado, mas vamos forçar a tentativa de matrícula para testar o conflito.
        System.out.println("\nJoão tenta matricular Lab Prog I (Turma T2-LP1 - conflita com Prog I):");
        System.out.println(sistema.tentarMatricularDisciplina(alunoJoao, turmaLabProg1_T2));
        // Esperado: Prog I (T1-P1) é removida do plano (mesma precedência, conflito irresolúvel),
        // e Lab Prog I (T2-LP1) é REJEITADA com mensagem de Conflito Irresolúvel.


        // --- Teste 8: Maria tenta matricular Álgebra Linear (T1-AL) - Sem conflito com o plano atual ---
        // Maria já tem Banco de Dados (T1-BD) no plano (Qua/Sex 14h-16h).
        // Álgebra Linear (T1-AL) tem horário Ter/Qui 14h-16h. Não há conflito de horário.
        // Ela já aprovou em Álgebra Linear no histórico. Se permitir refazer, vai ACEITAR.
        System.out.println("\nMaria tenta matricular Álgebra Linear (Turma T1-AL):");
        System.out.println(sistema.tentarMatricularDisciplina(alunoMaria, turmaAlgebra_T1));


        System.out.println("\n--- Relatórios Finais ---");
        System.out.println(sistema.gerarRelatorioFinalAluno(alunoJoao));
        System.out.println(sistema.gerarRelatorioFinalAluno(alunoMaria));
        System.out.println(sistema.gerarRelatorioFinalAluno(alunoPedro));

        System.out.println("\n--- Fim da Simulação ---");
    }
}