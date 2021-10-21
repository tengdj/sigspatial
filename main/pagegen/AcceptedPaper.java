package pagegen;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class AcceptedPaper {

/*
**Signal Reconstruction Approach for Map Inference from Crowd-Sourced GPS Traces** <br>
Eric He (Carnegie Mellon University), Fan Bai (General Motors), Vijayakumar Bhagavatula (Carnegie Mellon University), Curtis Hay (General Motors)
*/

public static void main(String[] args) throws FileNotFoundException, IOException {

	// load the authors
	HashMap<Integer, ArrayList<Author>> authors = new HashMap<Integer, ArrayList<Author>>();
	BufferedReader br = new BufferedReader(new FileReader("files/authors.csv",StandardCharsets.UTF_8));
	String input_line = "";
    //skip head
    br.readLine();
    while ((input_line = br.readLine()) != null) {
    	//input_line = input_line.substring(0,input_line.length()-1);
    	ArrayList<String> fields = Util.tokenize(input_line,",",true,"\"");
    	if(fields.size()!=3) {
    		System.err.println(input_line);
    	}else {
    		Author a = new Author();
    		int pid = Integer.parseInt(fields.get(0));
    		a.name = fields.get(1);
    		a.institute = fields.get(2);
    		if(authors.containsKey(pid)) {
    			authors.get(pid).add(a);
    		}else {
    			ArrayList<Author> as = new ArrayList<Author>();
    			as.add(a);
    			authors.put(pid, as);
    		}
    	}
    }
    br.close();
	
	
	ArrayList<Paper> papers = new ArrayList<Paper>();
	ArrayList<Paper> demos = new ArrayList<Paper>();
	ArrayList<Paper> posters = new ArrayList<Paper>();
	ArrayList<Paper> industry = new ArrayList<Paper>();
	br = new BufferedReader(new FileReader("files/papers.csv",StandardCharsets.UTF_8));

    //skip head
    br.readLine();
    while ((input_line = br.readLine()) != null) {
    	//input_line = input_line.substring(0,input_line.length()-1);
    	ArrayList<String> fields = Util.tokenize(input_line,",",true,"\"");
    	if(fields.size()!=4) {
    		System.err.println(input_line);
    	}else {
    		Paper p = new Paper();
    		p.id = Integer.parseInt(fields.get(0));
    		p.title = fields.get(2);
    		p.authors = authors.get(p.id);
    		if(!authors.containsKey(p.id)) {
    			System.err.println("no author information for "+p.title);
    		}
    		if(fields.get(3).toLowerCase().contains("poster")) {
    			p.type = PaperType.POSTER;
    			posters.add(p);
    		}else if(fields.get(3).toLowerCase().contains("demo")) {
    			p.type = PaperType.DEMO;
    			demos.add(p);
    		}else if(fields.get(3).toLowerCase().contains("system")) {
    			p.type = PaperType.SYSTEM;
    			industry.add(p);
    		}else if(fields.get(3).toLowerCase().contains("industr")) {
    			p.type = PaperType.INDUSTRY;
    			industry.add(p);
    		}else if(fields.get(3).toLowerCase().contains("research")) {
    			p.type = PaperType.RESEARCH;
    			papers.add(p);
    		}else {
    			System.err.println("wrong paper type "+fields.get(3));
    		}
    	}
    }
    br.close();	
	
	PrintStream out = new PrintStream("C:\\Users\\dejun teng\\Google ‘∆∂À”≤≈Ã\\sigspatial2021\\content\\accepted-papers.md","UTF-8");	
	out.println("Title: Accepted Papers\nCategory: Accepted Papers\n");
	
	out.println("### Full Research Papers");
	for(Paper p:papers) {
		out.println(p.toString()+"\n");
	}
	out.println("### Systems and Industrial Experience Papers");
	for(Paper p:industry) {
		out.println(p.toString()+"\n");
	}
	out.println("### Poster Papers");
	for(Paper p:posters) {
		out.println(p.toString()+"\n");
	}
	out.println("### Demonstration Papers");
	for(Paper p:demos) {
		out.println(p.toString()+"\n");
	}
	out.close();
}

}
