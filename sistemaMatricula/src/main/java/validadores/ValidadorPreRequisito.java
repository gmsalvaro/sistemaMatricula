package validadores;
import modelo.Aluno;
import java.util.List;

public interface ValidadorPreRequisito {
    abstract boolean verificarValidador(Aluno aluno);

    abstract String getMensagemErro();
}
