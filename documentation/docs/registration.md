# Registration

Before integrating with CamDX Digital Signature with your current system. You need to generate a new private key as well as a Certificate Signing Request(CSR) corresponding to the private key. You will need to submit the CSR manual via Digital Signature Adminstrator to get a certificate and complete the registration process. Through out the registration process, we'll be using [OpenSSL](https://www.openssl.org/). Please make sure you have it installed on your machine before proceeding.

## Key Generation

As of now, CamDX Digital Signature only support Eliptic Curve algorithm using curve secp256k1. Therefore, it's important to generate a private using the supported EC curves, which you do archive using the following command:

```
openssl ecparam -name secp256k1 -genkey | openssl ec -out private.key
```

The command above uses OpenSSL to generate a private key file name **private.key** using EC algorithm with secp256k1 curve, which can be used to create a Certificate Signing Request.

## Certificate Signing Request (CSR)

After you've generated a private key, it will be used to create Certificate Signing Request, which will be done using the following command:

```
openssl req -out certificate_signing_req.csr -key private.key -new
```

Upon entering this command, you will be asked to complete your organization information. Below is an example you can follow:

| Prompt                                       | Example                         |
| -------------------------------------------- | ------------------------------- |
| Country Name (2 letter code):                | KH                              |
| State or Province Name (full name):          | Phnom Penh                      |
| Locality Name (eg, city):                    | Phnom Penh                      |
| Organization Name (eg, company):             | Ministry of Economy and Finance |
| Organizational Unit Name (eg, section):      | Techo Startup Center            |
| Common Name (eg, fully qualified host name): | Technology and Innovation       |
| Email Address:                               |                                 |

???+ warning

    Please leave the password prompt blank. We won't be able to validate your CSR if you encrypt it with password :crying_cat_face:

After creating a CSR successfully, you will need to submit it manually to Digital Signature Adminstrations to get a valid certificate, which you can use for signing and verifying your transaction.

## Extract private key from PEM format

There is one more step final being fully ready to use our Digital Signature System. We need to extract private key from the **key** we've generated above. Here's the following command to archive that:

```
openssl pkey -in private.key -text | sed -n '/priv:/,/pub:/p' | sed -e '1d;$d' | tr -d ':'| tr -d ' ' | tr -d '\n' && echo
```
