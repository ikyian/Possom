/* Copyright (2012) Schibsted ASA
 *   This file is part of Possom.
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
 * RunningQueryTransformer.java
 *
 * Created on 13/06/2007, 12:15:21
 *
 */

package no.sesat.search.run.handler;

import java.io.Serializable;
import no.sesat.search.datamodel.DataModelContext;
import no.sesat.search.site.config.PropertiesContext;
import no.sesat.search.site.config.BytecodeContext;
import no.sesat.search.site.config.ResourceContext;
import no.sesat.search.site.SiteContext;

/**
 *
 *
 *
 * @version $Id: RunHandler.java 5821 2007-10-11 11:25:11Z ssmiweve $
 */
public interface RunHandler extends Serializable {

    interface Context extends DataModelContext, PropertiesContext, SiteContext, ResourceContext {
    }

    void handleRunningQuery(final Context context);
}
