package GBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//CLASS THAT CONTROL THE EDIT OPTIONS
public class Edit extends JFrame implements ActionListener
{
	//Attributes
	private Game myGame;	//Reference to the game, like this the gboard know where is it playing
	
	//The object we need to create the edit frame
	private JFrame frEdit;
	private JRadioButton [] rdbDificulty;
	private JButton btnAccept;
	private JButton btnCancel;

	//Constants to make easier remember each level
	static final int EASY=0;
	static final int MEDIUM=1;
	static final int HARD=2;
	
	public Edit(Game inputGame)
	{
		myGame=inputGame;
		createFrame();
		rdbDificulty[EASY].setSelected(true);
	}
	
	private void createFrame()
	{
		//Create the object that we need for show the preferences frame
		frEdit=new JFrame("Edit");
		JPanel pnlEdit=new JPanel();
		btnAccept=new JButton("Accept"); //Buttons to control if the user want to save the changes or not
		btnCancel=new JButton("Cancel");
		GridLayout gl = new GridLayout(4,2,0,0);
		pnlEdit.setLayout(gl);
		rdbDificulty=new JRadioButton[3];
		JLabel [] lblDificulty=new JLabel[3];
		lblDificulty[EASY]=new JLabel("Easy");
		lblDificulty[MEDIUM]=new JLabel("Medium");
		lblDificulty[HARD]=new JLabel("Hard");
		ButtonGroup bgDificulty=new ButtonGroup();
		for(int i=0;i<3;++i)
		{
			rdbDificulty[i]=new JRadioButton();
			bgDificulty.add(rdbDificulty[i]);
			pnlEdit.add(rdbDificulty[i]);
			pnlEdit.add(lblDificulty[i]);
		}
		btnAccept.addActionListener(this);
		btnCancel.addActionListener(this);
		pnlEdit.add(btnAccept);
		pnlEdit.add(btnCancel);
		frEdit.setContentPane(pnlEdit);
		frEdit.setSize(200,150);
		frEdit.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent evt)
	{
		myGame.finishEdit(); //Say to tha game that we finish editing
		if(btnAccept==evt.getSource())
			saveChanges();
		frEdit.setVisible(false);
		frEdit=null;
	}
	
	private void saveChanges()
	{
		int i=0;
		boolean find=false;
		while(i<3 && !find)
		{
			if(rdbDificulty[i].isSelected())
			{
				switch(i)
				{
					case EASY: 	myGame.setLevel(EASY);
									myGame.setSize(10);
									break;
					case MEDIUM:myGame.setLevel(MEDIUM);
									myGame.setSize(15);
									break;
					case HARD: 	myGame.setLevel(HARD);
									myGame.setSize(20);
									break;
				}
				find=true;
			}
			++i;
		}
	}
}
