import java.util.List;

public class SimuladorMain {

    public static void main(String[] args) {

        Hospital hospital = new Hospital();
        SimuladorUrgencia simulador = new SimuladorUrgencia(hospital);
        int pacientesPorDia = 200;

        System.out.println("--- Generando y guardando pacientes en el archivo ---");
        List<Paciente> pacientesGenerados = GeneradorPacientes.generar(pacientesPorDia);
        GeneradorPacientes.guardarPacientesEnArchivo(pacientesGenerados);
        System.out.println("----------------------------------------------------\n");

        System.out.println("--- Cargando pacientes desde el archivo para la simulación ---");
        List<Paciente> pacientesParaSimulacion = GeneradorPacientes.cargarPacientesDesdeArchivo();
        System.out.println("-------------------------------------------------------------\n");

        simulador.setListaDeLlegadas(pacientesParaSimulacion);

        simulador.simular();
    }
}