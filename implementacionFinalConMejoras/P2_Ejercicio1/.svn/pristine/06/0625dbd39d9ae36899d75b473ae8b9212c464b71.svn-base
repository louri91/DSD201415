package GBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//GRAPHIC CLASS OF THE TABLERO
public class GBoardComputer extends GBoard implements ActionListener
{
	 
	
	//CONSTRUCTOR GIVING THE SIZE OF THE BOARD
	public GBoardComputer(Game inputGame, int inputSize)
	{
		super(inputGame,inputSize);
		setButtons(iSize);
		setName("Computer");
		setTurn("Wait...");
	}
	
	public GBoardComputer(Game inputGame, String inputName, int inputSize)
	{
		super(inputGame,inputName,inputSize);
		setButtons(iSize);
		setTurn("Wait...");
	}
	
	private void setButtons(int iSize)
	{
		pnlButtons=new JPanel();
		GridLayout gl = new GridLayout(iSize,iSize,0,0);
		pnlButtons.setLayout(gl);
		ImageIcon iconWater = new ImageIcon("C:/workspac/juego/images/water.jpg");
		buttons=new JButton[iSize][iSize];
		for(int i=0;i<iSize;++i)
			for(int j=0;j<iSize;++j)
			{
				buttons[i][j]=new JButton(iconWater);
				pnlButtons.add(buttons[i][j]);
				buttons[i][j].addActionListener(this);
			}
			
		panel=new Board(iSize);
		
		BorderLayout bl=new BorderLayout(10,10);
		setLayout(bl);
		add("North",pnlTitle);
		add("Center",pnlButtons);
	}
	
	//In the computer board, when somebody click is for attack
	public void actionPerformed(ActionEvent evt){hit(evt);}
	
	private void hit(ActionEvent evt)
	{
		if(canHit==true)
		{
			for(int i=0;i<panel.getSize();++i)
				for(int j=0;j<panel.getSize();++j)
				{
					if(buttons[i][j]==evt.getSource())
					{
						switch(panel.hit(i,j))
						{
							case Board.WATER:	paintWater(i,j);
													playWater();
													finishHit();	
													break;
							case Board.TOUCH: paintTouch(i,j);
													finishHit();
													break;
							case Board.CRASH: paintTouch(i,j);
													playCrash1();	
													finishHit();
													doIWin();
													break;
						}
					}
				}
		}
	}
	
	private void finishHit()
	{
		canHit=false;
		setTurn("Is your turn...");
		myGame.computerTurn(); //Say to the game that finished playing with it, and it´s the turn of the computer
	}
}