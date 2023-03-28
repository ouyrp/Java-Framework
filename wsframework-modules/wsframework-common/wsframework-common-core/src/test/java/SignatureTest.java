import com.wusong.crypt.common.Signature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author p14
 */
public class SignatureTest {

    @Test
    public void testSign(){
        Assertions.assertEquals("0cc175b9c0f1b6a831c399e269772661", Signature.md5Hex("a"));
        Assertions.assertEquals("86f7e437faa5a7fce15d1ddcb9eaeaea377667b8",Signature.sha1Hex("a"));
        Assertions.assertEquals("06f30dc9049f859ea0ccb39fdc8fd5c2",Signature.hmacMD5("a","a"));
        Assertions.assertEquals("3902ed847ff28930b5f141abfa8b471681253673",Signature.hmacSHA1("a","a"));
    }
}