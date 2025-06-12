
import java.util.*;

class Paciente implements Comparable<Paciente>{

    private String nombre;
    private String apellido;
    private String id;
    private int categoria;
    private long tiempoLlegada;
    private String estado;
    private String area;
    private long tiempoAtencion;
    private Stack<String> historialCambios;

    public Paciente(String nombre, String apellido, String id, int categoria, long tiempoLlegada, String estado, String area) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.id = id;
        this.categoria = categoria;
        this.tiempoLlegada = tiempoLlegada;
        this.estado = estado;
        this.area = area;
        this.tiempoAtencion = -1;
        historialCambios = new Stack<>();
    }
    public String getNombre() {
        return nombre;
    }
    public int getCategoria() {
        return categoria;
    }
    public long getTiempoLlegada() {
        return tiempoLlegada;
    }
    public String getEstado() {
        return estado;
    }
    public String getId() {
        return id;
    }
    public String getApellido() {
        return apellido;
    }
    public String getArea() {
        return area;
    }
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public long getTiempoAtencion() {
        return tiempoAtencion;
    }
    public void setTiempoAtencion(long tiempoAtencion) {
        this.tiempoAtencion = tiempoAtencion;
    }
    public long tiempoEsperaActual(long tiempoActual) {
        return tiempoActual-tiempoLlegada;
    }
    public void registarCambio(String descripcion) {
        historialCambios.push(descripcion);
    }
    public String obtenerUltimoCambio() {
        return historialCambios.pop();
    }
    public int compareTo(Paciente paciente) {
        if(this.categoria != paciente.categoria) {
            return Integer.compare(this.categoria, paciente.categoria);
        }
        return Long.compare(this.tiempoLlegada, paciente.tiempoLlegada);
    }

}
class AreaAtencion{
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
class Hospital{
    private Map<String,Paciente> pacientesTotales;
    private PriorityQueue<Paciente> colaAtencion;
    private Map<String,AreaAtencion> areasAtencion;
    private List<Paciente> pacientesAtendidos;

    public Hospital(){
        pacientesTotales = new HashMap<>();
        colaAtencion = new PriorityQueue<>();
        areasAtencion = new HashMap<>();
        pacientesAtendidos = new ArrayList<>();
        areasAtencion.put("SAPU",new AreaAtencion("sapu",100));
        areasAtencion.put("urgencia_adulto", new AreaAtencion("urgencia_adulto", 100));
        areasAtencion.put("infantil", new AreaAtencion("infantil", 100));
    }

    public void registrarPaciente(Paciente p){
        pacientesTotales.put(p.getId(),p);
        colaAtencion.offer(p);
        p.registarCambio("registrado.");
    }
    public void reasignarCategoria(String id, int nuevaCategoria){
        Paciente p=pacientesTotales.get(id);
        if(p!=null){
            p.setCategoria(nuevaCategoria);
            p.registarCambio("reasignado a categoria: ."+ nuevaCategoria);

        }
    }
    public List<Paciente> obtenerPacientesPorCategoria(int Categoria) {
        List<Paciente> pacientes = new ArrayList<>();
        for(Paciente p: colaAtencion){
            if(p.getCategoria() == Categoria){
                pacientes.add(p);
            }
        }
        return pacientes;
    }
    public Paciente atenderSiguiente() {
        Paciente siguiente = colaAtencion.poll();
        if (siguiente != null) {
            siguiente.setEstado("atendido");
            pacientesAtendidos.add(siguiente);
        }
        return siguiente;
    }
    public AreaAtencion obtenerArea(String nombre){
        return areasAtencion.get(nombre);
    }

    public List<Paciente> getPacientesAtendidos() {
        return pacientesAtendidos;
    }
}