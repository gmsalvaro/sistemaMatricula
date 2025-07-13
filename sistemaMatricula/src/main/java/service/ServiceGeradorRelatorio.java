package service;
import model.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceGeradorRelatorio {
    public String gerarRelatorioFinalAluno(Aluno aluno, HashMap<Disciplina, String> disciplinasRejeitadas) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Relatório Final de Matrícula para: ").append(aluno.getNome()).append(" (Matrícula: ").append(aluno.getMatricula()).append(") ---\n");
        sb.append("Carga Horária Máxima Permitida: ").append(aluno.getCargaHorariaMax()).append("h/semestre\n");
        sb.append("-----------------------------------------------------------------------------------------------------\n");

        int cargaHorariaAceitaTotal = 0;
        List<Turma> turmasAceitas = aluno.getPlanejamentoFuturo();
        sb.append("Disciplinas Aceitas no Planejamento:\n");
        if (turmasAceitas == null || turmasAceitas.isEmpty()) {
            sb.append("  Nenhuma disciplina aceita.\n");
        } else {
            for (Turma turma : turmasAceitas) {
                sb.append("  - [ACEITA] ").append(turma.getDisciplina().getNome())
                        .append(" (").append(turma.getDisciplina().getCodigo()).append(")")
                        .append(" - Turma ").append(turma.getId())
                        .append(" - Horário: ").append(turma.getHorario())
                        .append(" - Carga Horária: ").append(turma.getDisciplina().getCargaHoraria()).append("h\n");
                cargaHorariaAceitaTotal += turma.getDisciplina().getCargaHoraria();
            }
            sb.append("  Carga Horária Total Aceita: ").append(cargaHorariaAceitaTotal).append("h\n");
        }
        sb.append("-----------------------------------------------------------------------------------------------------\n");
        sb.append("Disciplinas Rejeitadas na Simulação:\n");
        if (disciplinasRejeitadas == null || disciplinasRejeitadas.isEmpty()) {
            sb.append("  Nenhuma disciplina rejeitada.\n");
        } else {
            for (Map.Entry<Disciplina, String> entry : disciplinasRejeitadas.entrySet()) {
                Disciplina disciplinaRejeitada = entry.getKey();
                String motivoRejeicao = entry.getValue();
                sb.append("  - [REJEITADA] ").append(disciplinaRejeitada.getNome())
                        .append(" (").append(disciplinaRejeitada.getCodigo()).append(")")
                        .append(" - Motivo: ").append(motivoRejeicao).append("\n");
            }
        }
        sb.append("-----------------------------------------------------------------------------------------------------\n");
        return sb.toString();
    }
}