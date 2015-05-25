package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;

import java.awt.SystemColor;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class ClientGUI extends JFrame{
	private JTextField mensaje;
	private JTextArea clientesConectados;
	private JButton btnEnviar;
	private JButton btnConectar;
	private JButton btnDesconectar;
	private JButton btnSalir;
	private JPanel panel_1;
	
	public ClientGUI(Send cliente) {
		this.setBounds(0,0,763,594);
		setResizable(false);
		getContentPane().setLayout(null);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Usuarios Conectados", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(18, 50, 728, 417);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		clientesConectados = new JTextArea();
		clientesConectados.setBounds(16, 21, 693, 373);
		panel_1.add(clientesConectados);
		clientesConectados.setBackground(SystemColor.menu);
		clientesConectados.setEditable(false);
		
		mensaje = new JTextField();
		mensaje.setBounds(18, 494, 472, 48);
		getContentPane().add(mensaje);
		mensaje.setColumns(10);
		
		btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener()
	    {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!mensaje.getText().isEmpty()){
					try {
						cliente.conectarClientesPrivado();
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					getRootPane().setNextFocusableComponent(mensaje);
				}
			}
	    });
		btnEnviar.setBounds(503, 505, 82, 29);
		btnEnviar.setEnabled(false);
		getContentPane().add(btnEnviar);
		
		btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener()
	    {
			@Override
			public void actionPerformed(ActionEvent e) {
				String auxiliar = mensaje.getText();
				auxiliar = auxiliar.replaceAll("\\s+", "");
				auxiliar.toUpperCase();
				if(auxiliar.isEmpty()){
					JOptionPane.showMessageDialog(null, "Tienes que especificar primero tu nickname y pulsar Conectar", "InfoBox: " + "Error", JOptionPane.INFORMATION_MESSAGE);
				}else{
				try {
					cliente.conectar();
				} catch (RemoteException | MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				getRootPane().setNextFocusableComponent(mensaje);
				}
			}
	    });
		btnConectar.setBounds(621, 474, 117, 29);
		getContentPane().add(btnConectar);
		getRootPane().setDefaultButton(btnConectar);

		
		btnDesconectar = new JButton("Desconectar");
		btnDesconectar.addActionListener(new ActionListener()
	    {
			@Override
			public void actionPerformed(ActionEvent e) {
				getRootPane().setDefaultButton(btnConectar);
				cliente.desconectar();
			}
	    });
		btnDesconectar.setEnabled(false);
		btnDesconectar.setBounds(621, 505, 117, 29);
		getContentPane().add(btnDesconectar);
		
		btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cliente.desconectar();
				System.exit(0);
			}
		});
		btnSalir.setBounds(621, 537, 117, 29);
		getContentPane().add(btnSalir);
		
		JLabel lblIndiqueConQu = new JLabel("Indique con qué usuario se quiere comunicar");
		lblIndiqueConQu.setBounds(18, 22, 728, 16);
		getContentPane().add(lblIndiqueConQu);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
	}

	public JTextField getMensaje() {
		return mensaje;
	}

	public JTextArea getClientesConectados() {
		return clientesConectados;
	}

	public JButton getBtnConectar() {
		return btnConectar;
	}	

	public JButton getBtnDesconectar() {
		return btnDesconectar;
	}

	public JButton getBtnEnviar() {
		return btnEnviar;
	}
}
