import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimuladorUrgencia {

    private static final Map<Integer, Integer> TIEMPO_MAX_ESPERA = Map.of(
            1, 10, 2, 30, 3, 90, 4, 120, 5, 240
    );

    private Hospital hospital;
    private List<Paciente> listaDeLlegadas;
    private int contadorNuevosPacientes = 0;
    private int proximoIndiceLlegada = 0;

    public SimuladorUrgencia(Hospital hospital) {
        this.hospital = hospital;

    }

    public void setListaDeLlegadas(List<Paciente> listaDeLlegadas) {
        this.listaDeLlegadas = listaDeLlegadas;
        this.proximoIndiceLlegada = 0;
    }

    public void simular() { // <--- ¡Cambio aquí! Se eliminó 'int pacientesPorDia'
        if (listaDeLlegadas == null || listaDeLlegadas.isEmpty()) {
            System.out.println("Error: No se ha establecido una lista de pacientes para la simulación. Asegúrate de que el archivo existe y se pudo leer.");
            return; // Salir si no hay pacientes para simular
        }
        System.out.println("Simulación iniciada con " + listaDeLlegadas.size() + " pacientes programados.");

        for (int minutoActual = 0; minutoActual < 1440; minutoActual++) {

            if (proximoIndiceLlegada < listaDeLlegadas.size() &&
                    listaDeLlegadas.get(proximoIndiceLlegada).getTiempoLlegada() == minutoActual) {

                Paciente nuevoPaciente = listaDeLlegadas.get(proximoIndiceLlegada);
                hospital.registrarPaciente(nuevoPaciente);
                System.out.println("Minuto " + minutoActual + ":  Llega paciente " + nuevoPaciente.getId() + " (C" + nuevoPaciente.getCategoria() + ")");
                contadorNuevosPacientes++;
                proximoIndiceLlegada++;
            }

            atenderPorSLA(minutoActual);

            if (contadorNuevosPacientes >= 3) {
                System.out.println("Minuto " + minutoActual + "::️ Se acumularon 3 pacientes. Atendiendo a 2.");
                atenderPacientes(2, minutoActual);
                contadorNuevosPacientes = 0; // Reiniciar contador
            }

            if (minutoActual > 0 && minutoActual % 15 == 0) {
                System.out.println("Minuto " + minutoActual + ":  Pasaron 15 mins. Atendiendo a 1 paciente.");
                atenderPacientes(1, minutoActual);
            }

            if (minutoActual == 120) { // En la hora 2
                if (hospital.pacientesTotales.containsKey("P10")) {
                    System.out.println("Minuto " + minutoActual + ":  Paciente P10 mal categorizado. Cambiando de C3 a C1.");
                    hospital.reasignarCategoria("P10", 1);
                }
            }
        }

        System.out.println("\n--- SIMULACIÓN FINALIZADA ---");
        mostrarEstadisticas();
    }

    private void atenderPacientes(int cantidad, int minutoActual) {
        for (int i = 0; i < cantidad; i++) {
            if (!hospital.colaAtencion.isEmpty()) {
                Paciente atendido = hospital.atenderSiguiente(minutoActual);
                System.out.println("    -> ️ Atendido: " + atendido.getId() + " (C" + atendido.getCategoria() + "). Esperó " + atendido.getTiempoEspera() + " min.");
            }
        }
    }

    private void atenderPorSLA(int minutoActual) {
        List<Paciente> pacientesEnCola = new ArrayList<>(hospital.colaAtencion);
        for (Paciente p : pacientesEnCola) {
            long tiempoEsperando = minutoActual - p.getTiempoLlegada();
            if (TIEMPO_MAX_ESPERA.containsKey(p.getCategoria()) && tiempoEsperando > TIEMPO_MAX_ESPERA.get(p.getCategoria())) {
                System.out.println("Minuto " + minutoActual + ":  ¡ALERTA SLA! Paciente " + p.getId() + " (C" + p.getCategoria() + ") ha esperado " + tiempoEsperando + " min. ¡Atención inmediata!");
                hospital.colaAtencion.remove(p); // Lo sacamos de la cola

                p.setEstado("atendido");
                p.setTiempoInicioAtencion(minutoActual);
                p.setTiempoSalida(minutoActual);
                p.registarCambio("Atendido por VIOLACIÓN DE SLA en minuto " + minutoActual);
                hospital.getPacientesAtendidos().add(p);

            }
        }
    }

    public void mostrarEstadisticas() {
        List<Paciente> atendidos = hospital.getPacientesAtendidos();
        System.out.println("Total de pacientes atendidos: " + atendidos.size());
        System.out.println("Pacientes que quedaron en espera: " + hospital.colaAtencion.size());

        Map<Integer, List<Paciente>> porCategoria = atendidos.stream().collect(Collectors.groupingBy(Paciente::getCategoria));

        System.out.println("\n--- Tiempos de Espera Promedio por Categoría ---");
        for (int i = 1; i <= 5; i++) {
            if (porCategoria.containsKey(i)) {
                double avg = porCategoria.get(i).stream().mapToLong(Paciente::getTiempoEspera).average().orElse(0.0);
                System.out.printf("Categoría C%d: %.2f minutos (%d pacientes atendidos)\n", i, avg, porCategoria.get(i).size());
            } else {
                System.out.printf("Categoría C%d: N/A (0 pacientes atendidos)\n", i);
            }
        }

        Paciente pPrueba = hospital.pacientesTotales.get("P10");
        if (pPrueba != null) {
            System.out.println("\n--- Historial del paciente P10 ---");
            List<String> cambiosEnOrden = new ArrayList<>();
            while(!pPrueba.historialCambios.isEmpty()) {
                cambiosEnOrden.add(0, pPrueba.obtenerUltimoCambio()); // Añadir al principio para invertir el orden
            }
            for (String cambio : cambiosEnOrden) {
                System.out.println(cambio);
            }
        }
    }
}