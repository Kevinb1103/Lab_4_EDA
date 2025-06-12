import java.util.*;

public class Paciente implements Comparable<Paciente>{

    private String nombre;
    private String apellido;
    private String id;
    private int categoria;
    private String estado;
    private String area;
    public Stack<String> historialCambios;
    private long tiempoLlegada;
    private long tiempoInicioAtencion = -1;
    private long tiempoAtencion = -1;
    private long tiempoSalida = -1;

    public Paciente(String nombre, String apellido, String id, int categoria, long tiempoLlegada, String estado, String area) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.id = id;
        this.categoria = categoria;
        this.tiempoLlegada = tiempoLlegada;
        this.estado = estado;
        this.tiempoSalida=tiempoSalida;
        this.area = area;
        this.tiempoAtencion = -1;
        historialCambios = new Stack<>();
    }
    public long getTiempoInicioAtencion() { return tiempoInicioAtencion; }
    public void setTiempoInicioAtencion(long tiempoInicioAtencion) { this.tiempoInicioAtencion = tiempoInicioAtencion; }
    public void setTiempoAtencion(long tiempoAtencion) { this.tiempoAtencion = tiempoAtencion; }
    public long getTiempoSalida() { return tiempoSalida; }
    public void setTiempoSalida(long tiempoSalida) { this.tiempoSalida = tiempoSalida; }


    public void setTiempoLlegada(int tiempoLlegada) {
        this.tiempoLlegada = tiempoLlegada;
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
    public long getTiempoEspera() {
        if (tiempoInicioAtencion == -1) {
            return -1;
        }
        return tiempoInicioAtencion - tiempoLlegada;
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
    public void setTiempoAtencion(int tiempoAtencion) {
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
    public String toLinea() {
        return nombre + "," + apellido + "," + id + "," + categoria + "," + tiempoLlegada + "," + estado + "," + area;
    }
    public static Paciente fromLinea(String linea) {
        String[] partes = linea.split(",");
        return new Paciente(
                partes[0],
                partes[1],
                partes[2],
                Integer.parseInt(partes[3]),
                Long.parseLong(partes[4]),
                partes[5],
                partes.length > 6 ? partes[6] : ""
        );

    }
}