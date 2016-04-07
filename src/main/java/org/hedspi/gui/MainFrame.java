package org.hedspi.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.jms.JMSException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField lbGroupMember;
	private JTextArea txtMessageContainer;
	private JScrollPane scrollPane_1;
	private JTextField txtMessageInput;
	private JButton btnSend;
	private JLabel lbUsername;
	private Presenter presenter;

	/**
	 * Create the frame.
	 */
	public MainFrame(Presenter presenter) {
		this.presenter = presenter;

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 396);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		lbGroupMember = new JTextField();
		lbGroupMember.setForeground(Color.WHITE);
		lbGroupMember.setEditable(false);
		lbGroupMember.setBackground(SystemColor.activeCaption);
		lbGroupMember.setText("group chat");
		lbGroupMember.setHorizontalAlignment(SwingConstants.CENTER);
		lbGroupMember.setBounds(0, 31, 434, 25);
		panel.add(lbGroupMember);
		lbGroupMember.setColumns(10);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setBounds(0, 67, 434, 245);
		panel.add(scrollPane_1);

		txtMessageContainer = new JTextArea();
		txtMessageContainer.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtMessageContainer.setLineWrap(true);
		txtMessageContainer.setWrapStyleWord(true);
		txtMessageContainer.setEditable(false);
		scrollPane_1.setViewportView(txtMessageContainer);

		txtMessageInput = new JTextField();

		// when enter key pressed
		txtMessageInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendAction();
			}
		});
		txtMessageInput.setHorizontalAlignment(SwingConstants.LEFT);
		txtMessageInput.setBounds(0, 323, 347, 35);
		panel.add(txtMessageInput);
		txtMessageInput.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.setBackground(Color.GREEN);

		// when button SEND is clicked
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendAction();
			}
		});
		btnSend.setBounds(357, 323, 67, 35);
		panel.add(btnSend);

		lbUsername = new JLabel("Welcome ...");
		lbUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lbUsername.setForeground(Color.RED);
		lbUsername.setBounds(0, 0, 424, 25);
		panel.add(lbUsername);
	}

	private void sendAction() {
		String msg = txtMessageInput.getText().trim();
		if (msg.equals("")) {
			return;
		}

		try {
			presenter.sendMessage(msg);
		} catch (JMSException e) {
			JOptionPane.showMessageDialog(MainFrame.this, "Connection Fail! Close App then try to open again.");
			e.printStackTrace();
		}

		// clear input
		txtMessageInput.setText("");
	}

	public void setUserNameLabel(String username) {
		lbUsername.setText(username);
	}

	public void setGroupMember(String str) {
		lbGroupMember.setText("You're chatting with: " + str);
	}

	public void appendReceivedMessage(String username, String msg) {
		txtMessageContainer.append(username + ":\t" + msg + "\n\n");
	}

}
