 ## 接口更新:
### 1.新增 "# 230. 查询是否需要视频面签"
### 2.新增 "# 231. 工行面签报告提交"
### 3.新增 "# 232. 面签数量加一"
### 4.新增 "# 233. 面签数量减一"
### 5.新增 "# 234. 查询签约排队数量"
### 6.新增 "# 235. 查询签约账号信息"
### 7.新增 "# 236. 查询预审批申请中的状态"
### 8.新增 "## 237. 发送微众一审页面链接"
### 9.修改 "# 232. 面签数量加一"
### 10.修改 "# 233. 面签数量减一一"
### 11.修改 "### 3. 查询合同申请审批日志"


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

## 3. domain
- `ip`:222.73.56.22
- `port`:80

## 4. 全局返回值
- `status`: 请求状态（成功为SUCCESS,失败为ERROR）
- `error`: 请求失败时，服务器的错误信息(status=ERROR且error=10086时,提示在另一处登录,跳转到登录页)
- `data`: 返回的数据

示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
  }
}
```

```javascript
{
    "status": "ERROR",
    "error": "10086",
    "data": null
}
```


# 二、接口

## 1. 登录
- url: `http://domain/user`
- 请求方式： `GET`
- 入参位置： `Request Header`
- 入参参数：
	- `Authorization`: 认证信息
	- `deviceToken`: 友盟设备token(app端调用所有接口都需要传)
- 入参解释
```
    对“账号:密码”字符串进行base64加密，得到密钥与字符串“Basic ”拼接，以键值对的形式存放到headers中，键值为“Authorization”，app端调用所有授权接口都需要传。
```
- 入参位置: `Request Parameter`
- 入参参数:
  - `loginAddress`: 登录地点(非必传参数)
  - `deviceType`: 设备类型（必传）
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
  - `code`: 邀请码（userType不为"3"时才显示邀请码）
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
      "areaName": "HPL",
      "isHplUser": true,
      "levelId": 81,
      "userType": 0,
      "loginRecord": {
        "address": "上海",
        "time":
      },
      "userName": "admin",
      "permissions": [
        {
          "id": 1,
          "name": "yuqilv"
        },
      "code": "j786",
        ...
      ]
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
	http://domain/contracts/state?applyNum=36146445&fpName=&startDate=&endDate=&state=&page=1
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
	http://domain/contracts/36146445/log
- 出参参数：
	- `contractinfo`: 合同信息
		- `BASQXM`: 申请姓名
		- `BASQBH`: 申请编号
        - `applyState`: 主申人预审批状态
        - `mateApplyState`: 配偶状态
            - `icbc`: 工行预审状态（0：拒绝，1：通过，2：退回，3：审批中）
            - `tq`: 天启预审批状态（0：拒绝，1：通过，2：退回，3审批中）
            - `webank`: 微众一审状态（0：拒绝，1：通过，2：退回）  
                          
		- `contractstatelist`:
			- `XTCZRY`: 审批人
			- `BAGQZT`: 挂起状态
			- `BASQZT`: 申请状态
			- `BATHYY`: 挂起,取消,拒绝备注(申请状态为条件通过时，附加该备注)
			- `BASHRQ`: 状态时间
			- `RGTHYY`: 人工退回原因
- 出参示例：

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "contractinfo": {
            "BASQBH": "38160034",
            "BASQXM": "于朝霞",
            "applyState": {
                "icbc": "",
                "tq": "2",
                "webank": "1"
            },
            "mateApplyState": {
                "icbc": "",
                "tq": "2",
                "webank": ""
            },
            "contractstatelist": [
                {
                    "BAGQZT": "",
                    "BASHRQ": "20180828154223",
                    "BASQZT": "放款",
                    "BATHYY": "",
                    "RGTHYY": "无",
                    "XTCZRY": "liuxiaohong"
                },
                {
                    "BAGQZT": "",
                    "BASHRQ": "20180828153253",
                    "BASQZT": "待材料归档",
                    "BATHYY": "",
                    "RGTHYY": "无",
                    "XTCZRY": "zhuye"
                },
                {
                    "BAGQZT": "",
                    "BASHRQ": "20180828152046",
                    "BASQZT": "GPS保单审批",
                    "BATHYY": "",
                    "RGTHYY": "无",
                    "XTCZRY": "zhuye"
                },
                {
                    "BAGQZT": "",
                    "BASHRQ": "20180828151242",
                    "BASQZT": "合同审核",
                    "BATHYY": "",
                    "RGTHYY": "无",
                    "XTCZRY": "zhuye1"
                },
                {
                    "BAGQZT": "",
                    "BASHRQ": "20180828150619",
                    "BASQZT": "签约阶段",
                    "BATHYY": "",
                    "RGTHYY": "无",
                    "XTCZRY": "SH000"
                },
                {
                    "BAGQZT": "",
                    "BASHRQ": "20180828150333",
                    "BASQZT": "66510000：交易成功",
                    "BATHYY": "",
                    "RGTHYY": "无",
                    "XTCZRY": "超级管理员"
                },
                {
                    "BAGQZT": "",
                    "BASHRQ": "20180828150223",
                    "BASQZT": "66510000:交易成功",
                    "BATHYY": "",
                    "RGTHYY": "无",
                    "XTCZRY": ""
                },
                {
                    "BAGQZT": "",
                    "BASHRQ": "20180828150107",
                    "BASQZT": "审批通过",
                    "BATHYY": "",
                    "RGTHYY": "无",
                    "XTCZRY": "maofengxian"
                },
                {
                    "BAGQZT": "",
                    "BASHRQ": "20180828145828",
                    "BASQZT": "审批阶段",
                    "BATHYY": "",
                    "RGTHYY": "无",
                    "XTCZRY": "maofengxian1"
                },
                {
                    "BAGQZT": "",
                    "BASHRQ": "20180828144650",
                    "BASQZT": "创建申请",
                    "BATHYY": "",
                    "RGTHYY": "无",
                    "XTCZRY": "SH000"
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
		- `BARYBJ`: 融延保
		- `BARAXB`: 融安心宝
		- `BARBXJ`: 融保险
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "contractinfo": {
      "BABXJE": "4677",
      "BASXFS": "",
      "BASFJE": "8888",
      "BAXSJG": "35900",
      "BAWFJE": "0",
      "BACLCX": "2014款1.3L幸福型",
      "BACLPP": "欧诺",
      "BAYZYG": "1745.95",
      "BARZQX": "24",
      "BASQBH": "36157040",
      "BARAXB": "0",
      "BABZJE": "0",
      "BACLLX": "新车",
      "BASXFJ": "0",
      "BATZZE": "44311",
      "BAGPSY": "0",
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
	http://domain/contracts/repayment?applyNum=&fpName=&startDate&endDate&state=&page=1
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
	http://domain/contracts/1234426/repayDetail
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
            - `BBKHBJ`: 本金
            - `BBKHLX`: 利息

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
          "BAHKYG": "10000",
          "BASQQS": "",
          "BBKHBJ": "850",
          "BBKHLX": "150"
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
	- `queryAll`: 是否查询已下线的数据(非必传 是,否)
	
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
	- `code`:邀请码(返回""或null时画面不显示)
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
    "code": "111222"
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

## 13. 获取融资方案(车型,融资产品)
- url: `http://domain/financeProducts`
- 请求方式： `GET`
- 出参参数：
	- `id`: 车型id
	- `name`: 车型名称
	- `used`: 类别（0为新车，1为二手车）
	- `financeProducts`: 适用产品
		- `id`: 融资产品id
		- `name`: 产品名称
		- `downPay`: 最低首付
		- `minPeriod`: 最小融资周期(月)
		- `maxPeriod`: 最大融资周期(月)
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "id": 1,
      "name": "乘用车",
      "used": 0,
      "financeProducts": [
        {
          "id": 101,
          "name": "轻松融",
          "downPay": 0.2,
          "minPeriod": 12,
          "maxPeriod": 60,
        }
        ...
      ]
    },
    {
      "id": 2,
      "name": "轻客",
      "used": 0,
      "financeProducts": [
        {
          "id": 107,
          "name": "轻客轻松融",
          "downPay": 0.2
          "minPeriod": 12,
          "maxPeriod": 36
        }
        ...
      ]
    },
    {
      "id": 11,
      "name": "皮卡",
      "used": 1,
      "financeProducts": [
        {
          "id": 149,
          "name": "微面倾心融",
          "downPay": 0.2
          "minPeriod": 12,
          "maxPeriod": 36
        }
        ...
      ]
    }
    ...
  ]
}
```

## 14. 计算器
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

## 15. 获取计算历史记录
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

## 16. 获取app最新版本信息
- url: `http://222.73.56.22:8089/appVersions/latest`
- 请求方式： `GET`
- 入参位置：Request Parameter
- 入参参数：
	- `type`: app类型，安卓为0，ios为1
- 入参示例：
	http://222.73.56.22:8089/appVersions/latest?type=0
- 出参参数：
	- `version`: 版本号
	- `downloadUrl`: 下载地址(文件名+后缀名,前缀为固定的:http://222.73.56.22:89/android/)
	- `description`: 更新描述
	- `updateTime`: 更新时间
	- `mustUpdate`: 是否需要强制更新 true时需要强制更新
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "version": "v1.0.1",
    "downloadUrl": "log",
    "description": "222",
    "updateTime": 1474354031000,
    "mustUpdate": "true"
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
  - `type`: 上传类型(征信拍照:creditImg, 信息反馈录音:feedbackSound, 头像:headImg, 附件:annexesImg，
                     预审批身份证：idCard，驾驶证：drivingLicence，银行卡：bankCard，法院判决书：verdict,
                     二手车评估：usedCarEvaluation，征信授权书: letterOfCredit, 手持征信授权书: handHoldLetterOfCredit,
                     申请表照片：applyForm, 共申人附件:jointFile， 配偶附件:mateFile， 担保人附件:guaranteeFile,
                     请款文件信息: requestPaymentFile,请款保单信息: insurancePolicyPath, 在线沟通文件: onlineCommunication)
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
    "url": "http://localhost/approvalIdCard/2017-05-12/6db0a2a2-d2f3-44c7-aa05-121e2da15b46.jpg"
  }
}
```

## 19. 新增信息反馈
- url: `http://domain/sysUsers/feedback`
- 请求方式: `POST`
- 入参位置: Request Body
  - `content`: 文字反馈信息
  - `soundUrl`: 录音url

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
  "headImg": "http://xxx/xx.jpg"
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
  - `hasNext`: 是否有下一页
  - `hasPrevious`: 是否有上一页
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

## 27. 获取省份列表
- url: `http://domain/provinces`
- 请求方式: `GET`
- 入参参数: 无
- 出参参数:
  - `ProID`: 省份id
  - `name`: 省份名称
  - `ProSort`: 序号
  - `ProRemark`: 类型
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "name": "北京市",
      "proID": 1,
      "proSort": 1,
      "proRemark": "直辖市"
    },
    ...
  ]
}
```

## 28. 获取城市列表
- url: `http://domain/Provinces/{provinceId}/cities`
- 请求方式: `GET`
- 入参位置: Path Variable
- 入参参数:
  - `provinceId`: 省份id
- 入参示例:
  http://domain/Provinces/1/cities
- 出参参数:
  - `CityID`: 城市id
  - `name`: 城市名称
  - `ProID`: 所属省份id
  - `CitySort`: 序号
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "name": "邯郸市",
      "citySort": 5,
      "proID": 3,
      "cityID": 5
    },
    ...
  ]
}
```

## 29. 获取融资产品大类
- url: `http://domain/financeProducts/core`
- 请求方式: `GET`
- 入参位置: Request Parameter
- 入参参数:
  - `carType`: 车辆类型名称
- 入参示例:
  http://domain/financeProducts/core?carType=乘用车
- 出参参数:
  - `ID`: 融资产品大类id
  - `BADLMC`: 融资产品大类名称
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
          "BACXGS": "",
          "CWFGMC": "",
          "BADLBZ": "",
          "flowHref": "",
          "BADLMC": "快易融",
            ...
          "ID": 375,
            ...
        },
    ...
  ]
}
```

## 30. 获取融资产品详情
- url: `http://domain/financeProducts/{financeProductId}`
- 请求方式: `GET`
- 入参位置: Request Parameter
- 入参参数:
  - `financeProductId`: 融资产品id
- 入参示例:
  http://domain/financeProducts/1910001
- 出参参数: （逻辑: '1':'大于','2':'大于等于','3':'等于','4':'小于','5' :'小于等于','6':'不比较'）
  - `wfbllj`: 尾款比例逻辑
  - `basxfl`: 手续费率
  - `bzjebl`: 保证金比例
  - `sfsqfs`: 手续费收取方式
  - `sxfllj`: 手续费逻辑
  - `badkqx`: 贷款期限
  - `bzjelj`: 保证金比例逻辑
  - `bakhll`: 客户利率
  - `basfbl`: 首付比例
  - `basfje`: 首付款
  - `bajsll`: 结算利率
  - `wfjelj`: 尾款逻辑
  - `sfjelj`: 首付款逻辑
  - `sfbllj`: 首付比例逻辑
  - `bawfje`: 尾款
  - `bawfbl`: 尾款比例
  - `yb`: 延保（不可融，可融）
  - `gps`: 租赁管理费收取方式（可选，不可选）
  - `gzs`: 购置税是否可融 （不可融，可融）
  - `yyfs`: 营运方式 （支持，不支持）
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "sfjelj": "",
    "bawfje": "0",
    "sxfllj": "3",
    "gzs": "不可融",
    "yb": "可融",
    "basfje": "0",
    "wfbllj": "3",
    "bajsll": "20.68",
    "gps": "不可选",
    "basxfl": "0",
    "badkqx": "12, 24, 36",
    "sfsqfs": "分期收取",
    "bawfbl": "0",
    "bzjelj": "3",
    "sfbllj": "2",
    "yyfs": "不支持",
    "wfjelj": "3",
    "bakhll": "20.68",
    "basfbl": "20",
    "bzjebl": "0"
  }
}
```

## 31. 获取车辆列表
- url: `http://domain/cars`
- 请求方式: `GET`
- 入参位置: Request Parameter
- 入参参数:
  - `type`: 类型 1.查询厂商 2.查询品牌 3.查询车型 必传
  - `financeProductId`: 融资产品ID 必传
  - `conditionId`: 条件id 查询品牌需要输入厂商ID,查询车型需要品牌ID 必传
- 入参示例:
  http://domain/cars?type=1&financeProductId=1005459&conditionId
- 出参参数:
  - `balbid`: 车辆id
  - `balbmc`: 名称
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "balbid": "200238",
      "balbmc": "布加迪"
    },
    ...
  ]
}
```

## 32. 查询车辆详情
- url: `http://domain/cars/{carId}`
- 请求方式: `GET`
- 入参位置: Path Variable
- 入参参数:
  - `carId`: 车辆ID
- 入参示例:
  http://domain/cars/86151
- 出参参数:
  - `baclpl`: 排量
  - `bacldw`: 吨位
  - `bazdjg`: 指导价
  - `bazwsl`: 座位数
  - `backgz`: 参考购置税
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "baclpl": "1.497",
    "bacldw": "",
    "backgz": "5111"
    "bazdjg": "59800",
    "bazwsl": "5"
  }
}
```

## 33. 获取GPS硬件价格
- url: `http://domain/sales/gpsPrice`
- 请求方式: `GET`
- 入参位置: Request Parameter
- 入参参数:
    - `typeId`: 产品大类ID
    - `productCase`: 产品方案
- 入参示例:
    http://domain/sales/gpsPrice?productCase=轻松融A套餐&typeId=402
- 出参参数:
  - `gpsje`: GPS价格
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
  {
        "gpsje": "1000"
      },
      {
        "gpsje": "2200"
      },
      {
        "gpsje": "3000"
      },
      {
        "gpsje": "3600"
      }
  ]
}
```

## 34. 获取经销商
- url: `http://domain/sales/fp`
- 请求方式: `GET`
- 入参位置: Request Parameter
- 入参参数:
    - `type`: 类型 1.FP市场助理 2.FP销售经理 必传
- 入参示例:
    http://domain/sales/fp?type=1
- 出参参数:
  - `fpscmc`: 岗位名称
  - `fpscid`: 岗位ID
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
  {
    "fpscmc": "曹亚会",
    "fpscid": "1974"
  },
  ...
  ]
}
```

## 35. 获取回租抵押城市
- url: `http://domain/sales/pledgeCities`
- 请求方式: `GET`
- 入参位置: 无
- 入参参数: 无
- 入参示例: 无
- 出参参数:
  - `spdycs`: 上牌抵押城市
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
  {
    "spdycs": "合肥市"
  },
  ...
  ]
}
```

## 36. 获取还款借记卡开户行
- url: `http://domain/sales/banks`
- 请求方式: `GET`
- 入参位置: 无
- 入参参数: 无
- 入参示例: 无
- 出参参数:
  - `BAHKYH`: 银行名
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
  {
    "BAHKYH": "中国工商银行"
  },
  ...
  ]
}
```

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

## 38. 获取行业列表
- url: `http://domain/industries`
- 请求方式: `GET`
- 入参位置: 无
- 入参参数: 无
- 入参示例:
  http://domain/industries
- 出参参数:
  - `name`: 行业名称
  - `children`: 下属行业
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "name": "计算机/互联网/通信/电子",
      "children": [
        {
          "name": "计算机软件"
        }
      ]
    }
  ]
}
```

## 39. 保险测算
- url: `http://domain/sales/insurance`
- 请求方式: `POST`
- 入参位置: Request Body
- 入参参数:
  - `badycs`: 抵押城市
  - `bazwsl`: 座位数量
  - `baszzr`: 三者责任险
  - `baclzd`: 车辆指导价
  - `ryzrxsj`: 车上人员责任险（司机）
  - `ryzrxsjxe`: 车上人员责任险（司机）限额
  - `ryzrxck`: 车上人员责任险（乘客）
  - `ryzrxckxe`: 车上人员责任险（乘客）限额
  - `babjmp`: 车上人员责任险不计免赔
  - `bacshh`: 车身划痕险
  - `badqxe`: 盗抢险(传true)
  - `cshhxe`: 车身划痕险限额(bacshh=falses时传入0)
  - `cshhbjmp`: 车身划痕险不计免赔
  - `blddbs`: 玻璃单独破粹险
  - `babxcd`: 产地
  - `bazdzx`: 指定专修险
  - `bayyfs`: 营运方式
  - `bacldw`: 车辆吨位
  - `bacxlx`: 车型类型
  - `sfraxb`: 是否融安信宝(传false)
  - `baclpl`: 排量
- 入参示例:
```javascript
{

	"baclpl": "1.6",
	"badycs":"合肥市",
	"bazwsl":"3",
	"baszzr":"20",
	"baclzd":"20000",
	"ryzrxsj":"false",
	"ryzrxsjxe":"1",
	"ryzrxck":"true",
	"ryzrxckxe":"1",
	"babjmp":"true",
	"bacshh":"true",
	"badqxe":"true",
	"cshhxe":"2000",
	"cshhbjmp":"false",
	"blddbs":"true",
	"babxcd":"进口",
	"bazdzx":"true",
	"bayyfs":"非运营",
	"bacldw":"",
	"bacxlx":"新车",
	"sfraxb":"false"
}
```
- 出参参数:
  - `baccse`: 车船税
  - `basxye`: 商业险
  - `bajqxe`: 交强险
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "baccse": "180",
    "basxye": "4319.0",
    "bajqxe": "950"
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
	- `date`: 日期
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
	http://domain/overdueRate/getOverDueRateByArea?date=20161109&levelId=2791
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

## 43. 查询延保类别
- url: `http://domain/sales/warranty`
- 请求方式： `GET`
- 入参位置: 无
- 入参参数: 无
- 入参示例：
	http://domain/sales/warranty
- 出参参数：
	- `YBCP`: 类别名称
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "YBCP": "A款"
    },
    {
      "YBCP": "B款"
    },
    {
      "YBCP": "C款"
    },
    {
      "YBCP": "D款"
    }
  ]
}
```

## 44. 查询延保价格
- url: `http://domain/sales/warranty/{id}`
- 请求方式： `GET`
- 入参位置: PathVariable
- 入参参数:
  - `id`: 延保类别
- 入参示例：
	http://domain/sales/warranty/A款
- 出参参数：
	- `BASZCS`: 延保金额
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "BASZCS": "600"
    },
    {
      "BASZCS": "900"
    },
    {
      "BASZCS": "1200"
    }
  ]
}
```

## 45. 获取融资产品方案
- url: `http://domain/financeProducts/productCase`
- 请求方式： `GET`
- 入参位置: `Request Parameter`
- 入参参数:
  - `carType`: 车型
  - `typeId`: 产品大类ID
- 入参示例：
	http://domain/financeProducts/productCase?carType=乘用车&typeId=721
- 出参参数：
  - `BACPMC`: 产品方案名称
  - `BADKQX`: 产品融资期限
  - `ID`: 融资产品方案ID
- 出参示例：
```javascript
{
      "status": "SUCCESS",
      "error": "",
      "data": [
        {
          "BARZLJ": "",
          "BAYCFJ": "",
            ...
          "BADKQX": "12,24,36",
          "XTCZSJ": 0,
          "BACPMC": "轻松融A套餐",
          "ID": 1410000,
          "MC": "",
          "flowZALCID": "",
          "flowZAJDID": "",
          "HZRZXM": "",
            ...
          "BADQCZ": "",
          "ID": 1005459,
            ...
          "BAKHSX": "",
          "BACPBZ": "产品结构：等额本息还款,首付30%起(优质客户20%起)，融资额不限。适用所有车型，租期12-60个月(车价+GPS)融资，不限户籍\r\n风控政策：身份证+驾照+正规房产+6个月银行流水+结婚证或户口本（二选一）\r\n备注：正规房产资料包括商品房、土地证、购房合同、宅基地证，草原证。不包含村委会/居委会证明",
          "SXFYLJ": "",
            ...
        }
      ]
}
```

## 46. 获取产品车型
- url: `http://domain/cars/finance/carTypes`
- 请求方式： `GET`
- 入参位置: 无
- 入参参数: 无
- 入参示例：
	http://domain/cars/finance/carTypes
- 出参参数：
	- `cpcx`: 产品车型
	- `cpcxId`: 产品车型ID
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
   {
         "cpcx": "乘用车",
         "cpcxId": "101"
       },
       {
         "cpcx": "轻客",
         "cpcxId": "102"
       },
       {
         "cpcx": "微面",
         "cpcxId": "103"
       },
       {
         "cpcx": "皮卡",
         "cpcxId": "104"
       },
       {
         "cpcx": "轻卡",
         "cpcxId": "105"
       },
       {
         "cpcx": "平行进口车",
         "cpcxId": "106"
       },
       {
         "cpcx": "新能源车",
         "cpcxId": "107"
       }
  ]
}
```

## 47. 获取客户端最新版本描述
- url: `http://222.73.56.22:8089/appVersions/latest/description`
- 请求方式： `GET`
- 入参位置: Request Parameter
- 入参参数:
  - `type`: 客户端类型,0为安卓
- 入参示例：
	http://222.73.56.22:8089/appVersions/latest/description?type=0

## 48. 获取人身意外保障服务金额
- url: `http://domain/sales/accidentProtection`
- 请求方式： `GET`
- 入参位置: `Request Parameter`
- 入参参数:
  - `grade`: 融资档位（'2.2':'2.2‰','4':'4‰','6':'6‰','8':'8‰','10':'1%'）
  - `period`: 融资期限
  - `deposit`: 保证金(没有则传0)
  - `downPay`: 首付金额(没有则传0)
  - `sellingPrice`: 车辆价格
  - `isFinance`: 是否融资(为是则调用接口)
- 入参示例：
	http://domain/sales/accidentProtection?grade=10&period=36&deposit=0&downPay=2400&sellingPrice=240000&isFinance=是
- 出参参数：
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data":  "7128"
}
```

## 49. 获取先锋卫士金额
- url: `http://domain/sales/xfws`
- 请求方式： `GET`
- 入参位置: `Request Parameter`
- 入参参数:
  - `period`: 融资期限
  - `sellingPrice`: 车辆价格
  - `isFinance`: 是否融资(为是则调用接口)
- 入参示例：
	http://domain/sales/xfws?period=12&sellingPrice=70000&isFinance=是
- 出参参数：
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data":  "1503"
}
```

## 50. 获取购置税金额
- url: `http://domain/sales/purchaseTax`
- 请求方式： `GET`
- 入参位置: `Request Parameter`
- 入参参数:
  - `productCaseId`: 产品发ID
  - `productCaseName`: 产品方案名称
  - `sellingPrice`: 车辆价格
  - `isFinance`: 是否融资(为是则调用接口)
- 入参示例：
	http://domain/sales/purchaseTax?productCaseId=1006230&productCaseName=乘用车新车包牌融&isFinance=是&sellingPrice=420800
- 出参参数：
- 出参示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data":  "17983"
}
```

## 51. 销售申请提交
- url: `http://domain/sales/apply`
- 请求方式： `POST`
- 入参位置: Request Body
- 入参参数:
  - `jbxx_xm`：基本信息_姓名
  - `jbxx_xb`：基本信息_性别
  - `jbxx_csrq`: 基本信息_出生日期
  - `jbxx_zjlx`: 基本信息_证件类型
  - `jbxx_zjhm`: 基本信息_证件号码
  - `jbxx_sjhm`: 基本信息_手机号码
  - `jbxx_zzdh`: 基本信息_住宅电话
  - `jbxx_hyzk`: 基本信息_婚姻状况
  - `jbxx_hklb`: 基本信息_户口类别
  - `jbxx_xl`: 基本信息_学历
  - `zyxx_gzdw`: 职业信息_工作单位
  - `zyxx_qyxz`: 职业信息_企业性质
  - `zyxx_sshy`: 职业信息_所属行业
  - `zyxx_zw`: 职业信息_职务
  - `zyxx_zc`: 职业信息_职称
  - `zyxx_zznx`: 职业信息_在职年限
  - `zyxx_dwszcs`: 职业信息_单位所在城市
  - `zyxx_dwdh`: 职业信息_单位电话
  - `zyxx_dwdz`: 职业信息_单位地址
  - `zyxx_shnx`: 职业信息_税后年薪(万元)
  - `dzxx_xjzdrznx`: 地址信息_现居住地入住年限
  - `dzxx_xjzzk`: 地址信息_现居住状况
  - `dzxx_xjzcs`: 地址信息_现居住城市
  - `dzxx_xjzdz`: 地址信息_现居住地址
  - `dzxx_fclx`: 地址信息_房产类型
  - `dzxx_fcszcs`: 地址信息_房产所在城市
  - `dzxx_fcqy`: 地址信息_房产区域
  - `dzxx_fcdyqk`: 地址信息_房产抵押情况
  - `dzxx_hjszcs`: 地址信息_户籍所在城市
  - `dzxx_hjszdz`: 地址信息_户籍所在地址
  - `jjlxr_lxr1xm`: 紧急联系人_联系人1姓名
  - `jjlxr_lxr1sj`: 紧急联系人_联系人1手机
  - `jjlxr_yczrgx1`: 紧急联系人_与承租人关系1
  - `jjlxr_lxr2xm`: 紧急联系人_联系人2姓名
  - `jjlxr_lxr2sj`: 紧急联系人_联系人2手机
  - `jjlxr_yczrgx2`: 紧急联系人_与承租人关系2
  - `jjlxr_lxr3xm`: 紧急联系人_联系人3姓名
  - `jjlxr_lxr3sj`: 紧急联系人_联系人3手机
  - `jjlxr_yczrgx3`: 紧急联系人_与承租人关系3
  - `jjlxr_lxrdz1`: 紧急联系人_联系人1住址
  - `jjlxr_lxrdz2`: 紧急联系人_联系人2住址
  - `po_xm`: 配偶_姓名
  - `po_sjhm`: 配偶_手机号码
  - `po_zjlx`: 配偶_证件类型
  - `po_zjhm`: 配偶_证件号码
  - `po_gzdw`: 配偶_工作单位
  - `po_zw`: 配偶_职位
  - `po_dwdh`: 配偶_单位电话
  - `po_dwdz`: 配偶_单位地址
  - `po_shnx`: 配偶_税后年薪
  - `dbr_yczrgx`: 担保人_与承租人关系
  - `dbr_xm`: 担保人_姓名
  - `dbr_sjhm`: 担保人_手机号码
  - `dbr_zjlx`: 担保人_证件类型
  - `dbr_zjhm`: 担保人_证件号码
  - `dbr_hyzk`: 担保人_婚姻状况
  - `dbr_hklb`: 担保人_户口类别
  - `dbr_dwmc`: 担保人_单位名称
  - `dbr_dwdh`: 担保人_单位电话
  - `dbr_xb`: 担保人_性别
  - `dbr_zw`: 担保人_职务
  - `dbr_xjudnx`: 担保人_现居住地入住年限
  - `dbr_xjzzk`: 担保人_现居住状况
  - `bazznx`: 担保人_工作年限
  - `bazcbx`: 担保人_年收入/元
  - `dbr_xjzdz`: 担保人_现居住地址
  - `dbr_xjzcs`: 担保人_现居住城市
  - `gsr_yczrgx`: 共申人_与承租人关系
  - `gsr_xm`: 共申人_姓名
  - `gsr_sjhm`: 共申人_手机号码
  - `gsr_zjlx`: 共申人_证件类型
  - `gsr_zjhm`: 共申人_证件号码
  - `gsr_hyzk`: 共申人_婚姻状况
  - `gsr_hklb`: 共申人_户口类别
  - `gsr_dwmc`: 共申人_单位名称
  - `gsr_dwdh`: 共申人_单位电话
  - `gsr_xb`: 共申人_性别
  - `gsr_zw`: 共申人_职务
  - `gsr_xjudnx`: 共申人_现居住地入住年限
  - `gsr_xjzzk`: 共申人_现居住状况
  - `gsr_xjzdz`: 共申人_现居住地址
  - `gsr_xjzcs`: 共申人_现居住城市
  - `rzxx_cpdlid`: 融资信息_产品大类ID
  - `rzxx_cpxlid`: 融资信息_产品小类ID
  - `rzxx_cpfaid`: 融资信息_产品方案ID
  - `rzxx_zzsid`: 融资信息_制造商ID
  - `rzxx_ppid`: 融资信息_品牌ID
  - `rzxx_cxid`: 融资信息_车型ID
  - `rzxx_clzdj`: 融资信息_车辆指导价
  - `rzxx_clxsj`: 融资信息_车辆销售价
  - `rzxx_gpsyjfy`: 融资信息_GPS硬件费用
  - `rzxx_rzqx`: 融资信息_融资期限
  - `rzxx_sfyy`: 融资信息_是否营运
  - `rzxx_sfryb`: 融资信息_是否融延保
  - `rzxx_sfrbx`: 融资信息_是否融保险
  - `rzxx_sfraxb`: 融资信息_是否融安心宝
  - `rzxx_axbjg`: 融资信息_安心宝价格
  - `rzxx_gzs`: 融资信息_购置税
  - `rzxx_yb`: 融资信息_延保
  - `rzxx_jqx`: 融资信息_交强险(1:200;2:200;3:300...)表示第1年200,第2年200,第3年300以此类推，下同
  - `rzxx_ccs`: 融资信息_车船税
  - `rzxx_syx`: 融资信息_商业险
  - `rzxx_sfbl`: 融资信息_首付比例
  - `rzxx_sfje`: 融资信息_首付金额
  - `rzxx_wfbl`: 融资信息_尾付比例
  - `rzxx_wfje`: 融资信息_尾付金额
  - `rzxx_rzje`: 融资信息_融资金额
  - `rzxx_sxfsffq`: 融资信息_手续费是否分期
  - `rzxx_sxffl`: 融资信息_手续费费率
  - `rzxx_sxf`: 融资信息_手续费
  - `rzxx_bzjl`: 融资信息_保证金率
  - `rzxx_xsjlid`: 融资信息_销售经理ID
  - `rzxx_sczlid`: 融资信息_FP市场助理ID
  - `fp_sczlxm`: 市场助理_姓名
  - `xxjl_xm`: 销售经理_姓名
  - `rzxx_hkjjkkhh`: 融资信息_还款借记卡开户行
  - `rzxx_hkjjkzh`: 融资信息_还款借记卡帐号
  - `rzxx_hkjjkhm`: 融资信息_还款借记卡户名
  - `rzxx_zxsfhz`: 融资信息_征信是否后置
  - `rzxx_sqbz`: 融资信息_申请备注
  - `zfsqfs`: 融资信息_租赁管理费收取方式
  - `baybcp`: 融资信息_延保产品
  - `baybfy`: 融资信息_延保金额
  - `rzxx_baclpl`: 融资信息_车辆排量
  - `rzxx_bazwsl`: 融资信息_座位数量
  - `bacxlx`: 融资信息_车辆类型
  - `babzdw`: 融资信息_人身意外保障档位
  - `xfwsje`: 融资信息_先锋卫士金额
  - `babzdwje`: 融资信息_意外保障金额
  - `fj_sfzlj`: 附件_身份证路径(多个附件附件以";"分开,下同)
  - `fj_jsz`: 附件_驾驶证
  - `fj_jhz`: 附件_结婚证
  - `fj_hkb`: 附件_户口本
  - `fj_srzm`: 附件_收入证明
  - `fj_jgzkyhls`: 附件_近6个月工作卡银行流水
  - `fj_fclzl`: 附件_房产类资料
  - `fj_sqb`: 附件_申请表
  - `fj_dbrsfz`: 附件_担保人_身份证
  - `fj_dbrhkb`: 附件_担保人_户口本
  - `fj_dbrfclzl`: 附件_担保人_房产类资料
  - `fj_gsrsfz`: 附件_共申人_身份证
  - `fj_gsrjsz`: 附件_共申人_驾驶证
  - `fj_gsryhls`: 附件_共申人_近6个月银行
  - `fj_gsrfclzl`: 附件_共申人_房产类资料
  - `jbxx_gcyt`: 购车用途
  - `jbxx_ywfc`: 有无房产
  - `jbxx_jszyw`: 驾驶证有无
  - `jbxx_jszxm`: 驾驶证姓名
  - `jbxx_jszhm`: 驾驶证号码
  - `jbxx_jsdah`: 驾驶档案号
  - `clsysf`: 车辆使用省份
  - `jbxx_clsycs`: 车辆使用城市
  - `jbxx_sjycr`: 实际用车人
  - `jbxx_sjycrsj`: 实际用车人手机
  - `rzxx_hzldycs`: 回租赁抵押城市
  - `rzxx_dszzrxxe`: 第三者责任险限额
  - `rzxx_csrysj`: 车上人员责任险（司机）
  - `rzxx_csryck`: 车上人员责任险（乘客）
  - `ryzrxsjxe`: 车上人员责任险（司机）限额
  - `ryzrxckxe`: 车上人员责任险（乘客）限额
  - `rzxx_bjmp`: 车上人员责任险不计免赔
  - `rzxx_cshhx`: 车身划痕险
  - `cshhxe`: 车身划痕险限额
  - `rzxx_csbjmp`: 车身划痕险不计免赔
  - `rzxx_bldd`: 玻璃单独破碎险
  - `rzxx_zdzxx`: 指定专修险
  - `babxcd`: 融资信息_玻璃产地
  - `rzxx_bzj`: 保证金
  - `rzxx_tzze`: 投资总额
- 入参示例:
```javascript
{
 "jbxx_xm":"小与雨",
 "jbxx_xb":"女",
 "jbxx_csrq":"19930101",
 "jbxx_zjlx":"身份证",
 "jbxx_zjhm":"3424251994040852",
 "jbxx_sjhm":"180553182",
 "jbxx_zzdh":"0551-88883333",
 "jbxx_hyzk":"已婚有子女",
 "jbxx_hklb":"本地",
 "jbxx_xl":"大专",
 "zyxx_gzdw":"上海领友",
 "zyxx_qyxz":"国企",
 "zyxx_sshy":"计算机",
 "zyxx_zw":"中层管理人员",
 "zyxx_zc":"股东/法人",
 "zyxx_zznx":"5年以上",
 "zyxx_dwszcs":"合肥市",
 "zyxx_dwdh":"0551-88882222",
 "zyxx_dwdz":"合肥市蜀山区潜山路新华国际广场",
 "zyxx_shnx":"0.5",
 "dzxx_xjzdrznx":"1",
 "dzxx_xjzzk":"租房",
 "dzxx_xjzcs":"曲靖市",
 "dzxx_xjzdz":"成都金牛区花照壁上横街308号",
 "dzxx_fclx":"小产权、经适房",
 "dzxx_fcszcs":"呼伦贝尔市",
 "dzxx_fcqy":"县城/镇",
 "dzxx_fcdyqk":"未抵押",
 "dzxx_hjszcs":"文山壮族苗族自治州",
 "dzxx_hjszdz":"赣州市定南县历市镇鹅公塘路40号",
 "jjlxr_lxr1xm":"何明国",
 "jjlxr_lxr1sj":"18287938135",
 "jjlxr_yczrgx1":"朋友",
 "jjlxr_lxr2xm":"张仲国",
 "jjlxr_lxr2sj":"18287938135",
 "jjlxr_yczrgx2":"朋友",
 "po_xm":"张仲国",
 "po_sjhm":"18287938135",
 "po_zjlx":"身份证",
 "po_zjhm":"430103197402023026",
 "po_gzdw":"沧州艺泽佳塑料制造厂",
 "po_zw":"普通职员",
 "po_dwdh":"0762-3239888",
 "po_dwdz":"赣州市定南县历市镇鹅公塘路40号",
 "po_shnx":"150000",
 "dbr_yczrgx":"家人",
 "dbr_xm":"张仲",
 "dbr_sjhm":"18287938135",
 "dbr_zjlx":"身份证 ",
 "dbr_zjhm":"430103197402023026",
 "dbr_hyzk":"已婚",
 "dbr_hklb":"非本地",
 "dbr_dwmc":"蓝田县独二处店",
 "dbr_dwdh":"0551-22223333",
 "gsr_yczrgx":"家人",
 "gsr_xm":"小雨",
 "gsr_sjhm":"18287938135",
 "gsr_zjlx":"身份证",
 "gsr_zjhm":"430103197402023026",
"gsr_hyzk":"已婚",
"gsr_hklb":"本地",
"gsr_dwmc":"蓝田县独二处手抓羊肉店",
"gsr_dwdh":"0762-323298828",
"rzxx_cpdlid":"324",
"rzxx_cpxlid":"1000463",
"rzxx_cpfaid":"1000463",
"rzxx_zzsid":"200238",
"rzxx_ppid":"301210",
"rzxx_cxid":"912",
"rzxx_clzdj":"499000",
"rzxx_baclpl":"2.5L",
"rzxx_bazwsl":"4",
"xxjl_xm":"销售理",
"fp_sczlxm":"市助理",
"rzxx_clxsj":"499000",
"rzxx_gpsyjfy":"2016",
"rzxx_rzqx":"24",
"rzxx_sfyy":"运营",
"rzxx_sfryb":"是",
"rzxx_sfrbx":"是",
"rzxx_sfraxb":"否",
"rzxx_axbjg":"2300",
"rzxx_gzs":"2580",
"rzxx_yb":"200",
"rzxx_jqx":"1:200;2:200;3:300",
"rzxx_ccs":"1:200;2:200;3:300",
"rzxx_syx":"1:200;2:200;3:300",
"rzxx_sfbl":"10",
"rzxx_sfje":"77800",
"rzxx_wfbl":"5",
"rzxx_wfje":"88800",
"rzxx_rzje":"199999",
"rzxx_sxfsffq":"分期融资",
"rzxx_sxffl":"18.3600",
"rzxx_sxf":"77800.00",
"rzxx_bzjl":"15",
"rzxx_xsjlid":"2574",
"rzxx_sczlid":"2574",
"rzxx_hkjjkkhh":"中国建设银行",
"rzxx_hkjjkzh":"6217002750004178200",
"rzxx_hkjjkhm":"李晓伟",
"rzxx_zxsfhz":"是",
"rzxx_sqbz":"无",
"fj_sfzlj":"http://localhost/information/news/TaiMengBao.jpg",
"fj_jsz":"http://localhost/information/news/TaiMengBao.jpg;http://localhost/information/news/TaiMengBao.jpg",
"fj_jhz":"http://localhost/information/news/TaiMengBao.jpg",
"fj_hkb":"http://localhost/information/news/TaiMengBao.jpg",
"fj_srzm":"http://localhost/information/news/TaiMengBao.jpg",
"fj_jgzkyhls":"http://localhost/information/news/TaiMengBao.jpg",
"fj_fclzl":"http://localhost/information/news/TaiMengBao.jpg",
"fj_sqb":"http://localhost/information/news/TaiMengBao.jpg",
"fj_dbrsfz":"http://localhost/information/news/TaiMengBao.jpg",
"jbxx_gcyt":"本人自用",
"jbxx_jszyw":"有",
"jbxx_jszxm":"张妮娜",
"jbxx_jszhm":"35222519700102551X",
"jbxx_jsdah":"340503195708300218",
"clsysf":"湖南省",
"jbxx_ywfc":"有",
 "jbxx_clsycs":"长沙市",
 "jbxx_sjycr":"仲",
 "jbxx_sjycrsj":"18287938135",
 "jjlxr_lxr3xm":"国",
 "jjlxr_lxr3sj":"18287938135",
 "jjlxr_yczrgx3":"朋友",
 "jjlxr_lxrdz1":"沧州艺泽佳塑料制造厂",
 "jjlxr_lxrdz2":"沧州艺泽佳塑料制造厂",
 "dbr_xb":"男",
 "dbr_zw":"普通职员",
 "dbr_xjudnx":"1",
 "dbr_xjzzk":"自有无贷",
 "bazznx":"1年以下",
 "bazcbx":"500000",
 "dbr_xjzdz":"沧州艺泽佳塑料制造",
 "gsr_xb":"男",
 "gsr_zw":"普通职员",
 "gsr_xjudnx":"1",
 "gsr_xjzzk":"自有无贷",
 "gsr_xjzdz":"沧州艺泽佳塑料制造",
 "rzxx_hzldycs":"邯郸市",
 "zfsqfs":"分期融资",
 "baybcp":"B款",
 "baybfy":"1020",
 "rzxx_dszzrxxe":"30",
 "rzxx_csrysj":"否",
 "rzxx_csryck":"是",
 "ryzrxckxe":"2",
 "rzxx_bjmp":"是",
 "rzxx_cshhx":"是",
 "cshhxe":"5000",
 "rzxx_csbjmp":"是",
 "rzxx_bldd":"是",
 "rzxx_zdzxx":"1",
 "babxcd":"进口",
 "rzxx_bzj":"20",
 "rzxx_tzze":"234567",
 "fj_dbrhkb":"",
 "fj_dbrfclzl":"",
 "fj_gsrsfz":"",
 "fj_gsrjsz":"",
 "fj_gsryhls":"",
 "fj_gsrfclzl":"",
 "dbr_xjzcs":"长沙市",
 "gsr_xjzcs":"长沙市",
 "bacxlx":"乘用车",
 "ryzrxsjxe":"3",
 "babzdw":"1",
 "xfwsje":"3000",
 "babzdwje":"2345"
}
```
## 52. 退出登录(仅太盟使用：单点登陆)
- url: `http://domain/tmLogout`
- 请求方式: `GET`
- 入参参数: 无
- 出参参数: 无


## 53. 创建、修改身份证信息
- url: `http://domain/approval/addIdentityInfo?uniqueMark=`
- 请求方式: `POST`
- 入参位置: Request Body
- 入参参数:
  - `name`: 姓名(必传)
  - `sex`: 性别
  - `dateOfBirth`: 出生日期
  - `nation`: 民族
  - `address`: 地址
  - `idCardNum`: 身份证号码
  - `effectiveTerm`: 有效期限(YYYYMMDD-YYYYMMDD)
  - `issuingAuthority`: 签发机关
  - `frontImg`: 身份证正面照片
  - `behindImg`: 身份证背面照片
  - `productSource`: 产品来源(HPL,在线助力融)
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识(必传)

- 入参示例:
    http://domain/approval/addIdentityInfo?uniqueMark=
```javascript
{
	"name": "杜雨生",
	"sex":"男",
	"dateOfBirth":"19940202",
	"nation":"汉",
	"address":"安徽省舒城县",
	"idCardNum":"342425199404085555",
	"effectiveTerm":"20050513-20250513",
	"frontImg":"http://222.73.56.22:89/2123.png",
	"behindImg":"http://222.73.56.22:89/123.png",
	"productSource": "在线助力融",
	"issuingAuthority":"舒城县公安局"
}
```
- 出参参数:
  - `data`: 申请单唯一标识(uuid)
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": null,
  "data": "f39ae5f082824df5846b89af36046098"
}
```

## 54. 查询预审批身份证信息
- url: `http://domain/approval/getIdCardInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识uuid

- 入参示例:
    http://domain/approval/getIdCardInfo?uniqueMark=4de0351b53384a81b8153979142a1bb7

- 出参参数:
    - `name`: 姓名(必传)
    - `sex`: 性别
    - `dateOfBirth`: 出生日期
    - `nation`: 民族
    - `address`: 地址
    - `idCardNum`: 身份证号码
    - `effectiveTerm`: 有效期限
    - `frontImg`: 身份证正面照片
    - `behindImg`: 身份证背面照片
    - `productSource`: 产品来源(HPL,在线助力融)
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "name": "杜雨生",
    "sex": "男",
    "dateOfBirth": "19940202",
    "nation": "汉",
    "address": "安徽省舒城县",
    "idCardNum": "342425199404085555",
    "effectiveTerm": "20050513-20250513",
    "frontImg": "http://222.73.56.22:89/2123.png",
	"behindImg":"http://222.73.56.22:89/123.png",
	"productSource": "在线助力融"
  }
}
```


## 55.添加和修改驾驶证信息：
- url: `http://domain/approval/addDriveInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识(必传)
- 入参位置: Request Body
- 入参参数:
  - `name`: 驾驶证姓名
  - `sex`: 性别
  - `dateOfBirth`: 出生日期
  - `nationality`: 国籍
  - `firstIssueDate`: 初次领证日期
  - `address`: 地址
  - `quasiDriveType`: 准驾车型
  - `licenceNum`: 证件号码
  - `effectiveTerm`: 有效日期
  - `licenceImg`: 证件图片

- 入参示例:
    http://domain/approval/addDriveInfo?uniqueMark=4de0351b53384a81b8153979142fb1b7
```javascript
{
	"name": "杜雨",
	"sex":"女",
	"dateOfBirth":"19930102",
	"nationality":"China",
	"address":"AnHui",
	"firstIssueDate":"19990909",
	"effectiveTerm":"20160201",
	"quasiDriveType":"A1",
	"licenceNum":"88886688",
	"licenceImg":"http://222.73.56.2429/123.png"
}
```
- 出参参数:
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

# 56. 查询预审批驾驶证信息
- url: `http://domain/approval/getDriveInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识uuid

- 入参示例:
    http://domain/approval/getDriveInfo?uniqueMark=4de0351b53384a81b8153979142abb7

- 出参参数:
     - `name`: 驾驶证姓名
     - `sex`: 性别
     - `dateOfBirth`: 出生日期
     - `nationality`: 国籍
     - `firstIssueDate`: 初次领证日期
     - `address`: 地址
     - `quasiDriveType`: 准驾车型
     - `licenceNum`: 证件号码
     - `effectiveTerm`: 有效日期
     - `licenceImg`: 证件图片
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "name": "杜雨",
    "sex": "女",
    "nationality": "China",
    "dateOfBirth": "19930102",
    "firstIssueDate": "19990909",
    "address": "AnHui",
    "quasiDriveType": "A1",
    "licenceNum": "88886688",
    "effectiveTerm": "20160201",
    "licenceImg": "http://222.73.56.2429/123.png"
  }
}
```

## 57.添加和修改银行卡信息
- url: `http://domain/approval/addBankInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识(必传)
- 入参位置: Request Body
- 入参参数:
  - `name`: 驾驶证姓名
  - `bank`: 开户行名称
  - `accountNum`: 借记卡账号
  - `bankImg`: 借记卡照片
  - `bankPhoneNum`: 借记卡预留手机号
  
- 入参示例:
    http://domain/approval/addBankInfo?uniqueMark=a54d344fb3b24305879843cc2667872d&msgCode=123456
```javascript
{
	"name": "杜雨生",
	"bank":"光大银行",
	"accountNum":"62282723897129837182273",
	"bankImg":"http://222.73.56.22:89/123.png",
	"bankPhoneNum":"18055313782"
}
```
- 出参参数:
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

# 58. 查询预审批银行卡信息
- url: `http://domain/approval/getBankInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识uuid

- 入参示例:
    http://domain/approval/getBankInfo?uniqueMark=4de0351b53384a81b8153979142abb7

- 出参参数:
      - `name`: 驾驶证姓名
      - `bank`: 开户行名称
      - `accountNum`: 借记卡账号
      - `bankImg`: 借记卡照片
      - `bankPhoneNum`: 借记卡预留手机号
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "name": "杜雨生",
    "bank": "光大银行",
    "accountNum": "62282723897129837182273",
    "bankImg": "http://222.73.56.22:89/123.png",
    "bankPhoneNum":"18055313782"
  }
}
```

## 59.添加和修改其他信息
- url: `http://domain/approval/addOtherInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识(必传)
- 入参位置: Request Body
- 入参参数:
  - `homeNumber`: 住宅电话
  - `phoneNumber`: 手机号
  - `vicePhoneNumber`: 副手机号
- 入参示例:
    http://domain/approval/addOtherInfo?uniqueMark=4de0351b53384a81b8153979142afbb7
```javascript
{
	"homeNumber": "18055313782",
	"phoneNumber":"17730209526",
	"vicePhoneNumber":"111222333"
}
```
- 出参参数:
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

# 60. 查询预审批其他信息
- url: `http://domain/approval/getOtherInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识uuid

- 入参示例:
    http://domain/approval/getOtherInfo?uniqueMark=4de0351b53384a81b8153979142abb7

- 出参参数:
     - `homeNumber`: 住宅电话
     - `phoneNumber`: 手机号
     - `vicePhoneNumber`: 副手机号
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "homeNumber": "18055313782",
    "phoneNumber": "17730209526",
    "vicePhoneNumber": "111222333"
  }
}
```

# 61. 查询创建未提交列表
- url: `http://domain/approval/getLocalInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `condition`: 检索条件(此参数传值即根据查询条件模糊匹配，不传值则查询所有数据)
    - `originType`: 来源下拉框的筛选条件("1"：app，"2"：微信，"0"：全部)
- 入参示例:
    http://domain/approval/getLocalInfo
- 出参参数:
     - `name`: 姓名
     - `createTime`: 创建时间
     - `uniqueMark`: 申请单唯一标识uuid
     - `applyNum`: 申请编号
     - `createUser`: 创建人
     - `origin`: 来源（2:在线助力融（微盟贷） 1:微信，0，"",null :默认app）
     - `wxState`: 微信报单的预审批信息完善情况（0:未完善、1:已完善）
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "name": "杜雨生",
      "createUser": "SH000",
      "createTime": "2017-05-12",
      "uniqueMark": "f39ae5f082824df5846b89af36046098"，
      "applyNum": null,
      "origin": "1",
      "wxState": "0"
    },
    {
      "name": "杜雨生",
      "createUser": "SH000",
      "createTime": "2017-05-12",
      "uniqueMark": "4de0351b53384a81b8153979142afbb7"，
      "applyNum": null,
      "origin": null,
      "wxState": null
    }
  ]
}
```


# 62. 查询退回待修改列表
- url: `http://domain/approval/getBackInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参示例:
    http://domain/approval/getBackInfo
- 出参参数:
     - `name`: 姓名
     - `applyNum`: 申请编号
     - `reason`: 原因
     - `status`: 申请状态
     - `uniqueMark`: 申请单唯一标识uuid
     - `createUser`: 创建人
     - `updateTime`: 更新时间
     
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "name": "杜雨生",
      "applyNum": null,
      "createUser": "SH000",
      "reason": "法院非经济",
      "status": "300",
      "updateTime": "2018-06-14",
      "uniqueMark": "f39ae5f082824df5846b89af36046098"
    },
    {
      "name": "杜雨生",
      "applyNum": null,
      "createUser": "SH000",
      "reason": "手机号联系不上",
      "status": "300",
      "updateTime": "2018-06-14",
      "uniqueMark": "4de0351b53384a81b8153979142afbb7"
    }
  ]
}
```


# 63. 查询申请审批中列表
- url: `http://domain/approval/getSubmitInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参示例:
    http://domain/approval/getSubmitInfo
- 出参参数:
     - `name`: 姓名
     - `applyNum`: 申请编号
     - `status`: 状态
     - `uniqueMark`: 申请单唯一标识uuid
     - `createUser`: 创建人
     - `approvalSubmitTime`: 预审批提交时间
     
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "name": "杜雨生",
      "createUser": "SH000",
      "applyNum": null,
      "status": "申请中",
      "uniqueMark": "f39ae5f082824df5846b89af36046098",
      "approvalSubmitTime": "2018-07-17 11:22:54"
    },
    {
      "name": "杜雨生",
      "createUser": "SH000",
      "applyNum": null,
      "status": "申请中",
      "uniqueMark": "4de0351b53384a81b8153979142afbb7",
      "approvalSubmitTime": ""
    }
  ]
}
```

# 64. 查询审批完成列表
- url: `http://domain/approval/getAchieveInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `searchType`: 查询类型（1: 查询通过， 2: 查询拒绝，0: 查询全部）
    - `condition`: 检索条件(此参数传值即根据查询条件模糊匹配，不传值则查询所有数据)
    - `originType`: 来源下拉框的筛选条件("1"：app，"2"：微信，"0"：全部)
- 入参示例:
    http://domain/approval/getAchieveInfo?searchType=0
- 出参参数:
     - `name`: 姓名
     - `reason`: 原因
     - `status`: 状态(通过, 拒绝)
     - `uniqueMark`: 申请单唯一标识uuid
     - `isAutoApproval`: 是否为自动预审批(0: 自动预审批，1：人工预审批)
     - `idCardNum`: 省份证号
     - `phoneNumber`: 手机号
     - `appluNum`: 申请编号
     - `certificationStatus`: 认证状态
     - `time`: 时间
     - `createUser`: 创建人
     - `origin`: 来源（1:微信，为空默认app）

- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
   {
      "name": "刘云",
      "reason": "人工",
      "status": "通过",
      "uniqueMark": "99b0991ee6934f2ab854727d69ed4120",
      "isAutoApproval": "0",
      "idCardNum": "321023199202036214",
      "phoneNumber": "15821786884",
      "certificationStatus": "011",
      "time": "2017-08-04",
      "createUser": "SH000",
      "origin": "1"
   },
    ...
    {
      "name": "刘云",
      "reason": "操作失败，系统错误",
      "status": "拒绝",
      "uniqueMark": "da73ce69914b4c4d918cac73c40e41d3",
      "isAutoApproval": "0",
      "idCardNum": "321023199202036214",
      "phoneNumber": "15821786884",
      "applyNum": null
      "certificationStatus": "011",
      "time": "2017-08-04",
      "createUser": "SH000",
      "origin": null
    }
  ]
}
```


# 65.查询预审批首页不同状态审批数量
- url: `http://domain/approval/getApprovalCount`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参示例:
    http://domain/approval/getApprovalCount
- 出参参数:
     - `toSubmitListCount`: 创建未提交数量
     - `backListCount`: 退回待修改数量
     - `approvalListCount`: 申请审批中数量
     - `passListCount`: 审批已完成数量
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "toSubmitListCount": 1,
    "backListCount": 1,
    "approvalListCount": 0,
    "passListCount": 3
  }
}
```

# 66.人工预审批提交(返回退回待修改状态为300时提交法院附件调用该接口)
- url: `http://domain/approval/submit`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识(必传)
- 入参位置: Request Body
- 入参参数:
  - `caseProveUrl`: 结案证明url(数组)
  - `courtVerdictUrl`: 法院判决书url（数组）
- 入参示例
- 入参示例:
    http://domain/approval/submit?uniqueMark=4de0351b53384a81b8153979142afbb7
```javascript
{
	"caseProveUrl": ["http://www.xftm.com/images/1323123.jpg", "http://www.xftm.com/images/1323123.jpg"],
	"courtVerdictUrl": ["http://www.xftm.com/images/1weq1asd23.jpg","http://www.xftm.com/images/1weq1asd23.jpg"]
}
```
- 出参参数:
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

# 67.生成申请唯一标识（UUID）
- url: `http://domain/approval/getUniqueMark`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参示例:
    http://domain/approval/getUniqueMark
- 出参参数:
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": null,
  "data": "ca922b42f71f46d18b29792267c8c142"
}
```

# 68.自动预审批提交(首次提交时调用该接口)
- url: `http://domain/approval/autoFinancingPreApplySubmit`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
    - `uniqueMark`: 唯一标识
    - `longitude`: 经度 （非必传） Double
    - `latitude`: 纬度（非必传） Double
- 入参示例:
    http://domain/approval/autoFinancingPreApplySubmit?uniqueMark=4de0351b53384a81b8153979142afbb7&longitude=117.3653&latitude=23.2427
- 出参参数:
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```


## 69.添加和修改配偶信息
- url: `http://domain/approval/addMateInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识(必传)
- 入参位置: Request Body
- 入参参数:
  - `mateName`: 配偶姓名
  - `mateMobile`: 配偶手机
  - `mateIdty`: 配偶证件号码
  - `company`: 工作单位
  - `address`: 地址
- 入参示例:
    http://domain/approval/addMateInfo?uniqueMark=4de0351b53384a81b8153979142afbb7
```javascript
{
	"mateName": "kevin",
	"mateMobile":"18055313782",
	"mateIdty":"123123123453231231",
	"company":"上海领友",
	"address":"华府骏苑"
}
```
- 出参参数:
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

## 70.添加和修改紧急联系人信息
- url: `http://domain/approval/addContactInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识(必传)
- 入参位置: Request Body
- 入参参数:
  - `contact1Name`: 联系人1姓名
  - `contact1Mobile`: 联系人1手机
  - `contact1Relationship`: 联系人1与客户关系
  - `contact2Name`: 联系人2姓名
  - `contact2Mobile`: 联系人2手机
  - `contact2Relationship`: 联系人2与客户关系
- 入参示例:
    http://domain/approval/addContactInfo?uniqueMark=4de0351b53384a81b8153979142afbb7
```javascript
{
	"contact1Name": "kobe",
	"contact1Mobile":"18055313782",
	"contact1Relationship":"34234254534534534",
	"contact2Name":"mata",
	"contact2Mobile":"17730209526",
	"contact2Relationship":"343235232342354322423"
}
```
- 出参参数:
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

# 71. 查询预审批配偶信息
- url: `http://domain/approval/getMateInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识uuid

- 入参示例:
    http://domain/approval/getMateInfo?uniqueMark=4de0351b53384a81b8153979142abb7

- 出参参数:
  - `mateName`: 配偶姓名
  - `mateMobile`: 配偶手机
  - `mateIdty`: 配偶证件号码
  - `company`: 工作单位
  - `address`: 地址
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "mateName": "kevin",
    "mateMobile": "18055313782",
    "mateIdty": "12312312342423231231",
    "company": "上海领友",
    "address": "华府骏苑"
  }
}
```


# 72. 查询预审批紧急联系人信息
- url: `http://domain/approval/getContactInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识uuid

- 入参示例:
    http://domain/approval/getContactInfo?uniqueMark=4de0351b53384a81b8153979142abb7

- 出参参数:
  - `contact1Name`: 联系人1姓名
  - `contact1Mobile`: 联系人1手机
  - `contact1Relationship`: 联系人1与客户关系
  - `contact2Name`: 联系人2姓名
  - `contact2Mobile`: 联系人2手机
  - `contact2Relationship`: 联系人2与客户关系
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "contact1Name": "kobe",
    "contact1Mobile": "18055313782",
    "contact1Relationship": "34234254534534534",
    "contact2Name": "mata",
    "contact2Mobile": "17730209526",
    "contact2Relationship": "343235232342354322423"
  }
}
```

## 73. 销售计划查询(2017)
- url: `http://domain/sales/newPlan`
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
	- `salePlans`: 子集销售计划数组
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "salePlans": [
      {
        "levelId": 8882,
        "area": "中西区",
        "planCount": 44694,
        "realCount": 12669
      },
      {
        "levelId": 2374,
        "area": "南区",
        "planCount": 29462,
        "realCount": 7459
      },
      {
        "levelId": 2375,
        "area": "北区",
        "planCount": 21903,
        "realCount": 5609
      },
      {
        "levelId": 2378,
        "area": "东区",
        "planCount": 17616,
        "realCount": 4260
      }
    ],
    "levelId": 81,
    "area": "HPL",
    "planCount": 113675,
    "realCount": 29997
  }
}
```

## 74.新建申请首页不同状态审批数量
- url: `http://domain/onLineApply/getNewApprovalCount`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参示例:
    http://domain/onLineApply/getNewApprovalCount
- 出参参数:
     - `toSubmitListCount`: 创建未提交数量
     - `backListCount`: 退回待修改数量
     - `approvalListCount`: 申请审批中数量
     - `passListCount`: 审批已完成数量
- 出参示例:
```javascript
{
   "status": "SUCCESS",
    "error": "",
    "data": {
      "toSubmitListCount": 9,
      "backListCount": 3,
      "approvalListCount": 2,
      "passListCount": 3
    }
}
```

## 75.添加、修改产品方案
- url: `http://domain/onLineApply/addProductPlan`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号(必传)
- 入参位置: Request Body
- 入参参数:
  - `mainType`: 车辆类型 ------//新车/二手车
  - `productTypeName`: 产品类型名称
  - `productTypeId`: 产品类型id
  - `root`: 产品来源
  - `type`: 车辆类型
  - `specificTypeName`: 细分产品名称
  - `specificTypeId`: 产品细分id

- 入参示例:
    http://domain/onLineApply/addProductPlan?uniqueMark=342322
```javascript
{
	"mainType": "新车",
	"productTypeId":"324",
	"productTypeName":"优费融",
	"root":"先锋太盟",
	"type":"乘用车",
	"specificTypeId":"120302",
	"specificTypeName":"明白融"
}
```
- 出参参数:
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

## 76.查询产品方案信息
- url: `http://domain/onLineApply/getProductPlan`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号(必传)
- 入参示例:
    http://domain/onLineApply/getProductPlan?uniqueMark=564522
- 出参参数:
    - `mainType`: 车辆类型 ------//新车/二手车
    - `productTypeId`: 产品类型id
    - `productTypeName`: 产品类型名称
    - `root`: 产品来源
    - `type`: 车辆类型
    - `specificTypeId`: 产品细分Id
    - `specificTypeName`: 产品细分名称

- 出参示例:
```javascript
{
   "status": "SUCCESS",
     "error": "",
     "data": {
             	"mainType": "新车",
             	"productTypeId":"324",
             	"productTypeName":"优费融",
             	"root":"先锋太盟",
             	"type":"乘用车",
             	"specificTypeId":"120302",
             	"specificTypeName":"明白融"
             }
}
```

## 77.添加车辆信息
- url: `http://domain/onLineApply/addCarInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号(必传)
- 入参位置: Request Body
- 入参参数:
  - `mfrs`: 制造商
  - `brand`: 品牌
  - `type`: 车型
  - `typeId`: 车型ID
  - `officialPrice`: 指导价
  - `salePrice`: 销售价格
  - `carPurchaseTax`: 参考购置税
  - `secondOfficialPrice`: 二手车评估价
  - `secondDate`: 二手车出场日期
  - `secondYears`: 二手车车龄
  - `secondDistance`: 二手车公里数
  - `displacement`: 排量
  - `seatNumber`: 座位数量
- 入参示例:
    http://domain/onLineApply/addCarInfo?uniqueMark=743253
```javascript
{
	"mfrs": "大众",
	"brand":"帕萨塔",
	"type":"帕萨塔豪华版",
	"typeId":"564235",
	"officialPrice":"200000",
	"salePrice":"188800",
	"carPurchaseTax":"12345",
	"secondOfficialPrice":"",
	"secondDate":"",
	"secondYears":"",
	"secondDistance":"",
	"displacement":"1.8",
    "seatNumber":"4"
}
```

- 出参参数:
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

## 78.添加车辆抵押信息
- url: `http://domain/onLineApply/addCarPledgeInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号(必传)
- 入参位置: Request Body
- 入参参数:
  - `saler`: 车辆经销商名称
  - `licenseAttribute`: 牌照属性
  - `pledgeCity`: 回租赁抵押城市
  - `pledgeCompany`: 正租赁上牌&回租赁抵押公司
- 入参示例:
    http://domain/onLineApply/addCarPledgeInfo?uniqueMark=2411211

```javascript
{
	"saler": "车辆经销商",
	"licenseAttribute":"省级牌照",
	"pledgeCity":"合肥市",
	"pledgeCompany":"上海领友"
}
```

- 出参参数:
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

## 79.添加融资信息
- url: `http://domain/onLineApply/addFinanceInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号(必传)
- 入参位置: Request Body
- 入参参数:
  - `paymentRatio`: 首付比例
  - `paymentAmount`: 首付金额
  - `serviceRatio`: 服务费率
  - `serviceAmount`: 服务金额
  - `poundageRatio`: 手续费率
  - `poundageAmount`: 手续费金额
  - `bondRatio`: 保证金费率
  - `bondAmount`: 保证金金额
  - `rentRatio`: 租赁管理费率
  - `rentAmount`: 租赁管理费用
  - `financeAmount`: 融资金额
  - `financeTerm`: 融资期限
  - `totalInvestment`: 投资总额
  - `purchaseTaxFlag`: 是否融购置税（0/1）
  - `purchaseTax`: 购置税
  - `insuranceFlag`: 是否融保险（0/1）
  - `gpsFlag`: 是否融GPS（0/1）
  - `gps`: gps价格
  - `compulsoryInsurance`: 交强险
  - `commercialInsurance`: 商业险
  - `vehicleTax`: 车船税
  - `xfwsFlag`: 是否融先锋卫士（0/1）
  - `xfws`: 先锋卫士金额
  - `xfwsTerm`: 先锋卫士期限
  - `unexpectedFlag`: 是否融人身意外（0/1）
  - `unexpectedGear`: 人身意外档位
  - `unexpected`: 人身意外金额
  - `poundageRatioMonthlyFlag`: 手续费率是否月结（0/1）
  - `gpsMonthlyFlag`: gps是否月结（0/1）
  - `limitOfLiabilityInsurance`: 第三方责任险限额(万元)

- 入参示例:
    http://domain/onLineApply/addFinanceInfo?uniqueMark=221333

```javascript
{
    "paymentRatio": "20%",
	"paymentAmount":"23456",
	"serviceRatio":"",
	"serviceAmount":"222",
	"poundageRatio": "7%",
	"poundageAmount":"7098",
	"bondRatio":"25",
	"bondAmount":"27689",
	"rentRatio": "6%",
	"rentAmount":"6409",
	"financeAmount":"92312",
	"financeTerm":"36",
	"totalInvestment": "11985",
	"purchaseTaxFlag":"0",
	"purchaseTax":"0.0",
	"insuranceFlag":"1",
	"gpsFlag": "1",
	"gps":"1000",
	"compulsoryInsurance":"234,345,567",
	"commercialInsurance":"123,456,468",
	"vehicleTax":"234,345,342",
	"xfwsFlag":"1",
	"xfws": "124",
	"xfwsTerm":"36",
	"unexpectedFlag":"1",
	"unexpectedGear":"2.2‰",
	"unexpected":"2312",
	"poundageRatioMonthlyFlag":"1",
    "gpsMonthlyFlag":"0",
    "limitOfLiabilityInsurance":"20"
}
```

- 出参参数:
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

## 80.添加客户详细信息
- url: `http://domain/onLineApply/addDetailedInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号(必传)
- 入参位置: Request Body
- 入参参数:
  - `diploma`: 学历
  - `marriage`: 婚姻状况
  - `houseOwnership`: 有无房产
  - `realDriverName`: 实际用车人
  - `realDriverMobile`: 实际用车人手机
  - `realDrivingProv`: 车辆使用省份
  - `realDrivingCity`: 车辆使用城市
  - `accountType`: 户籍类型
  - `letterOfCredit`: 信用授权书
  - `handHoldLetterOfCredit`: 手持征信授权书
- 入参示例:
    http://domain/onLineApply/addDetailedInfo?uniqueMark=123155

```javascript
{
	"diploma": "博士",
	"marriage":"未婚",
	"houseOwnership":"无",
	"realDriverName":"杜雨生",
	"realDriverMobile": "18055313782",
	"realDrivingProv":"安徽",
	"realDrivingCity":"合肥",
	"accountType":"本地",
    "letterOfCredit":"http://222.73.56.22:89/letterOfCredit/9110b307-5e73-425b-8cb8-b451427e2184.jpg",
    "handHoldLetterOfCredit":"http://222.73.56.22:89/handHoldLetterOfCredit/9110b307-5e73-425b-8cb8-b451427e2184.jpg"
}
```

- 出参参数:
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

## 81.添加客户职业信息
- url: `http://domain/onLineApply/addWorkInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号(必传)
- 入参位置: Request Body
- 入参参数:
  - `workUnit`: 工作单位名称
  - `workUnitPost`: 职务
  - `workUnitNature`: 企业性质
  - `workingLife`: 在职年限
  - `workUnitTitle`: 职称
  - `workUnitIndustry`: 所属行业
  - `workUnitProv`: 单位所在省份
  - `workUnitCity`: 单位所在城市
  - `workUnitPhone`: 单位电话
  - `annualPayAfterTax`: 税后年薪
  - `workUnitAddressProv`: 工作单位所在省份
  - `workUnitAddressProvCity`: 工作单位所在城市
  - `workUnitAddress`: 工作单位详细地址
- 入参示例:
    http://domain/onLineApply/addWorkInfo?uniqueMark=123124

```javascript
{
    "workUnit": "上海领友",
	"workUnitPost":"普通职员",
	"workUnitNature":"个体工商户",
	"workingLife":"1",
	"workUnitTitle": "初",
	"workUnitIndustry":"IT",
	"workUnitProv":"上海",
	"workUnitCity":"上海",
	"workUnitPhone":"02189992266",
	"annualPayAfterTax": "1.0",
	"workUnitAddressProv":"安徽",
	"workUnitAddressProvCity":"合肥",
	"workUnitAddress":"新华国际广场"
}
```

- 出参参数:
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

## 82.添加共申人信息
- url: `http://domain/onLineApply/addJointInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号(必传)
- 入参位置: Request Body
- 入参参数:
  - `jointName`: 共申人姓名
  - `jointMobile`: 共申人手机
  - `jointIdType`: 共申人证件类型
  - `jointIdty`: 证件号码
  - `jointRelationship`: 与承租人关系
- 入参示例:
    http://domain/onLineApply/addJointInfo?uniqueMark=234234
```javascript
{
    "jointName": "胡宗诚",
	"jointMobile":"18022221111",
	"jointIdType":"身份证",
	"jointIdty":"342426199223091431",
	"jointRelationship": "同事"
}
```

- 出参参数:
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

## 83.添加二手车评估信息
- url: `http://domain/onLineApply/addUsedCarEvaluationDto`
- 请求方式: `POST`
- 入参位置: `Request Parameter`

- 入参参数:
- `uniqueMark`: 唯一标识(必传)
- 入参位置: Request Body
- 入参参数:
  - `carBillId`: 评估单号
  - `mark`: 备注
  - `images`: 图片数组
      - `imageSeqNum`: 图片序号(0,2,0,0,5)
      - `imageUrl`: 图片url(登记证首页,中控台含挡位杆,车左前45度,行驶证-正本/副本同照,左前门)
      - `imageClass`: 图片类型(登记证,车辆内饰,车身外观,行驶证,车体骨架)

- 入参示例:
    http://domain/onLineApply/addUsedCarEvaluationDto?uniqueMark=423422

```javascript
{
    "images": [
        {
            "imageSeqNum": "0",
            "imageUrl": "http://222.73.56.22:89/information/news/9110b307-5e73-425b-8cb8-b451427e2184.jpg",
            "imageClass": "登记证"
        },
        {
            "imageSeqNum": "5",
            "imageUrl": "http://222.73.56.22:89/information/news/2f034812-51ea-43b3-975b-25111184ddef.jpg",
            "imageClass": "车体骨架"
        },
        {
            "imageSeqNum": "0",
            "imageUrl": "http://222.73.56.22:89/information/news/dad86a4b-145c-413b-ae8d-aca4c82bacda.jpg",
            "imageClass": "登记证"
        }
    ],
    "mark": "test",
    "carBillId": "79"
}
```

- 出参参数:

- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

## 84.查询车辆信息
- url: `http://domain/onLineApply/getCarInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号(必传)

- 入参示例:
    http://domain/onLineApply/getCarInfo?uniqueMark=423123

- 出参参数:
    - `mfrs`: 制造商
    - `brand`: 品牌
    - `type`: 车型
    - `typeId`: 车型ID
    - `officialPrice`: 指导价
    - `salePrice`: 销售价格
    - `carPurchaseTax`:参考购置税
    - `secondOfficialPrice`: 二手车评估价格
    - `secondDate`: 二手车出场日期
    - `secondDistance`: 二手车公里数
    - `displacement`: 排量
    - `seatNumber`: 座位数量
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "mfrs": "大众",
    "brand": "帕萨塔",
    "type": "帕萨塔豪华版",
    "typeId": "564348",
    "officialPrice": "200000",
    "salePrice": "188800",
    "carPurchaseTax": "12345",
    "secondOfficialPrice": "",
    "secondDate": "",
    "secondDistance": ""，
    "displacement": "1.6",
    "seatNumber": "5"
  }
}
```


## 85. 查询车辆抵押信息
- url: `http://domain/onLineApply/getCarPledgeInfo?uniqueMark=9527`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号

- 入参示例:
    http://domain/onLineApply/getCarPledgeInfo?uniqueMark=9527

- 出参参数:
   - `saler`: 车辆经销商名称
   - `licenseAttribute`: 牌照属性
   - `pledgeCity`: 回租赁抵押城市
   - `pledgeCompany`: 正租赁上牌&回租赁抵押公司
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "saler": "合肥皖之星",
        "licenseAttribute": "回租赁",
        "pledgeCity": "合肥",
        "pledgeCompany": "先锋太盟"
    }
}
```

## 86. 查询融资信息
- url: `http://domain/onLineApply/getFinanceInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号

- 入参示例:
    http://domain/onLineApply/getFinanceInfo?uniqueMark=9527

- 出参参数:
       - `paymentRatio`: 首付比例
       - `paymentAmount`: 首付金额
       - `serviceRatio`: 服务费率
       - `serviceAmount`: 服务金额
       - `poundageRatio`: 手续费率
       - `poundageAmount`: 手续费金额
       - `bondRatio`: 保证金费率
       - `bondAmount`: 保证金金额
       - `rentRatio`: 租赁管理费率
       - `rentAmount`: 租赁管理费用
       - `financeAmount`: 融资金额
       - `financeTerm`: 融资期限
       - `totalInvestment`: 投资总额
       - `purchaseTaxFlag`: 是否融购置税（0/1）
       - `purchaseTax`: 购置税
       - `insuranceFlag`: 是否融保险（0/1）
       - `gpsFlag`: 是否融GPS（0/1）
       - `gps`: gps价格
       - `compulsoryInsurance`: 交强险
       - `commercialInsurance`: 商业险
       - `vehicleTax`: 车船税
       - `xfwsFlag`: 是否融先锋卫士（0/1）
       - `xfws`: 先锋卫士金额
       - `xfwsTerm`: 先锋卫士期限
       - `unexpectedFlag`: 人身意外档位
       - `unexpectedGear`: 是否融人身意外（0/1）
       - `unexpected`: 人身意外金额
       - `poundageRatioMonthlyFlag`: 手续费率是否月结（0/1）
       - `gpsMonthlyFlag`: gps是否月结（0/1）
       - `limitOfLiabilityInsurance`: 第三方责任险限额(万元)

- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "paymentRatio": "20%",
        "paymentAmount": "23456",
        "serviceRatio": "",
        "serviceAmount": "222",
        "poundageRatio": "7%",
        "poundageAmount": "7098",
        "bondRatio": "6%",
        "bondAmount": "6409",
        "rentRatio": "6%",
        "rentAmount": "6409",
        "financeAmount": "92312",
        "financeTerm": "36",
        "totalInvestment": "11985",
        "purchaseTaxFlag": "0",
        "purchaseTax": "0.0",
        "insuranceFlag": "1",
        "compulsoryInsurance": "234,345,567",
        "commercialInsurance": "123,456,468",
        "vehicleTax": "234,345,342",
        "gpsFlag": "1",
        "gps": "1000",
        "xfwsFlag": "1",
        "xfws": "124",
        "xfwsTerm": "36",
        "unexpectedGear": "2.2‰",
        "unexpectedFlag": "1",
        "unexpected": "2312"，
        "poundageRatioMonthlyFlag": "0",
        "gpsMonthlyFlag": "1",
        "limitOfLiabilityInsurance": "20"
    }
}
```

## 87. 查询客户详细信息
- url: `http://domain/onLineApply/getDetailedInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号

- 入参示例:
    http://domain/onLineApply/getDetailedInfo?uniqueMark=9527

- 出参参数:
    - `diploma`: 学历
    - `marriage`: 婚姻状况
    - `houseOwnership`: 有无房产
    - `realDriverName`: 实际用车人
    - `realDriverMobile`: 实际用车人手机
    - `realDrivingProv`: 车辆使用省份
    - `realDrivingCity`: 车辆使用城市
    - `accountType`: 户籍类型
    - `letterOfCredit`: 征信授权书
    - `handHoldLetterOfCredit`: 手持征信授权书
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "diploma": "博士",
        "marriage": "未婚",
        "houseOwnership": "无",
        "realDriverName": "杜雨生",
        "realDriverMobile": "18055313782",
        "realDrivingProv": "安徽",
        "realDrivingCity": "合肥",
        "accountType": "本地",
        "letterOfCredit":"http://222.73.56.22:89/letterOfCredit/9110b307-5e73-425b-8cb8-b451427e2184.jpg",
        "handHoldLetterOfCredit":"http://222.73.56.22:89/handHoldLetterOfCredit/9110b307-5e73-425b-8cb8-b451427e2184.jpg"
    }
}
```

## 88. 查询客户职业信息
- url: `http://domain/onLineApply/getWorkInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号

- 入参示例:
    http://domain/onLineApply/getWorkInfo?uniqueMark=9527

- 出参参数:
       - `workUnit`: 工作单位名称
       - `workUnitPost`: 职务
       - `workUnitNature`: 企业性质
       - `workingLife`: 在职年限
       - `workUnitTitle`: 职称
       - `workUnitIndustry`: 所属行业
       - `workUnitProv`: 单位所在省份
       - `workUnitCity`: 单位所在城市
       - `workUnitPhone`: 单位电话
       - `annualPayAfterTax`: 税后年薪
       - `workUnitAddressProv`: 工作单位所在省份
       - `workUnitAddressProvCity`: 工作单位所在城市
       - `workUnitAddress`: 工作单位详细地址
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "workUnit": "上海领友",
        "workUnitPost": "普通职员",
        "workUnitNature": "个体工商户",
        "workingLife": "1",
        "workUnitTitle": "初",
        "workUnitIndustry": "IT",
        "workUnitProv": "上海",
        "workUnitCity": "上海",
        "workUnitPhone": "02189992266",
        "annualPayAfterTax": "1.0",
        "workUnitAddressProv": "安徽",
        "workUnitAddressProvCity": "合肥",
        "workUnitAddress": "新华国际广场"
    }
}
```

## 89. 查询共申人信息
- url: `http://domain/onLineApply/getOtherInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号

- 入参示例:
    http://domain/onLineApply/getJointInfo?uniqueMark=9527

- 出参参数:
       - `jointName`: 共申人姓名
       - `jointMobile`: 共申人手机
       - `jointIdType`: 共申人证件类型
       - `jointIdty`: 证件号码
       - `jointRelationship`: 与承租人关系
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "jointName": "胡宗诚",
        "jointMobile": "18022221111",
        "jointIdType": "身份证",
        "jointIdty": "342426199223091431",
        "jointRelationship": "同事"
    }
}
```

## 90. 查询完善申请创建未提交列表
- url: `http://domain/onLineApply/getLocalInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `condition`: 检索条件(此参数传值即根据查询条件模糊匹配，不传值则查询所有数据)
- 入参示例:
    http://domain/onLineApply/getLocalInfo
- 出参参数:
     - `name`: 姓名
     - `applyNum`: 申请编号
     - `createTime`: 创建时间
     - `uniqueMark`: 唯一标识
     - `createUser`: 创建人
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
        "name": "刘云",
        "applyNum": "37248548",
        "createTime": "2017-07-10",
        "uniqueMark": "319f30df2f10478cb78c229f53f783ed",
        "createUser": "SH000"
    },
    {
        "name": "杜雨生",
        "applyNum": "1231241",
        "uniqueMark": "889f30df2f10478cb78c229f53f783fr",
        "createTime": "2017-07-10",
        "createUser": "SH000"
    }
  ]
}
```


## 91. 查询完善申请退回待修改列表
- url: `http://domain/onLineApply/getBackInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `condition`: 检索条件(此参数传值即根据查询条件模糊匹配，不传值则查询所有数据)
- 入参示例:
    http://domain/onLineApply/getBackInfo
- 出参参数:
     - `name`: 姓名
     - `applyNum`: 申请编号
     - `reason`: 原因
     - `status`: 申请状态
     - `createUser`: 创建人
     - `uniqueMark`: 唯一标识
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "name": "杜雨生",
      "applyNum": null,
      "reason": "法院非经济",
      "status": "4000",
      "createUser": "SH000",
      "uniqueMark": "889f30df2f10478cb78c229f53f783fr"
    },
    {
      "name": "杜雨生",
      "applyNum": "78775858",
      "reason": "手机号联系不上",
      "status": "4000",
      "createUser": "SH000",
      "uniqueMark": "889f30df2f10478cb78c229f53f783fr"
    }
  ]
}
```


## 92. 查询完善申请审批中列表
- url: `http://domain/onLineApply/getSubmitInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `condition`: 检索条件(此参数传值即根据查询条件模糊匹配，不传值则查询所有数据)
- 入参示例:
    http://domain/onLineApply/getSubmitInfo
- 出参参数:
     - `name`: 姓名
     - `applyNum`: 申请编号
     - `status`: 状态
     - `createUser`: 创建人
     - `uniqueMark`: 唯一标识
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "name": "杜雨生",
      "applyNum": "776858",
      "status": "申请中",
      "createUser": "SH000",
      "uniqueMark": "889f30df2f10478cb78c229f53f783fr"
    },
    {
      "name": "杜雨生",
      "applyNum": null,
      "status": "申请中",
      "createUser": "SH000",
      "uniqueMark": "889f30df2f10478cb78c229f53f783fr"
    }
  ]
}
```

## 93. 查询完善申请审批完成列表
- url: `http://domain/onLineApply/getAchieveInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `searchType`: 查询类型（1: 查询通过， 2: 查询拒绝，0: 查询全部）
    - `condition`: 检索条件(此参数传值即根据查询条件模糊匹配，不传值则查询所有数据)
- 入参示例:
    http://domain/onLineApply/getAchieveInfo?searchType=0
- 出参参数:
     - `name`: 姓名
     - `reason`: 原因
     - `status`: 状态(通过, 拒绝)
     - `uniqueMark`: 申请单唯一标识uuid
     - `applyNum`: 申请编号
     - `createUser`: 创建人

- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
   {
      "name": "刘云",
      "reason": "人工",
      "status": "通过",
      "uniqueMark": "99b0991ee6934f2ab854727d69ed4120",
      "applyNum": "37248548",
      "createUser": "SH000"
   },
    ...
    {
      "name": "刘云",
      "reason": "操作失败，系统错误",
      "status": "拒绝",
      "uniqueMark": "da73ce69914b4c4d918cac73c40e41d3",
      "applyNum": "37245298",
      "createUser": "SH000"
    }
  ]
}
```


## 94. 查询二手车评估结果
- url: `http://domain/onLineApply/getUsedCarEvaluationResult`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号

- 入参示例:
    http://domain/onLineApply/getUsedCarEvaluationResult?uniqueMark=342425

- 出参参数:
       - `status`: 状态(1 未评估, 1100 未通过, 100  评估中, 1000 通过)
       - `carBillId`: 单号
       - `price`: 评估价格
       - `years`: 车龄
       - `dateOfProduction`: 出厂日期
       - `resultReason`: 未通过原因

- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "status": "1100",
        "usedCarEvaluationResultDto": {
            "carBillId": "12345678",
            "price": "23123",
            "years": "24",
            "dateOfProduction": "20160209",
            "resultReason": ""
        }
    }
}
```


## 95. 获取车辆类型接口
- url: `http://domain/core/getCarType`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `carType`: 车辆新旧
- `root`: 来源：先锋特惠、HPL

- 入参示例:
    http://domain/core/getCarType?carType=新车&root=XFTH

- 出参参数:
       - `hqlhyhlist`: 车型list

- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "hqlhyhlist": [
            {
                "BADLMC": "乘用车"
            },
            {
                "BADLMC": "中卡"
            },
            {
                "BADLMC": "平行进口车"
            },
            {
                "BADLMC": "微卡"
            },
            {
                "BADLMC": "微面"
            },
            {
                "BADLMC": "新能源车"
            },
            {
                "BADLMC": "轻客"
            },
            {
                "BADLMC": "皮卡"
            },
            {
                "BADLMC": "轻卡"
            }
        ]
    }
}
```


## 96. 获取产品类型
- url: `http://domain/core/getProductType`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `mainType`: 车辆新旧
- `type`: 车辆类型
- `root`: 来源：先锋特惠、HPL

- 入参示例:
    http://domain/core/getProductType?mainType=新车&type=乘用车&root=HPL

- 出参参数:
       - `cpfabycxlist`: 产品类型list
       - `BADLMC`: 产品类型名称

- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "cpfabycxlist": [
            {
                "BADLMC": "包牌融产品",
                "ID": "821"
            },
            {
                "BADLMC": "特殊",
                "ID": "100000"
            },
            {
                "BADLMC": "快易融",
                "ID": "375"
            },
            {
                "BADLMC": "明白融20%保证金",
                "ID": "324"
            },
            {
                "BADLMC": "轻松融",
                "ID": "402"
            },
            {
                "BADLMC": "长安商用专属产品",
                "ID": "841"
            },
            {
                "BADLMC": "优费融",
                "ID": "1001"
            },
            {
                "BADLMC": "精品融",
                "ID": "1161"
            }
        ],
        "cpxlBycx": "success"
    }
}
```


## 97. 获取细分产品
- url: `http://domain/core/getSpecificType`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `mainType`: 车辆新旧
- `type`: 车辆类型
- `productTypeId`: 产品类型id
- `root`: 来源：先锋特惠、HPL

- 入参示例:
    http://domain/core/getSpecificType?mainType=新车&type=乘用车&productTypeId=402&root=XFTH

- 出参参数:
       - `specificProductDtos`: 细分产品list
       - `depict`: 产品描述
       - `specificProductName`: 产品名称
       - `specificProductId`:产品id

- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "specificProductDtos": [
            {
                "depict": [
                    "车辆类型：新车（乘用车）",
                    "申请类型：个人、企业",
                    "融资范围：车价、GPS、保险、人身意外险",
                    "首付：30%起",
                    "融资期限：12、24、36、48、60",
                    "材料：身份证、驾驶证"
                ],
                "specificProductName": "轻松融高息",
                "specificProductId": "1007551"
            },
            {
                "depict": [
                    "车辆类型：新车（乘用车）",
                    "申请类型：个人、企业",
                    "融资范围：车价、GPS、保险、人身意外险",
                    "首付：30%起",
                    "融资期限：12、24、36、48、60",
                    "材料：身份证、驾驶证"
                ],
                "specificProductName": "轻松融低息",
                "specificProductId": "1007550"
            }
        ]
    }
}
```

## 98. 获取品牌列表接口
- url: `http://domain/core/getBrand`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `productor`: 制造商
- `carType`: 车辆类型
- `productId`: 产品类型id
- `version`: 版本，新版本传"2"

- 入参示例:
    http://domain/core/getBrand?carType=乘用车&productId=1007517&productor=水星&version=2

- 出参参数:
        - `dataRows`: 数据组
        - `group`: 组
        - `dataRows`: 品牌list
        - `BACLMC`: 品牌名称
        - `ID`: 品牌ID

- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": [
        {
            "group": "A",
            "dataRows": [
                {
                    "ID": "200150",
                    "BACLMC": "安驰汽车"
                },
                {
                    "ID": "200152",
                    "BACLMC": "奥迪"
                },
                {
                    "ID": "200199",
                    "BACLMC": "阿尔法·罗密欧"
                },
                {
                    "ID": "200200",
                    "BACLMC": "阿斯顿·马丁"
                },
                {
                    "ID": "200297",
                    "BACLMC": "Arash"
                },
                {
                    "ID": "200303",
                    "BACLMC": "ACSchnitzer"
                },
                {
                    "ID": "200245",
                    "BACLMC": "奥兹莫比尔"
                },
                {
                    "ID": "409413",
                    "BACLMC": "奥迪(进口)"
                },
                {
                    "ID": "412390",
                    "BACLMC": "安徽江淮汽车股份有限公司"
                },
                {
                    "ID": "442322",
                    "BACLMC": "安徽江淮汽车股份有限公司"
                }
            ]
        },
        ...
        {
            "group": "Z",
            "dataRows": [
                {
                    "ID": "200134",
                    "BACLMC": "众泰汽车"
                },
                {
                    "ID": "200147",
                    "BACLMC": "中客华北汽车"
                },
                {
                    "ID": "200229",
                    "BACLMC": "宗申通宝"
                },
                {
                    "ID": "200272",
                    "BACLMC": "中欧房车"
                },
                {
                    "ID": "200255",
                    "BACLMC": "浙江中誉"
                },
                {
                    "ID": "200109",
                    "BACLMC": "郑州日产"
                },
                {
                    "ID": "407081",
                    "BACLMC": "重庆力帆"
                },
                {
                    "ID": "412606",
                    "BACLMC": "浙江豪情汽车制造有限公司"
                },
                {
                    "ID": "412776",
                    "BACLMC": "浙江豪情汽车制造有限公司"
                },
                {
                    "ID": "412528",
                    "BACLMC": "重庆长安汽车股份有限公司"
                },
                {
                    "ID": "429173",
                    "BACLMC": "郑州海马"
                },
                {
                    "ID": "427905",
                    "BACLMC": "众泰"
                },
                {
                    "ID": "428209",
                    "BACLMC": "重庆长安汽车股份有限公司"
                },
                {
                    "ID": "433619",
                    "BACLMC": "中华"
                },
                {
                    "ID": "437577",
                    "BACLMC": "重庆力帆汽车有限公司"
                },
                {
                    "ID": "438067",
                    "BACLMC": "重庆力帆乘用车有限公司"
                },
                {
                    "ID": "445931",
                    "BACLMC": "重庆长安铃木汽车有限公司"
                },
                {
                    "ID": "456967",
                    "BACLMC": "重庆比速汽车有限公司"
                },
                {
                    "ID": "463882",
                    "BACLMC": "浙江吉利汽车有限公司"
                }
            ]
        }
	]
}
```

## 99. 修改认证状态
- url: `http://domain/approval/alterCertificationStatus`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号
- `authItem`: 认证类型

- 入参示例:
    http://domain/approval/alterCertificationStatus?uniqueMark=2c1cf8a74e7a4931b7f810f4f158dca1&authItem=zhifubao

- 出参参数:

- 出参示例:
```javascript
{
    "status": "SUCCESS",
     "error": "",
     "data": null
}
```

## 100. 获取车辆经销商名称接口
- url: `http://domain/core/getSeller`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `productId`: 产品ID
    - `searchName`: 查询关键字(可不传，模糊查询时传)

- 入参示例:
    http://domain/core/getSeller?productId=1007517&searchName=先锋太盟

- 出参参数:
    - `BADMID`: 车辆经销商ID
    - `BADMMC`: 车辆经销商名称
- 出参示例:
```javascript
{
    "status": "SUCCESS",
      "error": "",
      "data": {
        "dataRows": [
          {
            "BADMID": "38653",
            "BADMMC": "先锋太盟渠道测试"
          },
          {
            "BADMID": "27763",
            "BADMMC": "先锋太盟融资租赁有限公司"
          }
        ]
      }
}
```

## 101. 获取回租抵押城市接口
- url: `http://domain/core/getLeaseBackCity`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `productId`: 产品ID

- 入参示例:
    http://domain/core/getLeaseBackCity?productId=1006750

- 出参参数:
    - `BADYID`: 抵押ID
    - `BASZCS`: 回租抵押所在城市
    - `BADYGS`: 正租赁上牌&回租赁抵押公司
- 出参示例:
```javascript
{
   "status": "SUCCESS",
   "error": "",
   "data": {
     "dataRows": [
       {
         "BADYID": "81",
         "BASZCS": "上海市",
         "BADYGS": "先锋太盟融资租赁有限公司"
       }
     ]
   }
}
```

## 102. 获取制造商接口
- url: `http://domain/core/getProductor`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `searchName`: 查询关键字(可不传，模糊查询时传)
    - `carType`: 车辆类型
    - `productId`: 产品类型id
    - `version`: 版本，新版本传"2"

- 入参示例:
    http://domain/core/getProductor?carType=乘用车&productId=1007517&searchName=布&version=2

- 出参参数:
    - `dataRows`: 数据组
    - `group`: 组
    - `BACLMC`: 制造商名称
    - `ID`: 制造商ID

- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": [
        {
            "group": "A",
            "dataRows": [
                {
                    "ID": "200150",
                    "BACLMC": "安驰汽车"
                },
                {
                    "ID": "200152",
                    "BACLMC": "奥迪"
                },
                {
                    "ID": "200199",
                    "BACLMC": "阿尔法·罗密欧"
                },
                {
                    "ID": "200200",
                    "BACLMC": "阿斯顿·马丁"
                },
                {
                    "ID": "200297",
                    "BACLMC": "Arash"
                },
                {
                    "ID": "200303",
                    "BACLMC": "ACSchnitzer"
                },
                {
                    "ID": "200245",
                    "BACLMC": "奥兹莫比尔"
                },
                {
                    "ID": "409413",
                    "BACLMC": "奥迪(进口)"
                },
                {
                    "ID": "412390",
                    "BACLMC": "安徽江淮汽车股份有限公司"
                },
                {
                    "ID": "442322",
                    "BACLMC": "安徽江淮汽车股份有限公司"
                }
            ]
        },
     ...
        {
                "group": "Z",
                "dataRows": [
                    {
                        "ID": "200134",
                        "BACLMC": "众泰汽车"
                    },
                    {
                        "ID": "200147",
                        "BACLMC": "中客华北汽车"
                    },
                    {
                        "ID": "200229",
                        "BACLMC": "宗申通宝"
                    },
                    {
                        "ID": "200272",
                        "BACLMC": "中欧房车"
                    },
                    {
                        "ID": "200255",
                        "BACLMC": "浙江中誉"
                    },
                    {
                        "ID": "200109",
                        "BACLMC": "郑州日产"
                    },
                    {
                        "ID": "407081",
                        "BACLMC": "重庆力帆"
                    },
                    {
                        "ID": "412606",
                        "BACLMC": "浙江豪情汽车制造有限公司"
                    },
                    {
                        "ID": "412776",
                        "BACLMC": "浙江豪情汽车制造有限公司"
                    },
                    {
                        "ID": "412528",
                        "BACLMC": "重庆长安汽车股份有限公司"
                    },
                    {
                        "ID": "429173",
                        "BACLMC": "郑州海马"
                    },
                    {
                        "ID": "427905",
                        "BACLMC": "众泰"
                    },
                    {
                        "ID": "428209",
                        "BACLMC": "重庆长安汽车股份有限公司"
                    },
                    {
                        "ID": "433619",
                        "BACLMC": "中华"
                    },
                    {
                        "ID": "437577",
                        "BACLMC": "重庆力帆汽车有限公司"
                    },
                    {
                        "ID": "438067",
                        "BACLMC": "重庆力帆乘用车有限公司"
                    },
                    {
                        "ID": "445931",
                        "BACLMC": "重庆长安铃木汽车有限公司"
                    },
                    {
                        "ID": "456967",
                        "BACLMC": "重庆比速汽车有限公司"
                    },
                    {
                        "ID": "463882",
                        "BACLMC": "浙江吉利汽车有限公司"
                    }
                ]
        }
	]
}
```


## 103. 获取款式、指导价、参考购置税接口
- url: `http://domain/core/getStyle`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `searchName`: 查询关键字(可不传，模糊查询时传)
    - `carType`: 车辆类型
    - `styleName`: 品牌名称
    - `mainType`: 车型大类：新车/二手车
    - `productId`: 产品Id
    - `version`: 版本，新版本传"2"

- 入参示例:
    http://domain/core/getStyle?carType=乘用车&styleName=米兰&searchName=水星米兰2.5-MT&mainType=二手车&productId=100061&version=2

- 出参参数:
    - `dataRows`: 数据组
    - `group`: 组
    - `baclmc`: 车辆款式名称
    - `id`: 车辆款式ID
    - `cxzdjg`: 车辆指导价
    - `backgz`: 参考购置税
    - `bapail`: 排量
    - `bazwsl`: 座位数

- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": [
        {
            "group": "S",
            "dataRows": [
                {
                    "id": "126052",
                    "baclmc": "水星米兰2.5-MT",
                    "cxzdjg": 352600,
                    "backgz": 30137,
                    "bapail": 2.488,
                    "bazwsl": 5
                },
                {
                    "id": "122355",
                    "baclmc": "水星米兰2.5-MT",
                    "cxzdjg": 352600,
                    "backgz": 30137,
                    "bapail": 2.488,
                    "bazwsl": 5
                },
                {
                    "id": "84706",
                    "baclmc": "水星米兰2.5-MT",
                    "cxzdjg": 352600,
                    "backgz": 30137,
                    "bapail": 2.488,
                    "bazwsl": 5
                },
                {
                    "id": "57711",
                    "baclmc": "水星米兰2.5-MT",
                    "cxzdjg": 352600,
                    "backgz": 30137,
                    "bapail": 2.488,
                    "bazwsl": 5
                },
                {
                    "id": "41421",
                    "baclmc": "水星米兰2.5-MT",
                    "cxzdjg": 352600,
                    "backgz": 30137,
                    "bapail": 2.488,
                    "bazwsl": 5
                },
                {
                    "id": "41420",
                    "baclmc": "水星米兰2.5-MT",
                    "cxzdjg": 352600,
                    "backgz": 30137,
                    "bapail": 2.488,
                    "bazwsl": 5
                },
                {
                    "id": "41419",
                    "baclmc": "水星米兰2.5-MT",
                    "cxzdjg": 352600,
                    "backgz": 30137,
                    "bapail": 2.488,
                    "bazwsl": 5
                },
                {
                    "id": "41417",
                    "baclmc": "水星米兰2.5-MT",
                    "cxzdjg": 352600,
                    "backgz": 30137,
                    "bapail": 2.488,
                    "bazwsl": 5
                }
            ]
        }
    ]
}
```

## 104. 保险试算接口
- url: `http://domain/core/getInsuranceTrial`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `carType`: 车辆类型
    - `baclzd`: 车辆指导价
    - `bapail`: 排量
    - `bxzwsl`: 座位数

- 入参示例:
    http://domain/core/getInsuranceTrial?carType=乘用车&baclzd=236000&bapail=1.6&bxzwsl=5

- 出参参数:
    - `commercialInsurance`: 商业险
    - `compulsoryInsurance`: 交强险
    - `vehicleTax`: 车船险

- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "commercialInsurance": "8086.0",
    "compulsoryInsurance": "950",
    "vehicleTax": "360"
  }
}
```

## 105. 线申请二期客户信息页面初期化
- url: `http://domain/approval/initCustomerInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号

- 入参示例:
    http://domain/approval/initCustomerInfo?applyNum=37248799

- 出参参数:
    - `realDriverName`: 实际用车人
    - `realDriverMobile`: 实际用车人手机号
    - `marriage`: 婚姻状况(0:未婚、1：已婚有子女)

- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "realDriverName": "罗浩健",
    "realDriverMobile": "18129788000",
    "marriage": "0"
  }
}
```

## 106. 获取所属行业接口
- url: `http://domain/core/getIndustry`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `searchName`: 查询关键字(可不传，模糊查询时传)

- 入参示例:
    http://domain/core/getIndustry?searchName=计

- 出参参数:
    - `BASJDM`: 所属行业名称
    - `ID`: 行业ID

- 出参示例:

```javascript
{
    "status": "SUCCESS",
      "error": "",
      "data": {
        "dataRows": [
          {
            "ID": "1254",
            "BASJDM": "计算机软件"
          },
          {
            "ID": "1255",
            "BASJDM": "计算机硬件"
          },
          {
            "ID": "1256",
            "BASJDM": "计算机服务（系统、数据服务、维修）"
          },
          {
            "ID": "1263",
            "BASJDM": "会计/审计"
          },
          {
            "ID": "1287",
            "BASJDM": "家居/室内设计/装潢"
          }
        ]
      }
}
```
# 107. 查询优质认证列表
- url: `http://domain/approval/getAuthInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `searchType`: 查询类型（1: 未认证， 2: 已认证）
- 入参示例:
    http://domain/approval/getAuthInfo?type=1
- 出参参数:
     - `name`: 姓名
     - `reason`: 原因
     - `status`: 状态(通过, 拒绝)
     - `uniqueMark`: 申请单唯一标识uuid
     - `isAutoApproval`: 是否为自动预审批(0: 自动预审批，1：人工预审批)
     - `idCardNum`: 省份证号
     - `phoneNumber`: 手机号
     - `appluNum`: 申请编号
     - `certificationStatus`: 认证状态
     - `type`: 时间
     - `createUser`: 创建人

- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
   {
         "name": "刘云",
         "reason": "人工",
         "status": "通过",
         "uniqueMark": "05955416521e4c75ad657e03c1f9298c",
         "isAutoApproval": "0",
         "idCardNum": "321023199202036214",
         "phoneNumber": "15821786884",
         "applyNum": "37248073",
         "certificationStatus": "000",
         "time": "2017-07-06",
         "createUser": "SH000"
   },
    ...
  ]
  }
```

## 108. 保存优质认证参数
- url: `http://domain/approval/getAuthParam`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `type`: 认证类型（微信:weixin/支付宝:zhifubao/淘宝:taobao）
- `uniqueMark`: uuid
- `token`: 本次认证凭证
- `sequenceNo`: 认证唯一key

- 入参示例:
    http://domain/approval/getAuthParam?type=weixin&token=123&uniqueMark=aaaaqqqqwweerrtttgg33455&sequenceNo=fjdkslkdfh34567fx45g788900

- 出参参数:

- 出参示例:
```javascript
{
    "status": "SUCCESS",
     "error": "",
     "data": null
}
```

## 109. 获取融资信息的接口
- url: `http://domain/core/getLeaseCalculation`
- 请求方式: `POST`
- 入参位置: Request Body
- 入参参数:
    - `productId`: 细分产品ID
    - `productName`: 细分产品名称
    - `leaseType`: 租赁属性（回租赁）
    - `applyType`: 申请类型
    - `carType`: 车辆类型
    - `mainType`: 车辆新旧类型
    - `salePrice`: 车辆销售价
    - `financeTerm`: 融资期限
    - `paymentRatio`: 首付比例
    - `tailPay`: 尾付比例
    - `serviceRatio`: 服务费率
    - `poundageRatio`: 手续费率
    - `bondRatio`: 保证金率
    - `rentRatio`: 租赁管理费率
    - `purchaseTax`: a2购置税
    - `installationFee`: a3加装费
    - `extendedWarrantyFee`: a4延保
    - `gpsFee`: a6 GPS硬件
    - `compulsoryInsurance`: 交强险合计
    - `commercialInsurance`: 商业险合计
    - `vehicleTax`: 车船税合计
    - `xfws`: 暂时没用了，传0
    - `unexpected`: 人身意外档位
    - `qingHongBao`: 清泓宝
    - `competitiveProducts`: 精品
    - `xfwsFlag`: 是否融先锋卫士
    - `xfwsTerm`: 先锋卫士期限(不融时传0)
    - `productTypeName`: 产品类型名称
    - `root`: 来源
    - `gpsFeeFlag`: 是否融GPS硬件(0/1)

- 入参示例:
    http://domain/core/getLeaseCalculation
```javascript
    {
    	"productId":"1007452",
    	"productName":" MMPV新车特惠融（普通）",
    	"leaseType":"回租赁",
    	"applyType":"个人",
    	"carType":"乘用车",
    	"mainType":"新车",
    	"salePrice":"100000",
    	"financeTerm":"36",
    	"paymentRatio":"30",
    	"tailPay":"0",
    	"serviceRatio":"1",
    	"poundageRatio":"2",
    	"bondRatio":"0",
    	"rentRatio":"2",
    	"purchaseTax":"100",
    	"installationFee":"200",
    	"extendedWarrantyFee":"300",
    	"gpsFee":"400",
    	"compulsoryInsurance":"241",
    	"commercialInsurance":"121",
    	"vehicleTax":"12",
    	"xfws":"0",
    	"unexpected":"10‰",
    	"qingHongBao":"100",
    	"competitiveProducts":"800",
    	"xfwsFlag":"1",
    	"xfwsTerm":"36",
    	"productTypeName":"微面特惠融（普通）",
        "root":"先锋特惠",
        "gpsFeeFlag":"1"
    }

- 出参参数:
     - `BABZJFY`: 保证金
     - `BAWFJE`: 尾付金额
     - `BASFJE`: 首付金额
     - `BAFWFY`: 服务费
     - `BARZZE`: 融资总额
     - `BAKHZJ`:
     - `BATZZE`: 投资总额
     - `BASXFY`: 手续费
     - `BAZLGLFY`: 租赁管理费
     - `YWBZFY`: 意外保障金额
     - `XFWSJE`: 先锋卫士金额
     - `FXRZE`: 风险融资额

- 出参示例:

```javascript
{
   "status": "SUCCESS",
    "error": "",
    "data": {
         "BABZJFY": "0.0",
         "BAWFJE": "0.0",
         "BASFJE": "30000.0",
         "BAFWFY": "700.0",
         "BARZZE": "73174.0",
         "BAKHZJ": "2521.58",
         "BATZZE": "103174.0",
         "BASXFY": "1463.0",
         "BAZLGLFY": "1400.0",
         "YWBZFY": "480.0",
         "XFWSJE": "1356",
         "FXRZE": "9000.0"
    }
  }
  ```
## 110. 获取产品详细信息接口
- url: `http://domain/core/getProductDetail`
- 请求方式: `POST`
- 入参位置: Request Body
- 入参参数:
    - `productId`: 产品ID
    - `productName`: 产品名称
    - `leaseType`: 租赁属性（回租赁）
    - `applyType`: 申请类型
    - `carType`: 车辆类型
    - `mainType`: 车辆新旧类型
    - `salePrice`: 车辆销售价
- 入参示例:
    http://domain/core/getProductDetail
```javascript
    {
        "productId": "1007525",
    	"productName": "明白融B套餐2年期手续费期初收取",
    	"leaseType":"回租赁",
    	"applyType":"个人",
    	"carType":"乘用车",
    	"mainType": "新车",
    	"salePrice":"200000"

    }
```
- 出参参数:
     - `WFJEQJ`: 尾付金额区间
     - `RZQXSZ`: 融资期限数组
     - `BZJLQJ`: 保证金率期间
     - `RSYWDWSZ`: 人生意外档位数组
     - `SFJEQJ`: 首付金额区间
     - `SFBLQJ`: 首付比例区间
     - `ZLGLFLQJ`: 租赁管理费率区间
     - `WFBLQJ`: 尾付比例区间
     - `RZXMSZ`: 融资项目数组（a1:车辆销售价，a2:购置税，a3:加装费，a4:延保，a6:GPS硬件，a7:交强险保费合计，a8:商业险保费合计，a9:车船税保费合计，a14:清泓宝，a15:意外保障，a16:先锋卫士，a18:精品）
     - `FWFLQJ`: 服务费率区间
     - `SXFLQJ`: 手续费率区间

- 出参示例:
```javascript
{
     "status": "SUCCESS",
     "error": "",
     "data": {
         "WFJEQJ": "0.0-0.0",
         "RZQXSZ": "36",
         "BZJLQJ": "20.0-20.0",
         "RSYWDWSZ": "2.2-4-6-8-10",
         "SFJEQJ": "0.0-10000000",
         "SFBLQJ": "0.0-100",
         "ZLGLFLQJ": "0-0",
         "WFBLQJ": "0.0-0.0",
         "RZXMSZ": "a1- a4- a6- a7- a8- a9- a15- a16",
         "FWFLQJ": "0-0",
         "SXFLQJ": "13.38-13.38"
     }
}
```

## 111. 获取融资相关参数接口
 - url: `http://domain/onLineApply/getApplyParam`
 - 请求方式: `GET`
 - 入参位置: `Request Parameter`
 - 入参参数:
 - `uniqueMark`: 申请编号

 - 入参示例:
     http://domain/onLineApply/getApplyParam?uniqueMark=4176b4f7c53c4d838d3d0a21431a1ffa

 - 出参参数:
      - `mainType`: 车辆新旧
      - `carType`: 车辆类型
      - `specificTypeId`: 细分产品ID
      - `specificTypeName`: 细分产品名称
      - `salePrice`: 车辆售价
      - `leaseType`: 租赁类型
      - `guidancePrices`: 指导价
      - `usedCarGuidancePrices`: 二手车评估价
      - `displacement`: 排量
      - `seatNumber`: 座位数量
      - `referenceCarPurchaseTax`: 参考购置税
      - `productTypeName`: 产品类型名称
      - `root`: 来源

 - 出参示例:
 ```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "mainType": "新车",
        "carType": "乘用车",
        "specificTypeId": "1007450",
        "specificTypeName": "华宇轻卡特惠融",
        "salePrice": null,
        "leaseType": null,
        "guidancePrices": null,
        "usedCarGuidancePrices": null,
        "displacement": null,
        "seatNumber": null,
        "referenceCarPurchaseTax": null,
        "productTypeName": "轻卡特惠融",
        "root": "HPL"
    }
}
 ```

## 112. 在线融资申请结果查询
 - url: `http://domain/onLineApply/{uniqueMark}/log`
 - 请求方式：`GET`
 - 入参位置： `Path Variable`
 - 入参参数：
 	- `uniqueMark`: 唯一标识
 - 入参示例：
 	http://domain/onLineApply/36146445/log
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
 			- `RGTHYY`: 人工退回原因
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
           "BASHRQ": "20160802120143",
           "RGTHYY": "征信不良，不建议操作。"
         }
       ]
     }
   }
}
 ```

## 113. 完善申请提交
 - url: `http://domain/onLineApply/applySubmit`
 - 请求方式：`GET`
 - 入参位置： `Request Parameter`
 - 入参参数：
 	- `uniqueMark`: 唯一标识
 	- `deviceId`: 设备id
    - `ipAddress`: ip地址
    - `location`: 经纬度
    - `os`: 操作系统
    - `brand`: 手机型号
 - 入参示例:
        http://domain/onLineApply/applySubmit?uniqueMark=4de0351b53384a81b8153979142afbb7&deviceId=123524424
        &ipAddress=222.73.56.99&location=117.4567，23.24&os=android&brand=nokia9
 - 出参参数：

 - 出参示例：

 ```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
 ```

## 114. 获取二手车评估单号
  - url: `http://domain/onLineApply/getUsedCarEvaluationNum`
  - 请求方式：`GET`
  - 入参示例：
  	http://domain/onLineApply/getUsedCarEvaluationNum
  - 出参参数：

  - 出参示例：

  ```javascript
{
    "status": "SUCCESS",
    "error": null,
    "data": "80"
}
  ```

## 115. 新增賬戶
  - url: `http://domain/sysUsers/addUserWx`
  - 请求方式：`POST`
   - 入参位置： `Request Body`
   - 入参参数：
  	- `userCode`: 新增用户账号
  	- `userType`: 新增用户角色（1：分公司账号；2：团队账号）
  	- `password`: 密码
  	- `phoneNum`: 手机号
  	- `sjbmdm`: 上级部门代码(经销商创建分公司，sjbmdm为经销商的xtczdm；经销商为分公司创建团队，sjbmdm为分公司的xtczdm；分公司创建自己的团队，sjbmdm为自己的xtczdm)

   - 入参示例：
   	http://domain/sysUsers/addUserWx

```javascript
   {
     "userCode": "SH001",
     "userType": 1,
     "password": "1234567",
     "phoneNum": "13867596846",
     "sjbmdm": "nijing"
   }
```
  - 出参参数：
  - 出参示例：

```javascript
  {
    "status": "SUCCESS",
    "error": null,
    "data": null
  }
```

## 116. 删除賬戶
  - url: `http://domain/sysUsers/deleteUserWx`
  - 请求方式：`GET`
   - 入参位置： `Request Parameter`
   - 入参参数：
  	- `userCode`: 删除用户账号
   - 入参示例：
   	http://domain/sysUsers/deleteUserWx?userCode=SH001
  - 出参参数：
  - 出参示例：

```javascript
  {
    "status": "SUCCESS",
    "error": null,
    "data": null
  }
```

## 117. 修改賬戶密码
  - url: `http://domain/sysUsers/updateUserWx`
  - 请求方式：`POST`
   - 入参位置： `Request Body`
   - 入参参数：
  	- `userCode`: 新增用户账号
  	- `newPwd`: 修改后密码
   - 入参示例：
   	http://domain/sysUsers/updateUserWx

```javascript
   {
     "userCode": "SH001",
     "newPwd": 1234567,
   }
```
  - 出参参数：
 - 出参示例：

```javascript
  {
    "status": "SUCCESS",
    "error": null,
    "data": null
  }
```

## 118. 组织架构展示接口
 - url: `http://domain/sysUsers/userWxList`
 - 请求方式：`GET`
 - 入参参数：无
 - 入参示例：
 	http://domain/sysUsers/userWxList
 - 出参参数：
  	- `type`: 类型
  	- `branchOfficeList`: 分公司账号集合
  			- `userName`: 分公司账号
  			- `teamList`: 团队账号集合
    - `teamList`: 团队账号集合
 - 出参说明：
    - type="0"时，表示当前账号为经销商，此时取branchOfficeList作为组织架构
    - type="1"时，表示当前账号为分公司，此时取teamList作为组织架构
    - type="2"时，表示当前账号为团队，此时无需组织架构
  - 出参示例：

```javascript
 {
   "status": "SUCCESS",
   "error": "",
   "data": {
     "type": "0",
     "branchOfficeList": [
       {
         "userName": "leadubao",
         "teamList": [
           "leaduhu",
           "leadudu"
         ]
       },
       {
         "userName": "leaduzhu",
         "teamList": []
       }
     ],
     "teamList": []
   }
 }
```

## 119. 角色同步接口
   - url: `http://domain/sysUsers/syncRole`
   - 请求方式：`GET`
   - 入参位置：
   - 入参参数：

   - 入参示例：
     http://domain/sysUsers/syncRole
   - 出参参数：
   - 出参示例：

## 120. 同步单一角色接口
  - url: `http://domain/sysUsers/syncOneRole`
  - 请求方式：`GET`
   - 入参位置： `Request Parameter`
   - 入参参数：
  	- `userName`: 用户账号
   - 入参示例：
   	http://domain/sysUsers/syncOneRole?userName=SH001
  - 出参参数：
  - 出参示例：

```javascript
  {
    "status": "SUCCESS",
    "error": null,
    "data": null
  }
```

## 121.查询优质认证不同状态数量
- url: `http://domain/approval/getAuthCount`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参示例:
    http://domain/approval/getAuthCount
- 出参参数:
     - `noAuthCount`: 未认证数量
     - `authCount`: 已认证数量
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "authCount": 4,
    "noAuthCount": 4
  }
}
```

## 122.添加客户地址信息
- url: `http://domain/onLineApply/addAddressInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号(必传)
- 入参位置: Request Body
- 入参参数:
  - `livingState`: 居住状况
  - `propertyArea`: 房产区域
  - `propertyPledgeState`: 房产抵押状况
  - `propertyAcreage`: 房产面积
  - `householdRegistrationProv`: 户籍所在省份
  - `householdRegistrationCity`: 户籍所在城市
  - `actualAddress`: 实际居住地址
  - `actualAddressProv`: 实际居住地址省份
  - `actualAddressCity`: 实际居住地址城市
- 入参示例:
    http://domain/onLineApply/addAddressInfo?uniqueMark=123155

```javascript
{
	"livingState": "自有无贷",
	"propertyArea":"市区",
	"propertyPledgeState":"未抵押",
	"propertyAcreage":"100",
	"householdRegistrationProv": "安徽",
	"householdRegistrationCity":"六安",
	"actualAddress":"华府",
	"actualAddressProv":"安徽",
    "actualAddressCity":"合肥"
}
```

- 出参参数:
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

## 123. 查询客户地址信息
- url: `http://domain/onLineApply/getAddressInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号

- 入参示例:
    http://domain/onLineApply/getAddressInfo?uniqueMark=9527

- 出参参数:
   - `livingState`: 居住状况
   - `propertyArea`: 房产区域
   - `propertyPledgeState`: 房产抵押状况
   - `propertyAcreage`: 房产面积
   - `householdRegistrationProv`: 户籍所在省份
   - `householdRegistrationCity`: 户籍所在城市
   - `actualAddress`: 实际居住地址
   - `actualAddressProv`: 实际居住地址省份
   - `actualAddressCity`: 实际居住地址城市
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
            "livingState": "自有无贷",
            "propertyArea":"市区",
            "propertyPledgeState":"未抵押",
            "propertyAcreage":"100",
            "householdRegistrationProv": "安徽",
            "householdRegistrationCity":"六安",
            "actualAddress":"华府",
            "actualAddressProv":"安徽",
            "actualAddressCity":"合肥"
            }
}
```

## 124. 保存太盟宝app用户登录地理位置信息
- url: `http://domain/sysUsers/saveLoginInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
    - `userName`: 用户名(必须输入)
    - `lat`: 经度(必须输入)
    - `lon`: 纬度(必须输入)
    - `address`: 中文地址

- 入参示例:
    http://domain/sysUsers/saveLoginInfo?userName=SH000&lat=39.913&lon=116.404&address=北京天安门

- 出参参数:
   - 无
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```



## 125. 通过邀请码新增賬戶
- url: `http://domain/sysUsers/addUser`
- 请求方式：`POST`
  - 入参位置： `Request Body`
  - 入参参数：
  	- `name`: 用户姓名
  	- `password`: 密码
  	- `phoneNum`: 手机号
  	- `code`: 邀请码：必传
  	- `msgCode`: 短信验证码

   - 入参示例：
   	http://domain/sysUsers/addUser

```javascript
   {
     "name": "杜雨生",
     "password": "1234567",
     "phoneNum": "13867596846",
     "code": "6660",
     "msgCode": "423422"
   }
```
  - 出参参数：
    - 用户名
  - 出参示例：

```javascript
  {
    "status": "SUCCESS",
    "error": null,
    "data": "SH000-001"
  }
```


## 126. 修改自身账号賬戶密码
- url: `http://domain/sysUsers/updateUser`
- 请求方式：`POST`
   - 入参位置： `Request Body`
   - 入参参数：
  	- `oldPwd`: 原密码
  	- `newPwd`: 修改后密码
   - 入参示例：
   	http://domain/sysUsers/updateUser

```javascript
   {
     "oldPwd": "iphonex8888",
     "newPwd": 1234567,
   }
```
  - 出参参数：
 - 出参示例：

```javascript
  {
    "status": "SUCCESS",
    "error": null,
    "data": null
  }
```
## 127. 上传太盟宝用户联系人信息
- url: `http://domain/sysUsers/uploadContacts`
- 请求方式: `POST`
- 入参参数:
    - `userName`: 用户名(必须输入)  Request Parameter
    - `contacts`: 联系人数组集合  RequestBody

- 入参示例:
    http://domain/sysUsers/uploadContacts?userName=SH000
    以下是RequestBody的内容
    [
        {
            "name": "张三",
            "phoneNum": "18255111681"
        },
        {
            "name": "李四",
            "phoneNum": "18255111682"
        },
        {
            "name": "王五",
            "phoneNum": "18255111683"
        },
        {
            "name": "徐六",
            "phoneNum": "18255111684"
        },
        {
            "name": "陈七",
            "phoneNum": "18255111685"
        },
        {
            "name": "刘八",
            "phoneNum": "18255111686"
        }
    ]

- 出参参数:
   - 无
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

## 128. 通过手机号将录单消息推送到客户微信号上
- url: `http://domain/sysUsers/pushOrder`
- 请求方式: `POST`
- 入参参数:
    - `phoneNum`: 客户电话  RequestBody
    - `userName`: 经销商账号  RequestBody

- 入参示例:
    http://domain/sysUsers/pushOrder
    以下是RequestBody的内容
    {
        "phoneNum": "18255111681",
        "userName": "SH000"
    }
- 出参参数:
   - 无
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```


## 129. 发送短信验证码接口
- url: `http://domain/sysUsers/sendRandomCode`
- 请求方式：`POST`
  - 入参参数：
  	- `phoneNum`: 手机号

   - 入参示例：
   	http://domain/sendRandomCode?phoneNum=18055313782

  - 出参示例：

```javascript
  {
      "status": "SUCCESS",
      "error": "",
      "data": null
  }
```

## 130. 禁用賬戶
  - url: `http://domain/sysUsers/banUserWx`
  - 请求方式：`GET`
   - 入参位置： `Request Parameter`
   - 入参参数：
  	- `userCode`: 禁止用户账号登录
   - 入参类型：String
   - 入参示例：
   	http://domain/sysUsers/banUserWx?userCode=SH000FGS01
  - 出参参数：
  - 出参示例：

```javascript
  {
    "status": "SUCCESS",
    "error": null,
    "data": null
  }
```


## 131. 获取预审批附件信息
- url: `http://domain/approval/getApprovalImages`
- 请求方式：`GET`
   - 入参位置： `Request Parameter`
   - 入参参数：
  	- `uniqueMark`: 唯一标识
- 入参类型：String
- 入参示例：
   	http://domain/approval/getApprovalImages?uniqueMark=00474a08392d4f598fc0686a2dd2c1aa
- 出参参数：
     - `frontImg`: 身份证正面
     - `behindImg`: 身份证反面
     - `driveLicenceImg`: 驾驶证
     - `bankImg`: 银行卡
- 出参示例：

```javascript
  {
      "status": "SUCCESS",
      "error": "",
      "data": {
          "frontImg": "http://222.73.56.27:5082/approvalIdCard/20180131/9625e938-ed26-452f-97ad-35b7ad38527f.jpg",
          "behindImg": "http://222.73.56.27:5082/approvalIdCard/20180131/9709d127-762b-4597-90e2-0d669f296e49.jpg",
          "driveLicenceImg": "http://222.73.56.27:5082/approvalDrive/20180131/692e6089-226b-47b3-9da7-dc29461b643f.jpg",
          "bankImg": "http://222.73.56.27:5082/approvalBank/20180131/48442e07-1644-48d4-8e29-864b033f265e.jpg"
      }
  }
```

## 132.添加客户附件信息
- url: `http://domain/onLineApply/addApplyFileInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号(必传)
- 入参位置: Request Body
- 入参参数:
  - `letterOfCredit`: 征信授权书
  - `handHoldLetterOfCredit`: 手持征信授权书
  - `applyForm`: 申请表照片
  - `frontImg`: 身份证正面
  - `behindImg`: 身份证反面
  - `driveLicenceImg`: 驾驶证
  - `bankImg`: 银行卡
- 入参示例:
    http://domain/onLineApply/addApplyFileInfo?uniqueMark=831d7ff4ff3c4da6b3983fcaa1140a18

```javascript
{
    "letterOfCredit": "http://222.73.56.27:5082/approvalIdCard/20180115/d86782b1-dfe6-44a9-8239-5b9ec9327df3.jpg",
    "handHoldLetterOfCredit": "http://222.73.56.27:5082/approvalIdCard/20180115/d86782b1-dfe6-44a9-8239-5b9ec9327df3.jpg",
    "applyForm": "http://222.73.56.27:5082/approvalIdCard/20180115/d86782b1-dfe6-44a9-8239-5b9ec9327df3.jpg",
    "frontImg": "http://222.73.56.27:5082/approvalIdCard/20180115/d86782b1-dfe6-44a9-8239-5b9ec9327df3.jpg",
    "behindImg": "http://222.73.56.27:5082/approvalIdCard/20180115/5223d3f6-65db-470f-bbf0-8e3e460d007f.jpg",
    "driveLicenceImg": "http://222.73.56.27:5082/approvalDrive/20180115/0978cc8c-7027-419b-9da1-ee06ccc661ef.jpg",
    "bankImg": "http://222.73.56.27:5082/approvalBank/20180115/2de5609d-a18f-4e0d-814f-d54969cf3685.jpg"
}
```

- 出参参数:
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

## 133. 查询客户附件信息
- url: `http://domain/onLineApply/getApplyFileInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号

- 入参示例:
    http://domain/onLineApply/getApplyFileInfo?uniqueMark=831d7ff4ff3c4da6b3983fcaa1140a18

- 出参参数:
  - `letterOfCredit`: 征信授权书
  - `handHoldLetterOfCredit`: 手持征信授权书
  - `applyForm`: 申请表照片
  - `frontImg`: 身份证正面
  - `behindImg`: 身份证反面
  - `driveLicenceImg`: 驾驶证
  - `bankImg`: 银行卡
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "letterOfCredit": "http://222.73.56.27:5082/approvalIdCard/20180115/d86782b1-dfe6-44a9-8239-5b9ec9327df3.jpg",
        "handHoldLetterOfCredit": "http://222.73.56.27:5082/approvalIdCard/20180115/d86782b1-dfe6-44a9-8239-5b9ec9327df3.jpg",
        "applyForm": "http://222.73.56.27:5082/approvalIdCard/20180115/d86782b1-dfe6-44a9-8239-5b9ec9327df3.jpg",
        "frontImg": "http://222.73.56.27:5082/approvalIdCard/20180115/d86782b1-dfe6-44a9-8239-5b9ec9327df3.jpg",
        "behindImg": "http://222.73.56.27:5082/approvalIdCard/20180115/5223d3f6-65db-470f-bbf0-8e3e460d007f.jpg",
        "driveLicenceImg": "http://222.73.56.27:5082/approvalDrive/20180115/0978cc8c-7027-419b-9da1-ee06ccc661ef.jpg",
        "bankImg": "http://222.73.56.27:5082/approvalBank/20180115/2de5609d-a18f-4e0d-814f-d54969cf3685.jpg"
    }
}
```


## 134.添加共申人附件信息
- url: `http://domain/onLineApply/addJointFileInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号(必传)
- 入参位置: Request Body
- 入参参数:
  - `jointIdCardFrontImg`: 共申人身份证正面
  - `jointIdCardBehindImg`: 共申人身份证反面
  - `jointLetterOfCredit`: 共申人征信授权书
  - `jointHandHoldLetterOfCredit`: 共申人手持征信授权书
- 入参示例:
    http://domain/onLineApply/addJointFileInfo?uniqueMark=831d7ff4ff3c4da6b3983fcaa1140a18

```javascript
{
    "jointIdCardFrontImg": "http://222.73.56.27:5082/letterOfCredit/20180129/87a8d2d5-23c2-486c-b183-442b6d611394.jpg",
    "jointIdCardBehindImg": "http://222.73.56.27:5082/letterOfCredit/20180129/87a8d2d5-23c2-486c-b183-442b6d611394.jpg",
    "jointHandHoldLetterOfCredit": "http://222.73.56.27:5082/letterOfCredit/20180129/87a8d2d5-23c2-486c-b183-442b6d611394.jpg",
    "jointLetterOfCredit": "http://222.73.56.27:5082/letterOfCredit/20180129/87a8d2d5-23c2-486c-b183-442b6d611394.jpg"
}
```

- 出参参数:
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

## 135. 查询共申人附件信息
- url: `http://domain/onLineApply/getJointFileInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号

- 入参示例:
    http://domain/onLineApply/getJointFileInfo?uniqueMark=831d7ff4ff3c4da6b3983fcaa1140a18

- 出参参数:
      - `jointIdCardFrontImg`: 共申人身份证正面
      - `jointIdCardBehindImg`: 共申人身份证反面
      - `jointLetterOfCredit`: 共申人征信授权书
      - `jointHandHoldLetterOfCredit`: 共申人手持征信授权书
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "jointIdCardFrontImg": "http://222.73.56.27:5082/letterOfCredit/20180129/87a8d2d5-23c2-486c-b183-442b6d611394.jpg",
        "jointIdCardBehindImg": "http://222.73.56.27:5082/letterOfCredit/20180129/87a8d2d5-23c2-486c-b183-442b6d611394.jpg",
        "jointHandHoldLetterOfCredit": "http://222.73.56.27:5082/letterOfCredit/20180129/87a8d2d5-23c2-486c-b183-442b6d611394.jpg",
        "jointLetterOfCredit": "http://222.73.56.27:5082/letterOfCredit/20180129/87a8d2d5-23c2-486c-b183-442b6d611394.jpg"
    }
}
```

## 136.添加车辆及抵押信息
- url: `http://domain/onLineApply/addCarAndPledgeInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号(必传)
- 入参位置: Request Body
- 入参参数:
  - `mfrs`: 制造商
  - `brand`: 品牌
  - `type`: 车型
  - `typeId`: 车型ID
  - `officialPrice`: 指导价
  - `salePrice`: 销售价格
  - `carPurchaseTax`: 参考购置税
  - `secondOfficialPrice`: 二手车评估价
  - `secondDate`: 二手车出场日期
  - `secondYears`: 二手车车龄
  - `secondDistance`: 二手车公里数
  - `displacement`: 排量
  - `seatNumber`: 座位数量
  - `saler`: 车辆经销商名称
  - `licenseAttribute`: 牌照属性
  - `pledgeCity`: 回租赁抵押城市
  - `pledgeCompany`: 正租赁上牌&回租赁抵押公司
- 入参示例:
    http://domain/onLineApply/addCarAndPledgeInfo?uniqueMark=2411211

```javascript
{
        "carInfoDto": {
            "mfrs": "大众",
            "brand": "帕萨塔",
            "type": "帕萨塔豪华版",
            "typeId": "564235",
            "officialPrice": "200000",
            "salePrice": "188800",
            "carPurchaseTax": "12345",
            "secondOfficialPrice": "",
            "secondDate": "",
            "secondYears": "",
            "secondDistance": "",
            "displacement": "1.8",
            "seatNumber": "4"
        },
        "carPledgeDto": {
            "saler": "车辆经销商",
            "licenseAttribute": "省级牌照",
            "pledgeCity": "合肥市",
            "pledgeCompany": "上海领友"
        }
}
```

- 出参参数:
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

## 137.查询车辆及抵押信息
- url: `http://domain/onLineApply/getCarAndPledgeInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号(必传)

- 入参示例:
    http://domain/onLineApply/getCarAndPledgeInfo?uniqueMark=423123

- 出参参数:
    - `mfrs`: 制造商
    - `brand`: 品牌
    - `type`: 车型
    - `typeId`: 车型ID
    - `officialPrice`: 指导价
    - `salePrice`: 销售价格
    - `carPurchaseTax`:参考购置税
    - `secondOfficialPrice`: 二手车评估价格
    - `secondDate`: 二手车出场日期
    - `secondDistance`: 二手车公里数
    - `displacement`: 排量
    - `seatNumber`: 座位数量
    - `saler`: 车辆经销商名称
    - `licenseAttribute`: 牌照属性
    - `pledgeCity`: 回租赁抵押城市
    - `pledgeCompany`: 正租赁上牌&回租赁抵押公司
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "carInfoDto": {
            "mfrs": "大众",
            "brand": "帕萨塔",
            "type": "帕萨塔豪华版",
            "typeId": "564235",
            "officialPrice": "200000",
            "salePrice": "188800",
            "carPurchaseTax": "12345",
            "secondOfficialPrice": "",
            "secondDate": "",
            "secondYears": "",
            "secondDistance": "",
            "displacement": "1.8",
            "seatNumber": "4"
        },
        "carPledgeDto": {
            "saler": "车辆经销商",
            "licenseAttribute": "省级牌照",
            "pledgeCity": "合肥市",
            "pledgeCompany": "上海领友"
        }
    }
}
```

## 138.添加配偶附件信息
- url: `http://domain/onLineApply/addMateFileInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号(必传)
- 入参位置: Request Body
- 入参参数:
  - `mateIdCardFrontImg`: 共申人身份证正面
  - `mateIdCardBehindImg`: 共申人身份证反面
  - `mateLetterOfCredit`: 共申人征信授权书
  - `mateHandHoldLetterOfCredit`: 共申人手持征信授权书
- 入参示例:
    http://domain/onLineApply/addMateFileInfo?uniqueMark=831d7ff4ff3c4da6b3983fcaa1140a18

```javascript
{
    "mateIdCardFrontImg": "http://222.73.56.27:5082/letterOfCredit/20180129/87a8d2d5-23c2-486c-b183-442b6d611394.jpg",
    "mateIdCardBehindImg": "http://222.73.56.27:5082/letterOfCredit/20180129/87a8d2d5-23c2-486c-b183-442b6d611394.jpg",
    "mateLetterOfCredit": "http://222.73.56.27:5082/letterOfCredit/20180129/87a8d2d5-23c2-486c-b183-442b6d611394.jpg",
    "mateHandHoldLetterOfCredit": "http://222.73.56.27:5082/letterOfCredit/20180129/87a8d2d5-23c2-486c-b183-442b6d611394.jpg"
}
```

- 出参参数:
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

## 139. 查询配偶附件信息
- url: `http://domain/onLineApply/getMateFileInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号

- 入参示例:
    http://domain/onLineApply/getMateFileInfo?uniqueMark=831d7ff4ff3c4da6b3983fcaa1140a18

- 出参参数:
      - `mateIdCardFrontImg`: 共申人身份证正面
      - `mateIdCardBehindImg`: 共申人身份证反面
      - `mateLetterOfCredit`: 共申人征信授权书
      - `mateHandHoldLetterOfCredit`: 共申人手持征信授权书
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data":
     {
        "mateIdCardFrontImg": "http://222.73.56.27:5082/letterOfCredit/20180129/87a8d2d5-23c2-486c-b183-442b6d611394.jpg",
        "mateIdCardBehindImg": "http://222.73.56.27:5082/letterOfCredit/20180129/87a8d2d5-23c2-486c-b183-442b6d611394.jpg",
        "mateLetterOfCredit": "http://222.73.56.27:5082/letterOfCredit/20180129/87a8d2d5-23c2-486c-b183-442b6d611394.jpg",
        "mateHandHoldLetterOfCredit": "http://222.73.56.27:5082/letterOfCredit/20180129/87a8d2d5-23c2-486c-b183-442b6d611394.jpg"
    }
}
```



## 140.腾讯AI明信片OCR
- url: `http://domain//tencent/postCardOcr`
- 请求方式: `POST`
- 入参参数:
- `base64image`: 图片base64数据(必传)
- 入参位置: Request Body 

- 入参示例:
    http://domain//tencent/postCardOcr
    
```javascript
{
    "base64image":"/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAAMCAgMCAgMDAwMEAwMEBQgFBQQEBQoHBwYIDAoMDAsK
                  CwsNDhIQDQ4RDgsLEBYQERMUFRUVDA8XGBYUGBIUFRT/2wBDAQMEBAUEBQkFBQkUDQsNFBQUFBQU
                  bbAAoDjbb2w6HQQ3NgMFBQAu+xw+CQL+/OC0J7B2Pxt7YKFyFpF9v3wqECx4tfe2DtYAtsD2wluN
                  Owtgq+ABbU1sO2tgDHziP1ABG+/7YkmkADa+/GE2Ae9rm/5YOoAwBbe+C7YA5A3vvge7AB5uRxgS"
}
```

- 出参参数:具体返回结果含义
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {...}
}
```



## 141.faceId 身份证OCR扫描
- url: `http://domain/tenure/ocrIdCard`
- 请求方式: `POST`
- 入参位置: `Request Body`
- 入参参数:
- `image`: 图片文件(必传)
- 入参示例:
    http://domain/tenure/ocrIdCard
- 出参参数:
- 出参示例:

```javascript
{
  "birthday": {
    "year": "1992",
    "day": "27",
    "month": "7"
  },
  "name": "胡宗诚",
  "race": "汉",
  "address": "安徽省淮南市大通区大通居仁村三区42-3-8室",
  "time_used": 474,
  "gender": "男",
  "head_rect": {
    "rt": {
      "y": 0.2007874,
      "x": 0.8905473
    },
    "lt": {
      "y": 0.19488189,
      "x": 0.5995025
    },
    "lb": {
      "y": 0.71259844,
      "x": 0.6007463
    },
    "rb": {
      "y": 0.72047246,
      "x": 0.8868159
    }
  },
  "request_id": "1509694559,b0afa320-0b0e-499b-93b9-2cd307cd88d1",
  "id_card_number": "34242219920727123X",
  "side": "front"
}
```

## 142. 身份验证/活体检测接口
- url: `http://domain/tenure/verify`
- 请求方式: `POST`
- 入参位置: `Request Body`
- 入参参数:
- `image`: 身份证图片（活体检测调用时为空（不需要传））
- `image_best`: 活体检测图片（身份证验证调用时为空（不需要传））

- 入参示例:
    http://domain/tenure/verify

- 出参参数:
- 出参示例:

暂无，与face++官方接口文档一致
https://faceid.com/pages/documents/4173286


## 143.添加和修改预审批附件信息
- url: `http://domain/approval//addApprovalImages`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识(必传)
- 入参位置: Request Body
- 入参参数:
  - `frontImg`: 身份证正面
  - `behindImg`: 身份证反面
  - `driveLicenceImg`: 驾驶证照片
  - `bankImg`: 银行卡照片
- 入参示例:
    http://domain/approval/addApprovalImages?uniqueMark=4de0351b53384a81b8153979142afbb7
    
```javascript
{
    "frontImg": "http://36.7.188.214:5080/files/download/jointFile/20180319/6e71f4b8-1c3f-4d1b-a1c9-daba0ae6b963.jpg",
    "behindImg": "http://36.7.188.214:5080/files/download/applyForm/20180319/322369c3-1722-4b42-af66-1dedcfee8fda.jpg",
    "driveLicenceImg": "http://222.73.56.27:5082/approvalDrive/20180131/692e6089-226b-47b3-9da7-dc29461b643f.jpg",
    "bankImg": "http://36.7.188.214:5080/files/download/applyForm/20180312/4360ce5a-5d5d-4adc-8e32-dbff5009ab34.jpg"
}
```
- 出参参数:
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```


## 144. 查询担保人附件信息
- url: `http://domain/onLineApply/getGuaranteeFileInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识

- 入参示例:
    http://domain/onLineApply/getGuaranteeFileInfo?uniqueMark=3c07581505f64563bd59da47392c8ef8

- 出参参数:
      - `guaranteeIdCardFrontImg`: 担保人身份证正面
      - `guaranteeIdCardBehindImg`: 担保人身份证反面
      - `guaranteeHandHoldLetterOfCredit`: 担保人征信授权书
      - `guaranteeLetterOfCredit`: 担保人手持征信授权书
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
       "guaranteeIdCardFrontImg": "http://60.168.131.134:5080/files/download/mateFile/20180323/febcaae2-5de0-492c-8a2f-07ee5f91cf78.jpg",
       "guaranteeIdCardBehindImg": "http://60.168.131.134:5080/files/download/mateFile/20180323/febcaae2-5de0-492c-8a2f-07ee5f91cf78.jpg",
       "guaranteeHandHoldLetterOfCredit": "http://60.168.131.134:5080/files/download/mateFile/20180323/febcaae2-5de0-492c-8a2f-07ee5f91cf78.jpg",
       "guaranteeLetterOfCredit": "http://60.168.131.134:5080/files/download/mateFile/20180323/febcaae2-5de0-492c-8a2f-07ee5f91cf78.jpg"
    }
}
```


## 145.添加担保人附件信息
- url: `http://domain/onLineApply/addJointFileInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号(必传)
- 入参位置: Request Body
- 入参参数:
  - `guaranteeIdCardFrontImg`: 担保人身份证正面
  - `guaranteeIdCardBehindImg`: 担保人身份证反面
  - `guaranteeHandHoldLetterOfCredit`: 担保人征信授权书
  - `guaranteeLetterOfCredit`: 担保人手持征信授权书
- 入参示例:
    http://domain/onLineApply/addJointFileInfo?uniqueMark=831d7ff4ff3c4da6b3983fcaa1140a18

```javascript
{
   "guaranteeIdCardFrontImg": "http://60.168.131.134:5080/files/download/mateFile/20180323/febcaae2-5de0-492c-8a2f-07ee5f91cf78.jpg",
   "guaranteeIdCardBehindImg": "http://60.168.131.134:5080/files/download/mateFile/20180323/febcaae2-5de0-492c-8a2f-07ee5f91cf78.jpg",
   "guaranteeHandHoldLetterOfCredit": "http://60.168.131.134:5080/files/download/mateFile/20180323/febcaae2-5de0-492c-8a2f-07ee5f91cf78.jpg",
   "guaranteeLetterOfCredit": "http://60.168.131.134:5080/files/download/mateFile/20180323/febcaae2-5de0-492c-8a2f-07ee5f91cf78.jpg"
}
```

- 出参参数:
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```




## 146. 查询待签约列表
- url: `http://domain/onApplySign/getLocalInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `condition`: 检索条件(此参数传值即根据查询条件模糊匹配，不传值则查询所有数据)
    - `beginTime`: 查询开始时间 格式：20180101  非必传
    - `endTime`: 查询结束时间 格式：20180101  非必传
- 入参示例:
    http://domain/onApplySign/getLocalInfo?beginTime=20170101&endTime=20180430
- 出参参数:
     - `name`: 姓名
     - `applyNum`: 申请编号
     - `passTime`: 创建时间
     - `userName`: 创建人
     - `state`: 状态
     - `contractState`: 合同生成状态（"",null,0:未生成: 不可签约,1:已生成（不可在生成，不可完善信息））
     - `signState`: 签约状态状态（"",null,0:未签约: 1:已签约（不可再签约））
     - `productType`: 产品类型（HPL,在线助力融）
     - `bankVerifyState`: 银行四要素验证状态状态（"",null,0:未验证， 1:已验证（不需要再做银行卡验证））
     
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": [
        {
            "name": "张明才",
            "applyNum": "36143945",
            "passTime": "20180111",
            "userName": "SH000",
            "state": "审批通过待生成合同",
            "contractState": "0",
            "signState": "0",
            "bankVerifyState": "1"
        }
    ]
}
```


## 147.查询补全车辆信息
- url: `http://domain/onApplySign/getCarInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `applyNum`: 申请编号(必传)

- 入参示例:
    http://domain/onApplySign/getCarInfo?applyNum=38372837

- 出参参数:
  - `vehicleIdentifyNum`: 车架号
  - `dateOfManufacture`: 出厂日期（格式:20180808）
  - `colour`: 颜色
  - `engineNo`: 发动机号
  - `vehicleLicensePlate`: 车牌号
  - `vehicleConfigDescription`: 车辆配置描述
  - `signAuthBookImg`: 签署授权书
  - `holdAuthBookImg`: 手持签署授权书
  - `authBookImg`:授权书
  - `submitState`:提交状态 0,"",null :未提交  1：已提交 
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "vehicleIdentifyNum": "车架号",
        "dateOfManufacture": "出厂日期格式:20180808",
        "colour": "颜色",
        "engineNo": "发动机号",
        "vehicleLicensePlate": "皖A85777",
        "vehicleConfigDescription": "配置",
        "signAuthBookImg": "签署征信授权书",
        "authBookImg": "征信授权书",
        "holdAuthBookImg": "手持签署征信授权书",
        "submitState": "0"
    }
}
```

## 148.添加补全车辆信息
- url: `http://domain/onApplySign/submitCarInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 申请编号(必传)
- 入参位置: Request Body
- 入参参数:
  - `vehicleIdentifyNum`: 车架号
  - `dateOfManufacture`: 出厂日期（格式:20180808）
  - `colour`: 颜色
  - `engineNo`: 发动机号
  - `vehicleLicensePlate`: 车牌号
  - `vehicleConfigDescription`: 车辆配置描述
  - `signAuthBookImg`: 签署电子合同签章授权书
  - `holdAuthBookImg`: 手持签署授权书
  - `authBookImg`:电子合同签章授权书
- 入参示例:
    http://domain/onApplySign/submitCarInfo?applyNum=38372837
```javascript
{
    "vehicleIdentifyNum": "车架号",
    "dateOfManufacture": "出厂日期格式:20180808",
    "colour": "颜色",
    "engineNo": "发动机号",
    "vehicleLicensePlate": "皖A85777",
    "vehicleConfigDescription": "配置",
    "signAuthBookImg":"签署电子合同签章授权书",
    "holdAuthBookImg":"手持电子合同签章授权书",
    "authBookImg":"电子合同签章授权书"
}
```

- 出参参数:
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

## 149. 查询电子签约状态
- url: `http://domain/onApplySign/getSignStatus`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
- 入参示例:
    http://domain/onApplySign/getSignStatus?applyNum=3782623
- 出参参数:
     - `isICBC`: 是否是工行产品（是,否）
     - `applyContractStatus`: 申请人签约状态（0:未完善, 1:已完善）
     - `jointContractStatus`: 共申人人签约状态（0:未完善, 1:已完善, "" or null: 不需要）
     - `guaranteeContractStatus`: 担保人签约状态（0:未完善, 1:已完善, "" or null: 不需要）
     - `gpsInfoStatus`: GPS安装信息填写状态（0:未完善, 1:已完善）
     - `applicantInfo`: 主贷人信息
     - `jointInfo`: 共申人信息
     - `guaranteeInfo`: 担保人信息
     
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "isICBC": "否",
        "applyContractStatus": "0",
        "jointContractStatus": null,
        "guaranteeContractStatus": null,
        "gpsInfoStatus": null,
        "applicantInfo": {
            "phoneNum": null,
            "idCard": "342425199404085232",
            "name": "杜雨生",
            "contactPdf": null,
            "repaymentPlanPdf": null,
            "confirmationPdf": null,
            "mortgageContractPdf": null,
            "riskNotificationPdf": null
        },
        "jointInfo": {
            "phoneNum": null,
            "idCard": null,
            "name": null,
            "contactPdf": null,
            "repaymentPlanPdf": null,
            "confirmationPdf": null,
            "mortgageContractPdf": null,
            "riskNotificationPdf": null
        },
        "guaranteeInfo": {
            "phoneNum": null,
            "idCard": null,
            "name": null,
            "contactPdf": null,
            "repaymentPlanPdf": null,
            "confirmationPdf": null,
            "mortgageContractPdf": null,
            "riskNotificationPdf": null
        }
    }
}
```


## 150. 主贷人获取待签署的合同
- url: `http://domain/onApplySign/getOriginContract`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号（测试请用：12345678901）
- 入参示例:
    http://domain/onApplySign/getOriginContract?applyNum=12345678901
- 出参参数:
     - `contactPdfUrl`: 待签署合同pdf
     - `deliveryReceitp`: 待签署确认函pdf
     - `mortgageContractPdfUrl`: 待签署抵押合同pdf
     - `repaymentPlanPdfUrl`: 待签署还款计划表pdf
     - `riskNotificationPdfUrl`: 待签署风险告知书pdf
     
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "contactPdfUrl": "http://222.73.56.22:89/cmdOtherImg/20180410/26cd9cf8-2fc5-418b-bbeb-2b9e1f18c1cb.pdf",
        "deliveryReceitp": "http://222.73.56.22:89/cmdOtherImg/20180410/71440593-552b-4647-a517-33dd3e87d7f9.pdf",
        "mortgageContractPdfUrl": "http://222.73.56.22:89/cmdOtherImg/20180410/8082bbe5-ffb7-4a02-91ad-62019a3865d7.pdf",
        "repaymentPlanPdfUrl": "http://222.73.56.22:89/cmdOtherImg/20180410/a06931a7-4a77-42e2-9658-aecc7e1970bf.pdf",
        "riskNotificationPdfUrl": "http://222.73.56.22:89/cmdOtherImg/20180410/91db727f-9b04-4fbc-9d9f-40e942f27d8c.pdf"
    }
}
```


## 151. 签署合同
- url: `http://domain/onApplySign/sign`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
    - `code`: E签宝验证码
- 入参示例:
    http://domain/onApplySign/sign?applyNum=12345678901&code=548367
- 出参参数:
     - `contactSignPdfUrl`: 签署后合同pdf
     - `confirmationSignPdfUrl`: 签署后确认函pdf
     - `mortgageContractSignPdfUrl`: 签署后抵押合同pdf
     - `repaymentPlanSignPdfUrl`: 签署后还款计划表pdf
     - `riskNotificationSignPdfUrl`: 签署后风险告知书pdf
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "contactSignPdfUrl": "http://60.168.131.134:5080/files/download/signedPdf/20180412/杜雨生_合同_1523503274310.pdf",
        "confirmationSignPdfUrl": "http://60.168.131.134:5080/files/download/signedPdf/20180412/杜雨生_确认函_1523503277332.pdf",
        "mortgageContractSignPdfUrl": "http://60.168.131.134:5080/files/download/signedPdf/20180412/杜雨生_抵押合同_1523503278539.pdf",
        "repaymentPlanSignPdfUrl": "http://60.168.131.134:5080/files/download/signedPdf/20180412/杜雨生_还款计划表_1523503284098.pdf",
        "riskNotificationSignPdfUrl": "http://60.168.131.134:5080/files/download/signedPdf/20180412/杜雨生_风险告知书_1523503290248.pdf"
    }
}
```



## 152. 发送E签宝短信验证
- url: `http://domain/onApplySign/sendMessage`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
- 入参示例:
    http://domain/onApplySign/sendMessage?applyNum=36234211
- 出参参数:
- 出参示例:

```javascript
{
    "status": "ERROR",
    "error": "短信发送失败，创建个人账户失败",
    "data": null
}
```

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```



## 153. 共申人获取待签署的合同
- url: `http://domain/jointSign/getOriginContract`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号（测试请用：12345678901）
- 入参示例:
    http://domain/jointSign/getOriginContract?applyNum=12345678901
- 出参参数:
     - `contactPdfUrl`: 待签署合同pdf
     - `repaymentPlanPdfUrl`: 待签署还款计划表pdf
     
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "contactPdfUrl": "http://192.168.1.210:6080/files/download/signedPdf/20180412/杜雨生_合同_1523524434991.pdf",
        "confirmationPdfUrl": null,
        "mortgageContractPdfUrl": null,
        "repaymentPlanPdfUrl": "http://192.168.1.210:6080/files/download/signedPdf/20180412/杜雨生_还款计划表_1523524444913.pdf",
        "riskNotificationPdfUrl": null
    }
}
```


## 154. 共申人签署合同
- url: `http://domain/jointSign/sign`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
    - `code`: E签宝验证码
- 入参示例:
    http://domain/jointSign/sign?applyNum=12345678901&code=548367
- 出参参数:
     - `contactSignPdfUrl`: 签署后合同pdf
     - `repaymentPlanSignPdfUrl`: 签署后还款计划表pdf
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "contactPdfUrl": "http://192.168.1.210:6080/files/download/signedPdf/20180412/杜雨生_合同_1523525923439.pdf",
        "confirmationPdfUrl": null,
        "mortgageContractPdfUrl": null,
        "repaymentPlanPdfUrl": "http://192.168.1.210:6080/files/download/signedPdf/20180412/杜雨生_还款计划表_1523525924397.pdf",
        "riskNotificationPdfUrl": null
    }
}
```



## 155. 共申人发送E签宝短信验证
- url: `http://domain/jointSign/sendMessage`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
- 入参示例:
    http://domain/jointSign/sendMessage?applyNum=36234211
- 出参参数:
- 出参示例:

```javascript
{
    "status": "ERROR",
    "error": "短信发送失败，创建个人账户失败",
    "data": null
}
```

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```



## 156. 担保人获取待签署的合同
- url: `http://domain/guaranteeSign/getOriginContract`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号（测试请用：12345678901）
- 入参示例:
    http://domain/guaranteeSign/getOriginContract?applyNum=12345678901
- 出参参数:
     - `contactPdfUrl`: 待签署合同pdf
     - `repaymentPlanPdfUrl`: 待签署还款计划表pdf
     
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "contactPdfUrl": "http://192.168.1.210:6080/files/download/signedPdf/20180412/杜雨生_合同_1523525923439.pdf",
        "confirmationPdfUrl": null,
        "mortgageContractPdfUrl": null,
        "repaymentPlanPdfUrl": "http://192.168.1.210:6080/files/download/signedPdf/20180412/杜雨生_还款计划表_1523525924397.pdf",
        "riskNotificationPdfUrl": null
    }
}
```


## 157. 担保人签署合同
- url: `http://domain/guaranteeSign/sign`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
    - `code`: E签宝验证码
- 入参示例:
    http://domain/guaranteeSign/sign?applyNum=12345678901&code=548367
- 出参参数:
     - `contactSignPdfUrl`: 签署后合同pdf
     - `repaymentPlanSignPdfUrl`: 签署后还款计划表pdf
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "contactPdfUrl": "http://192.168.1.210:6080/files/download/signedPdf/20180412/杜雨生_合同_1523526216654.pdf",
        "repaymentPlanPdfUrl": "http://192.168.1.210:6080/files/download/signedPdf/20180412/杜雨生_还款计划表_1523526222171.pdf",
    }
}
```



## 158. 担保人发送E签宝短信验证
- url: `http://domain/onApplySign/sendMessage`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
- 入参示例:
    http://domain/onApplySign/sendMessage?applyNum=36234211
- 出参参数:
- 出参示例:

```javascript
{
    "status": "ERROR",
    "error": "短信发送失败，创建个人账户失败",
    "data": null
}
```

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```


## 159. 开设账号
- url: `http://domain/sysUsers/addSysUser`
- 请求方式: `POST`
- 入参位置: `Request Body`
- 入参参数:
    - `password`: 用户密码
    - `userName`: 要开设账号的上级用户名
    - `name`: 姓名
- 入参示例:
    http://domain/sysUsers/addSysUser 
```javascript
{
	"password":"123456"
	"userName":"SH000L001",
	"name":"杜雨生"
}
```

- 出参示例:
```javascript
{
    "status": "ERROR",
    "error": "创建个人账户失败",
    "data": null
}
```

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": "SH000L024"
}
```



## 160. 账号手机认证
- url: `http://domain/userInfo/authUserPhone`
- 请求方式: `POST`
- 入参位置: `Request Body`
- 入参参数:
    - `phoneNum`: 手机号
    - `msgCode`: 短信验证码
    - `userName`: 用户名
- 入参示例:
    http://domain/userInfo/authUserPhone
```javascript
{
	"phoneNum":"18055313782",
	"msgCode":"369520",
	"userName":"SH000L024"
}
```

- 出参示例:
```javascript
{
    "status": "ERROR",
    "error": "认证失败",
    "data": null
}
```

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```


## 161. 账号身份证认证
- url: `http://domain/userInfo/authUserIdCard`
- 请求方式: `POST`
- 入参位置: `Request Body`
- 入参参数:
    - `name`: 姓名
    - `idCard`: 身份证号
    - `userName`: 用户名
- 入参示例:
    http://domain/userInfo/authUserIdCard
```javascript
{
	"userName":"SH000L024",
	"name":"杜雨生",
	"idCard":"342425199404048"
}
```

- 出参示例:
```javascript
{
    "status": "ERROR",
    "error": "认证失败",
    "data": null
}
```

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```


## 162. 账号信息提交
- url: `http://domain/userInfo/addUserInfo`
- 请求方式: `POST`
- 入参位置: `Request Body`
- 入参参数:
    - `userName`: 用户名
    - `bankNum`: 用户卡号
    - `weChat`: 微信号
- 入参示例:
    http://domain/userInfo/addUserInfo
```javascript
{
	"userName":"SH000L024",
	"bankNum":"23123123123",
	"weChat":"平头的夏日"
}
```

- 出参示例:
```javascript
{
    "status": "ERROR",
    "error": "认证失败",
    "data": null
}
```

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```




## 163. 用户认证状态查询
- url: `http://domain/userInfo/getUserAuthState`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `userName`: 用户名
- 入参示例:
    http://domain/userInfo/getUserAuthState?userName=SH000L024
- 出参参数:
     - `phoneState`: 手机认证状态（0:未完成，1:null,"",已完成）
     - `idCardState`: 身份证认证状态（0:未完成，1:null,"",已完成）
     - `userInfoState`: 用户信息是否提交（0:未完成，1:null,"",已完成）
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "phoneState": "1",
        "idCardState": "1",
        "userInfoState": "0"
    }
}
```



## 164. 更新用户登录权限
- url: `http://domain/userInfo/updateLoginAuth`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
    - `userCode`: 要更新的人员用户名
    - `loginAuth`: 权限 (0:无权限, 1: 有权限)
- 入参示例:
    http://domain/userInfo/updateLoginAuth?userCode=SH000L025&loginAuth=1

- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```




## 165. 获取用户登录权限
- url: `http://domain/userInfo/getLoginAuth`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `userCode`: 用户名
- 入参示例:
    http://domain//userInfo/getLoginAuth?userCode=SH000L025
- 出参参数:
- 出参示例:("0": 无权限, "1": 有权限)

```javascript
{
    "status": "SUCCESS",
    "error": null,
    "data": "1"
}
```

## 166.订单列表
- url: `http://domain/orderBind/getOrderList`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `userName`: 用户名（不一定是当前登录的用户名，查询谁的信息就传谁的用户名）
    - `condition`: 检索条件
    - `beginTime`: 开始日期
    - `endTime`: 结束日期
- 入参示例:
    http://domain/orderBind/getOrderList?userName=SH000FGS1&condition=123&beginTime=20180401&endTime=20180416
- 出参参数：
  	- `userName`: 用户名
  	- `name`: 客户姓名
    - `applyNum`: 申请编号
    - `state`: 状态
    - `createTime`: 创建时间
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "userName": "SH000",
      "name": "渣渣辉",
      "applyNum": "88009900",
      "state": "待签署合同",
      "createTime": 20180411
    },
    ...
    {
          "userName": "SH000",
          "name": "古天乐",
          "applyNum": "88009900",
          "state": "待签署合同",
          "createTime": 20180404
    }
  ]
}
```

## 167.查询某个用户下级机构及订单数量
- url: `http://domain/orderBind/getOrderCount`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `userName`: 用户名（不一定是当前登录的用户名，查询谁的信息就传谁的用户名）
    - `beginTime`: 开始日期
    - `endTime`: 结束日期
- 入参示例:
    http://domain/orderBind/getOrderCount?userName=SH000FGS1&beginTime=20180401&endTime=20180416
- 出参参数：
  	- `userId`: 自己id
  	- `parentId`: 上级id
    - `userName`: 用户名
    - `xtczmc`: 状态
    - `applyCount`: 订单数量
    - `users`: 下级用户信息
       - `userId`: 自己id
       - `parentId`: 上级id
       - `userName`: 用户名
       - `xtczmc`: 状态
       - `applyCount`: 订单数量
       - `users`: 下级用户信息
        ...
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "userId": "1",
    "parentId": null,
    "userName": "SH000",
    "xtczmc": "安徽汽车销售公司",
    "applyCount": "5",
    "users": [
      {
        "userId": "2",
        "parentId": "1",
        "userName": "SH000_01",
        "xtczmc": "小明",
        "applyCount": "5",
        "users": [
          {
            "userId": "3",
            "parentId": "2",
            "userName": "SH000_01_01",
            "xtczmc": "小明1",
            "applyCount": "5",
            "users": [
              {
                "userId": "4",
                "parentId": "3",
                "userName": "SH000_01_01_01",
                "xtczmc": "小明11",
                "applyCount": "5",
                "users": [
                  {
                    "userId": "5",
                    "parentId": "4",
                    "userName": "SH000_01_01_01_01",
                    "xtczmc": "小明111",
                    "applyCount": "5",
                    "users": []
                  }
                ]
              }
            ]
          },
          {
            "userId": "6",
            "parentId": "2",
            "userName": "SH000_02",
            "xtczmc": "小王",
            "applyCount": "5",
            "users": [
              {
                "userId": "7",
                "parentId": "6",
                "userName": "SH000_02_01",
                "xtczmc": "小王1",
                "applyCount": "5",
                "users": []
              }
            ]
          }
        ]
      }
    ]
  }
}
```

## 168. 订单绑定
- url: `http://domain/orderBind/orderBind`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `name`: 客户姓名
    - `userName`: 分配者用户名
    - `applyNum`: 申请编号
    - `targetUser`: 被分配者用户名
- 入参示例:
    http://domain/orderBind/orderBind?name=张三&userName=SH000&targetUser=SH000_01&applyNum=12345678
- 出参参数:
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```

## 169.订单绑定历史列表
- url: `http://domain/orderBind/getOrderHistory`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `userName`: 用户名（不一定是当前登录的用户名，查询谁的信息就传谁的用户名）
    - `condition`: 模糊查询条件
- 入参示例:
    http://domain/orderBind/getOrderHistory?userName=SH000&condition=123
- 出参参数：
    - `name`: 张三
    - `targetUser`: 绑定到的用户
  	- `sourceUser`: 执行绑定人用户名
    - `applyNum`: 申请编号
    - `createTime`: 创建时间
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "name": 张三,
      "targetUser": "SH000_01",
      "sourceUser": "SH000",
      "applyNum": "88009900",
      "createTime": "20180416"
    }
  ]
}
```

## 170. 查询待请款列表
- url: `http://domain/requestPayment/getLocalInfo`
- 请求方式: `get`
- 入参位置: `Request Parameter`
- 入参参数:
    - `condition`: 检索条件(此参数传值即根据查询条件模糊匹配，不传值则查询所有数据)
    - `beginTime`: 查询开始时间 格式：20180101  非必传
    - `endTime`: 查询结束时间 格式：20180101  非必传
- 入参示例:
    http://domain/requestPayment/getLocalInfo?beginTime=20170101&endTime=20180430
- 出参参数:
     - `userName`: 订单所属人员
     - `name`: 申请人姓名
     - `applyNum`: 申请编号
     - `createTime`: 通过日期
     - `state`:请款状态（11种状态文本）
     - `applyResult`: 请款状态(请按照上述9中状态数值返回)
     - `applyResultReason`: 原因
     - `carType`: 车辆类型（1,2,3,4）
     - `signState`: 是否已签约("",null,0: 未上传，1:已上传)
     - `isReturn`: 是否为退回 ("",null,0: 非退回，1:退回单子)

     HPL新车  		1
     HPL二手车 		2
     工行产品新车  	3
     工行产品二手车  4

     
     待请款  51000
     退回，待请款  51001
     待文件审核  5100
     文件审核待修改  52000
     文件审核通过待保单上传  61000
     退回，待保单上传  61001
     待保单审核  6100
     保单审核退回 6200
     审核通过 63000
     文件审核拒绝 51100
     保单审核拒绝 61100
     

- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": [
        {
            "userName": "SH000",
            "applyNum": "37263515",
            "createTime": "20170905",
            "createUser": "SH000",
            "state": "5100"
            "name": "杜雨生",
            "applyResult": "文件审核待修改",
            "carType": "1",
            "signState":"1",
            "isReturn ":"0"
        },
        {
            "userName": "SH000",
            "applyNum": "37263515",
            "createTime": "20170905",
            "createUser": "SH000",
            "state": "5100"
            "name": "杜雨生",
            "applyResult": "文件审核待修改",
            "carType": "1",
            "signState":"1",
            "isReturn ":"0"
      
        }
    ]
}
```



## 171. 获取gps安装sn号
- url: `http://domain/requestPayment/getSn`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
- 入参示例:
    http://domain/requestPayment/getSn?applyNum=12345678
- 出参参数:

```javascript
{
    "status": "SUCCESS",
    "error": null,
    "data": "12345"
}
```


## 172. 获取请款完善状态
- url: `http://domain/requestPayment/getRequestPaymentState`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
- 入参示例:
    http://domain/requestPayment/getRequestPaymentState?applyNum=12345678
- 出参参数:
     - `gpsStatus`: gps激活信息  0,null,""：未激活, 1:已激活
     - `fileInfoState`: 文件信息是否完善  0,null,""：未完善, 1:已完善
     - `insuranceInfoState`:保单信息是否完善 0,null,""：未完善, 1:已完善

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "gpsStatus": "1",
        "insuranceInfoState": "",
        "fileInfoState": ""
    }
}
```


## 173. 提交GPS激活确认信息
- url: `http://domain/requestPayment/submitGpsInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
    - `sn`: sn号
    - `name`: 申请人姓名
- 入参示例:
    http://domain/requestPayment/submitGpsInfo?applyNum=12345678&sn=123456&name=杜雨生
- 入参示例:
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```


## 174. 保存请款文件信息
- url: `http://domain/requestPayment/saveFileInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
- 入参示例:
    http://domain/requestPayment/saveFileInfo
- 入参位置: `Request Body`
- 入参位置: Request Body
    - `applyNum`: 申请编号
    - `fileList`: 附加集合
    - `name`: 附件名
    - `urlList`: 附件图片集合
    - `required`: 是否必须（"true","false"）
- 入参示例:

```javascript
{
  "applyNum": "38157474",
  "fileList": [
	  {
			"name": "合格证",
			"urlList": null,
			"required": "true"
		},
		{
			"name": "还款卡扫描件",
			"required": "false",
			"urlList": ["http://222.73.56.22:89/approvalIdCard/20180208/eff1a9e2-9933-43be-9ade-c3719edb5eb3.jpg", "http://222.73.56.22:89/approvalIdCard/20180208/eff1a9e2-9933-43be-9ade-c3719edb5eb3.jpg"]
		},
		{
			"name": "机动车销售发票",
			"required": "false",
			"urlList": ["http://222.73.56.22:89/approvalIdCard/20180208/eff1a9e2-9933-43be-9ade-c3719edb5eb3.jpg", "http://222.73.56.22:89/approvalIdCard/20180208/eff1a9e2-9933-43be-9ade-c3719edb5eb3.jpg"]
		}
	]
}
```
- 出参参数:
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```


## 175. 获取请款文件信息
- url: `http://domain/requestPayment/getFileInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
- 入参示例:
    http://domain/requestPayment/getFileInfo?applyNum=38339296
- 出参参数:
    - `bankCardImg`: 银行卡照片
    - `invoice`: 发票照片
    - `invoiceQRCodeImg`: 发票二维码
    - `cardListImg`: 工行卡单
    - `cardListSignImg`: 工行卡单面签
    - `bankDetailedImg`: 工行细则
    - `bankDetailedSignImg`: 工行细则面签
    - `paymentAgreementImg`: 工行代扣协议
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "bankCardImg": "312416234234",
        "invoice": "20160909",
        "invoiceQRCodeImg": "yellow",
        "cardListImg": "507423123",
        "cardListSignImg": "501651231231",
        "bankDetailedImg": "12345678",
        "bankDetailedSignImg": "123",
        "paymentAgreementImg": "www.taobao.com"
    }
}
```


## 176. 保存请款保单信息
- url: `http://domain/requestPayment/saveInsurancePolicyInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
- 入参示例:
    http://domain/requestPayment/saveInsurancePolicyInfo
- 入参位置: Request Body
    - `applyNum`: 申请编号
    - `policyList`: 附加集合
    - `name`: 附件名
    - `urlList`: 附件图片集合
    - `required`: 是否必须（"true","false"）
- 入参示例:
```javascript
{
  "applyNum": "38157474",
  "policyList": [
	  {
			"name": "合格证",
			"urlList": null,
			"required": "false"
		},
		{
			"name": "还款卡扫描件",
			"required": "true",
			"urlList": ["http://222.73.56.22:89/approvalIdCard/20180208/eff1a9e2-9933-43be-9ade-c3719edb5eb3.jpg", "http://222.73.56.22:89/approvalIdCard/20180208/eff1a9e2-9933-43be-9ade-c3719edb5eb3.jpg"]
		},
		{
			"name": "机动车销售发票",
			"required": "true",
			"urlList": ["http://222.73.56.22:89/approvalIdCard/20180208/eff1a9e2-9933-43be-9ade-c3719edb5eb3.jpg", "http://222.73.56.22:89/approvalIdCard/20180208/eff1a9e2-9933-43be-9ade-c3719edb5eb3.jpg"]
		}
	]
}
```

- 出参参数:
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```

## 177.查询某个用户下级机构
- url: `http://domain/orderBind/getOrganization`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `userName`: 用户名
- 入参示例:
    http://domain/orderBind/getOrganization?userName=SH000FGS1
- 出参参数：
  	- `userId`: 自己id
  	- `parentId`: 上级id
    - `userName`: 用户名
    - `userLevel`: 账号等级（主账号、子账号）
    - `xtczmc`: 系统操作名称
    - `userLowers`: 下级用户信息
       - `userId`: 自己id
       - `parentId`: 上级id
       - `userName`: 用户名
       - `userLevel`: 账号等级（主账号、子账号）
       - `userLowers`: 下级用户信息
       - `xtczmc`: 系统操作名称
        ...
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "userId": "12334",
    "parentId": null,
    "userName": "SH000",
    "xtczmc": "测试账号",
    "userLevel": "主账号",
    "users": [
      {
        "userId": "12337",
        "parentId": "12334",
        "userName": "SH000FGS1",
        "userLevel": "子账号",
        "xtczmc": "测试账号",
        "users": [
          {
            "userId": "12425",
            "parentId": "12337",
            "userName": "sh000",
            "userLevel": "子账号",
            "xtczmc": "测试账号",
            "users": []
          },
          ...
          {
            "userId": "14250",
            "parentId": "12337",
            "userName": "sh000qyjl",
            "userLevel": "子账号",
            "xtczmc": "测试账号",
            "users": []
          }
        ]
      },
      ...
      {
        "userId": "13829",
        "parentId": "12334",
        "userName": "nbcs01",
        "userLevel": "子账号",
        "xtczmc": "测试账号",
        "users": [
          {
            "userId": "13830",
            "parentId": "13829",
            "userName": "xzh01",
            "userLevel": "子账号",
            "xtczmc": "测试账号",
            "users": []
          },
          ...
          {
            "userId": "14411",
            "parentId": "13829",
            "userName": "xq01",
            "userLevel": "子账号",
            "xtczmc": "测试账号",
            "users": [
              {
                "userId": "15149",
                "parentId": "14411",
                "userName": "SH000S035",
                "userLevel": "子账号",
                "xtczmc": "测试账号",
                "users": []
              }
            ]
          },
          ...
          {
            "userId": "14421",
            "parentId": "13829",
            "userName": "ljd01",
            "userLevel": "子账号",
            "xtczmc": "测试账号",
            "users": []
          }
        ]
      }
    ]
  }
}
```



## 178. 获取请款保单信息
- url: `http://domain/requestPayment/getInsurancePolicyInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
- 入参示例:
    http://domain/requestPayment/getInsurancePolicyInfo?applyNum=38339296
- 出参参数:
    - `commercialInsurance`: 商业险
    - `compulsoryInsurance`: 交强险
    - `registration`: 登记证
    - `drivingLicense`: 行驶证
     
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "commercialInsurance": "商业险",
        "compulsoryInsurance": "交强险",
        "registration": "登记证",
        "drivingLicense": "行驶证"
    }
}
```


## 179.查询补全银行卡信息
- url: `http://domain/onApplySign/getBankCardInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `applyNum`: 申请编号(必传)

- 入参示例:
    http://domain/onApplySign/getBankCardInfo?applyNum=38372837

- 出参参数:
  - `applyNum`: 申请编号
  - `bankCardNum`: 银行卡号
  - `name`: 开户行
  - `bank`: 银行名称
  - `bankcardImg`: 银行卡照片
  - `authState`: 认证状态 0,"",null :未认证  1：已认证
  
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data":{
       "applyNum":"38157694" ,
       "bankCardNum":"6261231283123123",
       "name":"duysuheng" ,
       "bank":"中国银行",
       "bankcardImg":"www.baidu.com",
       "authState":"0"
       }
}
```

## 180.添加补全银行卡信息
- url: `http://domain/onApplySign/submitBankCardInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参位置: Request Body
- 入参参数:
  - `applyNum`: 申请编号
  - `bankCardNum`: 银行卡号
  - `name`: 借记卡姓名
  - `bank`: 银行名称
  - `bankcardImg`: 银行卡照片
  - `phoneNum`: 手机号
- 入参示例:
    http://domain/onApplySign/submitBankCardInfo?applyNum=38372837
```javascript
{
"applyNum":"38157694" ,
"bankCardNum":"6261231283123123",
"name":"duysuheng" ,
"bank":"中国银行",
"bankcardImg":"www.baidu.com"
"phoneNum":"12345678901"
}
```


- 出参参数:
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```

## 181.GPS邀约列表
- url: `http://domain/gpsConvention/getGpsList`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `condition`: 检索条件
    - `beginTime`: 开始日期
    - `endTime`: 结束日期
- 入参示例:
    http://domain/gpsConvention/getGpsList?condition=123&beginTime=20180401&endTime=20180416
- 出参参数：
  	- `userName`: 用户名
  	- `name`: 客户姓名
    - `applyNum`: 申请编号
    - `createTime`: 创建时间
    - `state`: 邀约状态（（已邀约，未邀约("",null)）
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": [
        {
            "userName": "SH000",
            "name": "黎国欢",
            "applyNum": "38157854",
            "createTime": "20180428",
            "state": "未邀约"
        },
        {
            "userName": "SH000",
            "name": "周丹秋",
            "applyNum": "38158217",
            "createTime": "20180424",
            "state": "未邀约"
        }
    ]
}
```

## 182.获取省、市、县
- url: `http://domain/gpsConvention/getAreaList`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `type`: 获取类型 [“0”:所有省份] [“1”:所有当前省的所有城市] [“2”:所有当前市的所有县]
    - `id`: 查询参数 [“0”:空] [“1”:省id] [“2”:市id]
- 入参示例:
    http://domain/gpsConvention/getGpsList?type=1&id=18
- 出参参数：
  	- `id`: 地区id
  	- `name`: 地区名称
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "id": "1",
      "name": "合肥市"
    },
    {
      "id": "2",
      "name": "芜湖市"
    },
    ...
    {
      "id": "14",
      "name": "淮南市"
    }
  ]
}
```

## 183.获取GPS安装品牌、方式
- url: `http://domain/gpsConvention/getGpsInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
    - `installProvince`: 安装省份
    - `installCity`: 安装城市
- 入参示例:
    http://domain/gpsConvention/getGpsInfo?applyNum=38155694&installProvince=云南省&installCity=保山市
- 出参参数：
  	- `installBrand`: GPS安装品牌（数组）
  	- `installWay`: GPS安装方式（数组）
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "installBrand": [
      "鲁诺",
      "车晓"
    ],
    "installWay": [
      "鲁诺安装",
      "自行安装"
    ]
  }
}
```

## 184.GPS邀约信息提交
- url: `http://domain/gpsConvention/submitGpsInfo`
- 请求方式: `POST`
- 入参位置: `Request Body`
- 入参参数:
    - `applyNum`: 申请编号
    - `installBrand`: 安装品牌
    - `installWay`: 安装方式
    - `contactsName`: 联系人姓名
    - `contactsPhone`: 联系人电话
    - `installDate`: 安装日期（字符串，格式：20180401）
    - `installAddress`: 安装地点
    - `installProvince`: 安装省份
    - `installCity`: 安装城市
    - `installCounty`: 安装县
- 入参示例:
    http://domain/gpsConvention/submitGpsInfo

```javascript
    {
      "applyNum": "12345678",
      "installBrand": "鲁诺",
      "installWay": "自行安装",
      "contactsName": "张三",
      "contactsPhone": "18888998899",
      "installDate": "20180401",
      "installAddress": "新华国际广场",
      "installProvince": "安徽省",
      "installCity": "合肥市",
      "installCounty": "蜀山区"
    }
```
- 出参参数：
  	无
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

## 185.GPS邀约历史列表
- url: `http://domain/gpsConvention/getGpsHistory`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `condition`: 模糊查询条件
- 入参示例:
    http://domain/gpsConvention/getGpsHistory?condition=123
- 出参参数：
    - `applyNum`: 1234567
    - `contactsName`: 联系人
  	- `createTime`: 创建时间
    - `createUser`: 操作人
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": [
    {
      "applyNum": "00997766",
      "contactsName": "小王",
      "createTime": 20180416,
      "createUser": "SH000"
    },
    ...
    {
          "applyNum": "88998899",
          "contactsName": "小李",
          "createTime": 20180416,
          "createUser": "SH000"
        }
  ]
}
```




## 186. 提交请款信息
- url: `http://domain/requestPayment/submitRequestPaymentInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
    - `type`: 提交类型 0：文件信息， 1：保单信息
    - `name`: 申请人姓名
- 入参示例:
    http://domain/requestPayment/submitRequestPaymentInfo?applyNum=38339296&type=0&name=杜雨生
- 出参参数:
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```



## 187.请款历史记录查询接口
- url: `http://domain/requestPayment/getRequestPaymentHistory`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `condition`: 模糊查询条件(申请编号or姓名)
- 入参示例:
    http://domain/requestPayment/getRequestPaymentHistory?condition=123
- 出参参数：
     - `userName`: 订单所属人员
     - `name`: 申请人姓名
     - `applyNum`: 申请编号
     - `createTime`: 通过日期
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": [
        {
            "userName": "SH000",
            "applyNum": "37263515",
            "createTime": "20170905",
            "name": "杜雨生",
        },
        {
            "userName": "SH000",
            "applyNum": "37263515",
            "createTime": "20170905",
            "name": "杜雨生"
        }
    ]
}
```


## 188.四要素验证接口
- url: `http://domain/onApplySign/fourFactorVerification`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `applyNum`: 申请编号(必传)
- 入参位置: Request Body
- 入参参数:
  - `applyNum`: 申请编号
  - `phoneNum`: 手机号
  - `msgCode`: 短信验证码
- 入参示例:
    http://domain/onApplySign/fourFactorVerification
```javascript
{
"applyNum":"38157694" ,
"phoneNum":"18055313788",
"msgCode":"123453" 
}
```

- 出参参数:
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```


## 189.电子签约历史记录查询接口
- url: `http://domain/onApplySign/getSignHistory`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `condition`: 模糊查询条件(申请编号or姓名)
- 入参示例:
    http://domain/onApplySign/getSignHistory?condition=123
- 出参参数：
     - `createUser`: 创建人
     - `name`: 申请人姓名
     - `applyNum`: 申请编号
     - `createTime`: 通过日期
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": [
        {
            "userName": "SH000",
            "applyNum": "37263515",
            "createTime": "20170905",
            "name": "杜雨生",
        },
        {
            "userName": "SH000",
            "applyNum": "37263515",
            "createTime": "20170905",
            "name": "杜雨生"
        }
    ]
}
```


## 190. 查询电子签约补全信息完善状态
- url: `http://domain/onApplySign/getInfoStatus`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
- 入参示例:
    http://domain/onApplySign/getInfoStatus?applyNum=3782623
- 出参参数:
     - `applyNum`: 申请编号
     - `signCarInfoState`: 签约补全车辆信息： 0,null,""：未完善,1:已完善
     - `signBankCardInfoState`: 银行卡验证： 0,null,"":未验证, 1:已验证
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "applyNum": "38927121",
        "signCarInfoState": "1",
        "signBankCardInfoState": "0",
    }
}
```


## 191. 电子签约提交接口
- url: `http://domain/onApplySign/submitContract`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
- 入参示例:
    http://domain/onApplySign/submitContract?applyNum=36143945
- 出参参数:
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```


## 192. 生成合同
- url: `http://domain/onApplySign/createContract`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
    - `applyNum`: 申请编号
- 入参示例:
    http://domain/onApplySign/createContract?applyNum=36143945
- 出参参数:
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```

## 193. 查询用户功能列表
- url: `http://domain/userAuth/getFunctionAuth`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `userCode`: 用户代码
- 入参示例:
    http://domain/userAuth/getFunctionAuth?userCode=SH000L30
- 出参参数:
     - `functionId`: 功能id
     - `description`: 功能描述
     - `state`: 状态： 0,null,"":未验证, 1:已验证
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "sysUserFunctionDtoList": [
            {
                "functionId": 1,
                "description": "合同查询",
                "state": "1"
            },
            {
                "functionId": 2,
                "description": "销售查询",
                "state": "0"
            },
            {
                "functionId": 3,
                "description": "在线申请",
                "state": "1"
            },
            {
                "functionId": 4,
                "description": "计算器",
                "state": "1"
            },
            {
                "functionId": 5,
                "description": "资讯",
                "state": "1"
            },
            {
                "functionId": 6,
                "description": "征信拍照",
                "state": "1"
            },
            {
                "functionId": 7,
                "description": "二手车",
                "state": "1"
            },
            {
                "functionId": 8,
                "description": "订单绑定",
                "state": "1"
            },
            {
                "functionId": 9,
                "description": "GPS邀约",
                "state": "1"
            },
            {
                "functionId": 10,
                "description": "电子签约",
                "state": "1"
            },
            {
                "functionId": 11,
                "description": "请款",
                "state": "1"
            }
        ]
    }
}
```


## 194.更新用户功能权限接口
- url: `http://domain/userAuth/updateFunctionAuth?userCode=SH000L30`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `userCode`: 用户代码(必传)
- 入参位置: Request Body
- 入参参数:
 - `functionId`: 功能id
 - `description`: 功能描述
 - `state`: 状态： 0,null,"":未验证, 1:已验证
- 入参示例:
    http://domain/userAuth/updateFunctionAuth?userCode=SH000L30
    
```javascript
{
        "sysUserFunctionDtoList": [
            {
                "functionId": "1",
                "description": "合同查询",
                "state": "1"
            },
            {
                "functionId": "2",
                "description": "销售查询",
                "state": "0"
            },
            {
                "functionId": "3",
                "description": "在线申请",
                "state": "1"
            },
            {
                "functionId": "4",
                "description": "计算器",
                "state": "0"
            },
            {
                "functionId": "5",
                "description": "资讯",
                "state": "0"
            },
            {
                "functionId": "6",
                "description": "征信拍照",
                "state": "1"
            },
            {
                "functionId": "7",
                "description": "二手车",
                "state": "1"
            },
            {
                "functionId": "8",
                "description": "订单绑定",
                "state": "1"
            },
            {
                "functionId": "9",
                "description": "GPS邀约",
                "state": "1"
            },
            {
                "functionId": "10",
                "description": "电子签约",
                "state": "1"
            },
            {
                "functionId": "11",
                "description": "请款",
                "state": "1"
            }
        ]
    }
```

- 出参参数:
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```




## 195. 查询预审批信息完成状态
- url: `http://domain/approval/autoApplyState`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `uniqueMark`: 唯一标识
- 入参示例:
    http://domain/approval/autoApplyState?uniqueMark=a54d344fb3b24305879843cc2667872d
- 出参参数:
     - `idCardInfoState`: 身份证信息完成状态： 0,null,"":未完成, 1:已完成
     - `driveLicenceInfoState`: 驾驶证信息完成状态： 0,null,"":未完成, 1:已完成
     - `bankCardInfoState`: 银行卡信息完成状态： 0,null,"":未完成, 1:已完成
     - `otherInfoState`: 联系方式完成状态： 0,null,"":未完成, 1:已完成
     - `mateInfoState`: 配偶信息完成状态： 0,null,"":未完成, 1:已完成
     - `contactInfoState`:紧急联系人完成状态： 0,null,"":未完成, 1:已完成
     - `maritalStatusState`:婚姻状态： 0,null,"":未完成, 1:已完成
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "idCardInfoState": "1",
        "driveLicenceInfoState": null,
        "bankCardInfoState": null,
        "otherInfoState": null,
        "mateInfoState": null,
        "contactInfoState": null
        "maritalStatusState":null
    }
}
```



## 196. 查询在线沟通列表
- url: `http://domain/onlineCommunication/getOnlineCommunicationJK`
- 请求方式: `get`
- 入参位置: `Request Parameter`
- 入参参数:
    - `condition`: 模糊查询条件(申请编号or姓名)
- 入参示例:
    http://domain/onlineCommunication/getOnlineCommunicationJK?condition=杜雨生
- 出参参数:
     - `operator`: 操作人
     - `name`: 申请人姓名
     - `applyNum`: 申请编号
     - `operationTime`: 操作时间

- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": [
        {
            "name": "黎忠伟",
            "applyNum": "38159414",
            "operationTime": "20180511",
            "operator": "SH000"
        },
        {
            "name": "二手车企业挂靠",
            "applyNum": "38159294",
            "operationTime": "20180510",
            "operator": "SH000"
        },
        {
            "name": "",
            "applyNum": "38159014",
            "operationTime": "20180507",
            "operator": "SH000"
        },
        ...
        {
            "name": "程跃",
            "applyNum": "38159074",
            "operationTime": "20180507",
            "operator": "SH000"
        }
    ]
}
```


## 197. 查询在线沟通消息历史
- url: `http://domain/onlineCommunication/historyMessageQuery`
- 请求方式: `get`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参示例:
    http://domain/onlineCommunication/historyMessageQuery?applyNum=38158954
- 出参参数:
     - `operator`: 操作人
     - `text`: 消息内容
     - `operationTime`: 操作时间
     - `txUrl`: 头像

- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": [
        {
            "text": "38158954测试文字",
            "operationTime": "20180514113145",
            "operator": "SH000",
            "type": null,
            "url": null,
            "txUrl" null
        },
        {
            "text": "38158954测试文字2",
            "operationTime": "20180514151719",
            "operator": "SH000",
            "type": null,
            "url": null,
            "txUrl" "wqweq.jpg"
        }
    ]
}
```

## 198. 查询在线沟通附件历史
- url: `http://domain/onlineCommunication/historyAttachmentQuery`
- 请求方式: `get`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参示例:
    http://domain/onlineCommunication/historyAttachmentQuery?applyNum=38158954
- 出参参数:
     - `operator`: 操作人
     - `url`: 访问地址
     - `operationTime`: 操作时间
     - `type`: 附件类型
     - `txUrl`: 头像

- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": [
        {
            "text": null,
            "operationTime": "20180514113145",
            "operator": "SH000",
            "type": "身份证",
            "url": "http://wx.tftm.com/file/38158954.jpg",
            "txUrl":"http://wx.tftm.com/file/38158.jpg"
        },
        {
            "text": null,
            "operationTime": "20180514151719",
            "operator": "SH000",
            "type": "其他",
            "url": "访问地址"
        },
        {
            "text": null,
            "operationTime": "20180514151719",
            "operator": "SH000",
            "type": "身份证",
            "url": "http://wx.tftm.com/file/38158954.jpg"
        },
        {
            "text": null,
            "operationTime": "20180514113145",
            "operator": "SH000",
            "type": "其他",
            "url": "访问地址"
        }
    ]
}
```


# 199.在线沟通提交接口
- url: `http://domain/onlineCommunication/postOnlineCommunication`
- 请求方式: `POST`
- 入参位置: 
- 入参参数:
- 入参位置: Request Body
- 入参参数:
 - `loginUser`: 当前登录人员
 - `applyNum`: 申请编号
 - `text`: 在线沟通内容
 - `fileList`: 附加list
  - `type`: 附件类型
  - `url`: 附件访问地址
 
- 入参示例:
    http://domain/onlineCommunication/postOnlineCommunication
    
```javascript
{
		"loginUser":"SH000",
		"applyNum":"38158954",
		"text":"app:38158954测试文字1",
        "fileList": [
            {
                "type": "其他",
                "url": "访问地址"
            },
            {
                "type": "身份证",
                "url": "http://wx.tftm.com/file/38158954.jpg"
            }
        ]
}
```

- 出参参数:
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```

# 200. 查询请款所需文件附件列表
- url: `http://domain/requestPayment/getPleaseDocument`
- 请求方式: `get`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参示例:
    http://domain/requestPayment/getPleaseDocument?applyNum=38157474
- 出参参数:
     - `name`: 文件名
     - `required`: 附近是否必需
     - `uploadState`: 是否已上传("",null,0: 未上传，1:已上传)
     

- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": [
        {
            "name": "回租赁主合同",
            "required": "true",
            "uploadState": "1",
            "urlList": null
        },
        {
            "name": "征信查询授权委托书",
            "required": "false",
            "urlList": null
        },
        {
            "name": "还款卡扫描件",
            "required": "true",
            "urlList": [
                "http://222.73.56.22:89/approvalIdCard/20180208/eff1a9e2-9933-43be-9ade-c3719edb5eb3.jpg",
                "http://222.73.56.22:89/approvalIdCard/20180208/eff1a9e2-9933-43be-9ade-c3719edb5eb3.jpg"
            ]
        },
        {
            "name": "合格证",
            "required": "false",
            "urlList": null
        },
        {
            "name": "机动车销售发票",
            "required": "true",
            "urlList": [
                "http://222.73.56.22:89/approvalIdCard/20180208/eff1a9e2-9933-43be-9ade-c3719edb5eb3.jpg",
                "http://222.73.56.22:89/approvalIdCard/20180208/eff1a9e2-9933-43be-9ade-c3719edb5eb3.jpg"
            ]
        },
        {
            "name": "人身保障服务协议书",
            "required": "false",
            "urlList": null
        },
        {
            "name": "延保合同",
            "required": "false",
            "urlList": null
        }
    ]
}
```

# 201. 查询请款所需保单附件列表
- url: `http://domain/requestPayment/getPleaseKindOfPolicy`
- 请求方式: `get`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参示例:
    http://domain/requestPayment/getPleaseKindOfPolicy?applyNum=38157474
- 出参参数:
     - `name`: 文件名
     - `required`: 附近是否必需
     - `uploadState`: 是否已上传("",null,0: 未上传，1:已上传)

- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": [
        {
            "name": "车辆登记证",
            "required": "false",
             "uploadState": "1",
            "urlList": [
                "http://222.73.56.22:89/approvalIdCard/20180208/eff1a9e2-9933-43be-9ade-c3719edb5eb3.jpg",
                "http://222.73.56.22:89/approvalIdCard/20180208/eff1a9e2-9933-43be-9ade-c3719edb5eb3.jpg"
            ]
        },
        {
            "name": "行驶证复印件",
            "required": "false",
            "urlList": null
        },
        {
            "name": "购置税完税证明",
            "required": "false",
            "urlList": null
        },
        {
            "name": "车辆交接单",
            "required": "true",
            "urlList": null
        }
    ]
}
```




## 202.添加和修改婚姻状况
- url: `http://domain/approval/addMaritalStatus`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识(必传)
- 入参位置: Request Body
- 入参参数:
  - `maritalStatus`: 婚姻状态
- 入参示例:
    http://domain/approval/addMaritalStatus?uniqueMark=4de0351b53384a81b8153979142afbb7
```javascript
{
	"maritalStatus": "未婚"
}
```
- 出参参数:
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

# 203. 查询预审批婚姻状态
- url: `http://domain/approval/getMaritalStatus`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识uuid

- 入参示例:
    http://domain/approval/getMaritalStatus?uniqueMark=4de0351b53384a81b8153979142abb7

- 出参参数:
     - `maritalStatus`: 婚姻状态
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
    "maritalStatus": "未婚"
  }
}
```



# 204. 查询是否可以完善申请
- url: `http://domain/init/getApplyState`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:

- 入参示例:
    http://domain/approval/getMaritalStatus

- 出参参数:
     true or false
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": "false"
}
```



## 205. 查询预审批产品来源
- url: `http://domain/approval/getProductSource`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识

- 入参示例:
    http://domain/approval/getProductSource?uniqueMark=7a5ab9b5688d46d1a4fa9b961600383c

- 出参参数:

- 出参示例:
失败示例：

```javascript
{
    "status": "ERROR",
    "error": "请完善身份证信息",
    "data": null
}
```
成功示例：

```javascript
{
    "status": "SUCCESS",
    "error": null,
    "data": "在线助力融"
}
```

## 206.添加和修改微众银行卡信息
- url: `http://domain/approval/addWeBankInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识(必传)
- 入参位置: Request Body
- 入参参数:
  - `name`: 驾驶证姓名
  - `bank`: 开户行名称
  - `accountNum`: 借记卡账号
  - `bankImg`: 借记卡照片
  - `bankPhoneNum`: 借记卡预留手机号
  - `ip`: ip地址
  - `statement`: 纳税人声明
  - `monthlyIncome`: 月收入
  - `appId`: app应用id
  
- 入参示例:
    http://domain/approval/addWeBankInfo?uniqueMark=a54d344fb3b24305879843cc2667872d&msgCode=123456
```javascript
{
	"name": "杜雨生",
	"bank":"光大银行",
	"accountNum":"62282723897129837182273",
	"bankImg":"http://222.73.56.22:89/123.png",
	"bankPhoneNum":"18055313782",
    "ip":"光大银行",
    "statement":"1",
    "monthlyIncome":"R01",
    "appId":"wx1234543sds"
}
```
- 出参参数:
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```



## 207. 查询微众预审批结果
- url: `http://domain/approval/queryApplyState`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识

- 入参示例:
    http://domain/approval/queryApplyState?uniqueMark=7a5ab9b5688d46d1a4fa9b961600383c

- 出参参数:  
    返回预审批结果
- 出参示例:
失败示例：

```javascript
{
    "status": "ERROR",
    "error": "请完善身份证信息",
    "data": null
}
```
成功示例：

```javascript
{
    "status": "SUCCESS",
    "error": null,
    "data": "人工"
}
```


## 208.HPL电子签约提交
- url: `http://domain/onApplySign/hplSignSubmit`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识(必传)
- 入参位置: Request Body
- 入参参数:
  - `applyNum`: 申请编号
  - `phoneNum`: 手机号
  - `appId`: appId
  - `ip`: ip
  - `msgCode`: 短信验证码

  
- 入参示例:
    http://domain/onApplySign/hplSignSubmit
```javascript
{
	"applyNum": "38123121",
	"phoneNum":"18055313778",
	"appId":"62282723897129837182273",
	"ip":"192.168.1.210",
	"msgCode":"123123"
}
```


## 209.在线助力融电子签约提交
- url: `http://domain/onApplySign/weBankSignSubmit`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参位置: Request Body
- 入参参数:
  - `applyNum`: 申请编号
  - `phoneNum`: 手机号
  - `appId`: appId
  - `ip`: ip
  - `msgCode`: 短信验证码

  
- 入参示例:
    http://domain/onApplySign/weBankSignSubmit
```javascript
{
	"applyNum": "38123121",
	"phoneNum":"18055313778",
	"appId":"62282723897129837182273",
	"ip":"192.168.1.210",
	"msgCode":"123123"
}
```



## 210. 电子签约信息查询接口
- url: `http://domain/onApplySign/querySignInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `applyNum`: 申请编号

- 入参示例:
    http://domain/onApplySign/querySignInfo?applyNum=378223123

- 出参参数:
    - `name`: 借款人姓名
    - `idCardNum`: 借款人身份证号
    - `carName`: 车辆名称
    - `grantBank`: 贷款发放银行 微众银行
    - `applyNum`: 借记卡预留手机号
    - `phoneNum`: 手机号
    - `totalInvestment`: 融资总额(1)
    - `financeTerm`: 融资期限
    - `monthPay`: 月供
    - `financeAmount`: 车辆融资额(2.1)
    - `purchaseTax`: 购置税(3.2)
    - `retrofittingFee`: 加装费(3.2)
    - `extendedWarranty`: 延保(3.2)
    - `gps`: GPS硬件(3.2)
    - `compulsoryInsurance`: 交强险保费(3.2)
    - `commercialInsurance`: 商业险保费(3.2)
    - `vehicleTax`: 车船税(3.2)
    - `financeTerm`: 融资期限
    - `unexpected`: 意外保障(3.2)
    - `xfws`: 先锋卫士(3.2)
    - `otherFee`: 其他费用(3.2)
    - `creditFinanceAmount`: 信用融资额(2.2)
    - `rates`: 贷款年利率
    

- 出参示例:
失败示例：

```javascript
{
    "status": "ERROR",
    "error": "请完善身份证信息",
    "data": null
}
```
成功示例：

```javascript
{
    "status": "SUCCESS",
    "error": null,
    "data": {
              "totalInvestment":"100000",
              "applyNum":"38154354",
              "financeTerm":"24",
              "monthPay":"4200",
              "salePrice":"20000",
              "retrofittingFee":"101",
              "extendedWarranty":"102",
              "gps":"650",
              "compulsoryInsurance":"100,101",
              "commercialInsurance":"201,202",
              "vehicleTax":"202,203",
              "unexpected":"1231",
              "xfws":"9527"
            }
}
```


## 211. 查询在线助力融银行列表
- url: `http://domain/approval/getBankList`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参示例:
    http://domain/approval/getBankList

- 出参参数:  
    返回预审批结果
- 出参示例:
失败示例：

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": [
        {
            "bank": "中国邮政储蓄银行"
        },
        {
            "bank": "浦东发展银行"
        },
        {
            "bank": "中国工商银行"
        },
        {
            "bank": "中国银行"
        },
        {
            "bank": "交通银行"
        },
        {
            "bank": "广东发展银行"
        },
        {
            "bank": "中国银行"
        },
        {
            "bank": "中信银行"
        },
        {
            "bank": "中国建设银行"
        },
        {
            "bank": "平安银行"
        },
        {
            "bank": "兴业银行"
        },
        {
            "bank": "中国光大银行"
        }
    ]
}
```



## 212.四要素验证结果查询
- url: `http://domain/onApplySign/userVerificationResult`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参位置: Request Body
- 入参参数:
  - `bankCardNum`: 银行卡号
  - `name`: 借记卡姓名
  - `idCardNum`: 身份证号
  - `phoneNum`: 手机号
- 入参示例:
    http://domain/onApplySign/userVerificationResult
```javascript
{
        "name": "杜雨生",
        "bankCardNum": "123454321234",
        "phoneNum":"18055313782",
        "idCardNum":"342425199404085234"
  }
```


- 出参参数:
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```



## 213.银行卡四要素验证信息录入接口(通用)
- url: `http://domain/apply/submitBankCardInfo`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参位置: Request Body
- 入参参数:
  - `bankCardNum`: 银行卡号
  - `name`: 借记卡姓名
  - `bank`: 银行名称
  - `idCardNum`: 身份证号码
  - `phoneNum`: 手机号
- 入参示例:
    http://domain/apply/submitBankCardInfo
```javascript
{
        "name": "杜雨生",
        "bankCardNum": "123454321234",
        "phoneNum":"18055313782",
        "idCardNum":"342425199404085234"
  }
```


- 出参参数:
 - `uniqueMark`: 验证唯一标识（流水号，验证时用）
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": "738172837128371927317233"
}
```



## 214.四要素验证接口（通用）
- url: `http://domain/apply/fourFactorVerification`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参位置: Request Body
- 入参参数:
  - `uniqueMark`: 验证唯一标识
  - `phoneNum`: 手机号
  - `msgCode`: 短信验证码
- 入参示例:
    http://domain/apply/fourFactorVerification
```javascript
{
"uniqueMark":"738172837128371927317233" ,
"phoneNum":"18055313788",
"msgCode":"123453" 
}
```

- 出参参数:
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```



## 215.获取放款卡信息
- url: `http://domain/sysUsers/getVerificationInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
  - `bankCardNum`: 银行卡号
- 入参示例:
    http://domain/sysUsers/getVerificationInfo?bankCardNum=62312312312333333223


- 出参参数:
  - `contractNum`: 合同号
  - `name`: 姓名
  - `idCardNum`: 身份证号
  - `bankCardNum`: 银行卡号
  - `verificationCount`: 核实次数
  - `recAddr`: 收卡地址
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "contractNum": "65434333",
        "name": "张明才",
        "idCardNum": "440921197401086015",
        "bankCardNum": "62312312312333333223",
        "verificationCount": "5"
        "recAddr":"安徽"
    }
}
```

## 216.核实工行放款卡提交
- url: `http://domain/sysUsers/submitVerificationInfo`
- 请求方式: `POST`
- 入参位置: Request Body
- 入参参数:
  - `bankCardNum`: 银行卡号
  - `userName`: 操作人用户名
- 入参示例:
    http://domain/sysUsers/submitVerificationInfo
```javascript
{
        "userName": "SH000",
        "bankCardNum": "123454321234"
  }
```

- 出参参数:
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": null
}
```





## 217. 创建、修改预审批附件信息
- url: `http://domain/approval/addApprovalAttachment?uniqueMark=`
- 请求方式: `POST`
- 入参位置: Request Body
- 入参参数:
  - `fingerprintImage`: 按指纹照片（必传）
  - `handHoldLetterOfCredit`: 手持征信照片（必传）
  - `signImage`: 签约时照片（必传）
  - `mateFingerprintImage`: 配偶按指纹照片
  - `mateHandHoldLetterOfCredit`: 配偶手持征信照片
  - `mateFrontImg`: 配偶身份证正面照片
  - `mateSignImage`: 配偶签约时照片
  - `mateBehindImg`: 配偶身份证反面照片
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识(必传)

- 入参示例:
    http://domain/approval/addApprovalAttachment?uniqueMark=9bd9d5b4f4624ee6b5d505581897ca9c1
    
```javascript
 {
        "fingerprintImage": "http://222.73.56.22:89/insurancePolicyFile/20180711/70dbd145-304e-4a0e-abff-0ac497aeb434.jpg",
        "handHoldLetterOfCredit": "http://222.73.56.22:89/insurancePolicyFile/20180711/70dbd145-304e-4a0e-abff-0ac497aeb434.jpg",
        "signImage": "http://222.73.56.22:89/insurancePolicyFile/20180711/70dbd145-304e-4a0e-abff-0ac497aeb434.jpg",
        "mateFingerprintImage": "http://222.73.56.22:89/insurancePolicyFile/20180711/70dbd145-304e-4a0e-abff-0ac497aeb434.jpg",
        "mateHandHoldLetterOfCredit": "http://222.73.56.22:89/insurancePolicyFile/20180711/70dbd145-304e-4a0e-abff-0ac497aeb434.jpg",
        "mateSignImage": "http://222.73.56.22:89/insurancePolicyFile/20180711/70dbd145-304e-4a0e-abff-0ac497aeb434.jpg",
        "mateFrontImg": "http://222.73.56.22:89/insurancePolicyFile/20180711/70dbd145-304e-4a0e-abff-0ac497aeb434.jpg",
        "mateBehindImg": "http://222.73.56.22:89/insurancePolicyFile/20180711/70dbd145-304e-4a0e-abff-0ac497aeb434.jpg"
 }
```
- 出参参数:
  - `data`: 申请单唯一标识(uuid)
- 出参示例:

```javascript
{
  "status": "SUCCESS",
  "error": null,
  "data": "f39ae5f082824df5846b89af36046098"
}
```

## 218. 查询预审批附件信息
- url: `http://domain/approval/getApprovalAttachment`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `uniqueMark`: 唯一标识uuid

- 入参示例:
    http://domain/approval/getApprovalAttachment?uniqueMark=4de0351b53384a81b8153979142a1bb7

- 出参参数:
  - `fingerprintImage`: 按指纹照片（必传）
  - `handHoldLetterOfCredit`: 手持征信照片（必传）
  - `signImage`: 签约时照片（必传）
  - `mateFingerprintImage`: 配偶按指纹照片
  - `mateHandHoldLetterOfCredit`: 配偶手持征信照片
  - `mateFrontImg`: 配偶身份证正面照片
  - `mateSignImage`: 配偶签约时照片
  - `mateBehindImg`: 配偶身份证反面照片
  
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data":  {
        "fingerprintImage": "http://222.73.56.22:89/insurancePolicyFile/20180711/70dbd145-304e-4a0e-abff-0ac497aeb434.jpg",
        "handHoldLetterOfCredit": "http://222.73.56.22:89/insurancePolicyFile/20180711/70dbd145-304e-4a0e-abff-0ac497aeb434.jpg",
        "signImage": "http://222.73.56.22:89/insurancePolicyFile/20180711/70dbd145-304e-4a0e-abff-0ac497aeb434.jpg",
        "mateFingerprintImage": "http://222.73.56.22:89/insurancePolicyFile/20180711/70dbd145-304e-4a0e-abff-0ac497aeb434.jpg",
        "mateHandHoldLetterOfCredit": "http://222.73.56.22:89/insurancePolicyFile/20180711/70dbd145-304e-4a0e-abff-0ac497aeb434.jpg",
        "mateSignImage": "http://222.73.56.22:89/insurancePolicyFile/20180711/70dbd145-304e-4a0e-abff-0ac497aeb434.jpg",
        "mateFrontImg": "http://222.73.56.22:89/insurancePolicyFile/20180711/70dbd145-304e-4a0e-abff-0ac497aeb434.jpg",
        "mateBehindImg": "http://222.73.56.22:89/insurancePolicyFile/20180711/70dbd145-304e-4a0e-abff-0ac497aeb434.jpg"
    }
}
```



# 219. 查询补充工行附件列表
- url: `http://domain/approval/getICBCAttachmentBackInfo`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
     - `condition`: 姓名查询条件(申请编号、姓名) 非必传
     - `page`: 当前页数(起始页:1)（必传）
     - `size`: 每页数量(必传)
- 入参示例:
    http://domain/approval/getICBCAttachmentBackInfo
- 出参参数:

     - `totalElements`: 总数
     - `page`: 当前页数
     - `size`: 每页数量
     - `content`: 详细内容
     
     - `name`: 姓名
     - `applyNum`: 申请编号
     - `reason`: 原因
     - `status`: 申请状态
     - `uniqueMark`: 申请单唯一标识uuid
     - `createUser`: 创建人
     - `updateTime`: 更新时间
     
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "page": 1,
        "size": 20,
        "content": [
            {
                "name": "杜雨生",
                "applyNum": "38431828",
                "reason": "资料不齐全",
                "status": "1000",
                "uniqueMark": "63beac031ee2440fa5bad18b38421532",
                "createUser": "SH000",
                "updateTime": "2018-07-13"
            },
            {
                "name": "季鹏颖",
                "applyNum": "38472781",
                "reason": "资料不齐全",
                "status": "1000",
                "uniqueMark": "07fc92ce673643f99a0cab7c788648a31",
                "createUser": "SH000",
                "updateTime": "2018-07-13"
            }
        ],
        "totalElements": 2
    }
}
```



## 220.获取放款卡信息
- url: `http://domain/sysUsers/getExpressInfoByCardNO`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
  - `bankCardNum`: 银行卡号
- 入参示例:
    http://domain/sysUsers/getExpressInfoByCardNO?bankCardNum=1111

- 出参参数:
  - `COURIERNUMBER`: 快递单号
  - `CONSIGNOR`: 发货人姓名
  - `BANKCARDNUM`: 银行卡号
  - `RECEIVEADDRESSR`: 收货地址
  - `PHONENUM`: 收货人手机号
  - `CONSIGNEE`: 收件人姓名
  - `POSTTIME`: 发货时间
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "COURIERNUMBER": 11115888,
        "CONSIGNOR": "先锋太盟",
        "BANKCARDNUM": 1111,
        "CONSIGNEE": "刘尚熠",
        "RECEIVEADDRESSR": "上海市宝山区恒高路",
        "PHONENUM": "1364164665"
    }
}
```


## 221. 车300二手车估价
- url: `http://domain/usedCarAnalysis/getSpecifiedPriceAnalysis`
- 请求方式: `POST`
- 入参位置: Request Body
- 入参参数:
  - `vin`: 车架号
  - `modelId`: 车型id （通过接口222获取）
  - `regDate`: 车辆上牌日期 2018-08
  - `zone`: 城市id（接口223获取）
  - `color`: 颜色
  - `operator`: 操作业务人员姓名
  - `operatorPhoneNum`: 操作业务人员手机号
  - `mile`: 车辆行驶公里数
  - `modelName`: 车型名称
  - `surface`: 漆面状况（优良中差）
  - `workState`: 工况状况（优良中差）
  - `makeDate`: 车辆出厂日期 2018-08 可选
  - `interior`: 车内装饰（优良中差）
  - `transferTimes`: 过户次数 可选
  - `messageCode`: 短信验证码
  - `usedCarFileDto`: 附件信息
      - `drivingLicense`: 行驶证
      - `registrationPage12`: 登记证1,2页
      - `registrationPage34`: 登记证3,4页
      - `leftFront45`: 左前45度
      - `leftAAndLeftB`: 左A柱与左B柱
      - `leftC`: 左C柱
      - `rightRear45`: 右后45度
      - `rightAAndRightB`: 右A柱与右B柱
      - `rightC`: 右C柱
      - `dashboard`: 仪表盘
      - `seatBelt`: 安全带
      - `centerConsole`: 中控台
      - `trunk`: 后备箱
      - `trunkFloor`: 后备箱底板
      - `nameplate`: 铭牌
      - `engineCompartment`: 发动机舱
      - `vin`: 车架号
      - `remake`: 照片备注

- 入参示例:
    http://domain/usedCarAnalysis/getSpecifiedPriceAnalysis
    
```javascript
    {
        "vin": "车架号",
        "modelId": "82",
        "regDate": "2013-11-10",
        "zone": "11",
        "color": "黑色",
        "operator": "操作人",
        "operatorPhoneNum": "操作人手机",
        "mile":2.33,
        "modelName":"优",
        "surface": "优",
        "workState": "差",
        "transferTimes":5,
        "makeDate":"2008-08",
        "interior":"优",
        "messageCode": "123123",
        "usedCarFileDto":{
            "nameplate":"21",
            "registrationPage12":"22"
        }
    }
```
- 出参参数:
    - `c2b_price`: c2b价格
    - `b2b_price`: b2b价格
    - `b2c_price`: b2c价格（我们需要的）
    - `report_url`: 定价报表
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "c2b_price": "12.3",
        "b2b_price": "12.43",
        "b2c_price": "13.93",
        "report_url": "http://dingjia.ceshi.che300.com/partner/service/detailReport?reportId=154152&userName=xftm"
    }
}
```



## 222.根据车架号获取车型
- url: `http://domain/usedCarAnalysis/identifyModelByVIN`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
  - `vin`: 车架号
- 入参示例:
    http://domain/usedCarAnalysis/identifyModelByVIN?vin=JS3JB43V294106017

- 出参参数:
  - `model_id`: 车型id
  - `brand_id`: 品牌id
  - `series_id`: 车系id
  - `brand_name`: 品牌名称
  - `model_name`: 车型名称
  - `series_name`: 车系名称
  - `min_reg_year`: 最小上牌年份
  - `max_reg_year`: 最大上牌年份
  - `model_year`: 车型年款
  - `model_price`: 指导价
  - `ext_model_id`: 如果合作伙伴的车型在车300这边做了映射的话，该字段会返回合作伙伴的车型ID，该字段为0就表示没有映射上去；如果双方没有映射那么该字段就是车300的车型ID
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "model_id": "12350",
        "brand_id": "78",
        "series_id": "817",
        "brand_name": "铃木",
        "model_name": "2009款 吉姆尼(进口) 1.3 AT 时尚型",
        "series_name": "吉姆尼(进口)",
        "min_reg_year": "2009",
        "max_reg_year": "2014",
        "model_year": "2009",
        "model_price": "15.5",
        "ext_model_id": "12350"
    }
}
```



# 223.获取城市列表
- url: `http://domain/usedCarAnalysis/getAllCity`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
  - `vin`: 车架号
- 入参示例:
    http://domain/usedCarAnalysis/getAllCity

- 出参参数:
  - `prov_id`: 当前省份id
  - `city_id`: 当前城市id
  - `cityListResult`: 城市列表
            - `group`: 省份组
            - `dataRows`: 该省份下所有城市
                - `city_name`: 城市名称
                - `prov_name`: 城市id
                - `prov_id`: 省份id
                - `city_id`: 城市id
           
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "status": "1",
        "prov_id": "13",
        "city_id": "13",
        "cityListResult": [
            {
                "group": "山东",
                "dataRows": [
                    {
                        "city_name": "滨州",
                        "prov_id": "16",
                        "prov_name": "山东",
                        "city_id": "354"
                    },
                    {
                        "city_name": "德州",
                        "prov_id": "16",
                        "prov_name": "山东",
                        "city_id": "331"
                    },
                    {
                        "city_name": "东营",
                        "prov_id": "16",
                        "prov_name": "山东",
                        "city_id": "130"
                    },
                    {
                        "city_name": "菏泽",
                        "prov_id": "16",
                        "prov_name": "山东",
                        "city_id": "364"
                    },
                    {
                        "city_name": "济南",
                        "prov_id": "16",
                        "prov_name": "山东",
                        "city_id": "16"
                    },
                    {
                        "city_name": "济宁",
                        "prov_id": "16",
                        "prov_name": "山东",
                        "city_id": "212"
                    },
                    {
                        "city_name": "临沂",
                        "prov_id": "16",
                        "prov_name": "山东",
                        "city_id": "317"
                    },
                    {
                        "city_name": "聊城",
                        "prov_id": "16",
                        "prov_name": "山东",
                        "city_id": "344"
                    },
                    {
                        "city_name": "莱芜",
                        "prov_id": "16",
                        "prov_name": "山东",
                        "city_id": "300"
                    },
                    {
                        "city_name": "青岛",
                        "prov_id": "16",
                        "prov_name": "山东",
                        "city_id": "46"
                    },
                    {
                        "city_name": "日照",
                        "prov_id": "16",
                        "prov_name": "山东",
                        "city_id": "283"
                    },
                    {
                        "city_name": "泰安",
                        "prov_id": "16",
                        "prov_name": "山东",
                        "city_id": "238"
                    },
                    {
                        "city_name": "潍坊",
                        "prov_id": "16",
                        "prov_name": "山东",
                        "city_id": "185"
                    },
                    {
                        "city_name": "威海",
                        "prov_id": "16",
                        "prov_name": "山东",
                        "city_id": "261"
                    },
                    {
                        "city_name": "烟台",
                        "prov_id": "16",
                        "prov_name": "山东",
                        "city_id": "158"
                    },
                    {
                        "city_name": "淄博",
                        "prov_id": "16",
                        "prov_name": "山东",
                        "city_id": "74"
                    },
                    {
                        "city_name": "枣庄",
                        "prov_id": "16",
                        "prov_name": "山东",
                        "city_id": "102"
                    }
                ]
            },
            ...
            {
                "group": "河北",
                "dataRows": [
                    {
                        "city_name": "保定",
                        "prov_id": "5",
                        "prov_name": "河北",
                        "city_id": "147"
                    },
                    {
                        "city_name": "承德",
                        "prov_id": "5",
                        "prov_name": "河北",
                        "city_id": "201"
                    },
                    {
                        "city_name": "沧州",
                        "prov_id": "5",
                        "prov_name": "河北",
                        "city_id": "227"
                    },
                    {
                        "city_name": "邯郸",
                        "prov_id": "5",
                        "prov_name": "河北",
                        "city_id": "91"
                    },
                    {
                        "city_name": "衡水",
                        "prov_id": "5",
                        "prov_name": "河北",
                        "city_id": "274"
                    },
                    {
                        "city_name": "廊坊",
                        "prov_id": "5",
                        "prov_name": "河北",
                        "city_id": "252"
                    },
                    {
                        "city_name": "秦皇岛",
                        "prov_id": "5",
                        "prov_name": "河北",
                        "city_id": "63"
                    },
                    {
                        "city_name": "石家庄",
                        "prov_id": "5",
                        "prov_name": "河北",
                        "city_id": "5"
                    },
                    {
                        "city_name": "唐山",
                        "prov_id": "5",
                        "prov_name": "河北",
                        "city_id": "35"
                    },
                    {
                        "city_name": "邢台",
                        "prov_id": "5",
                        "prov_name": "河北",
                        "city_id": "119"
                    },
                    {
                        "city_name": "张家口",
                        "prov_id": "5",
                        "prov_name": "河北",
                        "city_id": "174"
                    }
                ]
            }
        ]
    }
}
```

## 224.获取放款卡列表信息
- url: `http://domain/sysUsers/listCardStatusByCondition`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `queryType`: 车型类型（查询全部(无需传)
                    1：待确认列表
                    2：待邮寄列表
                   3：待收卡列表
                   4：被退回列表
                   5：已完成列表）   非必传
    - `startTime`: 开始时间 YYYYMMDD  非必传  
    - `endTime`: 结束时间 YYYYMMDD  非必传 
    - `verificationCount`: 核实次数  非必传
    - `condition`: 姓名/申请编号 非必传
    - `pageIndex`: 当前页 非必传
    - `pageSize`: 每页数量  非必传
- 入参示例:
    http://domain/sysUsers/listCardStatusByCondition?queryType=2

- 出参参数:
  - `RN`: 行数
  - `CS_ID`: 主键
  - `CS_CARD_NO`: 卡号
  - `CS_CONTRACT_NO`: 合同号
  - `CS_APPLY_NO`: 申请编号
  - `CS_STS_DESC`: 当前状态
  - `CI_NAME`: 姓名
  - `CI_COUNT`: 核卡次数
  - `CI_CREATE_TM`: 核卡时间
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": [
        {
            "RN": 1,
            "CS_ID": 320,
            "CS_FLOW_ID": 1,
            "CS_CARD_NO": 4392258382172478,
            "CS_CONTRACT_NO": 67398747,
            "CS_APPLY_NO": 38453349,
            "CS_STS_CODE": 2,
            "CS_STS_DESC": "待邮寄信用卡",
            "CS_OPR": "liaoxia",
            "CS_CREATE_TM": 1530843904000,
            "CI_ID": 201,
            "CI_CARD_NO": "4392258382172478",
            "CI_CONTRACT_NO": 67398747,
            "CI_APPLY_NO": 38453349,
            "CI_NAME": "刘尚熠",
            "CI_RECEIVER": "刘尚熠",
            "CI_OPR": "liaoxia",
            "CI_OPR_ID": 5301,
            "CI_OPR_NAME": "张恒",
            "CI_CREATE_TM": 1530626898842,
            "CI_ID_CARD": "310110198207264431",
            "CI_COUNT": 4,
            "CI_UPDATE_TM": 1530626898842
        }
    ]
}
```



## 225.获取放款卡快递详细信息
- url: `http://domain/sysUsers/getExpress`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `courierNumber`: 快递单号 非必传
- 入参示例:
    http://domain/sysUsers/getExpress?courierNumber=544176625708

- 出参参数:
  - `accept_address`: 快递地点
  - `accept_time`: 时间
  - `remark`: 备注
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": [
        {
            "accept_address": "上海市",
            "accept_time": "2018-07-13 19:08:41",
            "remark": "顺丰速运 已收取快件"
        },
        {
            "accept_address": "上海市",
            "accept_time": "2018-07-13 19:21:16",
            "remark": "顺丰速运 已收取快件"
        },
        {
            "accept_address": "上海市",
            "accept_time": "2018-07-13 20:14:42",
            "remark": "快件在【上海虹口通州营业部】已装车,准备发往下一站"
        },
        {
            "accept_address": "上海市",
            "accept_time": "2018-07-13 20:19:16",
            "remark": "快件已发车"
        },
        {
            "accept_address": "上海市",
            "accept_time": "2018-07-13 21:31:01",
            "remark": "快件到达 【上海浦江集散中心】"
        },
        {
            "accept_address": "上海市",
            "accept_time": "2018-07-13 22:52:25",
            "remark": "快件在【上海浦江集散中心】已装车,准备发往 【南昌昌北集散中心2】"
        },
        {
            "accept_address": "上海市",
            "accept_time": "2018-07-14 00:56:46",
            "remark": "快件已发车"
        },
        {
            "accept_address": "南昌市",
            "accept_time": "2018-07-14 12:03:33",
            "remark": "快件到达 【南昌昌北集散中心2】"
        },
        {
            "accept_address": "南昌市",
            "accept_time": "2018-07-14 13:26:09",
            "remark": "快件在【南昌昌北集散中心2】已装车,准备发往下一站"
        },
        {
            "accept_address": "南昌市",
            "accept_time": "2018-07-14 13:55:55",
            "remark": "快件已发车"
        },
        {
            "accept_address": "南昌市",
            "accept_time": "2018-07-14 15:55:30",
            "remark": "快件到达 【小兰工业园富山一路速运营业点】"
        },
        {
            "accept_address": "南昌市",
            "accept_time": "2018-07-14 16:07:41",
            "remark": "正在派送途中,请您准备签收(派件人:何伟,电话:15797640437)"
        },
        {
            "accept_address": "南昌市",
            "accept_time": "2018-07-14 16:29:36",
            "remark": "快件交给李杰，正在派送途中（联系电话：15279131851）"
        },
        {
            "accept_address": "南昌市",
            "accept_time": "2018-07-14 16:40:09",
            "remark": "快件派送不成功(因休息日或假期客户不便收件),待工作日再次派送"
        },
        {
            "accept_address": "南昌市",
            "accept_time": "2018-07-14 17:02:11",
            "remark": "因休息日或假期客户不便收件,待工作日派送"
        },
        {
            "accept_address": "南昌市",
            "accept_time": "2018-07-16 08:31:47",
            "remark": "正在派送途中,请您准备签收(派件人:李杰,电话:15279131851)"
        },
        {
            "accept_address": "南昌市",
            "accept_time": "2018-07-16 08:36:08",
            "remark": "快件交给李杰，正在派送途中（联系电话：15279131851）"
        },
        {
            "accept_address": "南昌市",
            "accept_time": "2018-07-16 09:23:24",
            "remark": "已签收,感谢使用顺丰,期待再次为您服务"
        },
        {
            "accept_address": "南昌市",
            "accept_time": "2018-07-16 09:24:12",
            "remark": "在官网\"运单资料&签收图\",可查看签收人信息"
        }
    ]
}
```



## 226.获取是否有权限查看二手车功能
- url: `http://domain/userAuth/getUsedCarAnalysis1Power`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `courierNumber`: 快递单号 非必传
- 入参示例:
    http://domain/sysUsers/userAuth/getUsedCarAnalysis1Power

- 出参参数:
  - `data`: 1;可以使用， 0,"",null:不可以使用
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": null,
    "data": "1"
}
```


## 227.行驶证识别获取车架号
- url: `http://domain/usedCarAnalysis/identifyDriverCard`
- 请求方式: `POST`
- 入参位置: `Request Body`
- 入参参数:
  - `driverCardBase64`: 行驶证图片Base64
- 入参示例:
    http://domain/usedCarAnalysis/identifyDriverCard
    
```javascript
{
    "driverCardBase64":"/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAAMCAgMCAgMDAwMEAwMEBQgFBQQEBQoHBwYIDAoMDAsK
                  CwsNDhIQDQ4RDgsLEBYQERMUFRUVDA8XGBYUGBIUFRT/2wBDAQMEBAUEBQkFBQkUDQsNFBQUFBQU
                  bbAAoDjbb2w6HQQ3NgMFBQAu+xw+CQL+/OC0J7B2Pxt7YKFyFpF9v3wqECx4tfe2DtYAtsD2wluN
                  Owtgq+ABbU1sO2tgDHziP1ABG+/7YkmkADa+/GE2Ae9rm/5YOoAwBbe+C7YA5A3vvge7AB5uRxgS"
}
```

- 出参参数:
  - `engine_num`: 发动机号
  - `plate_num`: 车牌号码
  - `vin`: 车架号
  - `model`: 厂牌型号
  - `register_date`: 注册日期
- 出参示例:

```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "engine_num": "416098",
        "plate_num": "沪A0M084",
        "vin": "LSVFF66R8C2116280",
        "model": "大众汽车牌SVW71611KS",
        "register_date": "20121127"
    }
}
```



# 228. 查询二手车评估列表
- url: `http://domain/usedCarAnalysis/findUsedCarAnalysisRecord`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
     - `page`: 当前页数(起始页:1)（必传）
     - `size`: 每页数量(必传)
     - `condition`: 查询条件(品牌) 非必传
     
     
- 入参示例:
    http://domain/usedCarAnalysis/findUsedCarAnalysisRecord?size=5&page=1&condition=
- 出参参数:

     - `totalElements`: 总数
     - `page`: 当前页数
     - `size`: 每页数量
     - `content`: 详细内容
     
     - `create_user`: 评估用户名
     - `id`: 订单号
     - `status`: 评估状态（0,"",null：失败，1：成功）
     - `update_time`: 评估时间
     - `model_name`: 车型名称
     - `report_url`: 报告url（状态为1 时显示报告url）
     - `error_msg`: 失败原因（状态为0时显示原因）
     
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": "",
    "data": {
        "page": 1,
        "size": 2,
        "content": [
            {
                "create_user": "SH000",
                "id": "4028e38164f9b6590164f9b75e690000",
                "status": "1",
                "update_time": "2018-08-02",
                "model_name": "优",
                "report_url": "http://dingjia.ceshi.che300.com/partner/service/detailReport?reportId=154201&userName=xftm",
                "error_msg": ""
            },
            {
                "create_user": "SH000",
                "id": "4028e38164ea1c6f0164ea8610380003",
                "status": "0",
                "update_time": "2018-07-30",
                "model_name": "优",
                "report_url": "http://dingjia.ceshi.che300.com/partner/service/detailReport?reportId=154180&userName=xftm",
                "error_msg": ""
            }
        ],
        "totalElements": 13
    }
}
```



# 229. 查询二手车评估附件是否必需
- url: `http://domain/usedCarAnalysis/getAnalysisFileIsMust`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
     - `page`: 当前页数(起始页:1)（必传）
- 入参示例:
    http://domain/usedCarAnalysis/getAnalysisFileIsMust
- 出参参数:

     - `data`: false:不需要,true必需
     
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": null,
    "data": "false"
}
```



# 230. 查询是否需要视频面签
- url: `http://domain/videoSign/iSvisaInterview`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
     - `applyNum`: 申请编号
- 入参示例:
    http://domain/videoSign/iSvisaInterview?applyNum=37822232
- 出参参数:

     - `sfxymq`:  是否需要视频面签 1 不需要面签；2 需要面签； 3 已经做过面签
     - `signState`:  签约状态 0 通过;1不通过（只有sfxymq为3才需要返回）
     
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": null,
    "data": {
        "sfxymq": "1",
        "signState": ""
    }
}
```




# 231. 工行面签报告提交
- url: `http://domain/videoSign/submitVisaInterview`
- 请求方式: `POST`
- 入参位置: `Request Body`
- 入参参数:
     - `applyNum`: 申请编号
     - `signNum`: 惠融编号
     - `longitude`: 经度 （非必传）
     - `latitude`: 纬度（非必传）
     
- 入参示例:
    http://domain/videoSign/submitVisaInterview
    {
      "applyNum": "34252672",
      "signNum": "1232",
      "longitude": "117.23",
      "latitude": "23.333"
    }
- 出参参数:


- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": null,
    "data": ""
}
```


# 232. 面签数量加一
- url: `http://domain/videoSign/addCount`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
     - `applyNum`: 申请编号
     - `signAccount`: 汇融面签账号
- 入参示例:
    http://domain/videoSign/addCount?applyNum=23233333&signAccount=1805531382
- 出参参数:

- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": null,
    "data": ""
}
```




# 233. 面签数量减一
- url: `http://domain/videoSign/subtractionCount`
- 请求方式: `POST`
- 入参位置: `Request Parameter`
- 入参参数:
     - `applyNum`: 申请编号
     - `signAccount`: 汇融面签账号
- 入参示例:
    http://domain/videoSign/subtractionCount?applyNum=23233333&signAccount=1805531382
- 出参参数:

- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": null,
    "data": ""
}
```



# 234. 查询签约排队数量
- url: `http://domain/videoSign/getSignCount`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参示例:
    http://domain/videoSign/getSignCount
- 出参参数:

     - `data`:  排队数量
     
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": null,
    "data": "2"
}
```


# 235. 查询签约账号信息
- url: `http://domain/videoSign/getSignAccount`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- 入参示例:
    http://domain/videoSign/getSignAccount
- 出参参数:
     - `signAccount`:  签约账号
     - `password`:  密码
     - `systemUserName`:  系统用户名
     
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": null,
    "data": {
        "id": 1,
        "signAccount": "12300000001",
        "password": "8888888",
        "systemUserName": "SH000",
        "createTime": null,
        "createUser": null,
        "updateTime": null,
        "updateUser": null
    }
}
```


# 236. 查询预审批申请中的状态
- url: `http://domain/approval/getApprovalSubmitState`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
    - `uniqueMark`:  唯一标识
- 入参示例:
    http://domain/approval/getApprovalSubmitState?uniqueMark=db456036688d4433b373f6aa57fda03a
- 出参参数:
     - `name`:  客户姓名
     - `productSource`:  产品名称
     - `state`:  状态
     - `operation`:  操作
     - `code`:  0:错误类(红色) 1:通过（绿色） 2：普通（黑色）
     
- 出参示例:
```javascript
{
    "status": "SUCCESS",
    "error": null,
    "data": {
        "applicantList": [
            {
                "name": "季鹏颖",
                "productSource": "天启",
                "state": "等待审核",
                "operation": "",
                "code": 2
            },
            {
                "name": "季鹏颖",
                "productSource": "工行",
                "state": "退回补充材料",
                "operation": "补充材料",
                "code": 0
            }
        ],
        "mateList": []
    }
}
```


## 237. 发送微众一审页面链接
- url: `http://domain/sysUsers/sendWeBankApply`
- 请求方式：`GET`
  - 入参参数：
  	- `phoneNum`: 手机号
  	- `name`: 姓名
  	- `uniqueMark`: 唯一标识

   - 入参示例：
   	http://domain/sysUsers/sendWeBankApply?phoneNum=18055313782&name=杜雨生&uniqueMark=123455

  - 出参示例：

```javascript
  {
      "status": "SUCCESS",
      "error": "",
      "data": null
  }
```
