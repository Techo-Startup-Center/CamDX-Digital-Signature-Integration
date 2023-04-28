# Java SDK

For Java developers who want to work closely with CamDX Digital Signature System, we've got you covered. We've offered a few options and Java SDK is one of them. More languages are coming soon, and they currently are in our release pipeline :wink: The following steps will get you ready to use our Java SDK.

## Installation

Before getting started, make sure you have our repository cloned. If you haven't, please refer to [this link](https://github.com/Techo-Startup-Center/CamDX-Digital-Signature-Integration) to clone the repository :simple-github:

### Installing Dependency

After cloning the repository, go to the project directory and into wrapper-service. After that, you can execute `lib_bash_install.sh` for MacOS and Linux user, or `lib_win_install.bat` if you're using Windows. <br />

???+ note

    Currently, you can only install the required dependencies via Maven. For those of you who are Gradle users, fear not, Gradle support is coming sooner than expected. Be patient :wink:

### Setting up environment

Please provide your private key and api server url to consume the API

## Usage

CamDX Digital Signature System provide 4 basic functionalities such as

- Sign transaction
- Approve transaction that'd been signed
- Revoke transaction that'd been signed or approved
- Verify transaction signature
  Details of each functionality will be listed below

### Signing Transaction

<h4 id="sign-relay-file">signRelayFile</h4>

Add a digital signature on the current file as InputStream. The file hash will be calculated using SHA-256 hashing algorithm. `cid` is optional. If you have the file upload to an IPFS, you can also provide the file cid. `signers` is a list of user addresses that needed to be signed on the transaction. This method return a [TransactionReceipt](#transaction-receipt)

```java
TransactionReceipt signRelayFile(InputStream inputStream, String cid, List<String> signers) throws IOException, NoSuchAlgorithmException, DisigException;
```

???+ note

    You can also include your own address in list of signers. However, you can not sign a transaction with empty signers.

#### signRelayHash

If you prefer to generate file hash, this method is optimized specifically for that purpose. It accepts hex encoded hash of the file, and the rest of the arguments remains the same, and also returns [TransactionReceipt](#transaction-receipt)

```java
TransactionReceipt signRelayHash(String hashHexEncode, String cid, List<String> signers) throws DisigException, NoSuchAlgorithmException, JsonProcessingException;
```

#### signRelayString

Similar to [signRelayFile](#sign-relay-file) you can also use the content of the transaction as string format for digital signature. Instead of the accepting the file, the first argument of the method accepts a string content of transaction, however, the rest of the method arguments remain unchanged.

```java
TransactionReceipt signRelayString(String inputString, String cid, List<String> signers) throws NoSuchAlgorithmException, DisigException, JsonProcessingException;
```

### Approving a signature

<h4 id="approve-relay-file"></h4>
#### approveRelayFile

After a signature has been added to a file, it is not automatically signed. Your consent is essential for a digital signature. If you want to approve your signature that's been signed by someone else to a file, this method will do the job. It accepts file as InputStream and returns a [TransactionReceipt](#transaction-receipt).

```java
TransactionReceipt approveRelayFile(InputStream inputStream) throws IOException, NoSuchAlgorithmException, DisigException;
```

#### approveRelayHash

If a signature has been signed on a transaction using hash in hex encoded format, the method is suitable for adding your consent to the signature corelates to the hash that's been signed. This method accept hex encoded hash instead of file InputStream, and returns [TransactionReceipt](#transaction-receipt).

```java
TransactionReceipt approveRelayFile(InputStream inputStream) throws IOException, NoSuchAlgorithmException, DisigException;
```

#### approveRelayString

Similarly, if the transaction was using content of file as string format, this method can be used to approve your signature on the transaction. This method accepts string instead of file InputStream.

```java
TransactionReceipt approveRelayString(String inputString) throws NoSuchAlgorithmException, DisigException, JsonProcessingException;
```

### Revoking a signature

#### revokeRelayFile
Accidentally approve a signature or don't want your consent on a signature? Don't worry :wink: We got you cover, this method is like the opposite method of [approveRelayFile](#approve-relay-file) and we think there's no better explain this :joy:

```java
TransactionReceipt revokeRelayFile(InputStream inputStream) throws IOException, NoSuchAlgorithmException, DisigException;
```

#### revokeRelayHash
This method is equivalent to the method above but it accepts hash file in hex format.

```java
TransactionReceipt revokeRelayHash(String hashHexEncode) throws DisigException, NoSuchAlgorithmException, JsonProcessingException;
```

#### revokeRelayString
This method is equivalent to the method above but it accepts content of file in string format.

```java
TransactionReceipt revokeRelayString(String inputString) throws NoSuchAlgorithmException, DisigException, JsonProcessingException;
```

### Verifying signatures

#### verifyFile

This method accept a transaction file as inputStream, calculate hash of the file then returns [SignerResponse](#signer-response), which includes signatures that have been signed on the file including the status of each signatures.

```java
SignerResponse verifyFile(InputStream inputStream) throws IOException, NoSuchAlgorithmException, DisigException;
```

#### verifyHash

If the transaction was signed using hashed calculated by the signer, this method can be used to verify the signature and similar to the method above also returns [SignerResponse](#signer-response).

```java
SignerResponse verifyHash(String hashHexEncode) throws DisigException, JsonProcessingException;
```

#### verifyString

On the hand, signer can also uses raw content of transaction as string format to sign the transaction, in this case this method is used for verify it signatures, which returns [SignerResponse](#signer-response).

```java
SignerResponse verifyString(String inputString) throws NoSuchAlgorithmException, DisigException, JsonProcessingException;
```

### Dto

Refer to Dto object body such as `SignerResponse`, `ApprovalDto`, `CertificateResponse`, `TransactionReceipt`

<h4 id="signer-response"></h4>
#### SignerResponse

```java title="SignerResponse.java"
@Data
public class SignerResponse {
    String hash;
    String cid;
    List<ApproverDto> signers;
}
```

<h4 id="approval-dto"></h4>
#### ApprovalDto

```java title="ApprovalDto.java"
@Data
public class ApproverDto {
    String walletAddress;
    int status;
    String statusString;
    CertificateResponse certInfo;
}
```

<h4 id="certificate-response"></h4>
#### CertificateResponse

```java title="CertificateResponse.java"
@Data
public class CertificateResponse {
    String serialNumber;
    String subject;
    String commonName;
    String org;
    String orgUnit;
    String country;
    String email;
    String pubKey;
    Timestamp notBefore;
    Timestamp notAfter;
    String certificateChain;
}
```

<h4 id="transaction-receipt"></h4>
#### TransactionReceipt

```java title="TransactionReceipt.java"
@Data
public class TransactionReceipt {
    private String transactionHash;
    private String transactionIndex;
    private String blockHash;
    private String blockNumber;
    private String cumulativeGasUsed;
    private String gasUsed;
    private String contractAddress;
    private String root;
    private String status;
    private String from;
    private String to;
    private List<Log> logs;
    private String logsBloom;
    private String revertReason;
    private String type;
    private String effectiveGasPrice;
}
```
