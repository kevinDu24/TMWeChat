app.controller('usedCarAnalysisController',[ 'toaster','$scope', '$state', '$http', '$modal', '$window', '$location','$localStorage','$cookies', '$filter', function(toaster,$scope,$state,$http,$modal,$window,$location,$localStorage,$cookies,$filter ) {
        $scope.toaster = {
            type: 'success',
            title: 'Title',
            text: 'Message'
        };
        $scope.pop = function(type,title,text){
            toaster.pop(type,'',text);
        };
        $scope.showMsg = '获取验证码';
        $scope.user = {
            color: '黑色',
            transferTimes : '1',
            workState: '中',
            interior:'中',
            surface:'中',
            regDate: new Date(),
            makeDate: new Date()
        };
   function getAllCity() {
        $http.get('/usedCarAnalysis/getAllCity').success(function(data){
            if(data.status == 'SUCCESS'){
                $scope.zones = data.data.cityListResult;
                $scope.zone = $scope.zones[0];
                $scope.optionChange($scope.zone);
            }else{
                alert(data.error);
                return;
            }
        }).error(function(){
              alert('获取省份列表错误，请稍后再试');
              return;
       });
    }

    getAllCity();


    $scope.optionChange = function(zone){
        $scope.cityList = $scope.zone.dataRows;
        $scope.city = $scope.cityList[0];
    };

    $scope.identifyModelByVIN = function(){
        if(!$scope.user.vin){
             alert('车架号不可为空');
             return;
        }
        $http.get('/usedCarAnalysis/identifyModelByVIN?vin=' + $scope.user.vin).success(function(data){
            if(data.status == 'SUCCESS'){
                $scope.user.modelId = data.data.model_id;
                $scope.user.modelName = data.data.model_name;
            }else{
                alert(data.error);
                return;
            }
        }).error(function(){
              alert('获取车辆型号错误，请稍后再试');
              return;
       });
    };


    //申请提交
    $scope.submit = function(){
        if($scope.startCountdown){
            return;
        }
        var phoneNum = $scope.user.operatorPhoneNum;
        if(phoneNum == null || phoneNum == undefined){
            alert('未填写手机号码');
            return;
        }
        if($scope.user.vin == null || $scope.user.vin == undefined){
             alert('车架号不可为空');
             return;
        }
        if($scope.user.modelName == null || $scope.user.modelName == undefined){
             alert('车辆型号不可为空');
             return;
        }
        if($scope.user.messageCode == null || $scope.user.messageCode == undefined){
            alert('请填写验证码');
            return;
        }
        $scope.user.regDate = $filter('date')($scope.user.regDate,'yyyy-MM');
        $scope.user.zone = $scope.city.city_id;
        $scope.user.makeDate =  $filter('date')($scope.user.makeDate,'yyyy-MM');
        $scope.user.operator = 'tmwechatWeb';

        $scope.user.notShow = true;
        $scope.$emit("BUSY");
        $http.post('/usedCarAnalysis/getSpecifiedPriceAnalysisUnverified', $scope.user).success(function(result){
            $scope.user.notShow = false;
            $scope.$emit("NOTBUSY");
            if(result.status == 'SUCCESS'){
                 $scope.b2c_price = result.data.b2c_price;
                 $scope.report_url = result.data.report_url;
                 alert('车辆评估价格为:' + $scope.b2c_price);
                 $window.location.href = $scope.report_url;
                 $scope.user = {};
            }else{
                alert("提交失败" + result.error);
            }
        }).error(function(){
            alert('提交失败，请稍后再试');
            $scope.user.notShow = false;
            $scope.$emit("NOTBUSY");
        });
    }
    $scope.getSmsCode = function(){
        if($scope.startCountdown){
            return;
        }
        var phoneNum = $scope.user.operatorPhoneNum;
        if(phoneNum == null || phoneNum == ''){
            alert('未填写手机号码');
            return;
         }
         if($scope.user.vin == null || $scope.user.vin == undefined){
             alert('车架号不可为空');
             return;
         }
         if($scope.user.modelName == null || $scope.user.modelName == ''){
             alert('车辆型号不可为空');
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
