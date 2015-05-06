
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author 
 *
 */
public class SwingTest01 {

    /**
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("基本コンポーネントのテスト");
        // 親コンテナ
        Container container = frame.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        frame.setContentPane(container);
        // 上部パネル（日付コントロール2個配置）
        JPanel panel1 = new JPanel(new GridLayout(1, 2, 5, 5));
        SwingCalendar calendar1 = new SwingCalendar(2015, 7, 22, "開始日");
        panel1.add(calendar1);
        SwingCalendar calendar2 = new SwingCalendar(2015, 8, 24, "終了日");
        panel1.add(calendar2);
        container.add(panel1);
        // 下部パネル（ボタン2個配置）
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton button = new JButton("値の表示");
        panel2.add(button);
        JButton buttonClose = new JButton("終了");
        panel2.add(buttonClose);
        container.add(panel2);
        // 「値の表示」ボタンの動作設定
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ボタンが押されたら、選択された日付をメッセージダイアログに表示
                JOptionPane.showMessageDialog(
                        frame,
                        "開始年月日： " + calendar1.getYear() + "/"
                                + calendar1.getMonth() + "/"
                                + calendar1.getDay() + "\n終了年月日： "
                                + calendar2.getYear() + "/"
                                + calendar2.getMonth() + "/"
                                + calendar2.getDay());
            }
        });
        // 「閉じる」ボタンの動作設定
        buttonClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frame.dispose();
            }
        });
        // 実行環境OSにあったUIを適用する
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
        }
        // メイン ダイアログの表示
        frame.pack();
        frame.setVisible(true);
    }
}