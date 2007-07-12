/* Copyright (2006-2007) Schibsted Søk AS
 * SearchTab.java
 *
 * Created on 20 April 2006, 07:55
 *
 */

package no.schibstedsok.searchportal.view.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.schibstedsok.searchportal.mode.NavigationConfig;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Immutable POJO holding the view configuration for a given tab.
 *
 * @author <a href="mailto:mick@wever.org">Michael Semb Wever</a>
 * @version $Id$
 */
public final class SearchTab {


    // Constants -----------------------------------------------------

    private static final Logger LOG = Logger.getLogger(SearchTab.class);
    
    // Attributes ----------------------------------------------------
    
    private final String id;
    private final int pageSize;
    private final int pagingSize = 10;    
    private final int enrichmentLimit;
    private final int enrichmentOnTop;
    private final String adCommand;
    private final int adLimit;
    private final int adOnTop;
    private final SearchTab inherit;
    private final String key;
    private final String parentKey;
    private final String rssResultName;
    private final boolean rssHidden;
    private final boolean absoluteOrdering;
    private final boolean displayCss;   
    private final boolean executeOnBlank;
    private final Collection<EnrichmentHint> enrichments = new ArrayList<EnrichmentHint> ();
    private final String mode;
    private final int enrichmentOnTopScore;
    private final List<String> css = new ArrayList<String>();
    private final List<String> javascript = new ArrayList<String>();
    private final Layout layout;
    private final NavigationConfig navigationConfig;
    
    // Static --------------------------------------------------------

    // Constructors --------------------------------------------------

    /** Creates a new instance of SearchTab */
    SearchTab(
                final SearchTab inherit,
                final String id,
                final String mode,
                final String key,
                final String parentKey,
                final String rssResultName,
                final boolean rssHidden,
                final int pageSize,
                final NavigationConfig navConf,
                final int enrichmentLimit,
                final int enrichmentOnTop,
                final int enrichmentOnTopScore,
                final Collection<EnrichmentHint> enrichments,
                final String adCommand,
                final int adLimit,
                final int adOnTop,
                final List<String> css,
                final List<String> javascript,
                final boolean absoluteOrdering,
                final boolean displayCss,
                final boolean executeOnBlank,
                final Layout layout){

        this.inherit = inherit;
        this.id = id;

        // rather compact code. simply assigns the property to that pass in, or that from the inherit object, or null/-1
        this.mode = mode != null && mode.trim().length() >0 ? mode : inherit != null ? inherit.mode : null;
        this.key = key != null && key.trim().length() >0 ? key : inherit != null ? inherit.key : null;
        this.parentKey = parentKey != null && parentKey.trim().length() >0
                ? parentKey
                : inherit != null ? inherit.parentKey : null;
        this.pageSize = pageSize >=0 || inherit == null ? pageSize : inherit.pageSize;
        this.navigationConfig = navConf;
        this.enrichmentLimit = enrichmentLimit >=0 || inherit == null ? enrichmentLimit : inherit.enrichmentLimit;
        this.enrichmentOnTop = enrichmentOnTop >=0 || inherit == null ? enrichmentOnTop : inherit.enrichmentOnTop;
        this.enrichmentOnTopScore = enrichmentOnTopScore >=0 || inherit == null
                ? enrichmentOnTopScore
                : inherit.enrichmentOnTopScore;
        this.enrichments.addAll(enrichments);
        this.adCommand = adCommand != null && adCommand.trim().length() >0
                ? adCommand
                : inherit != null ? inherit.adCommand : null;
        this.adLimit = adLimit >=0 || inherit == null ? adLimit : inherit.adLimit;
        this.adOnTop = adOnTop >=0 || inherit == null ? adOnTop : inherit.adOnTop;
        this.displayCss = displayCss;
        if(inherit != null){
            // we cannot inherit navigators because there require a live reference to the applicable SearchTabFactory
            // but we do inherit enrichments and css
            this.enrichments.addAll(inherit.enrichments);
            this.css.addAll(inherit.css);            
        }
        this.rssResultName = rssResultName;
        this.css.addAll(css);
        this.javascript.addAll(javascript);
        this.absoluteOrdering = absoluteOrdering;
        this.executeOnBlank = executeOnBlank;
        this.rssHidden = rssHidden;
        this.layout = layout;
    }

    // Getters --------------------------------------------------------

    

    /**
     * Getter for property id.
     * @return Value of property id.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Getter for property pageSize.
     * @return Value of property pageSize.
     */
    public int getPageSize() {
        return this.pageSize;
    }

    /**
     * Getter for property pagingSize.
     * @return Value of property pagingSize.
     */
    public int getPagingSize() {
        return this.pagingSize;
    }
    
    /**
     * Getter for property enrichmentLimit.
     * @return Value of property enrichmentLimit.
     */
    public int getEnrichmentLimit() {
        return this.enrichmentLimit;
    }

    /**
     * Getter for property enrichmentOnTop.
     * @return Value of property enrichmentOnTop.
     */
    public int getEnrichmentOnTop() {
        return this.enrichmentOnTop;
    }

    /**
     * Getter for property adCommand.
     * @return Value of property adCommand.
     */
    public String getAdCommand() {
        return this.adCommand;
    }

    /**
     * Getter for property adsLimit.
     * @return Value of property adsLimit.
     */
    public int getAdLimit() {
        return this.adLimit;
    }

    /**
     * Getter for property adOnTop.
     * @return Value of property adOnTop.
     */
    public int getAdOnTop() {
        return this.adOnTop;
    }

    /**
     * Getter for property inherit.
     * @return Value of property inherit.
     */
    public SearchTab getInherit() {
        return this.inherit;
    }

    /**
     * Getter for property key.
     * @return Value of property key.
     */
    public String getKey() {

       if (parentKey != null) {
            return parentKey;
        } else {
            return key;
        }
    }

    /**
     * Getter for property parentKey.
     * @return Value of property parentKey.
     */
    public String getParentKey() {
        return this.parentKey;
    }

    /**
     * Getter for property rssResultName.
     * @return Value of property rssResultName.
     */
    public String getRssResultName() {
        return rssResultName;
    }

    /**
     * Returns true if there should be no visible links to the rss version of this tab.
     * @deprecated Not JavaBean compatable. Use isRssHidden() instead.
     *
     * @return true if hidden.
     */
    public boolean getRssHidden() {
        return rssHidden;
    }
    
    /**
     * Returns true if there should be no visible links to the rss version of this tab.
     *
     * @return true if hidden.
     */
    public boolean isRssHidden() {
        return rssHidden;
    }
    
    /**
     * Getter for property showRss
     * @return 
     * @deprecated Not JavaBean compatable. Use isShowRss() instead.
     */
    public boolean getShowRss() {
        return rssResultName != "" && !getRssHidden();
    }
    
    /**
     * Getter for property showRss
     * @return 
     */
    public boolean isShowRss() {
        return rssResultName != "" && !isRssHidden();
    }

    /**
     * Getter for property absoluteOrdering
     * @return 
     * @deprecated Not JavaBean compatable. Use isAbsoluteOrdering() instead.
     */
    public boolean getAbsoluteOrdering() {
        return absoluteOrdering;
    }

    /**
     * Getter for property executeOnFront
     * @return 
     */
    public boolean isExecuteOnBlank() {
        return executeOnBlank;
    }
    
    /**
     * Getter for property absoluteOrdering
     * @return 
     */
    public boolean isAbsoluteOrdering() {
        return absoluteOrdering;
    }
    
    /**
     * Getter for property displayCss
     * @return 
     */
    public boolean isDisplayCss() {
        return displayCss;
    }    
    
    /**
     * Getter for property enrichments.
     * @return Value of property enrichments.
     */
    public Collection<EnrichmentHint> getEnrichments() {
        return Collections.unmodifiableCollection(enrichments);
    }

    /**
     * 
     * @param command 
     * @return 
     */
    public EnrichmentHint getEnrichmentByCommand(final String command){

        for(EnrichmentHint e : enrichments){
            if(e.getCommand().equals(command)){
                return e;
            }
        }
        return null;
    }

    /**
     * Getter for property mode.
     * @return Value of property mode.
     */
    public String getMode() {
        return this.mode;
    }

    public String toString(){
        return id + (inherit != null ? " --> " + inherit.toString() : "");
    }

    /**
     * Getter for property enrichmentScoreOnTop.
     * @return Value of property enrichmentScoreOnTop.
     */
    public int getEnrichmentOnTopScore() {
        return this.enrichmentOnTopScore;
    }

    /**
     * 
     * @return 
     */
    public List<SearchTab> getAncestry(){
        // XXX cache result
        final List<SearchTab> ancestry = new ArrayList<SearchTab>();
        for(SearchTab t = this; t != null; t = t.getInherit()){
            if (t.displayCss) {
                ancestry.add(t);
            }
        }
        Collections.reverse(ancestry);
        return Collections.unmodifiableList(ancestry);
    }

    /**
     * Getter for property css.
     * @return Value of property css.
     */
    public List<String> getCss() {
        return Collections.unmodifiableList(css);
    }

    /**
     * Getter for property javascript.
     * @return Value of property javascript.
     */
    public List<String> getJavascript() {
        return Collections.unmodifiableList(javascript);
    }
    
    /**
     * Getter for property layout.
     * @return Value of property layout.
     */
    public Layout getLayout() {
        return layout;
    }
    
    public NavigationConfig getNavigationConfiguration(){
        return navigationConfig;
    }

    // Inner classes -------------------------------------------------

    /** Immutable POJO holding Enrichment properties from a given tab. **/
    public static final class EnrichmentHint  {

        /**
         * 
         * @param rule 
         * @param threshold 
         * @param weight 
         * @param command 
         */
        public EnrichmentHint(
                final String rule,
                final int threshold,
                final float weight,
                final String command){

            this.rule = rule;
            this.threshold = threshold;
            this .weight = weight;
            this.command = command;
        }

        /**
         * Holds value of property rule.
         */
        private final String rule;

        /**
         * Getter for property rule.
         * @return Value of property rule.
         */
        public String getRule() {
            return this.rule;
        }

        /**
         * Holds value of property threshold.
         */
        private final int threshold;

        /**
         * Getter for property threshold.
         * @return Value of property threshold.
         */
        public int getThreshold() {
            return this.threshold;
        }

        /**
         * Holds value of property command.
         */
        private final String command;

        /**
         * Getter for property command.
         * @return Value of property command.
         */
        public String getCommand() {
            return this.command;
        }

        /**
         * Holds value of property weight.
         */
        private final float weight;

        /**
         * Getter for property weight.
         * @return Value of property weight.
         */
        public float getWeight() {
            return this.weight;
        }
    }
//
//    /** Immutable POJO holding navigation information for a given tab. **/
//    public static final class NavigatorHint {
//        
//        /** Plain constructor. 
//         * @param id 
//         * @param name 
//         * @param displayName 
//         * @param match 
//         * @param tabName 
//         * @param urlSuffix 
//         * @param image 
//         * @param priority 
//         * @param template
//         * @param tabFactory 
//         */
//        public NavigatorHint(
//                final String id,
//                final String name,
//                final String displayName,
//                final MatchType match,
//                final String tabName,
//                final String urlSuffix,
//                final String image,
//                final int priority,
//                final String template,
//                final SearchTabFactory tabFactory){
//            
//            
//            assert null != tabFactory : "Must supply a tabFactory for getTab() to work";
//
//            this.id = id;
//            this.name = name;
//            this.displayName = displayName;
//            this.match = match;
//            this.tabName = tabName;
//            this.urlSuffix = urlSuffix;
//            this.image = image;
//            this.priority = priority;
//            this.template = template;
//            this.tabFactory = tabFactory;
//        }
//        
//        /** Copy constructor. 
//         * @param copy 
//         * @param tabFactory 
//         */
//        public NavigatorHint(
//                final NavigatorHint copy,
//                final SearchTabFactory tabFactory){
//            
//            
//            assert null != tabFactory : "Must supply a tabFactory for getTab() to work";
//
//            this.id = copy.id;
//            this.name = copy.name;
//            this.displayName = copy.displayName;
//            this.match = copy.match;
//            this.tabName = copy.tabName;
//            this.urlSuffix = copy.urlSuffix;
//            this.image = copy.image;
//            this.priority = copy.priority;
//            this.template = copy.template;
//            this.tabFactory = tabFactory;
//        }
//
//        /**
//         * 
//         */
//        public enum MatchType {
//            /**
//             * 
//             */
//            PREFIX,
//            /**
//             * 
//             */
//            EQUAL,
//            /**
//             * 
//             */
//            SUFFIX;
//        }
//
//        private final SearchTabFactory tabFactory;
//
//        private final String id;
//        /**
//         * Getter for property id
//         * @return Value of property id.
//         */
//        public String getId() {
//            return id;
//        }
//   
//        /**
//         * Holds value of property tabName.
//         */
//        private final String tabName;
//
//
//        /**
//         * Getter for property tab.
//         * @return Value of property tab.
//         */
//        public String getTabName() {
//
//            return this.tabName;
//        }
//
//        /**
//         * Returns the tab associated with this hint.
//         * @return 
//         */
//        public SearchTab getTab() {
//            return tabFactory.getTabByName(tabName);
//        }
//
//        /**
//         * Holds value of property name.
//         */
//        private final String name;
//
//        /**
//         * Getter for property name.
//         * @return Value of property name.
//         */
//        public String getName() {
//            return this.name;
//        }
//        /**
//         * Holds value of property name.
//         */
//        private final String displayName;
//
//        /**
//         * Getter for property name.
//         * @return Value of property name.
//         */
//        public String getDisplayName() {
//            return this.displayName;
//        }
//
//        /**
//         * Holds value of property match.
//         */
//        private final MatchType match;
//
//        /**
//         * Getter for property match.
//         * @return Value of property match.
//         */
//        public MatchType getMatch() {
//            return this.match;
//        }
//
//        /**
//         * Holds value of property urlSuffix.
//         */
//        private final String urlSuffix;
//
//        /**
//         * Getter for property urlSuffix.
//         * @return Value of property urlSuffix.
//         */
//        public String getUrlSuffix() {
//            return this.urlSuffix;
//        }
//
//        /**
//         * Holds value of property image.
//         */
//        private String image;
//
//        /**
//         * Getter for property image.
//         * @return Value of property image.
//         */
//        public String getImage() {
//            return this.image;
//        }
//
//        private final int priority;
//
//        /**
//         * Getter for  property priority.
//         * @return Value of property priority.
//         */
//        public int getPriority() {
//            return this.priority;
//        }
//        
//        private final String template;
//        
//        public String getTemplate(){
//            return template;
//        }
//        
//    }

    /** POJO holding layout information for the given tab. 
     * readLayout(Element) is the only way to mutate the bean and can only be called once.
     **/
    public static final class Layout{
        
        private String origin;
        private String main;
        private String front;
        private Map<String,String> includes;
        private Map<String,String> properties;
        
        private Layout(){}
        
        /**
         * 
         * @param inherit 
         */
        public Layout(final Layout inherit){
            if( null != inherit ){
                // origin cannot be inherited!
                main = inherit.main;
                front = inherit.front;
                includes = inherit.includes;
                properties = inherit.properties;
            }
        }
        
        /**
         * 
         * @return 
         */
        public Map<String,String> getIncludes(){
            
            return includes;
        }
        
        /**
         * 
         * @param key 
         * @return 
         */
        public String getInclude(final String key){
            
            return includes.get(key);
        }
        
        /**
         * 
         * @return 
         */
        public Map<String,String> getProperties(){
            return properties;
        }
        
        /**
         * 
         * @param key 
         * @return 
         */
        public String getProperty(final String key){
            return properties.get(key);
        }
        
        /** 
         * @return 
         @deprecated **/
        public String getOrigin(){
            return origin;
        }
        
        /**
         * 
         * @return 
         */
        public String getMain(){
            return main;
        }
        
        /**
         * 
         * @return 
         */
        public String getFront(){
            return front;
        }
        
        /** Will return null when the element argument is null. 
         * Otherwise returns the Layout object deserialised from the contents of the Element.
         ** @param element 
         * @return 
         */
        public Layout readLayout(final Element element){
            
            if( null != origin ){
                throw new IllegalStateException("Not allowed to call readLayout(element) twice");
            }
            if( null != element ){

                origin = element.getAttribute("origin");
                if(0 < element.getAttribute("main").length()){
                    main = element.getAttribute("main");
                }
                if(0 < element.getAttribute("front").length()){
                    front = element.getAttribute("front");
                }
                includes = readMap(includes, element.getElementsByTagName("include"), "key", "template");
                properties = readMap(properties, element.getElementsByTagName("property"), "key", "value");
            }
            
            return null == element ? null : this;
        }
        
        private Map<String,String> readMap(
                final Map<String,String> inherited,
                final NodeList list, 
                final String keyElementName, 
                final String valueElementName){
            
            final Map<String,String> map 
                    = new HashMap<String,String>(null != inherited ? inherited : Collections.EMPTY_MAP);
            
            for(int i = 0; i< list.getLength(); ++i){
                final Element include = (Element) list.item(i);
                final String key = include.getAttribute(keyElementName);
                map.put(key, include.hasAttribute(valueElementName) ? include.getAttribute(valueElementName) : "");
            }
            return Collections.unmodifiableMap(map);
        }
    }


}