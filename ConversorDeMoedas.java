import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConversorDeMoedas {
    private String moedaOrigem;
    private String moedaDestino;
    private double taxaDeCambio;
    private final Scanner leitura;
    private final ConsultaCotacao consulta;
    private final List<Conversao> historicoConversoes;

    public ConversorDeMoedas() {
        this.leitura = new Scanner(System.in);
        this.consulta = new ConsultaCotacao();
        this.moedaOrigem = "";
        this.moedaDestino = "";
        this.taxaDeCambio = 0.0;
        this.historicoConversoes = new ArrayList();
    }

    public void iniciar() {
        int selecaoMenuPrincipal = -1;

        while(selecaoMenuPrincipal != 0) {
            this.mostrarMenuPrincipal();
            selecaoMenuPrincipal = this.leitura.nextInt();
            switch (selecaoMenuPrincipal) {
                case 0:
                    System.out.println("A aplicação será encerrada.");
                    break;
                case 1:
                    this.selecionarMoedaOrigem();
                    break;
                case 2:
                    this.selecionarMoedaDestino();
                    break;
                case 3:
                    this.realizarConversao();
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        }

    }

    private void mostrarMenuPrincipal() {
        System.out.println("----------------------------------------\n    BEM VINDO AO CONVERSOR DE MOEDAS\n----------------------------------------\n1 - Escolha a moeda de origem.\n2 - Escolha a moeda de destino.\n3 - Insira o valor que será convertido.\n0 - Sair.\n----------------------------------------\nMoeda de origem: " + this.moedaOrigem + "\nMoeda de destino: " + this.moedaDestino + "\n----------------------------------------\nSelecione uma das opções acima:");
    }

    private void selecionarMoedaOrigem() {
        System.out.println("----------------------------------------\n      SELECIONE A MOEDA DE ORIGEM\n----------------------------------------\n1 - Real Brasileiro (BRL).\n2 - Dólar Americano (USD).\n3 - Euro (EUR).\n4 - Peso Argentino (ARS).\n5 - Libra Esterlina (GBP).\n6 - Dólar Canadense (CAD).\n----------------------------------------\nSelecione uma das opções:");
        int selecao = this.leitura.nextInt();
        switch (selecao) {
            case 1 -> this.moedaOrigem = "BRL";
            case 2 -> this.moedaOrigem = "USD";
            case 3 -> this.moedaOrigem = "EUR";
            case 4 -> this.moedaOrigem = "ARS";
            case 5 -> this.moedaOrigem = "GBP";
            case 6 -> this.moedaOrigem = "CAD";
            default -> System.out.println("Opção inválida. Tente novamente.");
        }

        this.consulta.setCodigoMoedaOrigem(this.moedaOrigem);
        System.out.println("Sua moeda de origem selecionada é: " + this.moedaOrigem);
    }

    private void selecionarMoedaDestino() {
        System.out.println("----------------------------------------\n      SELECIONE A MOEDA DE DESTINO\n----------------------------------------\n1 - Real brasileiro (BRL).\n2 - Dólar Americano (USD).\n3 - Euro (EUR).\n4 - Peso Argentino (ARS).\n5 - Libra Esterlina (GBP).\n6 - Dólar Canadense (CAD).\n----------------------------------------\nSelecione uma das opções:");
        int selecao = this.leitura.nextInt();
        switch (selecao) {
            case 1 -> this.moedaDestino = "BRL";
            case 2 -> this.moedaDestino = "USD";
            case 3 -> this.moedaDestino = "EUR";
            case 4 -> this.moedaDestino = "ARS";
            case 5 -> this.moedaDestino = "GBP";
            case 6 -> this.moedaDestino = "CAD";
            default -> System.out.println("Opção inválida. Tente novamente.");
        }

        this.consulta.setCodigoMoedaDestino(this.moedaDestino);
        System.out.println("Sua moeda de destino selecionada é: " + this.moedaDestino);
    }

    private void salvarConversao(Conversao conversao) {
        this.historicoConversoes.add(conversao);

        try {
            FileWriter writer = new FileWriter("historico_conversoes.json");

            try {
                Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
                gson.toJson(this.historicoConversoes, writer);
            } catch (Throwable var6) {
                try {
                    writer.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }

                throw var6;
            }

            writer.close();
        } catch (IOException var7) {
            IOException e = var7;
            System.out.println("Erro ao salvar conversões: " + e.getMessage());
        }

    }

    private void realizarConversao() {
        System.out.println("Digite o valor que deseja converter:");
        double valorParaConversao = this.leitura.nextDouble();
        System.out.println("----------------------------------------\nValor inserido: " + valorParaConversao + " " + this.moedaOrigem + ".");
        this.taxaDeCambio = this.consulta.calcularTaxaDeCambio(this.moedaOrigem, this.moedaDestino);
        System.out.println("A taxa de câmbio foi definida em: " + this.taxaDeCambio);
        double valorResultado = valorParaConversao * this.taxaDeCambio;
        System.out.println("Valor convertido: " + valorResultado + " " + this.moedaDestino + ".");
        Conversao conversao = new Conversao(this.moedaOrigem, this.moedaDestino, valorParaConversao, valorResultado, this.taxaDeCambio);
        this.salvarConversao(conversao);
    }
}
