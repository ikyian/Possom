// Copyright (2006) Schibsted Søk AS
/*
 * TokenEvaluationEngineTestContext.java
 *
 * Created on 8. april 2006, 01:17
 *
 */

package no.schibstedsok.searchportal.query.token;


import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import no.schibstedsok.searchportal.site.SiteTestCase;
import no.schibstedsok.searchportal.site.config.*;
import no.schibstedsok.searchportal.site.Site;
import no.schibstedsok.searchportal.site.SiteContext;

/**
 *
 * @author mick
 */
public final class TokenEvaluationEngineTestContext extends SiteTestCase implements TokenEvaluationEngineImpl.Context{

    private final String query;

    /**
     * Creates a new instance of TokenEvaluationEngineTestContext
     */
    public TokenEvaluationEngineTestContext(final String query) {
        this.query = query;
    }

    /** TODO comment me. **/
    public String getQueryString() {
        return query;
    }

    /** TODO comment me. **/
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
    public BytecodeLoader newBytecodeLoader(SiteContext context, String className) {
        return FileResourceLoader.newBytecodeLoader(context, className);
    }


    /** TODO comment me. **/
    public Site getSite()  {
        return getTestingSite();
    }

}
