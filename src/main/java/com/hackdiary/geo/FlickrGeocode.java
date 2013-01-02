package com.hackdiary.geo;

import com.vividsolutions.jts.*;
import com.vividsolutions.jts.index.*;
import com.vividsolutions.jts.index.strtree.*;
import com.vividsolutions.jts.geom.*;
import java.io.*;
import java.util.*;
import org.geotools.feature.simple.*;
import org.geotools.feature.simple.*;
import org.geotools.data.*;
import org.geotools.data.shapefile.*;
import org.opengis.feature.simple.*;
import org.opengis.feature.*;
import org.geotools.geometry.jts.JTS;

import java.io.IOException;
import java.net.URL;

public class FlickrGeocode {
  public STRtree tree = new STRtree();
  GeometryFactory gf = new GeometryFactory();
  public FlickrGeocode(URL url) throws IOException {
    this(Collections.singletonList(url));
  }
  public FlickrGeocode(List<URL> urls) throws IOException {
    org.geotools.util.logging.Logging.GEOTOOLS.setLoggerFactory(org.geotools.util.logging.Log4JLoggerFactory.getInstance());
    org.apache.log4j.LogManager.getLogger("org.geotools").setLevel(org.apache.log4j.Level.OFF);
    for(URL url : urls) {
      ShapefileDataStore data = new ShapefileDataStore(url);
      int count = 0;
      for(FeatureReader<SimpleFeatureType, SimpleFeature> reader = data.getFeatureReader() ; reader.hasNext(); count++) {
        SimpleFeature f = reader.next();
        tree.insert(JTS.toGeometry(f.getBounds()).getEnvelopeInternal(),f);
      }
    }
  }
  public List<Map<String, Object>> geocode(double lat, double lng) {
    Coordinate c = new Coordinate(lng, lat);
    Point p = gf.createPoint(c);
    List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
    for(Object f : tree.query(new Envelope(c))) {
      SimpleFeature feature = (SimpleFeature)f;
      MultiPolygon g = (MultiPolygon)feature.getDefaultGeometry();
      try {
        if(g.contains(p)) {
          Map<String, Object> result = new HashMap<String, Object>();
          for(Property prop : feature.getProperties()) {
            if(!prop.getName().toString().equals("the_geom")) {
              result.put(prop.getName().toString(), prop.getValue());
            }
          }
          result.put("midpoint_lat", feature.getBounds().getMedian(1));
          result.put("midpoint_lng", feature.getBounds().getMedian(0));
          results.add(result);
        }
      } catch(TopologyException e) {
        //resp.getWriter().print(e);
      }
    }
    return results;
  }
}
