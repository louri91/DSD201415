package GBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//GRAPHIC CLASS OF THE TABLERO
public class GBoardPlayer extends GBoard implements ActionListener
{
	//BEHAVIORS	
	
	//CONSTRUCTOR GIVING THE SIZE OF THE BOARD
	public GBoardPlayer(Game inputGame, int inputSize)
	{
		super(inputGame,inputSize);
		setButtons(iSize);
		setName("Human Player");
		setTurn("Put your ships on your board");
	}
	
	public GBoardPlayer(Game inputGame, String inputName, int inputSize)
	{
		super(inputGame,inputName,inputSize);
		setButtons(iSize);
		setTurn("Put your ships on your board");
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
	
	//Only put ship, because in the board of the player, the player only can put his ships
	public void actionPerformed(ActionEvent evt){putShip(evt);}
	
	private void putShip(ActionEvent evt)
	{
		if(btnShip!=null)
		{
			Ship inputShip=panel.getShip(btnShip.getText());
			for(int i=0;i<panel.getSize();++i)
				for(int j=0;j<panel.getSize();++j)
					if(buttons[i][j]==evt.getSource())
						putShip(inputShip,i,j);
		}
	}
		
	public int hit(int x, int y)
	{
		if(canHit==true)
		{
			switch(panel.hit(x,y))
			{
				case Board.WATER:	paintWater(x,y);
										playWater();
										finishHit();
										return(Board.WATER);	
				case Board.TOUCH: paintTouch(x,y);
										finishHit();
										return(Board.TOUCH);
				case Board.CRASH: paintTouch(x,y);
										playCrash2();
										finishHit();
										doIWin();
										return(Board.CRASH);
				case Board.YOUHITBEFORE: 	return(Board.YOUHITBEFORE);
			}
		}
		return(Board.YOUHITBEFORE);
	}
	
	private void finishHit()
	{
		canHit=false; //Say that finished playing with this board
		setTurn("Is your turn..."); //Say to the owner of the board, that the finish attacking him and he can continue playing
		myGame.playerTurn(); //Say to the game that finished playing with it, and it´s the turn of the human player
	}
}