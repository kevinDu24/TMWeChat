app.controller('gpsActivateController', ['toaster', '$window', '$scope', '$state', '$http', '$rootScope', '$modal', '$cookies', '$location', '$localStorage', '$stateParams', function(toaster, $window, $scope,$state, $http, $rootScope, $modal, $cookies, $location, $localStorage, $stateParams) {

    $localStorage.gpsUrl = 'gps.activate';
    if($cookies.get('gpsUserInfoId') == null || $cookies.get('gpsUserInfoId') == ''){
        $state.go('gps.signin');
    }
    var userInfo = {id:$cookies.get('gpsUserInfoId'), phoneNum:$cookies.get('gpsUserInfoPhoneNum')};
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };
    $scope.uploading = false;

    function getLocation(){
        $window.wx.ready(function(){
            $window.wx.getLocation({
                type: 'gcj02', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
                success: function (res) {
                    $scope.device.lat = res.latitude;
                    $scope.device.lon = res.longitude;
                    $http.get('/maps?latitude='+res.latitude+'&longitude='+res.longitude).success(function(data){
                        $scope.positions = data.result.pois;
                        $scope.position = $scope.positions[0];
                    });
                }
            });
        });

    }

    function init(){
        $scope.loading = false;
        $scope.device = {fileIds:[]};
        $scope.gpsTypes = [{name:'有线GPS追踪器', type:'1'}, {name:'无线GPS追踪器', type:'2'}];
        $scope.gps = $scope.gpsTypes[0];
        $scope.images = [];
        getLocation();
    }

    init();

    function uploadImage(id){
        $window.wx.uploadImage({
            localId: id, // 需要上传的图片的本地ID，由chooseImage接口获得
            isShowProgressTips: 1, // 默认为1，显示进度提示
            success: function (result) {
                $scope.device.fileIds.push(result.serverId);
                //$scope.pop('success', '', '上传成功');
                alert("上传成功");
            }
        });
    }

    $scope.getPhoto = function(){
        if($scope.images.length >= 6){
            $scope.pop('error', '', '拍照数量最多为6张');
            return;
        }
        $window.wx.chooseImage({
            count: 1, // 默认9
            sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
            sourceType: ['camera'], // 可以指定来源是相册还是相机，默认二者都有
            success: function (res) {
                setTimeout(function(){
                    uploadImage(res.localIds[0]);
                    $scope.images.push(res.localIds[0]); // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                    $scope.$digest();//通知视图模型的变化
                }, 100);
            }
        });
    };

    $scope.submit = function(){
        //$scope.device.lat = '31';
        //$scope.device.lon = '117';
        //$scope.device.addr = '新华国际广场';
        if($scope.device.fileIds.length > 6){
            $scope.pop('error', '', '拍照数量最多为6张');
            return;
        }
        if($scope.device.fileIds.length == 0){
            $scope.pop('error', '', '请上传拍照');
            return;
        }
        if($scope.device.fileIds.length < 3){
            $scope.pop('error', '', '拍照数量最少为3张');
            return;
        }
        var temp = $scope.device.vin + '';
        var vinTemp = temp;
        for(var i=0; i<(6-temp.length); i++){
            vinTemp = '0'+vinTemp;
        }
        $scope.device.vin = vinTemp;
        $scope.loading = true;
        $scope.device.addr = $scope.position.title;
        $scope.device.type = $scope.gps.type;
        $scope.device.installPerson = {id:userInfo.id, phoneNum:userInfo.phoneNum};

        $scope.myPromise = $http.post('/gps/devices', $scope.device).success(function(result){
            if(result.status == 'SUCCESS'){
                $state.go('gps.activateQueryDetails');
            }else{
                $scope.pop('error', '', result.error);
            }
            $scope.loading = false;
        }).error(function(){
            $scope.loading = false;
            $scope.pop('error', '', '系统故障');
        });
    };

    $scope.deletePhoto = function(index){
        $scope.images = handleArray(index, $scope.images);
        $scope.device.fileIds = handleArray(index, $scope.device.fileIds);
        $scope.$digest();//通知视图模型的变化
    };

    function handleArray(index, array){
        var temp = [];
        for(var i in array){
            if(i != index){
                temp.push(array[i]);
            }
        }
        return temp;
    }

    $scope.showExampleImg = function(){
        $window.wx.previewImage({
            current: 'http://wx.xftm.com:89/device/安装示例图-车正面.jpg', // 当前显示图片的http链接
            urls: ['http://wx.xftm.com:89/device/安装示例图-车正面.jpg', 'http://wx.xftm.com:89/device/安装示例图-车架号.jpg', 'http://wx.xftm.com:89/device/安装示例图-设备安装位置图.jpg'] // 需要预览的图片http链接列表
        });
    };
}])
;
