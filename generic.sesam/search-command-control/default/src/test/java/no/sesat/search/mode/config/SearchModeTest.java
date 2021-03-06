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

 */
package no.sesat.search.mode.config;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.parsers.DocumentBuilder;
import no.sesat.commons.ioc.ContextWrapper;
import no.sesat.search.mode.SearchMode;
import no.sesat.search.site.config.DocumentLoader;
import no.sesat.search.run.RunningQuery;
import no.sesat.search.run.RunningQueryImpl;
import java.util.Properties;
import no.sesat.search.datamodel.DataModel;
import no.sesat.search.datamodel.DataModelFactory;
import no.sesat.search.datamodel.DataModelTestCase;
import no.sesat.search.datamodel.access.ControlLevel;
import no.sesat.search.site.config.PropertiesLoader;
import no.sesat.search.site.config.FileResourceLoader;
import no.sesat.search.site.config.BytecodeLoader;
import no.sesat.search.site.Site;
import no.sesat.search.site.SiteContext;
import no.sesat.search.view.config.SearchTab;
import no.sesat.search.view.SearchTabFactory;
import org.testng.annotations.Test;

/** SearchMode tests.
 *
 *
 * @version <tt>$Id$</tt>
 */
public final class SearchModeTest extends DataModelTestCase {

    /** Test the FastCommandConfig.
     ** @throws java.lang.Exception
     */
    @Test
    public void testFastCommandConfig() throws Exception {

        final SearchMode mode = new SearchMode();

        mode.setExecutor(SearchMode.SearchCommandExecutorConfig.PARALLEL);

        final FastCommandConfig webCrawl = new FastCommandConfig();

        webCrawl.setId("test-fast-search-configuration");
        webCrawl.setQueryServerUrl("queryServerURL.1");
        webCrawl.addCollections(new String[]{"webcrawlno1", "webcrawlno1deep1"});
        webCrawl.addCollections(new String[]{"webcrawlno2"});
//        webCrawl.addResultHandler(new TextOutputResultHandler());
        webCrawl.addResultFields(new String[]{"url"});
        webCrawl.addResultFields(new String[]{"title", "body"});
        webCrawl.setSpellchecklanguage("no");
        webCrawl.setResultsToReturn(10);

        final Collection<SearchConfiguration> searchConfigurations = new ArrayList<SearchConfiguration>();
        searchConfigurations.add(webCrawl);
        mode.setSearchConfigurations(searchConfigurations);

        final DataModel datamodel = getDataModel();

        final RunningQuery.Context rqCxt = new RunningQuery.Context() {
            public SearchMode getSearchMode() {
                return mode;
            }
            public SearchTab getSearchTab(){
                return SearchTabFactory.instanceOf(
                    ContextWrapper.wrap(SearchTabFactory.Context.class,
                    this,
                    new SiteContext(){
                        public Site getSite(){
                            return datamodel.getSite().getSite();
                        }
                    }))
                    .getTabByKey("d");
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
            public DataModel getDataModel(){
                return datamodel;
            }
        };

        // DataModel's ControlLevel will be REQUEST_CONSTRUCTION
        //  Increment it onwards to RUNNING_QUERY_CONSTRUCTION.
        DataModelFactory
                .instanceOf(ContextWrapper.wrap(
                DataModelFactory.Context.class,
                rqCxt,
                new SiteContext(){
                    public Site getSite(){
                        return datamodel.getSite().getSite();
                    }
                }))
                .assignControlLevel(datamodel, ControlLevel.RUNNING_QUERY_CONSTRUCTION);

        final RunningQuery query = new RunningQueryImpl(rqCxt, "aetat.no");

        query.run();

    }

    /** Test the OverturePPCCommandConfig.
     ** @throws java.lang.Exception
     */
    @Test
    public void testOverturePPCConfiguration() throws Exception {

        final String query = "linux";

        final SearchMode mode = new SearchMode();
        mode.setExecutor(SearchMode.SearchCommandExecutorConfig.PARALLEL);
        final OverturePpcCommandConfig searchConfiguration = new OverturePpcCommandConfig();
        searchConfiguration.setId("test-overture-ppc-command");
        searchConfiguration.setResultsToReturn(3);
        searchConfiguration.setHost("overtureHost");
        searchConfiguration.setPort("overturePort");
        searchConfiguration.setPartnerId("overturePartnerId");
        searchConfiguration.setUrl("/d/search/p/standard/eu/xml/rlb/?mkt=se&amp;adultFilter=clean&amp;accountFilters=schibstedsok_se");
        searchConfiguration.setEncoding("UTF-8");

        final Collection<SearchConfiguration> searchConfigurations = new ArrayList<SearchConfiguration>();
        searchConfigurations.add(searchConfiguration);
        mode.setSearchConfigurations(searchConfigurations);

        final DataModel datamodel = getDataModel();

        final RunningQuery.Context rqCxt = new RunningQuery.Context(){
            public SearchMode getSearchMode() {
                return mode;
            }
            public SearchTab getSearchTab(){
                return SearchTabFactory.instanceOf(
                    ContextWrapper.wrap(SearchTabFactory.Context.class,
                    this,
                    new SiteContext(){
                        public Site getSite(){
                            return datamodel.getSite().getSite();
                        }
                    }))
                    .getTabByKey("d");
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

            public DataModel getDataModel(){
                return datamodel;
            }
        };

        // DataModel's ControlLevel will be REQUEST_CONSTRUCTION
        //  Increment it onwards to RUNNING_QUERY_CONSTRUCTION.
        DataModelFactory
                .instanceOf(ContextWrapper.wrap(
                DataModelFactory.Context.class,
                rqCxt,
                new SiteContext(){
                    public Site getSite(){
                        return datamodel.getSite().getSite();
                    }
                }))
                .assignControlLevel(datamodel, ControlLevel.RUNNING_QUERY_CONSTRUCTION);

        final RunningQuery runningQuery = new RunningQueryImpl(rqCxt, query);

        runningQuery.run();

    }
}

