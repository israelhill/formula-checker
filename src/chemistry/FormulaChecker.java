package chemistry;

/**
 * This objects purpose is to take a string and check it to see if it is a syntactically correct chemical formula. It outputs true 
 * if the formula is valid and false in all other circumstances
 */
public class FormulaChecker {

    // The final output and message that goes along with it
    private String failureMessage;
    private boolean correctFormula;

    // Used for the logic of what characters will be valid in the formula
    private boolean parenthesesValid;
    private boolean upperCaseValid;
    private boolean lowerCaseValid;
    private int numLowerCase;
    private int numElements;
    private boolean numberValid;
    private boolean inParentheses;

    // Used for multiplier checking logic to ensure 1 and 0 are not multipliers
    private int numbersInARow;

    /**
     * This is the main method which is used to check a chemical formula.
     * @param formula -- The formula being checked
     * @return -- True if the formula is syntactically correct, false in all other cases
     */
    public boolean checkFormula(String formula) {
        // Before the formula is checked all the logic is set
        setLogic();

        // Handling invalid input forms
        if(formula == null) {
            syntaxFailure("String is null");
        }

        if(formula == "") {
            syntaxFailure("String is empty");
        }

        // Iterates through the string checking it's characters
        char[] characterFormula = formula.toCharArray();
        for(int i=0; i<=characterFormula.length; i++) {
            if(correctFormula == false) {
                return solution();
            }
            processChar(characterFormula[i]);
        }
        // Checks to make sure all parentheses were completed
        checkForUnfinishedParentheses();

        return solution();
    }

    /**
     * Prints out messages and returns the solution boolean. This is entirely for readability.
     * @return -- Returns whatever correctFormula is
     */
    private boolean solution() {
        System.out.println(booleanToOutput(correctFormula));
        System.out.println(failureMessage);
        return correctFormula;
    }

    /**
     * Changes a boolean true or false into a string T or F
     * @param b -- Boolean being changed
     * @return -- T if boolean is true, F if boolean is false
     */
    private String booleanToOutput(boolean b) {
        if(b) {
            return "True";
        } else {
            return "False";
        }
    }

    /**
     * Checks for unfinished parentheses. If there are it calls a syntax failure with an appropriate message.
     */
    private void checkForUnfinishedParentheses() {
        if(inParentheses) {
            syntaxFailure("Uncompleted parentheses");
        }
    }

    /**
     * Processes individual characters of the formula being examined to see if they fit with what is syntactically correct
     * @param c -- The character being examined
     */
    private void processChar(char c) {
        // This logic directs the character processing to the appropriate case
        if(isUpperCase(c)) {
            processUpperCase(c);
        } else if(isNumber(c)) {
            processNumber(c);
        } else if(isLowerCase(c)){
            processLowerCase(c);
        } else if(isNumber(c)) {
            processParentheses(c);
        } else {
            // If the character is none of the expected values the syntax is invalid because of an invalid character
            syntaxFailure("Invalid character");
        }
    }

    /**
     * Checks if char c is upper case
     * @param c -- Char being checked
     * @return -- True if c is an upper case letter and false otherwise
     */
    private boolean isUpperCase(char c) {
        return Character.getType((int)c)==Character.UPPERCASE_LETTER;
    }

    /**
     * Checks if c is lower case
     * @param c -- Char being checked
     * @return -- True if c is a lower case letter and false otherwise
     */
    private boolean isLowerCase(char c) {
        return Character.getType((int)c)==Character.LOWERCASE_LETTER;
    }

    /**
     * Checks if c is a number
     * @param c -- Char being checked
     * @return -- True if c is a number and false otherwise
     */
    private boolean isNumber(char c) {
        return Character.getType((int)c)== Character.DECIMAL_DIGIT_NUMBER;
    }

    /**
     * Checks if c is a parentheses
     * @param c -- Char being checked
     * @return -- True if c is a parentheses and false otherwise
     */
    private boolean isParentheses(char c) {
        return c == '(' || c == ')';
    }

    /**
     * If the character being processed is upper case then the next character can be lower case, or a number. The number of lower
     * 	case letters in a row is also reset.
     */
    private void processUpperCase(char c) {
        assert Character.getType((int)c)==Character.TITLECASE_LETTER;
        if(upperCaseValid) {
            // Appropriate logic changes
            lowerCaseValid = true;
            numberValid = true;
            numLowerCase = 0;
            parenthesesValid = true;

            // Keeps track of how many elements are inside a set of parentheses
            if(inParentheses) {
                numElements++;
            }

        } else {
            syntaxFailure("Upper case invalid");
        }
    }

    /**
     * If the character being processed is lower case then the count of lower case letters in a row increments. If it is at 2 there cannot be
     * 	another lower case letter.
     */
    private void processLowerCase(char c) {
        assert Character.getType((int)c)==Character.LOWERCASE_LETTER;
        if(lowerCaseValid) {
            // Increment the number of lower case letters in a row. If it is 2 or greater there cannot be another lower case letter.
            numLowerCase++;
            if(numLowerCase>=1) {
                lowerCaseValid = false;
            }
        } else {
            syntaxFailure("Lower case invalid");
        }

    }

    /**
     * If the character being processed is a number then there cannot be a lower case letter after it
     */
    private void processNumber(char c) {
        assert Character.getType((int)c)==Character.DECIMAL_DIGIT_NUMBER;
        if(parenthesesValid) {
            // Base logic changes for a number
            lowerCaseValid = true;
            upperCaseValid = true;
            parenthesesValid = true;
            // Logic for ensuring a proper first digit in the multiplier. Includes incrementing numbersInARow.
            checkForOverflow(numbersInARow);
            numbersInARow++;
            if(numbersInARow == 1) {
                if(c == '0') {
                    syntaxFailure("0 is not a valid first digit to a multiplier");
                }
            }
        } else {
            syntaxFailure("Number invalid");
        }

    }

    /**
     * Checks an int to make sure that it won't overflow. If it is going to the next time it is incremented then a syntax failure is called.
     * @param i -- The int being checked
     */
    private void checkForOverflow(int i) {
        if(i == Integer.MAX_VALUE) {
            syntaxFailure("Multiplier is too large");
        }
    }

    /**
     * If the character being processed is a parentheses, this splits to two different methods depending on whether there is
     * 	currently an open set of parentheses.
     * @param c -- The character being processed. This is needed to see what kind of parenthesis it is.
     */
    private void processParentheses(char c) {
        assert isParentheses(c);
        if(parenthesesValid) {

            parenthesesValid = false;

            if(inParentheses) {
                // If there are less then 2 elements between a set of parentheses then syntax is invalid
                if(numElements<2) {
                    syntaxFailure("Not enough elements in a set of parentheses");
                }
                checkForClosedParentheses(c);
                inParentheses = false;
            } else {
                // Resets number of elements in a set of parentheses to 0
                numElements = 0;
                checkForOpenParentheses(c);
                inParentheses = true;
            }
        } else {
            syntaxFailure("Parentheses invalid");
        }
    }

    /**
     * Checks to see if a character is a closed parenthesis, fails it if it is not.
     * @param c -- The character being checked
     */
    private void checkForClosedParentheses(char c) {
        if(c == ')') {
            // A number has to follow a closed parentheses
            numberValid = true;
            lowerCaseValid = false;
            upperCaseValid = false;
        } else {
            syntaxFailure("Found open parentheses: Expected closed");
        }
    }

    /**
     * Checks to see if a character is a open parenthesis, fails it if it is not.
     * @param c -- The character being checked
     */
    private void checkForOpenParentheses(char c) {
        if(c == '(') {
            // A new element has to follow an open parentheses, meaning an upper case letter does
            numberValid = false;
            lowerCaseValid = false;
            upperCaseValid = true;
        } else {
            syntaxFailure("Found closed parentheses: Expected open");
        }
    }

    /**
     * Changes the boolean keeping track of whether the formula is correct to false. Then sets the message to whatever is passed in.
     * @param message -- The message to be set that displays upon the conclusion of the syntax checking
     */
    private void syntaxFailure(String message) {
        correctFormula = false;
        failureMessage = message;
    }

    /**
     * Sets all the logic to it's initial state so before a check is performed with this object
     */
    private void setLogic() {
        correctFormula = true;
        failureMessage = "Formula is correct";

        // The logic for which characters can be used initially. Only open parenthesis and upper case letters are initially valid.
        parenthesesValid = true;
        inParentheses = false;
        upperCaseValid = true;
        lowerCaseValid = false;
        numberValid = false;

        // These counters are also reset to 0
        numElements = 0;
        numLowerCase = 0;
        numbersInARow = 0;
    }
}