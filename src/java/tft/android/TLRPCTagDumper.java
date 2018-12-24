/* vim: set tabstop=4 expandtab shiftwidth=4 softtabstop=4: */

/**
 * @file tft/android/TLRPCTagDumper.java
 *
 * @brief Dump the name-tag pair of each org.telegram.TLRPC inner class that is
 *  associated with a tag.
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

//import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
//import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
//import org.reflections.Reflections;
import tft.util.IO;


public class TLRPCTagDumper
{
    private static final char csvQuoteChar_ = '"';
    private static final char csvFieldSep_ = ',';
    private static final String tagFieldName_ = "constructor";


    private String blacklistPath_ = null;
    private boolean debug_ = false;
    private String whitelistPath_ = null;


    public void parseCommandLine(String[] args)
    {
        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        options.addOption(Option.builder("h")
                            .longOpt("help")
                            .desc("Print this message")
                            .build());
        options.addOption(Option.builder("b")
                            .longOpt("blacklist")
                            .desc("Path to the text file containing the name of the classes to exclude from the dump (one class name for line)")
                            .hasArg()
                            .argName("filename")
                            //.required()
                            .build());
        options.addOption(Option.builder()
                            .longOpt("debug")
                            .desc("Print debugging messages")
                            .build());
        options.addOption(Option.builder("w")
                            .longOpt("whitelist")
                            .desc("Path to the text file containing the name of the classes to select for the dump (one class name for line)")
                            .hasArg()
                            .argName("filename")
                            //.required()
                            .build());

        try
        {
            // Parse the command line arguments
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("blacklist"))
            {
                blacklistPath_ = line.getOptionValue("blacklist");
            }
            if (line.hasOption("debug"))
            {
                debug_ = true;
            }
            if (line.hasOption("help"))
            {
                usage(options);
                System.exit(0);
            }
            if (line.hasOption("whitelist"))
            {
                whitelistPath_ = line.getOptionValue("whitelist");
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
            IO.debug("- Blacklist File: " + blacklistPath_);
            IO.debug("- Debug: " + debug_);
            IO.debug("- Whitelist File: " + whitelistPath_);
        }
    }

    public void dump()
    {
        // Method #1: use org.reflections.Reflections
        // -> OK, but it is slow and requires further deps
        //Reflections reflections = new Reflections("org.telegram.tgnet");
        //Set<Class<? extends TLObject>> classes = reflections.getSubTypesOf(TLObject.class);
        //for (Class<? extends TLObject> clazz : classes)
        //{
        //    try
        //    {
        //        TLObject your = clazz.newInstance();
        //    }
        //    catch (Exception e)
        //    {
        //        e.printStackTrace();
        //    } 
        //}

        // Method #2: use io.github.lukehutch.fastclasspathscanner.FastClasspathScanner
        // -> OK, but with modern JVMs we could probably live without it
        ////XXX: debug
        ////List<String> classNames = new FastClasspathScanner(TLRPC.class.getPackage().getName()).scan().getNamesOfAllClasses();
        ////for (String className : classNames)
        ////{
        ////    System.out.println(className);
        ////}
        //List<Class<? extends TLObject>> subclasses = new ArrayList<>();
        //new FastClasspathScanner(TLRPC.class.getPackage().getName()).matchSubclassesOf(TLObject.class, subclasses::add).scan();
        //for (Class<? extends TLObject> clazz : subclasses)
        //{
        //    try
        //    {
        //        clazz.getField(tagFieldName_);
        //        System.out.println("Found field for class: " + clazz);
        //    }
        //    catch (Exception e)
        //    {
        //        //e.printStackTrace();
        //    } 
        //}

        // Method #3: use built-in reflection facilities
        Class<?>[] classes = TLRPC.class.getClasses();

        Set<String> whitelist = null;
        Set<String> blacklist = null;

        if (whitelistPath_ != null)
        {
            try
            {
                whitelist = new HashSet<>(Files.readAllLines(Paths.get(whitelistPath_)));
                if (debug_)
                {
                    IO.debug("Whilelist: " + Arrays.toString(whitelist.toArray()));
                }
            }
            catch (Exception e)
            {
                IO.fatalError("Unable to open whitelist file: " + e.getMessage());
            }
        }
        if (blacklistPath_ != null)
        {
            try
            {
                blacklist = new HashSet<>(Files.readAllLines(Paths.get(blacklistPath_)));
                if (debug_)
                {
                    IO.debug("Blacklist: " + Arrays.toString(blacklist.toArray()));
                }
            }
            catch (Exception e)
            {
                IO.fatalError("Unable to open blacklist file: " + e.getMessage());
            }
        }

        Arrays.sort(classes, new Comparator<Class<?>>() {
                                @Override
                                public int compare(Class<?> o1, Class<?> o2)
                                {
                                    return o1.getName().compareTo(o2.getName());
                                }
                            });
        for (Class<?> clazz : classes)
        {
            if (whitelist != null && !whitelist.contains(clazz.getName()))
            {
                if (debug_)
                {
                    IO.debug("Class '" + clazz.getName() + "' is not white-listed. Skip it.");
                }
                continue;
            }
            if (blacklist != null && blacklist.contains(clazz.getName()))
            {
                if (debug_)
                {
                    IO.debug("Class '" + clazz.getName() + "' is black-listed. Skip it.");
                }
                continue;
            }

            try
            {
                java.lang.reflect.Field field = clazz.getField(tagFieldName_);
                System.out.println(csvQuoteChar_ + clazz.getName() + csvQuoteChar_ + csvFieldSep_
                                 + csvQuoteChar_ + IO.toHexString(field.get(clazz.newInstance())) + csvQuoteChar_);
            }
            catch (NoSuchFieldException nsfe)
            {
                // Do nothing
                if (debug_)
                {
                    IO.debug("Class '" + clazz + "' does not contain the field '" + tagFieldName_ + "'. Skip it.");
                }
            }
            catch (Exception e)
            {
                IO.fatalError("Unexpected error while trying to get field '" + tagFieldName_ + "' from class '" + clazz + "': " + e.getMessage());
            } 
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
        TLRPCTagDumper dumper = new TLRPCTagDumper();

        dumper.parseCommandLine(args);

        dumper.dump();
    }
}
