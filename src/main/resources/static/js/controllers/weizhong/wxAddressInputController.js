app.controller('wxAddressInputController',[ 'toaster','$scope', '$state', '$http', '$modal', '$window', '$location','$localStorage','$cookies', function(toaster,$scope,$state,$http,$modal,$window,$location,$localStorage,$cookies ) {

    $scope.applyNum = $location.search().applyNum;
    if(!$scope.applyNum){
        alert('申请编号不可为空');
        return;
    }
    $scope.showMsg = '获取验证码';
    $scope.state = {};
    $scope.coreProvince = {};
    $scope.coreCity = {};
    $scope.city = {};
    $scope.provinces = [];
    $scope.cities = [];
    function getUserInfo(){
        $http.get('/apply/getAddressInputInfo?applyNum=' + $scope.applyNum).success(function(data){
            if(data.status == 'SUCCESS'){
                $scope.user = data.data;
                if($scope.user.province){
                   $scope.coreProvince.name = $scope.user.province;
                   $scope.coreCity.name = $scope.user.city;
                    getProvinces();
                }
            }else{
                alert('查询信息失败' + data.error);
                return;
            }
        }).error(function(){
                alert('获取用户信息错误，请稍后再试');
                return;
        });
    };
    var cities = [];
    function getProvinces(){
        $http.get('/json/province.json').success(function(result){
            $scope.provinces = result;
            if($scope.coreProvince.name){
                $scope.province.name = $scope.coreProvince.name;
                $scope.provinces.push($scope.province);
            }else{
                $scope.province = $scope.provinces[0];
            }
//            for (var i = 0;i < $scope.provinces.length; i++ ) {
//                if($scope.coreProvince.name == $scope.provinces[i].name) {
//                   $scope.province = $scope.provinces[i];
//                }
//            }
            getCities();
        });
    }
    function getCities(){
        if(cities.length == 0){
            $http.get('/json/city.json').success(function(result){
                $scope.cities = [];
                if($scope.coreCity.name){
                    $scope.city.name = $scope.coreCity.name;
                    $scope.cities.push($scope.city);
                }else{
                    for(var i in result){
                        if(result[i].ProID == $scope.province.ProID){
                            $scope.cities.push(result[i]);
                        }
                    }
                    $scope.city = $scope.cities[0];
                }
                cities = result;
            });
        }else{
            $scope.cities = [];
            if($scope.coreCity.name){
                $scope.city.name = $scope.coreCity.name;
                $scope.cities.push($scope.city);
            }else{
                for(var i in cities){
                    if(cities[i].ProID == $scope.province.ProID){
                        $scope.cities.push(cities[i]);
                    }
                }
                $scope.city = $scope.cities[0];
            }
        }
    }
    function init(){
      getUserInfo();
      getProvinces();
    }
    init();


    $scope.optionChange = function(){
//       for (var i = 0;i < $scope.provinces.length; i++ ) {
//           if($scope.coreProvince.name == $scope.provinces[i].name) {
//              $scope.province = $scope.provinces[i];
//           }
//       }
       $scope.coreCity = {};
       $scope.coreProvince = {};
       $scope.cities = [];
       getCities();
    };
    //申请提交
    $scope.submit = function(){
        $scope.user.applyNum = $scope.applyNum;
        $scope.user.province = $scope.province.name;
        $scope.user.city = $scope.city.name;
        $scope.state.notShow = true;
        $scope.$emit("BUSY");
        $http.post('/apply/submitAddressInputInfo',$scope.user).success(function(data){
            $scope.state.notShow = false;
            $scope.$emit("NOTBUSY");
            if(data.status == 'SUCCESS'){
                alert("提交成功!");
                $scope.user = {};
            }else{
                alert("提交失败" + data.error);
                return;
            }
        }).error(function(){
            $scope.state.notShow = false;
            $scope.$emit("NOTBUSY");
            alert('提交失败，请稍后再试');
        });
    };
}])
;
