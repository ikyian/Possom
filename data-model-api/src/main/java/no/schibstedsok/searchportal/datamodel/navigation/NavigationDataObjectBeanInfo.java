// Copyright (2007) Schibsted Søk AS
/*
 * MapDataObjectBeanInfo.java
 *
 * Created on 30 January 2007, 20:51
 *
 */

package no.schibstedsok.searchportal.datamodel.navigation;

import no.schibstedsok.searchportal.datamodel.*;
import no.schibstedsok.searchportal.datamodel.generic.MapDataObjectBeanInfo;
import java.beans.PropertyDescriptor;
import org.apache.log4j.Logger;

/**
 *
 * @author <a href="mailto:mick@semb.wever.org">Mck</a>
 * @version <tt>$Id$</tt>
 */
public final class NavigationDataObjectBeanInfo extends MapDataObjectBeanInfo{
    
    // Constants -----------------------------------------------------
    
    private static final Class BEAN_CLASS = NavigationDataObject.class;
    
    private static final Logger LOG = Logger.getLogger(NavigationDataObjectBeanInfo.class);
    
    // Attributes ----------------------------------------------------
    
    // Static --------------------------------------------------------
    
    
    // Constructors --------------------------------------------------
    
    /** Creates a new instance of MapDataObjectBeanInfo */
    public NavigationDataObjectBeanInfo() {
    }
    
    // Public --------------------------------------------------------
    
    @Override
    public PropertyDescriptor[] getPropertyDescriptors(){
        
      return MapDataObjectBeanInfo.addSingleMappedPropertyDescriptor("navigation", BEAN_CLASS);
    }
    
    // Package protected ---------------------------------------------
    
    // Protected -----------------------------------------------------
    
    // Private -------------------------------------------------------
    
    // Inner classes -------------------------------------------------
    
}