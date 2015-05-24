package Chat_v3Privado;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



public class PrivateGUI extends JFrame {
	private JTextField send;
	private JTextArea mensajes;
	private JPanel panel;
	private JButton btnEnviar;
	
	public PrivateGUI(Client cliente) {
		getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Mensajes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(6, 6, 438, 476);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		mensajes = new JTextArea();
		mensajes.setBounds(18, 26, 401, 352);
		panel.add(mensajes);
		
		send = new JTextField();
		send.setBounds(18, 390, 284, 64);
		panel.add(send);
		send.setColumns(10);
		
		btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cliente.enviar();
			}
		});
		btnEnviar.setBounds(314, 407, 117, 29);
		panel.add(btnEnviar);
		
		this.setVisible(true);
	}

	public JTextField getSend() {
		return send;
	}

	public JTextArea getMensajes() {
		return mensajes;
	}

	public JPanel getPanel() {
		return panel;
	}

	public JButton getBtnEnviar() {
		return btnEnviar;
	}
	
}
