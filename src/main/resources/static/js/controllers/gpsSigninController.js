'use strict';

/* Controllers */
  // signin controller
app.controller('GpsSigninFormController', ['$scope', '$http', '$state', '$rootScope', '$cookies', '$localStorage', function($scope, $http, $state, $rootScope, $cookies, $localStorage) {

    $scope.user = {};
    $scope.authError = null;
    $scope.showMsg = false;
    $scope.getSmsCode = function(){
        $scope.showMsg = false;
        $http.post('/gps/sendCode?phoneNum=' + $scope.phoneNum).success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.showMsg = true;
            }else{
                $scope.authError = result.error;
            }
        });
    };

    $scope.login = function() {
        $scope.showMsg = false;
        $scope.authError = null;
        $http.post('/gps/verifyCode?phoneNum=' + $scope.phoneNum + '&code=' + $scope.code).success(function(result){
            if(result.status == 'SUCCESS'){
                var expireDate = new Date();
                expireDate.setDate(expireDate.getDate() + 1);
                $cookies.put('gpsUserInfoId', result.data.id, {'expires': expireDate});
                $cookies.put('gpsUserInfoPhoneNum', result.data.phoneNum, {'expires': expireDate});
                var url = ($localStorage.gpsUrl == null && $localStorage.gpsUrl == '') ? 'gps.activate' : $localStorage.gpsUrl;
                $state.go(url);
            }else{
                $scope.authError = result.error;
            }
        });
    };
  }])
;