package com.example.project.parser;
/**
 * @author u7726399 Meitong Liu
 */
import java.util.Scanner;

//format：name=xxx|subject=xxx|unit=xxx  seperated by '|'
//
//<exp>		::= <equation> || <equation>|<exp>
//<equation> ::= <key><value>
//<key>  	 	::= {“code=” , "name=" , "subject="}
//<value> 	::= <codevalue>||<String>
//<codevalue>	::=<codesub> ||<codenum>||<codesub><codename>
//<codesub>	::=<string of 4 letters>
//<codenum>	::=<string of 4 numbers>
//        things can be searched: code（codesub+codenum），name，subject
public class Parser {
    /**
     * The following exception should be thrown if the parse is faced with series of tokens that do not
     * correlate with any possible production rule.
     */
    public static class IllegalProductionException extends IllegalArgumentException {
        public IllegalProductionException(String errorMessage) {
            super(errorMessage);
        }
    }

    // The tokenizer (class field) this parser will use.
    Tokenizer tokenizer;

    /**
     * Parser class constructor
     * Simply sets the tokenizer field.
     */
    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public static void main(String[] args) {
        // Create a scanner to get the user's input.
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String input = scanner.nextLine();

            // Check if 'quit' is provided.
            if (input.equals("q"))
                break;

            // Create an instance of the tokenizer.
            Tokenizer tokenizer = new Tokenizer(input);

            // Print out the expression from the parser.
            Parser parser = new Parser(tokenizer);
            Exp[] expression = parser.parseExp();
        }
    }

    /**
     * Edit the search condition array
     *
     * @return type: Exp[]
     */
    private Exp[] condition(Exp[] origin, Exp newcon) {

        if (newcon instanceof CodeExp) {
            origin[0] = newcon;
        } else if ((newcon instanceof SubExp)) {
            origin[1] = newcon;
        } else if (newcon instanceof NameExp) {
            origin[2] = newcon;
        }
        return origin;
    }

    public Exp[] parseExp() {
        int consize = new AndExp(null, null).getFilter_size();
        Exp[] allcon = new Exp[consize];
        Exp equal = this.parseKey();
        allcon = condition(allcon, equal);

        if (tokenizer.hasNext()) {
            if (tokenizer.current().getType().equals(Token.Type.SEP)) {
                tokenizer.next();
                if (tokenizer.hasNext()) {
                    Exp[] lattercon = parseExp();
                    allcon = condition(lattercon, equal);
                }
            } else throw new IllegalProductionException("wrong input format after '|'");
        }
        return allcon;
    }


    public Exp parseKey() {
        Exp valuereturn = null;

        if (tokenizer.hasNext()) {
            if (tokenizer.current().getType().equals(Token.Type.CODE)) {
                tokenizer.next();
                if (tokenizer.current().getType().equals(Token.Type.CODESUBNUM) ||
                        tokenizer.current().getType().equals(Token.Type.CODESUB) ||
                        tokenizer.current().getType().equals(Token.Type.CODENUM)) {
                    valuereturn = this.parseValue(1);
                } else throw new Parser.IllegalProductionException("wrong input after 'code='");
            } else if (tokenizer.current().getType().equals(Token.Type.SUBJECT)) {
                tokenizer.next();
                if (tokenizer.current().getType().equals(Token.Type.SUBVALUE)) {
                    valuereturn = this.parseValue(2);
                } else throw new Parser.IllegalProductionException("wrong input after 'subject'");
            } else if (tokenizer.current().getType().equals(Token.Type.NAME)) {
                tokenizer.next();
                if (tokenizer.current().getType().equals(Token.Type.NAMEVALUE)) {
                    valuereturn = this.parseValue(3);
                } else throw new Parser.IllegalProductionException("wrong input after 'name'");
            }
        }
        return valuereturn;

    }

    /**
     * Adheres to the grammar rule:
     * <code>   ::= <CodeExp>   //String codesub, String codenum
     * <subject>::=<SubExp>     //String
     * <name>   ::=<NameExp>    //just a string
     *
     * @return type: Exp.
     */

    public Exp parseValue(int option) {
//1:code 2:subject 3:name
        switch (option) {
            case 1:
                String codesub=null;
                String codenum=null;
                if(tokenizer.current().getType().equals(Token.Type.CODESUBNUM)){
                    codesub = tokenizer.current().getToken().substring(0, 4);
                    codenum = tokenizer.current().getToken().substring(4);
                } else if (tokenizer.current().getType().equals(Token.Type.CODESUB)) {
                    codesub = tokenizer.current().getToken().substring(0, 4);
                } else if (tokenizer.current().getType().equals(Token.Type.CODENUM)) {
                    codenum = tokenizer.current().getToken().substring(0, 4);
                }
                Exp coderesult = new CodeExp(codesub, codenum);
                tokenizer.next();
                return coderesult;

            case 2:
                String subject = tokenizer.current().getToken();
                Exp subresult = new SubExp(subject);
                tokenizer.next();
                return subresult;

            case 3:
                String namevalue = tokenizer.current().getToken();
                Exp nameresult = new NameExp(namevalue);
                tokenizer.next();
                return nameresult;
        }
        throw new IllegalArgumentException("invalid input to parseValue()");

    }
}

