app.controller('wxFactorVerificationController',[ 'toaster','$scope', '$state', '$http', '$modal', '$window', '$location','$localStorage','$cookies', function(toaster,$scope,$state,$http,$modal,$window,$location,$localStorage,$cookies ) {
        $scope.toaster = {
            type: 'success',
            title: 'Title',
            text: 'Message'
        };
        $scope.pop = function(type,title,text){
            toaster.pop(type,'',text);
        };
        $scope.applyNum = $location.search().applyNum;
        if(!$scope.applyNum){
            alert('申请编号不可为空');
            return;
        }
        $scope.showMsg = '获取验证码';
        $scope.user = {notShow: false};
        $scope.bank = {};
        $scope.bankNum = '';
        $scope.state = {};
        function getUserInfo(){
            $http.get('/apply/fourFactorVerificationInfo?applyNum=' + $scope.applyNum).success(function(data){
                if(data.status == 'SUCCESS'){
                    $scope.user = data.data;
                    if($scope.user.bank){
                        $scope.bank.BAHKYH = $scope.user.bank;
                    }
                }else{
                    alert(data.error);
                    return;
                }
            }).error(function(){
                    alert('获取用户信息错误，请稍后再试');
                    return;
            });
        }
        function init(){
             getUserInfo();
            $http.get('/apply/banks').success(function(res){
                if(res.status == 'SUCCESS'){
                   $scope.banks = res.data;
                   var bankList =  $scope.banks;
                    if($scope.bank.BAHKYH){
                       for (var i = 0;i < bankList.length; i++ ) {
                            if($scope.bank.BAHKYH == $scope.banks[i].BAHKYH){
                                $scope.bank = $scope.banks[i];
                            }
                       }
                    }else{
                        $scope.bank = $scope.banks[0];
                    }
                }else{
                     alert(res.error);
                     return;
                 }
            }).error(function(){
                     alert('获取银行列表错误，请稍后再试');
                     return;
            });
        }
        init();

        $scope.upload = function(){
            if($scope.file == null){
                alert('请上传银行卡正面照片');
                return;
            }
            var file = new FormData();
            file.append('file', $scope.file);
            $http.post('/files?type=bankCard', file, {
                      transformRequest: angular.identity,
                      headers: {'Content-Type': undefined}
                   }).success(function(result){
                if(result.status == 'SUCCESS'){
                    $scope.url = result.data.url;
                    alert('上传成功');
                }else{
                    $scope.error = result.error;
                }
            }).error(function(){
                  alert('上传失败，请稍后再试');
                  return;
            });
        };

    $scope.declare = function(){
            $modal.open({
                templateUrl: 'tpl/weizhong/testa.html',
                controller: 'wxDeclareController',
                resolve:{
                }
            });
    };


    //申请提交
    $scope.submit = function(){
//        if(!$scope.url){
//             alert("请上传银行卡照片");
//             return;
//        }
        $scope.user.bank = $scope.bank.BAHKYH;
        $scope.user.applyNum = $scope.applyNum;
//        $scope.user.bankcardImg = $scope.url;
        $scope.state.notShow = true;
        $scope.$emit("BUSY");
        $http.post('/apply/submitBankCardInfo',$scope.user).success(function(data){
            $scope.state.notShow = false;
            $scope.$emit("NOTBUSY");
            if(data.status == 'SUCCESS'){
                var modalInstance = $modal.open({backdrop : 'static',size:'md',
                    templateUrl: 'tpl/weizhong/wx_msgCodeController.html',
                    controller: 'wxMsgCodeController',
                    resolve:{
                    }
                });
                modalInstance.result.then(function (code) {
                    if(code == null || code == ''){
                        return;
                    }
                    $scope.verification = {};
                    $scope.verification.msgCode = code;
                    $scope.verification.applyNum = $scope.applyNum;
                    $scope.verification.phoneNum = $scope.user.phoneNum;
                    $scope.state.notShow = true;
                    $scope.$emit("BUSY");
                    $http.post('/apply/fourFactorVerification', $scope.verification).success(function(result){
                        $scope.state.notShow = false;
                        $scope.$emit("NOTBUSY");
                        if(result.status == 'SUCCESS'){
                            alert("验证成功!");
                            $scope.user = {};
                        }else{
                            alert("验证失败" + result.error);
                        }
                    }).error(function(){
                        alert('验证失败，请稍后再试');
                        $scope.state.notShow = false;
                        $scope.$emit("NOTBUSY");
                    });
                },function(){
                });
            }else{
                alert("提交失败" + data.error);
                return;
            }
        }).error(function(){
            $scope.state.notShow = false;
            $scope.$emit("NOTBUSY");
            alert('提交失败，请稍后再试');
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

    $scope.$on('timer-stopped', function (event, data){
        $scope.startCountdown = false;
        $scope.showMsg = "获取验证码";
        $scope.$digest();//通知视图模型的变化
    });
}])
;
