import java.util.*;

public class Hospital{
    Map<String,Paciente> pacientesTotales;
    public PriorityQueue<Paciente> colaAtencion;
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
    public Paciente atenderSiguiente(int minutoActual) {
        Paciente siguiente = colaAtencion.poll();
        if (siguiente != null) {
            long duracionConsulta = 15;
            siguiente.setEstado("atendido");
            siguiente.setTiempoInicioAtencion(minutoActual);
            siguiente.setTiempoAtencion(duracionConsulta);
            siguiente.setTiempoSalida(minutoActual + duracionConsulta);

            siguiente.registarCambio("Atendido en minuto " + minutoActual + ". Espera: " + siguiente.getTiempoEspera() + " min. Duración consulta: " + duracionConsulta + " min.");
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
