/* Copyright (2005-2006) Schibsted Søk AS
 *
 * AbstractQuery.java
 *
 * Created on 12 January 2006, 09:50
 *
 */

package no.schibstedsok.searchportal.query.parser;

import java.util.Set;
import no.schibstedsok.searchportal.query.LeafClause;
import no.schibstedsok.searchportal.query.Query;
import no.schibstedsok.searchportal.query.finder.Counter;
import no.schibstedsok.searchportal.query.finder.FirstLeafFinder;
import no.schibstedsok.searchportal.query.finder.PredicateCollector;
import no.schibstedsok.searchportal.query.token.TokenEvaluationEngine;
import no.schibstedsok.searchportal.query.token.TokenPredicate;


/** Abstract helper for implementing a Query class.
 * Handles input of the query string and finding the first leaf clause (term) in the clause hierarchy.
 * Is thread safe. No methods return null.
 *
 * @version $Id$
 * @author <a href="mailto:mick@wever.org">Michael Semb Wever</a>
 */
public abstract class AbstractQuery implements Query {

    private final FirstLeafFinder finder = new FirstLeafFinder();
    private final Counter counter = new Counter();
    private final PredicateCollector predicateCollector;
    
    private final TokenEvaluationEngine.State evaluationState;

    private final String queryStr;

    /** Creates a new instance of AbstractQuery .
     * @param queryStr the query string as inputted from the user.
     */
    protected AbstractQuery(final String queryStr) {
        
        this.queryStr = queryStr;
        predicateCollector  = new PredicateCollector(this);
        evaluationState = new TokenEvaluationEngine.State(){
            public String getTerm() {
                return null;
            }

            public Query getQuery() {
                return AbstractQuery.this;
            }

            public Set<TokenPredicate> getKnownPredicates() {
                return predicateCollector.getKnownPredicates();
            }

            public Set<TokenPredicate> getPossiblePredicates() {
                return predicateCollector.getPossiblePredicates();
            }
            
        };
    }

    /**
     * {@inheritDoc}
     */
    public String getQueryString() {
        return queryStr;
    }

    /**
     * {@inheritDoc}
     */
    public LeafClause getFirstLeafClause() {
        return finder.getFirstLeaf(getRootClause());
    }

    /** TODO comment me. **/
    public int getTermCount() {
        return counter.getTermCount(getRootClause());
    }

    /** TODO comment me. **/
    public boolean isBlank(){
        return false;
    }

    public TokenEvaluationEngine.State getEvaluationState(){
        return evaluationState;
    }
    
}