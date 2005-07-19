
package no.schibstedsok.front.searchportal.filters.overture;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import no.schibstedsok.front.searchportal.filters.AsynchronusBaseFilter;
import no.schibstedsok.front.searchportal.filters.SearchConsumer;
/**
 * 
 *  This is a prototype Filter, no thread safety etc has been taken 
 *  into consideration! 
 * 
 * @author Lars Johansson
 *
 */
public final class OvertureFilter extends AsynchronusBaseFilter {
    
	protected FilterConfig filterConfig = null;
	
	int offset = 0;
	String query = "";
	String language = "no";		//default
	long maxTime = 0L;
	int maxResults = 10;

    public void doExecuteAsynch(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String query = "Schibsted ASA";
        if (request.getParameter("q") != null)
            query = URLEncoder.encode(request.getParameter("q"), "UTF-8");

		if(request.getParameter("docs") != null)
			try {
				maxResults = Integer.parseInt(request.getParameter("docs"));				
			} catch (Exception e) {
				filterConfig.getServletContext().log("Wrong format for number of documents to return. Using default 10");
			}

        filterConfig.getServletContext().log("- Overture - Searching");
        // start this search in separate thread
        doSearch(query, response);

        filterConfig.getServletContext().log("- Overture - Moving on in chain");
        // go to next filter in chain
        chain.doFilter(request, response);

    }

    /**
     * 
     * 
     * @param query
     * @param response
     *
     */
    private void doSearch(String query, ServletResponse response) {
        
        final SearchConsumer w = new OvertureSearchConsumer(query, maxTime, response, maxResults, offset);
        Thread searchThread = new Thread(w);
        searchThread.start();
        
    }
}
