package com.jy.baselibrary.acp;

import java.util.List;

/**
 * @Author Administrator
 * @Date 2019/10/11-17:44
 * @TODO
 */
public interface AcpListener {
    /**
     * 同意
     */
    void onGranted();

    /**
     * 拒绝
     */
    void onDenied(List<String> permissions);
}
