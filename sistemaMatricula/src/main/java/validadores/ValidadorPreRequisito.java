package validadores;
import modelo.Aluno;

public interface ValidadorPreRequisito {
    abstract boolean verificarValidador(Aluno aluno);

    abstract String getMensagemErro();
}
