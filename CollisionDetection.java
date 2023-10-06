package S_DES;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CollisionDetection {
    public static void main(String[] args) {
        String givenPlaintext = "01010101"; // 给定的明文
        String givenCiphertext = "11111010"; // 对应的密文

        int numKeysToGenerate = 1024; // 要生成的密钥数量
        int collisionCount = 0; // 记录碰撞的次数
        List<String> collisionKeys = new ArrayList<>(); // 存储碰撞密钥的列表

        for (int i = 0; i < numKeysToGenerate; i++) {
            String key = generateRandomKey(); // 生成随机密钥
            S_DES.generateSubkeys(key); // 生成子密钥
            String ciphertext = S_DES.encrypt(givenPlaintext); // 使用当前密钥加密给定的明文

            if (ciphertext.equals(givenCiphertext)) {
                System.out.println("找到碰撞密钥：");
                System.out.println("给定的明文：" + givenPlaintext);
                System.out.println("给定的密文：" + givenCiphertext);
                System.out.println("碰撞密钥：" + key);
                collisionKeys.add(key); // 将碰撞密钥添加到列表中
                collisionCount++; // 增加碰撞次数
            }
        }

        if (collisionCount == 0) {
            System.out.println("未找到碰撞密钥。");
        } else {
            System.out.println("总共找到 " + collisionCount + " 个碰撞密钥：");
            for (String collisionKey : collisionKeys) {
                System.out.println("碰撞密钥：" + collisionKey);
            }
        }
    }

    private static String generateRandomKey() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int randomBit = random.nextInt(2);
            key.append(randomBit);
        }
        return key.toString();
    }
}
