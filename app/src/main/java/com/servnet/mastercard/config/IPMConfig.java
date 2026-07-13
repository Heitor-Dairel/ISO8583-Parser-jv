package com.servnet.mastercard.config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.servnet.mastercard.exception.IPMConfigException;
import com.servnet.mastercard.parameter.IPMParameter;

public final class IPMConfig extends IPMParameter {

    private final Logger logger = LoggerFactory.getLogger(IPMConfig.class);

    private final String fileDate;
    private final String fileCycle;

    private LocalDate datePrev;

    public IPMConfig(String fileDate, String fileCycle) throws IPMConfigException {

        super(fileDate, fileCycle);

        this.fileDate = fileDate;
        this.fileCycle = fileCycle;

        this.fileInfoValid();

    }

    public String fileDateFormatter() {

        return this.datePrev.format(IPMConfigConstants.DATE_FORMAT_CURR);

    }

    private void fileInfoValid() throws IPMConfigException {

        this.fileCycleValid();
        this.fileDateValid();

    }

    private void fileDateValid() throws IPMConfigException {

        try {

            this.datePrev = LocalDate.parse(fileDate, IPMConfigConstants.DATE_FORMAT_PREV);

        } catch (DateTimeParseException e) {

            logger.error(IPMConfigConstants.ERROR_MSG_DATE_LOG, this.fileDate);

            throw new IPMConfigException(e, IPMConfigConstants.ERROR_MSG_DATE_EXCEPTION, this.fileDate);
        }

    }

    private void fileCycleValid() throws IPMConfigException {

        switch (this.fileCycle) {
            case IPMConfigConstants.CYCLE_1, IPMConfigConstants.CYCLE_2, IPMConfigConstants.CYCLE_3 -> {
                break;
            }
            default -> {
                logger.error(IPMConfigConstants.ERROR_MSG_CYCLE_LOG, this.fileCycle);
                throw new IPMConfigException(IPMConfigConstants.ERROR_MSG_CYCLE_EXCEPTION, this.fileCycle);
            }
        }

    }
}

final class IPMConfigConstants {

    private IPMConfigConstants() {
    }

    // Logs Message
    public static final String ERROR_MSG_DATE_LOG = "Formato de data invalido: '{}'.";
    public static final String ERROR_MSG_CYCLE_LOG = "Ciclo invalido: '{}'.";

    // Exception Message
    public static final String ERROR_MSG_DATE_EXCEPTION = "Falha ao formatar data: '%s'.";
    public static final String ERROR_MSG_CYCLE_EXCEPTION = "Ciclo inválido: '%s'. Os ciclos disponíveis são: CIC1, CIC2, CIC3.";

    // File Cycles
    public static final String CYCLE_1 = "CIC1";
    public static final String CYCLE_2 = "CIC2";
    public static final String CYCLE_3 = "CIC3";

    // Date Formatter
    public static final DateTimeFormatter DATE_FORMAT_PREV = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter DATE_FORMAT_CURR = DateTimeFormatter.ofPattern("ddMMyyyy");

}
