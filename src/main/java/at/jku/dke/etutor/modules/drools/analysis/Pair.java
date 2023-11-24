package at.jku.dke.etutor.modules.drools.analysis;

/**
 * A helper class to return two objects of any type.
 *
 * @param left  The left item.
 * @param right The right item.
 * @param <L>   The type of the left item.
 * @param <R>   The type of the right item.
 */
public record Pair<L, R>(L left, R right) {
}
