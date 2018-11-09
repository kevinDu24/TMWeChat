 ## 接口更新:
### 1. 修改"1. 登录"接口



# 一、术语
## 1. 请求方式
- GET
- POST
- PUT
- DELETE

## 2. 入参位置
- Request Body
- Request Parameter
- Path Variable
每一个接口不限于使用一种入参位置，具体参考每个接口的说明。

## 3. domain（测试）
- `ip`:222.73.56.31
- `port`:8081

## 4. 全局返回值
- `status`: 请求状态（成功为SUCCESS,失败为ERROR）
- `error`: 请求失败时，服务器的错误信息
- `data`: 返回的数据

## 5. 全局消息头
- `Header-Param`: 消息头名称（value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}"，不传此消息头默认太盟）
- `systemflag`: 客户标识（taimeng/yachi）

示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
  }
}
```


# 二、接口

## 1. 登录
- url: `http://domain/user`
- 请求方式： `GET`
- 入参位置： `Request Header`
- 入参参数：
	- `Authorization`: 认证信息
- 入参解释
```
    对“账号:密码”字符串进行base64加密，得到密钥与字符串“Basic ”拼接，以键值对的形式存放到headers中，键值为“Authorization”，app端调用所有授权接口都需要传。
```
- 入参位置: `Request Parameter`
- 入参参数:
  - `loginAddress`: 登录地点(非必传参数)
- 出参参数:
  - `isHplUser`: 是否是先锋太盟总部人员
  - `loginRecord`:
    - `address`: 最近一次登录地点
    - `time`: 最近一次登录时间
  - `userName`: 登录用户名
  - `areaName`: 区域名称
  - `levelId`: 区域ID
  - `userType`: 用户类型(组织架构用)   0:经销商、1：分公司、2：团队、3：其他账号
  - `permissions`: 权限列表
    - `id`: 权限id
    - `name`: 权限名称
  - `userRole`: 用户角色
    - `roleId`: 角色id
    - `roleName`: 角色名称
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "areaName": "HPL",
    "permissions": [],
    "isHplUser": true,
    "levelId": 81,
    "userType": 0,
    "loginRecord": {
      "address": "null",
      "time": 1490003398373
    },
    "userRole": {
      "roleId": "1303",
      "roleName": "太盟宝测试"
    },
    "userName": "wangyuetest"
  }
}
```

## 2. 查询合同申请状态列表
- url：`http://domain/contracts/state`
- 请求方式： `GET`
- 入参位置:  `Request Parameter`
- 入参参数：
	- `applyNum`: 申请编号或姓名(按姓名查询时输入)
	- `fpName`: FP名称
	- `startDate`: 起时间(格式:20150601)
	- `endDate`: 止时间(格式:20150601)
	- `state`: 申请状态(审批阶段、放款阶段、待材料归档、取消、拒绝)
	- `page`: 当前页数(起始页:1)
- 入参示例:
	http://domain/contracts/state?applyNum=16100144&fpName=&startDate=20160908&endDate=20170310&state=&page=1
- 出参参数：
	- `page`: 分页信息
		- `BAZYEL`: 总页数
		- `BADQSL`: 每页数量
		- `BAPAGE`: 当前页
		- `BAZTSL`: 总数
	- `contractstatelist`: 合同集合
	    - `BADDSJ`: 等待时间
	    - `BAGQZT`: 挂起状态
		- `BASQXM`: 申请姓名
		- `BASQZT`: 申请状态
		- `BAKHJC`: FP简称
		- `BASQBH`: 申请编号
		- `BASQRQ`: 申请日期
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "page": {
      "BAZYEL": "5000",
      "BADQSL": "20",
      "BAPAGE": "1",
      "BAZTSL": "100000"
    },
    "contractstatelist": [
      {
        "BADDSJ": "13",
        "BAGQZT": "挂起",
        "BASQXM": "测试",
        "BASQZT": "审批阶段",
        "BAKHJC": "合肥速融",
        "BASQBH": "36146445",
        "BASQRQ": "20160805"
      }
    ]
  }
}
```

## 3. 查询合同申请审批日志
- url: `http://domain/contracts/{condition}/log`
- 请求方式：`GET`
- 入参位置： `Path Variable`
- 入参参数：
	- `condition`: 查询条件(申请编号、姓名、身份证)
- 入参示例：
	http://domain/contracts/16100144/log
- 出参参数：
	- `contractinfo`: 合同信息
		- `BASQXM`: 申请姓名
		- `BASQBH`: 申请编号
		- `contractstatelist`:
			- `XTCZRY`: 审批人
			- `BAGQZT`: 挂起状态
			- `BASQZT`: 申请状态
			- `BATHYY`: 挂起,取消,拒绝备注(申请状态为条件通过时，附加该备注)
			- `BASHRQ`: 状态时间
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "contractinfo": {
      "BASQXM": "唐相福",
      "BASQBH": "36157040",
      "contractstatelist": [
        {
          "XTCZRY": "zhangyongle",
          "BAGQZT": "",
          "BASQZT": "拒绝",
          "BATHYY": "半年内有两次M2逾期,当前逾期期数“4”及以上数字，且当前逾期总额大于信用额度10%,账户状态为“次级”、“可疑”、“损失”、“呆滞”“呆账”",
          "BASHRQ": "20160802120143"
        }
      ]
    }
  }
}
```

## 4. 查询产品详情
- url: `http://domain/contracts/{applyNum}/details`
- 请求方式：`GET`
- 入参位置： `Path Variable`
- 入参参数：
	- `applyNum`: 申请编号
- 入参示例：
	http://domain/contracts/36157040/details
- 出参参数：
	- `contractinfo`:
		- `BABXJE`: 申请姓名
		- `BASXFS`: 申请编号
		- `BASFJE`: 首付金额（如有）
		- `BAXSJG`: 销售价格
		- `BAWFJE`: 尾付金额（如有）
		- `BACLCX`: 车型
		- `BACLPP`: 品牌
		- `BAYZYG`: 月供
		- `BARZQX`: 融资期限
		- `BASQBH`: 申请编号
		- `BABZJE`: 保证金金额（如有）
		- `BACLLX`: 车辆类型
		- `BASXFJ`: 手续费金额（如有）
		- `BATZZE`: 投资总额（含gps）
		- `BAGPSY`: GPS硬件
		- `BAYBJE`: 延保金额（如有）
		- `BARAXB`: 融安心宝
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "contractinfo":{
      "BABXJE": "0",
      "BASXFS": "",
      "BASFJE": "18000",
      "BAXSJG": "60000",
      "BAWFJE": "0",
      "BACLCX": "2016款1.5L手动精英旅行版7座",
      "BACLPP": "东风风光370",
      "BAYZYG": "1231.63",
      "BARZQX": "36",
      "BASQBH": "16100144",
      "BARAXB": "0",
      "BABZJE": "0",
      "BACLLX": "新车",
      "BASXFJ": "0",
      "BATZZE": "61000",
      "BAGPSY": "1000",
      "BAYBJE": "0"
    }
  }
}
```


## 5. 查询合同还款计划列表
- url: `http://domain/contracts/repayment`
- 请求方式： `GET`
- 入参位置： `Request Parameter`
- 入参参数：
	- `applyNum`: 申请编号或姓名(按姓名查询时输入)
	- `fpName`: FP名称
	- `startDate`: 起时间(格式:20150601)
	- `endDate`: 止时间(格式:20150601)
	- `state`: 扣款状态(正常扣款、逾期30天内、逾期超过30天)
	- `page`: 当前页数(起始页:1)
- 入参示例：
	http://domain/contracts/repayment?applyNum=&fpName=&startDate=20160821&endDate=20170320&state=&page=1
- 出参参数：
	- `page`: 分页信息
		- `BAZYEL`: 总页数
		- `BADQSL`: 每页数量
		- `BAPAGE`: 当前页
		- `BAZTSL`: 总数
	- `contractrepayplanlist`:
		- `BASQXM`: 申请姓名
		- `BASQZT`: 申请状态
		- `BAKHJC`: FP简称
		- `BASQBH`: 申请编号
		- `BASQRQ`: 申请日期
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "page": {
      "BAZYEL": "3687",
      "BADQSL": "20",
      "BAPAGE": "1",
      "BAZTSL": "73734"
    },
    "contractrepayplanlist": [
      {
        "BAHKZT": "正常",
        "BASQXM": "叶伟英",
        "BAKHJC": "深圳乾丰联合",
        "BASQBH": "3689771",
        "BASQRQ": "20160129"
      }
    ]
  }
}
```

## 6. 查询合同还款计划明细表
- url: `http://domain/contracts/{condition}/repayDetail`
- 请求方式： `GET`
- 入参位置： `Path Variable`
- 入参参数：
	- `condition`: 查询条件(申请编号、姓名、身份证)
- 入参示例：
	http://domain/contracts/16100144/repayDetail
- 出参参数：
	- `contractrepayplaninfo`:
		- `BAYQTS`: 逾期天数
		- `BAYHQS`: 已还期数
		- `BASQXM`: 申请姓名
		- `BAYQLX`: 逾期利息
		- `BASQZQ`: 租期
		- `BAYQQS`: 逾期期数
		- `BASQBH`: 申请编号
		- `repayplan`:
			- `BAHKRQ`: 还款日期
			- `BAKKZT`: 待扣款
			- `BAYQLX`: 预期利息
			- `BAHKYG`: 月供
			- `BASQQS`: 期数

- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "contractrepayplaninfo": {
      "BAYQTS": "",
      "BAYHQS": "0",
      "BASQXM": "唐相福",
      "BAYQLX": "",
      "BASQZQ": "24",
      "BAYQQS": "0",
      "BASQBH": "36157040",
      "repayplan": [
        {
          "BAHKRQ": "20171001",
          "BAKKZT": "待扣款",
          "BAYQLX": "0",
          "BAHKYG": "1745.95",
          "BASQQS": ""
        }
      ]
    }
  }
}
```

## 7. 查询销售统计量
- url: `http://domain/TM/sales/queryStatisticsByDate`
- 请求方式: `GET`
- 入参位置： `Request Parameter`
- 入参参数：
	- `type`: 查询类别(1代表账号统计，2代表申请量统计，3代表CA人员审核量，4代表合同量统计，5融资额统计)
	- `userLevel`: 用户层级（选择的地区levelID）
	- `startDate`: 开始日期(格式：20160708)
	- `endDate`: 结束日期(格式：20170909)
	- `userId`: 查询同比环比传入hpl(非必传)
- 出参参数：
	- `tableGrid`:列表数据，比率为不带百分号的数字
		- `id`:编号
		- `levelId`:层级代码
		- `realCount`:实际量（type = 1，展业账号；type = 2，通过数；type = 3，通过数；type = 4，实际合同数；type = 5，空串
		- `planCount`:计划量（type = 1，开立账号数；type = 2，申请账号数；type = 3，审核账号数；type = 4，申请通过数；type = 5，实际融资额）
		- `area`:地区
		- `arrivalRate`:达成率（type = 1，展业率；type = 2，通过率；type = 3，通过率；type = 4，转化率；type = 5，空串）
		- `largArea`:大区
		- `refuseCount`:拒绝数量
		- `passCount`:通过数量(type=1, 展业总账号)
		- `cancelCount`:取消数量
		- `tbGrowthRate`:同比增长率
		- `hbGrowthRate`:环比增长率
- 入参示例：
	http://domain/sales/queryStatisticsByDate?type=1&userLevel=81&startDate=20160801&endDate=20160831
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "tableGrid": [
      {
        "realCount": "26",
        "qxl": "",
        "levelId": "2375",
        "cancelCount": "",
        "hbGrowthRate": "104",
        "arrivalRate": "67",
        "xcsl": "",
        "escsl": "",
        "id": "1",
        "jjl": "",
        "passCount": "39",
        "area": "北区",
        "xclcvsl": "",
        "sytgl": "",
        "esclcvsl": "",
        "planCount": "6",
        "largArea": "HPL",
        "refuseCount": "",
        "tbGrowthRate": "130"
      }
    ]
  }
}
```

## 8. 查询当前用户层级下的所有权限地区
- url: `http://domain/sales/queryAreaLevel`
- 请求方式: `GET`
- 出参参数：
	- `level`:层级
	- `levelId`:层级ID
	- `parentId`:上层级的ID
	- `areaCode`:地区编码
	- `areaName`:地区名称
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "parentId": "0",
      "level": "ZGS",
      "areaName": "HPL",
      "areaCode": "",
      "levelId": "81"
    }
  ]
}
```

## 9. 查询用户信息
- url: `http://domain/sysUsers/userInfo`
- 请求方式: `GET`
- 出参参数:
	- `name`:姓名
	- `companyName`:公司名称
	- `role`: 职位
	- `username`:账号
	- `phoneNum`:手机
	- `email`:邮箱
	- `headImg`:邮箱
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "name": "超级管理员",
    "companyName": "先锋太盟融资租赁有限公司",
    "role": "超级管理员",
    "username": "admin",
    "phoneNum": "111",
    "email": "11",
    "headImg": "http://xxx/xx.jpg"
  }
}
```

## 10. 按年查询销售统计-人员统计报表
- url: `http://domain/sales/queryStatisticsByYear`
- 请求方式： `GET`
- 入参位置： `Request Parameter`
- 入参参数：
	- `type`: 查询类别(1代表账号统计，2代表申请量统计，3代表CA人员审核量，4代表合同量统计，5融资额统计)
	- `userLevel`: 用户层级（选择的地区levelID）
	- `year`: 年份(格式：2016)
- 出参参数：
  - `tableGrid`:列表数据
    - `id`:编号
    - `levelId`:层级代码
    - `realCount`:实际量（type = 1，展业账号；type = 2，通过数；type = 3，通过数；type = 4，实际合同数；type = 5，空串）
    - `planCount`:计划量（type = 1，开立账号数；type = 2，申请账号数；type = 3，审核账号数；type = 4，申请通过数；type = 5，实际融资额）
    - `area`:地区
    - `arrivalRate`:达成率（type = 1，展业率；type = 2，通过率；type = 3，通过率；type = 4，转化率；type = 5，空串）
    - `largArea`:大区
    - `refuseCount`:拒绝数量
    - `passCount`:通过数量（不使用）
    - `cancelCount`:取消数量
    - `tbGrowthRate`:同比增长率
    - `hbGrowthRate`:环比增长率
  - `lineGrid`:按年统计数据
    - `id`:编号
    - `month`:月份
    - `realCount`:实际量（type = 1，展业账号；type = 2，空串；type = 3，空串；type = 4，实际合同数；type = 5，空串）
    - `planCount`:计划量（type = 1，开立账号数；type = 2，申请账号数；type = 3，审核账号数；type = 4，申请通过数；type = 5，实际融资额）
    - `tbGrowthRate`:同比增长率
    - `hbGrowthRate`:环比增长率
    - `arrivalRate`:达成率（type = 1，展业率；type = 2，通过率；type = 3，通过率；type = 4，空串；type = 5，空串）
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "lineGrid": [
      {
        "id": "6",
        "realCount": "178",
        "month": "201601",
        "hbGrowthRate": "0",
        "arrivalRate": "17800",
        "planCount": "1",
        "tbGrowthRate": "0"
      },
      ...
    ],
    "tableGrid": [
      {
        "realCount": "31",
        "qxl": "",
        "levelId": "2375",
        "cancelCount": "0",
        "hbGrowthRate": "",
        "arrivalRate": "517",
        "xcsl": "",
        "escsl": "",
        "id": "1",
        "jjl": "",
        "passCount": "0",
        "area": "北区",
        "xclcvsl": "",
        "sytgl": "",
        "esclcvsl": "",
        "planCount": "6",
        "largArea": "先锋太盟融资租赁有限公司",
        "refuseCount": "0",
        "tbGrowthRate": ""
      },
      ...
    ]
  }
}
```

## 11. 按月查询销售统计-人员统计报表
- url: `http://domain/sales/queryStatisticsByMonth`
- 请求方式： `GET`
- 入参位置： `Request Parameter`
- 入参参数：
	- `type`: 查询类别(1代表账号统计，2代表申请量统计，3代表CA人员审核量，4代表合同量统计，5融资额统计)
	- `userLevel`: 用户层级（选择的地区levelID）
	- `year`: 年份(格式：2016)
  - `month`:月份（格式：1）
- 出参参数
- 出参参数：
  - `tableGrid`:列表数据
    - `id`:编号
    - `levelId`:层级代码
    - `realCount`:实际量（type = 1，展业账号；type = 2，通过数；type = 3，通过数；type = 4，实际合同数；type = 5，空串）
    - `planCount`:计划量（type = 1，开立账号数；type = 2，申请账号数；type = 3，审核账号数；type = 4，申请通过数；type = 5，实际融资额）
    - `area`:地区
    - `arrivalRate`:达成率（type = 1，展业率；type = 2，通过率；type = 3，通过率；type = 4，转化率；type = 5，空串）
    - `largArea`:大区
    - `refuseCount`:拒绝数量
    - `passCount`:通过数量（不使用）
    - `cancelCount`:取消数量
    - `tbGrowthRate`:同比
    - `hbGrowthRate`:环比
    - `tbzcl`:同比增长率
    - `hbzcl`:环比增长率
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "lineGrid": [
      {
        "id": "6",
        "realCount": "178",
        "month": "201601",
        "hbGrowthRate": "0",
        "arrivalRate": "17800",
        "planCount": "1",
        "tbGrowthRate": "0"
      },
      ...
    ],
    "tableGrid": [
      {
        "realCount": "31",
        "qxl": "",
        "levelId": "2375",
        "cancelCount": "0",
        "hbGrowthRate": "",
        "arrivalRate": "517",
        "xcsl": "",
        "escsl": "",
        "id": "1",
        "jjl": "",
        "passCount": "0",
        "area": "北区",
        "xclcvsl": "",
        "sytgl": "",
        "esclcvsl": "",
        "planCount": "6",
        "largArea": "先锋太盟融资租赁有限公司",
        "refuseCount": "0",
        "tbGrowthRate": ""
      },
      ...
    ]
  }
}
```


## 12. 销售计划查询
- url: `http://domain/sales/plan`
- 请求方式： `GET`
- 入参位置： `Request Parameter`
- 入参参数：
	- `parentId`: 要查询的父级levelId
	- `startDate`: 开始日期，格式20160909
	- `endDate`: 结束日期，格式20160909
	- `planType`: 类型，销售计划（合同量）为7，销售计划（申请量）为6
- 入参示例：
	http://domain/sales/plan?parentId=81&startDate=20160208&endDate=20160908&planType=6
- 出参参数：
	- `levelId`: 层级id
	- `area`: 地区名称
	- `planCount`: 计划量
	- `realCount`: 实际量
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "levelId": 2374,
      "area": "华南区",
      "planCount": 8529,
      "realCount": 2147
    },
    ...
	]
}
```

## 13. 计算器
- url: `http://domain/calculators`
- 请求方式： `POST`
- 入参位置：Request Body
- 入参参数：
	- `carTypeId`: 车型id
	- `financeProductId`: 融资产品id
	- `sellingPrice`: 车价
	- `gpsPrice`: gps费用
	- `otherPrice`: 其他费用(保险/人身意外保障/其他等)
	- `financePeriod`: 融资期限(自贸融明白、明白融融资期限必须为12的整数倍)
	- `downPay`: 首付比例
	- `payMode`: 支付方式,0为一次性收取手续费,1为分期支付(明白融产品必传参数，其他可不传)
- 入参示例：
```javascript
{
    "carTypeId":1,
    "financeProductId":169,
    "sellingPrice":10000,
    "gpsPrice": 1000,
    "otherPrice":1000,
    "financePeriod":12,
    "downPay":0.9,
    "payMode":0
}
```
- 出参参数：
	- `financeAmount`: 融资金额
	- `monthPay`: 月供
	- `dayPay`: 日供
	- `downPay`: 首付
	- `deposit`: 保证金（明白融有保证金这一项，其他没有）
	- `totalInterest`: 利息总额
	- `yearInterest`: 年均利息
	- `yearEarnings`: 年投资收益（按8%计算）
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "financeAmount": 2000,
    "monthPay": 166.66666666666666,
    "dayPay": 5.555555555555555,
    "downPay": 9000,
    "deposit": 400,
    "totalInterest": 0,
    "yearInterest": 0,
    "yearEarnings": 160
  }
}
```


## 14. 获取计算历史记录
- url: `http://domain/calculators/records`
- 请求方式： `GET`
- 入参位置：Request Parameter
- 入参参数：
	- `startDate`: 起始日期（可选，格式：20160909）
	- `endDate`: 结束日期（可选，格式：20160909）
	- `page`: 当前页（从1开始）
	- `size`: 每页条数
- 入参示例：
	http://domain/calculators/records?startDate=20160701&endDate=20160916&page=1&size=10
- 出参参数：
	- `number`: 当前页（从0开始）
	- `size`: 每页条数
	- `numberOfElements`: 每页条数
	- `hasNext`: 是否有下一页
	- `hasPrevious`: 是否有上一页
	- `totalPages`: 总页数
	- `totalElements`: 总记录数
	- `last`: 是否是最后一页
	- `first`: 是否是第一页
	- `content`:
		- `id`: 历史记录id
		- `condition`: 用户输入的参数
			- `carTypeId`: 车型id
			- `financeProductId`: 融资产品id
			- `sellingPrice`: 车价
			- `gpsPrice`: gps费用
			- `otherPrice`: 其他费用
			- `financePeriod`: 融资期限
			- `downPay`: 首付比例
			- `carTypeName`: 车型名称
			- `financeProductName`: 融资产品名称
		- `record`:
			- `financeAmount`: 融资金额
			- `monthPay`: 月供
			- `dayPay`: 日供
			- `downPay`: 首付
			- `deposit`: 保证金（明白融有保证金这一项，其他没有）
			- `totalInterest`: 利息总额
			- `yearInterest`: 年均利息
			- `yearEarnings`: 年投资收益（按8%计算）
		- `time`: 计算时间
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "number": 0,
    "size": 5,
    "numberOfElements": 5,
    "content": [
      {
        "id": 20212,
        "condition": {
          "carTypeId": 1,
          "financeProductId": 169,
          "sellingPrice": 10000,
          "gpsPrice": 1000,
          "otherPrice": 1000,
          "financePeriod": 12,
          "downPay": 0.9,
          "carTypeName": "乘用车",
          "financeProductName": "明白融2证1卡产品"
        },
        "record": {
          "financeAmount": 2000,
          "monthPay": 166.66666666666666,
          "dayPay": 5.555555555555555,
          "downPay": 9000,
          "deposit": 400,
          "totalInterest": 0,
          "yearInterest": 0,
          "yearEarnings": 160
        },
        "time": 1473817928251
      },
      ...
    ],
    "hasNext": true,
    "hasPrevious": false,
    "totalPages": 4,
    "totalElements": 20,
    "last": false,
    "first": true
  }
}
```


## 15. 获取app最新版本信息
- url: `http://222.73.56.31:8080/appVersions/latest`(测试)
- 请求方式： `GET`
- 入参位置：Request Parameter
- 入参参数：
	- `type`: app类型，安卓为0，ios为1
- 入参示例：
	http://222.73.56.31:8089/appVersions/latest?type=0
- 出参参数：
	- `version`: 版本号
	- `downloadUrl`: 下载地址(文件名+后缀名,前缀为固定的:http://222.73.56.31:89/android/)
	- `description`: 更新描述
	- `updateTime`: 更新时间
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "version": "v1.0.1",
    "downloadUrl": "log",
    "description": "222",
    "updateTime": 1474354031000
  }
}
```

## 16. 获取app最新版本信息
- url: `http://222.73.56.31:8080/appVersions/latest`
- 请求方式： `GET`
- 入参位置：Request Parameter
- 入参参数：
	- `type`: app类型，安卓为0，ios为1
- 入参示例：
	http://222.73.56.31:8080/appVersions/latest?type=0
- 出参参数：
	- `version`: 版本号
	- `downloadUrl`: 下载地址(文件名+后缀名,前缀为固定的:http://222.73.56.31:89/android/)
	- `description`: 更新描述
	- `updateTime`: 更新时间
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "version": "v1.0.1",
    "downloadUrl": "log",
    "description": "222",
    "updateTime": 1474354031000
  }
}
```

## 17. 上传征信拍照信息
- url: `http://domain/sysUsers/uploadCreditMsg`
- 请求方式: `POST`
- 入参位置: Request Body
- 入参参数:
  - `type`: 拍照类型:1:申请人,2:配偶,3:共申人,4:担保人(必填)
  - `idCardNum`: 身份证号码(必填)
  - `name`: 姓名(必填)
  - `phone`: 联系方式(必填)
  - `idCardPostiveImg`: 身份证正面
  - `idCardOppositeImg`: 身份证反面
  - `idCardComposeImg`: 身份证正反两面合成照片
  - `faceImg`: 人脸大头照
  - `authBookImg`: 征信授权书
  - `holdAuthBookImg`: 手持征信授权书
- 入参示例:
```javascript
[
  {
    "authBookImg": "http://**/img.jpg",
    "faceImg": "http://**/img.jpg",
    "holdAuthBookImg": "http://**/img.jpg",
    "idCardNum": "430215199306235564",
    "idCardOppositeImg": "http://**/img.jpg",
    "idCardPostiveImg": "http://**/img.jpg",
    "idCardComposeImg": "http://**/img.jpg",
    "name": "张三",
    "phone": "18649520589",
    "type": 1
  },
  {
    "authBookImg": "http://**/img.jpg",
    "faceImg": "http://**/img.jpg",
    "holdAuthBookImg": "http://**/img.jpg",
    "idCardNum": "430215199306235564",
    "idCardOppositeImg": "http://**/img.jpg",
    "idCardPostiveImg": "http://**/img.jpg",
    "idCardComposeImg": "http://**/img.jpg",
    "name": "张三",
    "phone": "18649520589",
    "type": 2
  },
  {
    "authBookImg": "http://**/img.jpg",
    "faceImg": "http://**/img.jpg",
    "holdAuthBookImg": "http://**/img.jpg",
    "idCardNum": "430215199306235564",
    "idCardOppositeImg": "http://**/img.jpg",
    "idCardPostiveImg": "http://**/img.jpg",
    "idCardComposeImg": "http://**/img.jpg",
    "name": "张三",
    "phone": "18649520589",
    "type": 3
  },
  {
    "authBookImg": "http://**/img.jpg",
    "faceImg": "http://**/img.jpg",
    "holdAuthBookImg": "http://**/img.jpg",
    "idCardNum": "430215199306235564",
    "idCardOppositeImg": "http://**/img.jpg",
    "idCardPostiveImg": "http://**/img.jpg",
    "idCardComposeImg": "http://**/img.jpg",
    "name": "张三",
    "phone": "18649520589",
    "type": 4
  }
]
```

## 18. 文件上传
- url: `http://domain/files`
- 请求方式: `POST`
- 入参位置: Request Parameter
- 入参参数:
  - `type`: 上传类型(征信拍照:creditImg, 信息反馈录音:feedbackSound, 头像:headImg, 附件:annexesImg)
- 入参位置: Request Body
- 入参参数:
  - `file`: 文件
- 出参参数:
  - `url`: 文件路径
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "url": "http://localhost/creditImg/471e5baf-0d98-47b6-8110-8f4bbdb70b7a.jpg"
  }
}
```

## 19. 新增信息反馈
- url: `http://domain/sysUsers/feedback`
- 请求方式: `POST`
- 入参位置: Request Body
  - `content`: 文字反馈信息
  - `soundUrl`: 录音url
- 入参示例:
```javascript
{
  "content": "http://xxx/xx.jpg",
  "soundUrl": "15555452767"
}
```

## 20. 更新个人信息
- url: `http://domain/sysUsers/{userName}/userInfo`
- 请求方式: `PUT`
- 入参位置: Path Variable
  - `userName`: 用户名
- 入参位置: Request Body
  - `headImg`: 头像url(必传)
  - `email`: 电子邮件(必传)
  - `phoneNum`: 电话(必传)
- 入参示例:
```javascript
{
  "headImg": "http://xxx/xx.jpg",
  "email": "1232834@qq.com",
  "phoneNum": "15555452767"
}
```

## 21. 查询资讯列表
- url: `http://domain/informations`
- 请求方式: `GET`
- 入参位置: Request Parameter
- 入参参数:
  - `type`: 资讯类型(1:新闻,2:产品介绍,3:公告通知,4:行业资讯)
  - `page`: 当前页(从1开始)
  - `size`: 条数
- 入参示例:
  http://domain/informations?type=1&page=1&size=10
- 出参参数:
  - `number`: 当前页（从0开始）
  - `size`: 每页条数
  - `numberOfElements`: 每页条数
  - `hasNext`: 是否有下一页
  - `hasPrevious`: 是否有上一页
  - `totalPages`: 总页数
  - `totalElements`: 总记录数
  - `last`: 是否是最后一页
  - `first`: 是否是第一页
  - `content`: 内容(数组)
    - `id`: 新闻id
    - `title`: 新闻标题
    - `createDate`: 创建时间
    - `author`: 作者
    - `imgUrls`: 图片url集合
    - `tag`: 标签
    - `params`
    - `pageView`
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "totalElements": 3,
    "content": [
      {
        "id": 53440,
        "title": "sdasf",
        "createDate": "2016-10-10 14:55:55.079",
        "author": "admin",
        "imgUrls": [
          "http://localhost/information/news/5b64efba-c099-4307-aa91-01b6c77b47d1.jpg",
          "http://localhost/information/news/96e77702-cfde-4877-803c-2c4df25b5239.jpg",
          "http://localhost/information/news/a9b1924d-4871-4114-8578-c60a78c60495.jpg"
        ],
        "tag": ""
        "params": "{\"title\":\"test yachi\",\"content\":\"<p>​<img src=\"http://222.73.56.31:89/information/news/4f5842a2-87ca-43ce-a011-aa371b7c7bbd.png\"/>测试亚驰</p>\",\"imgUrls\":\"http://222.73.56.31:89/information/news/4f5842a2-87ca-43ce-a011-aa371b7c7bbd.png\",\"type\":1}",
        "pageView": 0
      },
      ...
    ],
    "number": 0,
    "size": 3,
    "numberOfElements": 10,
    "hasNext": false,
    "hasPrevious": false,
    "totalPages": 1,
    "first": true,
    "last": true
  }
}
```

## 22. 查询资讯详情
- url: `http://domain/informations`
- 请求方式: `GET`
- 入参位置: Path Variable
- 入参参数:
  - `infoId`: 新闻id
- 入参示例:
  http://domain/informations/53439
- 出参为html页面

## 23. 查询资料列表
- url: `http://domain/companyFiles`
- 请求方式: `GET`
- 入参位置: Request Parameter
- 入参参数:
  - `type`: 资料类型(1:培训资料,2:公司内刊)
  - `page`: 当前页(从1开始)
  - `size`: 条数
- 入参示例:
  http://domain/companyFiles?type=1&page=1&size=10
- 出参参数:
  - `number`: 当前页（从0开始）
  - `size`: 每页条数
  - `numberOfElements`: 每页条数
  - `totalPages`: 总页数
  - `totalElements`: 总记录数
  - `last`: 是否是最后一页
  - `first`: 是否是第一页
  - `content`: 内容(数组)
    - `id`: 资料id
    - `title`: 资料标题
    - `createDate`: 创建时间
    - `author`: 作者
    - `url`: 资料url
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "content": [
      {
        "id": 53455,
        "title": "公司培训",
        "url": "http://localhost/information/companyFile/045a25d2-a9fe-4ee9-9aa9-c87215f8588f.pdf",
        "type": 1,
        "createDate": 1476430032759,
        "author": "admin",
        "download": 0,
        "topTime": 1476430032759
      },
      ...
    ],
    "last": true,
    "totalPages": 1,
    "totalElements": 5,
    "size": 10,
    "number": 0,
    "first": true,
    "sort": null,
    "numberOfElements": 5
  }
}
```

## 24. 更新下载量(用户下载资料时调用)
- url: `http://domain/companyFiles/{companyFileId}/download`
- 请求方式: `PUT`
- 入参位置: Path Variable
- 入参参数:
  - `companyFileId`: 资料id
- 无出参

## 25. 获取轮播图列表
- url: `http://domain/informations/banners?size=3`
- 请求方式: `GET`
- 入参位置: Request Parameter
- 入参参数:
  - `size`: 轮播图数量
- 出参参数:
  - `number`: 当前页（从0开始）
  - `size`: 每页条数
  - `numberOfElements`: 每页条数
  - `hasNext`: 是否有下一页
  - `hasPrevious`: 是否有上一页
  - `totalPages`: 总页数
  - `totalElements`: 总记录数
  - `last`: 是否是最后一页
  - `first`: 是否是第一页
  - `content`: 内容(数组)
    - `id`: 新闻id
    - `title`: 新闻标题
    - `createDate`: 创建时间
    - `author`: 作者
    - `imgUrls`: 图片url集合
    - `tag`: 标签
    - `params`
    - `pageView`
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "totalElements": 3,
    "content": [
      {
        "id": 53440,
        "title": "sdasf",
        "createDate": "2016-10-10 14:55:55.079",
        "author": "admin",
        "imgUrls": [
          "http://localhost/information/news/5b64efba-c099-4307-aa91-01b6c77b47d1.jpg",
          "http://localhost/information/news/96e77702-cfde-4877-803c-2c4df25b5239.jpg",
          "http://localhost/information/news/a9b1924d-4871-4114-8578-c60a78c60495.jpg"
        ],
        "tag": ""
        "params": "{\"title\":\"test yachi\",\"content\":\"<p>​<img src=\"http://222.73.56.31:89/information/news/4f5842a2-87ca-43ce-a011-aa371b7c7bbd.png\"/>测试亚驰</p>\",\"imgUrls\":\"http://222.73.56.31:89/information/news/4f5842a2-87ca-43ce-a011-aa371b7c7bbd.png\",\"type\":1}",
        "pageView": 0
      },
      ...
    ],
    "number": 0,
    "size": 3,
    "numberOfElements": 10,
    "hasNext": false,
    "hasPrevious": false,
    "totalPages": 1,
    "first": true,
    "last": true
  }
}
```

## 26. 退出登录
- url: `http://domain/logout`
- 请求方式: `GET`
- 入参参数: 无
- 出参参数: 无

## 37. app端首页加载
- url: `http://domain/init`
- 请求方式: `GET`
- 入参位置: Request Parameter
- 入参参数:
  - `bannerNum`: banner数量
  - `infoNum`: 新闻数量
  - `noticeNum`: 公告数量
- 入参示例:
  http://domain/init?bannerNum=3&infoNum=1&noticeNum=1
- 出参参数:
  - `newsList`: 新闻列表
    - `id`: 新闻id
    - `title`: 新闻标题
  - `noticeList`: 公告列表
  - `bannerList`: banner列表
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "newsList": [
      {
        "id": 9542,
        "title": "韩勇：融资租赁已爆发，今年余额将达6.9万亿",
        "createDate": "2016-10-21 14:28:35.199",
        "author": "admin",
        "imgUrls": [
          "http://222.73.56.22:89/information/news/9f28f6ec-c0ea-41c9-9828-ec4f784bc999.jpg"
        ],
        "tag": "",
        "params": null,
        "pageView": 0
      }
    ],
    "noticeList": [
      {
        "id": 19525,
        "title": "风雨同舟 携手共进",
        "createDate": "2016-10-24 16:57:53.212",
        "author": "admin",
        "imgUrls": [
          "http://222.73.56.22:89/information/news/5dee2a3a-168c-41c2-9649-63fc9e6d0550.png"
        ],
        "tag": "",
        "params": null,
        "pageView": 0
      }
    ],
    "bannerList": [
      {
        "id": 7680,
        "title": "HPL人生不止一种选择",
        "createDate": "2016-10-18 19:06:39.495",
        "author": "admin",
        "imgUrls": [
          "http://222.73.56.22:89/information/news/ecb51e21-17b8-41ed-a0c9-135cf27b162f.jpg"
        ],
        "tag": "",
        "params": null,
        "pageView": 0
      },
      {
        "id": 7675,
        "title": "聚力创赢",
        "createDate": "2016-10-18 18:48:09.204",
        "author": "admin",
        "imgUrls": [
          "http://222.73.56.22:89/information/news/cbce19d1-e8d0-44b1-9404-79814ae192cd.jpg"
        ],
        "tag": "",
        "params": null,
        "pageView": 0
      },
      {
        "id": 7674,
        "title": "车主“意外保障”",
        "createDate": "2016-10-18 18:47:13.69",
        "author": "admin",
        "imgUrls": [
          "http://222.73.56.22:89/information/news/d3d89e73-8084-4e5e-9620-9514f59b8e89.jpg"
        ],
        "tag": "",
        "params": null,
        "pageView": 0
      }
    ]
  }
}
```

## 40. 按日期查询逾期率统计
- url: `http://domain/overdueRate/getOverDueRateByDate`
- 请求方式： `GET`
- 入参位置： `Request Parameter`
- 入参参数：
	- `type`: 日期查询类型：1:按年查询 2:按月查询 3:按日查询
	- `startDate`: 开始日期: 按年查询2016 按月查询201601 按日查询20160101
	- `endDate`: 结束日期: 按年查询2016 按月查询201601 按日查询20160101
	- `levelId`: 区域id
- 入参示例：
    http://domain/overdueRate/getOverDueRateByDate?type=3&startDate=20161109&endDate=20161109&levelId=2791

- 出参参数：
	- `dates`: 日期
	- `remain`: 保有量
	- `overdue`: 30+逾期量
	- `overdueRate`: 30+逾期率(保留2位小数)
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "overdueRate": ".25",
      "remain": "4",
      "dates": "20161110",
      "overdue": "1"
    },
    {
      "overdueRate": ".31",
      "remain": "456",
      "dates": "20161109",
      "overdue": "145"
    },
    ...
	]
}
```

## 41. 按区域查询逾期率统计
- url: `http://domain/overdueRate/getOverDueRateByArea`
- 请求方式： `GET`
- 入参位置： `Request Parameter`
- 入参参数：
	- `date`: 查询日期: 20160101
	- `levelId`: 区域id
- 入参示例：
	http://domain/overdueRate/getOverDueRateByArea?date=20160919&levelId=2791
- 出参参数：
      - `areaName`: 区域名称
      - `levelId`: 区域ID
      - `overdue`: 逾期量
      - `overdueRate`: 逾期率
      - `remain`: 保有量
      - `pId`: 传入区域ID
      - `intoPId`: 传入区域上级区域ID
      - `intoName`: 传入区域名称
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "intoPId": "81",
      "overdue": "3",
      "overdueRate": "0.0128",
      "areaName": "辽宁省",
      "remain": "234",
      "levelId": "2783",
      "intoName": "北区",
      "pId": "2375"
    },
    {
      "intoPId": "81",
      "overdue": "83",
      "overdueRate": "0.0159",
      "areaName": "内蒙古自治区",
      "remain": "5216",
      "levelId": "2781",
      "intoName": "北区",
      "pId": "2375"
    },
    ...
  ]
}
```

## 42. 获取直属下级区域
- url: `http://domain/sales/childLevel`
- 请求方式： `GET`
- 入参位置: Request Parameter
- 入参参数:
  - `levelId`: 区域ID
- 入参示例：
	http://domain/sales/childLevel?levelId=81
- 出参参数：
	- `areaName`: 当前区域名称
	- `levelId`: 当前区域ID
	- `qyjb`: 当前区域级别
	- `sjjgid`: 上级区域ID
	- `areaList`: 下级区域
	  - `areaName`: 区域名称
	  - `levelId`: 区域ID
	  - `qyjb`: 区域级别
	  - `sjjgid`: 上级区域ID
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "areaName": "HPL",
    "levelId": "2375",
    "qyjb": "HPL",
    "sjjgid": "81",
    "areaList": [
       {
           "areaName": "内蒙古自治区",
           "qyjb": "省份",
           "levelId": "2781",
           "sjjgid": "2375"
        },
      ...
    ]
  }
}
```

## 47. 获取客户端最新版本描述
- url: `http://222.73.56.31:8080/appVersions/latest/description`
- 请求方式： `GET`
- 入参位置: Request Parameter
- 入参参数:
  - `type`: 客户端类型,0为安卓
- 入参示例：
	http://222.73.56.31:8080/appVersions/latest/description?type=0
- 出参为html页面
