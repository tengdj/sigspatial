package pagegen;

public class Author {

	public int paperid;
	public String name;
	public String institute;
	public String toString() {
		return name+" ("+institute+")";
	}
}
