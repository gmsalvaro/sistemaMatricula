package validadores;

import excecoes.*;
import modelo.*;

import java.util.HashMap;

public class ValidadorCargaHoraria  {

    public void validarCargaHoraria(Aluno aluno, Disciplina disciplina, Turma turmaDesejada, HashMap<Disciplina, String> turmasRejeitadas) throws CargaHorariaExcedidaException {
        if (aluno == null || disciplina == null) {
            throw new CargaHorariaExcedidaException("Erro interno: Aluno ou disciplina nulo para validação de carga horária.");
        }
        int cargaHorariaAtual = aluno.getCargaHorariaAtualNoPlanejamento();
        int novaCargaHorariaTotal = cargaHorariaAtual + disciplina.getCargaHoraria();
        if (novaCargaHorariaTotal > aluno.getCargaHorariaMax()) {
            String msg =  "Carga horária máxima excedida para o aluno '" + aluno.getNome() + "' ao tentar adicionar '" + disciplina.getNome() + "'. " +
                    "Carga atual: " + cargaHorariaAtual + ", Carga da disciplina: " + disciplina.getCargaHoraria() +
                    ", Limite: " + aluno.getCargaHorariaMax() + ".";
            turmasRejeitadas.put(disciplina, msg);
            throw new CargaHorariaExcedidaException(msg);
        }
    }
}