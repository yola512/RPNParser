/*
Dla zadanego zestawu operacji: dodawanie, odejmowanie, mnożenie,
dzielenie oraz nawiasy – korzystając z Odwrotnej Notacji Polskiej napisz
funkcję parser. Funkcja taka na wejściu a przyjmować String zawierający
tylko cyfry, operatory i nawiasy. Na wyjściu ma zwracać liczbę
reprezentującą rozwiązanie.
 */

import java.util.*;

public class Main 
{
    // method that converts infix expression to ONP (by shunting-yard algorithm)
    public static String infixToRPN(String expression) 
    {
        Stack<Character> operators = new Stack<>(); // stack of operators
        StringBuilder result = new StringBuilder(); // result that will be written in ONP
        // prority of operators: values 1 and 2 show their importance in operations
        // (operators with prority 2 are done before the ones with 1)
        Map<Character, Integer> prioryty = Map.of('+', 1, '-', 1, '*', 2, '/', 2); 
        
        // removing all whitespaces and turning every symbol into char
        for (char symbol : expression.replaceAll("\s", "").toCharArray()) 
        { 
            if (Character.isDigit(symbol)) 
            { // if a symbol is a numer its added to the result (with a space after)
                result.append(symbol).append(' '); 
            } 
            else if (prioryty.containsKey(symbol)) 
            { // if its a operator we check the operators stack
                while (!operators.isEmpty() && prioryty.getOrDefault(operators.peek(), 0) 
                >= prioryty.get(symbol)) // if an operator already in the operators stack has the same or 
                                        // higher priority its added from the stack to the result
                                        // (until theres no more operators in the stack with higher priority)
                {
                    result.append(operators.pop()).append(' '); 
                }
                operators.push(symbol); // adding new operator to the stack
            } 
            else if (symbol == '(') 
            {
                operators.push(symbol); // '(' symbol added to the stack
            } 
            else if (symbol == ')') 
            { // if its a ')' symbol operators are getting removed from the stack 
             // (until a '(' symbol is found)
                while (!operators.isEmpty() && operators.peek() != '(') 
                {
                    result.append(operators.pop()).append(' '); // adding operators to the result
                }
                operators.pop(); // '(' removed from the stack (since all of its content 
                                // has been added to the result)
            }
        }
        
        while (!operators.isEmpty()) 
        { // all remaining operators are added to the result
            result.append(operators.pop()).append(' ');
        }

        return result.toString().trim(); // result with unnecessary space at the end removed
    }

     /* !! TODO: a method that calculates the result of an expression in ONP
     (cases "+", "-", "*", "/")

    public static int parser(String expression)
    {

    }
    */

    public static void main(String[] args) 
    {
        try (Scanner scanner = new Scanner(System.in)) 
        {
            System.out.println("Podaj wyrażenie do obliczenia: ");
            String input = scanner.nextLine();
            String onp = infixToRPN(input); 
            System.out.println("Wyrażenie w ONP: " + onp); 
           // System.out.println("Wynik: " + parser(rpn));
        }
    }

}