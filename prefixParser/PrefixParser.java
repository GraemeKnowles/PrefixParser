package prefixParser;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import prefixParser.Parser.Grapher;

public class PrefixParser {
	static final String helpShort = "-h";
	static final String helpLong = "--help";
	static final String verboseShort = "-v";
	static final String verboseLong = "--verbose";
	static final String wolframLong = "--wolfram";
	static final String desmosLong = "--desmos";
	static final String geogebraLong = "--geogebra";
	static final String examplesShort = "-e";
	static final String examplesLong = "--examples";
	static final String jarName = "PrefixParser.jar";
	static final String prefixParser = "java -jar " + jarName;

	static class Arguments {
		public Grapher grapher;
		boolean verbose;
		String equation;
		boolean exitProgram;
		boolean runExample;
	}

	public static void main(String[] args) {
		Arguments options = new Arguments();
		getOptions(args, options);

		if (options.runExample) {
			runExamples(options);
			return;
		}

		if (options.exitProgram) {
			return;
		}

		Parser p = new Parser(options.verbose, options.grapher);
		p.parse(options.equation);
		System.out.println(p.toInfix());
	}

	private static void getOptions(String[] arguments, Arguments options) {
		options.equation = "";
		options.exitProgram = false;
		options.verbose = false;
		options.grapher = Grapher.WOLFRAM;
		options.runExample = false;

		List<String> argList = Arrays.asList(arguments);
		Iterator<String> argIterator = argList.iterator();

		// If no arguments, print help
		if (arguments.length == 0) {
			printHelp();
			options.exitProgram = true;
			return;
		}

		while (argIterator.hasNext()) {
			String arg = argIterator.next();
			if (arg.compareTo(helpShort) == 0 || arg.compareTo(helpLong) == 0) {
				printHelp();
				options.exitProgram = true;
				break;
			} else if (arg.compareTo(verboseShort) == 0 || arg.compareTo(verboseLong) == 0) {
				options.verbose = true;
			} else if (arg.compareTo(wolframLong) == 0) {
				options.grapher = Grapher.WOLFRAM;
			} else if (arg.compareTo(desmosLong) == 0) {
				options.grapher = Grapher.DESMOS;
			} else if (arg.compareTo(geogebraLong) == 0) {
				options.grapher = Grapher.GEOGEBRA;
			} else if (arg.compareTo(examplesShort) == 0 || arg.compareTo(examplesLong) == 0) {
				options.runExample = true;
			} else {
				options.equation += arg + " ";
				while (argIterator.hasNext()) {
					options.equation += argIterator.next() + " ";
				}
				
				// This is a roundabout way to check if the equation being parsed in may have been 
				// corrupted from the fact that * is a wildcard on many systems. When it does occur,
				// * is replaced by a list of every file/folder in the system. This checks if the first
				// file in the system is found anywhere in the input equation.
				
				File folder = new File(Paths.get("").toAbsolutePath().toString());
				File[] listOfFiles = folder.listFiles();
				if(listOfFiles.length > 0) {
					if(options.equation.contains(listOfFiles[0].getName())){
						System.out.println("Equation parse error, put quotes around the equation and try again");
						options.exitProgram = true;
					}
				}
				
				break;
			}
		}
	}

	static void printHelp() {
		String[] helpText = {
				"Welcome to the fungp prefix parser. This program converts the prefix output of fungp and allows for output in infix.",
				"Infix is needed for many graphing calculators, many of which also have different support for various function names.",
				"Included in this program are options to tailor the output to work with various graphing calculators.",
				"\nUsage Syntax: ", prefixParser + " " + helpShort + " | " + helpLong,
				" " + helpShort + " " + helpLong + "       : Display help text that you're currently reading.",
				prefixParser + " " + "[" + wolframLong + " | " + desmosLong + " | " + geogebraLong + "] ["
						+ verboseShort + " | " + verboseLong + "] \"equation\"",
				" " + wolframLong + "       : Configure for WolframAlpha, this is the default if not specified.",
				" " + desmosLong + "        : Configure for Desmos",
				" " + geogebraLong + "      : Configure for GeoGebra",
				" " + verboseShort + " " + verboseLong + "    : Output descriptive error information",
				prefixParser + " " + examplesShort + " | " + examplesLong + " [" + wolframLong + " | " + desmosLong
						+ " | " + geogebraLong + "] [" + verboseShort + " | " + verboseLong + "]",
				" Run example equations with options" };

		for (String line : helpText) {
			System.out.println(line);
		}
	}

	static private void runExamples(Arguments options) {
		Parser p = new Parser(options.verbose, options.grapher);

		String[] tests = { "(+ (* (* c c) c) (+ (* c c) (+ (+ (+ c (- a (fungp.util/sdiv b 0.0))) c) c))))",
				"(+ (* (* c c) c) (+ (dec c) (+ (+ (+ c (- a (- b 0.0))) c) c))))",
				"(mod (inc (dec (abs (gcd a b)))) a)", "(+ stuff b)",
				"(fungp.util/sdiv (Math/sin (fungp.util/sdiv (Math/sin 0.9) 0.0)) (inc (Math/sin (Math/sin x))))",
				"(fungp.util/sdiv (* (fungp.util/abs 8.0) x) (* (inc (+ (+ (fungp.util/sdiv 7.0 1.0) (+ 7.0 y))(* (dec x) (dec y))))(fungp.util/abs (fungp.util/abs (- (- y y) (fungp.util/abs y))))))",
				"(   +    (  * ( * c    c) c) (+ (  dec   c) (+ (+ (+ c (- a (- b   0.0))) c) c))))",
				};

		for (int i = 0; i < tests.length; ++i) {
			System.out.println("Test " + i + ": " + tests[i]);
			if(p.parse(tests[i])) {
				System.out.println("Result: " + p.toInfix() + "\n");
			}else {
				System.out.println();
			}
		}
	}
}
