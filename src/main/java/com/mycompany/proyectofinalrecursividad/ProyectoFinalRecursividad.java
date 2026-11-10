/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectofinalrecursividad;

/**
 *
 * @author nenin
 */
public class ProyectoFinalRecursividad {

    public static int evaluar(String expresion) {
        if (expresion == null || expresion.isEmpty()) {
            throw new IllegalArgumentException("La expresion esta vacia");
        }
        Evaluador e = new Evaluador(expresion);
        int resultado = e.expresion();
        if (e.pos < expresion.length()) {
            throw new IllegalArgumentException("Caracter inesperado '"
                    + expresion.charAt(e.pos) + "' en la posicion " + e.pos);
        }
        return resultado;
    }

    private static class Evaluador {

        private final String s;
        private int pos = 0;

        Evaluador(String s) {
            this.s = s;
        }

        private boolean es(char c) {
            return pos < s.length() && s.charAt(pos) == c;
        }

        // expresion := termino (('+' | '-') termino)*
        int expresion() {
            return masTerminos(termino());
        }

        private int masTerminos(int acum) {
            if (es('+')) {
                pos++;
                return masTerminos(Math.addExact(acum, termino()));
            }
            if (es('-')) {
                pos++;
                return masTerminos(Math.subtractExact(acum, termino()));
            }
            return acum;
        }

        // termino := factor ('*' factor)*
        private int termino() {
            return masFactores(factor());
        }

        private int masFactores(int acum) {
            if (es('*')) {
                pos++;
                return masFactores(Math.multiplyExact(acum, factor()));
            }
            return acum;
        }

        // factor := numero | '(' expresion ')'
        private int factor() {
            if (pos >= s.length()) {
                throw new IllegalArgumentException("Expresion incompleta");
            }
            char c = s.charAt(pos);
            if (c == '(') {
                pos++;
                int valor = expresion();
                if (!es(')')) {
                    throw new IllegalArgumentException("Falta cerrar parentesis en la posicion " + pos);
                }
                pos++;
                return valor;
            }
            if (Character.isDigit(c)) {
                return numero(0);
            }
            throw new IllegalArgumentException("Caracter inesperado '" + c + "' en la posicion " + pos);
        }

        private int numero(int acum) {
            if (pos < s.length() && Character.isDigit(s.charAt(pos))) {
                int digito = s.charAt(pos) - '0';
                pos++;
                return numero(Math.addExact(Math.multiplyExact(acum, 10), digito));
            }
            return acum;
        }
    }

    private static void probar(String expresion) {
        try {
            System.out.println("\"" + expresion + "\" = " + evaluar(expresion));
        } catch (RuntimeException ex) {
            System.out.println("\"" + expresion + "\" -> Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        probar("7+10+2");
        probar("1+5*(7-1)+10");
        probar("5+7+(1+2*(8-9)+2)-10");
        probar("9-2*(75-9+(6+3+(1+2)*1)+7)+9");
        probar("5++1+10");
        probar("-5+10");
        probar("74-7/7");
        probar("10+9-A");
        probar("(1+2");
        probar("1+2)");
        probar("");
    }
}
