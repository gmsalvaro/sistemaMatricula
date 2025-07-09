package servico;
import servico.verificaEntrada;

public class prints {
    verificaEntrada entrada = new verificaEntrada();
    private void imprimirOpcoesValidador(){
        System.out.println("Digite o numero para a respectiva opcao");
        System.out.println("1 - Validador Simples");
        System.out.println("2 - Validador AND");
        System.out.println("3 - Validador OR");
        System.out.println("4 - ValidadorCreditos");
        entrada.lerOpcaoValidaInt(new int[]{1,2,3,4});
    }



}
