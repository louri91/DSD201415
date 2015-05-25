package GBoard;

import java.util.*;
import java.awt.*;

//CLASS OF THE INTELLIGENCE OF THE COMPUTER PLAYER
public class Intelligence 
{
	//Atributes
	private GBoardPlayer brdEnemy;	//A reference to the enemy GBoardPlayer
	
	private int iLevel;	//variable to keep the level of the intelligence
	private int iSize;
	
	private int iHitX; //Position where the last time where the intelligence hitted
	private int iHitY;
	
	private int iSum; //Variables to keep the direction, (top, left, bottom, right) where the machine will continue hitting
	private int iPos;
	private int iCount; //variable to count how many times we hit a ship
	private int iMissHits; //Variable use in the hard level, to know how many times it fails
		
	static final int EASY=0;
	static final int MEDIUM=1;
	static final int HARD=2;
				
	//BEHAVIORS	
	//default constructor
	public Intelligence(GBoardPlayer inputGBoard)
	{
		brdEnemy=inputGBoard;
		iLevel=EASY;iSize=10;
		iMissHits=0;
		resetHits();
	}
	//Constructor giving the level of intelligence
	public Intelligence(GBoardPlayer inputGBoard,int inputLevel)
	{
		brdEnemy=inputGBoard;
		iLevel=inputLevel;
		iMissHits=0;
		resetHits();
		switch(iLevel)
		{
			case EASY:	iSize=10;
							break;
			case MEDIUM:iSize=15;
							break;
			case HARD:	iSize=20;
							break;
		}
	}
	
	//Give you a value for the position for attack (Depending on the dificult level)
	public void attack()
	{
		switch(iLevel)
		{
			case EASY:	attackEasy();
							break;
			case MEDIUM:attackMedium();
							break;
			case HARD:	attackHard();
		}
	}
	
	private void attackEasy()
	{
		Random rnd=new Random();
		while(hitRandomly()==Board.YOUHITBEFORE);			
	}
	
	private void attackMedium()
	{
		if(iHitBefore())
		{
			if(onlyOneHit())
				makeNextHit(); 
			else
				continueHitting(); //when we know the position of the ship
		}
		else
			makeRandomMedium();
	}
	
	private void attackHard()
	{
		if(iHitBefore())
		{
			if(onlyOneHit())
				makeNextHit(); 
			else
				continueHitting(); //when we know the position of the ship
		}
		else
		{	
			if(iMissHits%5==0 && iMissHits!=0)
			{
				Point hitPoint=new Point();
				hitPoint=brdEnemy.getShipPosition();
				makeHardAttack((int)hitPoint.getX(),(int)hitPoint.getY());
			}
			else
				makeRandomMedium();
		}
			
	}
	
	private int hitRandomly()
	{
		Random rnd=new Random();
		iHitX=rnd.nextInt(iSize);
		iHitY=rnd.nextInt(iSize);
		return(brdEnemy.hit(iHitX,iHitY));
	}

	private boolean iHitBefore(){return(iHitX!=-1?true:false);}
	private boolean onlyOneHit(){return(iCount==0?true:false);} //if iSum is equal 0 is that we only make one hit (so we don´t know the position of the ship yet)
	
	private void makeNextHit()
	{
		Random rnd=new Random();
		int iPosAux=0;  //For know if it´s going to look in the space vertically or horizontally
		int iSumAux=0;  //if we are going to look up, down, left or right
		int iHit=0;
		do
		{
			iPosAux=(rnd.nextInt(2)==0?-1:1); //0 we´ll check on the left and 1 on the right part of the place that we hit before 
			iSumAux=(rnd.nextInt(2)==0?-1:1); //0 we´ll check on the top and 1 on the bottom part of the place that we hit before 
			if(iPosAux==-1) //look vertically
				iHit=brdEnemy.hit(iHitX,iHitY+iSumAux);
			else //look horizontally
				iHit=brdEnemy.hit(iHitX+iSumAux,iHitY);
		}while(iHit==Board.YOUHITBEFORE);
		switch(iHit)
		{
			case Board.TOUCH:	if(iPosAux==-1) //hold vertically
										holdHits(iHitX,iHitY+iSumAux);
									else //hold horizontally
										holdHits(iHitX+iSumAux,iHitY);
									++iCount;
									break;
			case Board.CRASH: resetHits();  //the 2 spaces ship crash in this funcion
									break;
		}
		if(iCount==1)
		{
			iSum=iSumAux;
			iPos=iPosAux;
		}
		
	}
	
	private void continueHitting()
	{
		int iHit=0;
		do
		{
			if(iPos==-1) //look vertically
				iHit=brdEnemy.hit(iHitX,iHitY+iSum);
			else //look horizontally
				iHit=brdEnemy.hit(iHitX+iSum,iHitY);
			if(iHit==Board.YOUHITBEFORE)
			{
				if(iPos==-1) //hold vertically
					holdHits(iHitX,iHitY+iSum);
				else //hold horizontally
					holdHits(iHitX+iSum,iHitY);
			}
		}while(iHit==Board.YOUHITBEFORE);
		switch(iHit)
		{
			case Board.WATER:	iSum=(iSum==1?-1:1);
									break;									
			case Board.TOUCH:	if(iPos==-1) //hold vertically
										holdHits(iHitX,iHitY+iSum);
									else //hold horizontally
										holdHits(iHitX+iSum,iHitY);
									break;
			case Board.CRASH: resetHits();
									break;			
		}
		
	}
	
	private void makeRandomMedium()
	{
		int iHit;
		Random rnd=new Random();
		do
		{
			iHitX=rnd.nextInt(iSize);
			iHitY=rnd.nextInt(iSize);
			iHit=brdEnemy.hit(iHitX,iHitY);
		}while(iHit==Board.YOUHITBEFORE);
		switch(iHit)
		{
			case Board.WATER:	++iMissHits;	
									resetHits();
									break;
			case Board.TOUCH:	++iCount;
									break;
		}
	}
	
	private void makeHardAttack(int x, int y)
	{
		iHitX=x;
		iHitY=y;
		brdEnemy.hit(iHitX,iHitY);
		++iCount;
		iMissHits=0;
	}
	
	private void resetHits(){iHitX=-1;iHitY=-1;iSum=0;iPos=0;iCount=-1;}
	private void holdHits(int inputX,int inputY){iHitX=inputX;iHitY=inputY;}
}
