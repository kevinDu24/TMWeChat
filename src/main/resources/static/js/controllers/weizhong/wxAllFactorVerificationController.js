app.controller('wxAllFactorVerificationController',[ 'toaster','$scope', '$state', '$http', '$modal', '$window', '$location','$localStorage','$cookies', function(toaster,$scope,$state,$http,$modal,$window,$location,$localStorage,$cookies ) {
        $scope.toaster = {
            type: 'success',
            title: 'Title',
            text: 'Message'
        };
        $scope.pop = function(type,title,text){
            toaster.pop(type,'',text);
        };
        $scope.showMsg = '获取验证码';
        $scope.user = {};
        $scope.bank = {};
        $scope.bankNum = '';
        $scope.state = {};
        function init(){
            $http.get('/apply/banks').success(function(res){
                if(res.status == 'SUCCESS'){
                   $scope.banks = res.data;
                   $scope.bank = $scope.banks[0];
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

       //获取验证码
        $scope.getSmsCode = function(){
            $scope.user.bank = $scope.bank.BAHKYH;
            if($scope.startCountdown){
                return;
            }
            if($scope.user.name == null || $scope.user.idCardNum == null ||$scope.user.bankCardNum == null ||  $scope.user.phoneNum == null ){
                alert('请补全信息');
                return;
            }
            $scope.state.notShow = true;
            $scope.$emit("BUSY");
            $http.post('/apply/submitBankCardInfo',$scope.user).success(function(data){
                $scope.state.notShow = false;
                $scope.$emit("NOTBUSY");
                if(data.status == 'SUCCESS'){
                    $scope.uniqueMark = data.data;
                    $scope.$broadcast('timer-start');//开始倒计时
                    $scope.showMsg = 's 后重新获取';
                    $scope.startCountdown = true;
                }else{
                    alert("失败" + data.error);
                    return;
                }
            }).error(function(){
                $scope.state.notShow = false;
                $scope.$emit("NOTBUSY");
                alert('失败，请稍后再试');
            });
        };


    //提交
    $scope.submit = function(){
        if($scope.user.name == null || $scope.user.idCardNum == null ||$scope.user.bankCardNum == null ||  $scope.user.phoneNum == null ){
            alert('请补全信息');
            return;
        }
        if($scope.user.code == null || $scope.user.code == ''){
            $scope.pop('error', '', '请填写验证码');
            return;
        }
        $scope.state.notShow = true;
        $scope.$emit("BUSY");
        $scope.verification = {};
        $scope.verification.msgCode = $scope.user.code;
        $scope.verification.uniqueMark = $scope.uniqueMark;
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
    };



//    //获取验证码
//    $scope.getSmsCode = function(){
//        $scope.user.bank = $scope.bank.BAHKYH;
//        $scope.state.notShow = true;
//        $scope.$emit("BUSY");
//        $http.post('/apply/submitBankCardInfo',$scope.user).success(function(data){
//            $scope.state.notShow = false;
//            $scope.$emit("NOTBUSY");
//            if(data.status == 'SUCCESS'){
//            $scope.uniqueMark = data.data;
//                var modalInstance = $modal.open({backdrop : 'static',size:'md',
//                    templateUrl: 'tpl/weizhong/wx_msgCodeController.html',
//                    controller: 'wxMsgCodeController',
//                    resolve:{
//                    }
//                });
//                modalInstance.result.then(function (code) {
//                    if(code == null || code == ''){
//                        return;
//                    }
//                    $scope.verification = {};
//                    $scope.verification.msgCode = code;
//                    $scope.verification.uniqueMark = $scope.uniqueMark;
//                    $scope.verification.phoneNum = $scope.user.phoneNum;
//                    $scope.state.notShow = true;
//                    $scope.$emit("BUSY");
//                    $http.post('/apply/fourFactorVerification', $scope.verification).success(function(result){
//                        $scope.state.notShow = false;
//                        $scope.$emit("NOTBUSY");
//                        if(result.status == 'SUCCESS'){
//                            alert("验证成功!");
//                            $scope.user = {};
//                        }else{
//                            alert("验证失败" + result.error);
//                        }
//                    }).error(function(){
//                        alert('验证失败，请稍后再试');
//                        $scope.state.notShow = false;
//                        $scope.$emit("NOTBUSY");
//                    });
//                },function(){
//                });
//            }else{
//                alert("失败" + data.error);
//                return;
//            }
//        }).error(function(){
//            $scope.state.notShow = false;
//            $scope.$emit("NOTBUSY");
//            alert('失败，请稍后再试');
//        });
//    };

    $scope.$on('timer-stopped', function (event, data){
        $scope.startCountdown = false;
        $scope.showMsg = "获取验证码";
        $scope.$digest();//通知视图模型的变化
    });
}])
;
