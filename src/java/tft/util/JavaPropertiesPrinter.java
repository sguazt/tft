/* vim: set tabstop=4 expandtab shiftwidth=4 softtabstop=4: */

/**
 * @file tft/util/JavaPropertiesPrinter.java
 *
 * @brief Print the actual content of Java system properties.
 *
 * For instance, this class can be called inside a shell script to know the
 * actual content of the Java library path and to append to it a new path, as follows:
 *
 *  #!/bin/bash
 *
 *  libpaths=":./mylibs"
 *  libpaths+=$(java tft.util.JavaProperties java.library.path)
 *
 *  java -Djava.library.path="$libpaths" ..
 *
 * Without this, when one sets the Java library path the previous content is lost.
 * 
 * @author Marco Guazzone (marco.guazzone@gmail.com)
 *
 * <hr/>
 *
 * Copyright 2017 Marco Guazzone (marco.guazzone@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package tft.util;


public class JavaPropertiesPrinter
{
    public static void main(String[] args)
    {
        if (args.length > 0)
        {
            for (String arg : args)
            {
                System.out.println(System.getProperty(arg));
            }
        }
        else
        {
            System.getProperties().list(System.out);
            //System.out.println(System.getProperty("java.library.path"));
        }
    }
}
