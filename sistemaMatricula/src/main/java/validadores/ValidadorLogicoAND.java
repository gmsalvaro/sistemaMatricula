package validadores;

import modelo.Aluno;
import modelo.Disciplina;

public class ValidadorLogicoAND implements ValidadorPreRequisito{
        private Disciplina preRequisito1;
        private Disciplina preRequisito2;

    public ValidadorLogicoAND(Disciplina preRequisito1, Disciplina preRequisito2) {
            this.preRequisito1 = preRequisito1;
            this.preRequisito2 = preRequisito2;
        }

        @Override
        public boolean verificarValidador(Aluno aluno) {
            return aluno.verificaAprovado(preRequisito1) && aluno.verificaAprovado(preRequisito2);
        }
        @Override
         public String getMensagemErro(){
            return "Todos os pré-requisitos (" + preRequisito1 +  " , " + preRequisito2 + ") devem ser cumpridos.";
        }
}

