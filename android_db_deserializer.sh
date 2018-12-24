#!/bin/bash

# WARNING: there is a bug in this script that prevent you to use database file name with spaces.
#          If you need to do this, don't use this script and use the java command directly.

classpath="./src/java"
#classpath+=":thirdparty/commons-cli-1.3.1.jar"
#classpath+=":thirdparty/commons-lang3-3.5.jar"
#classpath+=":thirdparty/sqlite-jdbc-3.16.1.jar"
for jar in $(ls thirdparty/*.jar) ; do
	classpath+=":$jar"
done

libpaths="./lib"
libpaths+=":$LD_LIBRARY_PATH"
libpaths+=":$(java -cp $classpath tft.util.JavaPropertiesPrinter java.library.path)"

#echo "java -Djava.library.path=\"$libpaths\" -cp $classpath tft.android.DatabaseDeserializer $*"
java -Djava.library.path="$libpaths" -cp $classpath tft.android.DatabaseDeserializer $*

