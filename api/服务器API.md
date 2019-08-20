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
600       |message


```json  
{
    "body": {},
    "footer": {
        "message": "用户密码错误，请确认密码是否输入正确，比如大小写！\r",
        "status": "600"
    }
}
``` 





## 2. 获取用户的分类列表

### 功能描述

获取用户分类列表

### 请求说明

> 请求方式：POST<br>

请求URL ：[/user/classify](#)

### 请求参数
无

### 请求demo
无

### 返回参数
字段       |字段类型       |字段说明
------------|-----------|-----------
classifyList |array     |分类列表
id | int|分类id
name|string|分类名称
 
### 返回结果
```json  
{
  "data": {
    "classifyList":[
      {
        "id":1,
        "name":"新人培训"
      },
      {
        "id":2,
        "name":"市场运维"
      },
      {
        "id":3,
        "name":"技术中心"
      },
      {
        "id":4,
        "name":"组织中心"
      },
      {
        "id":5,
        "name":"行政人事"
      },
    ]
  },
  "code": "0",
  "msg": "SUCCESS"
}
``` 



## 3. 获取用户文档列表

### 功能描述

用户获取首页文档列表

### 请求说明

> 请求方式：POST<br>

请求URL ：[/user/docs](#)

### 请求参数
字段       |字段类型       |字段说明
------------|-----------|-----------
token       |string       |token
page       |int        |页数
size       |int        |分类大小


### 请求demo
```json  
{
  "classifyId":1,
  "page":1,
  "size":10
}
``` 

### 返回参数
字段       |字段类型       |字段说明
------------|-----------|-----------
docs       |array        |文档列表
id         |int        |文档id
title      |string     |标题
releaseTime|string |发布时间
total      |int | 总条数

### 返回结果
```json  
{
  "data": {
    "docs":[
      {
        "id":1,
        "title":"第二节三陶企业文化培训记录",
        "releaseTime":"2019-05-10"
      }
    ],
    "total":200,
  },
  "code": "0",
  "msg": "SUCCESS"
}
``` 


## 4. 文档详情接口

### 功能描述

文档详情接口

### 请求说明

> 请求方式：GET<br>

请求URL ：[/doc/info](#)

### 请求参数
字段       |字段类型       |字段说明
------------|-----------|-----------
token       |string       |token
id       |int        |文档id

### 请求demo
http://demo/doc/info/1

### 返回参数
字段       |字段类型       |字段说明
------------|-----------|-----------
id          |int        |文档id
title       |string     |标题
content     |string     |内容
collect     |int        |是否收藏 0：未收藏  1：收藏
name        |string     |文件名称
filePath    |string     |文件地址
type        |string     |文件类型  pdf ,doc
total       |int        | 总条数

### 返回结果
```json  
{
  "data": {
        "id":1,
        "title":"第二节三陶企业文化培训记录",
        "content":"第二节三陶企业文化培训记录学习交流",
        "collect":1,
        "files":[
          {
            "name":"第二节三陶企业文",
            "filePath":"https://lanhuapp.com/web/#/item/project/board/detail?pid=2042a571-1c43-4987-9f1e-94a4632e0d3f&project_id=2042a571-1c43-4987-9f1e-94a4632e0d3f&image_id=7b340c9c-8e59-4eae-8472-b81a5b902976",
            "type":"doc"
          }
        ]
      },
  "code": "0",
  "msg": "SUCCESS"
}
```



## 5. 获取用户视频列表

### 功能描述

用户获取首页视频列表，token作为令牌head传输

### 请求说明

> 请求方式：POST<br>

请求URL ：[/user/videos](#)

### 请求参数
字段       |字段类型       |字段说明
------------|-----------|-----------
token      |string      |token
classifyId |int         |分类id
page       |int         |页数
size       |int         |分类大小

### 请求demo
```json  
{
  "classifyId":1,
  "page":1,
  "size":10
}
``` 

### 返回参数
字段       |字段类型       |字段说明
------------|-----------|-----------
videos      |array      |文档列表
id          |int        |文档id
title       |string     |标题
previewImg  |string     |预览图片
total       |int        | 总条数

### 返回结果
```json  
{
  "data": {
    "videos":[
      {
        "id":1,
        "title":"第二节三陶企业文化培训记录",
        "previewImg":"https://lanhuapp.com/web/#/item/project/",
        "releaseTime":"2019-05-10"
      }
    ],
    "total":200,
  },
  "code": "0",
  "msg": "SUCCESS"
}
```


## 6. 视频详情接口

### 功能描述

视频详情接口

### 请求说明

> 请求方式：GET<br>

请求URL ：[/video/info](#)

### 请求参数
字段       |字段类型       |字段说明
------------|-----------|-----------
id          |int        |视频id

### 请求demo
http://demo.com/video/info/1

### 返回参数
字段       |字段类型       |字段说明
------------|-----------|-----------
id          |int        |文档id
title       |string     |标题
content     |int   |内容
collect     |int   |是否收藏 0：未收藏  1：收藏
filePath    |string | 视频路径

### 返回结果
```json  
{
  "data": {
        "id":1,
        "title":"第二节三陶企业文化培训记录",
        "content":"第二节三陶企业文化培训记录学习交流",
        "collect":1,
        "filePaht":"https://lanhuapp.com/web/#/item/project/board/detail?pid=2042a571-1c43-4987-9f1e-94a4632e0d3f&project_id=2042a571-1c43-4987-9f1e-94a4632e0d3f&image_id=7b340c9c-8e59-4eae-8472-b81a5b902976"
      },
  "code": "0",
  "msg": "SUCCESS"
}
```



## 7. 用户收藏接口

### 功能描述

用户收藏接口,token作为令牌传输

### 请求说明

> 请求方式：POST<br>

请求URL ：[/user/collect](#)

### 请求参数
字段       |字段类型       |字段说明
------------|-----------|-----------
token       |string     |token
id          |int        |文件id
type        |string     |收藏类型 doc video
opt         |string     |文件id  0：取消收藏  1：收藏


### 请求demo
```json  
{
  "id":1,
  "type":"doc",
  "opt":0
}
``` 

### 返回参数

### 返回结果
```json  
{
  "code": "0",
  "msg": "SUCCESS"
}
``` 



## 8. 用户浏览记录上传

### 功能描述

用户浏览记录上传接口

### 请求说明

> 请求方式：POST<br>

请求URL ：[/user/browse](#)

### 请求参数
字段       |字段类型       |字段说明
------------|-----------|-----------
token       |string     |token
id          |int        |文件id
type        |string     |收藏类型 doc video


### 请求demo
```json  
{
  "id":1,
  "type":"doc"
}
``` 

### 返回参数

### 返回结果
```json  
{
  "code": "0",
  "msg": "SUCCESS"
}
``` 



## 9. 个人中心
### 功能描述
获取个人中心。
### 请求说明
> 请求方式：POST<br>
请求URL ：[user/me](#)

### 请求参数
无参数

### 返回参数
字段       |字段类型       |字段说明
------------|-----------|-----------
name        |string        |名字
browseHistory |array      |浏览记录
type       |string        |文件类型，doc.video
previewImg       |string        |vedio 存在
title       |string        |名称  
id       |int        |文件Id
collectClassifies |array   |收藏文件夹
id |int|文件夹id
name|string|文件夹名称
uploadFolders | array | 上传文件夹
id |int|文件夹id
name|string|文件夹名称


### 返回结果
```json  
{
  "data": {
    "name":"张三",
    "browseHistory":[
      {
        "type":"doc",
        "previewImg":"",
        "title":"第一节三陶",
        "id":1
      },
      {
        "type":"vedio",
        "previewImg":"",
        "title":"第一节三陶",
        "id":1
      }
    ],
    "collectClassifies":[
      {
        "id":1,
        "name":"新人培训",
      },
      {
        "id":2,
        "name":"技术中心",
      }
    ],
    "uploadFolders":[
      {
        "id":1,
        "name":"文件夹",
      },
      {
        "id":2,
        "name":"合肥",
      }
    ]
  },
  "code": "0",
  "msg": "SUCCESS"
}
``` 


## 10. 收藏列表
### 功能描述
获取用户收藏列表
### 请求说明
> 请求方式：POST<br>
请求URL ：[user/collect/list](#)

### 请求参数
字段       |字段类型       |字段说明
------------|-----------|-----------
token       |string       |token
id          |int          |文件夹id

### 请求demo
```json  
{
  "id":1
}
``` 

### 返回参数
字段       |字段类型       |字段说明
------------|-----------|-----------
collectList |array        |收藏列表
type       |string        |文件类型，doc.video
previewImg  |string   |vedio 存在
title       |string        |名称  
id       |int        |文件Id




### 返回结果
```json  
{
  "data": {
    "name":"张三",
    "collectFiles":[
      {
        "type":"doc",
        "previewImg":"",
        "title":"第一节三陶",
        "id":1
      },
      {
        "type":"vedio",
        "previewImg":"",
        "title":"第一节三陶",
        "id":1
      }
    ]
  },
  "code": "0",
  "msg": "SUCCESS"
}
``` 


## 11. 上传列表
### 功能描述
获取用户上传列表
### 请求说明
> 请求方式：POST<br>
请求URL ：[user/upload/list](#)


### 请求参数
字段       |字段类型       |字段说明
------------|-----------|-----------
token       |string       |token
id          |int          |收藏文件夹id

### 请求demo
```json  
{
  "id":1
}
``` 

### 返回参数
字段       |字段类型       |字段说明
------------|-----------|-----------
collectList |array        |收藏列表
type       |string        |文件类型，doc.vedio
previewImg  |string   |vedio 存在
title       |string        |名称  
id       |int        |文件Id

### 返回结果
```json  
{
  "data": {
    "name":"张三",
    "uploadFiles":[
      {
        "type":"doc",
        "title":"第一节三陶",
        "id":1
      },
      {
        "type":"vedio",
        "title":"第一节三陶",
        "id":1
      }
    ]
  },
  "code": "0",
  "msg": "SUCCESS"
}
``` 


## 12. 上传-创建文件夹
### 功能描述
用户创建文件夹
### 请求说明
> 请求方式：POST<br>
请求URL ：[user/upload/folder/create](#)


### 请求参数
字段       |字段类型       |字段说明
------------|-----------|-----------
token       |string       |token
name        |string       |文件夹名称

### 请求demo
```json  
{
  "name":"新的文件夹"
}
``` 

### 返回参数
无

### 返回结果
```json  
{
  "code": "0",
  "msg": "SUCCESS"
}
``` 


## 13. 上传-修改文件夹
### 功能描述
用户修改文件夹
### 请求说明
> 请求方式：POST<br>
请求URL ：[user/upload/folder/update](#)


### 请求参数
字段       |字段类型       |字段说明
------------|-----------|-----------
token       |string       |token
id          |int          |收藏文件夹id
name        |string       |文件夹名称

### 请求demo
```json  
{
  "id":1,
  "name":"新的文件夹"
}
``` 

### 返回参数
无

### 返回结果
```json  
{
  "code": "0",
  "msg": "SUCCESS"
}
``` 


## 14. 上传-删除文件夹
### 功能描述
用户文件夹删除操作
### 请求说明
> 请求方式：POST<br>
请求URL ：[user/upload/folder/del](#)


### 请求参数
字段       |字段类型       |字段说明
------------|-----------|-----------
token       |string       |token
id          |int          |收藏文件夹id

### 请求demo
```json  
{
  "id":1
}
``` 

### 返回参数
无

### 返回结果
```json  
{
  "code": "0",
  "msg": "SUCCESS"
}
``` 


## 15. 上传文件
### 功能描述
用户上传文件，token作为令牌传输
### 请求说明
> 请求方式：POST<br>
请求URL ：[user/upload/add](#)


### 请求参数
字段       |字段类型       |字段说明
------------|-----------|-----------
token       |string       |token
id          |int          |收藏文件夹id
file | file| 文件

### 请求demo
```json  
{
  "id":1,
}
``` 

### 返回参数
无

### 返回结果
```json  
{
  "code": "0",
  "msg": "SUCCESS"
}
``` 

## 16. 删除文件
### 功能描述
用户删除文件，token作为令牌传输
### 请求说明
> 请求方式：POST<br>
请求URL ：[user/upload/del](#)


### 请求参数
字段       |字段类型       |字段说明
------------|-----------|-----------
token       |string       |token
id          |int          |文件ID

### 请求demo
```json  
{
  "id":1
}
``` 

### 返回参数
无

### 返回结果
```json  
{
  "code": "0",
  "msg": "SUCCESS"
}
``` 

### 错误状态码
参见 [全局响应状态码说明](../introduction.html/#134-全局响应状态码说明)

 
