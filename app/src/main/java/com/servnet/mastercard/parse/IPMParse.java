package com.servnet.mastercard.parse;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.GenericPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.servnet.mastercard.exception.IPMLogException;
import com.servnet.mastercard.exception.IPMParseException;
import com.servnet.mastercard.log.IPMLog;
import com.servnet.mastercard.parameter.IPMParameter;

public final class IPMParse extends IPMParameter {

    private final Logger log = LoggerFactory.getLogger(IPMParse.class);

    private final byte[] ipmRaw;
    private final String ipmFileName;

    private int msgCount;

    public IPMParse(byte[] ipmRaw, String ipmFileName) {

        super(ipmRaw, ipmFileName);

        this.ipmRaw = ipmRaw;
        this.ipmFileName = ipmFileName;

    }

    public void initIPMParse() throws IPMParseException, IPMLogException {

        this.fileIsoPayload();

    }

    public int getMsgCount() {
        return this.msgCount;
    }

    private void fileIsoPayload() throws IPMParseException, IPMLogException {

        int index = 0;
        int lenRaw = this.ipmRaw.length;
        ISOPackager packager = this.getIsoPackager();

        try (IPMLog ipmLog = new IPMLog(this.ipmFileName);) {

            while (index < lenRaw) {

                ISOMsg msg = this.initIPMParseConfig(packager);

                IPMExtractResult extractResult = extractIsoPayload(this.ipmRaw, index, lenRaw);

                msg.unpack(extractResult.payload);

                msg.dump(ipmLog.writeFileLog, "");

                if (msg.hasField(48)) {

                    IPMPdsTag ipmPds = new IPMPdsTag(msg.getBytes(48));

                    ipmPds.dump(ipmLog.writeFileLog);

                }

                index += extractResult.consumed;

                this.msgCount++;

            }

        } catch (ISOException e) {

            log.error(IPMParseConstants.ERROR_MSG_PARSE_LOG, this.msgCount);

            throw new IPMParseException(e, IPMParseConstants.ERROR_MSG_PARSE_EXCEPTION, this.msgCount);

        }

    }

    private ISOMsg initIPMParseConfig(ISOPackager packager) {

        ISOMsg msg = new ISOMsg();
        msg.setPackager(packager);

        return msg;

    }

    private ISOPackager getIsoPackager() throws IPMParseException {

        try {

            return new GenericPackager(IPMParseConstants.PACKAGER_PATH);

        } catch (ISOException e) {

            log.error(IPMParseConstants.ERROR_MSG_PACKAGER_LOG, IPMParseConstants.PACKAGER_PATH);

            throw new IPMParseException(e, IPMParseConstants.ERROR_MSG_PACKAGER_EXCEPTION,
                    IPMParseConstants.PACKAGER_PATH);

        }
    }

    private IPMExtractResult extractIsoPayload(byte[] raw, int index, int lenRaw) {

        int indexCurr = index;
        ByteArrayOutputStream payload = new ByteArrayOutputStream();
        int segId;
        int segLen;
        int payloadLen;
        boolean isMatch = true;

        while (isMatch) {

            if (indexCurr + 4 > lenRaw) {
                isMatch = false;
            }

            segId = raw[indexCurr + 2] & 0xFF;
            segLen = (raw[indexCurr] & 0xFF) << 8 | (raw[indexCurr + 1] & 0xFF);

            payloadLen = segLen - 4;

            indexCurr += 4;

            payload.write(raw, indexCurr, payloadLen);

            indexCurr += payloadLen;

            if (segId == 0) {
                isMatch = false;
            }

            if (indexCurr + 2 < lenRaw && raw[indexCurr + 2] == 0) {
                isMatch = false;
            }
        }

        return new IPMExtractResult(payload.toByteArray(), indexCurr - index);

    }

}

final class IPMExtractResult {

    public final byte[] payload;
    public final int consumed;

    public IPMExtractResult(byte[] payload, int consumed) {
        this.payload = payload;
        this.consumed = consumed;
    }

}

final class IPMParseConstants {

    private IPMParseConstants() {
    }

    // Path Packager
    public static final InputStream PACKAGER_PATH = ClassLoader.getSystemClassLoader()
            .getResourceAsStream("packager/iso93ebcdic.xml");

    // Logs Message
    public static final String ERROR_MSG_PARSE_LOG = "Erro ao realizar parse na linha '{}'.";
    public static final String ERROR_MSG_PACKAGER_LOG = "Erro ao encontrar packager no diretorio '{}'.";

    // Exception Message
    public static final String ERROR_MSG_PARSE_EXCEPTION = "Falha ao realizar parse na linha '%d'.";
    public static final String ERROR_MSG_PACKAGER_EXCEPTION = "Falha ao encontrar packager no diretório '%s'.";

}
