import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * @author Jonnathan Juarez
 * @version 1.0 02/08/2017
 */
public class Principal {
    public static void main(String args[]) {
//lee la cadena que contiene la exprexion regular
        Scanner teclado = new Scanner(System.in);
        SuperClaseHiperMegaPro unaClase = new SuperClaseHiperMegaPro();
        String entrada;


        //PEDIR REGEX AL USUARIO
        System.out.println("Ingrese la expresion regular del automata. Notose @ simboliza epsilon");
        entrada = teclado.nextLine();
        //entrada = "ab*";
        String expresionPostfix = RegExConverter.infixToPostfix(entrada);
        System.out.println(expresionPostfix);
        unaClase.determinarAlfabeto(expresionPostfix);

        //devuelve el alfabeto
        System.out.println("alfabeto");
        System.out.println(unaClase.getAlfabeto());
        long startTime = System.currentTimeMillis();
        Automata afn = unaClase.analizador(expresionPostfix);
        long finishTime = System.currentTimeMillis();
        long tiempo = finishTime - startTime;
        System.out.println("Tiempo es: ");
        System.out.println(tiempo);

        HashSet<Trancision> trans =  afn.getTransicoines();
        for(Trancision i : trans){
            System.out.println(i.toString());
        }
        System.out.println("el estado final es");
        System.out.println(afn.getEstadoFinal().getIdentifiacador());

        System.out.println("el estado inicial es");
        System.out.println(afn.getEstadoInicale().getIdentifiacador());
    }
}
