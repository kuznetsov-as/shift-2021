package ftc.shift.sample.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import ftc.shift.sample.entity.Licence;
import ftc.shift.sample.exception.LicenceDecodeException;
import ftc.shift.sample.exception.LicenceGeneratorException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Base64;
import java.util.UUID;

public class LicenceUtil {

    private static final String KEY_ALGORITHM = "RSA";

    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private static KeyPairGenerator keyPairGenerator;
    private static Cipher cipher;

    static {
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            cipher = Cipher.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    private static String encryptPublicKey(PublicKey publicKey) throws LicenceGeneratorException {
        var data = publicKey.getEncoded();

        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            /*
             * Весь ключ не влезет по длине поэтому шифруем пополам
             */
            cipher.update(data, 0, data.length / 2);
            var licenseKey1 = cipher.doFinal();

            cipher.update(data, data.length / 2, data.length / 2);
            var licenseKey2 = cipher.doFinal();

            var licenseKey = ArrayUtils.addAll(licenseKey1, licenseKey2);

            return Base64.getEncoder().encodeToString(licenseKey);

        } catch (Exception e) {
            throw new LicenceGeneratorException("Public key generation err", e);
        }
    }

    private static PublicKey decryptPublicKey(String encodedKey, PrivateKey privateKey) throws LicenceDecodeException {
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            var licenseKey = Base64.getDecoder().decode(encodedKey);

            cipher.update(licenseKey, 0, licenseKey.length / 2);
            // Получаем public_key
            byte[] decoded1 = cipher.doFinal();

            cipher.update(licenseKey, licenseKey.length / 2, licenseKey.length / 2);
            byte[] decoded2 = cipher.doFinal();

            var decoded = ArrayUtils.addAll(decoded1, decoded2);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            return keyFactory.generatePublic(spec);

        } catch (Exception e) {
            throw new LicenceDecodeException("Public key decoding err", e);
        }

    }

    private static PrivateKey decodePrivateKetFromString(String encodedKey) throws LicenceDecodeException {
        try {
            var decoded = Base64.getDecoder().decode(encodedKey);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            return keyFactory.generatePrivate(spec);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new LicenceDecodeException(e);
        }

    }

    public static String generateLicenseString(Licence license) {
        return Base64.getEncoder().encodeToString(gson.toJson(license).getBytes(StandardCharsets.UTF_8));
    }

    public static Licence generateLicence(Long userId, Long duration) throws LicenceGeneratorException {

        keyPairGenerator.initialize(1024, new SecureRandom());

        var keyPair = keyPairGenerator.generateKeyPair();

        // В license_key будем хранить public_key и проверять его через шифровку-дешифровку
        var publicKey = keyPair.getPublic();
        var privateKey = keyPair.getPrivate();


        var licenseID = UUID.randomUUID();//Вот это пихаем в бд как праймари кей.

        var licenseKey = encryptPublicKey(publicKey);

        return new Licence(licenseID,
                Base64.getEncoder().encodeToString(privateKey.getEncoded()),
                licenseKey,
                Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now().plusDays(duration)), userId, "default");
    }


    // Этот метод достает из строки лицензии экземпляр лицензии
    public static PublicLicence getLicenseFromString(String licenseString) throws LicenceDecodeException {
        var encodedLicense = new String(Base64.getDecoder().decode(licenseString.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

        try {
            return gson.fromJson(encodedLicense, PublicLicence.class);
        } catch (JsonSyntaxException e) {
            throw new LicenceDecodeException("Wrong licence string", e);
        }

    }

    public static boolean isLicenceCorrect(PublicLicence publicLicense, String privateKeyString) throws LicenceDecodeException {

        try {
            var privateKey = LicenceUtil.decodePrivateKetFromString(privateKeyString);

            var publicKey = LicenceUtil.decryptPublicKey(publicLicense.getLicenseKey(), privateKey);

            // Шифруем-дешифруем
            Cipher decode = Cipher.getInstance(KEY_ALGORITHM);
            Cipher encode = Cipher.getInstance(KEY_ALGORITHM);


            decode.init(Cipher.DECRYPT_MODE, privateKey);
            encode.init(Cipher.ENCRYPT_MODE, publicKey);


            int length = 30;
            boolean useLetters = true;
            boolean useNumbers = false;
            String randomTestString = RandomStringUtils.random(length, useLetters, useNumbers);


            encode.update(randomTestString.getBytes(StandardCharsets.UTF_8));
            decode.update(encode.doFinal());

            return randomTestString.equals(new String(decode.doFinal(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new LicenceDecodeException(e);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class PublicLicence {
        @Expose
        private final UUID id;

        @Expose
        private final String licenseKey;

        @Expose
        private final Date createDate;

        @Expose
        private final Date endDate;
    }
}
