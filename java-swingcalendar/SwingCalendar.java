/**
 * 
 */
package com.example.swingtest01;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Swing用 カレンダー コントロール
 * @author Original Sourcecode : http://pastebin.com/TvdWJsWu
 * @author Branched by : INOUE Hirokazu  http://oasis.halfmoon.jp/
 * @version 1.1
 */
public class SwingCalendar extends JPanel {

    private int selectedYear, selectedMonth, selectedDay;

    private JSpinner monthChooser = new JSpinner();
    private JSpinner yearChooser = new JSpinner();

    private JLabel[] weekLabels = new JLabel[7];
    private JLabel[][] dayLabels = new JLabel[6][7];

    private static final Color WEEK_BG = new Color(220, 220, 220);
    private static final LineBorder WEEK_BORDER = new LineBorder(new Color(200,
            200, 200), 1, false);

    private static final Color DAY_BG = new Color(250, 250, 255);
    private static final Color DAY_FG = Color.BLACK;
    private static final LineBorder DAY_BORDER = new LineBorder(new Color(240,
            240, 250), 1, false);

    private static final Color TODAY_BG = new Color(200, 200, 200);
    private static final Color TODAY_FG = new Color(0, 0, 0);
    private static final LineBorder TODAY_BORDER = new LineBorder(new Color(
            180, 180, 180), 1, false);

    private static final Dimension DAY_LABEL_SIZE = new Dimension(18, 18);
    // private static final String[] WEEK_NAMES = new String[] {"SUN", "MON",
    // "TUE", "WED", "THU", "FRI", "SAT"};
    private static final String[] WEEK_NAMES = new String[] { "日", "月", "火",
            "水", "木", "金", "土" };
    // private static final String[] MONTH_NAMES = new String[] {"JAN", "FEB",
    // "MAR", "APR", "MAY", "JUNE", "JULY", "AUG", "SEP", "OCT", "NOV", "DEC"};
    private static final String[] MONTH_NAMES = new String[] { "1月", "2月",
            "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月" };

    /**
     *  カレンダー コントロールのデフォルト コンストラクタ<br/>
     *  今日の年月日を、選択年月日の初期値とする。
     * @param title - コントロールのタイトル<br/>nullを指定するとタイトル無し
     */
    public SwingCalendar(String title) {
        // 今日の年月日を、現在の選択年月日としてSwingCalendarCoreを実行
        this(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1,
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH), title);
    }


    /**
     * カレンダー コントロールのコンストラクタ<br/>
     * 指定されたyear,month,dayを選択年月日の初期値とする
     * 
     * @param year : 選択する年
     * @param month : 選択する月 (1 ... 12)
     * @param day : 選択する日 (1 ...)
     * @param title - コントロールのタイトル<br/>nullを指定するとタイトル無し
     */
    public SwingCalendar(int year, int month, int day, String title) {
        // 引数の年月日が妥当かチェックする
        Calendar cal = Calendar.getInstance();
        cal.setLenient(false);   // 厳密モード
        cal.set(year, month, day);
        try{
            cal.getTime();
        }catch(Exception e){
            // エラーの場合は今日の年月日を再設定
            cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH) + 1;
            day = cal.get(Calendar.DAY_OF_MONTH);
        }
        // カレンダー コントロールを実際に定義・描画
        SwingCalendarCore(year, month, day, title);
    }
    

    /**
     * カレンダー コントロールを実際に定義・描画するメソッド
     * 
     * @param year : 選択する年
     * @param month : 選択する月 (1 ... 12)
     * @param day : 選択する日 (1 ...)
     * @param title - コントロールのタイトル<br/>nullを指定するとタイトル無し
     */
    private void SwingCalendarCore(int year, int month, int day, String title) {
        selectedYear = year;
        selectedMonth = month;
        selectedDay = day;
//        try {
//            UIManager
//                    .setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//        } catch (ClassNotFoundException | InstantiationException
//                | IllegalAccessException | UnsupportedLookAndFeelException e) {
//            e.printStackTrace();
//        }

        // 月選択スピナーのパネル
        monthChooser.setModel(new SpinnerListModel(MONTH_NAMES));
        JTextField tm = ((JSpinner.DefaultEditor) monthChooser.getEditor())
                .getTextField();
        tm.setHorizontalAlignment(JTextField.CENTER);
        ((JSpinner.DefaultEditor) monthChooser.getEditor()).getTextField().setEditable(false);
        monthChooser.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));

        /*
         * 年選択 JSpinner, 月選択 JSpinner の変更検知リスナー
         */
        ChangeListener monthYearListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                refreshDayLabels((int) yearChooser.getValue(),
                        getMonthIndex((String) monthChooser.getValue()));
            }
        };

        monthChooser.addChangeListener(monthYearListener);

//        Dimension dm = monthChooser.getPreferredSize();
//        dm.width += 10; // dm.height += 1;
//        monthChooser.setPreferredSize(dm);

        // 年選択スピナーのパネル
        yearChooser.setModel(new SpinnerNumberModel(2012, 0, 5000, 1));
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(yearChooser,
                "#");
        yearChooser.setEditor(editor);
        yearChooser.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        yearChooser.addChangeListener(monthYearListener);

//        Dimension dy = yearChooser.getPreferredSize();
//        dy.width += 10; // dy.height += 1;
//        yearChooser.setPreferredSize(dy);

        // 日付グリッドのパネル
        JPanel dayPanel = new JPanel();
        GridLayout layout = new GridLayout(7, 7);
        layout.setHgap(3);
        layout.setVgap(3);
        dayPanel.setLayout(layout);

        for (int i = 0; i < 7; i++) {
            weekLabels[i] = new JLabel(WEEK_NAMES[i]);
            weekLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            weekLabels[i].setVerticalAlignment(SwingConstants.CENTER);
            weekLabels[i].setPreferredSize(DAY_LABEL_SIZE);
            weekLabels[i].setOpaque(true);
            weekLabels[i].setBackground(WEEK_BG);
            weekLabels[i].setBorder(WEEK_BORDER);
            if (i == 0) {
                weekLabels[i].setForeground(new Color(160, 0, 0));  // 日曜の色
            } else if (i == 6) {
                weekLabels[i].setForeground(Color.BLUE);
            }
            weekLabels[i].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));

            dayPanel.add(weekLabels[i]);
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                dayLabels[i][j] = new JLabel();
                dayLabels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                dayLabels[i][j].setVerticalAlignment(SwingConstants.CENTER);
                dayLabels[i][j].setOpaque(true);
                dayLabels[i][j].setBackground(DAY_BG);
                dayLabels[i][j].setPreferredSize(DAY_LABEL_SIZE);
                dayLabels[i][j].setBorder(new LineBorder(Color.DARK_GRAY, 2,
                        false));
                dayLabels[i][j].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
                dayPanel.add(dayLabels[i][j]);
                // 日付がクリックされた場合の処理（選択年月日を更新し再描画する）
                dayLabels[i][j].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JLabel label = (JLabel) e.getSource();
                        // 選択年月日を更新
                        selectedYear = (int) yearChooser.getValue();
                        selectedMonth = getMonthIndex((String) monthChooser
                                .getValue()) + 1;
                        selectedDay = Integer.parseInt(label.getText());
                        // カレンダーの日付グリッドを再描画
                        refreshDayLabels(selectedYear, selectedMonth - 1);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }
                });
            }
        }

        // 年・月 選択スピナーのパネル
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel chooserPanel = new JPanel();
        GridLayout l = new GridLayout(1, 2);
        l.setHgap(10);
        l.setVgap(10);
        chooserPanel.add(yearChooser);
        chooserPanel.add(monthChooser);

        // タイトル文字のラベル
        if(title != null) {
            JPanel panelLabel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelLabel.add(new JLabel(title));
            add(panelLabel);
        }
        // 年・月 選択スピナーのパネル
        add(chooserPanel);
        // 日付グリッドのパネル
        add(dayPanel);

        Calendar cal = Calendar.getInstance();
        cal.set(selectedYear, selectedMonth - 1, selectedDay);
        monthChooser.setValue(MONTH_NAMES[cal.get(Calendar.MONTH)]);
        yearChooser.setValue(cal.get(Calendar.YEAR));
        refreshDayLabels(cal, true);
    }

    /**
     * カレンダー グリッドの日付を描画し、選択日を着色描画する
     * @param year - 表示する年
     * @param month - 表示する月
     */
    private void refreshDayLabels(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(selectedYear, selectedMonth - 1, selectedDay);
        if (month == cal.get(Calendar.MONTH) && year == cal.get(Calendar.YEAR)) {
            refreshDayLabels(cal, true);
        } else {
            cal.set(year, month, 1);
            refreshDayLabels(cal, false);
        }
    }

    /**
     * カレンダー グリッドの日付を描画し、選択日を着色描画する
     * 
     * @param calendar - 選択年月日を格納したCalendar
     * @param currentMonth - 選択年月日を含む月の場合 true
     */
    private void refreshDayLabels(Calendar calendar, boolean currentMonth) {
        int maxDate = calendar.getActualMaximum(Calendar.DATE);
        int today = calendar.get(Calendar.DATE);

        calendar.set(Calendar.DATE, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        for (JLabel[] ll : dayLabels) {
            for (JLabel l : ll) {
                l.setText("");
            }
        }

        for (int day = 1; day <= maxDate; day++) {
            getDayLabel(day, firstDayOfWeek).setText(Integer.toString(day));
        }

        for (int i = 0; i < dayLabels.length; i++) {
            for (int j = 0; j < dayLabels[i].length; j++) {
                JLabel l = dayLabels[i][j];
                l.setBackground(DAY_BG);
                l.setForeground(DAY_FG);
                l.setBorder(DAY_BORDER);

                if (l.getText().length() != 0) {
                    l.setVisible(true);
                } else {
                    l.setVisible(false);
                }
            }
        }

        if (currentMonth) {
            JLabel l = getDayLabel(today, firstDayOfWeek);
            l.setBackground(TODAY_BG);
            l.setForeground(TODAY_FG);
            l.setBorder(TODAY_BORDER);
        }
    }

    private JLabel getDayLabel(int day, int firstDayOfWeek) {
        return dayLabels[(day + firstDayOfWeek - 2) / 7][(day + firstDayOfWeek - 2) % 7];
    }

    /**
     * 指定された文字列の、配列 MONTH_NAMES 上でのインデックスを返す
     * 
     * @param str - 検索する文字列
     * @return インデックス (0 ... 11)<br/>引数strが一致しない場合、0を返す
     */
    private int getMonthIndex(String str) {
        int month = 0;
        for (int i = 0; i < MONTH_NAMES.length; i++) {
            if (str.equals(MONTH_NAMES[i])) {
                month = i;
                break;
            }
        }
        return month;
    }

    /**
     * 選択された「年」を返す
     * 
     * @return 選択された年
     */
    public int getYear() {
        return selectedYear;
    }

    /**
     * 選択された「月」を返す
     * 
     * @return 選択された月 (1 ... 12)
     */
    public int getMonth() {
        return selectedMonth;
    }

    /**
     * 選択された「日」を返す
     * 
     * @return 選択された日 (1 ... )
     */
    public int getDay() {
        return selectedDay;
    }
}
