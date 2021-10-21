package pagegen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class Author {

	public int paperid;
	public String name;
	public String institute;
	public String toString() {
		return name+" ("+institute+")";
	}
	
	public static HashMap<Integer, ArrayList<Author>> parseAuthors(String author_file) throws IOException {
		// load the authors
		HashMap<Integer, ArrayList<Author>> authors = new HashMap<Integer, ArrayList<Author>>();
		BufferedReader br = new BufferedReader(new FileReader(author_file,StandardCharsets.UTF_8));
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
	    return authors;
	}
}
