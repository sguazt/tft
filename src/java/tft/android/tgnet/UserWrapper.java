/* vim: set tabstop=4 expandtab shiftwidth=4 softtabstop=4: */

/**
 * @file tft/tgnet/UserWrapper.java
 *
 * @brief Wrapper class for Telegram users.
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

package tft.android.tgnet;


import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.telegram.tgnet.TLRPC.User;


public class UserWrapper
{
	public User user_;


	public UserWrapper(User user)
	{
		user_ = user;
	}

	public String toString()
	{
        // Automatic toString method by means of reflection
        return new ReflectionToStringBuilder(this, new RecursiveToStringStyle()).toString();

/*
		StringBuilder sb = new StringBuilder();

		sb.append("id: " + user_.id);
		sb.append(", first_name: '" + user_.first_name + "'");
        sb.append(", last_name: '" + user_.last_name + "'");
        sb.append(", username: '" + user_.username + "'");
        sb.append(", phone: '" + user_.phone + "'");
        sb.append(", photo: " + Util.toPrettyString(user_.photo));
        sb.append(", status: " + Util.toPrettyString(user_.status));
        sb.append(", flags: " + user_.flags);
        sb.append(", self: " + user_.self);
        sb.append(", contact: " + user_.contact);
        sb.append(", mutual_contact: " + user_.mutual_contact);
        sb.append(", deleted: " + user_.deleted);
        sb.append(", bot: " + user_.bot);
        sb.append(", bot_chat_history: " + user_.bot_chat_history);
        sb.append(", bot_nochats: " + user_.bot_nochats);
        sb.append(", verified: " + user_.verified);
        sb.append(", restricted: " + user_.restricted);
        sb.append(", min: " + user_.min);
        sb.append(", bot_inline_geo: " + user_.bot_inline_geo);
        sb.append(", access_hash: 0x" + Long.toHexString(user_.access_hash));
        sb.append(", bot_info_version: " + user_.bot_inline_placeholder);
        sb.append(", restriction_reason: " + stringify(user_.bot_inline_placeholder));
        //sb.append(", bot_inline_placeholder: '" + user_.bot_inline_placeholder + "'");
        sb.append(", bot_inline_placeholder: " + stringify(user_.bot_inline_placeholder));

		//TODO

		return sb.toString();
*/
	}
}
