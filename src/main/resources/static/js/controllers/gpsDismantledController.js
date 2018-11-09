/**
 * Created by LEO on 16/11/9.
 */
app.controller('gpsDismantledController', ['toaster', '$scope', '$state', '$http', '$cookies', '$localStorage', function(toaster, $scope,$state, $http, $cookies, $localStorage) {
    $localStorage.gpsUrl = 'gps.dismantle';
    var phoneNum = $cookies.get('gpsUserInfoPhoneNum');
    if(phoneNum == null || phoneNum == ''){
        $state.go('gps.signin');
    }
    $scope.simCardList = [];
    $scope.selectedSimCard = [];
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };

    $scope.dismantle = {};

    $scope.properties = ['鲁诺', 'FP'];

    $scope.submit = function(){
        $scope.dismantle.applyTasks = [];
        for(var i in $scope.selectedSimCard){
            $scope.dismantle.applyTasks.push({id:$scope.selectedSimCard[i].orderId});
        }
        if($scope.dismantle.applyTasks.length == 0){
            $scope.pop('error', '', '请选中要拆除的sim卡');
            return;
        }
        $scope.dismantle.submitPersonPhone = phoneNum;
        $scope.dismantle.customerName = $scope.taskList[0].applyOrder.customerName;
        $scope.dismantle.vin = $scope.taskList[0].applyOrder.vin;
        $scope.dismantle.dismantlePersonName = $scope.dismantle.dismantlePersonName == null ? '' : $scope.dismantle.dismantlePersonName;
        $scope.dismantle.dismantlePersonPhone = $scope.dismantle.dismantlePersonPhone == null ? '' : $scope.dismantle.dismantlePersonPhone;
        $scope.dismantle.providerName = $scope.property == null ? '' : $scope.property;
        $http.post('/gps/dismantle', $scope.dismantle).success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.pop('success', '', '申请提交成功,请耐心等待审核');
            }else{
                $scope.pop('error', '', result.error);
            }
        }).error(function(){
            $scope.pop('error', '', '系统故障');
        });
    };

    $scope.query = function(){
        var temp = $scope.vin + '';
        var vinTemp = temp;
        for(var i=0; i<(6-temp.length); i++){
            vinTemp = '0'+vinTemp;
        }
        $http.get('/gps/applyTasks?applyNum='+$scope.applyNum + '&vin='+vinTemp).success(function(result){
            $scope.simCardList = [];
            $scope.selectedSimCard = [];
            if(result.status == 'SUCCESS'){
                if(result.data != null && result.data != ''){
                    for(var i in result.data){
                        var temp = {simCardNum:result.data[i].simCardNum, type:result.data[i].deviceType == '1' ? '有线' : '无线', orderId: result.data[i].id};
                        $scope.selectedSimCard.push(temp);
                        $scope.simCardList.push(temp);
                    }
                }else{
                    $scope.pop('error', '', '未查询到数据');
                }
                $scope.taskList = result.data;
            }else{
                $scope.pop('error', '', result.error);
            }
        }).error(function(){
            $scope.pop('error', '', '系统故障');
        });
    };
}])
;