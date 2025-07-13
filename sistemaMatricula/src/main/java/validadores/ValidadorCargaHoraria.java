package validadores;

import excecoes.*;
import model.*;

import java.util.HashMap;

public class ValidadorCargaHoraria  {

    public void validarCargaHoraria(Aluno aluno, Disciplina disciplinaDesejada, HashMap<Disciplina, String> turmasRejeitadas) throws CargaHorariaExcedidaException {
        if (aluno == null || disciplinaDesejada == null)
            throw new CargaHorariaExcedidaException("Erro interno: Aluno ou disciplina nulo para validação de carga horária.");

        int cargaHorariaAtual = aluno.getCargaHorariaAtualNoPlanejamento();
        int novaCargaHorariaTotal = cargaHorariaAtual + disciplinaDesejada.getCargaHoraria();
        if (novaCargaHorariaTotal > aluno.getCargaHorariaMax()) {
            String msg =  "Carga horária máxima excedida para o aluno '" + aluno.getNome() + "' ao tentar adicionar '" + disciplinaDesejada.getNome() + "'. " +
                    "Carga atual: " + cargaHorariaAtual + ", Carga da disciplina: " + disciplinaDesejada.getCargaHoraria() +
                    ", Limite: " + aluno.getCargaHorariaMax() + ".";
            turmasRejeitadas.put(disciplinaDesejada, msg);
            throw new CargaHorariaExcedidaException(msg);
        }
    }
}