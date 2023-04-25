package kh.gov.camdx.disig.lib.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kh.gov.camdx.disig.lib.dto.SignerResponse;
import kh.gov.camdx.disig.lib.dto.TransactionReceipt;
import kh.gov.camdx.disig.lib.exceptions.DisigException;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface DigitalSignatureService {
    SignerResponse verifyFile(InputStream inputStream) throws IOException, NoSuchAlgorithmException, DisigException;
    SignerResponse verifyHash(String hashHexEncode) throws DisigException, JsonProcessingException;
    SignerResponse verifyString(String inputString) throws NoSuchAlgorithmException, DisigException, JsonProcessingException;
    TransactionReceipt signRelayFile(InputStream inputStream, String cid, List<String> signers) throws IOException, NoSuchAlgorithmException, DisigException;
    TransactionReceipt signRelayHash(String hashHexEncode, String cid, List<String> signers) throws DisigException, NoSuchAlgorithmException, JsonProcessingException;
    TransactionReceipt signRelayString(String inputString, String cid, List<String> signers) throws NoSuchAlgorithmException, DisigException, JsonProcessingException;
    TransactionReceipt approveRelayFile(InputStream inputStream) throws IOException, NoSuchAlgorithmException, DisigException;
    TransactionReceipt approveRelayHash(String hashHexEncode) throws DisigException, NoSuchAlgorithmException, JsonProcessingException;
    TransactionReceipt approveRelayString(String inputString) throws NoSuchAlgorithmException, DisigException, JsonProcessingException;
    TransactionReceipt revokeRelayFile(InputStream inputStream) throws IOException, NoSuchAlgorithmException, DisigException;
    TransactionReceipt revokeRelayHash(String hashHexEncode) throws DisigException, NoSuchAlgorithmException, JsonProcessingException;
    TransactionReceipt revokeRelayString(String inputString) throws NoSuchAlgorithmException, DisigException, JsonProcessingException;
}
