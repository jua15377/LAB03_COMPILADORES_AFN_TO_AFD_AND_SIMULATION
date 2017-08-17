import java.util.HashSet;
import java.util.Scanner;


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

        long startTime ;
        long finishTime;
        Double tiempo;

        //PEDIR REGEX AL USUARIO
        System.out.println("Ingrese la expresion regular del automata. Notose @ simboliza epsilon");
        entrada = teclado.nextLine();
        //pide cadena a usuario
        System.out.println("Ingrese la cadena a evaluar");
        String cadena = teclado.nextLine();
        String expresionPostfix = RegExConverter.infixToPostfix(entrada);
        System.out.println("Esta es la traduccion a postfix" + expresionPostfix);
        unaClase.determinarAlfabeto(expresionPostfix);

        startTime = System.nanoTime();
        Automata afn = unaClase.analizador(expresionPostfix);
        finishTime = System.nanoTime();
        tiempo = (finishTime - startTime)/1000000.0;
        System.out.println("Tiempo de cracion de nfa  es: ");
        System.out.println(tiempo +" ms");
        //genrartexto
        unaClase.crearTextFile(afn);


        /**
         * para DFA
         */
        startTime = System.nanoTime();
        AutomataAFD automataAFD = unaClase.convertToAFD(afn);
        finishTime = System.nanoTime();
        tiempo = (finishTime - startTime)/1000000.0;
        System.out.println("Tiempo de cracion de conversion de nfa a dfa  es: ");
        System.out.println(tiempo +" ms");
        unaClase.crearTextoAFD(automataAFD);


        //probando simulacion
        startTime = System.nanoTime();
        boolean result = unaClase.simuladorDFA(automataAFD, cadena);
        finishTime = System.nanoTime();
        tiempo = (finishTime - startTime)/1000000.0;
        System.out.println("Tiempo de simulacion del dfa  es: ");
        System.out.println(tiempo +" ms");
        if (result){System.out.println("si se acepta la cadena en dfa");}

        // para simulacion fna
        startTime = System.nanoTime();
        boolean result2 = unaClase.simuladorNFA(afn, cadena);
        finishTime = System.nanoTime();
        tiempo = (finishTime - startTime)/1000000.0;
        System.out.println("Tiempo de simulaicon del nfa es: ");
        System.out.println(tiempo +" ms");
        if (result2){System.out.println("si se acepta la cadena en nfa");}
    }
}
