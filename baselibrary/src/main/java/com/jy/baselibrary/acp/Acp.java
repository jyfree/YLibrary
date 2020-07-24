package com.jy.baselibrary.acp;

/**
 * @Author Administrator
 * @Date 2019/10/12-10:51
 * @TODO
 */
public class Acp {

    private AcpManager acpManager;

    private Acp() {
        acpManager = new AcpManager();
    }

    public static Acp getInstance() {
        return AcPermissionHolder.instance;
    }

    private static class AcPermissionHolder {
        private static final Acp instance = new Acp();
    }

    public AcpManager getAcpManager() {
        return acpManager;
    }
}
