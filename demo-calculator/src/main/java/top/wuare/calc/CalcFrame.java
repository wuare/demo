package top.wuare.calc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author wuare
 * @date 2021/7/8
 */
public class CalcFrame {

    private JFrame frame;
    private Interpreter interpreter;

    public void start() {
        frame = new JFrame("计算器");
        interpreter = new Interpreter();
        // 屏幕信息
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        System.out.println("screen width: " + width);
        System.out.println("screen height: " + height);
        int frameWidth = 330;
        int frameHeight = 460;
        frame.setBounds(width / 2 - (frameWidth / 2), height / 2 - (frameHeight / 2), frameWidth, frameHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, frameWidth, frameHeight);
        frame.add(panel);

        placeComponents(panel, frameWidth, frameHeight);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel, int width, int height) {
        // 显示
        int textPaneHeight = 100;
        JTextPane textPane = new JTextPane();
        textPane.setBounds(0, 0, width, textPaneHeight);
        textPane.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        textPane.setEditable(true);
        panel.add(textPane);

        // 按键
        // title 占30，高460， 显示占100， 剩余330
        int panel0Height = 330;
        JPanel panel0 = new JPanel();
        panel0.setLayout(null);
        panel0.setBounds(0, textPaneHeight, width, panel0Height);
        panel.add(panel0);

        ActionListener btnActionListener = e -> textPane.setText(textPane.getText() + ((JButton) e.getSource()).getText());
        addBtn(panel0, "9", 0, 0).addActionListener(btnActionListener);
        addBtn(panel0, "8", 80, 0).addActionListener(btnActionListener);
        addBtn(panel0, "7", 160, 0).addActionListener(btnActionListener);
        addBtn(panel0, "/", 240, 0).addActionListener(btnActionListener);
        addBtn(panel0, "6", 0, 80).addActionListener(btnActionListener);
        addBtn(panel0, "5", 80, 80).addActionListener(btnActionListener);
        addBtn(panel0, "4", 160, 80).addActionListener(btnActionListener);
        addBtn(panel0, "*", 240, 80).addActionListener(btnActionListener);
        addBtn(panel0, "3", 0, 160).addActionListener(btnActionListener);
        addBtn(panel0, "2", 80, 160).addActionListener(btnActionListener);
        addBtn(panel0, "1", 160, 160).addActionListener(btnActionListener);
        addBtn(panel0, "-", 240, 160).addActionListener(btnActionListener);
        addBtn(panel0, "c", 0, 240).addActionListener(e -> textPane.setText(null));
        addBtn(panel0, "0", 80, 240).addActionListener(btnActionListener);
        addBtn(panel0, "+", 160, 240).addActionListener(btnActionListener);
        addBtn(panel0, "=", 240, 240).addActionListener(e -> {
            String text = textPane.getText();
            if (text == null || "".equals(text)) {
                return;
            }
            try {
                interpreter.reset(text);
                int expr = interpreter.expr();
                textPane.setText(expr + "");
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                if ("Invalid character".equals(ex.getMessage())) {
                    JOptionPane.showMessageDialog(frame, "计算器当前只支持整数计算，" +
                            "请检查您是否输入了其它字符");
                } else if ("Invalid syntax".equals(ex.getMessage())) {
                    JOptionPane.showMessageDialog(frame, "语法错误，请检查输入是否符合算术表达式");
                } else {
                    JOptionPane.showMessageDialog(frame, "Sorry about that, " +
                            "it's so pity have error, please contact developer to fix this.");
                }
            }
        });
    }

    public JButton addBtn(JPanel panel, String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 80, 80);
        btn.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        btn.setFocusPainted(false);
        panel.add(btn);
        return btn;
    }

    public static void main(String[] args) {
        new CalcFrame().start();
    }
}
