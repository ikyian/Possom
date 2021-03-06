/* Copyright (2006-2012) Schibsted ASA
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
 * TokenEvaluationEngineTestContext.java
 *
 * Created on 8. april 2006, 01:17
 *
 */

package no.sesat.search.query.token;


import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import no.sesat.search.site.SiteTestCase;
import no.sesat.search.site.config.*;
import no.sesat.search.site.Site;
import no.sesat.search.site.SiteContext;

/**
 *
 * @version $Id$
 */
public final class TokenEvaluationEngineTestContext extends SiteTestCase implements TokenEvaluationEngineImpl.Context{

    private final String query;

    /**
     * Creates a new instance of TokenEvaluationEngineTestContext
     * @param query the query string to test against.
     */
    public TokenEvaluationEngineTestContext(final String query) {
        this.query = query;
    }

    public String getQueryString() {
        return query;
    }

    /** Application's properties, ie configuration.properties. *
     * @return properties holding all application (including inherited) properties.
     */
    public Properties getApplicationProperties() {
        return FileResourcesSiteConfigurationTest.valueOf(getTestingSite()).getProperties();
    }

    public PropertiesLoader newPropertiesLoader(
            final SiteContext siteCxt,
            final String resource,
            final Properties properties) {

        return FileResourceLoader.newPropertiesLoader(siteCxt, resource, properties);
    }

    public DocumentLoader newDocumentLoader(
            final SiteContext siteCxt,
            final String resource,
            final DocumentBuilder builder) {

        return FileResourceLoader.newDocumentLoader(siteCxt, resource, builder);
    }

    public BytecodeLoader newBytecodeLoader(SiteContext context, String className, String jar) {
        return FileResourceLoader.newBytecodeLoader(context, className, jar);
    }

    public Site getSite()  {
        return getTestingSite();
    }

    public String getUniqueId() {
                        return "";
                    }
}
