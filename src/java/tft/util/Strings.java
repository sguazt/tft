/* vim: set tabstop=4 expandtab shiftwidth=4 softtabstop=4: */

/**
 * @file tft/util/Strings.java
 *
 * @brief String utilities.
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


import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
//import org.apache.commons.lang3.StringUtils;


public class Strings
{
    public static <T> String stringify(T t)
    {
        return stringify(t, false);
    }

    public static <T> String stringify(T t, boolean transientFields, boolean staticFields)
    {
        return stringify(t, transientFields, staticFields, false);
    }

    public static <T> String stringify(T t, boolean multiline)
    {
        return  multiline
                ? new ReflectionToStringBuilder(t, new MultilineRecursiveToStringStyle()).toString()
                : new ReflectionToStringBuilder(t, new RecursiveToStringStyle()).toString();
    }

    public static <T> String stringify(T t, boolean transientFields, boolean staticFields, boolean multiline)
    {
        return  multiline
                ? new ReflectionToStringBuilder(t, new MultilineRecursiveToStringStyle(), null, null, transientFields, staticFields).toString()
                : new ReflectionToStringBuilder(t, new RecursiveToStringStyle(), null, null, transientFields, staticFields).toString();
    }

    public static byte[] hexStringToByteArray(String s)
    {
        int len = s.length();
        byte[] data = new byte[len / 2]; 
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static <T> String toHexString(T value)
    {
        return String.format("0x%08X", value);
    }
}
