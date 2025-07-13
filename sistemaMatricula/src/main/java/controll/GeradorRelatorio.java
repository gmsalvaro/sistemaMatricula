package controll;

import modelo.*;

import java.util.List;

public class GeradorRelatorio {

    public GeradorRelatorio(){}

    public String gerarRelatorioFinalAluno(Aluno aluno) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Relatório Final de Matrícula para: ").append(aluno.getNome()).append(" (Matrícula: ").append(aluno.getMatricula()).append(") ---\n");
        sb.append("Carga Horária Máxima Permitida: ").append(aluno.getCargaHorariaMax()).append("h/semestre\n");
        sb.append("-----------------------------------------------------------------------------------------------------\n");

        int cargaHorariaAceitaTotal = 0;
        List<Turma> turmasAceitas = aluno.getPlanejamentoFuturo();

        sb.append("Disciplinas Aceitas no Planejamento:\n");
        if (turmasAceitas.isEmpty()) {
            sb.append("  Nenhuma disciplina aceita.\n");
        } else {
            for (Turma turma : turmasAceitas) {
//                sb.append("  - ").append(turma.getDisciplina().getNome())
//                        .append(" (").append(turma.getDisciplina().getCodigo()).append(")")
//                        .append(" - Turma ").append(turma.getId())
//                        .append(" - Horário: ").append(turma.getHorario())
//                        .append(" - Carga Horária: ").append(turma.getDisciplina().getCargaHoraria()).append("h\n");
//                cargaHorariaAceitaTotal += turma.getDisciplina().getCargaHoraria();
            }
            sb.append("  Carga Horária Total Aceita: ").append(cargaHorariaAceitaTotal).append("h\n");
        }
        sb.append("-----------------------------------------------------------------------------------------------------\n");
        return sb.toString();
    }
}
