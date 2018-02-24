### What is FileCrypt?
FileCrypt is a simple to use, open-source Android application for encrypting files using [AES](https://en.wikipedia.org/wiki/Advanced_Encryption_Standard)  
You can [download the APK from here](https://github.com/steptowards/FileCrypt/blob/master/app/release/FileCrypt_v1.0.apk?raw=true) [[Verify](https://raw.githubusercontent.com/steptowards/FileCrypt/master/app/release/Verify.txt?raw=true)] or build it using the source code.

### What can I use FileCrypt for?
FileCrypt app can be used for encrypting / decrypting files from internal storage of your devices. It uses AES with 256 bit key length for encryption. You can also use FileCrypt to securely delete files using upto 3 overwrites.

### Why should I trust FileCrypt
File Crypt offers AES with 256 bit key to perform encryption on files. It is open-sourced under MIT Licence and relies on [Java Crypto library](https://docs.oracle.com/javase/7/docs/api/javax/crypto/package-use.html#javax.crypto) for implementation . You can do an independent review of the source code before using the app.

### How secure is AES algorithm 
Taken from [AES Wikipedia entry](https://en.wikipedia.org/wiki/Advanced_Encryption_Standard#Known_attacks) : 
> At present, there is no known practical attack that would allow someone without knowledge of the key to read data encrypted by AES when correctly implemented.

To put things into perspective, Seagate published a technical paper titled [128-Bit Versus 256-Bit AES Encryption](http://www.axantum.com/AxCrypt/etc/seagate128vs256.pdf).  
Page 4 of the paper gives a simplified analysis of AES-128 strength :  

> If you assume:
>   * Every person on the planet owns 10 computers.
>   * There are 7 billion people on the planet.
>   * Each of these computers can test 1 billion key combinations per second.
>   * On average, you can crack the key after testing 50% of the possibilities.

> Then the earth's population can crack one encryption key in 77,000,000,000,000,000,000,000,000 years!

### AES looks nice, I want to know more about it
You can use the [Wikipedia page for AES](https://en.wikipedia.org/wiki/Advanced_Encryption_Standard) to know more about it.  
<br>
Jeff Moser has created a [stick-figure guide to understanding AES](http://www.moserware.com/2009/09/stick-figure-guide-to-advanced.html) on his website. It offers an explanation using a personified AES stick-figure in a play. You are, however, required to sign a [Foot-Shooting Prevention Agreement](http://www.moserware.com/assets/stick-figure-guide-to-advanced/aes_act_3_scene_02_agreement_1100.png) before delving into details. 

### What kind of files can I encrypt or delete using FileCrypt
You can encrypt or delete any files including (but not limited to) documents, images, videos etc.  
Please note that because AES requires heavy computation, larger files such as videos can take a _long_ time to be encrypted or securely deleted. The total time required depends on your device's processor.  

### That's great, but I would like more features
We need all the help from you for feature suggestion. You can request a feature using email, github comments or raise a pull request for your feature.

### Licence Information
FileCrypt is licenced under [MIT Licence](https://github.com/steptowards/FileCrypt/blob/master/LICENCE).
Icons used in the app are provided by [Icons8](https://icons8.com)
