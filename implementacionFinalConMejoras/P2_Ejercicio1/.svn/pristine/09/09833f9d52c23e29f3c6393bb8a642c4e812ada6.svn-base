package GBoard;

//CLASS THAT CONTAIN THE INTERFACE OF ALL THE SHIPS
public class Ship 
{
	//
	private String sName; //Variable to know the name and type of the ship
	private int iSize; //Variable to know the total size of the ship
	private int iHits; //Variable to know how many hits the ship has recived
	
	//Constansts
	static final int TOUCH=1;
	static final int CRASH=2;
			
	//BEHAVIORS	
	//CONSTRUCTOR
	public Ship(String inputName, int inputSize)
	{
		sName=inputName;
		iSize=inputSize;
		iHits=0;
	} 
	
	//READ METHOD
	public String getName(){return(sName);}
	public int getSize(){return(iSize);}
	public int getHits(){return(iHits);}
	
	//METHOD TO KNOW IF THE SHIP IS CRASHED
	public boolean crash(){return(getHits()==getSize()?true:false);}
		
	private void incrementHits(){++iHits;}
	
	//THE SHIP HAS BEEN HIT
	public int hitShip()
	{
		if (getHits()<getSize())
		{
			incrementHits();
			if(crash())
				return(CRASH);
			return(TOUCH);
      }
		return(CRASH);
   }
}