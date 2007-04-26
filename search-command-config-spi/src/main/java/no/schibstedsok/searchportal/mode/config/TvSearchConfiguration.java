// Copyright (2007) Schibsted Søk AS
/*
 * TvSearchConfiguration.java
 *
 * Created on 19 May 2006, 14:45
 *
 * 
 */

package no.schibstedsok.searchportal.mode.config;

import java.util.ArrayList;
import java.util.List;
import no.schibstedsok.searchportal.mode.config.AbstractSearchConfiguration.Controller;

/**
 * Configuration class for the TvSearchCommand
 *
 * @author andersjj
 * @version $Id$
 */
@Controller("TvSearchCommand")
public class TvSearchConfiguration extends FastSearchConfiguration {
    
    /** Filter for use when an empty query is sumbitted or no spesific sorting is used. **/
    private List<String> defaultChannels = new ArrayList<String>();
    
    /** Number of results to fetch for empty searches **/
    private int resultsToFetch;
    
    /**
     * 
     */
    public TvSearchConfiguration() {
        super(null);
    }
    
    /** Creates a new instance of TvSearchConfiguration 
     * @param asc 
     */
    public TvSearchConfiguration(final SearchConfiguration asc) {
        super(asc);
    }
    
    /**
     * 
     * @return 
     */
    public List<String> getDefaultChannels() {
        return defaultChannels;
    }
    
    /**
     * 
     * @param defaultChannels 
     */
    public void setDefaultChannels(List<String> defaultChannels) {
        this.defaultChannels = defaultChannels;
    }
    
    /**
     * 
     * @param defaultChannel 
     */
    public void addDefaultChannel(String defaultChannel) {
        defaultChannels.add(defaultChannel);
    }
    
    /**
     * 
     * @return 
     */
    public int getResultsToFetch() {
        return resultsToFetch;
    }
    
    /**
     * 
     * @param resultsToFetch 
     */
    public void setResultsToFetch(int resultsToFetch) {
        this.resultsToFetch = resultsToFetch;
    }
}