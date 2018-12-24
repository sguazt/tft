/* vim: set tabstop=4 expandtab shiftwidth=4 softtabstop=4: */

/**
 * @file tft/android/tgnet/Util.java
 *
 * @brief Utility functions for handling with Telegram TGNET types.
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

package tft.android.tget;

class Util
{
	public static <T> String toPrettyString(T t)
	{
		StringBuilder sb = new StringBuilder();

		sb.append("<");
		if (s != null)
		{
			sb.append(s);
		}
		sb.append(">");

		return sb.toString();
	}

	public static String toPrettyString(String s)
	{
		StringBuilder sb = new StringBuilder();

		sb.append("'");
		if (s != null)
		{
			sb.append(s);
		}
		sb.append("'");

		return sb.toString();
	}

	public static String toPrettyString(Boolean b)
	{
		return b.toString();
	}

	public static String toPrettyString(Character s)
	{
		return Character.toString(s);
	}

	public static String toPrettyString(Number n)
	{
		return n.toString();
	}

	public static String toPrettyString(Void v)
	{
		return v.toString();
	}
}
