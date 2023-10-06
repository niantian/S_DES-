package S_DES;

import java.util.Scanner;

public class S_DES {
    static int P4[] = new int[] { 2, 4, 3, 1 };
    static int P8[] = new int[] { 6, 3, 7, 4, 8, 5, 10, 9 };
    static int P10[] = new int[] { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6 };
    static int IP[] = new int[] { 2, 6, 3, 1, 4, 8, 5, 7 };
    static int IP_1[] = new int[] { 4, 1, 3, 5, 7, 2, 8, 6 };
    static int EP[] = new int[] { 4, 1, 2, 3, 2, 3, 4, 1 };
    static int S1[][] = { { 1, 0, 3, 2 }, { 3, 2, 1, 0 }, { 0, 2, 1, 3 }, { 3, 1, 0, 2 } };
    static int S2[][] = { { 0, 1, 2, 3 }, { 2, 3, 1, 0 }, { 3, 0, 1, 2 }, { 2, 1, 0, 3 } };
    static int subkey1, subkey2 = 0;

    static int applyPermutation(int a, int b[], int c) {
        int x = 0;
        for (int i = 0; i < b.length; i++) {
            x <<= 1;
            x |= (a >> (c - b[i])) & 1;
        }
        return x;
    }

    static int applyEP(int a, int b) {
        int t, t0, t1, x1, x2, y1, y2;
        t = applyPermutation(a, EP, 4);
        t0 = (t >> 4) & 0xf;
        t1 = t & 0xf;
        x1 = ((t0 & 0x8) >> 2) | (t0 & 1);
        y1 = (t0 >> 1) & 0x3;
        x2 = ((t1 & 0x8) >> 2) | (t1 & 1);
        y2 = (t1 >> 1) & 0x3;
        t0 = S1[x1][y1];
        t1 = S2[x2][y2];
        t = applyPermutation((t0 << 2) | t1, P4, 4);
        return t;
    }

    static int applyF(int a, int subkey) {
        int left = (a >> 4) & 0xf;
        int right = a & 0xf;
        return ((left ^ applyEP(right, subkey)) << 4) | right;
    }

    static void generateSubkeys(String key) {
        int x = Integer.parseInt(key, 2);
        x = applyPermutation(x, P10, 10);
        int leftKey, rightKey = 0;
        leftKey = (x >> 5) & 0x1f;
        rightKey = x & 0x1f;
        leftKey = ((leftKey & 0xf) << 1) | ((leftKey & 0x10) >> 4);
        rightKey = ((rightKey & 0xf) << 1) | ((rightKey & 0x10) >> 4);
        subkey1 = applyPermutation((leftKey << 5) | rightKey, P8, 10);
        leftKey = ((leftKey & 0x07) << 2) | ((leftKey & 0x18) >> 3);
        rightKey = ((rightKey & 0x07) << 2) | ((rightKey & 0x18) >> 3);
        subkey2 = applyPermutation((leftKey << 5) | rightKey, P8, 10);
    }

    static String encrypt(String plaintext) {
        int temp = Integer.parseInt(plaintext, 2);
        temp = applyPermutation(temp, IP, 8);
        temp = applyF(temp, subkey1);
        temp = ((temp & 0xf) << 4) | ((temp >> 4) & 0xf);
        temp = applyF(temp, subkey2);
        temp = applyPermutation(temp, IP_1, 8);
        String ciphertext = Integer.toBinaryString(temp);
        while (ciphertext.length() < 8) {
            ciphertext = "0" + ciphertext;
        }
        return ciphertext;
    }

    static String decrypt(String ciphertext) {
        int temp = Integer.parseInt(ciphertext, 2);
        temp = applyPermutation(temp, IP, 8);
        temp = applyF(temp, subkey2);
        temp = ((temp & 0xf) << 4) | ((temp >> 4) & 0xf);
        temp = applyF(temp, subkey1);
        temp = applyPermutation(temp, IP_1, 8);
        String plaintext = Integer.toBinaryString(temp);
        while (plaintext.length() < 8) {
            plaintext = "0" + plaintext;
        }
        return plaintext;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("请选择操作：");
        System.out.println("1. 加密");
        System.out.println("2. 解密");
        System.out.println("3. ASCII加密");
        System.out.println("4. ASCII解密");
        int choice = scan.nextInt();
        scan.nextLine();  // 消耗掉换行符

        if (choice == 1) {
            System.out.println("请输入10位二进制密钥：");
            String key = scan.nextLine();
            System.out.println("请输入8位二进制明文：");
            String plaintext = scan.nextLine();
            generateSubkeys(key);
            String ciphertext = encrypt(plaintext);
            System.out.println("加密后的密文: " + ciphertext);
        } else if (choice == 2) {
            System.out.println("请输入10位二进制密钥：");
            String key = scan.nextLine();
            System.out.println("请输入8位二进制密文：");
            String ciphertext = scan.nextLine();
            generateSubkeys(key);
            String plaintext = decrypt(ciphertext);
            System.out.println("解密后的明文: " + plaintext);
        } else if (choice == 3) {
            System.out.println("请输入10位二进制密钥：");
            String key = scan.nextLine();
            System.out.println("请输入ASCII明文：");
            String plaintext = scan.nextLine();
            // 将ASCII明文转换为二进制字符串
            StringBuilder binaryPlaintext = new StringBuilder();
            for (char c : plaintext.toCharArray()) {
                binaryPlaintext.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
            }
            generateSubkeys(key);
            String ciphertext = encrypt(binaryPlaintext.toString());
            // 将二进制密文转换为ASCII字符串
            StringBuilder asciiCiphertext = new StringBuilder();
            for (int i = 0; i < ciphertext.length(); i += 8) {
                String byteStr = ciphertext.substring(i, i + 8);
                int byteValue = Integer.parseInt(byteStr, 2);
                asciiCiphertext.append((char) byteValue);
            }
            System.out.println("加密后的密文: " + asciiCiphertext.toString());
        } else if (choice == 4) {
            System.out.println("请输入10位二进制密钥：");
            String key = scan.nextLine();
            System.out.println("请输入ASCII密文：");
            String ciphertext = scan.nextLine();
            generateSubkeys(key);
            // 将ASCII密文转换为二进制字符串
            StringBuilder binaryCiphertext = new StringBuilder();
            for (char c : ciphertext.toCharArray()) {
                binaryCiphertext.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
            }
            String plaintextBinary = decrypt(binaryCiphertext.toString());
            // 将二进制明文转换为ASCII字符串
            StringBuilder plaintext = new StringBuilder();
            for (int i = 0; i < plaintextBinary.length(); i += 8) {
                String byteStr = plaintextBinary.substring(i, i + 8);
                int byteValue = Integer.parseInt(byteStr, 2);
                plaintext.append((char) byteValue);
            }
            System.out.println("解密后的明文: " + plaintext.toString());
        } else {
            System.out.println("无效的选择。");
        }
    }
}