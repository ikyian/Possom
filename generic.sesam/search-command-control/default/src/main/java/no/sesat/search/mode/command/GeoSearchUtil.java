/* Copyright (2006-2007) Schibsted Søk AS
 * This file is part of SESAT.
 *
 *   SESAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   SESAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with SESAT.  If not, see <http://www.gnu.org/licenses/>.

 */
package no.sesat.search.mode.command;

import javax.measure.units.SI;

import no.sesat.search.datamodel.request.ParametersDataObject;

import org.apache.log4j.Logger;
import org.jscience.geography.coordinates.LatLong;
import org.jscience.geography.coordinates.UTM;
import org.jscience.geography.coordinates.crs.ReferenceEllipsoid;

/**
 * Utility class for GEO search.
 * 
 * @author <a href="mailto:anders@sesam.no">Anders Johan Jamtli</a>
 * @author Stian Hegglund.
 * @version $Id$
 */
public class GeoSearchUtil {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(GeoSearchUtil.class);

    /** Constants for selected map rectangel. */
    private static final String MIN_X = "minX";
    private static final String MAX_X = "maxX";
    private static final String MIN_Y = "minY";
    private static final String MAX_Y = "maxY";
    
    /** UTM center x cordinate. */
    private static final String CENTER_X = "cx";
    
    /** UTM center y cordinate. */
    private static final String CENTER_Y = "cy";
    
    /** Request parameter name for radius restriction in GEO search. */
    private static final String RADIUS_PARAMETER_NAME = "radius";

    /** Measure unit to use. */
    public static final String RADIUS_MEASURE_UNIT_TYPE = "km";

    /** The sort by to use when the search is a geo search. */
    public static final String GEO_SORT_BY = "geo_spec_sortable";


    /** Utility class, should be used by calling static methods. */
    private GeoSearchUtil() {

    }


    /**
     * Calcluates a center point from minX,maxX,minY,maxY parameters.
     * If a centerpoint is given in the request, return new LatLong converted centerpoint.
     * @param requestParameters Parameters for the request.
     * @return The center point in latlong format.
     */
    public static String getCenter(final ParametersDataObject requestParameters) {

        if (!isGeoSearch(requestParameters)) {
            throw new IllegalArgumentException(
                    "Given requestParameter object must contain parameters: minX,maxX,minY,maxY");
        }
        
        if(hasCenterPoint(requestParameters)){
            int centerX = Integer.parseInt(requestParameters.getValue(CENTER_X).getString());
            int centerY = Integer.parseInt(requestParameters.getValue(CENTER_Y).getString());
            final UTM utm = UTM.valueOf(33, 'W', centerX, centerY, SI.METER);
            LatLong latLong = UTM.utmToLatLong(utm, ReferenceEllipsoid.WGS84);
            final double latLongX = latLong.getOrdinate(0);
            final double latLongY = latLong.getOrdinate(1);
            
            StringBuilder centerPoint = new StringBuilder();
            centerPoint.append("(").append(latLongX).append(",").append(latLongY).append(")");
            return centerPoint.toString();
        }

        final int minX = Integer.parseInt((String) requestParameters.getValue(MIN_X).getString());
        final int maxX = Integer.parseInt((String) requestParameters.getValue(MAX_X).getString());
        final int minY = Integer.parseInt((String) requestParameters.getValue(MIN_Y).getString());
        final int maxY = Integer.parseInt((String) requestParameters.getValue(MAX_Y).getString());

        final UTM utmMin = UTM.valueOf(33, 'W', minX, minY, SI.METER);
        final UTM utmMax = UTM.valueOf(33, 'W', maxX, maxY, SI.METER);

        final LatLong llMin = UTM.utmToLatLong(utmMin, ReferenceEllipsoid.WGS84);
        final LatLong llMax = UTM.utmToLatLong(utmMax, ReferenceEllipsoid.WGS84);

        final double llMinX = llMin.getOrdinate(0);
        final double llMaxX = llMax.getOrdinate(0);
        final double llMinY = llMin.getOrdinate(1);
        final double llMaxY = llMax.getOrdinate(1);

        LOG.debug("(" + minX + "," + minY + ") (" + llMinX + "," + llMinY + ")");
        LOG.debug("(" + maxX + "," + maxY + ") (" + llMaxX + "," + llMaxY + ")");

        final String center = new StringBuilder("(").append(llMinX + (llMaxX - llMinX) / 2).append(",").append(
                llMinY + (llMaxY - llMinY) / 2).append(")").toString();
        return center;

    }


    /**
     * Responsible for checking if a ParameterDataObject is a geosearch.
     * @param requestParameters
     * @return true if the given parameter object contains minX,maxX,minY,maxY values.
     */
    public static boolean isGeoSearch(final ParametersDataObject requestParameters) {
        //centerpoint is also a geo search.
        if(hasCenterPoint(requestParameters)){
            return true;
        }
        
        if (requestParameters.getValue(MIN_X) == null || requestParameters.getValue(MAX_X) == null
            || requestParameters.getValue(MIN_Y) == null || requestParameters.getValue(MAX_Y) == null) {
            return false;
        }

        if (requestParameters.getValue(MIN_X).getString().length() == 0
            || requestParameters.getValue(MAX_X).getString().length() == 0
            || requestParameters.getValue(MIN_Y).getString().length() == 0
            || requestParameters.getValue(MAX_Y).getString().length() == 0) {
            return false;
        }
        
       
        return true;
    }

    /**
     * Responsible for checking if a ParameterDataObject has parameters for centerpoint.
     * 
     * @param requestParameters The ParameterDataObject to check.
     * @return true if the given object has x,y and restriction radius.
     */
    public static boolean hasCenterPoint(ParametersDataObject requestParameters) {
        if(requestParameters.getValue(CENTER_X) == null || requestParameters.getValue(CENTER_Y) == null ||
                requestParameters.getValue(RADIUS_PARAMETER_NAME) == null){
            return false;
        }
        
        if(requestParameters.getValue(CENTER_X).getString().length() == 0 || requestParameters.getValue(CENTER_Y).getString().length() == 0||
        requestParameters.getValue(RADIUS_PARAMETER_NAME).getString().length() == 0){
            return false;
        }
        
        return true;
    }

    /**
     * Retruns the radius to search from a given centerpoint or map selection.
     * @param pdo The ParameterDataObject.
     * @return The radius from the centerpoint to search.
     */
    public static String getRadiusRestriction(final ParametersDataObject pdo) { 
        if (isGeoSearch(pdo)) {
            if(hasCenterPoint(pdo)){
                return pdo.getValue(RADIUS_PARAMETER_NAME).getString();
            }
            final int minX = Integer.parseInt((String) pdo.getValue(MIN_X).getString());
            final int maxX = Integer.parseInt((String) pdo.getValue(MAX_X).getString());
            
            double restrictedRadius =  ((maxX - minX) / 2) / 1000.0;
            return Double.toString(restrictedRadius);
        }
        throw new IllegalArgumentException("Given requestParameter object must contain parameters: minX,maxX,minY,maxY");
    }
}