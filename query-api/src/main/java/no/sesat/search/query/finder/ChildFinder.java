/* Copyright (2007-2012) Schibsted ASA
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
package no.sesat.search.query.finder;

import no.sesat.commons.visitor.AbstractReflectionVisitor;
import no.sesat.search.query.Clause;
import no.sesat.search.query.BinaryClause;
import no.sesat.search.query.LeafClause;
import no.sesat.search.query.UnaryClause;
import no.sesat.search.query.parser.*;


/** Used to find if a particular clause exists underneath another.
 *
 * @version $Id$
 */
public final class ChildFinder extends AbstractReflectionVisitor {

    private boolean found;
    private Clause child = null;

    /** Does the child clause exist any depth underneath the parent.
     *
     * @param parent the parent clause
     * @param child the child clause.
     * @return Does the child clause exist any depth underneath the parent.
     */
    public synchronized boolean childExists(final UnaryClause parent, final Clause child) {

        found = false;
        this.child = child;
        visit(parent);
        return found;
    }

    protected void visitImpl(final BinaryClause clause) {
        if (!found) { // still looking
            clause.getFirstClause().accept(this);
            clause.getSecondClause().accept(this);
        }
    }

    protected void visitImpl(final UnaryClause clause) {
        if (!found ) { // still looking
            clause.getFirstClause().accept(this);
        }
    }

    protected void visitImpl(final LeafClause clause) {

        found = clause == child;
    }

}