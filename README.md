# PostTool
轻量级的、基于Java的、源码级的接口Post工具

# 1.使用方法
## 1.1.配置唯一的常量: 项目根路径
## 1.2.创建不同环境的配置文件
## 1.3.发起请求

# 2.整体架构
- init config
- init Request
- init Response
- pre request(interceptor)
- send request
- receive response
- post request(interceptor)

## 2.1.requestTemplate 现在仅支持http协议
### 2.1.1.Request
#### Header
#### GET
#### POST Json
#### POST Form
### 2.1.1.Response
#### Json Response
#### File Response
## 2.2.interceptor
### 2.2.1.host指定拦截器
### 2.2.2.默认请求头拦截器
### 2.2.3.日志拦截器
### 2.2.4.请求耗时记录拦截器
### 2.2.5.占位符替换拦截器
### 2.2.6.路径生效拦截器
#### 路由切换
## 2.3.config(env variables)
### 2.3.1.优先级
### 2.3.2.切换环境
## 2.4.schedule
### 2.4.1.请求编排
### 2.4.2.循环
### 2.4.3.周期

# 4.其他本地工具
