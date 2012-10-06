package com.hackdiary.geo;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.*;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

public class FlickrGeocodeServlet extends HttpServlet {
  FlickrGeocode geocode;
  public FlickrGeocodeServlet() throws IOException {
    geocode = new CachingFlickrGeocode(getClass().getResource("flickr_shapes_public_dataset_2.0/flickr_shapes_countries/OGRGeoJSON.shp"));
  }
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    double lat = Double.parseDouble(req.getParameter("lat"));
    double lng = Double.parseDouble(req.getParameter("lng"));
    ObjectMapper mapper = new ObjectMapper();
    mapper.writerWithDefaultPrettyPrinter().writeValue(resp.getWriter(), geocode.geocode(lat, lng));
  }

  public static void main(String[] args) throws Exception{
    Server server = new Server(Integer.valueOf(System.getenv("PORT")));
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
    context.addServlet(new ServletHolder(new FlickrGeocodeServlet()),"/*");
    server.start();
    server.join();   
  }
}
