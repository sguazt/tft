/* vim: set tabstop=4 expandtab shiftwidth=4 softtabstop=4: */

/**
 * @file tft/android/UserConfigSerializer.java
 *
 * @brief Application entry-point for the deserialization of Telegram user config
 * file.
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
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.telegram.messenger.UserConfig;
import tft.util.IO;
import tft.util.Strings;


public class UserConfigDeserializer
{
    String configPath_ = "./userconfing.xml";
    boolean debug_ = false;
    boolean multiline_ = false;
 

    public void parseCommandLine(String[] args)
    {
        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        options.addOption(Option.builder("h")
                            .longOpt("help")
                            .desc("Print this message")
                            .build());
        options.addOption(Option.builder("c")
                            .longOpt("config")
                            .desc("Path to the Telegram user config file. Supports either XML or DAT files (e.g., see 'userconfing.xml' and 'user.dat' files respectively).")
                            .hasArg()
                            .argName("filename")
                            //.required()
                            .build());
        options.addOption(Option.builder()
                            .longOpt("debug")
                            .desc("Print debugging messages")
                            .build());
        options.addOption(Option.builder()
                            .longOpt("multiline")
                            .desc("Prints fields of a deserialized entity on multiple lines.")
                            .build());

        try
        {
            // Parse the command line arguments
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("config"))
            {
                configPath_ = line.getOptionValue("config");
            }
            if (line.hasOption("debug"))
            {
                debug_ = true;
            }
            if (line.hasOption("help"))
            {
                //HelpFormatter formatter = new HelpFormatter();
                ////formatter.printHelp(this.getClass().getName(), options, true);
                //formatter.printHelp(this.getClass().getName() + " [options]", "Options:\n", options, "");
                usage(options);
                System.exit(0);
            }
            if (line.hasOption("multiline"))
            {
                multiline_ = true;
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
            IO.debug("- User Configuration: " + configPath_);
            IO.debug("- Debug: " + debug_);
            IO.debug("- Multiline: " + multiline_);
        }
    }

    public void deserialize()
    {
        checkFile();

        //NOTE: UserConfig is a class made of static members only
        //      So, to print the contents of this class:
        //      1. We call the loadXmlConfig method to populate static members
        //      2. We create an instance of UserConfig class
        //      3. We print this instance

        UserConfig.loadXmlConfig(new File(configPath_));

        System.out.println("-- User Config: " + Strings.stringify(new UserConfig(), false, true, multiline_));
    }

    private void checkFile()
    {
        if (configPath_ == null)
        {
            IO.fatalError("User config file not specified");
        }

        try
        {
            File dbFile = new File(configPath_);

            if (!dbFile.exists())
            {
                IO.fatalError("User config '" + configPath_ + "' not found");
            }
            if (!dbFile.isFile())
            {
                IO.fatalError("User config '" + configPath_ + "' is not a file");
            }
        }
        catch (Exception e)
        {
            IO.fatalError("Problems while checking user config: '" + configPath_ + "': " + e.getMessage());
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
        UserConfigDeserializer des = new UserConfigDeserializer();

        des.parseCommandLine(args);

        des.deserialize();
    }
}
