/**
 * Created by LEO on 16/11/3.
 */
'use strict';

app.controller('msgHistoryController', ['$scope', '$http', '$modal', 'toaster', '$state', '$localStorage', '$cookies', function ($scope, $http, $modal, toaster, $state, $localStorage, $cookies) {

    $localStorage.gpsUrl = 'gps.msgHistory';
    if($cookies.get('gpsUserInfoId') == null || $cookies.get('gpsUserInfoId') == ''){
        $state.go('gps.signin');
    }
    var userInfo = {id:$cookies.get('gpsUserInfoId'), phoneNum:$cookies.get('gpsUserInfoPhoneNum')};

    $scope.simCardNum = '';
    $scope.providerName = '';
    //ngGrid初始化数据
    $scope.filterOptions = {
        filterText: "",
        useExternalFilter: true
    };
    $scope.pagingOptions = {
        pageSizes: [10, 15, 20],
        pageSize: '10',
        currentPage: 1
    };
    $scope.gridOptions = {
        data: 'smsList',
        enablePaging: true,
        showFooter: true,
        footerTemplate: '<button class="btn btn-default pull-right" ng-click="downPage()" ng-disabled="!hasNext">下一页</button><button class="btn btn-default pull-right m-r" ng-click="upPage()" ng-disabled="!hasPrevious">上一页</button>',
        multiSelect: false,
        rowHeight: 41,
        enableHighlighting : true,
        headerRowHeight: 36,
        totalServerItems: 'totalServerItems',
        pagingOptions: $scope.pagingOptions,
        enableColumnResize: true,
        columnDefs: [
            { field: 'terminalId', displayName: 'sim卡号',width:'40%'},
            { field: 'lastTime', displayName: '最后发送/接收短信时间',width:'40%', cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"},
            { field: 'remove' , displayName: '操作', cellTemplate: '<button ng-click="searchMsg(row.entity)" class="btn btn-default m-l-xs" style="margin-top: 2px"><i class="fa fa-send"></i></button>',width:'10%' }
        ]
    };


    $scope.search = function(){
        $scope.pagingOptions.currentPage = 1;
        $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, '');
    }

    $scope.searchMsg = function(entity){
        var simCardNum = this.row.entity.terminalId;
        $state.go("gps.msglist",{terminalId:simCardNum});
    }

    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text, 'trustedHtml');
    };

    $scope.seeRowIndex = function(entity){
        var rtn = $modal.open({
            templateUrl: 'tpl/gps_msgDetails.html',
            controller: 'msgDetailsController',
            resolve:{
                msg : function (){ return entity }
            }
        });
        rtn.result.then(function (status) {
            $scope.pop('success','','已关闭');
            $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
        },function(){
        });
    };

    $scope.getPagedDataAsync = function (pageSize, page, searchText) {
        var url = '/sms?page='+page+'&size='+pageSize + '&operator=' + userInfo.phoneNum;
            +'&simCardNum='+$scope.simCardNum+'&providerName='+$scope.providerName+"&timestap="+new Date();
        $http.get(url).success(function (pagedata) {
            $scope.smsList = pagedata.data.content;
            $scope.totalServerItems = pagedata.data.totalElements;
            if($scope.smsList.length < pageSize){
                $scope.hasNext = false;
            }
            if(page == 1){
                $scope.hasPrevious = false;
            }
        });
    };
    $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, "");

    $scope.$watch('pagingOptions', function (newVal, oldVal) {
        if (newVal !== oldVal || newVal.currentPage !== oldVal.currentPage || newVal.pageSize !== oldVal.pageSize) {
            $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
        }
    }, true);

    $scope.$watch('filterOptions', function (newVal, oldVal) {
        if (newVal !== oldVal) {
            $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
        }
    }, true);

    $scope.upPage = function(){
        if($scope.pagingOptions.currentPage <= 1){
            return;
        }
        $scope.pagingOptions.currentPage --;
    };

    $scope.downPage = function(){
        $scope.pagingOptions.currentPage ++;
    }
}])
;