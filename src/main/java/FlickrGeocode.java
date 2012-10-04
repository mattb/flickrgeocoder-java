import com.fasterxml.jackson.databind.ObjectMapper;
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
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

public class FlickrGeocode extends HttpServlet {
  public STRtree tree = new STRtree();
  public FlickrGeocode() throws IOException {
    org.geotools.util.logging.Logging.GEOTOOLS.setLoggerFactory(org.geotools.util.logging.Log4JLoggerFactory.getInstance());
    org.apache.log4j.LogManager.getLogger("org.geotools").setLevel(org.apache.log4j.Level.OFF);
    ShapefileDataStore data = new ShapefileDataStore(getClass().getResource("flickr_shapes_public_dataset_2.0/flickr_shapes_localities/OGRGeoJSON.shp"));
    int count = 0;
    for(FeatureReader<SimpleFeatureType, SimpleFeature> reader = data.getFeatureReader() ; reader.hasNext(); count++) {
      SimpleFeature f = reader.next();
      tree.insert(JTS.toGeometry(f.getBounds()).getEnvelopeInternal(),f);
    }
  }
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    double lat = Double.parseDouble(req.getParameter("lat"));
    double lng = Double.parseDouble(req.getParameter("lng"));
    GeometryFactory gf = new GeometryFactory();
    Coordinate c = new Coordinate(lng, lat);
    Point p = gf.createPoint(c);
    List<Map> results = new ArrayList<Map>(5);
    for(Object f : tree.query(new Envelope(c))) {
      SimpleFeature feature = (SimpleFeature)f;
      MultiPolygon g = (MultiPolygon)feature.getDefaultGeometry();
      try {
        if(g.contains(p)) {
          Map<String, Object> result = new HashMap();
          for(Property prop : feature.getProperties()) {
            if(!prop.getName().toString().equals("the_geom")) {
              result.put(prop.getName().toString(), prop.getValue());
            }
          }
          results.add(result);
        }
      } catch(TopologyException e) {
        //resp.getWriter().print(e);
      }
    }
    ObjectMapper mapper = new ObjectMapper();
    mapper.writerWithDefaultPrettyPrinter().writeValue(resp.getWriter(), results);
  }

  public static void main(String[] args) throws Exception{
    Server server = new Server(Integer.valueOf(System.getenv("PORT")));
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
    context.addServlet(new ServletHolder(new FlickrGeocode()),"/*");
    server.start();
    server.join();   
  }
}
