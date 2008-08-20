/*
 * Copyright (2005-2008) Schibsted Søk AS
 * This file is part of SESAT.
 *
 *   SESAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   SESAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with SESAT.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package no.sesat.search.query.token;

/** Something went wrong evaluating the token.
 *
 * Because of this
 * TokenPredicates (because of their associated known and possible predicates) cannot be weakly cached
 * and so should be marked stale.
 *
 *
 * @version <tt>$Id$</tt>
 */
public final class EvaluationException extends Exception {

    /**
     * Create a new VeryFastListQueryException.
     *
     * @param s detailed message
     * @param e underlying exception
     */
    public EvaluationException(final String s, final Exception e) {
        super(s, e);
    }
}