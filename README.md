Digital stamping java library
=============================

This project contains source code for Java library for digital time stamping.
Library using postsignum service to generate time time stamp for given file.

## Links

- Postsignum - http://www.postsignum.cz/testovaci_casova_razitka.html
- GIT repository - https://phab.ys-dev.cz/diffusion/YSDIGISTAMP/

## Development

The project uses the following technologies:

- Java 1.6

### Deps

- org.bouncycastle.bctsp-jdk16

```
<dependency>
  <groupId>org.bouncycastle</groupId>
  <artifactId>bctsp-jdk16</artifactId>
  <version>1.46</version>
</dependency>
```

## Build

The following command generates jar archive.

    mvn package
    
Generated jar contains truststore with postsignum certs.

## Usage

The fellowing code generates time stamp for `sample/sample.zip` file, 
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
