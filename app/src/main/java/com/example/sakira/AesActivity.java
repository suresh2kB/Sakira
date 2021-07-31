package com.example.sakira;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AesActivity extends AppCompatActivity {

    private byte[] encryptionKey = {9, 115, 51, 86, 105, 4, -31, -23, -68, 88, 17, 20, 3, -105, 119, -53};
    private static Cipher enCipher,deCipher;
    private static SecretKeySpec secretKeySpec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aes);

        try {
            enCipher = Cipher.getInstance("AES");
            deCipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        secretKeySpec = new SecretKeySpec(encryptionKey,"AES");
    }

    static String DesEnc1(String string){

        byte[] stringBytes = string.getBytes();
        byte[] EncryptedBytes = new byte[stringBytes.length];

        try {
            enCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            EncryptedBytes = enCipher.doFinal(stringBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        String FinalString = null;

        try {
            FinalString = new String(EncryptedBytes, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return FinalString;
    }


    public static String DesDec1(String string) throws UnsupportedEncodingException {

        byte[] EncryptedByte = string.getBytes("ISO-8859-1");
        String decryptedString = null;

        byte[] decryption;

        try {
            deCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            decryption = deCipher.doFinal(EncryptedByte);
            decryptedString = new String(decryption);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return decryptedString;
    }
}