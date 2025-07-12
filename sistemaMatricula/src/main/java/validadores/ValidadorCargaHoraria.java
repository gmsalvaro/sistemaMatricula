package validadores;
import  modelo.Aluno;
import  modelo.Disciplina;


public class ValidadorCargaHoraria {
        Aluno aluno;
        Disciplina disciplina;

        public ValidadorCargaHoraria( Aluno aluno, Disciplina disciplina ){
            this.aluno = aluno;
            this.disciplina = disciplina;

        }
        public boolean validarCargaHoraria() {
            int cargaHorariaAtual = aluno.getCargaHorariaAtualNoPlanejamento();
            int novaCargaHorariaTotal = cargaHorariaAtual + disciplina.getCargaHoraria();
            if (novaCargaHorariaTotal > aluno.getCargaHorariaM())
                return false;
            return true;
        }
}