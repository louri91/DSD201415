package GBoard;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;

import Casino.IJuego;

//GRAPHIC CLASS OF THE TABLERO
abstract public class GBoard extends JPanel  
{
	 
	
	//Attributes
	protected Game myGame;	//Reference to the game, like this the gboard know where is it playing
	
	protected JButton [][] buttons;  //All the buttons 
	protected Board panel; //The board
	protected int iSize; //The size of the boards
	
	//Panels 
	protected JPanel pnlTitle; //The panel where appear the name and the turn of the player
	protected JPanel pnlButtons;
	
	//Labels
	protected JLabel lblName;
	protected JLabel lblTurn;
	protected boolean canHit; //Control if is his turn or not
	
	//Reference to a object and variable that the board should know when the user is putting the ships
	protected JButton btnShip;
	protected int iPosition;
	static final int HORIZONTAL=0;
	static final int VERTICAL=1;

	
	
	//CONSTRUCTOR GIVING THE SIZE OF THE BOARD
	public GBoard(Game inputGame, int inputSize)
	{
		myGame=inputGame;
		iSize=inputSize;
		canHit=true;
		setTitle();
	}
	
	public GBoard(Game inputGame, String inputName, int inputSize)
	{
		myGame=inputGame;
		iSize=inputSize;
		canHit=true;
		setTitle();
		setName(inputName);
	}
			
	private void setTitle()
	{
		//Create the space that we need for the objects
		pnlTitle=new JPanel(); 
		lblName=new JLabel();
		lblTurn=new JLabel();
	
		//Prepare all the layout that we are going to use
		GridLayout gl=new GridLayout(1,2,15,15);
		FlowLayout fl=new FlowLayout(FlowLayout.CENTER,5,5);
		
		JPanel pnlName=new JPanel();
		JPanel pnlTurn=new JPanel();
		
		pnlName.setLayout(fl);
		pnlTurn.setLayout(fl);
		
		pnlName.add(lblName);
		pnlTurn.add(lblTurn);
		
		pnlTitle.setLayout(gl);
		
		pnlTitle.add(pnlName);
		pnlTitle.add(pnlTurn);
	}
	
	protected boolean putShip(Ship inputShip,int x, int y)
	{
		if(iPosition==HORIZONTAL)
		{
			if(putShipHorizontal(inputShip,x,y))
			{
				if(btnShip!=null)
				{
					paintShipHorizontal(inputShip,x,y);
					btnShip.setVisible(false);
					btnShip=null;
					myGame.selectNextShip();
				}
				return(true);
			}
			else
				return(false);
		}
		else
		{
			if(putShipVertical(inputShip,x,y))
			{
				if(btnShip!=null)
				{
					paintShipVertical(inputShip,x,y);
					btnShip.setVisible(false);
					btnShip=null;
					myGame.selectNextShip();
				}
				return(true);
			}
			else
				return(false);
		}
	}
	
	public void putShipAutomatically()
	{
		Random rnd=new Random();
		Ship auxShip;
		for(int i=0;i<Board.NUMSHIPS;++i)
			do
			{
				auxShip=panel.getShip(i);
				setPosition(rnd.nextInt(2));
			}while(putShip(auxShip,rnd.nextInt(iSize),rnd.nextInt(iSize))==false);
		
	}
	
	protected boolean putShipHorizontal(Ship inputShip, int x, int y)
	{
		return(panel.putShip(inputShip,x,y,HORIZONTAL));
	}
	
	protected boolean putShipVertical(Ship inputShip, int x, int y)
	{
		return(panel.putShip(inputShip,x,y,VERTICAL));
	}
	 
	
	protected void paintShipHorizontal(Ship inputShip, int x, int y)
	{
		ImageIcon iconLeft = new ImageIcon("/Documents and Settings/Administrador/Escritorio/juegos/Casino/src/GBoard/imagesLeft.gif");
		ImageIcon iconCentre = new ImageIcon("/Documents and Settings/Administrador/Escritorio/juegos/Casino/src/GBoard/Centre.gif");
		ImageIcon iconRight = new ImageIcon("/Documents and Settings/Administrador/Escritorio/juegos/Casino/src/GBoard/Right.gif");
		buttons[x][y].setIcon(iconLeft);
		for(int i=y+1;i<y+inputShip.getSize()-1;++i)
					buttons[x][i].setIcon(iconCentre);
		buttons[x][y+inputShip.getSize()-1].setIcon(iconRight);
	}
	
	protected void paintShipVertical(Ship inputShip, int x, int y)
	{
		ImageIcon iconTop = new ImageIcon("/Documents and Settings/Administrador/Escritorio/juegos/Casino/src/GBoard/Top.gif");
		ImageIcon iconCentre = new ImageIcon("/Documents and Settings/Administrador/Escritorio/juegos/Casino/src/GBoard/Centre2.gif");
		ImageIcon iconBottom = new ImageIcon("/Documents and Settings/Administrador/Escritorio/juegos/Casino/src/GBoard/Bottom.gif");
		buttons[x][y].setIcon(iconTop);
		for(int i=x+1;i<x+inputShip.getSize()-1;++i)
					buttons[i][y].setIcon(iconCentre);
		buttons[x+inputShip.getSize()-1][y].setIcon(iconBottom);

	}
	
	protected void paintWater(int x, int y)
	{
		ImageIcon iconWater = new ImageIcon("/Documents and Settings/Administrador/Escritorio/juegos/Casino/src/GBoard/fail.jpg");
		buttons[x][y].setIcon(iconWater);
	}
	
	protected void paintTouch(int x, int y)
	{	int s;
		int level,cantidad=0;
	
		ImageIcon iconWater = new ImageIcon("/Documents and Settings/Administrador/Escritorio/juegos/Casino/src/GBoard/touch.jpg");
		buttons[x][y].setIcon(iconWater);
	
		s = myGame.darSaldo();
		level = myGame.iLevel;
		if(lblName.getText().equals("Computer")){
			//le sumamos euros por tocarle una parte del barco del enemigo segun el nivel
			if(level==0){
				//easy
				cantidad = 1;
			}else if(level==1){
				//medium
				cantidad = 3;
			}else if(level == 2){
				//hard
				cantidad = 5;
			}
			s = s + cantidad;
			JOptionPane.showMessageDialog(null,"ACERTASTE,"+cantidad+"EUROS,SALDO:"+s);			
		}else{
			//le quitamos 3 euros por tocarle una parte del barco
			if(level==0){
				//easy
				cantidad = 2;
			}else if(level==1){
				//medium
				cantidad = 3;
			}else if(level == 2){
				//hard
				cantidad = 4;
			}
			s = s - cantidad;
			JOptionPane.showMessageDialog(null,"TOCADO,"+cantidad+"EUROS,SALDO:"+s);
			if (s<2){
				if(s<0){
					s = 0;
				}
				JOptionPane.showMessageDialog(null,"SALDO INSUFICIENTE, PARTIDA ACABADA.SALDO:"+s);
				myGame.ponerSaldo(s);
				myGame.initialize();
			}			
		}
		myGame.ponerSaldo(s);
	}
	
	//Pass the button of the ship that we are going to put on the board
	public void setShip(JButton inputShip){btnShip=inputShip;}

	//Pass the position of the ship that we are going to put on the board
	public void setPosition(int inputPosition){iPosition=inputPosition;}
	
	public void setCanHit(boolean inputCanHit){canHit=inputCanHit;}
	public void setName(String inputName){lblName.setText(inputName);}
	public void setTurn(String inputTurn){lblTurn.setText(inputTurn);}
	
	public void doIWin()
	{
		int s,level,cantidad1=0,cantidad2=0;
		level = myGame.iLevel;
		if(panel.doYouWin())
		{			
			if(level==0){
				//easy /facil
				cantidad1 = 3;
				cantidad2 = 5;
			}else if(level==1){
				//medium /medio
				cantidad1 = 10;
				cantidad2 = 8;
			}else if(level == 2){
				//hard / dificil
				cantidad1 = 16;
				cantidad2 = 10;
			}
			s = myGame.darSaldo();
			if(lblName.getText().equals("Computer")){
				//le sumamos la cantidad de euros segun el nivel de dificultad
				s = s + cantidad1;
				JOptionPane.showMessageDialog(null,"HAS GANADO!: "+cantidad1+" EUROS SALDO:"+s);
			}
			else{
				//le quitamos la cantidad de euros segun el nivel de dificultad
				s = s - cantidad2;
				if (s<0){
					s = 0;
				}
				
				JOptionPane.showMessageDialog(null,"HAS PERDIDO:" +cantidad2+" EUROS MENOS. SALDO:"+s);
				//juego acabado. Salimos de la aplicacion.
			}
			//aqui acabaria el juego, actualizamos SALDO
			myGame.ponerSaldo(s);
			myGame.initialize();
		}
	}
	
	public Point getShipPosition()
	{
		for(int i=0;i<iSize;++i)
			for(int j=0;j<iSize;++j)
				if(panel.isShip(i,j))
					return(new Point(i,j));
		return(new Point(-1,-1));
	}
	
	//For this functions I use part of code from http://www.htmlpoint.com/guidajava/java_36.htm
	//The website is in spanish, and they explain what do each line, so at last i understand what happend to play the sound, more or less
	
	public void playWater()
	{
		File sf=new File("../sounds/water.wav");
		try
		{
			AudioFileFormat aff=AudioSystem.getAudioFileFormat(sf);
			AudioInputStream ais=AudioSystem.getAudioInputStream(sf);
			AudioFormat af=aff.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class,ais.getFormat(),((int) ais.getFrameLength() *af.getFrameSize()));
			Clip ol = (Clip) AudioSystem.getLine(info);
			ol.open(ais);
			ol.loop(0);
		}
		catch(UnsupportedAudioFileException ee){}
		catch(IOException ea){}
		catch(LineUnavailableException LUE){}
		catch(Exception e){}
	}
	
	public void playCrash1()
	{
		File sf=new File("../sounds/explosion1.wav");
		try
		{
			AudioFileFormat aff=AudioSystem.getAudioFileFormat(sf);
			AudioInputStream ais=AudioSystem.getAudioInputStream(sf);
			AudioFormat af=aff.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class,ais.getFormat(),((int) ais.getFrameLength() *af.getFrameSize()));
			Clip ol = (Clip) AudioSystem.getLine(info);
			ol.open(ais);
			ol.loop(0);
		}
		catch(UnsupportedAudioFileException ee){}
		catch(IOException ea){}
		catch(LineUnavailableException LUE){}
		catch(Exception e){}
	}
	
	public void playCrash2()
	{
		File sf=new File("../sounds/explosion2.wav");
		try
		{
			AudioFileFormat aff=AudioSystem.getAudioFileFormat(sf);
			AudioInputStream ais=AudioSystem.getAudioInputStream(sf);
			AudioFormat af=aff.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class,ais.getFormat(),((int) ais.getFrameLength() *af.getFrameSize()));
			Clip ol = (Clip) AudioSystem.getLine(info);
			ol.open(ais);
			ol.loop(0);
		}
		catch(UnsupportedAudioFileException ee){}
		catch(IOException ea){}
		catch(LineUnavailableException LUE){}
		catch(Exception e){}
	}
}

