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
*
*/
package no.sesat.search.mode.command;

import java.util.concurrent.Callable;
import no.sesat.commons.ioc.BaseContext;
import no.sesat.search.datamodel.DataModelContext;
import no.sesat.search.mode.config.SearchConfiguration;
import no.sesat.search.mode.config.SearchConfigurationContext;
import no.sesat.search.site.config.ResourceContext;
import no.sesat.search.query.token.TokenEvaluationEngineContext;
import no.sesat.search.result.ResultItem;
import no.sesat.search.result.ResultList;

/** Behavour requirements for any implementating Search Command.
 *
 *
 * @version <tt>$Id$</tt>
 */
public interface SearchCommand extends Callable<ResultList<ResultItem>> {


    /** Being a factory for all the commands - it propagates all the contextual needs of the underlying commands it
     * creates.
     */
    public interface Context extends
            BaseContext,
            ResourceContext,
            DataModelContext,
            SearchConfigurationContext,
            TokenEvaluationEngineContext {
    }

    /**
     * Returns the configuration associated with this search command.
     * Typically delegates to the context provided.
     *
     * @return The search configuration.
     */
    SearchConfiguration getSearchConfiguration();

    // --- EXECUTION ---

    /** From Callable interface.
     *
     * @{@inheritDoc}
     *
     * @return the completed search result list.
     */
    ResultList<ResultItem> call();

    /** Allows the SearchCommand to clean itself after a long night out when s/he didn't get home in time.
     * @return if cleaning was actually performed
     **/
    boolean handleCancellation();

    /** Has this command be cancelled.
     *
     * @return true if cancelled.
     */
    boolean isCancelled();


    /** Can this command provide paginated results.
     *
     * @return true if results can be paginated.
     */
    boolean isPaginated();

    /** Can this command be sorted differently at user's requst.
     *
     * @return true if user sorting is applicable.
     */
    boolean isUserSortable();
}
