package at.jku.dke.etutor.modules.rt.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import javax.xml.transform.ErrorListener;
import java.io.Serializable;

public class RTAnalysis extends DefaultAnalysis {
    String input;

    public RTAnalysis(String input) {
        super();
        this.input = input;
    }

    public boolean checkSyntax(){
        boolean checkSuccess = true;

        try {
            errorListener errorListener = new errorListener();
            ANTLRInputStream input = new ANTLRInputStream(this.input);
            rtSyntaxLexer lexer = new rtSyntaxLexer(input);
            lexer.addErrorListener(errorListener);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            rtSyntaxParser parser = new rtSyntaxParser(tokens);
            parser.addErrorListener(errorListener);
            ParseTree tree = parser.start();
        }
        catch(Exception e) {
            checkSuccess = false;
            System.out.println(e.toString() + " Yes");
        }
        return checkSuccess;
    }

    public boolean checkSemantik(){
        //Muss auf Sicht aus der DB, abgeholt werden.
        String muster = "test(|id|)";
        boolean checkSuccess = true;
        if(this.input != muster){
            return false;
        }
        return checkSuccess;
    }
}
