import java.util.*;

public class Main {
    // method that converts infix expression to ONP (by shunting-yard algorithm)
    public static String infixToRPN(String expression)
    {
        Stack<Character> operators = new Stack<>(); // stack of operators
        StringBuilder result = new StringBuilder(); // result that will be written in ONP
        // priority of operators: values 1 and 2 show their importance in operations
        // (operators with priority 2 are done before the ones with 1)
        Map<Character, Integer> priority = Map.of('+', 1, '-', 1, '*', 2, '/', 2);

        StringBuilder currentNumber = new StringBuilder(); // collects digits for multi-digit numbers


        // removing all whitespaces and turning every symbol into char
        for (char symbol : expression.replaceAll("\s", "").toCharArray())
        {
            if (Character.isDigit(symbol)) {
                // collect digits of a multi-digit number
                currentNumber.append(symbol);
            }
            else {
                if (!currentNumber.isEmpty())
                {
                    result.append(currentNumber.toString()).append(' '); // append collected number
                    currentNumber.setLength(0); // clean the current number buffer
                }

                if (priority.containsKey(symbol)) {
                    // if its an operator we check the operators stack
                    while (!operators.isEmpty() && priority.getOrDefault(operators.peek(), 0)
                            >= priority.get(symbol))
                    // if an operator is already in the operators stack has the same or
                    // higher priority its added from the stack to the result
                    // (until theres no more operators in the stack with higher priority)
                    {
                        result.append(operators.pop()).append(' ');
                    }
                    operators.push(symbol); // adding new operator to the stack
                }
                if (symbol == '(')
                {
                    operators.push(symbol); // '(' symbol added to the stack
                }
                if (symbol == ')') {
                    // if its a ')' symbol operators are getting removed from the stack
                    // (until a '(' symbol is found)
                    while (!operators.isEmpty() && operators.peek() != '(')
                    {
                        result.append(operators.pop()).append(' '); // adding operators to the result
                    }
                    operators.pop(); // '(' removed from the stack (since all of its content
                    // has been added to the result)
                }
            }
        }

        if (!currentNumber.isEmpty())
        {
            result.append(currentNumber.toString()).append(' '); // append any remaining number
        }

        while (!operators.isEmpty())
        { // all remaining operators are added to the result
            result.append(operators.pop()).append(' ');
        }

        return result.toString().trim(); // result with unnecessary space at the end removed
    }

     // parser(String expression) - a method that calculates the result of an expression in RPN

    public static int parser(String expression)
    {
        // stack to hold operands for calculations
        Stack<Integer> stack = new Stack<>();
        // split into operators and operands
        String[] array = expression.split("\\s+");

        for (int i = 0; i <= array.length - 1; i++) {
            // if digit -> add to the stack
            if (is_digit(array[i])) {
                stack.push(Integer.parseInt(array[i])); // convert string to int and add to the stack
            }
            // if operator, process the operands
            else if (is_operator(array[i])) {
                // at least 2 operands are needed to calculate the result
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("ERROR: Not enough operands for operation");
                }

                // pop two operands from the stack
                int a = stack.pop();
                int b = stack.pop();

                // calculate the result for two operands
                int result = calc(b, a, array[i]);
                stack.push(result);
            }
            // if operator in neither a number nor an operator -> throw error
            else {
                throw new IllegalArgumentException("ERROR: Invalid character: " + array[i]);
            }
        }
        // after going through the entire expression there should be one value left in the stack (the result)
        if (stack.size() == 1) {
            return stack.pop();
        }
        else {
            throw new IllegalArgumentException("ERROR: Too many operands, invalid RPN expression.");
        }
    }


    // ADDITIONAL METHODS

    // method to check if a string is a valid digit (int)
    public static boolean is_digit(String s) {
        return s.matches("-?\\d+"); // handles positive/negative int
    }

    // method to check if a string is a valid operator (+, -, *, /)
    public static boolean is_operator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/");
    }

    // method to calculate two operands using given operator (+, -, *, /)
    public static int calc(int b, int a, String operator) {
        return switch (operator) {
            case "+" -> b + a;
            case "-" -> b - a;
            case "*" -> b * a;
            case "/" ->
            {
                // handle division + (error) division by zero
                if (a == 0) {
                    throw new ArithmeticException("ERROR: Division by zero exception");
                }
                yield b / a;
            }
            default -> throw new IllegalArgumentException("ERROR: Unknown operator: " + operator);
        };
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.println("\nEnter expression to calculate (or 'X' to exit): ");
            String input = scanner.nextLine();

            // check if user wants to exit the program
            if (input.equalsIgnoreCase("X")) {
                System.out.println("Exiting program...");
                break;
            }
            try {
                String rpn = infixToRPN(input);
                System.out.println("Expression in RPN: " + rpn);
                System.out.println("Result: " + parser(rpn));
            }
            catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
        scanner.close();
    }


}