package com.servnet.mastercard;

import com.servnet.mastercard.ipm.IPMCore;

public class App {

    public static void main(String... args) {

        IPMCore ipm = new IPMCore("26/05/2025", "CIC2");

        ipm.init();

    }

}
