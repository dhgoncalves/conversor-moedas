import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class ConsultaCotacao {
    private final String chaveAPI = "bb8179d0a922e70274dc6e54";
    private String codigoMoedaOrigem;
    private String codigoMoedaDestino;
    private double taxaDeCambio;

    public ConsultaCotacao() {
    }

    public void setCodigoMoedaOrigem(String codigoMoedaOrigem) {
        this.codigoMoedaOrigem = codigoMoedaOrigem;
    }

    public void setCodigoMoedaDestino(String codigoMoedaDestino) {
        this.codigoMoedaDestino = codigoMoedaDestino;
    }

    public double calcularTaxaDeCambio(String codigoMoedaInicial, String codigoMoedaFinal) {
        String url = "https://v6.exchangerate-api.com/v6/bb8179d0a922e70274dc6e54/pair/" + this.codigoMoedaOrigem + "/" + this.codigoMoedaDestino;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            Gson gson = new Gson();
            JsonObject jsonResponse = (JsonObject)gson.fromJson((String)response.body(), JsonObject.class);
            return this.taxaDeCambio = jsonResponse.get("conversion_rate").getAsDouble();
        } catch (Exception var9) {
            System.out.println("Erro na aplicação. Não foi possivel obter a taxa de câmbio.");
            return this.taxaDeCambio;
        }
    }
}
