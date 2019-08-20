## 1. 登录

### 功能描述

- 提供手机号和密码的登录方式，返回的token作为令牌

### 请求说明

> 请求方式：POST<br>

- 请求URL: `http://domain/api/tv/home/v2.0/login.json`

### 请求参数
- 入参位置: `Request Parameter`

字段       |字段类型       |字段说明
------------|-----------|-----------
phone       |string        |用户名-手机号
password       |string        |密码
serialNumber       |string        |盒子序列号

### 请求demo

    http://localhost:8090/api/tv/home/v2.0/login.json?phone=15000932523&password=z123456&serialNumber=F202180B0E14

### 返回参数
字段       |字段类型       |字段说明
------------|-----------|-----------
status       |string        |状态码
token       |string        |token值
gid       |string        |用户id
schoolId       |int        |学校id
name       |string        |姓名
version       |int        |登录版本号
phone       |string        |手机号


### 返回结果
```json  
{
    "body": {
        "gid": "1106504",
        "phone": "15000932523",
        "schoolId": 3315,
        "name": "428测试",
        "version": 91,
        "token": "102fc8d7e52d48b784c3aaee9ef396cb"
    },
    "footer": {
        "status": "200"
    }
}
``` 


### 错误状态码
状态码       |说明
------------|-----------
600         |message


```json  
{
    "body": {},
    "footer": {
        "message": "用户密码错误，请确认密码是否输入正确，比如大小写！\r",
        "status": "600"
    }
}
``` 


## 2. 退出登录

### 功能描述

- 根据用户id退出登录

### 请求说明

> 请求方式：POST<br>

- 请求URL: `http://domain/api/tv/home/v2.0/loginOut.tvbox`

### 请求参数
- 入参位置: `Request Parameter`

字段       |字段类型       |字段说明
------------|-----------|-----------
gid       |string        |用户id


### 请求demo

    http://localhost:8090/api/tv/home/v2.0/loginOut.tvbox?gid=1106504

### 返回参数
字段       |字段类型       |字段说明
------------|-----------|-----------
status       |string        |状态码



### 返回结果
```json  
{
    "body": {},
    "footer": {
        "status": "200"
    }
}
``` 


### 错误状态码
状态码       |说明
------------|-----------
600         |message


```json  
{
    "body": null,
    "footer": {
        "message": "用户ID不能为空！",
        "status": "600"
    }
}
``` 


## 3. 修改密码

### 功能描述

- 提供用户id，老密码，新密码修改用户密码

### 请求说明

> 请求方式：POST<br>

- 请求URL: `http://domain/api/tv/home/v2.0/updatePassword.tvbox`

### 请求参数
- 入参位置: `Request Parameter`

字段       |字段类型       |字段说明
------------|-----------|-----------
gid       |string        |用户id
password       |string        |新密码
oldPassword       |string        |老密码

### 请求demo

    http://localhost:8090/api/tv/home/v2.0/updatePassword.tvbox?gid=1106504&password=z123456&oldPassword=z12345678

### 返回参数
字段       |字段类型       |字段说明
------------|-----------|-----------
status       |string        |状态码
memberId       |string        |用户id

### 返回结果
```json  
{
    "body": {
        "memberId": "1106504"
    },
    "footer": {
        "status": "200"
    }
}
``` 

### 错误状态码
状态码       |说明
------------|-----------
600         |message


```json  
{
    "body": {},
    "footer": {
        "message": "用户密码错误，请确认密码是否输入正确，比如大小写！\r",
        "status": "600"
    }
}
``` 


## 3. 获取首页轮播消息

### 功能描述

- 获取盒子首页顶部banner消息

### 请求说明

> 请求方式：GET<br>

- 请求URL: `http://domain/api/tv/home/v2.0/queryHomeMessage.tvbox`

### 请求参数


### 请求demo

    http://localhost:8090/api/tv/home/v2.0/queryHomeMessage.tvbox

### 返回参数
字段       |字段类型       |字段说明
------------|-----------|-----------
status       |string        |状态码
message       |string        |轮播消息

### 返回结果
```json  
{
    "body": {
        "message": "让一亿孩子有质变，让教育回归教育。"
    },
    "footer": {
        "status": "200"
    }
}
``` 

### 错误状态码
状态码       |说明
------------|-----------
600         |message


```json  
{
    "body": {},
    "footer": {
        "message": "...",
        "status": "600"
    }
}
``` 

