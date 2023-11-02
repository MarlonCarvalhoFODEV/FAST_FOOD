package com.c.hotburguerdelivery.apis.encrypt;


import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {

    private PublicKey publicKey;
    private PrivateKey privateKey;

    private int bits = 2048 ;

    private String PATH_CHAVE_PUBLICA = "public.cry" ;
    private String PATH_CHAVE_PRIVADA = "private.cry" ;

    public RSA(PublicKey publicKey,PrivateKey privateKey){
        this.publicKey = publicKey ;
        this.privateKey = privateKey ;
    }

    public RSA(PublicKey publicKey,PrivateKey privateKey, int bits){
        this.publicKey = publicKey ;
        this.privateKey = privateKey ;
        this.bits = bits ;
    }

    public String getPATH_CHAVE_PRIVADA() {
        return PATH_CHAVE_PRIVADA;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }


    public void geraChave() {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(bits, random);
            final KeyPair key = keyGen.generateKeyPair();

            File chavePrivadaFile = new File(PATH_CHAVE_PRIVADA);
            File chavePublicaFile = new File(PATH_CHAVE_PUBLICA);

            // Cria os arquivos para armazenar a chave Privada e a chave Publica
            if (chavePrivadaFile.getParentFile() != null) {
                chavePrivadaFile.getParentFile().mkdirs();
            }

            chavePrivadaFile.createNewFile();

            if (chavePublicaFile.getParentFile() != null) {
                chavePublicaFile.getParentFile().mkdirs();
            }

            chavePublicaFile.createNewFile();

            // Salva a Chave Pública no arquivo
            ObjectOutputStream chavePublicaOS = new ObjectOutputStream(
                    new FileOutputStream(chavePublicaFile));
            chavePublicaOS.writeObject(key.getPublic());
            chavePublicaOS.close();

            // Salva a Chave Privada no arquivo
            ObjectOutputStream chavePrivadaOS = new ObjectOutputStream(
                    new FileOutputStream(chavePrivadaFile));
            chavePrivadaOS.writeObject(key.getPrivate());
            chavePrivadaOS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Verifica se o par de chaves Pública e Privada já foram geradas.
     */
    public boolean verificaSeExisteChavesNoSO() {

        File chavePrivada = new File(PATH_CHAVE_PRIVADA);
        File chavePublica = new File(PATH_CHAVE_PUBLICA);

        if (chavePrivada.exists() && chavePublica.exists()) {
            return true;
        }

        return false;
    }


    public void setBits(int bits) {
        this.bits = bits;
    }

    public KeyPair genKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA/ECB/PKCS1Padding");
        generator.initialize(bits);
        KeyPair keyPair = generator.generateKeyPair();

        return keyPair;
    }

    public byte[] encrypt(String string) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(string.getBytes());
    }

    public String encryptToJson(Object object) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        byte[] msgEncrypted = encrypt(new Gson().toJson(object));
        return new Gson().toJson(msgEncrypted);
    }

    public String decrypt(byte[] contentCrypto) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(contentCrypto));
    }

    public String decryptFromJson(String contentCrypto) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(new Gson().fromJson(contentCrypto, byte[].class)));
    }
}
