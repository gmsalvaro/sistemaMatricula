package service;

import excecoes.*;
import model.*;

import java.util.HashMap;

public class ServiceResolveConflitoHorario {

    protected void resolverConflitoHorario(Aluno aluno, Turma turmaDesejada, Disciplina disciplinaDesejada, HashMap<Disciplina, String> turmasRejeitadas ) throws ConflitoDeHorarioException {
       Turma turmaComConflitoExistente = null;
        for (Turma aceitaAnteriormente : aluno.getPlanejamentoFuturo()) {
            if (aceitaAnteriormente.getHorario().equals(turmaDesejada.getHorario())) {
                turmaComConflitoExistente = aceitaAnteriormente;
                break;
            }
        }
        if (turmaComConflitoExistente != null) {
            Disciplina disciplinaConflitante = turmaComConflitoExistente.getDisciplina();
            int precedenciaNova = disciplinaDesejada.getPrecedencia();
            int precedenciaExistente = disciplinaConflitante.getPrecedencia();

            if (precedenciaNova > precedenciaExistente) {
                aluno.removerTurmaDoPlanejamento(turmaComConflitoExistente);
                turmaComConflitoExistente.desmatricularAluno();
            } else if (precedenciaNova < precedenciaExistente) {
                String msg = "Conflito de horário com '" + disciplinaConflitante.getNome() + "' (maior prioridade). '" + disciplinaDesejada.getNome() + "' rejeitada.) ";
                turmasRejeitadas.put(disciplinaDesejada, msg);
                throw new ConflitoDeHorarioException(msg);
            } else {
                String msg = "Conflito de horário irresolúvel entre '" + disciplinaDesejada.getNome() + "' e '" + disciplinaConflitante.getNome() + "' (mesma prioridade).) ";
                turmasRejeitadas.put(disciplinaDesejada,msg);
                throw new ConflitoDeHorarioException(msg);
            }
        }

    }

}
