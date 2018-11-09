/**
 * Created by LEO on 16/11/3.
 */
'use strict';
app.controller('sendMsgController', ['$scope', '$http', 'toaster', '$state', '$localStorage', '$cookies', function ($scope, $http, toaster, $state, $localStorage, $cookies) {

    $localStorage.gpsUrl = 'gps.sendMsg';
    if($cookies.get('gpsUserInfoId') == null || $cookies.get('gpsUserInfoId') == ''){
        $state.go('gps.signin');
    }
    var userInfo = {id:$cookies.get('gpsUserInfoId'), phoneNum:$cookies.get('gpsUserInfoPhoneNum')};

    //提示信息
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };
    $scope.msg = {};
    $scope.sendMsg = function(){
        $scope.msg.operator = userInfo.phoneNum;
        $http.post('sms/sendMsg',$scope.msg).success(function(data){
            if(data.status == 'SUCCESS'){
                $scope.pop('success','','发送成功');
                var simCardNum = $scope.msg.terminalId;
                $state.go("gps.msglist",{terminalId:simCardNum});
            }else{
                $scope.pop('error','',data.error);
            }
        })
    }
}])
;