package pagegen;

import java.util.ArrayList;

public class Paper {
	public int id;
	public String name;
	public ArrayList<Author> authors;
	public PaperType type;
	
	public String uid;
	public String youtube_url;
	public String bilibili_url;
	public String pdf_url;
	public String share;
	
	public String toString() {
		String ret = "";
		ret += "**"+name;
		if(type==PaperType.SYSTEM&&!name.toLowerCase().contains("(system")) {
			ret += " (Systems)";
		}
		if(type==PaperType.INDUSTRY&&!name.toLowerCase().contains("(industr")) {
			ret += " (Industrial)";
		}
		ret += "**<br>\n";
		for(int i=0;i<authors.size();i++) {
			if(i>0) {
				ret += ", ";
			}
			ret += authors.get(i).toString();
		}
		return ret;
	}
}
