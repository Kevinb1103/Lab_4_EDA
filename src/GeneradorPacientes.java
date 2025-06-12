import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GeneradorPacientes {

    private static final String[] NOMBRES = {"Ana", "Luis", "Carlos", "Sofía", "Pedro", "Lucía", "Juan", "María", "Elena", "Miguel"};
    private static final String[] APELLIDOS = {"Pérez", "Gómez", "Rodríguez", "Fernández", "López", "García", "Martínez", "Sánchez"};
    private static final Random RAND = new Random();
    private static final String RUTA_ARCHIVO_PACIENTES = "Pacientes_24h.txt";

    public static List<Paciente> generar(int cantidad) {
        List<Paciente> pacientes = new ArrayList<>();

        long timestampInicio = 0;

        for (int i = 0; i < cantidad; i++) {
            String nombre = NOMBRES[RAND.nextInt(NOMBRES.length)];
            String apellido = APELLIDOS[RAND.nextInt(APELLIDOS.length)];
            String id = "P" + (i + 1);

            long tiempoLlegada = timestampInicio + (i * 10L);

            int categoria = generarCategoria();
            String estadoInicial = "en_espera";
            String areaInicial = "";

            Paciente paciente = new Paciente(nombre, apellido, id, categoria, tiempoLlegada, estadoInicial, areaInicial);
            pacientes.add(paciente);
        }
        System.out.println("Se generaron " + pacientes.size() + " pacientes aleatorios.");
        return pacientes;
    }

    private static int generarCategoria() {
        double prob = RAND.nextDouble();
        if (prob < 0.10) { // 10%
            return 1;
        } else if (prob < 0.25) { // 15% (0.10 + 0.15)
            return 2;
        } else if (prob < 0.43) { // 18% (0.25 + 0.18)
            return 3;
        } else if (prob < 0.70) { // 27% (0.43 + 0.27)
            return 4;
        } else { // 30%
            return 5;
        }
    }

    public static void guardarPacientesEnArchivo(List<Paciente> pacientes) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA_ARCHIVO_PACIENTES))) {
            for (Paciente paciente : pacientes) {
                writer.println(paciente.toLinea());
            }
            System.out.println("Se han registrado " + pacientes.size() + " pacientes en " + RUTA_ARCHIVO_PACIENTES);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de pacientes: " + e.getMessage());
        }
    }
    public static List<Paciente> cargarPacientesDesdeArchivo() {
        List<Paciente> pacientesCargados = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(RUTA_ARCHIVO_PACIENTES))) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                pacientesCargados.add(Paciente.fromLinea(linea));
            }
            System.out.println("Se han cargado " + pacientesCargados.size() + " pacientes desde " + RUTA_ARCHIVO_PACIENTES);
        } catch (FileNotFoundException e) {
            System.err.println("Error: El archivo " + RUTA_ARCHIVO_PACIENTES + " no se encontró. Asegúrate de haberlo generado antes.");
        } catch (Exception e) {
            System.err.println("Error al leer o parsear el archivo de pacientes: " + e.getMessage());
        }
        return pacientesCargados;
    }
    public static void main(String[] args) {
        int cantidadPacientes = 144;
        List<Paciente> pacientesGenerados = GeneradorPacientes.generar(cantidadPacientes);
        GeneradorPacientes.guardarPacientesEnArchivo(pacientesGenerados);
    }
}
