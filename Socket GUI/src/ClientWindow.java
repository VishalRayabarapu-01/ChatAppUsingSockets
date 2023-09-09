import java.awt.event.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.*;
public class ClientWindow {
	String socketToServer;
	private JFrame frame;
	private JTextField textField;
	private JTextArea txtrField;
	private final JSeparator separator = new JSeparator();
	private JButton btnExit;
	private boolean btnexit=false,submitOk=false;
	String tArea="";
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientWindow window = new ClientWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClientWindow() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame("Chat Application");
		frame.setBounds(100, 100, 592, 725);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 25));
		textField.setBounds(12, 614, 411, 51);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				socketToServer=textField.getText();
				submitOk=true;
				/*StringBuffer add= new StringBuffer();
				for(int i=0;i<56;i++) {
					add.append(" ");
				}
				add.append(socketToServer);
				tArea=tArea+"\n "+add;*/
				tArea=tArea+"\n Me: "+socketToServer;
				txtrField.setText(tArea);
				textField.setText("");
			}
		});
		btnSend.setFont(new Font("Tahoma", Font.PLAIN, 23));
		btnSend.setBounds(435, 622, 110, 43);
		btnSend.setFocusable(false);
		frame.getContentPane().add(btnSend);

		txtrField = new JTextArea();
		txtrField.setFont(new Font("Segoe UI Semibold", Font.BOLD, 21));
		txtrField.setBounds(19, 109, 526, 477);
		txtrField.setEditable(false);
		frame.getContentPane().add(txtrField);

		JLabel lblClient = new JLabel("Client Chat :");
		lblClient.setFont(new Font("Tahoma", Font.BOLD, 33));
		lblClient.setBounds(23, 13, 215, 58);
		frame.getContentPane().add(lblClient);
		separator.setBounds(0, 80, 574, 16);
		frame.getContentPane().add(separator);

		btnExit = new JButton("EXIT");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnexit=true;
			}
		});
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 23));
		btnExit.setFocusable(false);
		btnExit.setBounds(423, 28, 110, 43);
		frame.getContentPane().add(btnExit);
		new ClientClass();
	}

	public class ClientClass {
		ClientClass() {
			//System.out.println("Waiting For Connection..");
			JOptionPane.showMessageDialog(frame, "Waiting For Connection", "Info", JOptionPane.INFORMATION_MESSAGE);
			//System.out.println("Connected...");
			
			try {
				
				InetAddress ip = InetAddress.getLocalHost();
				//System.out.println(ip.getHostAddress());
				Socket cs = new Socket(ip.getHostAddress(), 9999);
				JOptionPane.showMessageDialog(frame, "Connected to Server", "Success", JOptionPane.INFORMATION_MESSAGE);
				new Reading(cs);
				new Writing(cs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	class Writing implements Runnable{
		Socket socket;
		Thread th;
		public Writing(Socket cs) {
			socket=cs;
			th=new Thread(this,"Write");
			th.start();
		}
		public void run() {
			try{
				PrintWriter sw = new PrintWriter(socket.getOutputStream(), true);
				while (true) {
					if(submitOk){
						sw.println(socketToServer);
						submitOk=false;
					}
					if (btnexit){
						socket.close();
						frame.dispose();
						System.exit(0);
					}
				}

			}catch(Exception e){
				JOptionPane.showMessageDialog(frame, "Error "+e,"Error",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	class Reading implements Runnable {
		Socket sclient;
		Thread t;
		BufferedReader br;

		public Reading(Socket s) throws Exception {
			sclient = s;
			t = new Thread(this, "read");
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			t.start();
		}

		public void run() {
			String string = "";
			try {
				while (true) {
					string = br.readLine();
					if (btnexit){
						sclient.close();
						br.close();
						frame.dispose();
						System.exit(0);
					}
					if(!string.equals(null)){
						tArea=tArea+"\n Server: "+string;
						txtrField.setText(tArea);
					}
					// System.out.println("From Client:" + string);

				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frame, "Error " + e, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}