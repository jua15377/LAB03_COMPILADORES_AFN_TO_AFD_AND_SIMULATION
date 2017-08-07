import java.util.HashSet;

/**
 * @author Jonnathan Juarez
 * @version 1.0 02/08/2017
 */

public class TrancisionesAFD {
    private int identifiacador = 0;
    private boolean marcado ;
    private boolean esFinal;
    private boolean esInical;
    private String simbolo;
    private HashSet<Estado> conjuntoOrigen = new HashSet<>();
    private HashSet<Estado> conjuntoDestino = new HashSet<>();

    public TrancisionesAFD(HashSet<Estado> setInicial, HashSet<Estado> setFinales, String simbolo) {
        this.conjuntoOrigen = setInicial;
        this.conjuntoDestino = setFinales;
        this.simbolo = simbolo;

        for (Estado e: setInicial){
            if(e.getEsinicial()){
                setEsInical(true);
                break;
            }
            else if (e.getEsFinal()){
                setEsFinal(true);
                break;
            }

        }

    }

    public boolean isMarcado() {
        return marcado;
    }

    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }

    public boolean isEsFinal() {
        return esFinal;
    }

    public void setEsFinal(boolean esFinal) {
        this.esFinal = esFinal;
    }

    public boolean isEsInical() {
        return esInical;
    }

    public void setEsInical(boolean esInical) {
        this.esInical = esInical;
    }

    public int getIdentifiacador() {
        return identifiacador;
    }

    public void setIdentifiacador(int identifiacador) {
        this.identifiacador = identifiacador;
    }


    @Override
    public String toString(){
        return "[" + String.valueOf(conjuntoOrigen)+
                ", " + simbolo + ", " +
                String.valueOf(conjuntoDestino) + "]";
    }


    public HashSet<Estado> getConjuntoOrigen() {
        return conjuntoOrigen;
    }

    public void setConjuntoOrigen(HashSet<Estado> conjuntoOrigen) {
        this.conjuntoOrigen = conjuntoOrigen;
    }

    public HashSet<Estado> getConjuntoDestino() {
        return conjuntoDestino;
    }

    public void setConjuntoDestino(HashSet<Estado> conjuntoDestino) {
        this.conjuntoDestino = conjuntoDestino;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
}




