package pagegen;

import java.util.ArrayList;

public class Paper {
	public int id;
	public String title;
	public ArrayList<Author> authors;
	public PaperType type;
	
	public String uid = null; // id with session info
	public String session = null;
	public int session_id = 0;
	public String youtube_url = null;
	public String bilibili_url = null;
	public String pdf_url = null;
	public boolean share = false;
	public boolean best_paper = false;
	
	public String toString() {
		// title
		String ret = "<strong>";
		if(uid!=null) {
			ret += uid+": ";
		}
		ret += title;
		if(type==PaperType.SYSTEM&&!title.toLowerCase().contains("(system")) {
			ret += " (Systems)";
		}
		if(type==PaperType.INDUSTRY&&!title.toLowerCase().contains("(industr")) {
			ret += " (Industrial)";
		}
		if(best_paper){
			ret += "<font color=\"red\">(best paper candidate)</font>";
		}
		ret += "</strong>";
		
		// author list
		ret += "<br>";
		for(int i=0;i<authors.size();i++) {
			if(i>0) {
				ret += ", ";
			}
			ret += authors.get(i).toString();
		}
		return ret;
	}
	
	@SuppressWarnings("unused")
	public String urlInfo() {

		String ret = "";
		// print the URL for bilibili, youtube, and shared document
		ret += "<br>";
		if(false) {
			ret += "<a target=\"_blank\" href=\"";
			ret += pdf_url;
			ret += "\"><img src=\"../images/buttons/pdf.png\" style=\"max-height:25px;\" class=\"confimage\"></a>";
		}
		if(share){
			if(youtube_url!=null) {
				ret += "&nbsp;&nbsp;&nbsp;&nbsp;<a target=\"_blank\"  href=\"";
				ret += youtube_url;
				ret += "\"><img src=\"../images/buttons/youtube.svg\" style=\"max-height:20px;\" class=\"confimage\"></a>";
			}
			if(bilibili_url!=null) {
				ret += "&nbsp;&nbsp;&nbsp;&nbsp;<a target=\"_blank\"  href=\"";
				ret += bilibili_url;
				ret += "\"><img src=\"../images/buttons/bilibili.png\" style=\"max-height:25px;\" class=\"confimage\"></a>";
			}
		}
		ret += "<br>";
		return ret;
	}
	
	//Paper ID, Paper Type, UID (sessionID+seqno), Dropbox PDF, Dropbox Video, Youtube,	Bilibili, Paper title, share, best paper candidate, Notes
	//0             , 1       , 2         , 3                    , 4          , 5            , 6      , 7       , 8           , 9    , 10
	public static Paper parse(String pstring) {
		System.out.println(pstring);
		ArrayList<String> fields = Util.tokenize(pstring,",",true,"\"");
		Paper p = null;
    	if(fields.size()==0) {
    		return p;
    	}
		p = new Paper();
		p.id = Integer.parseInt(fields.get(0));
		p.type = parseType(fields.get(1));
		p.uid = fields.get(2);
		p.pdf_url = fields.get(3);
		p.youtube_url = fields.get(5);
		if(p.youtube_url.length()==0||p.youtube_url.charAt(0)!='h') {
			p.youtube_url=null;
		}
		p.bilibili_url = fields.get(6);
		if(p.bilibili_url.length()==0||p.bilibili_url.charAt(0)!='h'){
			p.bilibili_url=null;
		}
		p.title = fields.get(7);
		p.share = (fields.get(8).equalsIgnoreCase("yes"));
		p.best_paper = (fields.get(9).equalsIgnoreCase("yes"));
		//out.print(++id<<"\t"<<fields[4]<<endl;
		fields.clear();
		return p;
	}
	
	
	public static PaperType parseType(String type) {
		if(type.toLowerCase().contains("poster")) {
			return PaperType.POSTER;
		}else if(type.toLowerCase().contains("demo")) {
			return PaperType.DEMO;
		}else if(type.toLowerCase().contains("system")) {
			return PaperType.SYSTEM;
		}else if(type.toLowerCase().contains("industr")) {
			return PaperType.INDUSTRY;
		}else if(type.toLowerCase().contains("research")) {
			return PaperType.RESEARCH;
		}else {
			System.err.println("wrong paper type "+type);
			return PaperType.RESEARCH;
		}
	}
	
}
