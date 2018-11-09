app.controller('contractDetailsController', ['$scope', '$state', '$localStorage' , '$http', function($scope,$state,$localStorage, $http) {
    function getContractDetails(){
        $http.get('/contracts/'+$localStorage.contractInfo.BASQBH+'/details').success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.contractDetails = result.data.contractInfo;
            }
        });
    }
    getContractDetails();
}])
;

