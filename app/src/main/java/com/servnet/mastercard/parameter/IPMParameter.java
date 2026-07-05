package com.servnet.mastercard.parameter;

import java.util.Objects;

public class IPMParameter {

    protected IPMParameter(String fileDate, String fileCycle) {

        Objects.requireNonNull(fileDate, "O parâmetro 'fileDate' não pode ser nulo.");
        Objects.requireNonNull(fileCycle, "O parâmetro 'fileCycle' não pode ser nulo.");

    }

    protected IPMParameter(byte[] ipmRaw, String ipmFileName) {

        Objects.requireNonNull(ipmRaw, "O parâmetro 'ipmRaw' não pode ser nulo.");
        Objects.requireNonNull(ipmFileName, "O parâmetro 'ipmFileName' não pode ser nulo.");

    }

    protected IPMParameter(String ipmFileName) {

        Objects.requireNonNull(ipmFileName, "O parâmetro 'ipmFileName' não pode ser nulo.");

    }

    protected IPMParameter(byte[] raw) {

        Objects.requireNonNull(raw, "O parâmetro 'raw' não pode ser nulo.");

    }
}
