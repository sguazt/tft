/* vim: set tabstop=4 expandtab shiftwidth=4 softtabstop=4: */

/**
 * @file tft/util/IO.java
 *
 * @brief I/O utilities.
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


public class IO
{
    public static void debug(String s)
    {
        System.err.println("[DEBUG] " + s);
    }

    public static void warn(String s)
    {
        System.err.println("[WARNING] " + s);
    }

    public static void fatalError(String s)
    {
        error(s, true);
    }

    public static void error(String s)
    {
        error(s, false);
    }

    public static void error(String s, boolean terminate)
    {
        System.err.println("[ERROR] " + s + ".");
        if (terminate)
        {
            System.exit(1);
        }
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
