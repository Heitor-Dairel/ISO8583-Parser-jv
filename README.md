<p align="center">
<img src="https://upload.wikimedia.org/wikipedia/commons/2/2a/Mastercard-logo.svg" alt="Mastercard Logo" width="400">
<br>
<br>
</p>
 
# Mastercard ISO8583-1993 Parser (Java)
A high-performance parser developed in **Java** utilizing **jPOS** to read and interpret financial messages in the **ISO8583-1993** standard, with a specific focus on Mastercard **IPM (Interchange File)** files.
 
## About the Project
This project parses ISO8583 messages contained within Mastercard IPM files. It extracts **VBS** frames from binary files, decodes the content from **EBCDIC**, and interprets each message using a jPOS `GenericPackager`, generating a detailed log for each processing cycle.
 
## Features
- Reads binary IPM files from a source directory, locating the correct file automatically by **date** and **cycle** (CIC1/CIC2/CIC3).
- Extracts **VBS** segment frames (2 bytes length + 2 bytes ID) and reassembles the complete ISO8583 payload.
- Parses ISO8583-1993 messages via **jPOS `GenericPackager`**, using a custom EBCDIC configuration packager (`iso93ebcdic.xml`).
- Provides custom parsing for **DE048** (Additional Data), decomposing **PDS** (Private Data Subelements) into tags/values and exporting them as XML.
- Generates detailed **logs** per processed file (ISO8583 message dump + PDS), automatically cleaning the output directory on each execution.
- Implements a layered exception architecture, encapsulating configuration, file, parsing, and logging failures into a top-level `IPMException`.
- Validates input parameters (date format and valid cycle type) prior to processing.
- Optimized for performance: ~0.32ms processing time per message, using disk writes via `BufferedOutputStream`.
 
## How It Works
The parser executes the following operations in sequence:
1. Locates the matching IPM file corresponding to the provided **date** and **cycle** inside the configured source directory.
2. Reads the complete binary file into memory using `Files.readAllBytes`.
3. Iterates through the file to extract each **VBS** segment, reconstructing the full ISO8583 message payload from individual segment frames.
4. Performs an **unpack** operation on the message through jPOS `ISOMsg`, applying the custom Mastercard EBCDIC layout template.
5. If the message includes data in field **DE048**, it decomposes the internal PDS elements and logs them in an XML structure.
6. Writes the dump of every processed message to the dedicated log file corresponding to the source IPM file.
 
## Technologies Used
- **Java 25+**
- [`jPOS`](https://jpos.org/) — ISO8583 Parser framework (`ISOMsg`, `GenericPackager`).
- **SLF4J + Logback** — Application logging ecosystem.
- **Gradle (Kotlin DSL)** — Build automation and dependency management.
- `java.nio.file` — NIO file reading and directory manipulation.
- `java.io.BufferedOutputStream` — Optimized disk writing stream for output logs.
 
## License
This project is licensed under the [MIT](LICENSE) license.
 
## Author
Developed by **Heitor Dairel**  
[GitHub](https://github.com/heitor-dairel) · [LinkedIn](https://linkedin.com/in/heitor-dairel)
