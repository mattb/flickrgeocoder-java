package com.hackdiary.geo;

import com.google.common.cache.*;
import java.util.*;
import java.io.*;
import java.net.URL;

class LatLng {
  public LatLng(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
  }
  public double lat;
  public double lng;
  public int hashCode() {
    int hash = new Double(lat).hashCode() + 3 * new Double(lng).hashCode();
    return hash;
  }
  public boolean equals(Object obj) {
    if (obj instanceof LatLng) {
      LatLng o = (LatLng)obj;
      return (Double.compare(this.lat, o.lat) == 0 &&
              Double.compare(this.lng, o.lng) == 0);
    }
    return false;
  }
  public String toString() {
    return String.format("%f, %f", lat, lng);
  }
}

public class CachingFlickrGeocode extends FlickrGeocode {
  LoadingCache<LatLng, List<Map<String, Object>>> cache;
  public CachingFlickrGeocode(URL url) throws IOException {
    this(url, "maximumSize=10000");
  }
  public CachingFlickrGeocode(URL url, String cacheSpec) throws IOException {
    super(url);
    cache = CacheBuilder.newBuilder()
       .from(cacheSpec)
       .build(
           new CacheLoader<LatLng, List<Map<String, Object>>>() {
             public List<Map<String, Object>> load(LatLng key) {
               return CachingFlickrGeocode.super.geocode(key.lat, key.lng);
             }
           }
       );
  }
  public List<Map<String, Object>> geocode(double lat, double lng) {
    return cache.getUnchecked(new LatLng(lat, lng));
  }
}
