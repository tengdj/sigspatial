package pagegen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class Program {

	static boolean is_session(String line){
		//return line.length()>4&&line.charAt(0)=='-'&&line.charAt(1)==' '&&line.charAt(2)=='*'&&line.charAt(3)=='*';
		return line.contains("placeholder");
	}
	
	public static void main(String[] args) throws IOException {
		
		// load the authors 
		HashMap<Integer, ArrayList<Author>> authors = Author.parseAuthors("files/authors.csv");
		
		// load the paper information from the csv file
		HashMap<String, ArrayList<Paper>> sessions = new HashMap<String, ArrayList<Paper>>();
		BufferedReader br = new BufferedReader(new FileReader("files/paper_info.csv",StandardCharsets.UTF_8));
	    String input_line = "";
	    //skip head
	    br.readLine();
	    while ((input_line = br.readLine()) != null) {
	    	//input_line = input_line.substring(0,input_line.length()-1);
	    	Paper p = Paper.parse(input_line);
	    	if(p==null) {
	    		continue;
	    	}
	    	p.authors = authors.get(p.id);
	    	
	    	if(p.uid.length()>0) {
		    	ArrayList<String> sesinfo = Util.tokenize(p.uid, "-", true, "\"");
		    	p.session = sesinfo.get(0);
		    	p.session_id = Integer.parseInt(sesinfo.get(1));	    	
		    	if(!sessions.containsKey(p.session)) {
		    		ArrayList<Paper> papers = new ArrayList<Paper>();
		    		sessions.put(p.session, papers);
		    	}
		    	sessions.get(p.session).add(p);
	    	}
	    }
	    br.close();
		
	    //update the program.md file with the paper information
	    br = new BufferedReader(new FileReader("C:\\Users\\tengd\\我的云端硬盘\\sigspatial2021\\content\\program_template.html",StandardCharsets.UTF_8));
		PrintStream out = new PrintStream("C:\\Users\\tengd\\我的云端硬盘\\sigspatial2021\\content\\program3.html","UTF-8");
		//out = System.out;
	    while ((input_line = br.readLine()) != null) {
	    	// skip other lines
	    	if(!is_session(input_line)){
				out.println(input_line);
				continue;
			}
	    	ArrayList<String> sesinfo = Util.tokenize(input_line, "-", true, "\"");
	    	if(sessions.containsKey(sesinfo.get(0))) {
		    	out.println("<ul>");
		    	for(int i=1;i<=sessions.get(sesinfo.get(0)).size();i++) {
		    		out.println("<li>");
			    	for(Paper p:sessions.get(sesinfo.get(0))) {
			    		if(p.session_id == i) {
				    		// print the name
							out.print(p.toString());
							// print the URL for bilibili, youtube, and shared document
							//out.println(p.urlInfo());
							out.flush();
			    		}
			    	}
					out.println("</li>");
		    	}
		    	out.println("</ul>");
	    	}else {
	    		out.println("TBA");
	    	}
	    }
	    //out.close();
	    br.close();
	}
}
