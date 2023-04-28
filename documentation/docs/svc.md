# Wrapper Service

This wrapper service abstracts all implementations and make it easy for organisations to integrate with digital signature with less modification over the existing systems, programing language and framework independent or knowledge of blockchain technology. This is offered in the form of docker image for developer to host as a separate docker container service along side their existing service, which can be interacted through API call to perform actions related with digital signature.

## Get started

Clone our integration repository

``` bash
git clone https://github.com/Techo-Startup-Center/CamDX-Digital-Signature-Integration.git
```
Go into cloned directory.
Modify `docker-compose.yml` environment variables:

- **spring.servlet.multipart.max-file-size:** maximum file size for multipart upload
- **spring.servlet.multipart.max-request-size:** maximum file size for multipart upload
- **camdx.disig.server-url:** url for disig platform either development or production
- **camdx.disig.private-key:** extracted private key [how to get private key](../registration#extract-key).

After modification run

``` bash
docker-compose up -d
```

## API Catelog
Please refer to this [link](https://documenter.getpostman.com/view/13705832/2s93eR6bxL) for API catelog of this wrapper service

## Request Body for APIs
### File Request Type
- `/api/v1.0/approve/file`, `/api/v1.0/verify/file`, `/api/v1.0/revoke/file`
```java title="FileRequest.java"
@Data
public class FileRequest {
    MultipartFile file;
}
```
- `/api/v1.0/sign/file`
```java title="SignFileRequest.java"
@Data
public class SignFileRequest{
    MultipartFile file;
    private String cid;
    private List<String> signers;
}
```

### String Request Type
- `/api/v1.0/approve/string`, `/api/v1.0/verify/string`, `/api/v1.0/revoke/string`
```java title="StringRequest.java"
@Data
public class StringRequest {
    String payloadString;
}
```
- `/api/v1.0/sign/string`
```java title="SignStringRequest.java"
@Data
public class SignStringRequest{
    String payloadString;
    private String cid;
    private List<String> signers;
}
```

### Hash Request Type
- `/api/v1.0/approve/hash`, `/api/v1.0/verify/hash`, `/api/v1.0/revoke/hash`
```java title="HashRequest.java"
@Data
public class HashRequest {
    String hashHex;
}
```
- `/api/v1.0/sign/string`
```java title="SignStringRequest.java"
@Data
public class SignHashRequest{
    String hashHex;
    private String cid;
    private List<String> signers;
}
```