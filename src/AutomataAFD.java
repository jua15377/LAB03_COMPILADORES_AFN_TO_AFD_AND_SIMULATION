/*
* Tomando de referencia la clase DirectedGraph
* encontrada en:
* https://github.com/shaunak1111/Directed-Graph-Implementation-java/blob/master/DirectedGraph.java*/


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AutomataAFD {
    /*
    * atributos donde se guarada la informacion del automata*/
    private  HashSet<EstadoAFD> estados = new HashSet<>();
    private  HashSet<TrancisionesAFD> transicoines = new HashSet<>();
    private  HashMap<Integer, EstadoAFD> mapaDeEstados = new HashMap<Integer,EstadoAFD>();
    /*
    * Constructor
    * */
    public AutomataAFD(){
    }

    /**
     * agregar nodos
     */
    public void addEstado(AutomataAFD a, EstadoAFD estadoAgregar){
        a.estados.add(estadoAgregar);
        a.mapaDeEstados.put(estadoAgregar.getIdentifiacador(),estadoAgregar);
    }
    /**
     * Crea y agrega una transicion
     */
    public void addTrancion(AutomataAFD a, EstadoAFD estInicial, EstadoAFD estFinal, String transition ){
        TrancisionesAFD t = new TrancisionesAFD(estInicial, estFinal, transition);
        a.transicoines.add(t);
    }

    /**
     * Agregar una transicion existente
     */
    public void addTrancion(AutomataAFD a, TrancisionesAFD t){
        a.transicoines.add(t);

    }


    /**
     * Obtiene el nodo inicial del automata
     */
    public EstadoAFD getEstadoInicale(){
        HashSet<EstadoAFD> estados = getEstados();
        for (EstadoAFD i : estados) {
            // Buscar el nodo inicial
            if (i.getEsinicial()){
                return i;
            }
        }return null;
    }

    /**
     * Obtiene el nodo final del automata
     */
    public ArrayList<EstadoAFD> getEstadoFinal(){
        ArrayList<EstadoAFD> estadosFinales = new ArrayList<>();
        HashSet<EstadoAFD> estados = getEstados();
        for (EstadoAFD i : estados) {
            // Buscar el nodo inicial
            if (i.getEsFinal()){
                estadosFinales.add(i);
            }

        }
        if (!estadosFinales.isEmpty()){

            return estadosFinales;
        }
        else {
            return null;
        }
    }
    /**
     * Obtiene un nodo especifico
     */
    public  EstadoAFD getEstadoEspecifico(int id){
        if (mapaDeEstados.containsKey(id))
            return mapaDeEstados.get(id);
//        encaso no exista devulve null
        return null;
    }

    /**
     * setter y getters
     */
    public HashSet<EstadoAFD> getEstados() {
        return estados;
    }

    public HashSet<TrancisionesAFD> getTransicoines() {
        return transicoines;
    }

    public HashMap<Integer, EstadoAFD> getMapaDeEstados() {
        return mapaDeEstados;
    }

    public void setMapaDeEstados(HashMap<Integer, EstadoAFD> mapaDeEstados) {
        this.mapaDeEstados = mapaDeEstados;
    }
}
