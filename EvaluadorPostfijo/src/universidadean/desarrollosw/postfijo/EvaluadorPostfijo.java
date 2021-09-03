/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad Ean (Bogotá - Colombia)
 * Departamento de Tecnologías de la Información y Comunicaciones
 * Licenciado bajo el esquema Academic Free License version 2.1
 * <p>
 * Proyecto Evaluador de Expresiones Postfijas
 * Fecha: Febrero 2021
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package universidadean.desarrollosw.postfijo;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * Esta clase representa una clase que evalúa expresiones en notación polaca o
 * postfija. Por ejemplo: 4 5 +
 */
public class EvaluadorPostfijo {

    /**
     * Realiza la evaluación de la expresión postfijo utilizando una pila
     * @param expresion una lista de elementos con números u operadores
     * @return el resultado de la evaluación de la expresión.
     */
    static int evaluarPostFija(List<String> expresion) throws Exception{
        // Se cambia la pila a elementos de tipo double para manejo de decimales
        Stack<Integer> pila = new Stack<>();

        // TODO: Realiza la evaluación de la expresión en formato postfijo
        // Se recorre la lista (expresion) de items ingresados por consola y separados uno por uno con la funcion Token.dividir
        for (String item: expresion) {
            // Se asigna el String item a un objeto de tipo StringReader sr
            StringReader sr = new StringReader(item);
            // Se asigna el StringReader sr a un objeto de tipo StramTokenizer st
            StreamTokenizer st = new StreamTokenizer(sr);
            st.slashSlashComments(false);
            st.slashStarComments(false);
            st.commentChar('#');
            st.ordinaryChar('/');
            // Se inicializa una variable double para guardar valores futuros de las operaciones
            int newValue = 0;

            // se crea una variable entera tok con el valor de st.nexToken
            int tok = st.nextToken();
            // Se crea una variable de tipo Token para poder validar si el item dado es operador o numero
            Token t = new Token(tok, st.sval, (int) st.nval);
            // Se valida si el valor de la lista item es un numero
            if (t.isNumber()) {
                // Si el valor es un numero, se agrega a la pila como un double
                pila.push(Integer.valueOf(item));
                // Si no es un numero se valida si es un operador
            } else if (t.isOperator()) {
                // Si es un operador primero validamos si la pila tiene elementos
                if (pila.empty()) {
                    // Si no se tiene elementos se retorna una excepcion
                    throw new Exception("No se han ingresado suficientes elementos para operar");
                } else {
                    // Si la pila tiene elemetos, sacamos ese valor de la pila para operarlo
                    int value1 = pila.pop();
                    // se valida si existe otro elemento en la pila para operar
                    if (pila.empty()) {
                        // si no se tiene se retorna una excepcion
                        throw new Exception("No se han ingresado suficientes elementos para operar");
                    } else {
                        // si existe otro elemeto, se saca de la pila para operar con el elemento anterior
                        int value2 = pila.pop();
                        // Se valida cual es el operador y dependiendo de este se realiza la respectiva operacion con los valores extraidos de la pila
                        switch (item) {
                            case "+":
                                newValue = suma(value2, value1);
                                break;
                            case "-":
                                newValue = resta(value2, value1);
                                break;
                            case "*":
                                newValue = multiplicacion(value2, value1);
                                break;
                            case "/":
                                newValue = division(value2, value1);
                                break;
                            case "^":
                                newValue = potencia(value2, value1);
                                break;
                            case "%":
                                newValue = modulo(value2, value1);
                            default:
                                throw new Exception("No se puede utilizar el operador " + item);
                        }
                        // Se agrega el resultado de la operacion a la pila
                        pila.push(newValue);
                    }
                }
            }
            // Se continua con el proceso hasta que recorramos la lista completa (expresion)
        }
        // Se valida que al final del proceso quede un solo elemento en la pila
        if(pila.size()>1){
            // Se retorna una excepcion ya que nos da a conocer que la expresion postfija no esta correctamente exrita
            throw new Exception("Se han introducido demasiados elementos para operar");
        }
        // Se saca el ultimo elemento de la pila que deberia ser el resultado de toda la expresion introducida
        return pila.pop();
    }

    static int suma(int a, int b){
        return a + b;
    }

    static int resta(int a, int b){
        return a - b;
    }

    static int multiplicacion(int a, int b){
        return a * b;
    }

    static int modulo(int a, int b){ return a % b;}

    static int division(int a, int b) throws Exception {
        if(b==0){
            throw new Exception("Division por 0 no permitida");
        }else {
            return a / b;
        }
    }

    static  int potencia(int a, int b){
        return Integer.parseInt(String.valueOf(Math.pow(a,b)));
    }

    /**
     * Programa principal
     */
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        System.out.print("> ");
        String linea = teclado.nextLine();

        try {
            List<String> expresion = Token.dividir(linea);
            System.out.println(evaluarPostFija(expresion));
        }
        catch (Exception e) {
            System.err.printf("Error grave en la expresión: %s", e.getMessage());
        }

    }
}
