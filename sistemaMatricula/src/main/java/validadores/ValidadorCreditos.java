package validadores;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.Turma;

public class ValidadorCreditos { // Renomeado
     private Aluno aluno;
     private Disciplina disciplina;


    public ValidadorCreditos(Aluno aluno, Disciplina disciplina) { // Construtor ajustado
        this.aluno = aluno;
        this.disciplina = disciplina;
    }


    public boolean verificarQtdCredito(Aluno aluno, Disciplina disciplina) {
        int creditosAtuaisNoPlanejamento = this.aluno.getCreditosAcumulados();
        for(Turma turma : this.aluno.getPlanejamentoFuturo()){
            creditosAtuaisNoPlanejamento += turma.getDisciplina().getCredito();
        }
        int novosCreditosTotais = creditosAtuaisNoPlanejamento + this.disciplina.getCredito();
        if (novosCreditosTotais > this.aluno.getCreditoMax())
            return false;
        return true;
    }

}