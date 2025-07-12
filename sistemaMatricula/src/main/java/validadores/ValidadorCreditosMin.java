package validadores;
import modelo.Aluno;
import modelo.Disciplina;

public class ValidadorCreditosMin implements validadores.ValidadorPreRequisito { // Renomeado
    private int creditosMinimosExigidos;
    private String mensagemErro;
    private Disciplina disciplinaReferencia;

    public ValidadorCreditosMin(Disciplina disciplinaReferencia, int creditosMinimosExigidos) {
        this.disciplinaReferencia = disciplinaReferencia;
        this.creditosMinimosExigidos = creditosMinimosExigidos;
        this.mensagemErro = "";
    }

    @Override
    public boolean verificarValidador(Aluno aluno) {
        if (aluno.getCreditosAcumulados() < this.creditosMinimosExigidos) {
            this.mensagemErro = "O aluno não possui os " + this.creditosMinimosExigidos + " créditos mínimos acumulados para cursar '" +
                    this.disciplinaReferencia.getNome() + "'. Créditos atuais: " +
                    aluno.getCreditosAcumulados();
            return false;
        }
        return true;
    }

    @Override
    public String getMensagemErro() {
        return mensagemErro;
    }
}