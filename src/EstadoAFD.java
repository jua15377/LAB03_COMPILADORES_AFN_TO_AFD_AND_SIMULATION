import java.util.HashSet;
/**
 * @author Jonnathan Juarez
 * @version 1.0 02/08/2017
 */

public class EstadoAFD extends RegExConverter {
    private boolean esFinal;
    private boolean esInical;
    private HashSet<Estado> estadosQueContiene = new HashSet<>();
    private int identifiacador = 0;

    public EstadoAFD(int id, HashSet<Estado> estdos) {
        this.estadosQueContiene = estdos;
        this.identifiacador = id;

        for (Estado e: estdos){
            if(e.getEsinicial()){
                setInical(true);
                break;
            }
            else if (e.getEsFinal()){
                setFinal(true);
                break;
            }

        }

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

    public void setInical(boolean esInical) {
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
        return "Id: " + String.valueOf(identifiacador);// +", Es ini:" +esInical + ", Es fin: " + esFinal;
    }

    public HashSet<Estado> getEstadosQueContiene() {
        return estadosQueContiene;
    }

    public void setEstadosQueContiene(HashSet<Estado> estadosQueContiene) {
        this.estadosQueContiene = estadosQueContiene;
    }
}
