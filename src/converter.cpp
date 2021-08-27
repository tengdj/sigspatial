#include <string>
#include <vector>
#include <sstream>
#include <unistd.h>
#include <stdlib.h>
#include <iostream>
#include <assert.h>
using namespace std;

inline void tokenize( const std::string& str, std::vector<std::string>& result,
	const std::string& delimiters = " ,;:\t",
	const bool keepBlankFields=false,
	const std::string& quote="\"\'"
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
int main(int argc, char **argv) {

	string former_paper;
	string input_line;
	vector<string> fields;

	while(getline(std::cin, input_line)){

		input_line = input_line.substr(0, input_line.size()-1);
		tokenize(input_line,fields,"\t");

		if(fields.size()!=5){
			cerr<<fields.size()<<"\t"<<input_line<<endl;
		}else{
			if(fields[0]!=former_paper){
				former_paper = fields[0];
				cout<<endl<<endl<<"**"<<fields[1]<<"** <br>"<<endl;
				cout<<fields[2]<<" "<<fields[3]<<" ("<<fields[4].c_str()<<")";
			}else{
				cout<<", "<<fields[2]<<" "<<fields[3]<<" ("<<fields[4].c_str()<<")";
			}
//			for(int i=0;i<5;i++)
//			cout<<i<<"\t"<<fields[i]<<endl;

		}
		cout.flush();
		input_line.clear();
		fields.clear();

	}
	cout<<endl;

	return 0;
}
