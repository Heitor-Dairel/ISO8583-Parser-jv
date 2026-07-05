package com.servnet.mastercard.parse;

import java.io.PrintStream;

import com.servnet.mastercard.parameter.IPMParameter;

public final class IPMPdsTag extends IPMParameter {

    private final String ipmPds;
    private final byte[] raw;

    public IPMPdsTag(byte[] raw) {

        super(raw);

        this.raw = raw;
        this.ipmPds = this.parse();

    }

    public void dump(PrintStream p) {

        p.print(this.ipmPds);

        return;
    }

    private String parse() {

        StringBuilder stringBuilder = new StringBuilder(3000);
        int lenRaw = this.raw.length, index = 0;
        String tag, value;
        int len;

        stringBuilder.append(IPMPdsTagConstants.OPENING_TAG);

        while (index < lenRaw) {

            tag = new String(this.raw, index, IPMPdsTagConstants.LEN_TAG_FIELD);
            len = Integer.parseInt(new String(this.raw, index += IPMPdsTagConstants.LEN_TAG_FIELD, 3));
            value = new String(this.raw, index += 3, len);
            index += len;

            stringBuilder.append(IPMPdsTagConstants.INDENT)
                    .append(IPMPdsTagConstants.XML_INIT)
                    .append(tag)
                    .append(IPMPdsTagConstants.XML_MIDDLE)
                    .append(value)
                    .append(IPMPdsTagConstants.XML_END);

        }

        stringBuilder.append(IPMPdsTagConstants.CLOSING_TAG);

        return stringBuilder.toString();

    }

}

class IPMPdsTagConstants {

    // XML Pds Parse
    public static final String OPENING_TAG = "<pds>\n";
    public static final String CLOSING_TAG = "</pds>\n";
    public static final String INDENT = "  ";
    public static final int LEN_TAG_FIELD = 4;
    public static final String XML_INIT = "<field id=\"";
    public static final String XML_MIDDLE = "\" value=\"";
    public static final String XML_END = "\"/>\n";

}
