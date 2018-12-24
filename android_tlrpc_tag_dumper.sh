#!/bin/bash

# WARNING: there is a bug in this script that prevent you to use database file name with spaces.
#          If you need to do this, don't use this script and use the java command directly.

classpath="./src/java"
for jar in $(ls thirdparty/*.jar) ; do
	classpath+=":$jar"
done

libpaths="./lib"
libpaths+=":$LD_LIBRARY_PATH"
libpaths+=":$(java -cp $classpath tft.util.JavaPropertiesPrinter java.library.path)"

java -Djava.library.path="$libpaths" -cp $classpath tft.android.TLRPCTagDumper $*
