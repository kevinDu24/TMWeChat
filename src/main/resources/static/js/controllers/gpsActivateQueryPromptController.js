/**
 * Created by LEO on 16/11/12.
 */
app.controller('gpsActivateQueryPromptController', ['toaster', '$scope', '$state', '$http', '$cookies', '$localStorage', '$modal', 'id', '$modalInstance', 'simCard', function(toaster, $scope,$state, $http, $cookies, $localStorage, $modal, id, $modalInstance, simCard) {
    $scope.complete = function(type){
        if(type == 1){
            $modalInstance.close(null);
        }else{
            $scope.myPromise = $http.put('/gps/applyTasks/reset/'+simCard+'/' + id + '?resetReason=' + $scope.reason).success(function(result){
                $modalInstance.close(result, 'success');
            }).error(function(){
                $modalInstance.close(null, 'error');
            });
        }
    };
}])
;