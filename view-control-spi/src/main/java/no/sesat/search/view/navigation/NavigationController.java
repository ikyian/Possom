/* Copyright (2005-2007) Schibsted Søk AS
 * This file is part of SESAT.
 * You can use, redistribute, and/or modify it, under the terms of the SESAT License.
 * You should have received a copy of the SESAT License along with this program.  
 * If not, see https://dev.sesat.no/confluence/display/SESAT/SESAT+License
*
* Jul 20, 2007 11:20:16 AM
*/
package no.sesat.search.view.navigation;

import no.sesat.search.result.NavigationItem;
import no.sesat.search.datamodel.DataModel;
import no.sesat.search.datamodel.DataModelContext;
import no.sesat.search.site.config.ResourceContext;
import no.sesat.search.site.SiteContext;

/**
 * TODO: Move into sesat-search-command-control-spi once that module is ready for action.
 */
public interface NavigationController {

    interface Context extends DataModelContext, ResourceContext, SiteContext {
    }

    NavigationItem getNavigationItems(Context context);
}