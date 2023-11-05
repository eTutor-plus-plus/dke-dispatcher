// Generated from C:/ETutor/dke-dispatcher/src/main/java/at/jku/dke/etutor/modules/rt/analysis/rtSyntax.g4 by ANTLR 4.13.1
package at.jku.dke.etutor.modules.rt.analysis;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link rtSyntaxVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
@SuppressWarnings("CheckReturnValue")
public class rtSyntaxBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements rtSyntaxVisitor<T> {
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitStart(rtSyntaxParser.StartContext ctx) { return visitChildren(ctx); }
}