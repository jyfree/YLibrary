@[TOC](YLibrary框架)

# 欢迎使用YLibrary框架


## 介绍

YLibrary是使用kotlin&Java混合编写的，是YFramework的升级版本https://github.com/jyfree/YFramework
分为三个library：

## 一、sociallibrary（社会化库）

主要包括以下功能：
 1. 集成**QQ、微博、微信登录&分享**
 2. 集成**支付宝、微信支付**
 3. 提供继承和接口方式接入sdk

## 二、baselibrary（基础库）

主要包括以下功能：
 1. 具有**MVC、MVP、MVVM**架构，支持viewBinding
 2. 封装**权限、拍照、图库**访问
 3. 封装**sp**，使用内存缓存，子线程存储sp
 4. 统一线程池管理

## 三、commonlibrary（常用库）

主要包括以下功能：
 1. 支持Aop切面编程：**Aspect**
 2. 注解式**DB**、并支持**协程，子线程**访问
 3. 封装主流的**RxJava3、retrofit2、RxBus、glide**框架
 4. 支持方法体任务队列（单任务、链式任务）

## gradle引入方式
 1. 基础库 implementation 'com.jy.baselibrary:baselibrary:1.1.5'
 2. 社会化库 implementation 'com.jy.sociallibrary:sociallibrary:1.2.5'
 3. 常用库 implementation 'com.jy.commonlibrary:commonlibrary:1.2.5'

注：三个库都可以单独使用， commonlibrary依赖于baselibrary， 引用sociallibrary需要把支付宝引入，如：implementation files("alipaysdk-15.8.01.210112203525.aar")

## 如何使用？O(∩_∩)O哈哈~比较懒不想写

具体使用方法请看[App示例](https://github.com/jyfree/YLibrary/tree/master/app)



