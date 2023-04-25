package kh.gov.camdx.disig.lib.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kh.gov.camdx.disig.lib.dto.SignerResponse;
import kh.gov.camdx.disig.lib.exceptions.DisigException;
import kh.gov.camdx.disig.lib.utility.Base58;
import kh.gov.camdx.disig.lib.utility.HttpRequester;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Sign;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Component
public class DigitalSignatureImpl implements DigitalSignatureService {
    private final String verifyEndpoint = "/api/v1.0/verify/";
    private final String signEndpoint = "/api/v1.0/sign";
    private final String revokeEndpoint = "/api/v1.0/revoke";
    private final String approveEndpoint = "/api/v1.0/approve";
    @Value("${camdx.disig.server-url}")
    private String serverUrl;
    @Value("${camdx.disig.private-key}")
    private String privateKey;
    private final ObjectMapper objectMapper;

    public DigitalSignatureImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public SignerResponse verifyFile(InputStream inputStream) throws IOException, NoSuchAlgorithmException, DisigException {
        return verify(getHashFromFile(inputStream));
    }

    @Override
    public SignerResponse verifyHash(String hashHexEncode) throws DisigException, JsonProcessingException {
        return verify(Numeric.hexStringToByteArray(hashHexEncode));
    }

    @Override
    public SignerResponse verifyString(String inputString) throws NoSuchAlgorithmException, DisigException, JsonProcessingException {
        return verify(getHashFromString(inputString));
    }

    @Override
    public TransactionReceipt signRelayFile(InputStream inputStream, String cid, List<String> addresses) throws IOException, NoSuchAlgorithmException, DisigException {
        return sign(getHashFromFile(inputStream), cid, addresses);
    }

    @Override
    public TransactionReceipt signRelayHash(String hashHexEncode, String cid, List<String> addresses) throws DisigException, NoSuchAlgorithmException, JsonProcessingException {
        return sign(Numeric.hexStringToByteArray(hashHexEncode),cid,addresses);
    }

    @Override
    public TransactionReceipt signRelayString(String inputString, String cid, List<String> addresses) throws NoSuchAlgorithmException, DisigException, JsonProcessingException {
        return sign(getHashFromString(inputString), cid, addresses);
    }

    @Override
    public TransactionReceipt approveRelayFile(InputStream inputStream) throws IOException, NoSuchAlgorithmException, DisigException {
        return approve(getHashFromFile(inputStream));
    }

    @Override
    public TransactionReceipt approveRelayHash(String hashHexEncode) throws DisigException, NoSuchAlgorithmException, JsonProcessingException {
        return approve(Numeric.hexStringToByteArray(hashHexEncode));
    }

    @Override
    public TransactionReceipt approveRelayString(String inputString) throws NoSuchAlgorithmException, DisigException, JsonProcessingException {
        return approve(getHashFromString(inputString));
    }

    @Override
    public TransactionReceipt revokeRelayFile(InputStream inputStream) throws IOException, NoSuchAlgorithmException, DisigException {
        return revoke(getHashFromFile(inputStream));
    }

    @Override
    public TransactionReceipt revokeRelayHash(String hashHexEncode) throws DisigException, NoSuchAlgorithmException, JsonProcessingException {
        return revoke(Numeric.hexStringToByteArray(hashHexEncode));
    }

    @Override
    public TransactionReceipt revokeRelayString(String inputString) throws NoSuchAlgorithmException, DisigException, JsonProcessingException {
        return revoke(getHashFromString(inputString));
    }

    private SignerResponse verify(byte[] inputHash) throws DisigException, JsonProcessingException {
        String hashHex = Numeric.toHexString(inputHash);
        Map<String, Object> rawRespond = HttpRequester.requestToServer(constructRequestUrl(verifyEndpoint, hashHex),
                HttpMethod.GET,
                ""
        );
        return objectMapper.readValue(objectMapper.writeValueAsString(rawRespond.get("data")), SignerResponse.class);
    }

    private TransactionReceipt approve(byte[] encodeHash) throws NoSuchAlgorithmException, JsonProcessingException, DisigException {
        String encodedData = TypeEncoder.encode(
                new DynamicStruct(new Bytes32(encodeHash),
                        new Bytes32(getRandomSalt())));
        Map<String, Object> requestBody = getRelayRequestBody(encodedData);
        Map<String, Object> rawRespond = HttpRequester.requestToServer(constructRequestUrl(approveEndpoint, ""), HttpMethod.POST, objectMapper.writeValueAsString(requestBody));
        return objectMapper.readValue(objectMapper.writeValueAsString(rawRespond), TransactionReceipt.class);
    }
    private TransactionReceipt revoke(byte[] encodeHash) throws NoSuchAlgorithmException, JsonProcessingException, DisigException {
        String encodedData = TypeEncoder.encode(
                new DynamicStruct(new Bytes32(encodeHash),
                new Bytes32(getRandomSalt())));
        Map<String, Object> requestBody = getRelayRequestBody(encodedData);
        Map<String, Object> rawRespond = HttpRequester.requestToServer(constructRequestUrl(revokeEndpoint, ""), HttpMethod.POST, objectMapper.writeValueAsString(requestBody));
        return objectMapper.readValue(objectMapper.writeValueAsString(rawRespond), TransactionReceipt.class);
    }

    private TransactionReceipt sign(byte[] encodeHash, String cid, List<String> addresses) throws NoSuchAlgorithmException, DisigException, JsonProcessingException {
        byte[] cidBytes = processCidToBytes(cid);
        String encodedData = TypeEncoder.encode(
                new DynamicStruct(new Bytes32(encodeHash),
                        new Bytes32(getRandomSalt()),
                        new Bytes4(Arrays.copyOfRange(cidBytes, 0, 4)),
                        new Bytes32(Arrays.copyOfRange(cidBytes, 4, cidBytes.length)),
                        new DynamicArray(Address.class, addresses)));
        Map<String, Object> requestBody = getRelayRequestBody(encodedData);
        Map<String, Object> rawRespond = HttpRequester.requestToServer(constructRequestUrl(signEndpoint, ""), HttpMethod.POST, objectMapper.writeValueAsString(requestBody));
        return objectMapper.readValue(objectMapper.writeValueAsString(rawRespond), TransactionReceipt.class);
    }

    private Map<String, Object> getRelayRequestBody(String encodedData) {
        Credentials signer = getCredential();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("address", signer.getAddress());
        requestBody.put("data", encodedData);
        requestBody.put("signature", Numeric.toHexString(getSignatureBytesFromData(encodedData, signer.getEcKeyPair())));
        return requestBody;
    }

    private byte[] getSignatureBytesFromData(String encodedData, ECKeyPair ecKeyPair) {
        Sign.SignatureData signatureData = Sign.signPrefixedMessage(encodedData.getBytes(StandardCharsets.UTF_8), ecKeyPair);
        byte[] signatureBytes = new byte[65];
        System.arraycopy(signatureData.getR(), 0, signatureBytes, 0, 32);
        System.arraycopy(signatureData.getS(), 0, signatureBytes, 32, 32);
        System.arraycopy(signatureData.getV(), 0, signatureBytes, 64, 1);
        return signatureBytes;
    }

    private String constructRequestUrl(String endpoint, String hash) {
        String concat = ObjectUtils.isEmpty(hash) ? "" : "/" + hash;
        return serverUrl + endpoint + concat;
    }

    private Credentials getCredential() {
        return Credentials.create(privateKey);
    }

    private byte[] processCidToBytes(String cid) throws DisigException {
        String rawCid = cid.substring(1);
        byte[] cidBytes = Base58.decode(rawCid);
        if (cidBytes.length != 36)
            throw new DisigException("Invalid length of Cid, expect 36 but got " + cidBytes.length, 401);
        return cidBytes;
    }

    private byte[] getRandomSalt() throws NoSuchAlgorithmException {
        return getHashFromString(UUID.randomUUID().toString());
    }

    private byte[] getHashFromFile(InputStream inputStream) throws IOException, NoSuchAlgorithmException {
        return getHashFromByteArray(inputStream.readAllBytes());
    }

    private byte[] getHashFromString(String inputString) throws NoSuchAlgorithmException {
        return getHashFromByteArray(inputString.getBytes(StandardCharsets.UTF_8));
    }

    private byte[] getHashFromByteArray(byte[] inputByteArray) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(inputByteArray);
    }
}
