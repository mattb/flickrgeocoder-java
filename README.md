Flickr Shapefiles Geocoder
==========================

A Java geocoder that uses [Geotools](http://www.geotools.org/) and [JTS](http://tsusiatsoftware.net/jts/main.html) to index [Flickr Shapefiles](http://code.flickr.com/blog/2011/01/08/flickr-shapefiles-public-dataset-2-0/) to map (latitude, longitude) to [WOEID](http://en.wikipedia.org/wiki/WOEID).

ESRI Shapefiles for the Flickr dataset are included in the repository. They were converted from Flickr GeoJSON using [ogr2ogr](http://www.gdal.org/ogr2ogr.html). The original files were released under a [Creative Commons Zero Waiver](http://creativecommons.org/publicdomain/zero/1.0/), and so are these data files.

A Procfile and basic servlet (hardcoded to use the countries dataset) are included so that the geocoder can be run on Heroku.

Software License
================

> Copyright (c) 2012 Matt Biddulph
> 
> Permission is hereby granted, free of charge, to any person
> obtaining a copy of this software and associated documentation
> files (the "Software"), to deal in the Software without
> restriction, including without limitation the rights to use,
> copy, modify, merge, publish, distribute, sublicense, and/or sell
> copies of the Software, and to permit persons to whom the
> Software is furnished to do so, subject to the following
> conditions:
> 
> The above copyright notice and this permission notice shall be
> included in all copies or substantial portions of the Software.
> 
> THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
> EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
> OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
> NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
> HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
> WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
> FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
> OTHER DEALINGS IN THE SOFTWARE.
