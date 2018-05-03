# PrefixParser

PrefixParser is a recursive descent parser and converter that is designed to convert the prefix output of [fungp](https://github.com/vollmerm/fungp) into a more human readable infix notation that can be understood by graphing calculators.

#### Supported Calculators:
- [Wolfram Alpha](https://www.wolframalpha.com/) - Recommended.
- [Desmos](https://www.desmos.com/calculator)
- [Geogebra](https://www.geogebra.org/graphing)

#### Requirements
- [Java 7](https://java.com/en/download/) or above.

#### Use
- This program is meant to be compiled into a .jar and interacted with through the command line. 
- The .jar can be found in the [latest release](https://github.com/GraemeKnowles/PrefixParser/releases/latest).
- Included in the repository are two scripts, windowsBuild.bat and unixBuild.sh that can be run to compile the jar file from scratch. 

```
Usage Syntax:
java -jar PrefixParser.jar -h | --help
 -h --help       : Display help text that you're currently reading.
java -jar PrefixParser.jar [--wolfram | --desmos | --geogebra] [-v | --verbose] equation
 --wolfram       : Configure for WolframAlpha, this is the default if not specified.
 --desmos        : Configure for Desmos
 --geogebra      : Configure for GeoGebra
 -v --verbose    : Output descriptive error information
java -jar PrefixParser.jar -e | --examples [--wolfram | --desmos | --geogebra] [-v | --verbose]
 Run example equations with options
```

#### Limitations
Basic support for safe divide (as used in fungp) is included. The parser can handle cases like x/0 (and will replace it with x/1), however it does not evaluate or reduce the equation and cannot handle more complex cases like 1/(1-1) or 1/(x-x). In these cases it will simply output them as they are.
