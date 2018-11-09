/**
 * Created by LEO on 16/9/12.
 */
app.controller('contractSearchController', ['$scope', '$http', '$state', '$localStorage', '$modalInstance', function($scope, $http, $state, $localStorage, $modalInstance) {
    $scope.search = function(){
        $modalInstance.close($scope.condition);
    };
}]);