package at.jku.dke.etutor.modules.nf.parser;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public class NFParserErrorCollector implements ANTLRErrorListener {

    private final List<String> syntaxErrors;

    public NFParserErrorCollector() {
        this.syntaxErrors = new LinkedList<>();
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object o, int i, int i1, String s, RecognitionException e) {
        syntaxErrors.add("Line " + i + ", position " + i1 + ": " + s);
    }

    @Override
    public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean b, BitSet bitSet, ATNConfigSet atnConfigSet) {

    }

    @Override
    public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitSet, ATNConfigSet atnConfigSet) {

    }

    @Override
    public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atnConfigSet) {

    }

    public List<String> getSyntaxErrors() {
        return syntaxErrors;
    }

    public String getStringOfAllErrors() {
        StringJoiner ret = new StringJoiner(";");

        for(String s : syntaxErrors) {
            ret.add(s);
        }

        return ret.toString();
    }
}
