/*
 * Copyright (2005-2006) Schibsted Søk AS
 */
package no.schibstedsok.front.searchportal.query.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import no.schibstedsok.front.searchportal.analyzer.TokenEvaluatorFactory;
import no.schibstedsok.front.searchportal.analyzer.TokenPredicate;

/**
 * The AndClause represents a joining clause between two terms in the query.
 * For example: "term1 AND term2".
 *<b>Objects of this class are immutable</b>
 *
 * @author <a href="mailto:mick@wever.org">Michael Semb Wever</a>
 * @version $Id$
 */
public final class AndClause extends AbstractOperationClause {

    /** Values are WeakReference object to AbstractClause.
     * Unsynchronized are there are no 'changing values', just existance or not of the AbstractClause in the system.
     */
    private static final Map/*<Long,WeakReference<AndClause>>*/ WEAK_CACHE = new HashMap/*<Long,WeakReference<AndClause>>*/();

    /* A WordClause specific collection of TokenPredicates that *could* apply to this Clause type. */
    private static final Collection/*<Predicate>*/ PREDICATES_APPLICABLE;

    static {
        final Collection/*<Predicate>*/ predicates = new ArrayList();
        predicates.add(TokenPredicate.ALWAYSTRUE);
        // Predicates from RegExpEvaluators
        predicates.add(TokenPredicate.CATALOGUEPREFIX);
        predicates.add(TokenPredicate.MATHPREDICATE);
        // Add all FastTokenPredicates
        predicates.addAll(TokenPredicate.getFastTokenPredicates());
        PREDICATES_APPLICABLE = Collections.unmodifiableCollection(predicates);
    }


    private final Clause firstClause;
    private final Clause secondClause;

    /**
     * Creator method for AndClause objects. By avoiding the constructors,
     * and assuming all AndClause objects are immutable, we can keep track
     * (via a weak reference map) of instances already in use in this JVM and reuse
     * them.
     * The methods also allow a chunk of creation logic for the AndClause to be moved
     * out of the QueryParserImpl.jj file to here.
     * @param first the left child clause of the operation clause we are about to create (or find).
     * The current implementation always creates a right-leaning query heirarchy.
     * Therefore the left child clause to any operation clause must be a LeafClause.
     * @param second the right child clause of the operation clause we are about to create (or find).
     * @param predicate2evaluatorFactory the factory handing out evaluators against TokenPredicates.
     * Also holds state information about the current term/clause we are finding predicates against.
     * @return returns a AndClause instance matching the term, left and right child clauses.
     * May be either newly created or reused.
     */
    public static AndClause createAndClause(
        final LeafClause first,
        final Clause second,
        final TokenEvaluatorFactory predicate2evaluatorFactory) {

        // construct the proper "schibstedsøk" formatted term for this operation.
        //  XXX eventually it would be nice not to have to expose the internal string representation of this object.
        final String term = (first.getField() != null ? first.getField() + ":" : "")
                + first.getTerm()
                + " AND "
                + (second instanceof LeafClause && ((LeafClause) second).getField() != null
                    ?  ((LeafClause) second).getField() + ":"
                    : "")
                + second.getTerm();

        // update the factory with what the current term is
        predicate2evaluatorFactory.setCurrentTerm(term);

        // use helper method from AbstractLeafClause
        return (AndClause) createClause(
                AndClause.class,
                term,
                first,
                second,
                predicate2evaluatorFactory,
                PREDICATES_APPLICABLE, WEAK_CACHE);
    }

    /**
     * Create clause with the given term, field, known and possible predicates.
     * @param term the term (query string) for this clause.
     * @param first the leaf child clause
     * @param second the right child clause
     * @param knownPredicates the set of known predicates for this clause.
     * @param possiblePredicates the set of possible predicates for this clause.
     */
    protected AndClause(
            final String term,
            final Clause first,  // really is a LeafClause
            final Clause second,
            final Set/*<Predicate>*/ knownPredicates,
            final Set/*<Predicate>*/ possiblePredicates) {

        super(term, knownPredicates, possiblePredicates);
        this.firstClause = first;
        this.secondClause = second;
    }

    /**
     * Get the firstClause.
     *
     * @return the firstClause.
     */
    public Clause getFirstClause() {
        return firstClause;
    }

    /**
     * Get the secondClause.
     *
     * @return the secondClause.
     */
    public Clause getSecondClause() {
        return secondClause;
    }

}
