// Copyright (2007) Schibsted Søk AS
package no.schibstedsok.searchportal.mode.command;

import no.schibstedsok.searchportal.datamodel.DataModel;
import no.schibstedsok.searchportal.datamodel.generic.StringDataObject;
import no.schibstedsok.searchportal.mode.config.NewsAggregatorCommandConfig;
import no.schibstedsok.searchportal.result.BasicSearchResult;
import no.schibstedsok.searchportal.result.BasicSearchResultItem;
import no.schibstedsok.searchportal.result.FastSearchResult;
import no.schibstedsok.searchportal.result.Modifier;
import no.schibstedsok.searchportal.result.Navigator;
import no.schibstedsok.searchportal.result.SearchResult;
import no.schibstedsok.searchportal.result.SearchResultItem;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

/**
 * Search command that will try to get pregenerated clusters from xml files. If the xml file is not available it will
 * fall back to a search.
 */
public class NewsAggregatorSearchCommand extends ClusteringESPFastCommand {

    private static final Logger LOG = Logger.getLogger(NewsAggregatorSearchCommand.class);
    private static final String PARAM_CLUSTER_ID = "clusterId";

    /**
     * @param cxt       The context to execute in.
     */
    public NewsAggregatorSearchCommand(final Context cxt) {
        super(cxt);
    }

    public SearchResult execute() {
        NewsAggregatorCommandConfig config = getSearchConfiguration();
        StringDataObject clusterId = datamodel.getParameters().getValue(PARAM_CLUSTER_ID);
        String xmlFile = getXmlFileName(datamodel, config);
        LOG.debug("Loading xml file at: " + config.getXmlSource() + xmlFile);
        if (clusterId == null) {
            return getPageResult(config, xmlFile);
        } else {
            return getClusterResult(config, clusterId, xmlFile);
        }
    }

    private String getXmlFileName(DataModel dataModel, NewsAggregatorCommandConfig config) {
        String geographic = "main";
        String category = "main";
        String[] geographicFields = config.getGeographicFieldArray();
        for (String geographicField : geographicFields) {
            StringDataObject geo = dataModel.getParameters().getValue(geographicField);
            if (geo != null) {
                geographic = formatToConvention(geo.getString());
                break;
            }
        }

        for (String categoryField : config.getCategoryFieldArray()) {
            StringDataObject cat = dataModel.getParameters().getValue(categoryField);
            if (cat != null) {
                category = formatToConvention(cat.getString());
                break;
            }
        }
        StringBuilder sb = new StringBuilder("fp_");
        sb.append(category).append('_').append(geographic).append(".xml");
        return sb.toString();
    }

    private String formatToConvention(String replaceString) {
        String newString = StringUtils.replaceChars(replaceString.toLowerCase(), "\u00E6", "ae");
        newString = StringUtils.replaceChars(newString, '\u00F8', 'o');
        newString = StringUtils.replaceChars(newString, '\u00E5', 'a');
        newString = StringUtils.replaceChars(newString, ' ', '_');
        return newString;
    }

    private SearchResult getClusterResult(NewsAggregatorCommandConfig config, StringDataObject clusterId, String xmlFile) {
        try {
            final NewsAggregatorXmlParser newsAggregatorXmlParser = new NewsAggregatorXmlParser();
            final InputStream inputStream = getInputStream(config, xmlFile);
            SearchResult searchResult = newsAggregatorXmlParser.parseCluster(config, inputStream, clusterId.getString(), this);
            if (searchResult != null && searchResult.getHitCount() > 0) {
                return searchResult;
            }
        } catch (IOException e) {
            LOG.debug("Falling back to search instead of xml parse", e);
        } catch (JDOMException e) {
            LOG.debug("Falling back to search instead of xml parse", e);
        }
        return search(config, clusterId.getString());
    }

    private SearchResult search(NewsAggregatorCommandConfig config, String clusterId) {
        LOG.debug("------ Running search to get clusters ---------");
        LOG.debug("clusterId=" + clusterId);
        LOG.debug("result-fields=" + config.getResultFields());
        LOG.debug("query-server=" + config.getQueryServer());
        LOG.debug("-----------------------------------------------");
        return super.execute();
    }

    @Override
    public NewsAggregatorCommandConfig getSearchConfiguration() {
        return (NewsAggregatorCommandConfig) super.getSearchConfiguration();
    }


    private SearchResult getPageResult(NewsAggregatorCommandConfig config, String xmlFile) {
        final NewsAggregatorXmlParser newsAggregatorXmlParser = new NewsAggregatorXmlParser();
        try {
            SearchResult searchResult = newsAggregatorXmlParser.parseFullPage(config, getOffset(), getInputStream(config, xmlFile), this);
            if (searchResult != null && searchResult.getHitCount() > 0) {
                return searchResult;
            }
        } catch (JDOMException e) {
            LOG.debug("Falling back to search instead of xml parse", e);
        } catch (IOException e) {
            LOG.debug("Falling back to search instead of xml parse", e);
        }
        return search(config, null);
    }

    private InputStream getInputStream(NewsAggregatorCommandConfig config, String xmlFile) throws IOException {
        final URL url = new URL(config.getXmlSource() + xmlFile);
// ---------
//        Can not use HTTPClient in this case since it ignores protocol of the url. (need file:// and https://)
//  --------
//        final HTTPClient client = HTTPClient.instance(url.getHost(), url.getHost(), url.getPort());
//        try{
//            return client.getBufferedStream(url.getHost(), url.getPath());
//        }catch (IOException ex) {
//            throw client.interceptIOException(ex);
//        }
        final URLConnection urlConnection = url.openConnection();
        urlConnection.setConnectTimeout(1000);
        urlConnection.setReadTimeout(1000);
        return new BufferedInputStream(urlConnection.getInputStream());
    }

    @SuppressWarnings({"unchecked"})
    public static class NewsAggregatorXmlParser {
        private static final String ELEMENT_CLUSTER = "cluster";
        private static final String ELEMENT_ENTRY_COLLECTION = "entryCollection";
        private static final String ATTRIBUTE_FULL_COUNT = "fullcount";
        private static final String ATTRIBUTE_CLUSTERID = "id";
        private static final String ELEMENT_RELATED = "related";
        private static final String ATTRIBUTE_TYPE = "type";
        private static final String ELEMENT_CATEGORY = "category";
        private static final String ELEMENT_COLLAPSEID = "collapseid";
        private static final String ELEMENT_GEONAVIGATION = "geonavigation";
        private static final String ATTRIBUTE_NAME = "name";
        private static final String ATTRIBUTE_XML = "xml";
        private static final String ELEMENT_COUNTS = "counts";
        private static final String ATTRIBUTE_ENTRY_COUNT = "entries";
        private static final String ATTRIBUTE_CLUSTER_COUNT = "clusters";

        private Document getDocument(InputStream inputStream) throws JDOMException, IOException {
            final SAXBuilder saxBuilder = new SAXBuilder();
            saxBuilder.setValidation(false);
            saxBuilder.setIgnoringElementContentWhitespace(true);

            return saxBuilder.build(inputStream);
        }

        public FastSearchResult parseCluster(NewsAggregatorCommandConfig config, InputStream inputStream, String clusterId, NewsAggregatorSearchCommand searchCommand) throws JDOMException, IOException {
            try {
                final FastSearchResult searchResult = new FastSearchResult(searchCommand);
                final Document doc = getDocument(inputStream);
                final Element root = doc.getRootElement();
                List<Element> clusters = root.getChildren(ELEMENT_CLUSTER);
                for (Element cluster : clusters) {
                    if (cluster.getAttributeValue(ATTRIBUTE_CLUSTERID).equals(clusterId)) {
                        handleFlatCluster(config, cluster, searchCommand, searchResult);
                        handleRelated(config, cluster.getChild(ELEMENT_RELATED), searchResult);
                        break;
                    }
                }
                handleGeoNav(root.getChild(ELEMENT_GEONAVIGATION), searchResult);
                return searchResult;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        // Ignoring
                    }
                }
            }
        }

        public FastSearchResult parseFullPage(NewsAggregatorCommandConfig config, int offset, InputStream inputStream, SearchCommand searchCommand) throws JDOMException, IOException {
            try {

                final FastSearchResult searchResult = new FastSearchResult(searchCommand);
                final Document doc = getDocument(inputStream);
                final Element root = doc.getRootElement();

                handleClusters(config, offset, root.getChildren(ELEMENT_CLUSTER), searchResult, searchCommand);
                handleCounts(config, root.getChild(ELEMENT_COUNTS), offset, searchResult);
                handleRelated(config, root.getChild(ELEMENT_RELATED), searchResult);
                handleGeoNav(root.getChild(ELEMENT_GEONAVIGATION), searchResult);
                return searchResult;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        // Ignoring
                    }
                }
            }
        }

        private void handleCounts(NewsAggregatorCommandConfig config, Element countsElement, int offset, FastSearchResult searchResult) {
            if (countsElement != null) {
                final String entries = countsElement.getAttributeValue(ATTRIBUTE_ENTRY_COUNT);
                if (entries != null && entries.length() > 0) {
                    searchResult.setHitCount(Integer.parseInt(entries));
                }
                final String clusters = countsElement.getAttributeValue(ATTRIBUTE_CLUSTER_COUNT);
                if (clusters != null && clusters.length() > 0) {
                    if (offset + config.getResultsToReturn() < Integer.parseInt(clusters)) {
                        searchResult.addField(ClusteringESPFastCommand.PARAM_NEXT_OFFSET, Integer.toString(offset + config.getResultsToReturn()));
                    }
                }
            }
        }

        private void handleGeoNav(Element geonavElement, FastSearchResult searchResult) {
            if (geonavElement != null) {
                final List<Element> geoNavElements = geonavElement.getChildren();
                for (Element geoNavElement : geoNavElements) {
                    String navigationType = geoNavElement.getAttributeValue(ATTRIBUTE_TYPE);
                    Navigator nav = new Navigator(geoNavElement.getAttributeValue(ATTRIBUTE_XML), null, geoNavElement.getAttributeValue(ATTRIBUTE_NAME), null);
                    Modifier modifier = new Modifier(geoNavElement.getAttributeValue(ATTRIBUTE_NAME), -1, nav);
                    searchResult.addModifier(navigationType, modifier);
                }
            }
        }

        private void handleRelated(NewsAggregatorCommandConfig config, Element relatedElement, FastSearchResult searchResult) {
            if (relatedElement != null) {
                final List<Element> categoryElements = relatedElement.getChildren(ELEMENT_CATEGORY);
                for (Element categoryElement : categoryElements) {
                    final String categoryType = categoryElement.getAttributeValue(ATTRIBUTE_TYPE);

                    final List<Modifier> relatedList = searchResult.getModifiers(categoryType);
                    int categoryCount = 0;
                    if (relatedList != null) {
                        categoryCount = relatedList.size();
                    }
                    if (categoryCount < config.getRelatedMaxCount()) {
                        final Modifier modifier = new Modifier(categoryElement.getTextTrim(), -1, null);
                        searchResult.addModifier(categoryType, modifier);
                    }
                }
            }
        }

        private void handleClusters(NewsAggregatorCommandConfig config, int offset, List<Element> clusters, SearchResult searchResult, SearchCommand searchCommand) {
            int maxOffset = offset + config.getResultsToReturn();
            for (int i = offset; i < clusters.size() && i < maxOffset; i++) {
                Element cluster = clusters.get(i);
                handleCluster(config, cluster, searchCommand, searchResult);
            }
        }

        private void handleFlatCluster(NewsAggregatorCommandConfig config, Element cluster, SearchCommand searchCommand, SearchResult searchResult) {
            if (cluster != null) {
                final Element entryCollectionElement = cluster.getChild(config.getNestedResultsField());
                if (entryCollectionElement != null) {
                    final List<Element> entryList = entryCollectionElement.getChildren();
                    searchResult.setHitCount(entryList.size());

                    final HashMap<String, SearchResultItem> collapseMap = new HashMap<String, SearchResultItem>();
                    for (Element entry : entryList) {
                        final SearchResultItem searchResultItem = new BasicSearchResultItem();
                        handleEntry(entry, searchResultItem);
                        addResult(config, searchResultItem, searchResult, searchCommand, collapseMap);
                    }
                }
            }
        }

        private int handleCluster(NewsAggregatorCommandConfig config, Element cluster, SearchCommand searchCommand, SearchResult searchResult) {
            if (cluster != null) {
                final SearchResultItem searchResultItem = new BasicSearchResultItem();
                searchResultItem.addField("size", Integer.toString(Integer.parseInt(cluster.getAttributeValue(ATTRIBUTE_FULL_COUNT)) - 1));
                searchResultItem.addField(PARAM_CLUSTER_ID, cluster.getAttributeValue(ATTRIBUTE_CLUSTERID));

                final Element entryCollectionElement = cluster.getChild(ELEMENT_ENTRY_COLLECTION);
                final List<Element> entryList = entryCollectionElement.getChildren();
                final BasicSearchResult nestedSearchResult = new BasicSearchResult(searchCommand);

                for (int i = 0; i < entryList.size(); i++) {
                    Element nestedEntry = entryList.get(i);
                    if (i == 0) {
                        // First element is main result
                        handleEntry(nestedEntry, searchResultItem);
                    } else {
                        SearchResultItem nestedResultItem = new BasicSearchResultItem();
                        handleEntry(nestedEntry, nestedResultItem);
                        addResult(config, nestedResultItem, nestedSearchResult, searchCommand);
                    }
                }
                searchResultItem.addNestedSearchResult(config.getNestedResultsField(), nestedSearchResult);
                searchResult.addResult(searchResultItem);
                nestedSearchResult.setHitCount(entryList.size());
                return entryList.size();
            }
            return 0;
        }


        private void handleEntry(Element entryElement, SearchResultItem searchResultItem) {
            final List<Element> entrySubElements = entryElement.getChildren();
            for (Element entrySubElement : entrySubElements) {
                if (entrySubElement.getText() != null && entrySubElement.getTextTrim().length() > 0) {
                    searchResultItem.addField(entrySubElement.getName(), entrySubElement.getTextTrim());
                }
            }
        }

        private void addResult(NewsAggregatorCommandConfig config, SearchResultItem nestedResultItem,
                               SearchResult nestedSearchResult,
                               SearchCommand searchCommand) {
            addResult(config, nestedResultItem, nestedSearchResult, searchCommand, null);
        }


        private void addResult(NewsAggregatorCommandConfig config,
                               SearchResultItem nestedResultItem,
                               SearchResult nestedSearchResult,
                               SearchCommand searchCommand,
                               HashMap<String, SearchResultItem> collapseMap) {
            // Check if entry is duplicate and should be a subresult
            SearchResultItem collapseParent = null;
            String collapseId = nestedResultItem.getField(ELEMENT_COLLAPSEID);
            if (collapseMap != null) {
                collapseParent = collapseMap.get(collapseId);
            }
            if (collapseParent == null) {
                // No duplicate in results or should not be collapsed
                nestedSearchResult.addResult(nestedResultItem);
                if (collapseMap != null) {
                    collapseMap.put(collapseId, nestedResultItem);
                }
            } else {
                // duplicate item, adding as a subresult to first item.
                SearchResult collapsedResults = collapseParent.getNestedSearchResult(config.getNestedResultsField());
                if (collapsedResults == null) {
                    collapsedResults = new BasicSearchResult(searchCommand);
                    collapseParent.addNestedSearchResult(config.getNestedResultsField(), collapsedResults);
                }
                collapsedResults.addResult(nestedResultItem);
            }
        }
    }

}
