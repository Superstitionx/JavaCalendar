package com.calendar.detail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLInput;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import com.calendar.main.CalendarFrame;
import com.calendar.main.CalendarTable;

public class CalendarDetail extends JFrame {

	JPanel p_north, p_center;
	JButton bt_modify, bt_delete, bt_order;
	JTable table;
	JScrollPane scroll;

	String selectDate;
	boolean flag = false;
	int row;
	boolean focus = false;
	CalendarFrame calendarFrame;
	CalendarTable calendarTable;

	public CalendarDetail(CalendarFrame calendarFrame, String date, boolean flag) {
		this.flag = flag;
		this.setTitle(date + " 일정 list");
		this.calendarFrame = calendarFrame;
		selectDate = date;
		p_north = new JPanel();
		p_center = new JPanel();
		bt_order = new JButton("정렬");
		bt_modify = new JButton("수정");
		bt_delete = new JButton("삭제");
		table = new JTable();
		scroll = new JScrollPane(table);

		p_north.setLayout(new BorderLayout());
		p_north.setPreferredSize(new Dimension(400, 400));
		p_north.setBackground(Color.WHITE);
		p_center.setPreferredSize(new Dimension(400, 130));
		p_center.setLayout(null);
		p_center.setBackground(Color.WHITE);

		bt_order.setBounds(60, 60, 80, 30);
		bt_order.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		bt_order.setBackground(SystemColor.control);
		bt_modify.setBounds(170, 60, 80, 30);
		bt_modify.setBackground(SystemColor.control);
		bt_modify.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		bt_delete.setBounds(280, 60, 80, 30);
		bt_delete.setBackground(SystemColor.control);
		bt_delete.setFont(new Font("맑은 고딕", Font.PLAIN, 12));

		scroll.getViewport().setBackground(Color.white);
		
		p_north.add(scroll);
		p_center.add(bt_order);
		p_center.add(bt_modify);
		p_center.add(bt_delete);

		table.setPreferredScrollableViewportSize(new Dimension(360, 390));
		table.setBackground(Color.WHITE);
		table.getTableHeader().setBackground(Color.WHITE);
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		table.setRowHeight(30);
		this.add(p_north, BorderLayout.NORTH);
		this.add(p_center);
		this.setLocation(calendarFrame.getX() + calendarFrame.getWidth(), calendarFrame.getY());
		this.setSize(400, 530);
		this.setResizable(false);
		this.setVisible(this.flag);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				row = table.getSelectedRow();
				int selectedRow = chooseRow(row);
				focus = true;
				if (e.getClickCount() == 2) {
					modifyDialog(row, selectedRow);
				}
			}
		});

		bt_order.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectSchedule(2);
			}
		});
		
		bt_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "삭제 하시겠습니까??");
				if (result == JOptionPane.OK_OPTION) {
					deleteSchedule(row);
					selectSchedule(1);
					calendarFrame.reloadCalendar();
				}
			}
		});

		bt_modify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (focus != false) {
					int selectedRow = chooseRow(row);
					modifyDialog(row, selectedRow);
				} else {
					JOptionPane.showMessageDialog(null, "수정할 일정을 선택해 주세요");
				}
			}
		});

		if (selectDate != "") {
			selectSchedule(1);
		}
	}

	public void selectSchedule(int x) {
		Connection con = calendarFrame.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsm = null;
		
		String sql = "select calendar_title, calendar_importance from calendar where calendar_date = ?";
		String sql2 = "select calendar_title, calendar_importance from calendar where calendar_date = ? order by calendar_importance asc";
		try {
			if(x == 1) {
				pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			}else {
				pstmt = con.prepareStatement(sql2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			}
			pstmt.setString(1, selectDate);
			rs = pstmt.executeQuery();
			rsm = rs.getMetaData();
			String[] columnName = new String[rsm.getColumnCount() + 1];
			for (int i = 0; i < columnName.length; i++) {
				if (i == 0)
					columnName[i] = "번호";
				if (i == 1)
					columnName[i] = "제목";
				if (i == 2)
					columnName[i] = "중요도";
			}
			rs.last();
			int row = rs.getRow();
			Object[][] data = null;
			if (row > 0) {
				data = new Object[row][columnName.length];
				rs.beforeFirst();
				for (int i = 0; i < row; i++) {
					rs.next();
					for (int j = 0; j < columnName.length; j++) {
						if (j == 0) {
							data[i][j] = (Integer) (i + 1);
						} else if (j == 2) {
							if(rs.getInt(2) == 0) {
								data[i][j] = "상";
							}else if(rs.getInt(2) == 1) {
								data[i][j] = "중";
							}else {
								data[i][j] = "하";
							}
						} else {
							data[i][j] = rs.getString(j);
						}
					}
				}
			} else {
				JOptionPane.showMessageDialog(calendarFrame, "해당 날짜는 일정이 없습니다.");
				data = new Object[0][0];
			}
			calendarTable = new CalendarTable();
			calendarTable.columnName = columnName;
			calendarTable.data = data;
			table.setModel(calendarTable);
			
			DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
			cellCenter.setHorizontalAlignment(JLabel.CENTER);
			table.getColumnModel().getColumn(0).setPreferredWidth(30);
			table.getColumnModel().getColumn(1).setPreferredWidth(270);
			table.getColumnModel().getColumn(2).setPreferredWidth(60);
			table.getColumnModel().getColumn(0).setCellRenderer(cellCenter);
			table.getColumnModel().getColumn(1).setCellRenderer(cellCenter);
			table.getColumnModel().getColumn(2).setCellRenderer(cellCenter);
			//table.updateUI();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			calendarFrame.connectionManager.closeDB(pstmt, rs);
		}
	}

	public int chooseRow(int row) {
		int calendar_id = 0;
		Connection con = calendarFrame.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select calendar_id from calendar where calendar_title = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, (String) table.getValueAt(row, 1));
			rs = pstmt.executeQuery();
			if(!rs.next()) {
				System.out.println("안됨...");
				return 0;
			}
			calendar_id = rs.getInt("calendar_id");
			System.out.println(calendar_id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return calendar_id;
	}

	public void deleteSchedule(int row) {
		Connection con = calendarFrame.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "delete from calendar where calendar_date = ? and calendar_title = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, selectDate);
			pstmt.setString(2, (String) table.getValueAt(row, 1));
			rs = pstmt.executeQuery();
			table.updateUI();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			calendarFrame.connectionManager.closeDB(pstmt, rs);
		}
	}

	public void modifyDialog(int row, int selectedRow) {
		CalendarUpdate calendarUpdate = new CalendarUpdate(ModalityType.APPLICATION_MODAL, selectDate, this, row, selectedRow);
		calendarUpdate.setLocationRelativeTo(this);
		calendarUpdate.setVisible(true);
	}

}
