package org.hedspi.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.jms.JMSException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JTextField txtMembers;
	private Presenter presenter;

	/**
	 * Create the frame.
	 */
	public LoginFrame(final Presenter presenter) {
		this.presenter = presenter;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.activeCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		final JPanel panel = new JPanel();
		panel.setBackground(SystemColor.activeCaption);
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = txtUsername.getText();
				String members = txtMembers.getText();
				try {
					presenter.login(username, members);
				} catch (JMSException e1) {
					JOptionPane.showMessageDialog(LoginFrame.this, "Connection fail! Please try again.");
					e1.printStackTrace();
				}
			}
		});
		btnSubmit.setBorder(UIManager.getBorder("Button.border"));
		btnSubmit.setBackground(Color.GREEN);
		btnSubmit.setBounds(168, 159, 89, 31);
		panel.add(btnSubmit);

		txtUsername = new JTextField();
		txtUsername.setBounds(168, 49, 170, 31);
		panel.add(txtUsername);
		txtUsername.setColumns(10);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(96, 57, 69, 14);
		panel.add(lblUsername);

		JLabel lblMembers = new JLabel("Members");
		lblMembers.setBounds(96, 108, 69, 14);
		panel.add(lblMembers);

		txtMembers = new JTextField();
		txtMembers.setBounds(168, 100, 170, 31);
		panel.add(txtMembers);
		txtMembers.setColumns(10);
	}
}
