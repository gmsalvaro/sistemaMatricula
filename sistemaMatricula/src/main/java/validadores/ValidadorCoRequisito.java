package validadores;

import modelo.Aluno;
import modelo.Disciplina;
import modelo.Turma;

import java.util.List;

public class ValidadorCoRequisito  {
    protected List< Disciplina > coRequisitos;
    private Aluno aluno;


    public ValidadorCoRequisito(Aluno aluno, List< Disciplina > coRequisitos) {
        this.coRequisitos = coRequisitos;
        this.aluno = aluno;
    }

    public boolean validarCoRequisitos() {
        // ESSA É A LÓGICA CORRETA PARA VALIDAR SE TODOS OS CO-REQUISITOS ESTÃO PRESENTES
        // Corrigido da sua versão anterior
        if (coRequisitos == null || coRequisitos.isEmpty()) {
            return true; // Se não há co-requisitos, a validação passa
        }

        for (Disciplina coRequisitoEsperado : coRequisitos) {
            boolean encontradoNoPlano = false;
            for (Turma turmaPlanejada : aluno.getPlanejamentoFuturo()) {
                if (turmaPlanejada.getDisciplina().equals(coRequisitoEsperado)) {
                    encontradoNoPlano = true;
                    break; // Encontrou este co-requisito, pode parar de procurar por ele.
                }
            }
            // Se, após verificar todo o planejamento, este co-requisito NÃO foi encontrado,
            // então a validação geral falha para este conjunto de co-requisitos.
            if (!encontradoNoPlano) {
                return false; // Retorna false imediatamente se um co-requisito obrigatório não for encontrado.
            }
        }
        return true; // Se o loop terminou, todos os co-requisitos foram encontrados.
    }
}