import com.wusong.crypt.common.AESEncryptor;
import com.wusong.crypt.common.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author p14
 */
public class AesTest {
    @Test
    public void testEncryptAndDecrypt(){
        String password = "abc";
        String salt = "abx";
        String text = "mysql.url=jdbc:mysql://192.168.25.119:3306/ws_cmdb?useUnicode=true&amp;characterEncoding=utf8&amp;serverTimezone=GMT" +
                "\nmysql.user=defdefdef" +
                "\nmysql.pass=adsfasf#$#@%$@#%FBG";
        for (int i = 0; i < 1; i++) {
            text += text;
        }
        String enc = new AESEncryptor(salt).encryptGCM(password, text);
        String dec = new AESEncryptor(salt).decryptGCM(password, enc);
        System.out.println("text " + text.length());
        System.out.println("enc " + enc.length());
        Assertions.assertEquals(text,dec);
    }


    @Test
    public void testEncryptAndDecrypt1(){
        String password = "abc";
        String salt = "abx";
        String text = "ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒";
        for (int i = 0; i < 3; i++) {
            text += text;
        }
        String enc = new AESEncryptor(salt).encryptGCM(password, text,"a");
        String dec = new AESEncryptor(salt).decryptGCM(password, enc,"a");
        System.out.println("text " + text.length());
        System.out.println("enc " + enc.length());
        Assertions.assertEquals(text,dec);
    }

    @Test()
    public void testEncryptAndDecrypt2(){
        Assertions.assertThrows(RuntimeException.class,this::testEncryptAndDecryptThrow);
    }

    private void testEncryptAndDecryptThrow(){
        String password = "abc";
        String salt = "abx";
        String text = "ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒";
        for (int i = 0; i < 3; i++) {
            text += text;
        }
        String enc = new AESEncryptor(salt).encryptGCM(password, text,"a");
        String dec = new AESEncryptor(salt).decryptGCM(password, enc);
        System.out.println("text " + text.length());
        System.out.println("enc " + enc.length());
        Assertions.assertEquals(text,dec);
    }

    @Test
    public void testEncryptAndDecrypt3(){
        String password = "abc";
        String salt = "abx";
        String text = "ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒ce shi #$@#$ 舒服撒";
        for (int i = 0; i < 3; i++) {
            text += text;
        }
        String enc = new AESEncryptor(salt).encryptGCM(password, text,"a");
        Assertions.assertNotEquals(enc, new AESEncryptor(salt).encryptGCM(password, text,"a"));
    }

}
