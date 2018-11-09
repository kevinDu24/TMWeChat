app.controller('applyController', ['$scope', '$http', '$state', '$localStorage', '$filter', '$rootScope', '$modal', '$cookies', function($scope, $http, $state, $localStorage, $filter, $rootScope, $modal, $cookies) {
    $localStorage.requestUrl = 'app.hpl.apply';
    $scope.canSccroll = false;
    $scope.loadComplete = false;
    $scope.loading = false;
    $scope.contract = {applyNum: '', fpName: '', page:1};
    $scope.currentPage = 1;
    $scope.startDate = $rootScope.startDate == null ? new Date() : $rootScope.startDate;
    $scope.endDate = $rootScope.endDate == null ? new Date() : $rootScope.endDate;
    $scope.applyList = {};
    $scope.page = 1;
    $scope.categories = [{showName:'全部', value:'', index:0}, {showName:'审批阶段', value:'审批阶段', index:1},
        {showName:'放款阶段', value:'放款阶段', index:2}, {showName:'待材料归档', value:'待材料归档', index:3},
        {showName:'取消', value:'取消', index:4}, {showName:'拒绝', value:'拒绝', index:5}];
    $scope.category = $scope.categories[$rootScope.applyCategoryIndex == null ? 0 : $rootScope.applyCategoryIndex];
    function getContractData(){
        if($scope.category.value == '全部'){
            $scope.contract.state = ''
        }else{
            $scope.contract.state = $scope.category.value;
        }
        $scope.loading = true;
        $scope.contract.startDate = $filter('date')($scope.startDate==null? new Date() : $scope.startDate,'yyyyMMdd');
        $scope.contract.endDate = $filter('date')($scope.endDate==null? new Date() : $scope.endDate,'yyyyMMdd');
        if($scope.condition != null && $scope.condition != ''){
            $scope.contract.applyNum = $scope.condition;
        }
        $http.get('/contracts/state', {params:$scope.contract}).success(function(result){
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
                $scope.canScroll = true;
                if(tempList.length < 20){
                    $scope.loadComplete = true;
                }
            }
            $scope.loading = false;
        });
    }

    getContractData();

    $scope.refresh = function(){
        $scope.applyList = {};
        $scope.canScroll = true;
        $scope.currentPage = 1;
        $scope.contract.page = 1;
        $scope.canScroll = false;
        $scope.loadComplete = false;
        $rootScope.startDate = $scope.startDate;
        $rootScope.endDate = $scope.endDate;
        $rootScope.applyCategoryIndex = $scope.category.index;
        getContractData();
    };

    $scope.goToDetail = function(row){
        $state.go('app.hpl.applyDetails');
        $localStorage.contractInfo = row;
    };

    $scope.gotoPage = function(page){
        $state.go(page);
    };

    $scope.nextPage = function(){
        if(!$scope.canScroll || $scope.loadComplete){
            return;
        }
        $scope.loading = true;
        $scope.canScroll = false;
        $scope.currentPage++;
        $scope.contract.page = $scope.currentPage;
        $http.get('/contracts/state', {params:$scope.contract}).success(function(result){
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
                if(parseInt(result.data.page.BAPAGE) >= parseInt(result.data.page.BAZYEL)){
                    $scope.canScroll = false;
                }else{
                    $scope.canScroll = true;
                }
                if(tempList.length < 20){
                    $scope.loadComplete = true;
                }
            }
            $scope.loading = false;
        });
    };

    $scope.search = function () {
        if($scope.loading){
            return;
        }
        var modalInstance = $modal.open({
            templateUrl: 'tpl/hpl_search.html',
            controller: 'contractSearchController',
            size: 'small'
        });

        modalInstance.result.then(function (condition) {
            if(condition == null || condition == ''){
                return;
            }
            $scope.condition = condition;
            $scope.applyList = {};
            $scope.canScroll = true;
            $scope.currentPage = 1;
            $scope.contract.page = 1;
            $scope.canScroll = false;
            $scope.loadComplete = false;
            $rootScope.startDate = $scope.startDate;
            $rootScope.endDate = $scope.endDate;
            $scope.category = $scope.categories[0];
            getContractData();
        }, function () {
            console.log();
        });
    };

    $scope.logout = function(){
        $http.post('logout').success(function(){
            $cookies.remove('Authorization');
            $state.go('access.signin');
        });
    };
}]);
