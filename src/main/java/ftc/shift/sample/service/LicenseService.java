package ftc.shift.sample.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ftc.shift.sample.entity.Licence;
import ftc.shift.sample.exception.LicenceGeneratorException;
import ftc.shift.sample.repository.LicenceRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class LicenseService {
    private KeyPairGenerator keyPairGenerator;
    private Cipher cipher;

    {
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            cipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }


    private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();


    private final LicenceRepository licenseRepository;

    @Autowired
    public LicenseService(LicenceRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    private String encryptPublicKey(PublicKey publicKey) throws LicenceGeneratorException {
        var data = publicKey.getEncoded();

        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

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

    private PublicKey decryptPublicKey(String encodedKey, PrivateKey privateKey) throws LicenceGeneratorException {
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
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");


            return keyFactory.generatePublic(spec);
        } catch (Exception e) {
            throw new LicenceGeneratorException("Public key decoding err", e);
        }

    }

    private PrivateKey decodePrivateKetFromString(String encodedKey) throws LicenceGeneratorException {
        try {
            var decoded = Base64.getDecoder().decode(encodedKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePrivate(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new LicenceGeneratorException(e);
        }

    }

    public String generateLicence(Long userId, Long duration) throws LicenceGeneratorException {

        keyPairGenerator.initialize(1024, new SecureRandom());

        var keyPair = keyPairGenerator.generateKeyPair();

        // В license_key будем хранить public_key и проверять его через шифровку-дешифровку
        var publicKey = keyPair.getPublic();
        var privateKey = keyPair.getPrivate();


        var licenseID = UUID.randomUUID();//Вот это пихаем в бд как праймари кей.

        var licenseKey = this.encryptPublicKey(publicKey);

        Licence generatedLicence = new Licence(licenseID,
                Base64.getEncoder().encodeToString(privateKey.getEncoded()),
                licenseKey,
                Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now().plusDays(duration)), userId, "default");

        licenseRepository.save(generatedLicence);

        return this.generateLicenseString(generatedLicence);
    }

    public String generateLicenseString(Licence license) {
        return Base64.getEncoder().encodeToString(gson.toJson(license).getBytes(StandardCharsets.UTF_8));
    }
}
