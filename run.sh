#!/bin/sh

PORT=5000
export PORT

JAVA=`which java`

${JAVA} -Xmx384m -cp 'target/classes:target/dependency/*' com.hackdiary.geo.FlickrGeocodeServlet $*
