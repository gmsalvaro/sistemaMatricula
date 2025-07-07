package excecoes;

public abstract class ConflitoDeHorarioException extends Exception {
    public ConflitoDeHorarioException(String msg){
        super(msg);
    }
}
