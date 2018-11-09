app.controller('wxMsgCodeController',['$scope', '$http', '$modalInstance', '$stateParams','$rootScope', function($scope, $http, $modalInstance, $stateParams, $rootScope) {

    $scope.close = function(){
        $modalInstance.close();
    };
    $scope.code = '';
    $scope.choose = function(item){
        if(!$scope.code){
            alert("请输入短信验证码");
        }
        $modalInstance.close(item);
    };
}])
;