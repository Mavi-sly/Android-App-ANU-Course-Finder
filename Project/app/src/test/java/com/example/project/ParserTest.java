package com.example.project;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.project.parser.CodeExp;
import com.example.project.parser.Exp;
import com.example.project.parser.NameExp;
import com.example.project.parser.Parser;
import com.example.project.parser.SubExp;
import com.example.project.parser.Token;
import com.example.project.parser.Tokenizer;
/**
 * @author u7619947 Xinlong Wu u7726399 Meitong Liu
 */
public class ParserTest {

    private static Tokenizer tokenizer;

    private static final String SIMPLE_CASE = "code=COMP6442|name=qiqilalababa5673/.,|subject=PopulationHealth";
    @Test
    public void testSimple() {
        Tokenizer tokenizer = new Tokenizer(SIMPLE_CASE);
        Exp[] t1 = new Parser(tokenizer).parseExp();
        Exp answer= new CodeExp("COMP","6442");
        Exp subj=new SubExp("PopulationHealth");
        Exp name=new NameExp("qiqilalababa5673/.,");
        assertEquals(t1[0].show(),answer.show());
        assertEquals(t1[2].show(),name.show());
        assertEquals(t1[1].show(),subj.show());
    }

    @Test
    public void testCourseCode(){
        Tokenizer tokenizer = new Tokenizer("COMP6442");
        Exp[] res = new Parser(tokenizer).parseExp();
        Exp ans = new CodeExp("COMP","6442");
        assertEquals(res[0].show(),ans.show());

        tokenizer = new Tokenizer("COMP");
        res = new Parser(tokenizer).parseExp();
        ans = new CodeExp("COMP",null);
        assertEquals(res[0].show(),ans.show());

        tokenizer = new Tokenizer("6442");
        res = new Parser(tokenizer).parseExp();
        ans = new CodeExp("6442",null);
        assertEquals(res[0].show(),ans.show());
    }

    @Test(expected = Tokenizer.IllegalTokenException.class)
    public void testInvalidCourseCode(){
        Tokenizer tokenizer = new Tokenizer("code=COMP644");
        Exp[] res = new Parser(tokenizer).parseExp();
    }

    @Test(expected = Tokenizer.IllegalTokenException.class)
    public void testNotExistCourseSubjectCode(){
        Tokenizer tokenizer = new Tokenizer("COMM");
        Exp[] res = new Parser(tokenizer).parseExp();
    }

    @Test
    public void testCourseSubject(){
        Tokenizer tokenizer = new Tokenizer("subJect=PopulationHealth");
        Exp[] res = new Parser(tokenizer).parseExp();
        Exp ans = new SubExp("PopulationHealth");
        assertEquals(res[1].show(),ans.show());

        tokenizer = new Tokenizer("PopulationHealth");
        res = new Parser(tokenizer).parseExp();
        ans = new SubExp("PopulationHealth");
        assertEquals(res[1].show(),ans.show());
    }

    @Test(expected = Parser.IllegalProductionException.class)
    public void testInvalidCourseSubject(){
        Tokenizer tokenizer = new Tokenizer("subject=Population");
        Exp[] res = new Parser(tokenizer).parseExp();
        Exp ans = new SubExp("PopulationHealth");
        assertEquals(res[1].show(),ans.show());
    }

    @Test(expected = Tokenizer.IllegalTokenException.class)
    public void testInvalidKeyword(){
        Tokenizer tokenizer = new Tokenizer("subje=Population");
        Exp[] res = new Parser(tokenizer).parseExp();
    }
}
