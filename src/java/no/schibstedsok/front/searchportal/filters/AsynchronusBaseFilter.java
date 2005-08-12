/**
 * 
 */
package no.schibstedsok.front.searchportal.filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import no.schibstedsok.front.searchportal.util.SearchConstants;

/**
 * A AsynchronusBaseFilter.
 * 
 * Executes work in a separate thread by delegating the execute() 
 * method to doExecuteAsynch() that AsynchronousFilters must 
 * implement. 
 * 
 * @author <a href="lars.johansson@conduct.no">Lars Johansson</a>
 * @version $Revision$
 */
public abstract class AsynchronusBaseFilter extends BaseFilter {

    /**
     *
     */
    public AsynchronusBaseFilter() {
        super();
    }

    /* (non-Javadoc)
     * @see com.schibstedsok.portal.search.filters.BaseFilter#doExecute(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doExecute(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //forward to the implementation of the filter
		List processList = (List)request.getAttribute(SearchConstants.PIPELINE);

		String name = getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1);

		//should this filter be executed?
		if(processList.contains(getClass().getName())) {
	        doExecuteAsynch(request, response, chain);
		} else {
			filterConfig.getServletContext().log("Skipped " + name);
	        chain.doFilter(request, response);
		}


    }

    public abstract void doExecuteAsynch(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException;


    protected void startThread(SearchConsumer w, ServletRequest req) {
        ThreadGroup group = (ThreadGroup) req.getAttribute("threadGroup");
        Thread searchThread = new Thread(group, w);
        searchThread.start();
    }
}
