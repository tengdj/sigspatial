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
	
	// read files into lists

	HashMap<PaperType, ArrayList<Paper>> sessions = new HashMap<PaperType, ArrayList<Paper>>();
    sessions.put(PaperType.DEMO, new ArrayList<Paper>());
    sessions.put(PaperType.POSTER, new ArrayList<Paper>());
    sessions.put(PaperType.SYSTEM, new ArrayList<Paper>());
    sessions.put(PaperType.INDUSTRY, new ArrayList<Paper>());
    sessions.put(PaperType.RESEARCH, new ArrayList<Paper>());
    
	br = new BufferedReader(new FileReader("C:\\Users\\tengd\\我的云端硬盘\\sigspatial2021\\paperinfo\\papers.csv",StandardCharsets.UTF_8));
    //skip head
    br.readLine();
    while ((input_line = br.readLine()) != null) {
    	//input_line = input_line.substring(0,input_line.length()-1);
    	Paper p = Paper.parse(input_line);
    	if(p==null) {
    		continue;
    	}
    	p.uid = null;
    	p.authors = authors.get(p.id);
    	sessions.get(p.type).add(p);
    }
    br.close();
    
    
	br = new BufferedReader(new FileReader("files/papers.csv",StandardCharsets.UTF_8));

	PrintStream out = new PrintStream("C:\\Users\\tengd\\我的云端硬盘\\sigspatial2021\\content\\accepted-papers.md","UTF-8");	
	//out = System.out;
	out.println("Title: Accepted Papers\nCategory: Program\n");
	
	out.println("### Full Research Papers");
	for(Paper p:sessions.get(PaperType.RESEARCH)) {
		out.println(p.toString()+"\n");
	}
	out.println("### Systems and Industrial Experience Papers");
	for(Paper p:sessions.get(PaperType.SYSTEM)) {
		out.println(p.toString()+"\n");
	}
	for(Paper p:sessions.get(PaperType.INDUSTRY)) {
		out.println(p.toString()+"\n");
	}
	out.println("### Poster Papers");
	for(Paper p:sessions.get(PaperType.POSTER)) {
		out.println(p.toString()+"\n");
	}
	out.println("### Demonstration Papers");
	for(Paper p:sessions.get(PaperType.DEMO)) {
		out.println(p.toString()+"\n");
	}
	out.close();
}

}
