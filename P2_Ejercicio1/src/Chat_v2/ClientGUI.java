package Chat_v2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
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

import javax.swing.JScrollPane;

public class ClientGUI extends JFrame {
	private JTextField mensaje;
	private JTextArea listaMensajes;
	private JTextArea clientesConectados;
	private JButton btnEnviar;
	private JButton btnConectar;
	private JButton btnDesconectar;
	private JButton btnSalir;
	private JPanel panel;
	private JPanel panel_1;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;

	public ClientGUI(Client cliente) {
		this.setBounds(0, 0, 763, 594);
		setResizable(false);
		getContentPane().setLayout(null);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Mensajes",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(0, 6, 591, 460);
		panel.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 22, 557, 421);
		panel.add(scrollPane);

		listaMensajes = new JTextArea();
		scrollPane.setViewportView(listaMensajes);
		listaMensajes.setBackground(SystemColor.menu);
		listaMensajes.setEditable(false);

		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Usuarios Conectados",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(591, 6, 171, 461);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(16, 21, 138, 421);
		panel_1.add(scrollPane_1);

		clientesConectados = new JTextArea();
		scrollPane_1.setViewportView(clientesConectados);
		clientesConectados.setBackground(SystemColor.menu);
		clientesConectados.setEditable(false);

		mensaje = new JTextField();
		mensaje.setBounds(18, 494, 472, 48);
		mensaje.setColumns(10);
		this.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				mensaje.requestFocus();
			}
		});
		getContentPane().add(mensaje);

		btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!mensaje.getText().isEmpty()) {
					cliente.enviar();
					getRootPane().setNextFocusableComponent(mensaje);
				}
				panel.repaint();
			}
		});
		btnEnviar.setBounds(503, 505, 82, 29);
		btnEnviar.setEnabled(false);
		getContentPane().add(btnEnviar);

		btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String auxiliar = mensaje.getText();
				auxiliar = auxiliar.replaceAll("\\s+", "");
				auxiliar.toUpperCase();
				if (auxiliar.isEmpty()) {
					listaMensajes
							.setText("Tienes que especificar primero tu nickname\n");
				} else {
					cliente.conectar();
					getRootPane().setNextFocusableComponent(mensaje);
				}
				panel.repaint();
			}
		});
		btnConectar.setBounds(621, 474, 117, 29);
		getContentPane().add(btnConectar);
		getRootPane().setDefaultButton(btnConectar);

		btnDesconectar = new JButton("Desconectar");
		btnDesconectar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getRootPane().setDefaultButton(btnConectar);
				cliente.desconectar();
				panel.repaint();
			}
		});
		btnDesconectar.setEnabled(false);
		btnDesconectar.setBounds(621, 505, 117, 29);
		getContentPane().add(btnDesconectar);

		btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnDesconectar.isEnabled()) {
					cliente.desconectar();
					System.exit(0);
				} else {
					System.exit(0);
				}
			}
		});
		btnSalir.setBounds(621, 537, 117, 29);
		getContentPane().add(btnSalir);
		getContentPane().add(panel);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
	}

	public JTextArea getListaMensajes() {
		return listaMensajes;
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
