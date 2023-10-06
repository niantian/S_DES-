package S_DES;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SDESGUI extends JFrame {
    private JTextField keyField, inputField, outputField;
    private JButton encryptButton, decryptButton;
    private JRadioButton binaryRadio, asciiRadio;

    public SDESGUI() {
        setTitle("S-DES 加密/解密");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel keyLabel = new JLabel("密钥 (10位二进制):");
        keyField = new JTextField(10);
        JLabel inputLabel = new JLabel("输入文本 :");
        inputField = new JTextField(20);
        JLabel outputLabel = new JLabel("输出文本:");

        encryptButton = new JButton("加密");
        decryptButton = new JButton("解密");

        outputField = new JTextField();
        outputField.setEditable(false);

        binaryRadio = new JRadioButton("二进制", true);
        asciiRadio = new JRadioButton("ASCII");

        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(binaryRadio);
        radioGroup.add(asciiRadio);

        mainPanel.add(keyLabel);
        mainPanel.add(keyField);
        mainPanel.add(inputLabel);
        mainPanel.add(inputField);
        mainPanel.add(encryptButton);
        mainPanel.add(decryptButton);
        mainPanel.add(outputLabel);
        mainPanel.add(outputField);
        mainPanel.add(binaryRadio);
        mainPanel.add(asciiRadio);

        add(mainPanel, BorderLayout.CENTER);

        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key = keyField.getText();
                String input = inputField.getText();
                if (binaryRadio.isSelected()) {
                    String ciphertext = S_DES.encrypt(input);
                    outputField.setText(ciphertext);
                } else if (asciiRadio.isSelected()) {
                    // 将ASCII明文转换为二进制字符串
                    StringBuilder binaryPlaintext = new StringBuilder();
                    for (char c : input.toCharArray()) {
                        binaryPlaintext.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
                    }
                    String ciphertext = S_DES.encrypt(binaryPlaintext.toString());
                    // 将二进制密文转换回ASCII字符串
                    StringBuilder asciiCiphertext = new StringBuilder();
                    for (int i = 0; i < ciphertext.length(); i += 8) {
                        String byteStr = ciphertext.substring(i, i + 8);
                        int byteValue = Integer.parseInt(byteStr, 2);
                        asciiCiphertext.append((char) byteValue);
                    }
                    outputField.setText(asciiCiphertext.toString());
                }
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key = keyField.getText();
                String input = inputField.getText();
                String plaintext = "";
                if (binaryRadio.isSelected()) {
                    plaintext = S_DES.decrypt(input);
                } else if (asciiRadio.isSelected()) {
                    // 将ASCII密文转换为二进制字符串
                    StringBuilder binaryCiphertext = new StringBuilder();
                    for (char c : input.toCharArray()) {
                        binaryCiphertext.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
                    }
                    plaintext = S_DES.decrypt(binaryCiphertext.toString());
                    // 将二进制明文转换回ASCII字符串
                    StringBuilder asciiPlaintext = new StringBuilder();
                    for (int i = 0; i < plaintext.length(); i += 8) {
                        String byteStr = plaintext.substring(i, i + 8);
                        int byteValue = Integer.parseInt(byteStr, 2);
                        asciiPlaintext.append((char) byteValue);
                    }
                    plaintext = asciiPlaintext.toString();
                }
                outputField.setText(plaintext);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SDESGUI gui = new SDESGUI();
                gui.setVisible(true);
            }
        });
    }
}
