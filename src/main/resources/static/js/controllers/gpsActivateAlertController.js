/**
 * Created by LEO on 16/9/26.
 */
app.controller('gpsActivateAlertController', ['$scope', '$state', '$http', '$filter', '$localStorage', '$modal', '$modalInstance', 'data', function($scope,$state, $http, $filter, $localStorage, $modal, $modalInstance, data) {

    $scope.data = data;
    $scope.complete = function(){
        $modalInstance.close();
    };
}]);