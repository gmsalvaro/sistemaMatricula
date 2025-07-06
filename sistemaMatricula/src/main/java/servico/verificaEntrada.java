package servico;

import java.util.Scanner;

public class verificaEntrada {
    private Scanner teclado = new Scanner(System.in);

    public int lerOpcaoValidaInt(int[] opcoesValidas) {
        boolean valido = false;
        String entrada = null;
        int escolha = 0;

        while (!valido) {
            entrada = teclado.nextLine().trim();
            try {
                escolha = Integer.parseInt(entrada);
                for (int op : opcoesValidas) {
                    if (escolha == op) {
                        valido = true;
                        break;
                    }
                }
                if (!valido) {
                    System.out.println("Opção inválida. Por favor, digite uma das opções válidas.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
            }
        }
        return escolha;
    }

}
