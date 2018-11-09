app.controller('marketApplyController', ['$scope', '$state', '$http', '$filter', '$localStorage', '$modal', function($scope,$state, $http, $filter, $localStorage, $modal) {

    $scope.startDate = $scope.endDate = new Date();
    var currentArea;
    function init(){
        //if($localStorage.areaLevels != null && $localStorage.areaLevels.length != 0){
        //    currentArea = $localStorage.areaLevels[0];
        //    getStatisticData(currentArea);
        //}else{
            $http.get('/sales/queryAreaLevel').success(function(result){
                if(result.status == 'SUCCESS'){
                    $localStorage.areaLevels = [];
                    $localStorage.areaLevels = result.data;
                    currentArea = $localStorage.areaLevels[0];
                    getStatisticData(currentArea);
                }
            });
        //}
    }

    function getParentLevel(levelId){
        if(levelId == 0){
            return;
        }
        var area = getAreaInfo(levelId);
        return getAreaInfo(area.parentId);
    }

    function getAreaInfo(levelId){
        var areaLevels = $localStorage.areaLevels;
        for(var i in areaLevels){
            if(areaLevels[i].levelId == levelId){
                return areaLevels[i];
            }
        }
    }

    function getStatisticData(area){
        $http.get('/sales/plan?parentId='+area.levelId+'&startDate='+$filter('date')($scope.startDate,'yyyyMMdd')+'&endDate='+$filter('date')($scope.endDate,'yyyyMMdd')+'&planType=6').success(function(data){
            if(data.status == 'SUCCESS'){
                if(data.data == null || data.data.length == 0 || (data.data.length == 1 && data.data[0].levelId == area.levelId)){
                    return;
                }
                currentArea = area;
                $scope.datas = data.data;
                $scope.header = {planCount:0, realCount:0};
                if(currentArea.area == '' || currentArea.area == null){
                    $scope.header.name = currentArea.areaName;
                }else {
                    $scope.header.name = currentArea.area;
                }
                $scope.header.levelId = currentArea.levelId;
                for(var i in $scope.datas){
                    if($scope.datas[i].planCount == ''){
                        $scope.datas[i].planCount = 0;
                    }
                    $scope.header.planCount += parseInt($scope.datas[i].planCount);
                    $scope.header.realCount += parseInt($scope.datas[i].realCount);
                }
            }
        });
    }
    init();

    $scope.goToDeep = function(area){
        if(currentArea.levelId == area.levelId){
            return;
        }
        //currentArea = area;
        getStatisticData(area);
    };

    $scope.goUp = function(area){
        var temp = getParentLevel(area.levelId);
        if(temp == null){
            return;
        }
        currentArea.area = temp.areaName;
        currentArea.levelId = temp.levelId;
        getStatisticData(currentArea);
    };

    $scope.refresh = function(){
        getStatisticData(currentArea);
    };

    $scope.open = function () {
        var modalInstance = $modal.open({
            templateUrl: 'tpl/areaSelector.html',
            controller: 'areaSelectorController',
            size: 'small'
        });

        modalInstance.result.then(function (area) {
            if(area != null && area != ''){
                //$scope.currentArea = area;
                getStatisticData(area);
            }
        }, function () {
            console.log();
        });
    };
}])
;
