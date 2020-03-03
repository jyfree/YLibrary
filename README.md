@[TOC](YLibrary框架)

# 欢迎使用YLibrary框架


## 介绍

YLibrary是使用kotlin&Java混合编写的，分为三个library：

## 一、sociallibrary（社会化库）

主要包括以下功能：
 1. 集成**QQ、微博、微信登录&分享**
 2. 集成**支付宝、微信支付**

## 二、baselibrary（基础库）

主要包括以下功能：
 1. 具有**MVC、MVP、MVVM**架构
 2. 封装**权限、拍照、图库**访问
 3. 封装view圆角、变色等功能的 **selector库**
 4. 封装**sp**，使用内存缓存，子线程存储sp
 5. 统一线程池管理

## 三、commonlibrary（常用库）

主要包括以下功能：
 1. 支持Aop切面编程：**Aspect**
 2. 注解式**DB**、并支持**协程，子线程**访问
 3. 使用主流的**RxJava2、retrofit2、RxBus、glide**框架
 4. 基于**sociallibrary**、二次封装社会化库

注：sociallibrary和baselibrary可以单独使用，  commonlibrary依赖于sociallibrary和baselibrary

## 如何使用？O(∩_∩)O哈哈~比较懒不想写

具体使用方法请看[App示例](https://github.com/jyfree/YLibrary/tree/master/app)



