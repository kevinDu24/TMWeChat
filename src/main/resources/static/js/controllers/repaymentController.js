app.controller('repaymentController', ['$scope', '$state', '$http', '$localStorage', '$filter', '$rootScope', '$modal', '$cookies', function($scope,$state, $http, $localStorage, $filter, $rootScope, $modal, $cookies) {

    $scope.canScroll = false;
    $scope.loadComplete = false;
    $scope.loading = false;
    $scope.currentPage = 1;
    $scope.contract = {applyNum: '', fpName: '', page:1};
    $scope.startDate = $rootScope.repayStartDate == null ? new Date() : $rootScope.repayStartDate;
    $scope.endDate = $rootScope.repayEndDate == null ? new Date() : $rootScope.repayEndDate;
    $scope.applyList = {};
    $scope.page = 1;
    $scope.categories = [{showName:'全部', value:'', index:0}, {showName:'正常扣款', value:'正常扣款', index:1},
        {showName:'逾期30天内', value:'逾期30天内', index:2}, {showName:'逾期超过30天', value:'逾期超过30天', index:3}];
    $scope.category = $scope.categories[$rootScope.repayCategoryIndex == null ? 0 : $rootScope.repayCategoryIndex];
    $scope.bakssj = $scope.bajssj = new Date();
    function getContractData(){
        $scope.loading = true;
        if($scope.category.value == '全部'){      // 还款状态 -- 2018/9/7 10:32 By ChengQC
            $scope.contract.state = '';
        }else{
            $scope.contract.state = $scope.category.value;
        }
        if($scope.condition != null && $scope.condition != ''){     // 查询关键字（申请号，姓名） -- 2018/9/7 10:33 By ChengQC
            $scope.contract.applyNum = $scope.condition;
        }
        $scope.contract.startDate = $filter('date')($scope.startDate==null? new Date() : $scope.startDate,'yyyyMMdd');
        $scope.contract.endDate = $filter('date')($scope.endDate==null? new Date() : $scope.endDate,'yyyyMMdd');
        $http.get('/contracts/repayment', {params:$scope.contract}).success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.applyList = {};
                var tempList = result.data.contractrepayplanlist;
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
        $scope.currentPage = 1;
        $scope.contract.page = 1;
        $scope.canScroll = false;
        $scope.loadComplete = false;
        $rootScope.repayStartDate = $scope.startDate;
        $rootScope.repayEndDate = $scope.endDate;
        $rootScope.repayCategoryIndex = $scope.category.index;
        getContractData();
    };

    $scope.goToDetail = function(row){
        $state.go('app.hpl.repaymentDetails');
        $localStorage.repaymentInfo = row;
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
        $http.get('/contracts/repayment', {params:$scope.contract}).success(function(result){
            if(result.status == 'SUCCESS'){
                var tempList = result.data.contractrepayplanlist;
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
            }
            $scope.loading = false;
        });
    };

    // 弹出搜索框 -- 2018/9/7 10:23 By ChengQC
    $scope.search = function () {
        if($scope.loading){
            return;
        }
        var modalInstance = $modal.open({       // angluar弹层 -- 2018/9/7 10:14 By ChengQC
            templateUrl: 'tpl/hpl_search.html',
            controller: 'contractSearchController',
            size: 'small'
        });

        modalInstance.result.then(function (condition) {   // 正常关闭后执行搜索 -- 2018/9/7 10:12 By ChengQC
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
        }, function () {                    // 取消或退出执行的函数 -- 2018/9/7 10:13 By ChengQC
            console.log("取消搜索操作");
        });
    };

    $scope.logout = function(){
        $http.post('logout').success(function(){
            $cookies.remove('Authorization');
            $state.go('access.signin');
        });

    };
}])
;
