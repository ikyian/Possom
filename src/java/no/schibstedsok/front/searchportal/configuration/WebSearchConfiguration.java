/*
 * WebSearchConfiguration.java
 *
 * Created on March 7, 2006, 1:28 PM
 *
 */

package no.schibstedsok.front.searchportal.configuration;

/**
 *
 * @author magnuse
 */
public class WebSearchConfiguration extends FastSearchConfiguration {

    public WebSearchConfiguration(){
        super(null);
    }
    
    public WebSearchConfiguration(final SearchConfiguration asc){
        super(asc);
    }
}
