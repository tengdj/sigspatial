#include <string>
#include <vector>
#include <sstream>
#include <unistd.h>
#include <stdlib.h>
#include <iostream>
#include <assert.h>
#include <fstream>
#include <map>

using namespace std;

inline void tokenize( const std::string& str, std::vector<std::string>& result,
	const std::string& delimiters = " ,;:\t",
	const bool keepBlankFields=true,
	const std::string& quote="\""
	){

    // clear the vector
    if (!result.empty()){
    	result.clear();
    }

    // you must be kidding
    if (delimiters.empty())
	return ;

    std::string::size_type pos = 0; // the current position (char) in the string
    char ch = 0; // buffer for the current character

    char current_quote = 0; // the char of the current open quote
    bool quoted = false; // indicator if there is an open quote
    std::string token;  // string buffer for the token
    bool token_complete = false; // indicates if the current token is
    // read to be added to the result vector
    std::string::size_type len = str.length();  // length of the input-string

    // for every char in the input-string
	while(len > pos){
		// get the character of the string and reset the delimiter buffer
		ch = str.at(pos);

		bool add_char = true;
		if ( false == quote.empty()){
			// if quote chars are provided and the char isn't protected
			if (std::string::npos != quote.find_first_of(ch)){
				if (!quoted){
					quoted = true;
					current_quote = ch;
					add_char = false;
				} else {
					if (current_quote == ch){
						quoted = false;
						current_quote = 0;
						add_char = false;
					}
				}
			}
		}

		if (!delimiters.empty()&&!quoted){
			// if ch is delemiter
			if (std::string::npos != delimiters.find_first_of(ch)){
				token_complete = true;
				// don't add the delimiter to the token
				add_char = false;
			}
		}

		// add the character to the token
		if (add_char){
			token.push_back(ch);
		}

		// add the token if it is complete
		// if ( true == token_complete && false == token.empty() )
		if (token_complete){
			if (token.empty())
			{
			if (keepBlankFields)
				result.push_back("");
			}
			else
			result.push_back( token );
			token.clear();
			token_complete = false;
		}
		++pos;
    } // while
    // add the final token
    if ( false == token.empty() ) {
    	result.push_back( token );
    } else if(keepBlankFields && std::string::npos != delimiters.find_first_of(ch) ){
    	result.push_back("");
    }
}
/*
**Signal Reconstruction Approach for Map Inference from Crowd-Sourced GPS Traces** <br>
Eric He (Carnegie Mellon University), Fan Bai (General Motors), Vijayakumar Bhagavatula (Carnegie Mellon University), Curtis Hay (General Motors)
*/

struct paper{
	string uid;
	string youtube_url;
	string bilibili_url;
	string pdf_url;
	string share;
};

bool is_paper(string line){
	return line.length()>4&&line.at(0)=='-'&&line.at(1)==' '&&line.at(2)=='*'&&line.at(3)=='*';
}

void parse_paper(string input_line){
	vector<string> fields;
	tokenize(input_line,fields,"\t");

	fields.clear();
}
int main(int argc, char **argv) {

	std::ifstream infile("papers.csv");
	string input_line;
	std::map<std::string, struct paper *> papers;
	vector<string> fields;
	getline(infile, input_line);//skip the header
	int id = 0;
	while(getline(infile, input_line)){
		tokenize(input_line,fields,",");
		struct paper *p = new struct paper();
		p->uid = fields[0];
		p->youtube_url = fields[2];
		if(p->youtube_url.size()>0&&p->youtube_url.at(0)!='h'){
			p->youtube_url="";
		}
		p->bilibili_url = fields[3];
		if(p->bilibili_url.size()>0&&p->bilibili_url.at(0)!='h'){
			p->bilibili_url="";
		}
		p->pdf_url = fields[1];
		p->share = fields[5];
		papers[fields[4]] = p;
		//cout<<++id<<"\t"<<fields[4]<<endl;
		fields.clear();
	}
	infile.close();
	char title[256];
	int num = 0;
	string authorline;
	while(getline(std::cin, input_line)){

		if(!is_paper(input_line)){
			cout<<input_line<<endl;
			continue;
		}
		bool isbest = false;
		for(int i=4;i<input_line.length();i++){

			if(input_line.at(i)=='*'){
				title[i-4] = '\0';
				break;
			}else if(input_line.at(i)=='<'){
				title[i-4] = '\0';
				isbest = true;
				break;
			}else{
				title[i-4] = input_line.at(i);
			}
		}

		if(papers.find(string(title))==papers.end()){
			cout<<input_line<<endl;
			continue;
		}
		paper *p = papers[string(title)];

		cout<<"- **";
		cout<<p->uid<<": ";
		cout<<title;
		if(isbest){
			cout<<"<font color=\"red\">(best paper candidate)</font>";
		}
		cout<<"** <br>"<<endl;


		getline(std::cin, authorline);
		cout<<authorline<<endl;

		cout<<"<br><a target=\"_blank\" href=\"";
		cout<<p->pdf_url;
		cout<<"\"><img src=\"../images/buttons/pdf.png\" style=\"max-height:25px;\" class=\"confimage\"></a>";
		if(p->share=="yes"){
			cout<<"&nbsp;&nbsp;&nbsp;&nbsp;<a target=\"_blank\"  href=\"";
			cout<<p->youtube_url;
			cout<<"\"><img src=\"../images/buttons/youtube.svg\" style=\"max-height:20px;\" class=\"confimage\"></a>";
			cout<<"&nbsp;&nbsp;&nbsp;&nbsp;<a target=\"_blank\"  href=\"";
			cout<<p->bilibili_url;
			cout<<"\"><img src=\"../images/buttons/bilibili.png\" style=\"max-height:25px;\" class=\"confimage\"></a>";
		}
		cout<<"<br>";
		cout<<endl;
		cout.flush();
		input_line.clear();
		authorline.clear();
	}

	return 0;
}
