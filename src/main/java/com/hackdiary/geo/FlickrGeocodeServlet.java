package com.hackdiary.geo;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.*;
import java.net.*;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

public class FlickrGeocodeServlet extends HttpServlet {
  FlickrGeocode geocode;
  public FlickrGeocodeServlet() throws IOException {
    geocode = new CachingFlickrGeocode(Collections.singletonList(getClass().getResource("zillow/ZillowNeighborhoods-CA.shp")));
  }
  public FlickrGeocodeServlet(String[] files) throws IOException {
    List<URL> urls = new ArrayList<URL>(files.length);
    for(String file : files) {
      urls.add(new File(file).toURI().toURL());
    }
    geocode = new CachingFlickrGeocode(urls);
  }
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    double lat = Double.parseDouble(req.getParameter("lat"));
    double lng = Double.parseDouble(req.getParameter("lng"));
    resp.setContentType("application/json");
    resp.setHeader("Access-Control-Allow-Origin", "*");
    ObjectMapper mapper = new ObjectMapper();
    mapper.writerWithDefaultPrettyPrinter().writeValue(resp.getWriter(), geocode.geocode(lat, lng));
  }

  public static void main(String[] args) throws Exception{
    Server server = new Server(Integer.valueOf(System.getenv("PORT")));
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
    FlickrGeocodeServlet servlet;
    if(args.length == 0) {
      servlet = new FlickrGeocodeServlet();
    } else {
      servlet = new FlickrGeocodeServlet(args);
    }
    context.addServlet(new ServletHolder(servlet),"/*");
    server.start();
    server.join();   
  }
}
