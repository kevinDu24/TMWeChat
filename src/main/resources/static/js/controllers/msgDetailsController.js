/**
 * Created by LEO on 16/11/3.
 */
app.controller('msgDetailsController', ['$scope', '$http', '$modalInstance', 'msg', '$localStorage', '$cookies', function ($scope, $http, $modalInstance,msg, $localStorage, $cookies) {
    $localStorage.gpsUrl = 'gps.msglist';
    if($cookies.get('gpsUserInfoId') == null || $cookies.get('gpsUserInfoId') == ''){
        $state.go('gps.signin');
    }
    function init(){
        $scope.msg = msg;
    }
    init();

    /**
     * 关闭新增窗口
     * @param status
     */
    $scope.close = function(status){
        $modalInstance.close(status);
    };
}]);
