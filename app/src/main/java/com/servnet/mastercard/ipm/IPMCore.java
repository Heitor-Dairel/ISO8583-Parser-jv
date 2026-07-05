package com.servnet.mastercard.ipm;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.servnet.mastercard.config.IPMConfig;
import com.servnet.mastercard.exception.IPMConfigException;
import com.servnet.mastercard.exception.IPMException;
import com.servnet.mastercard.exception.IPMFileException;
import com.servnet.mastercard.exception.IPMLogException;
import com.servnet.mastercard.exception.IPMParseException;
import com.servnet.mastercard.file.IPMFile;
import com.servnet.mastercard.parameter.IPMParameter;
import com.servnet.mastercard.parse.IPMParse;

public final class IPMCore extends IPMParameter {

    private final Logger logger = LoggerFactory.getLogger(IPMCore.class);

    private final String fileDate, fileCycle;

    public IPMCore(String fileDate, String fileCycle) throws IPMException {

        super(fileDate, fileCycle);

        try {
            this.fileDate = new IPMConfig(fileDate, fileCycle).fileDateFormatter();
        } catch (IPMConfigException e) {
            throw new IPMException(e);
        }

        this.fileCycle = fileCycle;

    }

    public void init() throws IPMException {

        try {

            IPMFile ipmFile = new IPMFile(this.fileDate, this.fileCycle);
            Optional<byte[]> ipmData = ipmFile.getFileRaw();
            Optional<String> ipmFileName = ipmFile.getFileName();

            if (ipmData.isPresent() && ipmFileName.isPresent()) {

                IPMParse ipmParse = new IPMParse(ipmData.get(), ipmFileName.get());

                ipmParse.initIPMParse();

                logger.info(IPMCoreConstants.INFO_MSG_IPM_LOGG, ipmFileName.get(), ipmParse.getMsgCount());

            }
        } catch (IPMFileException | IPMParseException | IPMLogException e) {

            throw new IPMException(e);

        }

        return;
    }

}

class IPMCoreConstants {

    // Loggs Message
    public static final String INFO_MSG_IPM_LOGG = "{} processado com sucesso, {} linhas lidas.";

}