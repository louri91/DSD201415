package cliente;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JScrollPane;

public class PrivateGUI extends JFrame {
	private JTextField send;
	private JTextArea mensajes;
	private JPanel panel;
	private JButton btnEnviar;
	private JScrollPane scrollPane;

	public PrivateGUI(Client cliente, String nombreConversacion) {
		getContentPane().setLayout(null);
		this.setBounds(0, 0, 450, 530);
		String nombreConver = nombreConversacion;

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Mensajes",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(6, 6, 438, 476);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(18, 26, 401, 352);
		panel.add(scrollPane);

		mensajes = new JTextArea();
		scrollPane.setViewportView(mensajes);
		mensajes.setEditable(false);

		send = new JTextField();
		send.setBounds(18, 390, 284, 64);
		this.addWindowListener( new WindowAdapter() {
		    public void windowOpened( WindowEvent e ){
		        send.requestFocus();
		    }
		}); 
		panel.add(send);
		send.setColumns(10);

		btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String aux = send.getText().toString();
				if (!aux.isEmpty()) {
					cliente.enviarPrivado(nombreConver);
				}
			}
		});
		btnEnviar.setBounds(314, 407, 117, 29);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		getRootPane().setDefaultButton(btnEnviar);
		panel.add(btnEnviar);
		this.setResizable(false);
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
