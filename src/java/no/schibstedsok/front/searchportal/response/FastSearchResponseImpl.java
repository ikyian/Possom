/**
 * 
 */
package no.schibstedsok.front.searchportal.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * FAST specific response object that holds information
 * regarding search Time and spelling suggestions
 * 
 * @author Lars Johansson
 * 
 *
 */
public class FastSearchResponseImpl extends SearchResponseImpl {

    // set spelling suggestion from FAST if available in the Command
    private List spellingSuggestions = null;
	
	/**  the wikipedia result if existing*/ 
    private List wikiResult = new ArrayList();

	/**  the retriever results if existing in reponse */ 
	private List retrieverResults = new ArrayList();

	/** How many wiki results there was in index*/
	private int wikiDocumentsInIndex;

	/** How many media results there was in index*/
	private int mediaDocumentsInIndex;

	/** How many webcrawl results there was in index*/
	private int webCrawlDocumentsInIndex;


	
    /**
     * 
     * Default empty Constructor
     */
    public FastSearchResponseImpl() {
        super();
    }

	/** 
	 * Add a Wiki result to the <code>wikiResult</code> <code>Collection</code>.
	 * 
	 * @param wikiResult <code>SearchResultElement</code>
	 */
	public void addWikiResult(SearchResultElement wikiResult){
		this.wikiResult.add(wikiResult);
	}
	
	/** 
	 * 
	 * Add a Wiki result to the <code>wikiResult</code> <code>Collection</code>.
	 * 
	 * @param retrieverResult <code>SearchResultElement</code>
	 */
	public void addRetreiverResult(SearchResultElement retrieverResult){
		this.retrieverResults.add(retrieverResult);
	}
	
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this).append("spellingSuggestions",
                this.spellingSuggestions).append("documentsReturned",
                this.getDocumentsReturned()).append("fetchTime",
                this.getFetchTime()).append("result", this.getResults()).append(
                "totalDocumentsAvailable", this.getTotalDocumentsAvailable())
                .append("consequtiveSearchStartsAt",
                        this.getConsequtiveSearchStartsAt()).toString();
    }
    
    /** 
     * 
     * Returns a <code>Collection</code> of <code>String</code> spelling suggstions
     * 
     * @return
     */
    public Collection getSpellingSuggestions() {
        return spellingSuggestions;
    }
    

    /** 
     * 
     * Add a spelling suggestion to the <code>Collection</code>
     * 
     * @param spellingSuggestion
     */
    public void addSpellingSuggestion(String spellingSuggestion) {
        
		if(this.spellingSuggestions == null)
			this.spellingSuggestions = new ArrayList();
		
		this.spellingSuggestions.add(spellingSuggestion);
    }

	
	/**
	 * 
	 * Returns a <code>Collection</code> of <code>SearchResultElement</code> 
	 * containing Media results
	 * 
	 * @return <code>Collection</code>
	 */
	public Collection getRetrieverResults() {
		Collections.sort(retrieverResults);
		return retrieverResults;
	}
	

	/**
	 * 
	 * Returns a <code>Collection</code> of <code>SearchResultElement</code> 
	 * containing wiki results
	 * 
	 * @return <code>Collection</code>
	 */
	public Collection getWikiResult() {
		return wikiResult;
	}

	/** FIXME Comment this
	 * 
	 * @param count
	 */
	public void setWikiDocumentsReturned(int count) {
		this.wikiDocumentsInIndex = count;
		
	}

	/** 
	 * 
	 * How many media documents there was in index
	 * 
	 * @return
	 */
	public int getMediaDocumentsInIndex() {
		return mediaDocumentsInIndex;
	}
	

	public void setMediaDocumentsInIndex(int mediaDocumentsInIndex) {
		this.mediaDocumentsInIndex = mediaDocumentsInIndex;
	}
	

	/** 
	 * 
	 * How many webcrawl documents there was in index
	 * 
	 * @return
	 */
	public int getWebCrawlDocumentsInIndex() {
		return webCrawlDocumentsInIndex;
	}
	

	public void setWebCrawlDocumentsInIndex(int webCrawlDocumentsInIndex) {
		this.webCrawlDocumentsInIndex = webCrawlDocumentsInIndex;
	}
	

	/** 
	 * 
	 * How many wiki documents there was in index
	 * 
	 * @return
	 */
	public int getWikiDocumentsInIndex() {
		return wikiDocumentsInIndex;
	}
	

	public void setWikiDocumentsInIndex(int wikiDocumentsInIndex) {
		this.wikiDocumentsInIndex = wikiDocumentsInIndex;
	}

	/** 
	 * Returns all search <code>SearchResultElement</code> results available from the search.
	 * @return
	 */
	public Collection getAllResults() {
		List allResults = (ArrayList)getResults();
		allResults.addAll(getRetrieverResults());
		allResults.addAll(getWikiResult());
		return allResults;
	}
	
	


}
