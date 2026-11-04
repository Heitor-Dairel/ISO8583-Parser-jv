package com.servnet.mastercard;

import com.servnet.mastercard.ipm.IPMCore;

public class App {

    private App() {
    }

    public static void main() {

        IPMCore ipm = new IPMCore("26/05/2025", "CIC2");

        ipm.init();

    }

}
