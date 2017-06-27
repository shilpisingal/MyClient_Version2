
 package com.simple.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;


public class ReadProperties{
	
	private Properties property=null;
		
	// *********************************** Main Method ********************
	public static void main(String[] args)
	{
		ReadProperties rp = new ReadProperties();
		rp.loadFile("config.properties");    
	}
	
	// **********************Get all the property names specified in the file *****************	
	public Set<Object> getAllKeys(){
        Set<Object> keys = property.keySet();
        return keys;
    }
     
	// **********************Get value of a particular property *****************
    public String getPropertyValue(String key){
        return this.property.getProperty(key);
    }
    
   
    // **********************Get list of property names that match a given pattern *****************
    public List<String> getPropertyList(String pattern)
    {
    	  //get list of properties and their values
		  Set<Object> propList=property.keySet();
		  Iterator<Object> itr=propList.iterator();
  
		  List<String> patternList = new ArrayList<String>();

		  while(itr.hasNext())
		  {
			 String str=(String)itr.next();
			 if(str.toLowerCase().startsWith(pattern.toLowerCase()))
			 {
			   patternList.add(str);
			 }
		  }
    	
		  return patternList;
    }//end method getPropertyList 
    
    
   // *********************************** Method to print all the properties ********************
    public void printProperties()
    {
    	//get list of properties and their values
		  Set<Object> propList=property.keySet();
		  Iterator<Object> itr=propList.iterator();
		  
		  System.out.println("Property Name\t\tProperty Value");
		  System.out.println("..............................................");
		  while(itr.hasNext())
		  {
			 String str=(String)itr.next();
			 System.out.println(str +"\t\t"+property.getProperty(str));
		  }	
    }//end method printProperties 
    
    // ******************Load the property file and print all properties on console ********************
	public String loadFile(String propFileName)
	{
		String result="";
		property= new Properties();
				
		try 
		{
		 
		  InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		  if(inputStream !=null)
		  {
			  property.load(inputStream);					
			  //printProperties(); 
		  }else
		  {
			  System.out.println("property file '" + propFileName + "' not found in the classpath");
			  System.out.println("Please add the resources folder to the class path");
		  }
		}
		catch(FileNotFoundException e){
			System.out.println("property file '" + propFileName + "' not found in the classpath");
			System.out.println("Please add the resources folder to the class path");
		    e.printStackTrace();			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return result;
	}//end method loadFile
	
}//end class ReadProperties











