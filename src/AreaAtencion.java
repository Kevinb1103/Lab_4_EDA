import java.util.*;

public class AreaAtencion{
    private String nombre;
    private PriorityQueue<Paciente> pacientesHeap;
    private int capacidadMaxima;

    public AreaAtencion(String nombre, int capacidadMaxima) {
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
        this.pacientesHeap = new PriorityQueue<>(new Comparator<Paciente>( ) {
            public int compare(Paciente p1, Paciente p2) {
                if (p1.getCategoria() != p2.getCategoria()) {
                    return Integer.compare(p1.getCategoria(), p2.getCategoria()); // menor categoría = más urgente
                } else {
                    return Long.compare(p1.getTiempoLlegada(), p2.getTiempoLlegada()); // más antiguo = más urgente
                }
            }
        });
    }
    public boolean estaSaturada(){
        return pacientesHeap.size() >= capacidadMaxima;
    }
    public void ingresarPaciente(Paciente p){
        if(!estaSaturada()){
            pacientesHeap.offer(p);
        } else{
            System.out.println("saturado, no se puede ingresar paciente");
        }
    }
    public Paciente atenderPaciente(){
        return pacientesHeap.poll();
    }
    public List<Paciente> obtenerPacientesPorHeapSort() {
        List<Paciente> pacientes = new ArrayList<>(pacientesHeap);
        pacientes.sort(null);
        return pacientes;
    }

}