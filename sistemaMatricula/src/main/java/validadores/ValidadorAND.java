package validadores;
import modelo.Disciplina;
import modelo.Aluno;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ValidadorAND implements validadores.ValidadorPreRequisito {
    Disciplina preRequisito1;
    Disciplina preRequisito2;
    private String mensagemErro;

    public ValidadorAND(Disciplina preRequisito1,Disciplina preRequisito2) { // Varargs para facilitar a criação
        this.preRequisito1 = preRequisito1;
        this.preRequisito2 = preRequisito2;
    }

    @Override
    public boolean verificarValidador(Aluno aluno) {
        Map<Disciplina, Double> disciplinasCursadasMap = aluno.getDisciplinasCursadas();
        boolean cumpriuPreRequisito1 = false ;
        boolean cumpriuPreRequisito2 = false;
        for (modelo.Disciplina disciplinaCursada : disciplinasCursadasMap.keySet()) {
            if (disciplinaCursada.equals(preRequisito1)) {
                cumpriuPreRequisito1 = true;
            }
            if (disciplinaCursada.equals(preRequisito2)) {

                cumpriuPreRequisito2 = true;
            }
            if (cumpriuPreRequisito1 && cumpriuPreRequisito2) {
                break;
            }
        }

        if (cumpriuPreRequisito1 && cumpriuPreRequisito2) {
            return true;
        } else {
            // Constrói a mensagem de erro detalhada
            StringBuilder sb = new StringBuilder("Pré-requisito(s) não cumprido(s): ");
            if (!cumpriuPreRequisito1) {
                sb.append(preRequisito1.getNome());
            }
            if (!cumpriuPreRequisito2) {
                if (!cumpriuPreRequisito1) {
                    sb.append(" e ");
                }
                sb.append(preRequisito2.getNome());
            }
            this.mensagemErro = sb.toString();
            return false;
        }
    }

    @Override
    public String getMensagemErro() {
        return mensagemErro;
    }
}