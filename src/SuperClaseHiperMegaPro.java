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
        //crearTextFile(automataFinal);
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
        texto += "\nEn total son: " + contador2 + " transiciones\n";
        try {
            Writer output;
            output = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "\\AFN.txt"));  //clears file every time
            output.append(texto);
            output.close();
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }

    }



    public HashSet<Estado> eClousureT(HashSet<Estado> s, HashSet<Trancision> trancisions){
        //agregar los estados del conjunto de entrada a un stack
        Stack<Estado> estadosAanalizar = new Stack<>();
        estadosAanalizar.addAll(s);
        //agregar los estasods del conjunto S al eclosure
        HashSet<Estado> resultadoEclosure = new HashSet<>();
        resultadoEclosure.addAll(s);
        //mientraas el stack no este vacio
        while (!estadosAanalizar.isEmpty()) {
            //saca un elemento e del stack
            Estado e = estadosAanalizar.pop();
            //crea el cojunto de estado de lacerradura de u con epsion
            HashSet<Estado> uConjunto = moveDeEstado(e,trancisions,"@");
            //para cada uno de los estados de la pila revisa si ya esta en el conjunto resultant3
            for (Estado u: uConjunto){
                if(!resultadoEclosure.contains(u)){
                    resultadoEclosure.add(u);
                    estadosAanalizar.add(u);
                }
            }
        }
        return resultadoEclosure;
    }


    /**
     * funcion moveDeConjuntos
     * @param estado conjunto de estados a analizar
     * @param trancisionesDelAutomata sirve para evaluar la condicion de existencia de una
     *                                transiconn.
     * @param simbolo el simbolo que debe tener la transicion.
     * @return conmunto con todos los estados que se alcanzaron partir del resultado
     */
    public HashSet<Estado> moveDeConjuntos(HashSet<Estado> estado, HashSet<Trancision> trancisionesDelAutomata, String simbolo){
        //crea stack con los estados a analizar
        Stack<Estado> estadosAnalizar = new Stack<>();
        for(Estado e : estado){estadosAnalizar.add(e);}
        //crea el conjunto de estaddos resultantes
        HashSet<Estado> estadoAlcanzados = new HashSet<>();
        //revisa en cada trancion que cumpla con que el estado inical es estado que se saco del srack
        while (!estadosAnalizar.empty()) {
            Estado e = estadosAnalizar.pop();
            for (Trancision t : trancisionesDelAutomata) {
                //si se cuple que hay una transico con ese simbolo
                String simTransitiocn = t.getSimbolos();
                if (t.getEstadoInicial().equals(e)  && simTransitiocn.equals(simbolo)) {
                    //agrega al resultado el estado al conjunto de resultados
                    estadoAlcanzados.add(t.getEstadoFinal());
                }
            }
        }
        return  estadoAlcanzados;
    }
    /**
     * funcion moveDeestados
     * @param estado  estado a analizar
     * @param trancisionesDelAutomata sirve para evaluar la condicion de existencia de una
     *                                transiconn.
     * @param simbolo el simbolo que debe tener la transicion.
     * @return conmunto con todos los estaod que se alcanzaron partir del resultado
     */
    public HashSet<Estado> moveDeEstado(Estado estado, HashSet<Trancision> trancisionesDelAutomata, String simbolo) {
        HashSet<Estado> estadoAlcanzados = new HashSet<>();
        estadoAlcanzados.add(estado);
        for (Trancision t : trancisionesDelAutomata) {
            if(t.getEstadoInicial()== estado && t.getSimbolos() == simbolo) {
                estadoAlcanzados.add(t.getEstadoFinal());
            }
        }
        return  estadoAlcanzados;
    }

    public AutomataAFD convertToAFD(Automata automata){
        //crear nuevo atomataAFD
        AutomataAFD automataAFD = new AutomataAFD();
        //informacion necesarioa del afn
        HashSet<Estado> estados = automata.getEstados();
        HashSet<Trancision> trancisions = automata.getTransicoines();
        //estado de q0 del AFN
        Estado q0 = automata.getEstadoInicale();
        //convierta estado a un conjuntocon un elemenoto
        HashSet<Estado> q0conjunto = new HashSet<>();
        q0conjunto.add(q0);
        //almacenar toda la informacion de las trancisiones del futuro AFD
        HashSet<TrancisionesAFD> dTransicnoes = new HashSet<>();
        //crea un estado de los estados que no se han revisado
        //stack de conjuntos de estados
        Stack<HashSet<Estado>> estadoSinMarcar = new Stack<>();
        //agrega el primer conjunto de estado a estados sin marcar
        HashSet<Estado> closureInical = eClousureT(q0conjunto, trancisions);
        estadoSinMarcar.add(closureInical);
        //este es el conjunto que conitiene los estados "Marcados"
        HashSet<HashSet<Estado>>  estadosMarcados = new HashSet<>();
        //mientras todavia exista conjuntos de estados en el stack de si "sin marcar"
        //un contador
        int cont = 0;
        while (!estadoSinMarcar.isEmpty()){
            //saca un elemento del stack y lo agrega a resultado (marcados)
            HashSet<Estado> t = estadoSinMarcar.pop();
            estadosMarcados.add(t);
            //para cada letra del alafabeto
            for (String s : alfabeto){
                //obtiene un conjunto de aplicar la cerradura de los estados que se alcanza
                HashSet<Estado> u = eClousureT(moveDeConjuntos(t,trancisions,s),trancisions);
                // si el conjunto obtenido no esta en el marcado (resultado), lo pone en el stack de "sin marcar"
                if(!estadosMarcados.contains(u)){
                    estadoSinMarcar.add(u);
                }
                //finalmente crea una transion donde se guarda la informacion.
                EstadoAFD estI = new EstadoAFD(cont, t);
                EstadoAFD estF = new EstadoAFD(cont+1, u);
                dTransicnoes.add( new TrancisionesAFD(estI,estF,s));
                cont +=2;
            }
        }
        //agregando estados a automataAFD
        int contador = 0 ;
        for(HashSet<Estado> e: estadosMarcados){
            EstadoAFD estadoAFD = new EstadoAFD(contador, e);
            automataAFD.addEstado(automataAFD, estadoAFD);
            contador ++;
        }
        //por cada tranciones revisa cual es el conjunto de estados de la
        //tranciones y lo compara con el conkunto de estados de cada EstadoDFA en el automata
        for(TrancisionesAFD t: dTransicnoes) {
            HashSet<EstadoAFD> estadosDeAFD = automataAFD.getEstados();
          //  EstadoAFD nuevoInical = null;
          //  EstadoAFD nuevoFinal = null;
            //verifica y crea la referencia que se utiliza para crear la trancision
            for (EstadoAFD e: estadosDeAFD) {
                if (e.getEstadosQueContiene().equals(t.getConjuntoOrigen().getEstadosQueContiene())) {
                    t.setConjuntoOrigen(e);
                }
                if (e.getEstadosQueContiene().equals(t.getConjuntoDestino().getEstadosQueContiene())) {
                    t.setConjuntoDestino(e);
                }

            }
            //guarda la transicion en el automata
            automataAFD.addTrancion(automataAFD, t);
        }

        return automataAFD;
    }

    public void crearTextoAFD(AutomataAFD automata){
        String texto = "";
        HashSet<EstadoAFD> estadosAFD = automata.getEstados();
        texto += "ESTADOS = {";
        int contador = 0;
        for (EstadoAFD e: estadosAFD) {
            texto += String.valueOf((e.getIdentifiacador()));
            if (contador < estadosAFD.size()-1){
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
        texto +=  "INICIO = {"+ String.valueOf(automata.getEstadoInicale().getIdentifiacador()) + " }\n";

        ArrayList<EstadoAFD> estDeacptacion = automata.getEstadoFinal();
        texto +=  "ACEPTACION = {";
        contador = 0;
        for (EstadoAFD e :estDeacptacion ){
            texto +=  String.valueOf(e.getIdentifiacador());
            if (contador < estDeacptacion.size()-1){
                texto  += ", ";
            }
            else {
                texto += "}\n";
            }
            contador ++;
        }

        // agregando transciones
        HashSet<TrancisionesAFD> transionesAFD = automata.getTransicoines();
        texto += "TRANCIONES = {";
        int contador2 = 0;
        for (TrancisionesAFD t: transionesAFD) {
            texto += t.toString();
            if (contador2 < transionesAFD.size()-1){
                texto  += "-";
            }
            else {
                texto += "}";
            }
            contador2 ++;
        }
        texto += "\nEn total son: " + contador2 + " transiciones\n";

        try {
            Writer output;
            output = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "\\AFD.txt"));  //clears file every time
            output.append(texto);
            output.close();
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }

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

    /**
     * clase que simula (evalua la cadena dada) para un dfa
     * @return true si se acepta la cadena o false de lo contrario
     */
    public boolean simuladorDFA(AutomataAFD automataAFD, String cadena){
        HashSet<TrancisionesAFD> trancisionesAFD = automataAFD.getTransicoines();
        EstadoAFD s = automataAFD.getEstadoInicale();
        ArrayList<EstadoAFD> estadosFinales = automataAFD.getEstadoFinal();
        int cont = 0;
        while (cont < cadena.length()){
            String c = cadena.substring(cont, cont+1);
            s = moveDeEstadoAFD(s, trancisionesAFD, c);
            cont ++;
        }
        if (estadosFinales.contains(s)){
            return true;
        }
        else{return false;}
    }

    /**
     * funcion moveDeestadosAFD adapataccion de la funcion utilizada anteriormente.
     * @param estado  estado a analizar
     * @param trancisionesDelAutomata sirve para evaluar la condicion de existencia de una
     *                                transiconn.
     * @param simbolo el simbolo que debe tener la transicion.
     * @return conmunto con todos los estaod que se alcanzaron partir del resultado
     */
    public EstadoAFD moveDeEstadoAFD(EstadoAFD estado, HashSet<TrancisionesAFD> trancisionesDelAutomata, String simbolo) {
        EstadoAFD estadoAlcanzado;
        for (TrancisionesAFD t : trancisionesDelAutomata) {
            if(t.getConjuntoOrigen().equals(estado) && t.getSimbolo().equals(simbolo)) {
                estadoAlcanzado = t.getConjuntoDestino();
                return  estadoAlcanzado;
            }
        }
        return null; // no debertia de pasar
    }
    public boolean simuladorNFA(Automata automata, String cadena){
        HashSet<Trancision> trancisiones = automata.getTransicoines();
        Estado estadoinicial = automata.getEstadoInicale();
        HashSet<Estado> conjuntoInical = new HashSet<>();
        conjuntoInical.add(estadoinicial);
        HashSet<Estado> s = eClousureT(conjuntoInical, trancisiones);
        Estado estadosFinal = automata.getEstadoFinal();
        int cont = 0;
        while (cont < cadena.length()){
            String c = cadena.substring(cont, cont+1);
            s = eClousureT(moveDeConjuntos(s, trancisiones, c), trancisiones);
            cont ++;
        }
        if (s.contains(estadosFinal)){
            return true;
        }
        else{return false;}
    }
}
