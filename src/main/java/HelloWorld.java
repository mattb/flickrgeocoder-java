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
import org.geotools.geometry.jts.JTS;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

public class HelloWorld extends HttpServlet {
  public STRtree tree = new STRtree();
  public HelloWorld() throws IOException {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
    resp.getWriter().print(tree.size() + " shapes loaded.");
    }

  public static void main(String[] args) throws Exception{
    Server server = new Server(Integer.valueOf(System.getenv("PORT")));
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
    context.addServlet(new ServletHolder(new HelloWorld()),"/*");
    server.start();
    server.join();   
  }
}
