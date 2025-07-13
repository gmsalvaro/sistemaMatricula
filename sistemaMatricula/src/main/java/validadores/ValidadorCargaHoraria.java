package validadores;

import excecoes.*;
import modelo.*;

public class ValidadorCargaHoraria  {

    public void validarCargaHoraria(Aluno aluno, Disciplina disciplina) throws CargaHorariaExcedidaException {
        if (aluno == null || disciplina == null) {
            throw new CargaHorariaExcedidaException("Erro interno: Aluno ou disciplina nulo para validação de carga horária.");
        }
        int cargaHorariaAtual = aluno.getCargaHorariaAtualNoPlanejamento();
        int novaCargaHorariaTotal = cargaHorariaAtual + disciplina.getCargaHoraria();
        if (novaCargaHorariaTotal > aluno.getCargaHorariaMax()) {
            throw new CargaHorariaExcedidaException(
                    "Carga horária máxima excedida para o aluno '" + aluno.getNome() + "' ao tentar adicionar '" + disciplina.getNome() + "'. " +
                            "Carga atual: " + cargaHorariaAtual + ", Carga da disciplina: " + disciplina.getCargaHoraria() +
                            ", Limite: " + aluno.getCargaHorariaMax() + "."
            );
        }
    }
}