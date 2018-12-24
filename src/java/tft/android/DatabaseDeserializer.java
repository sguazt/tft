/* vim: set tabstop=4 expandtab shiftwidth=4 softtabstop=4: */

/**
 * @file tft/android/DatabaseSerializer.java
 *
 * @brief Application entry-point for the deserialization of Telegram data types
 * from DB stored on Android devices.
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

package tft.android;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.sqlite.SQLiteConfig;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLRPC;
import tft.util.IO;
import tft.util.Strings;


public class DatabaseDeserializer
{
//    static
//    {
//        System.loadLibrary("tgnet");
//    }

    enum DatabaseEntity
    {
        CHAT (0, "chat"),
        ENCRYPTED_CHAT (1, "encrypted_chat"),
        MESSAGE (2, "message"),
        USER (3, "user");

        private final int tag_;
        private final String label_;

        DatabaseEntity(int tag, String label)
        {
            tag_ = tag;
            label_ = label;
        }

        private int tag() { return tag_; }
        private String label() { return label_; }

        public String toString()
        {
            return label_;
        }

        public static DatabaseEntity parse(String s)
        {
            if (s.equalsIgnoreCase("chat"))
            {
                return CHAT;
            }
            if (s.equalsIgnoreCase("encrypted_chat"))
            {
                return ENCRYPTED_CHAT;
            }
            if (s.equalsIgnoreCase("message"))
            {
                return MESSAGE;
            }
            if (s.equalsIgnoreCase("user"))
            {
                return USER;
            }
            return null;
        }
    }


    public static final long ALL_ID = Long.MIN_VALUE;
    //private static final String OUTPUT_SEPARATOR = "-";
    //private static final int NUM_OUTPUT_SEPARATOR = 40;


    String dbPath_ = "./cache4.db";
    DatabaseEntity dbEntity_ = DatabaseEntity.USER;
    long id_ = ALL_ID;
    boolean debug_ = false;
    boolean multiline_ = false;
    boolean permissive_ = false;
 

    public void parseCommandLine(String[] args)
    {
        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        options.addOption(Option.builder("h")
                            .longOpt("help")
                            .desc("Print this message")
                            .build());
        options.addOption(Option.builder("d")
                            .longOpt("db")
                            .desc("Path to the Telegram database")
                            .hasArg()
                            .argName("filename")
                            //.required()
                            .build());
        options.addOption(Option.builder()
                            .longOpt("debug")
                            .desc("Print debugging messages")
                            .build());
        options.addOption(Option.builder("e")
                            .longOpt("entity")
                            .desc("Entity to be deserialized. Possible values are: " + StringUtils.join(DatabaseEntity.values(), ", "))
                            .hasArg()
                            .argName("label")
                            //.required()
                            .build());
        options.addOption(Option.builder()
                            .longOpt("id")
                            .desc("Integer number representing an entity identifier in the Telegram datatabase (e.g., for the 'user' entity, it represents the 'uid' field of 'users' table). Use the value " + ALL_ID + " to select all records.")
                            .type(Long.class)
                            .hasArg()
                            .argName("number")
                            .build());
        options.addOption(Option.builder()
                            .longOpt("multiline")
                            .desc("Prints fields of a deserialized entity on multiple lines.")
                            .build());
        options.addOption(Option.builder()
                            .longOpt("permissive")
                            .desc("Don't fail if some entity of field cannot be deserialized.")
                            .build());

        try
        {
            // Parse the command line arguments
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("db"))
            {
                dbPath_ = line.getOptionValue("db");
            }
            if (line.hasOption("debug"))
            {
                debug_ = true;
            }
            if (line.hasOption("entity"))
            {
                dbEntity_ = DatabaseEntity.parse(line.getOptionValue("entity"));
                if (dbEntity_ == null)
                {
                    IO.error("Unknow database entity '" + line.getOptionValue("entity") + "'");
                    usage(options);
                    System.exit(1);
                }
            }
            if (line.hasOption("help"))
            {
                //HelpFormatter formatter = new HelpFormatter();
                ////formatter.printHelp(this.getClass().getName(), options, true);
                //formatter.printHelp(this.getClass().getName() + " [options]", "Options:\n", options, "");
                usage(options);
                System.exit(0);
            }
            if (line.hasOption("id"))
            {
                id_ = Long.parseLong(line.getOptionValue("id"));
            }
            if (line.hasOption("multiline"))
            {
                multiline_ = true;
            }
            if (line.hasOption("permissive"))
            {
                permissive_ = true;
            }
        }
        catch (ParseException pe)
        {
            IO.error("Parse error: " + pe.getMessage());
            usage(options);
            System.exit(1);
        }

        if (debug_)
        {
            IO.debug("Parameters: ");
            IO.debug("- Database: " + dbPath_);
            IO.debug("- Debug: " + debug_);
            IO.debug("- Entity: " + dbEntity_);
            IO.debug("- Entity ID: " + id_ + (id_ == ALL_ID ? " (all entity's records)" : ""));
            IO.debug("- Multiline: " + multiline_);
            IO.debug("- Permissive: " + permissive_);
        }
    }

    public void deserialize()
    {
        checkDatabase();

        final String dburi = "jdbc:sqlite:" + dbPath_;
        SQLiteConfig config = new SQLiteConfig();
        config.setReadOnly(true);

        try (Connection conn = DriverManager.getConnection(dburi, config.toProperties()))
        {
            switch (dbEntity_)
            {
                case CHAT:
                    deserializeChat(conn);
                    break;
                case ENCRYPTED_CHAT:
                    deserializeEncryptedChat(conn);
                    break;
                case MESSAGE:
                    deserializeMessage(conn);
                    break;
                case USER:
                    deserializeUser(conn);
                    break;
                default:
                    IO.error("Unknown entity: " + dbEntity_);
                    break;
            }
        }
        catch (SQLException se)
        {
            IO.fatalError("SQL Error: " + se.getMessage());
        }
    }

    private void deserializeUser(Connection conn) throws SQLException
    {
        String query = "SELECT uid, data FROM users";
        if (id_ != ALL_ID)
        {
            query += " WHERE uid=?";
        }
        //query += ";";

        //System.err.println("QUERY: " + query);

        try (PreparedStatement stmt = conn.prepareStatement(query))
        {
            if (id_ != ALL_ID)
            {
                stmt.setLong(1, id_);
            }
            //stmt.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                // read the result set

                if (debug_)
                {
                    IO.debug("uid = " + rs.getInt("uid"));
                }

                //rs.getBlob("data") // Actually, not supported by SQLITE JDBC

                //rs.getBlob("data").
                NativeByteBuffer data = NativeByteBuffer.wrap(rs.getBytes("data"));
                if (data != null)
                {
                    //UserWrapper user = new UserWrapper(TLRPC.User.TLdeserialize(data, data.readInt32(!permissive_), !permissive_));
                    int tag = data.readInt32(!permissive_);
                    TLRPC.User user = TLRPC.User.TLdeserialize(data, tag, !permissive_);

                    System.out.println("-- User (tag: " + Strings.toHexString(tag) + "): " + Strings.stringify(user, multiline_));
                    //System.out.println(StringUtils.repeat(OUTPUT_SEPARATOR, NUM_OUTPUT_SEPARATOR));

                    data.reuse();
                }
            }
        }
    }

    private void deserializeMessage(Connection conn) throws SQLException
    {
        String query = "SELECT mid, data FROM messages";
        if (id_ != ALL_ID)
        {
            query += " WHERE mid=?";
        }
        //query += ";";

        if (debug_)
        {
            IO.debug("QUERY: " + query);
        }

        try (PreparedStatement stmt = conn.prepareStatement(query))
        {
            if (id_ != ALL_ID)
            {
                stmt.setLong(1, id_);
            }
            //stmt.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                // read the result set

                if (debug_)
                {
                    IO.debug("mid = " + rs.getInt("mid"));
                }

                //rs.getBlob("data") // Actually, not supported by SQLITE JDBC
                NativeByteBuffer data = NativeByteBuffer.wrap(rs.getBytes("data"));
                if (data != null)
                {
                    int tag = data.readInt32(!permissive_);
                    TLRPC.Message msg = TLRPC.Message.TLdeserialize(data, tag, !permissive_);

                    System.out.println("-- Message (tag: " + Strings.toHexString(tag) + "): " + Strings.stringify(msg, multiline_));
                    //System.out.println(StringUtils.repeat(OUTPUT_SEPARATOR, NUM_OUTPUT_SEPARATOR));

                    data.reuse();
                }
            }
        }
    }

    private void deserializeEncryptedChat(Connection conn) throws SQLException
    {
        String query = "SELECT uid, data, g, authkey, fauthkey, khash FROM enc_chats";
        if (id_ != ALL_ID)
        {
            query += " WHERE uid=?";
        }
        //query += ";";

        if (debug_)
        {
            IO.debug("QUERY: " + query);
        }

        try (PreparedStatement stmt = conn.prepareStatement(query))
        {
            if (id_ != ALL_ID)
            {
                stmt.setLong(1, id_);
            }
            //stmt.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                // read the result set

                if (debug_)
                {
                    IO.debug("uid = " + rs.getInt("uid"));
                }

                //rs.getBlob("data") // Actually, not supported by SQLITE JDBC
                NativeByteBuffer data = NativeByteBuffer.wrap(rs.getBytes("data"));
                if (data != null)
                {
                    int tag = data.readInt32(!permissive_);
                    TLRPC.EncryptedChat encChat = TLRPC.EncryptedChat.TLdeserialize(data, tag, !permissive_);

                    data.reuse();

                    System.out.println("-- Encrypted Chat (tag: " + Strings.toHexString(tag) + "): [");
                    System.out.println("- data: " + Strings.stringify(encChat, multiline_));
                    //System.out.println("g=" + Strings.stringify(rs.getBytes("g"), multiline_)); // -> throws a StackOverflow exception
                    //System.out.println("g=" + new ReflectionToStringBuilder(rs.getBytes("g"), new StandardToStringStyle())); // -> does not iterate through array elements
                    //data = NativeByteBuffer.wrap(rs.getBytes("g"));
                    //System.out.println("  g=" + Strings.stringify(data, multiline_)); // Too verbose
                    //data.reuse();
                    System.out.println("- g: " + Arrays.toString(rs.getBytes("g")));
                    //data = NativeByteBuffer.wrap(rs.getBytes("authkey"));
                    //System.out.println("  authkey=" + Strings.stringify(data, multiline_)); // Too verbose
                    //data.reuse();
                    System.out.println("- authkey: " + Arrays.toString(rs.getBytes("authkey")));
                    //data = NativeByteBuffer.wrap(rs.getBytes("fauthkey"));
                    //System.out.println("  fauthkey=" + Strings.stringify(data, multiline_)); // Too verbose
                    //data.reuse();
                    System.out.println("- fauthkey: " + Arrays.toString(rs.getBytes("fauthkey")));
                    //data = NativeByteBuffer.wrap(rs.getBytes("khash"));
                    //System.out.println("  khash=" + Strings.stringify(data, multiline_)); // Too verbose
                    //data.reuse();
                    System.out.println("- khash: " + Arrays.toString(rs.getBytes("khash")));
                    System.out.println("]");
                    //System.out.println(StringUtils.repeat(OUTPUT_SEPARATOR, NUM_OUTPUT_SEPARATOR));
                }
            }
        }
    }

    private void deserializeChat(Connection conn) throws SQLException
    {
        String query = "SELECT uid, name, data FROM chats";
        if (id_ != ALL_ID)
        {
            query += " WHERE uid=?";
        }
        //query += ";";

        if (debug_)
        {
            IO.debug("QUERY: " + query);
        }

        try (PreparedStatement stmt = conn.prepareStatement(query))
        {
            if (id_ != ALL_ID)
            {
                stmt.setLong(1, id_);
            }
            //stmt.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                // read the result set

                if (debug_)
                {
                    IO.debug("uid = " + rs.getInt("uid"));
                }

                //rs.getBlob("data") // Actually, not supported by SQLITE JDBC
                NativeByteBuffer data = NativeByteBuffer.wrap(rs.getBytes("data"));
                if (data != null)
                {
                    int tag = data.readInt32(!permissive_);
                    TLRPC.Chat chat = TLRPC.Chat.TLdeserialize(data, tag, !permissive_);

                    data.reuse();

                    System.out.println("-- Chat (tag: " + Strings.toHexString(tag) + "): [");
                    System.out.println("- data: " + Strings.stringify(chat, multiline_));
                    //System.out.println("- name: " + rs.getString("name"));
                    System.out.println("]");
                    //System.out.println(StringUtils.repeat(OUTPUT_SEPARATOR, NUM_OUTPUT_SEPARATOR));
                }
            }
        }
    }

    private void checkDatabase()
    {
        if (dbPath_ == null)
        {
            IO.fatalError("Database file not specified");
        }

        try
        {
            File dbFile = new File(dbPath_);

            if (!dbFile.exists())
            {
                IO.fatalError("Database '" + dbPath_ + "' not found");
            }
            if (!dbFile.isFile())
            {
                IO.fatalError("Database '" + dbPath_ + "' is not a file");
            }
        }
        catch (Exception e)
        {
            IO.fatalError("Problems while checking database: '" + dbPath_ + "': " + e.getMessage());
        }
    }

    private void usage(Options options)
    {
        HelpFormatter formatter = new HelpFormatter();
        //formatter.printHelp(this.getClass().getName(), options, true);
        formatter.printHelp(this.getClass().getName() + " [options]", "Options:\n", options, "");
    }


    public static void main(String[] args)
    {
        DatabaseDeserializer des = new DatabaseDeserializer();

        des.parseCommandLine(args);

        des.deserialize();
    }
}
