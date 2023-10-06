package S_DES;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SDESBreakerThreadPool {
    private static final String ciphertext = "11111011"; // 替换为你的密文
    private static final String plaintext = "01010101"; // 替换为你的明文
    private static final int NUM_THREADS = 4; // 定义线程数量
    private static final int SUBKEY_SPACE_SIZE = 1024; // 定义密钥空间大小

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            final int startKey = i * (SUBKEY_SPACE_SIZE / NUM_THREADS);
            final int endKey = (i + 1) * (SUBKEY_SPACE_SIZE / NUM_THREADS);

            executor.submit(new Runnable() {
                @Override
                public void run() {
                    crackKeys(startKey, endKey);
                }
            });
        }

        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void crackKeys(int startKey, int endKey) {
        for (int key = startKey; key < endKey; key++) {
            String keyString = String.format("%10s", Integer.toBinaryString(key)).replace(' ', '0');
            S_DES.generateSubkeys(keyString); // 生成子密钥

            String decrypted = S_DES.decrypt(ciphertext); // 使用当前密钥解密密文
            if (decrypted.equals(plaintext)) { // 检查是否解密后的明文与给定明文匹配
                System.out.println("找到正确的密钥：" + keyString);
                //System.exit(0); // 找到密钥后退出程序
            }
            else{
                System.out.println("ffff");
            }
        }
    }
}
