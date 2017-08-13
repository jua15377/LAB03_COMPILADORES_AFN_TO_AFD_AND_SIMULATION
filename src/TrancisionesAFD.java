import java.util.HashSet;

/**
 * @author Jonnathan Juarez
 * @version 1.0 02/08/2017
 */

public class TrancisionesAFD {
    private String simbolo;
    private EstadoAFD conjuntoOrigen;
    private EstadoAFD conjuntoDestino;

    public TrancisionesAFD(EstadoAFD setInicial, EstadoAFD setFinales, String simbolo) {
        this.conjuntoOrigen = setInicial;
        this.conjuntoDestino = setFinales;
        this.simbolo = simbolo;

    }


    @Override
    public String toString(){
        return "[" + String.valueOf(conjuntoOrigen.getIdentifiacador())+ ", " + simbolo + ", " + String.valueOf(conjuntoDestino.getIdentifiacador()) + "]";
    }


    public EstadoAFD getConjuntoOrigen() {
        return conjuntoOrigen;
    }

    public void setConjuntoOrigen(EstadoAFD conjuntoOrigen) {
        this.conjuntoOrigen = conjuntoOrigen;
    }

    public EstadoAFD getConjuntoDestino() {
        return conjuntoDestino;
    }

    public void setConjuntoDestino(EstadoAFD conjuntoDestino) {
        this.conjuntoDestino = conjuntoDestino;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

}




