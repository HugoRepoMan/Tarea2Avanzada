package com.shadowops.redteam.config;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Bean explícito declarado en ShadowOpsConfig (@Bean) encargado de generar
 * firmas criptográficas SHA-256 para cada evidencia de explotación reportada.
 */
public class GeneradorHuellaCriptograficaBean {

    private final String prefijoFirma;

    public GeneradorHuellaCriptograficaBean(String prefijoFirma) {
        this.prefijoFirma = prefijoFirma;
    }

    public String generarHuellaEvidencia(Long operacionId, Long objetivoId, String cve, String vectorCvss) {
        String payload = String.format("%d|%d|%s|%s", operacionId, objetivoId, cve, vectorCvss);
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            String hex = HexFormat.of().formatHex(hash).substring(0, 16).toUpperCase();
            return prefijoFirma + "-" + hex;
        } catch (NoSuchAlgorithmException e) {
            return prefijoFirma + "-FALLBACK";
        }
    }
}
