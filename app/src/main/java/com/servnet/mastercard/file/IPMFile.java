package com.servnet.mastercard.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.servnet.mastercard.exception.IPMFileException;
import com.servnet.mastercard.parameter.IPMParameter;

import io.github.cdimascio.dotenv.Dotenv;

public final class IPMFile extends IPMParameter {

    private final Logger logger = LoggerFactory.getLogger(IPMFile.class);

    private final String fileDate, fileCycle;

    private Optional<String> fileName = Optional.empty();
    private Optional<byte[]> fileRaw = Optional.empty();

    public IPMFile(String fileDate, String fileCycle) throws IPMFileException {

        super(fileDate, fileCycle);

        this.fileDate = fileDate;
        this.fileCycle = fileCycle;

        this.getFileIso();

    }

    public Optional<String> getFileName() {
        return this.fileName;
    }

    public Optional<byte[]> getFileRaw() {
        return this.fileRaw;
    }

    private void getFileIso() throws IPMFileException {

        String fileNamePreview = String.format("CSU_ACQ_MASTER_OUTGOING_%s_%s_", this.fileCycle, this.fileDate);

        Predicate<Path> filterFileIso = path -> path.toString().contains(fileNamePreview);

        Optional<Path> colFilePathIso = Optional.empty();

        try (Stream<Path> walkDirIso = Files.walk(IPMFileConstants.DIR_ISO)) {

            colFilePathIso = walkDirIso.filter(filterFileIso).findFirst();

        } catch (IOException e) {

            logger.error(IPMFileConstants.ERROR_MSG_DIR_LOGG, IPMFileConstants.DIR_ISO);

            throw new IPMFileException(e, IPMFileConstants.ERROR_MSG_DIR_EXCEPTION, IPMFileConstants.DIR_ISO);

        }

        if (colFilePathIso.isPresent()) {

            Path filePathIso = colFilePathIso.get();

            this.fileName = Optional.of(filePathIso.getFileName().toString());

            this.fileRaw = Optional.of(this.readFileIso(filePathIso));

            return;

        }

        logger.warn(IPMFileConstants.ERROR_MSG_FILE_LOGG, this.fileDate, this.fileCycle);

        return;

    }

    private byte[] readFileIso(Path path) throws IPMFileException {

        try {

            return Files.readAllBytes(path);

        } catch (IOException e) {

            logger.error(IPMFileConstants.ERROR_MSG_READ_LOGG, path.getFileName());

            throw new IPMFileException(e, IPMFileConstants.ERROR_MSG_READ_EXCEPTION, path.getFileName());

        }

    }

}

class IPMFileConstants {

    public static final Path DIR_ISO = Path.of(Dotenv.load().get("DIR_PATH"));

    // Loggs Message
    public static final String ERROR_MSG_DIR_LOGG = "Erro ao percorrer diretorio '{}'.";
    public static final String ERROR_MSG_FILE_LOGG = "Arquivo nao encontrado para data '{}' e ciclo '{}'.";
    public static final String ERROR_MSG_READ_LOGG = "Erro ao ler arquivo '{}'.";

    // Exception Message
    public static final String ERROR_MSG_DIR_EXCEPTION = "Falha ao percorrer diretório '%s'";
    public static final String ERROR_MSG_READ_EXCEPTION = "Falha ao ler arquivo '%s'.";
}
