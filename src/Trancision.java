/**
 * @author Jonnathan Juarez
 * @version 1.0 02/08/2017
 */

public class Trancision {
    private String simbolos;
    private Estado estadoInicial;
    private Estado estadoFinal;

    public Trancision(Estado estIni, Estado estFin, String simbolos){
        this.simbolos = simbolos;
        this.estadoInicial = estIni;
        this.estadoFinal = estFin;
    }

    public String getSimbolos() {
        return simbolos;
    }

    public void setSimbolos(String simbolos) {
        this.simbolos = simbolos;
    }

    public Estado getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(Estado estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public Estado getEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(Estado estadoFinal) {
        this.estadoFinal = estadoFinal;
    }

    @Override
    public boolean equals(Object obj) {
        Trancision t = (Trancision) obj;
        return t.getEstadoFinal() == estadoFinal && t.getEstadoInicial() == estadoInicial;
    }
    @Override
    public String toString(){
        return "[" + String.valueOf(estadoInicial.getIdentifiacador() )+
                ", " + simbolos + ", " +
                String.valueOf(estadoFinal.getIdentifiacador()) + "]";
    }
}
