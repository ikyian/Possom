/*
 * BasicNavigationItem.java
 * 
 * Created on 11/06/2007, 11:25:25
 * 
 */

package no.sesat.search.result;

/**
 *
 * @author mick
 * @version $Id$
 */
public class BasicNavigationItem extends BasicResultList<NavigationItem> implements NavigationItem{

    private boolean selected = false;
    
    public BasicNavigationItem(){}

    /**
     * @param title
     * @param url
     * @param hitCount
     */
    public BasicNavigationItem(final String title, final String url, final int hitCount) {
        super(title, url, hitCount);
    }

    /**
     * @param selected
     */
    public void setSelected(final boolean selected) {
        this.selected = selected;
    }

    /**
     * @return
     */
    public boolean isSelected() {
        return selected;
    }
    
    public NavigationItem getSelectedChild() {
        
        return getChildSelectedImpl();
    }

    public boolean isChildSelected() {
        
        return null != getChildSelectedImpl();
    }

    public NavigationItem getChildByTitle(String title) {
        for (NavigationItem navigationItem : getResults()) {
            if (navigationItem.getTitle().equals(title)) {
                return navigationItem;
            }
        }
        return null;
    }

    private NavigationItem getChildSelectedImpl() {
        
        // XXX Geir's original work had a dirty flag here to cache this result.
        //  I doubt that this is an application hotspot, 
        //  and the flag wasn't bomb-proof, 
        // so it's all been removed for now.
        
        NavigationItem childSelected = null;
        for (NavigationItem item : getResults()) {
            if (item.isSelected()) {
                childSelected = item;
                break;
            }
        }
        return childSelected;
    }
}