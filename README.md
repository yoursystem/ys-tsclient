Digital stamping java library
=============================

This project contains source code for Java library for digital time stamping.
Library using postsignum service to generate time stamp for given file.

## Links

- Postsignum - http://www.postsignum.cz/testovaci_casova_razitka.html
- GIT repository - https://phab.ys-dev.cz/diffusion/YSDIGISTAMP/

## Development

The project uses the following technologies:

- Java 1.6

### Deps

- org.bouncycastle.bctsp-jdk16
- org.slf4j.slf4j-api

```
<dependency>
  <groupId>org.bouncycastle</groupId>
  <artifactId>bctsp-jdk16</artifactId>
  <version>1.46</version>
</dependency>
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-api</artifactId>
  <version>1.7.13</version>
</dependency>
<dependency>
  <groupId>ch.qos.logback</groupId>
  <artifactId>logback-classic</artifactId>
  <version>1.1.8</version>
</dependency>
```

## Build

The following command generates jar archive.

    mvn package

Generated jar contains truststore with postsignum certs.

If you want to package jar with all dependencies use the following command:

    mvn assembly:single

It will create target/ys-timestamp-client-1.0.1-jar-with-dependencies.jar .

## Usage

The fellowing code generates time stamp using postsignum demo username and password and testing authority, takes `sample/sample.zip` file,
saves digest to `sample/sample.zip.tsq` and time stamp to `sample/sample.zip.tsr`.

```
// Load truststore with certificates needed for SSL connection to Postsignum
TrustStoreUtils.loadTrustStore();

// Init client, auth using username and password, postsignum authority
TSClient tsClient = new TSClient("demoTSA", "demoTSA2010", TSClient.POSTSIGNUM_TSA);

// Generate time stamp for sample zip file
String sample_file = "./sample/export.zip";
tsClient.stamp(sample_file, DigestAlgEnum.HA_1);
```
