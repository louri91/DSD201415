package GBoard;

//CLASS OF THE BOARD
public class Board 
{
	//Atributes
	private Ship [][] board; //The grid where the boards are
	private boolean [][] hits; //The grid where alredy has been hitted
	private Ship [] ships;	//Array of all the ships of the user
	private int iSize;	//variable to keep the size of the board
	
	static final int NUMSHIPS=4;
	static final int HORIZONTAL=0;
	static final int VERTICAL=1;
	
	static final int WATER=0;
	static final int TOUCH=1;
	static final int CRASH=2;
	static final int YOUHITBEFORE=3;
				
	//BEHAVIORS	
	//CONSTRUCTOR GIVING THE SIZE OF THE BOARD
	public Board(int inputSize)
	{
		board=new Ship[inputSize][inputSize];
		hits=new boolean[inputSize][inputSize]; //Default all are false so we don´t need to initialice
		ships=new Ship[NUMSHIPS];
		iSize=inputSize;
		initShips();	//Initialice the ships
	}
	
	//PUBLIC METHOD TO SET UP THE BOARD WITH ALL THE SHIPS
	public boolean putShip(Ship inputShip, int x, int y, int pos)
	{	
		
		if(pos==HORIZONTAL) //Horizontal
			if(possibleToPutHorizontal(inputShip,x,y))
			{
				putShipHorizontal(inputShip,x,y);
				return(true);
			}
			else
				return(false);
		else //vertical
			if(possibleToPutVertical(inputShip,x,y))
			{
				putShipVertical(inputShip,x,y);
				return(true);
			}
			else
				return(false);
	}
	
	//Look if is possible to put the ship
	private boolean possibleToPutHorizontal(Ship inputShip,int x,int y)
	{
		if(y+inputShip.getSize()<=iSize)
		{
			int count=0;
			for(int i=y;i<y+inputShip.getSize();++i)
				if(board[x][i]==null) //Calculate the space
					++count;
				if(count==inputShip.getSize())  //If space enough
					return(true);
		}
		return(false);
	}
	
	//Look if is possible to put the ship
	private boolean possibleToPutVertical(Ship inputShip,int x,int y)
	{
		if(x+inputShip.getSize()<=iSize)
		{
			int count=0;
			for(int i=x;i<x+inputShip.getSize();++i)
				if(board[i][y]==null)
					++count;
				if(count==inputShip.getSize())
					return(true);
		}
		return(false);
	}
	
	//Put the ship 
	private void putShipHorizontal(Ship inputShip, int x, int y)
	{
		for(int i=y;i<y+inputShip.getSize();++i)
			board[x][i]=inputShip; //Make the reference
	}
	
	private void putShipVertical(Ship inputShip, int x, int y)
	{	
		for(int i=x;i<x+inputShip.getSize();++i)
			board[i][y]=inputShip;
	}
	
	public Ship getShip(String name)
	{
		for(int i=0;i<NUMSHIPS;++i)
			if(name.equals(ships[i].getName()))
				return(ships[i]);
		return(null);
	}
	
	public Ship getShip(int pos)
	{
		if(pos<NUMSHIPS)
			return(ships[pos]);
		return(null);
	}
	
	//Hit the ship on the board
	public int hit(int posX, int posY)
	{	
		if(posX<0 || posX>=iSize || posY<0 || posY>=iSize)
			return(YOUHITBEFORE);
		if(hits[posX][posY]==false)
		{
			hits[posX][posY]=true;
			if(board[posX][posY]!=null)
				return(board[posX][posY].hitShip());
			else
				return(WATER);	
		}
		else
			return(YOUHITBEFORE);
	}
	
	//Look if all the ships have been crashed 
	public boolean doYouWin()
	{
		int total=0;
		for(int i=0;i<NUMSHIPS;++i)
			if(ships[i].crash())
				++total;
		return(total==NUMSHIPS?true:false);
	}
	
	public int getSize(){return(iSize);}
	
	//PRIVATE METHOD TO INITIALIZE THE ARRAY OF THE SHIPS OF THE USER
	private void initShips()
	{
		ships[0]=new Ship("Battleship",4);
		ships[1]=new Ship("Cruiser",3);
		ships[2]=new Ship("Destroyer",3);
		ships[3]=new Ship("Submarine",2);
	}
	
	//Look if there is a ship or not
	public boolean isShip(int x, int y)
	{
		if(hits[x][y]==false)
			return(board[x][y]!=null?true:false);
		else
			return(false);
	}
}
