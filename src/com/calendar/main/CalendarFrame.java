package com.calendar.main;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Clock;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.calendar.db.ConnectionManager;
import com.calendar.detail.CalendarDetail;

public class CalendarFrame extends JFrame {
	CalendarDetail calendarDetail;
	CalendarFrame calendarFrame = this;

	JPanel contentPane;
	JTable dayViewTable;
	JLabel label;
	JButton leftButton;
	JButton rightButton;
	JTable weekBarTable;
	JButton todayButton;
	JButton addButton;
	Thread clockThread;

	boolean focus = true;
	int todayY;
	int todayM;
	int todayD;
	Calendar calendar = Calendar.getInstance();
	DefaultTableModel weekTable = new DefaultTableModel(0, 7);
	DefaultTableModel dayTable = new DefaultTableModel(6, 7);
	private JLabel console;
	Connection con;
	public ConnectionManager connectionManager;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CalendarFrame frame = new CalendarFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public CalendarFrame() {
		connectionManager = new ConnectionManager();
		con = connectionManager.getConnection();

		setResizable(false);
		setFont(new Font("굴림", Font.PLAIN, 12));
		setTitle("Calendar");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(530, 530);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				do_contentPane_mouseWheelMoved(arg0);
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// 요일 표시 테이블
		String[] weekColumns = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
		weekTable.addRow(weekColumns);

		// 가운데 정렬 renderer
		DefaultTableCellRenderer center = new DefaultTableCellRenderer();
		center.setHorizontalAlignment(JLabel.CENTER);

		// 일요일 빨간색
		DefaultTableCellRenderer sunRed = new DefaultTableCellRenderer();
		sunRed.setForeground(Color.WHITE);
		sunRed.setBackground(Color.RED);
		sunRed.setHorizontalAlignment(JLabel.CENTER);

		// 토요일 파란색
		DefaultTableCellRenderer satBlue = new DefaultTableCellRenderer();
		satBlue.setForeground(Color.WHITE);
		satBlue.setBackground(Color.BLUE);
		satBlue.setHorizontalAlignment(JLabel.CENTER);

		label = new JLabel("");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		label.setBounds(70, 10, 384, 54);
		contentPane.add(label);

		DefaultTableCellRenderer top = new DefaultTableCellRenderer();
		top.setVerticalAlignment(SwingConstants.TOP);

		// 날짜 일요일 빨간색
		DefaultTableCellRenderer sunDateRed = new DefaultTableCellRenderer();
		sunDateRed.setForeground(Color.RED);
		sunDateRed.setVerticalAlignment(SwingConstants.TOP);

		// 날짜 토요일 파란색
		DefaultTableCellRenderer satDateBlue = new DefaultTableCellRenderer();
		satDateBlue.setForeground(Color.BLUE);
		satDateBlue.setVerticalAlignment(SwingConstants.TOP);

		leftButton = new JButton("◀");
		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_leftButton_actionPerformed(e);
			}
		});
		leftButton.setFont(new Font("굴림", Font.BOLD, 20));
		leftButton.setBounds(12, 17, 76, 46);

		// 버튼 투명화
		leftButton.setOpaque(false);
		leftButton.setContentAreaFilled(false);
		leftButton.setBorderPainted(false);
		leftButton.setFocusable(false);
		contentPane.add(leftButton);

		rightButton = new JButton("▶");
		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_rightButton_actionPerformed(e);
			}
		});
		rightButton.setFont(new Font("굴림", Font.BOLD, 20));
		rightButton.setBounds(436, 17, 76, 46);
		rightButton.setOpaque(false);
		rightButton.setContentAreaFilled(false);
		rightButton.setBorderPainted(false);
		rightButton.setFocusable(false);
		contentPane.add(rightButton);

		todayButton = new JButton("Today");
		todayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_todayButton_actionPerformed(e);
			}
		});
		todayButton.setBackground(SystemColor.control);
		todayButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		todayButton.setBounds(377, 61, 79, 23);
		todayButton.setOpaque(false);
		todayButton.setBorderPainted(false);
		todayButton.setFocusable(false);
		contentPane.add(todayButton);

		addButton = new JButton("Detail");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_addButton_actionPerformed(e);
			}
		});
		addButton.setBackground(SystemColor.control);
		addButton.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		addButton.setBounds(442, 61, 80, 23);
		addButton.setOpaque(false);
		addButton.setFocusable(false);
		addButton.setBorderPainted(false);
		contentPane.add(addButton);

		weekBarTable = new JTable(weekTable) {
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};

		weekBarTable.setEnabled(false);
		weekBarTable.setRowSelectionAllowed(false);
		weekBarTable.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		weekBarTable.setRowHeight(25);
		weekBarTable.setFillsViewportHeight(true);
		weekBarTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		weekBarTable.setBounds(12, 85, 500, 23);
		weekBarTable.setBackground(Color.WHITE);

		weekBarTable.getColumnModel().getColumn(1).setCellRenderer(center);
		weekBarTable.getColumnModel().getColumn(2).setCellRenderer(center);
		weekBarTable.getColumnModel().getColumn(3).setCellRenderer(center);
		weekBarTable.getColumnModel().getColumn(4).setCellRenderer(center);
		weekBarTable.getColumnModel().getColumn(5).setCellRenderer(center);
		weekBarTable.getColumnModel().getColumn(0).setCellRenderer(sunRed);
		weekBarTable.getColumnModel().getColumn(6).setCellRenderer(satBlue);
		contentPane.add(weekBarTable);

		// 날짜 표시 테이블
		dayViewTable = new JTable(dayTable) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			};

		};
		dayViewTable.setCellSelectionEnabled(true);
		dayViewTable.setColumnSelectionAllowed(true);
		dayViewTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				do_dayViewTable_mouseClicked(e);
			}
		});
		dayViewTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		dayViewTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		dayViewTable.setRowHeight(60);
		dayViewTable.setFillsViewportHeight(true);
		dayViewTable.setBounds(12, 107, 500, 360);
		dayViewTable.getColumnModel().getColumn(0).setCellRenderer(top);
		dayViewTable.getColumnModel().getColumn(1).setCellRenderer(top);
		dayViewTable.getColumnModel().getColumn(2).setCellRenderer(top);
		dayViewTable.getColumnModel().getColumn(3).setCellRenderer(top);
		dayViewTable.getColumnModel().getColumn(4).setCellRenderer(top);
		dayViewTable.getColumnModel().getColumn(5).setCellRenderer(top);
		dayViewTable.getColumnModel().getColumn(6).setCellRenderer(top);
		dayViewTable.getColumnModel().getColumn(0).setCellRenderer(sunDateRed);
		dayViewTable.getColumnModel().getColumn(6).setCellRenderer(satDateBlue);
		contentPane.add(dayViewTable);

		console = new JLabel("");
		console.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		console.setHorizontalAlignment(SwingConstants.RIGHT);
		console.setBounds(12, 472, 500, 15);
		contentPane.add(console);

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					digitalClock();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		todayY = calendar.get(Calendar.YEAR);
		todayM = calendar.get(Calendar.MONTH);
		todayD = calendar.get(Calendar.DATE);

		// 실행시 날짜 뿌리면서 오늘로 포커싱

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				connectionManager.disconnect(con);
				System.exit(0);
			}
		});
		reloadCalendar();
		todayFocus();
	}

	// 달력 소스
	public void reloadCalendar() {
		label.setText((calendar.get(Calendar.YEAR) + "년  ") + (calendar.get(Calendar.MONTH) + 1) + "월  "
				/*+ (calendar.get(Calendar.DATE) + "일")*/);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		
		String s_year = calendar.get(Calendar.YEAR) + "";
		String s_month = (calendar.get(Calendar.MONTH) + 1) + "";
		String s_day = "";
		if (s_month.length() == 1) {
			s_month = "0" + s_month;
		}
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				dayViewTable.setValueAt("", i, j);
			}
		}
		
		int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int day = 1, row = 0, col = dayWeek - 1; day < endDay + 1; day++, col++) {
			if (col % 7 == 0) {
				col = 0;
				row += 1;
			}
			if (day < 10) {
				s_day = "0" + day;
			} else {
				s_day = "" + day;
			}
			int count = checkDB(s_year + s_month + s_day);
			if (count > 0) {
				dayViewTable.setValueAt("" + day + " 일정 : " + count, row, col);
			} else {
				dayViewTable.setValueAt("" + day, row, col);
			}
		}
	}

	// < 버튼 입력시
	protected void do_leftButton_actionPerformed(ActionEvent e) {
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - 1, calendar.get(Calendar.DATE));
		reloadCalendar();
	}

	// > 버튼 입력시
	protected void do_rightButton_actionPerformed(ActionEvent e) {
		calendar.set(calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH) + 1,
						calendar.get(Calendar.DATE));
		reloadCalendar();
	}

	// 마우스 휠 구현
	protected void do_contentPane_mouseWheelMoved(MouseWheelEvent arg0) {
		int notches = arg0.getWheelRotation();
		if (notches < 0) {
			do_leftButton_actionPerformed(null);
		} else {
			do_rightButton_actionPerformed(null);
		}
	}

	// 오늘 버튼 클릭
	protected void do_todayButton_actionPerformed(ActionEvent e) {
		calendar.set(todayY, todayM, todayD);
		reloadCalendar();
		todayFocus();
	}

	// 오늘로 포커싱 하기
	protected void todayFocus() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				if (dayViewTable.getValueAt(i, j).equals("" + todayD)) {
					dayViewTable.changeSelection(i, j, false, false);
					dayViewTable.setCellSelectionEnabled(true);
					dayViewTable.requestFocus();
					// dayViewTable.focus
				}
			}
		}
	}

	// 더블클릭 이벤트
	protected void do_dayViewTable_mouseClicked(MouseEvent e) {
		focus = true;
		if (e.getClickCount() == 2) {
			addSchedule();
		}
	}

	// 일정추가 버튼
	protected void do_addButton_actionPerformed(ActionEvent e) {
		String date = returndate();
		if (focus == true) {
			if (date != "")
				calendarDetail = new CalendarDetail(this, date, true);
		} else {
			JOptionPane.showMessageDialog(this, "날짜를 선택해 주세요");
		}

	}

	// 일정추가 메서드
	protected void addSchedule() {
		String sqlInput = returndate();
		if (!sqlInput.equals("")) {
			CalendarAdd calendarAdd = new CalendarAdd(this, ModalityType.APPLICATION_MODAL, sqlInput, this);
			calendarAdd.setLocationRelativeTo(this);
			calendarAdd.setVisible(true);
		}
	}

	public String returndate() {
		String date = "";
		int row = dayViewTable.getSelectedRow();
		int col = dayViewTable.getSelectedColumn();
		String day = dayViewTable.getValueAt(row, col).toString();
		String[] array = day.split(" ");
		String sqlYear = calendar.get(Calendar.YEAR) + "";
		String sqlMonth = (calendar.get(Calendar.MONTH) + 1) + "";
		String sqlDay = array[0].trim();

		if (sqlMonth.length() == 1) {
			sqlMonth = "0" + sqlMonth;
		}
		if (sqlDay.length() == 1) {
			sqlDay = "0" + sqlDay;
		}
		date = sqlYear + sqlMonth + sqlDay;
		return date;
	}

	public int checkDB(String date) {
		int check = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from calendar where calendar_date = ?";

		try {
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pstmt.setString(1, date);
			rs = pstmt.executeQuery();
			rs.last();
			check = rs.getRow();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			connectionManager.closeDB(pstmt, rs);
		}
		return check;
	}
	
	public void digitalClock() {
		Calendar clock = Calendar.getInstance();
		String year = clock.get(Calendar.YEAR) +"";
		String month = clock.get(Calendar.MONTH) + 1 +"";
		String date = clock.get(Calendar.DATE) +"";
		String hour = clock.get(Calendar.HOUR_OF_DAY) +"";
		String minute = clock.get(Calendar.MINUTE) +"";
		String second = clock.get(Calendar.SECOND) +"";
		
		if(hour.length() == 1) {
			hour = "0" + hour;
		}
		if(minute.length() == 1) {
			minute = "0" + minute;
		}
		if(second.length() == 1) {
			second = "0" + second;
		}
		String now = year + "년  " + month + "월  " + date + "일  " + hour + ":" + minute + ":" + second;
		console.setText(now);
	}
	
	
	public Connection getCon() {
		return con;
	}
}
