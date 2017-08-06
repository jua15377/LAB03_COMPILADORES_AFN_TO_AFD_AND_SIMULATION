
import jdk.nashorn.internal.ir.WhileNode;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
/**
 * @author Jonnathan Juarez
 * @version 1.0 02/08/2017
 */
public class SuperClaseHiperMegaPro {

    private List<String> alfabeto = new ArrayList<String>();
    private ArrayList<String> operadoresList = new ArrayList<String>(Arrays.asList(".", "|", "*", "+", "?", "^","@"));
    private static Stack<Automata> stackDeAutomatas = new Stack<>();
    public static int contador = 0;
    //constructor
    public SuperClaseHiperMegaPro() {

    }

    //determina los simbolos que forma en el alfabeto
    public void determinarAlfabeto(String postRegex) {
        char[] simbolos = postRegex.toCharArray();
        for (int i = 0; i < simbolos.length; i++) {
            char letra = simbolos[i];
            if (alfabeto.contains(Character.toString(letra)) == false && operadoresList.contains(Character.toString(letra)) == false) {
                alfabeto.add(Character.toString(letra));
            }
        }
    }

    public List<String> getAlfabeto() {
        return alfabeto;
    }

    //trabajo en progreso
    public String analizadorDeAbreviaturas(String cadenaRegex) {
        String nuevaCadena = "";
        for (int i = 0; i < cadenaRegex.length(); i++) {
            //cambia el simbolo de abreviatura por su valor origina
            if (String.valueOf(cadenaRegex.charAt(i)).equals("+")) {
                nuevaCadena += "(" + String.valueOf(cadenaRegex.charAt(i - 1)) + String.valueOf(cadenaRegex.charAt(i - 1)) + "*)";
            } else if (String.valueOf(cadenaRegex.charAt(i)).equals("?")) {
                nuevaCadena += "(" + String.valueOf(cadenaRegex.charAt(i - 1)) + "|@)";
            } else {
                nuevaCadena += String.valueOf(cadenaRegex.charAt(i));
            }
        }
        return nuevaCadena;
    }
    /**
     * Evalula la expresion y determina que operacion hacer, add or pop del stack y cuantas veces hacerlo
     *
     */
    public Automata analizador(String expresionENPostFix){

        for (int i = 0; i < expresionENPostFix.length(); i++){
            char simboloAct = expresionENPostFix.charAt(i);
//            necesita de dos automatas para proceder
            if (simboloAct == '|' || simboloAct == '.') {
                if (simboloAct == '.'){
//                    extrae los automatas a trabajar
                    Automata b = stackDeAutomatas.pop();
                    Automata a = stackDeAutomatas.pop();
                    Automata resultante = crearConcatenacion(a, b);
                    stackDeAutomatas.add(resultante);
                }
                else if(simboloAct == '|'){
//                    extrae los automatas a trabajar
                    Automata b = stackDeAutomatas.pop();
                    Automata a = stackDeAutomatas.pop();
                    Automata resultante = crearOr(a, b);
                    stackDeAutomatas.add(resultante);

                }
            }
//            solo necesita de un automata
            else if(simboloAct == '*' || simboloAct == '?' || simboloAct == '+'){
                if (simboloAct == '*'){
//                    extrae el automata a trabajar
                    Automata a = stackDeAutomatas.pop();
                    Automata resultante = crearkleene(a);
                    stackDeAutomatas.add(resultante);
                }
                else if(simboloAct == '?'){
                    Automata a = stackDeAutomatas.pop();
                    Automata resultante = crearOptional(a);
                    stackDeAutomatas.add(resultante);
                }
                else if(simboloAct == '+'){
                    Automata a = stackDeAutomatas.pop();
                    Automata resultante = crearPlusKleene(a);
                    stackDeAutomatas.add(resultante);
                }
            }
//            no hace ningua operacion
            else {
                Automata automata = crearAutomataSimple(String.valueOf(simboloAct));
                stackDeAutomatas.add(automata);
            }
        }
        Automata automataFinal = stackDeAutomatas.pop();
        crearTextFile(automataFinal);
        return automataFinal;
    }
    public Automata crearAutomataSimple(String simbolo){
        Automata a = new Automata();
        a.addEstado(a,contador,true,false);
        a.addEstado(a,contador+1, false, true);
        a.addTrancion(a,a.getEstadoInicale(),a.getEstadoFinal(),simbolo);
        contador +=2;
        return a;
    }
    public Automata crearConcatenacion(Automata a, Automata b){
        HashSet<Estado> estadosB = b.getEstados();
        HashSet<Trancision> transicoinesB = b.getTransicoines();
        Estado aNodoFinal = a.getEstadoFinal();
//        agregar tranciones
        // Copiar cada estado a automata 1 sin copiar estado inicial anterior
        for (Estado i: estadosB) {
            if (!i.getEsinicial()){
                a.addEstado(a, i);
            }
        }

        // Copiar cada transicion a automata 1 cambiando la transicion del nodo inicial anterior
        for (Trancision t: transicoinesB) {
            if (t.getEstadoInicial().getEsinicial()){
                t.setEstadoInicial(aNodoFinal);  // Cambiar nodo inicial de transicion
                //aNodoFinal.setFinal(false);  // Quitarle propiedad de estado final a op1FinalNode
                a.addTrancion(a, t);  // Agregar a nuevo automata

            }
            else if (t.getEstadoFinal().getEsinicial()){
                t.setEstadoFinal(aNodoFinal);
                a.addTrancion(a, t);
            }
            else {
                a.addTrancion(a, t);
            }
        }
        aNodoFinal.setFinal(false);  // Quitarle propiedad de estado final a op1FinalNode
        return a;
    }


    public Automata crearOr(Automata a, Automata b){
        //crear estado final
        Estado i = new Estado(true,false,contador);
        // crear estado final.
        Estado f = new Estado(false, true,contador+1);
        Automata resultamte = new Automata();
        //agrega los estados
        resultamte.addEstado(resultamte, i);
        contador +=2;
        //obtener el los estados y transicone de ambos automatas
        HashSet<Estado> estadosA = a.getEstados();
        HashSet<Estado> estadosB = b.getEstados();
        HashSet<Trancision> transA = a.getTransicoines();
        HashSet<Trancision> transB = b.getTransicoines();
        //crear transicones inicales
        Trancision t1 = new Trancision(resultamte.getEstadoInicale(),a.getEstadoInicale(),"@");
        Trancision t2 = new Trancision(resultamte.getEstadoInicale(),b.getEstadoInicale(),"@");
        Trancision t3 = new Trancision(a.getEstadoFinal(),f ,"@");
        Trancision t4 = new Trancision(b.getEstadoFinal(),f ,"@");
        resultamte.addEstado(resultamte, f);
        //crear transciones del estado incial a los automastas
        resultamte.addTrancion(resultamte,t1);
        resultamte.addTrancion(resultamte,t2);
        resultamte.addTrancion(resultamte,t3);
        resultamte.addTrancion(resultamte,t4);
        //cambiando propiedades de los estados de los antiguos nodos
        a.getEstadoInicale().setEsInical(false);
        a.getEstadoFinal().setFinal(false);
        b.getEstadoInicale().setEsInical(false);
        b.getEstadoFinal().setFinal(false);
        //agregar transiciones de A
        for (Trancision t: transA){
            resultamte.addTrancion(resultamte, t);
        }
        //agrega estados de A
        for(Estado e: estadosA){
            resultamte.addEstado(resultamte, e);
        }
    //agregar transiciones de b
        for (Trancision t: transB){
            resultamte.addTrancion(resultamte, t);
        }
    //agrega estados de A
        for(Estado e: estadosB){
        resultamte.addEstado(resultamte, e);
    }
        //devolver el atomata generado
        return resultamte;
    }


    public Automata crearkleene(Automata a){
        //estados ficticios extras.
        Automata i = crearAutomataSimple("@");
        //crea estado final
        Automata f = crearAutomataSimple("@");
        contador += 2;
        //crea trnasicion entre el nodo final y el inicial del automata base
        Trancision t1 = new Trancision(a.getEstadoFinal(),a.getEstadoInicale(), "@");
        a.addTrancion(a, t1);
        //agrega estado inicial
        Automata a2 = crearConcatenacion(i,a);
        //concatena el automata con el estado final ficticio
        Automata a3 = crearConcatenacion(a2,f);
        //crea la ultima transicion que permite al automata saltar del inicio al final
        Trancision t2 = new Trancision(a3.getEstadoInicale(), a3.getEstadoFinal(),"@");
        a3.addTrancion(a3, t2);
        //devueleve el nuevo automata
        return a3;
    }
    public Automata crearOptional(Automata b){
        Automata a = crearAutomataSimple("@");
        Automata result = crearOr(b,a);
        return result;
    }
    public Automata crearPlusKleene(Automata automata){
        //estados ficticios extras.
        Estado i = new Estado(true,false,contador);
        //crea estado final
        Automata f = crearAutomataSimple("@");
        //crea trnasicion entre el nodo final y el inicial del automata base
        Trancision t1 = new Trancision(automata.getEstadoFinal(),automata.getEstadoInicale(), "@");
        automata.addTrancion(automata , t1);
        //agrega estado inicial
        Trancision t2 = new Trancision(i, automata.getEstadoInicale(), "@");
        automata.addEstado(automata , i);
        //"desactiva " el estado inicial anterior , cono inicial
        automata.getEstadoInicale().setEsInical(false);
        automata.addTrancion(automata , t2);
        //concatena el automata con el estado final ficticio
        Automata a2 = crearConcatenacion(automata ,f);
        //devueleve el nuevo automata
        return a2;
    }
    // metodo que se encarga de crear el archiv de texto
    public void crearTextFile(Automata automata){
        /**
         * ESTADOS = {0, 1,… n}
         SIMBOLOS = {a, b, c,… z}
         INICIO = {0}
         ACEPTACION = {0, 1,… n}
         TRANSICION = (0, a, 1)-(0, e, 2)- … (3, b, n)
         */
        //agrega la informacion de los estados del automata
        String texto = "";
        HashSet<Estado> estados = automata.getEstados();
        texto += "ESTADOS = {";
        int contador = 0;
        for (Estado e: estados) {
            texto += String.valueOf((e.getIdentifiacador()));
            if (contador < estados.size()-1){
                texto  += ", ";
            }
            else {
                texto += "}\n";
            }
            contador ++;
        }

        texto += "En total son: " + contador + " estados\n";
        // agrega simbolos del alfabeto
        texto += "SIMBOLOS = " + getAlfabeto() +"\n";
        // agregar nodo final e inicial
        texto +=  "INICIO = {"+ String.valueOf(automata.getEstadoInicale().getIdentifiacador()) +"}\n";
        texto +=  "ACEPTACION = {"+ String.valueOf(automata.getEstadoFinal().getIdentifiacador()) +"}\n";
        // agregando transcione
        HashSet<Trancision> transiones = automata.getTransicoines();
        texto += "TRANCIONES = {";
        int contador2 = 0;
        for (Trancision t: transiones) {
            texto += t.toString();
            if (contador2 < transiones.size()-1){
                texto  += "-";
            }
            else {
                texto += "}";
            }
            contador2 ++;
        }
        texto += "\nEn total son: " + contador2 + " transiones\n";
        try {
            Writer output;
            output = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "\\Resultados.txt"));  //clears file every time
            output.append(texto);
            output.close();
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }

    }



    public HashSet<Estado> eClousureT(HashSet<Estado> estados, HashSet<Trancision> trancisions){
        /**
         * obtener todos los estados (lista de estados a parti de conjunto)
         * obtenes todas las trancicisones
         *
         * crear conjuntos de etaddos nuevos vacio
         * mientra haya estaos en la lista
         *      paara cada trancion
         *      si el estado que se saco de la lista es igual al estado inciial de la transicon
         *            si el simbolo es @ entonces agregar al conjuntoNuevo de estados
         *      si no pasar
         *
         */

        Stack<Estado> estadosAanalizar = new Stack<>();
        estadosAanalizar.addAll(estados);
        HashSet<Estado> estadoNuevos = new HashSet<>();
        estadoNuevos.addAll(estados);
        while (!estadosAanalizar.isEmpty()) {
            Estado e = estadosAanalizar.pop();
            estadoNuevos.addAll(eClousureS2(e, trancisions));

        }
        //if(estadoNuevos){}
        return estadoNuevos;
    }
    public HashSet<Estado> eClousureS2(Estado estado, HashSet<Trancision> trancisionesDelAutomata){
        HashSet<Estado> estadoAlcanzados = new HashSet<>();
        System.out.println("en el emtodo " + estado);
        estadoAlcanzados.add(estado);
        for (Trancision t : trancisionesDelAutomata) {
            if(t.getEstadoInicial()== estado && t.getSimbolos() == "@") {
                estadoAlcanzados.add(t.getEstadoFinal());
            }
        }
        return  estadoAlcanzados;
    }
    public Automata renumerar(Automata automata){
        HashSet<Estado> est =  automata.getEstados();
        int nuevaNumeracion = 0;
        HashMap<Integer, Estado> nuevoMapa = new HashMap<>();
        for (Estado e : est){
            e.setIdentifiacador(nuevaNumeracion);
            nuevoMapa.put(nuevaNumeracion, e);
            nuevaNumeracion ++;
        }
        automata.setMapaDeEstados(nuevoMapa);
        return automata;
    }
}
