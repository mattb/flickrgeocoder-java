#!/bin/sh

JAVA=`which java`
PORT=5000

${JAVA} -Xmx384m -cp 'target/classes:target/dependency/*' com.hackdiary.geo.FlickrGeocodeServlet $*