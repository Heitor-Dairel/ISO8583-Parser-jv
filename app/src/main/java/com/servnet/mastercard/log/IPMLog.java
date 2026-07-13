package com.servnet.mastercard.log;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.servnet.mastercard.exception.IPMLogException;
import com.servnet.mastercard.parameter.IPMParameter;

public final class IPMLog extends IPMParameter implements AutoCloseable {

    private final Logger logger = LoggerFactory.getLogger(IPMLog.class);

    private final String fileName;

    public final PrintStream writeFileLog;

    public IPMLog(String fileName) throws IPMLogException {

        super(fileName);

        this.fileName = fileName;

        this.constructor();

        this.writeFileLog = this.createPrintStream(this.createFile());

    }

    @Override
    public void close() {
        this.writeFileLog.close();
    }

    private void constructor() {

        this.deleteDirectory();
        this.createDirectory();

    }

    private PrintStream createPrintStream(Path path) {

        OutputStream channel;
        BufferedOutputStream buffered;

        try {
            channel = Files.newOutputStream(path);
        } catch (IOException e) {

            logger.error(IPMLogConstants.ERROR_MSG_CREATE_STREAM_LOG, path);

            throw new IPMLogException(e, IPMLogConstants.ERROR_MSG_CREATE_STREAM_EXCEPTION, path);

        }

        buffered = new BufferedOutputStream(channel, 64 * 1024);

        return new PrintStream(buffered, false, StandardCharsets.UTF_8);

    }

    private Path createFile() {

        Path filePath = IPMLogConstants.OUTPUT.resolve(fileName + ".LOG");
        String filePathString = filePath.toString();

        try {

            Files.createFile(filePath);

            return filePath;

        } catch (IOException e) {

            logger.error(IPMLogConstants.ERROR_MSG_CREATE_FILE_LOG, filePathString);

            throw new IPMLogException(e, IPMLogConstants.ERROR_MSG_CREATE_FILE_EXCEPTION, filePathString);

        }

    }

    private void createDirectory() throws IPMLogException {
        try {

            Files.createDirectories(IPMLogConstants.OUTPUT);

        } catch (IOException e) {

            logger.error(IPMLogConstants.ERROR_MSG_CREATE_DIR_LOG, IPMLogConstants.OUTPUT);

            throw new IPMLogException(e, IPMLogConstants.ERROR_MSG_CREATE_DIR_EXCEPTION, IPMLogConstants.OUTPUT);
        }
    }

    private void deleteDirectory() throws IPMLogException {

        List<Path> collectPath;

        if (Files.exists(IPMLogConstants.OUTPUT)) {

            try (Stream<Path> walkDirOutput = Files.walk(IPMLogConstants.OUTPUT)) {

                collectPath = walkDirOutput.sorted(Comparator.reverseOrder()).toList();

            } catch (IOException e) {

                logger.error(IPMLogConstants.ERROR_MSG_DIR_LOG, IPMLogConstants.OUTPUT);

                throw new IPMLogException(e, IPMLogConstants.ERROR_MSG_DIR_EXCEPTION, IPMLogConstants.OUTPUT);

            }

            try {

                for (Path path : collectPath)
                    Files.deleteIfExists(path);

            } catch (IOException e) {

                logger.error(IPMLogConstants.ERROR_MSG_DELETE_DIR_LOG, IPMLogConstants.OUTPUT);

                throw new IPMLogException(e, IPMLogConstants.ERROR_MSG_DELETE_DIR_EXCEPTION,
                        IPMLogConstants.OUTPUT);

            }
        }
    }

}

final class IPMLogConstants {

    private IPMLogConstants() {
    }

    public static final Path OUTPUT = Path.of(System.getProperty("user.dir"), "output");

    // Logs Message
    public static final String ERROR_MSG_CREATE_FILE_LOG = "Erro ao criar arquivo '{}'.";
    public static final String ERROR_MSG_CREATE_STREAM_LOG = "Erro ao iniciar escrita no arquivo '{}'.";
    public static final String ERROR_MSG_CREATE_DIR_LOG = "Erro ao criar diretorio '{}'.";
    public static final String ERROR_MSG_DIR_LOG = "Erro ao percorrer diretorio '{}'.";
    public static final String ERROR_MSG_DELETE_DIR_LOG = "Erro ao deletar pastas e arquivos do diretorio '{}'.";

    // Exception Message
    public static final String ERROR_MSG_CREATE_FILE_EXCEPTION = "Falha ao criar arquivo '%s'.";
    public static final String ERROR_MSG_CREATE_STREAM_EXCEPTION = "Falha ao iniciar escrita no arquivo '%s'.";
    public static final String ERROR_MSG_CREATE_DIR_EXCEPTION = "Falha ao criar diretório '%s'.";
    public static final String ERROR_MSG_DIR_EXCEPTION = "Falha ao percorrer diretório '%s'";
    public static final String ERROR_MSG_DELETE_DIR_EXCEPTION = "Falha ao deletar pastas e arquivos do diretório '%s'.";

}
