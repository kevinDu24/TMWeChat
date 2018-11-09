app.controller('wxStatementController',['$scope', '$http', '$modalInstance', '$stateParams','$rootScope', function($scope, $http, $modalInstance, $stateParams, $rootScope) {
    $scope.submit = function(){
        $modalInstance.close();
    };
}])
;