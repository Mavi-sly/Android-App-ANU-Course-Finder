package com.example.project;

import static org.junit.Assert.assertEquals;

import com.example.project.parser.Token;
import com.example.project.parser.Tokenizer;

import org.junit.Test;
/**
 * @author u7619947 Xinlong Wu u7726399 Meitong Liu
 */
public class TokenizerTest {
    private static Tokenizer tokenizer;
    private static final String MID = "12 * 5 - 3";
    private static final String ADVANCED = "illgal1234";
    private static final String Code = "code=COMP6442|name=sdfs2333|  subject=NationalSecurityPolicy";

    @Test
    public void testCodeToken() {
        tokenizer = new Tokenizer(Code);

        // check the type of the first token
        assertEquals("wrong token type", Token.Type.CODE, tokenizer.current().getType());

        // check the actual token value"
        assertEquals("wrong token value", "code=", tokenizer.current().getToken());

        // extract next token (just to skip first passCase token)
        tokenizer.next();

        // check the type of the first token
        assertEquals("wrong token type", Token.Type.CODESUBNUM, tokenizer.current().getType());

        // check the actual token value
        assertEquals("wrong token value", "COMP6442", tokenizer.current().getToken());
    }

    @Test(timeout=1000)
    public void testSepToken() {
        tokenizer = new Tokenizer(Code);

        // extract next token (just to skip first passCase token)
        tokenizer.next();
        tokenizer.next();

        // check the type of the first token
        assertEquals("wrong token type", Token.Type.SEP, tokenizer.current().getType());

        // check the actual token value
        assertEquals("wrong token value", "|", tokenizer.current().getToken());
    }
    @Test
    public void testNameToken() {   //"code=COMP6442|name=sdfs2333|  subject=NationalSecurityPolicy"
        tokenizer = new Tokenizer(Code);

        // extract next token (just to skip first passCase token)
        tokenizer.next();
        tokenizer.next();
        tokenizer.next();

        // check the type of the first token
        assertEquals("wrong token type", Token.Type.NAME, tokenizer.current().getType());

        // check the actual token value
        assertEquals("wrong token value", "name=", tokenizer.current().getToken());
        tokenizer.next();

        // check the type of the first token
        assertEquals("wrong token type", Token.Type.NAMEVALUE, tokenizer.current().getType());

        // check the actual token value
        assertEquals("wrong token value", "sdfs2333", tokenizer.current().getToken());
    }



    @Test
    public void testSubjectToken() {
        tokenizer = new Tokenizer(Code);

        // extract next token (just to skip first passCase token)
        tokenizer.next();
        tokenizer.next();
        tokenizer.next();
        tokenizer.next();
        tokenizer.next();
        tokenizer.next();

    
        // check the type of the first token
        assertEquals("wrong token type", Token.Type.SUBJECT, tokenizer.current().getType());

        // check the actual token value
        assertEquals("wrong token value", "subject=", tokenizer.current().getToken());
        tokenizer.next();

        // check the type of the first token
        assertEquals("wrong token type", Token.Type.SUBVALUE, tokenizer.current().getType());

        // check the actual token value
        assertEquals("wrong token value", "NationalSecurityPolicy", tokenizer.current().getToken());
    }

    @Test
    public void testUnnormalizedCourseCode(){
        tokenizer = new Tokenizer("COMP6442");
        assertEquals(Token.Type.CODE, tokenizer.current().getType());
        tokenizer.next();
        assertEquals(Token.Type.CODESUBNUM, tokenizer.current().getType());
        assertEquals("COMP6442", tokenizer.current().getToken());

        tokenizer = new Tokenizer("COMP");
        assertEquals(Token.Type.CODE, tokenizer.current().getType());
        tokenizer.next();
        assertEquals(Token.Type.CODESUB, tokenizer.current().getType());
        assertEquals("COMP", tokenizer.current().getToken());

        tokenizer = new Tokenizer("6442");
        assertEquals(Token.Type.CODE, tokenizer.current().getType());
        tokenizer.next();
        assertEquals(Token.Type.CODENUM, tokenizer.current().getType());
        assertEquals("6442", tokenizer.current().getToken());

        tokenizer = new Tokenizer("code=COMP");
        assertEquals(Token.Type.CODE, tokenizer.current().getType());
        tokenizer.next();
        assertEquals(Token.Type.CODESUB, tokenizer.current().getType());
        assertEquals("COMP", tokenizer.current().getToken());
    }

    @Test(expected = Tokenizer.IllegalTokenException.class)
    public void testInvalidCourseCode(){
        tokenizer = new Tokenizer("code=COMM");
        tokenizer.next();
    }

    @Test
    public void testSubject(){
        tokenizer = new Tokenizer("NationalSecurityPolicy");
        assertEquals(Token.Type.SUBJECT, tokenizer.current().getType());
        tokenizer.next();
        assertEquals(Token.Type.SUBVALUE, tokenizer.current().getType());
        assertEquals("NationalSecurityPolicy", tokenizer.current().getToken());

        tokenizer = new Tokenizer("National Security Policy");
        assertEquals(Token.Type.SUBJECT, tokenizer.current().getType());
        tokenizer.next();
        assertEquals(Token.Type.SUBVALUE, tokenizer.current().getType());
        assertEquals("National Security Policy", tokenizer.current().getToken());

        tokenizer = new Tokenizer("national security policy");
        assertEquals(Token.Type.SUBJECT, tokenizer.current().getType());
        tokenizer.next();
        assertEquals(Token.Type.SUBVALUE, tokenizer.current().getType());
        assertEquals("national security policy", tokenizer.current().getToken());

        tokenizer = new Tokenizer("subject=national security policy");
        assertEquals(Token.Type.SUBJECT, tokenizer.current().getType());
        tokenizer.next();
        assertEquals(Token.Type.SUBVALUE, tokenizer.current().getType());
        assertEquals("national security policy", tokenizer.current().getToken());
    }

    @Test(expected = Tokenizer.IllegalTokenException.class)
    public void testInvalidSubject(){
        tokenizer = new Tokenizer("Subjext=NationalSecurity");
        assertEquals(Token.Type.SUBJECT, tokenizer.current().getType());
        tokenizer.next();
        assertEquals(Token.Type.SUBVALUE, tokenizer.current().getType());
        assertEquals("NationalSecurityPolicy", tokenizer.current().getToken());
    }
}
