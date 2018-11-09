app.controller('gpsActivateQueryDetailsController', ['toaster', '$scope', '$state', '$http', '$cookies', '$localStorage', '$modal', function(toaster, $scope,$state, $http, $cookies, $localStorage, $modal) {

    $localStorage.gpsUrl = 'gps.activateQueryDetails';
    var phoneNum = $cookies.get('gpsUserInfoPhoneNum');
    var waitingActivateDeviceIndexs = [];
    var waitingTempIndexs = [];
    if(phoneNum == null || phoneNum == ''){
        $state.go('gps.signin');
    }
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };

    function getDeviceInfo(){
        if(waitingTempIndexs.length == 0){
            reload();
            return;
        }
        var index = waitingTempIndexs.pop();
        $http.get('/gps/devices/simCards/' + $scope.datas[index].simCardNum + '?num=' + count).success(function(result){
            if(result.status == 'SUCCESS'){
                //if(temp.status != '4' && temp.status != '3'){
                //    temp.status = '2';
                //}
                $scope.datas[index] = result.data[0];
                $scope.$digest;
                getDeviceInfo();
            }
        }).error(function(){
            $scope.pop('error', '', '系统故障');
        });
    }

    var count = 0;
    function reload(){
        var temp = [];
        for(var i in waitingActivateDeviceIndexs){
            //if($scope.datas[waitingActivateDeviceIndexs[i]].status != '4' && $scope.datas[waitingActivateDeviceIndexs[i]].status != '6'){
            //    temp.push(waitingActivateDeviceIndexs[i]);
            //}
            if($scope.datas[waitingActivateDeviceIndexs[i]].status == '2'){
                temp.push(waitingActivateDeviceIndexs[i]);
            }
        }
        waitingActivateDeviceIndexs = temp;
        if(count >= 3){
            //for(var i in waitingActivateDeviceIndexs){
            //    $scope.datas[waitingActivateDeviceIndexs[i]].status = '5';
            //}
            return;
        }
        if(waitingActivateDeviceIndexs.length == 0){
            return;
        }
        count ++;
        waitingTempIndexs = angular.copy(waitingActivateDeviceIndexs);
        //if(count == 1){
        //    getDeviceInfo();
        //    return;
        //}
        setTimeout(function(){
            getDeviceInfo();
        }, 35000);
    }

    $scope.getData = function(simCard, type){
        if(simCard == null || simCard == ''){
            $scope.pop('error', '', '请输入SIM卡号');
            return;
        }
        $scope.data = null;
        var url = type == 1 ? '/gps/devices/simCards/' + simCard + '?num=1' : 'gps/applyTasks/installPersons/'+simCard;
        $scope.datas = [];
        $scope.myPromise = $http.get(url).success(function(result){
            if(result.status == 'SUCCESS'){
                if(result.data.length == 0){
                    $scope.pop('error', '', '未查询到任何记录');
                    return;
                }
                $scope.datas = result.data;
                for(var i in $scope.datas){
                    if($scope.datas[i].status == '2'){
                        waitingActivateDeviceIndexs.push(i);
                    }
                }
                reload();
                $scope.$digest;
            }else{
                $scope.pop('error', '', result.error);
            }
        })
        .error(function(status){
                $scope.pop('error', '', '系统正在处理,请稍后点击顶部刷新按钮进行查询');
            });
    };

    $scope.refresh = function(){
        $scope.getData(phoneNum, 2);
    };

    $scope.getData(phoneNum, 2);

    //更换设备
    $scope.changeDevice = function(type, index){
        var rtn = $modal.open({
            templateUrl: 'tpl/gps_activateQuery_prompt.html',
            controller: 'gpsActivateQueryPromptController',
            size: 'lg',
            resolve:{
                id:function(){return $scope.datas[index].id;},
                simCard:function(){return $scope.datas[index].simCardNum}
            }
        });
        rtn.result.then(function (result, msg) {
            if(result != null){
                if(result.status == 'SUCCESS'){
                    //$state.go('gps.activate', {vin: $scope.datas[index].applyOrder.vin.substr($scope.datas[index].applyOrder.vin.length - 6), carOwnerName: $scope.datas[index].applyOrder.customerName, gpsType: $scope.datas[index].deviceType});
                    $state.go('gps.activate');
                }else{
                    $scope.pop('error', '', result.error);
                }
            }else if(msg == 'error'){
                $scope.pop('error', '', '系统故障');
            }
        }, function () {

        });
    };

    $scope.complete = function(index){
        $http.put('/gps/applyTasks/complete/' + $scope.datas[index].id).success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.datas[index].status = '6';
            }else{
                $scope.pop('error', '', result.error);
            }
        }).error(function(){
            $scope.pop('error', '', '系统故障');
        });
    };

    $scope.refreshState = function(index){
        $scope.myPromise = $http.get('/gps/devices/simCards/' + $scope.datas[index].simCardNum +'?num=1').success(function(result){
            if(result.status == 'SUCCESS'){
                var temp = result.data[0];
                $scope.datas[index] = temp;
                $scope.$digest;
            }
        }).error(function(){
            $scope.pop('error', '', '系统故障');
        });
    };

    $scope.reactivate = function(index){
        $scope.myPromise = $http.get('/gps/devices/reactive/simCards/' + $scope.datas[index].simCardNum).success(function(result){
            if(result.status == 'SUCCESS'){
                var temp = result.data[0];
                $scope.datas[index] = temp;
                $scope.$digest;
            }
        }).error(function(){
            $scope.pop('error', '', '系统故障');
        });
    };
}])
;
