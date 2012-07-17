package xTrophy.Tools;

import java.util.Comparator;

public class Sorter implements Comparator<String>{
	
		  public int compare(String o1, String o2) {
		    String[] s1  = o1.split("(§§)");
		    String[] s2  = o2.split("(§§)");
		    
		    if (Integer.parseInt(s1[1]) < Integer.parseInt(s2[1])) {
		        return -1;
		      } else if (Integer.parseInt(s1[1]) > Integer.parseInt(s2[1])) {
		        return 1;
		      } else {
		        return 0;
		      }
		    
		  }
}


