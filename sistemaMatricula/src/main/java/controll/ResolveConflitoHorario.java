package controll;

import excecoes.*;
import modelo.*;

public class ResolveConflitoHorario {

   public ResolveConflitoHorario(){}

    public void resolverConflitoHorario(Aluno aluno, Turma turmaAtual, Disciplina disciplinaAtual) throws ConflitoDeHorarioException {

       //Confere Conflito
       Turma turmaComConflitoExistente = null;
        for (Turma aceitaAnteriormente : aluno.getPlanejamentoFuturo()) {
            if (aceitaAnteriormente.getHorario().equals(turmaAtual.getHorario())) {
                turmaComConflitoExistente = aceitaAnteriormente;
                break;
            }
        }

        //Trabalha Relacionada ao conflito!!
        if (turmaComConflitoExistente != null) {
            Disciplina discConflitante = turmaComConflitoExistente.getDisciplina();
            int precedenciaNova = disciplinaAtual.getPrecedencia();
            int precedenciaExistente = discConflitante.getPrecedencia();

            if (precedenciaNova > precedenciaExistente) {
                aluno.removerTurmaDoPlanejamento(turmaComConflitoExistente);
                turmaComConflitoExistente.desmatricularAluno();
            } else if (precedenciaNova < precedenciaExistente) {
                throw new ConflitoDeHorarioException(
                        "Conflito de horário com '" + discConflitante.getNome() + "' (maior prioridade). '" + disciplinaAtual.getNome() + "' rejeitada."
                );
            } else {
               // aluno.removerTurmaDoPlanejamento(turmaComConflitoExistente);
               // turmaComConflitoExistente.desmatricularAluno();
                throw new ConflitoDeHorarioException(
                        "Conflito de horário irresolúvel entre '" + disciplinaAtual.getNome() + "' e '" + discConflitante.getNome() + "' (mesma prioridade)."
                );
            }
        }

    }

}
