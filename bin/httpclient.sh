#!/bin/sh

separator=";"
if [ -z `uname | grep -i cygwin` ]; then
  separator=":"
fi

bin=`dirname $0`
app=$bin/../

classpath=$CLASSPATH
classpath="$classpath$separator$app/lib/commons-codec-1.3.jar"
classpath="$classpath$separator$app/lib/commons-httpclient-3.1.jar"
classpath="$classpath$separator$app/lib/commons-logging-1.1.1.jar"
classpath="$classpath$separator$app/lib/jetty-6.1.7.jar"
classpath="$classpath$separator$app/lib/jetty-util-6.1.7.jar"
classpath="$classpath$separator$app/lib/junit.jar"
classpath="$classpath$separator$app/lib/log4j-1.2.15.jar"
classpath="$classpath$separator$app/lib/servlet-api-2.5-6.1.7.jar"
classpath="$classpath$separator$app/lib/sqlitejdbc-v043-native.jar"
classpath="$classpath$separator$app/etc"
classpath="$classpath$separator$app/bin/yaacs-0.0.3.jar"

CLASSPATH="$classpath" java com.honnix.yaacs.adapter.http.ui.ACHttpClientCli

