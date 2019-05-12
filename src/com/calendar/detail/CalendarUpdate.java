package com.calendar.detail;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class CalendarUpdate extends JDialog {

	private final JPanel contentPanel = new JPanel();
	JLabel title_label, text_label, importance_label;
	Choice ch;
	JPanel p_area;
	JTextField title_area;
	JTextArea text_area;
	JScrollPane scroll;
	JButton modifyButton;
	JButton cancelButton;
	
	JLabel check_text;
	JLabel check_label;
	
	CalendarDetail calendarDetail;	
	int selectedRow;
	int row;
	
	public static void main(String[] args) {
	}

	// 수정부분
	public CalendarUpdate(ModalityType modalityType, String sqlDay, CalendarDetail calendarDetail, int row, int selectedRow) {
		super(calendarDetail, modalityType);
		this.row = row;
		this.selectedRow = selectedRow;
		this.calendarDetail = calendarDetail;
		this.setTitle("수정할 일정 : " + calendarDetail.table.getValueAt(row, 1));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setResizable(false);

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
		
		ch = new Choice();
		ch.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		ch.setBounds(79, 8, 53, 10);
		ch.add("상");
		ch.add("중");
		ch.add("하");
		contentPanel.add(ch);
		
		title_area = new JTextField();
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

		cancelButton = new JButton("취소");
		cancelButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		cancelButton.setBounds(217, 243, 70, 23);
		contentPanel.add(cancelButton);
		cancelButton.setBackground(SystemColor.control);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_cancelButton_actionPerformed();
			}
		});
		cancelButton.setActionCommand("Cancel");

		modifyButton = new JButton("수정");
		modifyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_modifyButton_actionPerformed();
			}
		});
		modifyButton.setBackground(SystemColor.control);
		modifyButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		modifyButton.setBounds(144, 243, 70, 23);
		contentPanel.add(modifyButton);
		load();
	}

	protected void do_cancelButton_actionPerformed() {
		this.dispose();
	}
	
	protected void do_modifyButton_actionPerformed(){
		Connection con = calendarDetail.calendarFrame.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		sb.append("update calendar set calendar_title = ?, calendar_text = ?, calendar_importance = ? where calendar_id = ?");
		
		try {
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, title_area.getText());
			pstmt.setString(2, text_area.getText());
			pstmt.setInt(3, ch.getSelectedIndex());
			pstmt.setInt(4, selectedRow);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				JOptionPane.showMessageDialog(null, "수정 성공!!!");
				dispose();
				calendarDetail.selectSchedule(1);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			calendarDetail.calendarFrame.connectionManager.closeDB(pstmt, rs);
		}
	}
	
	public void load() {
		Connection con = calendarDetail.calendarFrame.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select calendar_title, calendar_text, calendar_importance from calendar where calendar_id = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, selectedRow);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				title_area.setText(rs.getString("calendar_title"));
				text_area.setText(rs.getString("calendar_text"));
				ch.select(rs.getInt("calendar_importance"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			calendarDetail.calendarFrame.connectionManager.closeDB(pstmt, rs);
		}
	}
}
