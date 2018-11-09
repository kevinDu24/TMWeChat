app.controller('apply','$state', ['$scope',function($scope,$state) {
    $scope.gotoPage = function(page){
        $state.go(page);
    };

}])
;
