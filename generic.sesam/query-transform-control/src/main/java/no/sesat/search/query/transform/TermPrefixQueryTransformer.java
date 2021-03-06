/*
 * Copyright (2005-2012) Schibsted ASA
 * This file is part of Possom.
 *
 *   Possom is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Possom is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Possom.  If not, see <http://www.gnu.org/licenses/>.
 */
package no.sesat.search.query.transform;

import java.util.Map;
import no.sesat.search.query.AndClause;
import no.sesat.search.query.Clause;
import no.sesat.search.query.DefaultOperatorClause;
import no.sesat.search.query.EmailClause;
import no.sesat.search.query.IntegerClause;
import no.sesat.search.query.LeafClause;
import no.sesat.search.query.UnaryClause;
import no.sesat.search.query.OrClause;
import no.sesat.search.query.PhoneNumberClause;
import no.sesat.search.query.UrlClause;
import org.apache.log4j.Logger;

/**
 * @see TermPrefixQueryTransformerConfig
 * @version $Id$
 */
public final class TermPrefixQueryTransformer extends AbstractQueryTransformer {

    private static final Logger LOG = Logger.getLogger(TermPrefixQueryTransformer.class);

    private final TermPrefixQueryTransformerConfig config;

    /** Default constructor for QueryTransformers.
     *
     * @param config matching configuration class
     */
    public TermPrefixQueryTransformer(final QueryTransformerConfig config){
        this.config = (TermPrefixQueryTransformerConfig)config;
    }

    /**
     * This is th default fallback. Adds the prefix in the <code>prefix</code>
     * property
     *
     * @param clause The clause to prefix.
     */
     public void visitImpl(final LeafClause clause) {

        if (clause.getField() == null || getContext().getFieldFilter(clause) == null) {
            addPrefix(clause, config.getPrefix());
        }
    }

    /**
     * Add prefix to an integer clause.
     *
     * @param clause The clause to prefix.
     */
    public void visitImpl(final IntegerClause clause) {
        addPrefix(clause, config.getNumberPrefix());
    }

    /**
     * Add prefixes to an or clause. The two operand clauses are prefixed
     * individually.
     *
     * @param clause The clause to prefix.
     */
    public void visitImpl(final OrClause clause) {
        clause.getFirstClause().accept(this);
        clause.getSecondClause().accept(this);
    }

    /**
     * Add prefixes to an default operator clause. The two operand clauses are prefixed
     * individually.
     *
     * @param clause The clause to prefix.
     */
    public void visitImpl(final DefaultOperatorClause clause) {
        clause.getFirstClause().accept(this);
        clause.getSecondClause().accept(this);
    }

    /**
     * Add prefixes to an and operator clause. The two operand clauses are prefixed
     * individually.
     *
     * @param clause The clause to prefix.
     */
    public void visitImpl(final AndClause clause) {
        clause.getFirstClause().accept(this);
        clause.getSecondClause().accept(this);
    }

    /**
     * Add prefixes to an generic operator clause. The child operand clauses is prefixed
     * individually.
     *
     * @param clause The clause to prefix.
     */
    public void visitImpl(final UnaryClause clause) {
        clause.getFirstClause().accept(this);
    }

    /**
     * Prefix a phone number clause with the number prefix.
     *
     * @param clause  The clause to prefix.
     */
    public void visitImpl(final PhoneNumberClause clause) {
        addPrefix(clause, config.getPhoneNumberPrefix());
    }

    /**
     * Prefix a url clause with the url prefix.
     *
     * @param clause  The clause to prefix.
     */
    public void visitImpl(final UrlClause clause) {
        addPrefix(clause, config.getUrlPrefix());
    }

    /**
     * Prefix a email clause with the email prefix.
     *
     * @param clause  The clause to prefix.
     */
    public void visitImpl(final EmailClause clause) {
        addPrefix(clause, config.getEmailPrefix());
    }

    private void addPrefix(final Clause clause, final String prefix) {

        final String term = getTransformedTerms().get(clause);

        if (term != null && !(term.equals("") || isAlreadyPrefixed(term, prefix))) {
            final String[] prefixArr = prefix.split(",");
            final StringBuilder builder = new StringBuilder();
            if(1 < prefixArr.length){ builder.append('('); }
            for(String p : prefixArr){
                builder.append(p + ':' + term);
                if(!p.equals(prefixArr[prefixArr.length-1])){
                    builder.append(' ' + config.getMultiTermJoin() + ' ');
                }
            }
            if(1 < prefixArr.length){ builder.append(')'); }
            getTransformedTerms().put(clause, builder.toString());
        }
    }

    private static boolean isAlreadyPrefixed(final String term, final String prefix) {
        return term.indexOf(prefix + ':') > -1;
    }

    private Map<Clause,String> getTransformedTerms() {
        return getContext().getTransformedTerms();
    }

}
