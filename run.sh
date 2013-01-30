#!/bin/sh

PORT=0
SHAPES=''

for param in "$@"
do

    if test $PORT = 0
    then
	PORT=$param
    else
	SHAPES="${SHAPES} ${param}"
    fi
done

export PORT

JAVA=`which java`

${JAVA} -Xmx500m -cp 'target/classes:target/dependency/*' com.hackdiary.geo.FlickrGeocodeServlet ${SHAPES}
