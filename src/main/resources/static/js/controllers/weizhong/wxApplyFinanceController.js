app.controller('wxApplyFinanceController',['$scope', '$http', '$modalInstance', '$stateParams','$rootScope', function($scope, $http, $modalInstance, $stateParams, $rootScope) {

    $scope.cancel = function(){
        $modalInstance.close('0');
    };

    $scope.submit = function(){
        $modalInstance.close('1');
    };
}])
;