app.controller('repaymentDetailsController', ['$scope', '$state' , '$localStorage', '$http', function($scope,$state, $localStorage, $http) {

    $scope.showSearchList = false;
    $scope.loadComplete = false;
    $scope.loading = false;
    var currentPage = 1;
    $scope.applyList = {};

    (function () {
        if (typeof Object.defineProperty === 'function') {
            try {
                Object.defineProperty(Array.prototype, 'sortBy', {value: sb});
            } catch (e) {
            }
        }
        if (!Array.prototype.sortBy) Array.prototype.sortBy = sb;

        function sb(f) {
            for (var i = this.length; i;) {
                var o = this[--i];
                this[i] = [].concat(f.call(o, o, i), o);
            }
            this.sort(function (a, b) {
                for (var i = 0, len = a.length; i < len; ++i) {
                    if (a[i] != b[i]) return a[i] < b[i] ? -1 : 1;
                }
                return 0;
            });
            for (var i = this.length; i;) {
                this[--i] = this[i][this[i].length - 1];
            }
            return this;
        }
    })();

    function getPlanInfo(condition){
        $http.get('/contracts/'+condition+'/repayDetail').success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.contractInfo = result.data.contractrepayplaninfo;
                $scope.contractInfo.repayplan = $scope.contractInfo.repayplan.sortBy(function(){return this.BAHKRQ});
                if($scope.contractInfo.BAYQTS == ''){
                    $scope.contractInfo.BAYQTS = '0';
                }
                if($scope.contractInfo.BAYQLX == ''){
                    $scope.contractInfo.BAYQLX = '0';
                }
            }
        });
    }

    function getContractPlans(){
        $scope.loading = true;
        var contract = {applyNum: '', fpName: '', startDate: '', endDate: '', state: '', page:currentPage};
        if(!isNaN($scope.condition) || /^[\u4e00-\u9fa5]+$/.test($scope.condition)){
            contract.applyNum = $scope.condition;
        }else{
            contract.fpName = $scope.condition;
        }
        $http.get('/contracts/repayment', {params:contract}).success(function(result){
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
                if(tempList.length < 20){
                    $scope.loadComplete = true;
                }
            }
            $scope.loading = false;
        });
    }
    getPlanInfo($localStorage.repaymentInfo.BASQBH);

    $scope.search = function(){
        if($scope.condition == null || $scope.condition == ''){
            return;
        }
        $scope.showSearchList = true;
        $scope.applyList = {};
        getContractPlans();
    };

    $scope.nextPage = function(){
        currentPage ++;
        $scope.showSearchList = true;
        getContractPlans();
    };

    $scope.goToDetail = function(data){
        $scope.showSearchList = false;
        currentPage = 1;
        $localStorage.repaymentInfo.BASQBH = data.BASQBH;
        getPlanInfo(data.BASQBH);
    };
}])
;

