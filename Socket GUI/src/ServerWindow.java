import java.awt.Component;
import java.awt.EventQueue;
import java.net.*;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JSeparator;

class Frame extends JFrame {
	private JTextField textField;
	JButton btnExit;
	JTextArea txtrField;
	String socWriting;
	String tArea1 = "";
	String lenOfTxtArea="..........nsgdyujwsdhgsyuiwjdfhgewuijfdeiuwodjfiujeuids";
	public boolean btnexit = false, submitOk = false;

	Frame() {
		super("Chat Application");
		setBounds(100, 100, 592, 725);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setVisible(true);

		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 30));
		textField.setBounds(12, 614, 411, 51);
		getContentPane().add(textField);
		textField.setColumns(10);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				socWriting = textField.getText();
				submitOk = true;
				/*StringBuffer add= new StringBuffer();
				for(int i=0;i<(lenOfTxtArea.length() - socWriting.length());i++){
					add.append(" ");
				}
				add.append(socWriting);
				tArea1 = tArea1 + "\n " + add;
				txtrField.setText(tArea1);*/
				
				tArea1=tArea1+"\n Me: "+socWriting;
				txtrField.setText(tArea1);
				textField.setText("");
			}
		});
		btnSend.setFont(new Font("Tahoma", Font.PLAIN, 29));
		btnSend.setBounds(435, 622, 110, 43);
		btnSend.setFocusable(false);
		getContentPane().add(btnSend);

		txtrField = new JTextArea();
		txtrField.setFont(new Font("Segoe UI Semibold", Font.BOLD, 21));
		txtrField.setBounds(19, 109, 526, 477);
		txtrField.setEditable(false);
		getContentPane().add(txtrField);

		btnExit = new JButton("EXIT");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnexit = true;
			}
		});
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 23));
		btnExit.setFocusable(false);
		btnExit.setBounds(416, 37, 110, 43);
		getContentPane().add(btnExit);

		JLabel lblServerChat = new JLabel("Server Chat :");
		lblServerChat.setFont(new Font("Tahoma", Font.BOLD, 33));
		lblServerChat.setBounds(29, 25, 230, 58);
		getContentPane().add(lblServerChat);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 95, 574, 16);
		getContentPane().add(separator);

		new ServerClass();
	}

	class socketReading implements Runnable {
		Socket s;
		Thread t;
		BufferedReader br;

		public socketReading(Socket s) throws Exception {
			this.s = s;
			t = new Thread(this, "read");
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			t.start();
		}

		public void run() {
			String string = "";
			try {
				while (true) {
					string = br.readLine();
					if (btnexit) {
						s.close();
						br.close();
						Frame.this.dispose();
						System.exit(0);
					}
					if(!string.equals(null)){
						tArea1 = tArea1 + "\n Client: " + string;
						// System.out.println("From Client:" + string);
						txtrField.setText(tArea1);
					}
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(Frame.this, "Error " + e, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class socketWriting implements Runnable {
		Socket socket;
		Thread t;

		public socketWriting(Socket cs) {
			socket = cs;
			t = new Thread(this, "Write");
			t.start();
		}

		public void run() {
			try {
				PrintWriter sw = new PrintWriter(socket.getOutputStream(), true);
				while (true) {
					if (submitOk) {
						sw.println(socWriting);
						submitOk = false;
					}
					if (btnexit) {
						socket.close();
						Frame.this.dispose();
						System.exit(0);
					}
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(Frame.this, "Error " + e, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class ServerClass {
		ServerClass() {
			try {
				// System.out.println("Server Waiting for Connection...");
				ServerSocket ss = new ServerSocket(9999);
				Socket cs = ss.accept();
				JOptionPane.showMessageDialog(Frame.this, "Connected TO CLient", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				// System.out.println("Conected to CLient.");
				new socketReading(cs);
				new socketWriting(cs);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(Frame.this, "Error " + e, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}

public class ServerWindow {

	public static void main(String[] args) {
		Frame frame = new Frame();
	}
}