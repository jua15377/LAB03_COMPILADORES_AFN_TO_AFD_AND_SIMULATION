/**
 * @author Jonnathan Juarez
 * @version 1.0 02/08/2017
 */

public class Estado {
    private boolean esFinal;
    private boolean esInical;
    private int identifiacador = 0;

    public Estado(boolean esInical, boolean esFinal, int id) {
        this.esInical = esInical;
        this.esFinal = esFinal;
        this.identifiacador = id;

    }
    public boolean getEsFinal() {
        return esFinal;
    }

    public void setFinal(boolean esFinal) {
        this.esFinal = esFinal;
    }

    public boolean getEsinicial() {
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
        return "Identificador: " + String.valueOf(identifiacador) +", Es inicial:" +esInical + ", Es final: " + esFinal;
    }
}
