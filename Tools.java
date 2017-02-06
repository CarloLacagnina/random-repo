package tools;

import java.util.Scanner;

public class Tools {
	
    /**
     * Sorting array in increasing order
     * 
     * @param array1 : input int array
     * @param array2 : input string array
     * 
     */
    public static void sortArray(int[] array1, String[] array2) {
   	    boolean swapped = true;
   	    int j = 0;
   	    int tmp1;
   	    String tmp2;
   	    while (swapped) {
   	        swapped = false;
   	        j++;
   	        for (int i = 0; i < array1.length - j; i++) {
   	            if (array1[i] > array1[i + 1]) {
   	                tmp1 = array1[i];
   	                tmp2 = array2[i];
   	                array1[i] = array1[i+1];
   	                array2[i] = array2[i+1];
   	                array1[i+1] = tmp1;
   	                array2[i+1] = tmp2;
   	                swapped = true;
   	            }
   	        }
   	    }
   	}

	 /**
	  * Throwing away parts of array not filled by information
	  * 
	  * @param index : input array
	  * 
	  * @return a copy of a part of input array
	  * 
	  */
	 
    public static int[] resizeArray(int[] inputArray){
        int fLen = 0; //length of new array containing information
        for(int i=0; i<inputArray.length; i++) {
           if(inputArray[i] == -1) {
              fLen=i;
              break;                 
           }
        }
        
        //copying part of input array
        int[] outputArray = new int[fLen];
        for(int i=0; i<outputArray.length; i++) {
           outputArray[i]=inputArray[i];
        }
        return outputArray;
     }
    
	 /**
	  * Selecting column in given 2D array
	  * 
	  * @param inputArray : input array
	  * @param colToFind : column where looking for variable
	  *
	  * @return column of given 2D array
	  * 
	  */

	 public static String[] reduce2Dto1D(String[][] inputArray, int colToFind) {
	     String[] array1D = new String[inputArray.length];
		 for(int i=0; i<array1D.length; i++) {
		    array1D[i] = inputArray[i][colToFind];
		 }
		 return array1D;
	 }
	
	 /**
	  *  Looking for string in array
	  *  
	  * @param inputArray : input array
	  * @param toFind : string to find
	  * 
	  * @return rows where searched string is
	  * 
	  */
	 public static int[] find(String[] inputArray, String toFind) {
		 int[] index = new int[inputArray.length];
		 int count = 0;
		 	 
		 for (int i = 0; i < inputArray.length; i++) {
			 index[i] = -1; //initializing array
			 if (inputArray[i].equals(toFind)) {
	             index[count] = i;
	 		     count++;
			 }
	     }
		 
        //when no string in found in array then no further processing
		 if(count == 0) {
			 int[] noInfo = {-1};
			 return noInfo;
		 }else{
			 return resizeArray(index);
		 }
	 }
	 
	   /**
	    * Partial matching of country names
	    * @param inputArray : input array with countries
	    * @param toFind : string to look for
	    * 
	    * @return : either country name or country code
	    */
	   public static String countryPartialMatch(String[] inputArray, String toFind) {
			 int[] index = new int[inputArray.length];
			 int count = 0;
			 
			//if country name is given, then looks for related country code
			 if(toFind.length() > 4) { //"US" = 4 characters
				 toFind = "\""+toFind.split("\"")[1]; //needed because of issues with quotes
				 for (int i = 0; i < inputArray.length; i++) {
					 index[i] = -1; //initializing array
					 if (inputArray[i].startsWith(toFind)) {
			             index[count] = i;
			 		     count++;
					 }
			     }
		         //when no string in found in array then no further processing
				 if(count == 0) {
					 System.out.print("Country not in dataset!");
					 System.exit(1);
				 }
				//one match found
				 if (count == 1) {
					 return inputArray[index[0]];
				 }else{
					 //more matches found
					 System.out.print("More countries starting with same name. Type country code.");
					 Scanner in = new Scanner(System.in);
					 String userIn = "\""+in.nextLine()+"\""; //needed because of issues with quotes
					 in.close();
					 return userIn;
					 
				 }
			 }else{
				 return toFind;
			 }
			  
	   } 
    
}
