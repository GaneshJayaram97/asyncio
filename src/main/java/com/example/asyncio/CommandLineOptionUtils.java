package com.example.asyncio;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

/**
 * author ganesh.jayaraman
 * Mar 30, 2020
 */

public class CommandLineOptionUtils {

	private static final Logger log = LoggerFactory.getLogger(CommandLineOptionUtils.class);

	@Bean
	public static Options populateOptions() {
		Options options = new Options();
		Option numberOfDevicesOption = new Option("num", "Number of Devices to be upgraded");
		numberOfDevicesOption.setArgs(1);
		options.addOption(numberOfDevicesOption);
		return options;
	}

	public static CommandLine parseCLI(String[] args) {
		CommandLineParser parser = new BasicParser();
		Options options = populateOptions();
		CommandLine line = null;
		try {
			if (args != null) {
				line = parser.parse(options, args);
			}
	    }
	    catch( ParseException exp ) {
	        log.error("Error in parsing the command line option {}", exp);
	    }
		return line;
	}

	public static Integer getNumberOfDevices(String[] args) {
		CommandLine line = parseCLI(args);
		if (line != null && line.hasOption("num")) {
			return Integer.valueOf(line.getOptionValue("num"));
		}
		return 20;
	}
}
