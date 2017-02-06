import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import tools.Tools;

/**
 * Class processing user request between Query and Report about Countries, Airports and Runways
 * 
 * @author: Carlo Lacagnina
 * 
 * @notes: additional method is needed to remove quotes in output for better display
 * @notes: web application is still needed to be implemented
 * 
 */
class InScanner {
	private String userChoice1; //user choice between Query and Reports
	private String userChoice2; //user choice about country to query
	//arrays containing information from input files
	private String[][] inputAirports;
	private String[][] inputRunways;
	private String[][] inputCountries;
	//column number in arrays containing information to be processed
	static final int cCountriesName = 2;
	static final int cCountriesCode = 1;
	static final int cAirportsIsoCountry = 8;
	static final int cAirportsName = 3;
	static final int cAirportsId = 0;
	static final int cRunwaysAirportRef = 1;
	static final int cRunwaysAirportIdent = 2;
	static final int cRunwaysSurface = 5;
	static final int cRunwaysLeIdent = 8;
	
	/**
	 * Class constructor
	 * 
	 * @param pathAirports : input file Airports
	 * @param pathRunways : input file Runways
	 * @param pathCountries : input file Countries
	 * @param sep : character separating fields
	 * @param startLine : how many rows to skip in files
	 * 
	 */
	InScanner(String pathAirports, String pathRunways, String pathCountries, String sep, int startLine) throws FileNotFoundException {
		int nRow; //number of rows in input file
		int nCol; //number of columns in input file
		
		nRow = getFileDim(pathAirports, sep, startLine)[0];
		nCol = getFileDim(pathAirports, sep, startLine)[1];
		inputAirports = readerCSV(pathAirports, sep, startLine, nRow, nCol);
		nRow = getFileDim(pathRunways, sep, startLine)[0];
		nCol = getFileDim(pathRunways, sep, startLine)[1];
		inputRunways = readerCSV(pathRunways, sep, startLine, nRow, nCol);
		nRow = getFileDim(pathCountries, sep, startLine)[0];
		nCol = getFileDim(pathCountries, sep, startLine)[1];
		inputCountries = readerCSV(pathCountries, sep, startLine, nRow, nCol);
	}
	
	/**
	 * Reading and processing user choice between Query and Reports
	 * 
	 * @return: request by user
	 * 
	 */
	public void setKeyboardScanner()  {
		Scanner in = new Scanner(System.in);
		System.out.print("Choose between Query or Reports: ");
		userChoice1 = in.nextLine();
		if(userChoice1.equals("Query") || userChoice1.equals("Q")) {
			System.out.print("Which country do you want to query? Type country name or related code: ");
			userChoice2 = "\""+in.nextLine()+"\""; //it needs "" to work, because input array has ""
			query(inputCountries, inputAirports, inputRunways, userChoice2);
			in.close();
		}else{
			if(userChoice1.equals("Reports") || userChoice1.equals("R")) {	
				report(inputCountries, inputAirports, inputRunways);
				in.close();
			}else{
				System.out.print("Wrong input! Try again");
				in.close();
				System.exit(1);
			}
		}
	}
	
	/**
	 * Get dimension of input files
	 * 
	 * @param fileNameIn : input CSV file
	 * @param sep : separator type among fields in CSV file
	 * @param startLine : number of lines to skip reading because just text
	 * @return : number of rows and columns in file
	 * 
	 */
	 public int[] getFileDim(String fileNameIn, String sep, int startLine) throws FileNotFoundException {
		 int i = 0; //index for line numbering
		 int[] dim = {0,0}; //array containing n rows and n columns
		 
		 try {
			 BufferedReader in = new BufferedReader(new FileReader(fileNameIn));
			 String line = in.readLine();
			 String str0[] = line.split(sep);
			 while(line!=null) {
				 i++;
				 line = in.readLine();
			 }
			 in.close();
			 dim[0] = i-startLine;
			 dim[1] = str0.length;
		 
		 } catch(FileNotFoundException e) {
			 System.out.println("File not found: " + fileNameIn);
			 System.exit(1);
		 } catch(IOException e) {
			 System.out.println("Unable to read file: " + fileNameIn);
			 System.exit(1);
		 }
		 return dim;
	 }
	
	/**
	 * Read CSV files
	 * 
	 * @param fileNameIn : input CSV file
	 * @param sep : character separating fields in files
	 * @param startLine : how many rows to skip in input files
	 * @param nRow : number of rows in in input file
	 * @param nCol : number of columns in input file
	 * 
	 * @return arrays containing fields from CSV files
	 * 
	 */
	 private String[][] readerCSV(String fileNameIn, String sep, int startLine, int nRow, int nCol) throws FileNotFoundException {
		 String[][] input = new String[nRow][nCol]; //declaration input array
		 int i = 0; //index for lines
		 int jj = 0; //index for columns
		 
		 try {
			 BufferedReader in = new BufferedReader(new FileReader(fileNameIn));
			 String line = in.readLine();
			 while(line!=null) {
				 if(i>=startLine){
					 String str0[] = line.split(sep);
					 String str1 = "";
					 for(int j=0; j<str0.length; j++) {
						//if is needed to work around with cases like: "str, str, str" e.g. line 453 in airports file
						 if(j>=nCol){
							str1 = str1+" "+str0[j];
							jj = nCol-1;
						 } else {
							 str1 = str0[j];
							 jj = j;
						 }
						 input[i-startLine][jj] = str1;
					 }
				 }
				 i++;
				 line = in.readLine();
			 }
			 in.close();
		 } catch(FileNotFoundException e) {
			 System.out.println("File not found: " + fileNameIn);
			 System.exit(1);
		 } catch(IOException e) {
			 System.out.println("Unable to read file: " + fileNameIn);
			 System.exit(1);
		 }
		 return input;
	 }
	 
     /**
      * Processing user choice Query
      * 
      * @param inputCountries : array containing countries-related fields
      * @param inputAirports : array containing airports-related fields
      * @param inputRunways : array containing runways-related fields
      * @param choice : user choice of country to be queried
      * 
      */
	 private void query(String[][] inputCountries, String[][] inputAirports, String[][] inputRunways, String choice) {
		 String airName = new String(); //airport name
		 String airId = new String();   //airport ID
		 int outputRunways = -1;        //where is in file runway information associated with airport
		 
		 choice = Tools.countryPartialMatch(Tools.reduce2Dto1D(inputCountries, cCountriesName), choice); //partial match country name
		//if country name is given, then looks for related country code
		 if(choice.length() > 4) { //"US" = 4 characters			 
			 int outputCountry = Tools.find(Tools.reduce2Dto1D(inputCountries, cCountriesName), choice)[0]; //country name
			 if(outputCountry == -1) { System.out.println("Country not in dataset!"); System.exit(1); }
			 choice = inputCountries[outputCountry][cCountriesCode]; //country code
		 }
		 
	     int[] outputAirports = Tools.find(Tools.reduce2Dto1D(inputAirports, cAirportsIsoCountry), choice); //position in file of airports associated to country
	     if(outputAirports[0] == -1) { System.out.println("No additional information!"); System.exit(1); }
	     for(int i=0; i<outputAirports.length; i++) {
	    	 airName = inputAirports[outputAirports[i]][cAirportsName]; //airport name associated with country
	         airId = inputAirports[outputAirports[i]][cAirportsId];  //airport id associated with country
	         System.out.println("Airport in "+choice+" : "+airName);
	         outputRunways = Tools.find(Tools.reduce2Dto1D(inputRunways, cRunwaysAirportRef), airId)[0];
		     if(outputRunways == -1) {
		    	 System.out.println("Additional Information about runway is not available");
		     }else{
		    	 System.out.println("Additional information: airport ident: "+
		            inputRunways[outputRunways][cRunwaysAirportIdent]); //airport ident in runways
		     }
	     }         
	 }
	 
	 /**
	  * Processing user choice Report
	  * 
	  * @param inputCountries : array containing list of countries
	  * @param inputAirports  : array containing list of airports
	  * @param inputRunways   : array containing list of runways
	  * 
	  */
	 private void report(String[][] inputCountries, String[][] inputAirports, String[][] inputRunways) {
		 int lenCountries = inputCountries.length;        //length country array
		 int[] nAirports = new int[lenCountries];         //array with number of airports for country
	 
		 String[] countryName = Tools.reduce2Dto1D(inputCountries, cCountriesName);
		 String[] countryCode = Tools.reduce2Dto1D(inputCountries, cCountriesCode);
		 
		 for(int i=0; i<lenCountries; i++) {
		     int[] outputAirports = Tools.find(Tools.reduce2Dto1D(inputAirports, cAirportsIsoCountry), countryCode[i]); //position in file of airports associated to country	     
		     if(outputAirports[0] == -1) continue;
		     nAirports[i] = outputAirports.length;	     
		     printingTypeRunways(outputAirports, countryName[i]);
		 }	 
		 printing10Countries(nAirports, countryName, lenCountries);
		 printing10Runways(inputRunways);	 
	 }
	 
	 /**
	  * Printing type of runways (surface) per country
	  * 
	  * @param outputAirports : position in input file of airports associated to given country
	  * @param countryName    : name of given country
	  * 
	  */
	 
	 private void printingTypeRunways(int[] outputAirports, String countryName) {
		 String airId = new String();   //airport ID
		 int outputRunways = -1;        //where is in file runway information associated with airport
	     HashMap<String, Integer> map = new HashMap<String, Integer>();
	     
	     for(int j=0; j<outputAirports.length; j++) {
		     airId = inputAirports[outputAirports[j]][cAirportsId];  //airport id associated with country
		     outputRunways = Tools.find(Tools.reduce2Dto1D(inputRunways, cRunwaysAirportRef), airId)[0];
		     if(outputRunways == -1) {
		    	 continue;
		     }else{
		    	 String str = inputRunways[outputRunways][cRunwaysSurface];
		         if(!map.containsKey(str)){
		        	 map.put(str,1);
			     }else{
			         int count = map.get(str)+1;
			         map.put(str,count);
			     }
		      }  
	     }
	     System.out.println("\n Type of runways (surface) in "+countryName+": ");
	     System.out.println(map.keySet());
	 }
	  
	 /**
	  * Printing first 10 countries with highest or lowest airports
	  * 
	  * @param nAirports : array with number of airports in each country
	  * @param countryName : array with name of countries
	  * @param lenCountries : length of countryName array
	  * 
	  */
     private void printing10Countries(int[] nAirports, String[] countryName, int lenCountries) {
    	 Tools.sortArray(nAirports,countryName); //sorting countries by increasing number of present airports
		 
		 System.out.println("\n First 10 countries with highest number of airports:");
		 for(int i=lenCountries-1; i>lenCountries-11; i--) {
			 System.out.println("Country: "+countryName[i]+", number of airports: "+nAirports[i]);
		 }
		 
		 System.out.println("\n First 10 countries with lowest number of airports: ");
		 for(int i=0; i<10; i++) {
			 System.out.println("Country: "+countryName[i]+", number of airports: "+nAirports[i]);
		 }
     }
     
     /**
      * Printing first 10 most common runways identifications
      * 
      * @param inputRunways : array containing runways
      * 
      */
     private void printing10Runways(String[][] inputRunways) {
         HashMap<String, Integer> map = new HashMap<String, Integer>();
         
		 String[] indentifications = Tools.reduce2Dto1D(inputRunways, cRunwaysLeIdent);
		 
         //creates map containing identifications occurrences
         for(String s:indentifications){
	         if(!map.containsKey(s)){
	            map.put(s,1);
	         }else{
	            int count = map.get(s)+1;
	            map.put(s,count);
	         }
	      }
         
          //extracts arrays from map of frequencies
          int lenMap = map.size();
          int[] frequencyInt = new int[lenMap];
          String[] frequencyString = new String[lenMap];
          int w = 0;
	      for (String s : map.keySet()) {
	    	  frequencyInt[w] = map.get(s);
	    	  frequencyString[w] = s;
	    	  w++;
	      }
	      
	      //sorting identifications in inputRunways by increasing frequency 
	      Tools.sortArray(frequencyInt, frequencyString);
		  
	      System.out.println("\n First 10 most common runways identifications: ");
		  for(int i=lenMap-1; i>lenMap-11; i--) {
			  System.out.println("Occurrence of identification: "+frequencyString[i]+" is: "+frequencyInt[i]);
		  }
	 }
}

public class LunaTech0 {

	public static void main(String[] args) throws FileNotFoundException {
		String pathIn = "C:/Users/Lacagnina/Documents/LunaTech/random-repo/resources/"; //path for input files
		String pathAirports = pathIn+"airports.csv";  //name input file
		String pathRunways = pathIn+"runways.csv";   //name input file
		String pathCountries = pathIn+"countries.csv"; //name input file
		int startLine = 1; //number of lines to skip because just text
		String sep = ",";  //type of separator among fields in file

		InScanner resources1 = new InScanner(pathAirports, pathRunways, pathCountries, sep, startLine);
		resources1.setKeyboardScanner();
	}
}
