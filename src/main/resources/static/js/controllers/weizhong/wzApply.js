app.controller('wzApplyController',[ 'toaster','$scope', '$state', '$http', '$modal', '$window', '$location','$localStorage','$cookies', function(toaster,$scope,$state,$http,$modal,$window,$location,$localStorage,$cookies ) {
        $scope.toaster = {
            type: 'success',
            title: 'Title',
            text: 'Message'
        };
        $scope.pop = function(type,title,text){
            toaster.pop(type,'',text);
        };
        $scope.applyCode = $location.search().applyCode;
        var uniqueMark = $location.search().uniqueMark;

    $scope.showMsg = '获取验证码';
    $scope.user = {};
    $scope.statement = '1';
    $scope.monthlyIncome = 'R02';
    $scope.bank = {};
    $scope.bankNum = '';
    $scope.device = {};
    $scope.form = {protocol: {
        a:false,b:false,c:true
    }};
//    var pageData = window.localStorage;
//    var dataString = pageData.getItem("wzUserData");
//    $scope.user = JSON.parse(dataString);
//    if($scope.user != null){
//        $scope.statement = $scope.user.statement;
//        $scope.monthlyIncome = $scope.user.monthlyIncome;
//        $scope.bank.bank = $scope.user.bank;
//        $scope.bankNum = $scope.user.bankNum;
//        $scope.bank = {name : $scope.bank.bank, bankNum : $scope.bankNum};
//        $scope.appId = $scope.user.appId;
//     //APP推送过来的单子查询四要素
//    } else if(uniqueMark){
//        $scope.user = {};
        userInfoSearch();
//    }


        function userInfoSearch(){
            var url = '/apply/searchUserBasicInfo?uniqueMark=' + uniqueMark;
            $http.get(url).success(function(data){
                if(data.status == 'SUCCESS'){
                    $scope.userBasicInfo = data.data;
                    $scope.user.name = $scope.userBasicInfo.name;
                    $scope.user.idCard = $scope.userBasicInfo.idCard;
                    $scope.user.bankCardNum = $scope.userBasicInfo.bankCardNum;
                    $scope.user.bank = $scope.userBasicInfo.bank;
                    $scope.user.phoneNum = $scope.userBasicInfo.phoneNum;
                    $scope.user.uniqueMark = uniqueMark;
                    $scope.user.appId = $scope.userBasicInfo.appId;
                    $scope.monthlyIncome = $scope.userBasicInfo.monthlyIncome;
                    $scope.user.fpName = $scope.userBasicInfo.fpName;
                    $scope.getBank();
                }else{
                    alert(data.error);
                    return;
                }
            }).error(function(){
                    alert('系统错误，请稍后再试');
                    return;
            });
        }
    $scope.getBank = function(){
        //清空上次填入的信息;
        $scope.bank = '';
        if(!$scope.user.bankCardNum){
            alert('请输入正确的银行卡号');
            return;
        }
        $http.get('/apply/getBank?bankCardNum=' + $scope.user.bankCardNum).success(function(data){
            if(data.status == 'SUCCESS'){
                $scope.bank = data.data;
            }else{
                alert(data.error);
                return;
            }
        }).error(function(){
              alert('获取银行信息错误，请稍后再试');
              return;
       });
    }




    function checkbox(){
        if($scope.form.protocol){
            var log = [];
            angular.forEach($scope.form.protocol,function(v){
                    if(v==true)
                        this.push(v);
            },log);
            if(log.length < 3){
                alert('请仔细阅读，并同意所有协议');
                return false;
            }else {
                return true;
            }
        }else{
            alert('请仔细阅读，并同意所有协议');
            return false;
        }
    }

    //申请提交
    $scope.submit = function(){
        if(!checkbox()){
            return;
        }
        //check协议是否都勾选
        if($scope.statement == null || $scope.statement == undefined){
            alert('请选择人纳税声明');
            return;
        }
        if($scope.user.code == null || $scope.user.code == ''){
            alert('请填写验证码');
            return;
        }
//        if(code  == ''){
//            alert('请获取验证码');
//            return;
//        }
        $http.post('/apply/checkMsgCode?phoneNum=' + $scope.user.phoneNum + '&code=' + $scope.user.code).success(function(data){
            if(data.status == 'SUCCESS'){
                var modalInstance = $modal.open({backdrop : 'static',size:'lg',
                    templateUrl: 'tpl/weizhong/wx_applyFinance.html',
                    controller: 'wxApplyFinanceController',
                    size: 'small'
                });
                modalInstance.result.then(function (item) {
                    if(item == '1'){
                        try {
                            //ip地址,引用搜狐js
                            var cip = returnCitySN["cip"];
                        }catch(err) {
                        }
                        $scope.user.ip = cip;
                        $scope.user.bank = $scope.bank.name;
                        $scope.user.bankNum = $scope.bank.bankNum;
                        $scope.user.statement = $scope.statement;
                        $scope.user.monthlyIncome = $scope.monthlyIncome;
                        $scope.user.notShow = true;
                        $scope.$emit("BUSY");
                        $http.post('/apply/weBankSubmit', $scope.user).success(function(result){
                            $scope.user.notShow = false;
                            $scope.$emit("NOTBUSY");
                            if(result.status == 'SUCCESS'){
                                alert("您的申请已提交，请耐心等待审核!");
                                $scope.user = {};
                            }else{
                                alert("申请失败！" + result.error);
                            }
                        }).error(function(){
                            alert('提交失败，请稍后再试');
                            $scope.user.notShow = false;
                            $scope.$emit("NOTBUSY");
                        });
                    }
                },function(){
                });
            }else{
                alert(data.error);
                return;
            }
        }).error(function(){
          alert('短信校验失败，请稍后再试');
        });
    };

    $scope.openStatement = function(){
        var modalInstance = $modal.open({
            templateUrl: 'tpl/weizhong/wx_statement.html',
            controller: 'wxStatementController',
            size: 'small'
        });
        modalInstance.result.then(function (item) {
        },function(){
        });
    }
    $scope.saveData = function(type){
//            var data = window.localStorage;
//            if($scope.user == null){
//                $scope.user = {};
//            }
//             $scope.user.bank = $scope.bank.name;
//             $scope.user.bankNum = $scope.bank.bankNum;
//             $scope.user.protocol = $scope.form.protocol;
//             $scope.user.statement = $scope.statement;
//             $scope.user.monthlyIncome = $scope.monthlyIncome;
//             $scope.user.appId = $scope.appId;
//            var d=JSON.stringify($scope.user);
//            data.setItem("wzUserData",d);
//            console.log(data.wzUserData);
//            var json=data.getItem("wzUserData");
//            var jsonObj=JSON.parse(json);
//            console.log(typeof jsonObj);
            var url = '';
            if(type == 'b'){
                url = "http://fwh.xftm.com/tpl/weizhong/contract1v31.html"
            }else if(type == 'a'){
                url = "http://fwh.xftm.com/tpl/weizhong/contract2v31.html"
            }else if(type == 'd'){
                url = "http://fwh.xftm.com/tpl/weizhong/contract3v41.html"
            }
            $window.location.href = url;
    };

    $scope.getSmsCode = function(){
        if(!checkbox()){
            return;
        }
        if($scope.startCountdown){
            return;
        }
        if($scope.statement == null || $scope.statement == undefined){
            alert('请选择人纳税声明');
            return;
        }
        var phoneNum = $scope.user.phoneNum;
        if(phoneNum == null || phoneNum == ''){
            alert('未填写手机号码');
            return;
         }
        $http.post('/sysUsers/sendRandomCode?phoneNum=' + phoneNum).success(function(data){
            if(data.status == 'SUCCESS'){
                $scope.$broadcast('timer-start');//开始倒计时
                $scope.showMsg = 's 后重新获取';
                $scope.startCountdown = true;
            }else{
                alert(data.error);
            }
        }).error(function(){
                alert('发送短信验证码失败，请稍后再试');
        });
    };

    $scope.$on('timer-stopped', function (event, data){
        $scope.startCountdown = false;
        $scope.showMsg = "获取验证码";
        $scope.$digest();//通知视图模型的变化
    });
}])
;
