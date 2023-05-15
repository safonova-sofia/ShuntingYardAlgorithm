import java.util.Stack;
import java.util.StringTokenizer;


public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ShuntingYardAlgorithm infixToPostfix = new ShuntingYardAlgorithm();
        Calculation calculation = new Calculation();
        infixToPostfix.parse("5*8*(2+9)+(7-5+8-9*(5*5)+5)");
        calculation.calculations(infixToPostfix.stackRPN);
    }
}


class ShuntingYardAlgorithm extends Stack<String> {
    Stack<String> stackOperations = new Stack<String>();
    Stack<String> stackRPN = new Stack<String>();
    Stack<String> stackNPN = new Stack<String>();

    String OPERATORS = "+-*/";

    boolean isNumber(String token) {
        try {
            int tmp = Integer.parseInt(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    boolean isOpenBracket(String token) {
        return token.equals("(");
    }

    boolean isCloseBracket(String token) {
        return token.equals(")");
    }

    boolean isOperator(String token) {
        return OPERATORS.contains(token);
    }

    byte getPrecedence(String token) {
        if (token.equals("+") || token.equals("-")) {
            return 1;
        }
        return 2;
    }

    public void parse(String expression) {
        // cleaning stacks
        stackOperations.clear();
        stackRPN.clear();

        // make some preparations
        expression = expression.replace(" ", "").replace("(-", "(0-")
                .replace(",-", ",0-");
        if (expression.charAt(0) == '-') {
            expression = "0" + expression;
        }
        // splitting input string into tokens
        StringTokenizer stringTokenizer = new StringTokenizer(expression, OPERATORS + "()", true);

        // loop for handling each token - shunting-yard algorithm
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            if (isOpenBracket(token)) {
                stackOperations.push(token);
            } else if (isCloseBracket(token)) {
                while (!stackOperations.empty() && !isOpenBracket(stackOperations.lastElement())) {
                    stackRPN.push(stackOperations.pop());
                }
                stackOperations.pop();
            } else if (isNumber(token)) {
                stackRPN.push(token);
            } else if (isOperator(token)) {
                while (!stackOperations.empty()
                        && isOperator(stackOperations.lastElement())
                        && getPrecedence(token) <= getPrecedence(stackOperations
                        .lastElement())) {
                    stackRPN.push(stackOperations.pop());
                }
                stackOperations.push(token);
            }
        }
        while (!stackOperations.empty()) {
            stackRPN.push(stackOperations.pop());
        }


        System.out.println(stackRPN); //RPN
        System.out.println(stackRPN.peek());
    }
}

class Calculation extends ShuntingYardAlgorithm {
    Stack<Integer> stackAnswer = new Stack<Integer>();
    void calculations(Stack<String> stackNPN){
        for (String item : stackNPN) {
            System.out.println(item);
            if (isNumber(item)) {
                stackAnswer.push(Integer.parseInt(item));
            } else {
                int tmp1 = stackAnswer.pop();
                int tmp2 = stackAnswer.pop();
                switch (item) {
                    case "+" -> stackAnswer.push(tmp2 + tmp1);
                    case "-" -> stackAnswer.push(tmp2 - tmp1);
                    case "*" -> stackAnswer.push(tmp2 * tmp1);
                    case ":" -> stackAnswer.push(tmp2 / tmp1);
                }
            }
        }
        System.out.println(stackAnswer);
        int answer = stackAnswer.peek();
    }
}