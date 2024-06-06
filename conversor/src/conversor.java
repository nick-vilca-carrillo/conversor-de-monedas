import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Scanner;

public class conversor {

    private static JsonObject conversionRates;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = false;

        do {
            try {
                // Interfaz de usuario para ingresar la moneda base
                System.out.println("Seleccione la moneda base (ARS, BOB, BRL, CLP, COP, USD): ");
                String monedaBase = scanner.next().toUpperCase();

                // Validar la moneda base
                if (!monedaBase.equals("ARS") && !monedaBase.equals("BOB") && !monedaBase.equals("BRL") &&
                        !monedaBase.equals("CLP") && !monedaBase.equals("COP") && !monedaBase.equals("USD")) {
                    System.out.println("Moneda base no válida. Seleccione entre ARS, BOB, BRL, CLP, COP, USD.");
                    return;
                }

                // Construir la URI con la moneda base proporcionada
                String uri = "https://v6.exchangerate-api.com/v6/1855cfc5ccb28a577772cc1a/latest/" + monedaBase;

                // Obtener el JSON desde la API
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(uri))
                        .build();


                // Parsear el JSON usando Gson
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
                conversionRates = jsonObject.getAsJsonObject("conversion_rates");

                // Interfaz de usuario simple para la conversión
                System.out.println("Ingrese la cantidad a convertir: ");
                double cantidad = scanner.nextDouble();
                System.out.println("Ingrese la moneda de destino (ARS, BOB, BRL, CLP, COP, USD): ");
                String monedaDestino = scanner.next().toUpperCase();

                // Validar la moneda destino
                if (!monedaDestino.equals("ARS") && !monedaDestino.equals("BOB") && !monedaDestino.equals("BRL") &&
                        !monedaDestino.equals("CLP") && !monedaDestino.equals("COP") && !monedaDestino.equals("USD")) {
                    System.out.println("Moneda de destino no válida. Seleccione entre ARS, BOB, BRL, CLP, COP, USD.");
                    return;
                }

                double resultado = convertir(monedaBase, monedaDestino, cantidad);
                System.out.println(cantidad + " " + monedaBase + " es igual a " + resultado + " " + monedaDestino);

                // Guardar la conversión en el archivo
                guardarConversion(monedaBase, monedaDestino, cantidad, resultado);

                // Preguntar al usuario si desea realizar otra conversión
                System.out.println("¿Desea realizar otra conversión? (Sí/No): ");
                String respuesta = scanner.next().toUpperCase();
                continuar = respuesta.equals("SI") || respuesta.equals("SÍ");

            } catch (IOException | InterruptedException e) {
                System.err.println("Error al obtener o analizar los datos: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error inesperado: " + e.getMessage());
            }
        } while (continuar);
    }

    public static double convertir(String monedaOrigen, String monedaDestino, double cantidad) {
        double tasaOrigen = conversionRates.get(monedaOrigen).getAsDouble();
        double tasaDestino = conversionRates.get(monedaDestino).getAsDouble();
        return (cantidad / tasaOrigen) * tasaDestino;
    }

    public static void guardarConversion(String monedaOrigen, String monedaDestino, double cantidad, double resultado) {
        try {
            FileWriter fw = new FileWriter("conversiones.txt", true); // El true indica que se va a añadir al archivo existente
            PrintWriter pw = new PrintWriter(fw);
            pw.println(monedaOrigen + " " + cantidad + " = " + monedaDestino + " " + resultado);
            pw.close();
        } catch (IOException e) {
            System.err.println("Error al guardar la conversión: " + e.getMessage());
        }
    }
}






