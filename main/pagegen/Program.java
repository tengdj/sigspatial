package pagegen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class Program {

	static boolean is_paper(String line){
		return line.length()>4&&line.charAt(0)=='-'&&line.charAt(1)==' '&&line.charAt(2)=='*'&&line.charAt(3)=='*';
	}
	
	public static void main(String[] args) throws IOException {
		// load the paper information from the csv file
		HashMap<String,Paper> papers = new HashMap<String,Paper>();
		BufferedReader br = new BufferedReader(new FileReader("paper_info.csv",StandardCharsets.UTF_8));
	    String input_line = "";
	    //skip head
	    br.readLine();
	    while ((input_line = br.readLine()) != null) {
	    	//input_line = input_line.substring(0,input_line.length()-1);
	    	ArrayList<String> fields = Util.tokenize(input_line,",",true,"\"");
	    	if(fields.size()==0) {
	    		continue;
	    	}
			Paper p = new Paper();
			p.uid = fields.get(0);
			p.youtube_url = fields.get(2);
			if(p.youtube_url.length()>0&&p.youtube_url.charAt(0)!='h'){
				p.youtube_url="";
			}
			p.bilibili_url = fields.get(3);
			if(p.bilibili_url.length()>0&&p.bilibili_url.charAt(0)!='h'){
				p.bilibili_url="";
			}
			p.pdf_url = fields.get(1);
			p.share = fields.get(5);
			p.name = fields.get(4);
			papers.put(p.name, p);
			//out.print(++id<<"\t"<<fields[4]<<endl;
			fields.clear();
	    }
	    br.close();
		
	    //update the program.md file with the paper information
	    br = new BufferedReader(new FileReader("C:\\Users\\dejun teng\\Google ÔÆ¶ËÓ²ÅÌ\\sigspatial2021\\content\\program.md",StandardCharsets.UTF_8));
		PrintStream out = new PrintStream("C:\\Users\\dejun teng\\Google ÔÆ¶ËÓ²ÅÌ\\sigspatial2021\\content\\program2.md","UTF-8");
		String authorline;
	    while ((input_line = br.readLine()) != null) {
	    	// skip other lines
	    	if(!is_paper(input_line)){
				out.println(input_line);
				continue;
			}
	    	// identify the best paper
			boolean isbest = false;
			String title = "";
			for(int i=4;i<input_line.length();i++){
				if(input_line.charAt(i)=='*'){
					break;
				}else if(input_line.charAt(i)=='<'){
					isbest = true;
					break;
				}else{
					title += input_line.charAt(i);
				}
			}
			
			// no entry in the paper list
			if(!papers.containsKey(title)) {
				out.println(input_line);
				System.err.println("error parsing title: "+input_line);
				continue;
			}
			
			if(!papers.containsKey(title)) {
				
				System.out.println("cannot find "+title);
				continue;
			}
			Paper p = papers.get(title);
			
			// print the name
			out.print("- **");
			out.print(p.uid+": ");
			out.print(title);
			if(isbest){
				out.print("<font color=\"red\">(best paper candidate)</font>");
			}
			out.println("** <br>");

			// print the author list
			authorline = br.readLine();
			out.println(authorline);

			// print the URL for bilibili, youtube, and shared document
			out.print("<br><a target=\"_blank\" href=\"");
			out.print(p.pdf_url);
			out.print("\"><img src=\"../images/buttons/pdf.png\" style=\"max-height:25px;\" class=\"confimage\"></a>");
			if(p.share.equalsIgnoreCase("yes")){
				out.print("&nbsp;&nbsp;&nbsp;&nbsp;<a target=\"_blank\"  href=\"");
				out.print(p.youtube_url);
				out.print("\"><img src=\"../images/buttons/youtube.svg\" style=\"max-height:20px;\" class=\"confimage\"></a>");
				out.print("&nbsp;&nbsp;&nbsp;&nbsp;<a target=\"_blank\"  href=\"");
				out.print(p.bilibili_url);
				out.print("\"><img src=\"../images/buttons/bilibili.png\" style=\"max-height:25px;\" class=\"confimage\"></a>");
			}
			out.println("<br>");
			out.flush();
	    }
	    out.close();
	    br.close();
	}
}
