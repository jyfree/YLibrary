package com.jy.baselibrary.acp;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @Author Administrator
 * @Date 2019/10/12-10:06
 * @TODO
 */
public class AcpConstant {

    //manifest注册的权限集合
    static final Set<String> manifestPermissionSet = new HashSet<>();
    //拒绝授权集合
    static List<String> deniedPermissionList = new LinkedList<>();

}
