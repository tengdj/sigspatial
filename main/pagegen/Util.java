package pagegen;

import java.util.ArrayList;

public class Util {
	public static ArrayList<String> tokenize(String str, String delimiters, boolean keepBlankFields,String quote){

		ArrayList<String> result = new ArrayList<String>();
	    // you must be kidding
	    if (delimiters.isEmpty())
		return result;

	    char ch = '\0';
	    int pos = 0; // the current position (char) in the string	    
	    char current_quote = 0; // the char of the current open quote
	    boolean quoted = false; // indicator if there is an open quote
	    String token = "";  // string buffer for the token
	    boolean token_complete = false; // indicates if the current token is
	    // read to be added to the result vector
	    int len = str.length();  // length of the input-string
	    // for every char in the input-string
		while(len > pos){
			// get the character of the string and reset the delimiter buffer
			ch = str.charAt(pos);
			boolean add_char = true;
			if (quote.contains(ch+"")){
				// if quote chars are provided and the char isn't protected
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

			if (!delimiters.isEmpty()&&!quoted){
				// if ch is delemiter
				if (delimiters.contains(ch+"")){
					token_complete = true;
					// don't add the delimiter to the token
					add_char = false;
				}
			}

			// add the character to the token
			if (add_char){
				token = token+ch;
			}

			// add the token if it is complete
			// if ( true == token_complete && false == token.empty() )
			if (token_complete){
				if (token.isEmpty()){
					if (keepBlankFields)
						result.add("");
				}else {
					result.add(token);
				}
				token = "";
				token_complete = false;
			}
			++pos;
	    } // while
	    // add the final token
	    if (!token.isEmpty() ) {
	    	result.add(token);
	    } else if(keepBlankFields && delimiters.contains(ch+"") ){
	    	result.add("");
	    }
	    return result;
	}
}
