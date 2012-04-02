package antlrgraph;

import parsers.*;
import visuals.*;

// antlr dependencies
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.antlr.stringtemplate.*;

// other dependencies
import java.io.File;
import java.io.FileReader;

public class Main {

    static String inputFile = "";
    static String outputFile = "";
    static boolean help = false;
    static boolean infile = false, outfile = false;
    
    public static void main(String[] args) throws Exception {

        for(int i=0; i<args.length; i++) {
            if(args[i].equals("-i"))
            {
                inputFile = args[i + 1];
                infile = true;
                args[i+1] = "";
            }
            else if(args[i].equals("-o"))
            {
                outputFile = args[i + 1];
                outfile = true;
                args[i+1] = "";
            }
            else if(args[i].equals("--help")) {
                help = true;
                System.out.println("ANTLR AST Graph Visualizer");
                System.out.println("Rules: ");
                System.out.println("\t-\t The main ANTLR rule has to be called 'mainrule' in order for the visualizer to work;");
                System.out.println("\t-\t ANTLR parsers and lexers need to be called specParser and specLexer, respectively;");
                System.out.println("\t-\t -i: input grammar;");
                System.out.println("\t-\t -o: output file;");
                System.out.println("\t-\t Both input and output files need to be specified;");
            }
        }
        if(!help && (infile && outfile)) {
            
            ANTLRReaderStream in = new ANTLRReaderStream(new FileReader(inputFile));
            specLexer lexer = new specLexer(in);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            specParser parser = new specParser(tokens);
            specParser.specobjects_return result = parser.specobjects();

            Tree specParseTree = (Tree)result.getTree();

            DOTTreeGenerator gen = new DOTTreeGenerator();
            StringTemplate st = gen.toDOT(specParseTree);

            String str2 = st.toString();;
            str2 = str2.replaceAll("bgcolor=\"lightgrey\"", "bgcolor=\"white\"");
            str2 = str2.replaceAll("fontcolor=\"blue\"", "fontcolor=\"black\"");
            str2 = str2.replaceAll("fillcolor=\"white\"", "fillcolor=\"white\"");
            str2 = str2.replaceAll("fontname=\"Helvetica-\"", "fontname=\"arial\"");
            str2 = str2.replaceAll("shape=box", "shape=doublecircle");
            str2 = str2.replaceAll("bold", "");
            GraphViz gv = new GraphViz();
            gv.add(str2);
            gv.addln(gv.end_graph());
            File out = new File(outputFile);
            gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), "png" ), out );
        }
    }

}
