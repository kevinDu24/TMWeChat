app.controller('applyReportController', ['$scope', '$http', '$modal', '$window', '$filter','$localStorage',function ($scope, $http, $modal, $window,$filter,$localStorage) {
    $localStorage.requestUrl = 'app.applyReport';
    $scope.status="";
    $scope.applyNum="";
    $scope.fpName="";
    $scope.name="";
    $scope.nowDate = new Date();
    $scope.endTime = $filter('date')($scope.nowDate, 'yyyy-MM-dd');
    $scope.beginTime =  $filter('date')(new Date(new Date().getTime()-7*1000*60*60*24), 'yyyy-MM-dd');
    $scope.monthData;
    $scope.searchType = 0;
    $scope.statusList = [
        {name:'预审批状态', value:''},
        {name:'待提交', value:'0'},
        {name:'审批中', value:'100'},
        {name:'审批通过', value:'1100'},
        {name:'审批拒绝', value:'1000'}
    ];
    $scope.status = $scope.statusList[0];

    //ngGrid初始化数据
    $scope.filterOptions = {
        filterText: "",
        useExternalFilter: true
    };

    $scope.pagingOptions = {
        pageSizes: [10, 15, 20, 50, 100],
        pageSize: '10',
        currentPage: 1
    };


    $scope.gridOptions = {
        data: 'codes',
        enablePaging: true,
        showFooter: true,
        multiSelect: false,
        rowHeight: 41,
        headerRowHeight: 36,
        enableHighlighting : true,
        enableColumnResize: true,
        totalServerItems: 'totalServerItems',
        pagingOptions: $scope.pagingOptions,
        columnDefs: [
            { field: 'name', displayName: '姓名', width:'100px' },
            { field: 'applynum', displayName: '申请编号', width:'120px',cellTemplate: '<div class="ui-grid-cell-contents"  style="margin-top: 8px; margin-left:15px">{{ row.entity.applynum == null ? "无" : row.entity.applynum }}</div>' },
            { field: 'status', displayName: '状态', width:'100px', cellTemplate: '<div class="ui-grid-cell-contents"  style="margin-top: 8px; margin-left:15px">{{ row.entity.status == 0 ? "待提交" : (row.entity.status == 100 || row.entity.status == 2000) ? "审批中" : (row.entity.status == 1000 || row.entity.status == 3000) ? "审批通过" : (row.entity.status == 1100 || row.entity.status == 3500) ? "审批拒绝" : row.entity.status == 4000 ? "退回待修改" : row.entity.status }}</div>' },
            { field: 'applydate', displayName: '申请时间', width:'170px', cellFilter: "date:'yyyy-MM-dd HH:mm:ss'" },
            { field: 'fpname', displayName: '创建者', width:'120px' }
        ]
    };

    if($scope.searchType == 0){
        getMonthData();
    } else if($scope.searchType == 1){
        getMonthDataOnline();
    }


    // 点击radio按钮
    $scope.onChangeRadio = function () {
        if($scope.searchType == 0){
            $scope.statusList = [
                {name:'预审批状态', value:''},
                {name:'待提交', value:'0'},
                {name:'审批中', value:'100'},
                {name:'审批通过', value:'1100'},
                {name:'审批拒绝', value:'1000'}
            ];
            getMonthData();
        } else if($scope.searchType == 1){
            $scope.statusList = [
                {name:'在线申请状态', value:''},
                {name:'审批中', value:'2000'},
                {name:'审批通过', value:'3000'},
                {name:'审批拒绝', value:'3500'},
                {name:'退回待修改', value:'4000'}
            ];
            getMonthDataOnline();
        }
        $scope.status = $scope.statusList[0];
        $scope.search();

    };

    $scope.getPagedDataAsync = function (pageSize, page, searchText) {
        var startTime = $scope.beginTime;
        var endTime = $scope.endTime;

        var url = '/approvalData/approvalSearch?page=' + page + '&size=' + pageSize
                +'&status='+$scope.status.value
                +'&applyNum='+$scope.applyNum
                +'&fpName='+$scope.fpName
                +'&name='+$scope.name
                +'&searchType='+$scope.searchType
            ;
        if($scope.beginTime != ""){
            url+='&beginTime='+startTime;
        }
        if($scope.endTime != ""){
            url+='&endTime='+endTime;
        }
        $scope.$emit("BUSY");
        $http.get(encodeURI(url)).success(function (pagedata) {
            $scope.$emit("NOTBUSY");
            if (pagedata.status == 'SUCCESS') {
                $scope.codes = pagedata.data.content;
                $scope.totalServerItems = pagedata.data.totalElements;
                if ($scope.totalServerItems == '0') {
                    alert('无数据');
                }
            } else {
                alert(pagedata.error);
            }
        });
    };

    $scope.export = function (){
        $scope.beginTime = $("#beginTime").val();
        $scope.endTime = $("#endTime").val();

        var url = '/excel/approvalSearch?'
                +'status='+$scope.status.value
                +'&applyNum='+$scope.applyNum
                +'&fpName='+$scope.fpName
                +'&name='+$scope.name
                +'&searchType='+$scope.searchType
            ;
        if($scope.beginTime != ""){
            url+='&beginTime='+$scope.beginTime;
        }
        if($scope.endTime != ""){
            url+='&endTime='+$scope.endTime;
        }
        var popup  = $window.open("about:blank", "_blank");
        popup.location = encodeURI(url);
    }

    $scope.search = function(){
        $scope.beginTime = $("#beginTime").val();
        $scope.endTime = $("#endTime").val();
        if( $scope.pagingOptions.currentPage == 1){
            $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, '');
        }else{
            $scope.pagingOptions.currentPage = 1;
        }
    }

    $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, "");

    $scope.$on("$destroy", function() {
        $scope.$emit("NOTBUSY");
    })

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

    // 最近一六个月预审批申请量走势
    function getMonthData() {
        var url = '/approvalData/approvalCount?searchType=' + $scope.searchType;
        $http.get(encodeURI(url)).success(function (data) {
            if (data.status == 'SUCCESS') {
                $scope.monthData = data.data;
                initMonthData();
            } else {
                alert(data.error);
            }
        });
    }

    // 每月的申请量分布图
    function initMonthData() {
        //初始化
        var myChart = echarts.init(document.getElementById('monthData'));
        option = {
            title: {
                text: '近六个月预审批申请量走势',
                left: "center",
                top: 0,
                textStyle: {
                    fontSize: 18
                }
            },
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                top:25,
                right:20,
                data: ['审批通过', '审批拒绝']
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis:  {
                type: 'value'
            },
            yAxis: {
                type: 'category',
                data: $scope.monthData.monthArray
            },
            series: [
                {
                    name: '审批通过',
                    type: 'bar',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'insideRight'
                        }
                    },
                    data: $scope.monthData.passArray
                },
                {
                    name: '审批拒绝',
                    type: 'bar',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'insideRight'
                        }
                    },
                    data: $scope.monthData.refuseArray
                }
            ]
        };
        myChart.setOption(option,true);
    }

    // 最近六个月在线申请申请量走势
    function getMonthDataOnline() {
        var url = '/approvalData/approvalCount?searchType=' + $scope.searchType;
        $http.get(encodeURI(url)).success(function (data) {
            if (data.status == 'SUCCESS') {
                $scope.monthData = data.data;
                initMonthDataOnline();
            } else {
                alert(data.error);
            }
        });
    }

    // 每月的申请量分布图
    function initMonthDataOnline() {
        //初始化
        var myChart = echarts.init(document.getElementById('monthData'));
        option = {
            title: {
                text: '近六个月在线申请申请量走势',
                left: "center",
                top: 0,
                textStyle: {
                    fontSize: 18
                }
            },
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                top:25,
                right:20,
                data: ['审批通过', '审批拒绝','退回待修改']
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis:  {
                type: 'value'
            },
            yAxis: {
                type: 'category',
                data: $scope.monthData.monthArray,
                axisLabel: {
                    textStyle: {
                        fontSize: 8
                    }
                }
            },
            series: [
                {
                    name: '审批通过',
                    type: 'bar',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'insideRight'
                        }
                    },
                    data: $scope.monthData.passArray
                },
                {
                    name: '审批拒绝',
                    type: 'bar',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'insideRight'
                        }
                    },
                    data: $scope.monthData.refuseArray
                },
                {
                    name: '退回待修改',
                    type: 'bar',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'insideRight'
                        }
                    },
                    data: $scope.monthData.returnArray
                }
            ]
        };
        myChart.setOption(option,true);
    }

}])
;
