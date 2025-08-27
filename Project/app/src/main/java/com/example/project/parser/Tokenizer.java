package com.example.project.parser;
/**
 * @author u7726399 Meitong Liu
 */
import static com.example.project.parser.Codesub.getCodeSub;
//import static com.example.project.parser.Subject.Chemistry;
import static com.example.project.parser.Subject.getSubject;

import java.util.Scanner;

public class Tokenizer {
    private String buffer;          // String to be transformed into tokens each time next() is called.
    private Token currentToken;     // The current token. The next token is extracted when next() is called.

    private Token lastToken;
    public static class IllegalTokenException extends IllegalArgumentException {
        public IllegalTokenException(String errorMessage) {
            super(errorMessage);
        }
    }


    public static void main(String[] args) {
        // Create a scanner to get the user's input.
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String input = scanner.nextLine();

//            // Check if 'quit' is provided.
//            if (input.equals("q"))
//                break;

            // Create an instance of the tokenizer.
            Tokenizer tokenizer = new Tokenizer(input);

            // Print all the tokens.
            while (tokenizer.hasNext()) {
                System.out.print(tokenizer.current() + " ");
                tokenizer.next();
            }
            System.out.println();
        }
    }

    /**
     * Tokenizer class constructor
     * The constructor extracts the first token and save it to currentToken
     */
    public Tokenizer(String text) {
        buffer = normalizeText(text);          // save input text (string)
        lastToken = null;
        next();                 // extracts the first token.
    }
    /**
     *if the string belongs to the enum{codesub}, return true.
     */
    private boolean isCodesub(String getstr) {
        return getCodeSub(getstr) != null;
    }

    private boolean isSubject(String getstr) {
        return getSubject(getstr) != null;
    }

    /**
     * check if string(numbers) is 4 digits.
     * @param numbers
     * @return
     */
    private static boolean isCodenum(String numbers) {
        if(numbers.length()!=4)return false;
        for (int i=0;i<4;i++) {
            if (!Character.isDigit(numbers.charAt(i)))
                return false;
        }
        return true;
    }

    /**
     * This function will find and extract a next token from {@code _buffer} and
     * save the token to {@code currentToken}.
     */
    public void next() {


        buffer = buffer.trim();     // remove whitespace

        if (buffer.isEmpty()) {
            currentToken = null;    // if there's no string left, set currentToken null and return
            return;
        }

        if(lastToken!=null&&(lastToken.getType()== Token.Type.CODE||lastToken.getType()==Token.Type.NAME||lastToken.getType()==Token.Type.SUBJECT)){
            switch ((lastToken.getType())){
                case CODE:
                    int pos=0;
                    while (pos < buffer.length() && buffer.charAt(pos)!='|' ){
                        pos++;
                    }
                    if(pos!=8&&pos!=9&&pos!=4) throw new IllegalTokenException("illegal course code input format!");
                    String codefront=buffer.substring(0,4);
                    if(isCodesub(codefront)){
                        if(pos==4){    //only input subject
                            currentToken=new Token(codefront, Token.Type.CODESUB);
                        } else {
                            String codenum=buffer.substring(4,8);
                            if(isCodenum(codenum)){
                                currentToken=new Token(codefront+codenum, Token.Type.CODESUBNUM);
                                if(pos==9){
                                    if(buffer.charAt(pos)=='A'||buffer.charAt(pos)=='B'){
                                    /*TODO:for courses' codes ends with 'A' or 'B'
                                    throw an exception?
                                    */
                                        //buffer = buffer.substring(1);
                                    }else throw new IllegalTokenException("illegal coureseCode-number format!");
                                }
                            }else throw new IllegalTokenException("illegal coureseCode-number format!");
                        }
                    }else if (pos==4) {
                        for (int i = 0; i < codefront.length(); i++) {
                            if (!Character.isDigit(codefront.charAt(i)))
                                throw new IllegalTokenException("illegal courseCode format!");
                        }
                        currentToken=new Token(codefront, Token.Type.CODENUM);
                    }
                    else throw new IllegalTokenException("illegal courseCode format!");
                    break;
                case SUBJECT:
                    int posi=0;
                    while (posi < buffer.length() && buffer.charAt(posi)!='|' ){
                        posi++;
                    }
                    String getst=buffer.substring(0,posi);
                    if(isSubject(getst)){
                        currentToken=new Token(getSubject(getst).name(), Token.Type.SUBVALUE);
                    }
                    else throw new IllegalTokenException("illegal Subject '"+getst+"'");
                    break;
                case NAME:
                    int posj=0;
                    while (posj < buffer.length() && buffer.charAt(posj)!='|' ){
                        posj++;
                    }
                    String ge=buffer.substring(0,posj);
                    currentToken=new Token(ge, Token.Type.NAMEVALUE);
                    break;
            }   //end of switch
        }   //end of if(lastToken!=null)
        else {
            int len=0;
            char firstChar = buffer.charAt(0);
            if (firstChar == '|')
                currentToken = new Token("|", Token.Type.SEP);
            else{
                int pos=0;
                while (pos < buffer.length() && buffer.charAt(pos)!='|' ){
                    pos++;
                }
                if(pos<5) throw new IllegalTokenException("illegal input format!");
                String getstr=buffer.substring(0,pos);
                if(getstr.substring(0,5).toLowerCase().equals("name=")){
                    len=5;
                    currentToken= new Token(getstr.substring(0,5), Token.Type.NAME);
                }else if(getstr.substring(0,8).toLowerCase().equals("subject=")){
                    len=8;
                    currentToken= new Token(getstr.substring(0,8), Token.Type.SUBJECT);
                }else if(getstr.substring(0,5).toLowerCase().equals("code=")){
                    len=5;
                    currentToken= new Token(getstr.substring(0,5), Token.Type.CODE);
                }else {
                    throw new IllegalTokenException("Invalid Token at: "+getstr);
                }
            }
        }

        /* Remove the extracted token from buffer*/
        lastToken= new Token(currentToken.getToken(),currentToken.getType());
        int tokenLen = currentToken.getToken().length();
        buffer = buffer.substring(tokenLen);

    }

    private static String normalizeText(String text){

        if(isNormalized(text))
            return text;

        String normalized = "";
        String[] queryList = text.split("\\|");
        for (String item:
                queryList) {
            if (item.indexOf("=") != -1){
                normalized += item+"|";
                continue;
            }

            if (item.length() == 4){
                // will be CodeSub or CodeNum
                if (getCodeSub(item)!=null)
                    normalized += "code="+item.toUpperCase()+"|";
                else
                    normalized += "code="+item+"|";
            }
            else if(item.length() >= 4){
                String codeSub = item.substring(0,4);
                if (item.length()==8 && getCodeSub(codeSub)!=null && isCodenum(item.substring(4)))
                    normalized += "code="+item+"|";
                else if(getSubject(item)!=null) {
                    normalized += "subject="+getSubject(item).name()+"|";
                }
                else
                    normalized += "name="+item+"|";
            }
            else
                normalized += "name="+item+"|";
        }
        return normalized;
    }

    private static boolean isNormalized(String text){
        String[] list = text.split("\\|");
        for (String item:
             list) {
            if (!item.contains("="))
                return false;
        }
        return true;
    }


    /**
     * Returns the current token extracted by {@code next()}
     *
     * @return type: Token
     */
    public Token current() {
        return currentToken;
    }

    public Token getLastToken(){
        return lastToken;
    }

    /**
     * Check whether tokenizer still has tokens left
     *
     * @return type: boolean
     */
    public boolean hasNext() {
        return currentToken != null;
    }
}

