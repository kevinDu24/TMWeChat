app.controller('applyDetailsController', ['$scope', '$http', '$state', '$localStorage', function($scope, $http, $state, $localStorage) {

    $scope.showSearchList = false;
    $scope.loadComplete = false;
    $scope.loading = false;
    var currentPage = 1;
    $scope.applyList = {};
    function getContractLog(condition){
        $http.get('/contracts/'+condition+'/log').success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.items = result.data.contractinfo.contractstatelist;
                $scope.ApplicantsInfo = {name: result.data.contractinfo.BASQXM, num: result.data.contractinfo.BASQBH};
            }
        });
    }

    function getContractList(){
        $scope.loading = true;
        var contract = {applyNum: '', fpName: '', startDate: '', endDate: '', state: '', page:currentPage};
        if(!isNaN($scope.condition) || /^[\u4e00-\u9fa5]+$/.test($scope.condition)){
            contract.applyNum = $scope.condition;
        }else{
            contract.fpName = $scope.condition;
        }
        $http.get('/contracts/state', {params:contract}).success(function(result){
            if(result.status == 'SUCCESS'){
                var tempList = result.data.contractstatelist;
                for(var i = 0; i < tempList.length; i++){
                    var index = tempList[i].BASQRQ.substring(0,4)+'-'+tempList[i].BASQRQ.substring(4,6)
                    +'-'+tempList[i].BASQRQ.substring(6,8);
                    if($scope.applyList[index] != undefined){
                        $scope.applyList[index].push(tempList[i]);
                    }else{
                        $scope.applyList[index] = [];
                        $scope.applyList[index].push(tempList[i]);
                    }
                }
                if(tempList.length < 20){
                    $scope.loadComplete = true;
                }
            }
            $scope.loading = false;
        });
    }

    getContractLog($localStorage.contractInfo.BASQBH);

    $scope.search = function(){
        if($scope.condition == null || $scope.condition == ''){
            return;
        }
        $scope.showSearchList = true;
        $scope.applyList = {};
        getContractList();
    };

    $scope.convertDate = function(date){
        return date.substring(0, 4) + '-' + date.substring(4, 6) + '-' + date.substring(6, 8) + ' ' + date.substring(8, 10)+ ':' + date.substring(10, 12) + ':' + date.substring(12,14)
    };

    $scope.nextPage = function(){
        currentPage ++;
        $scope.showSearchList = true;
        getContractList();
    };

    $scope.goToDetail = function(data){
        $scope.showSearchList = false;
        currentPage = 1;
        $localStorage.contractInfo.BASQBH = data.BASQBH;
        getContractLog(data.BASQBH);
    };
}])
;

