package com.calendar.main;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class CalendarAdd extends JDialog {

	JPanel contentPanel = new JPanel();
	JLabel title_label, text_label, importance_label;
	Choice ch;
	JPanel p_area;
	JTextField title_area;
	JTextArea text_area;
	JScrollPane scroll;

	JButton saveButton, cancelButton;
	JLabel check_text;
	JLabel check_label;
	CalendarFrame calendarFrame;

	public static void main(String[] args) {

	}

	// 수정부분
	public CalendarAdd(Window owner, ModalityType modalityType, String sqlDay, CalendarFrame calendarFrame) {
		super(owner, modalityType);
		this.setTitle(sqlDay);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.calendarFrame = calendarFrame;
		setBounds(100, 100, 315, 303);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		title_label = new JLabel("제  목");
		title_label.setHorizontalAlignment(SwingConstants.CENTER);
		title_label.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		title_label.setBounds(12, 42, 57, 15);
		contentPanel.add(title_label);

		check_label = new JLabel("*");
		check_label.setForeground(Color.RED);
		check_label.setHorizontalAlignment(SwingConstants.CENTER);
		check_label.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		check_label.setBounds(10, 42, 14, 15);
		contentPanel.add(check_label);

		importance_label = new JLabel("중 요 도");
		importance_label.setHorizontalAlignment(SwingConstants.CENTER);
		importance_label.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		importance_label.setBounds(12, 8, 67, 15);
		contentPanel.add(importance_label);

		text_label = new JLabel("내  용");
		text_label.setHorizontalAlignment(SwingConstants.CENTER);
		text_label.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		text_label.setBounds(12, 98, 57, 15);
		contentPanel.add(text_label);

		title_area = new JTextField();
		title_area.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				do_agendaTitle_caretUpdate(arg0);
			}
		});

		ch = new Choice();
		ch.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		ch.setBounds(79, 8, 53, 10);
		ch.add("상");
		ch.add("중");
		ch.add("하");
		contentPanel.add(ch);
	

		check_text = new JLabel("제목을 입력하세요");
		check_text.setForeground(Color.GRAY);
		check_text.setHorizontalAlignment(SwingConstants.CENTER);
		check_text.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		check_text.setBounds(167, 8, 120, 19);
		contentPanel.add(check_text);

		title_area.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		title_area.setBounds(64, 42, 223, 46);
		contentPanel.add(title_area);
		title_area.setColumns(10);

		p_area = new JPanel();
		p_area.setLayout(new BorderLayout());
		p_area.setBounds(64, 98, 223, 135);

		text_area = new JTextArea();
		text_area.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		text_area.setLineWrap(true);
		scroll = new JScrollPane(text_area);
		p_area.add(scroll);
		contentPanel.add(p_area);

		cancelButton = new JButton("취   소");
		cancelButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		cancelButton.setBounds(217, 243, 70, 23);
		contentPanel.add(cancelButton);
		cancelButton.setBackground(SystemColor.control);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_cancelButton_actionPerformed(arg0);
			}
		});
		cancelButton.setActionCommand("Cancel");

		saveButton = new JButton("저   장");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					do_saveButton_actionPerformed(e);
					if(calendarFrame.calendarDetail != null && calendarFrame.calendarDetail.isVisible() == true) {
						calendarFrame.calendarDetail.selectSchedule(1);
					}else {
						
					}
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		saveButton.setBackground(SystemColor.control);
		saveButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		saveButton.setBounds(144, 243, 70, 23);
		contentPanel.add(saveButton);
		saveButton.setEnabled(false);
	}

	protected void do_cancelButton_actionPerformed(ActionEvent arg0) {
		this.dispose();
	}

	// 일정 추가하기
	protected void do_saveButton_actionPerformed(ActionEvent e) throws ClassNotFoundException, SQLException {
		Connection connection = calendarFrame.getCon();
		if (!title_area.getText().equals("")) {
			StringBuffer sb = new StringBuffer();
			sb.append("insert into calendar (calendar_id, calendar_date, calendar_title, calendar_text, calendar_importance)");
			sb.append(" values (calendar_seq.nextval, ?, ?, ?, ?)");
			PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
			preparedStatement.setString(1, getTitle());
			preparedStatement.setString(2, title_area.getText());
			preparedStatement.setString(3, text_area.getText());
			preparedStatement.setInt(4, ch.getSelectedIndex());
			preparedStatement.executeUpdate();
			dispose();
			calendarFrame.reloadCalendar();
		}
	}

	
	protected void do_agendaTitle_caretUpdate(CaretEvent arg0) {
		if (!title_area.getText().equals("")) {
			saveButton.setEnabled(true);
			check_text.setText("");
		} else {
			saveButton.setEnabled(false);
			check_text.setText("제목을 입력해 주세요.");
		}
	}
}
