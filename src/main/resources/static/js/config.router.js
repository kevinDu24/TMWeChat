'use strict';

/**
 * Config for the router
 */
angular.module('app')
  .run(
    [          '$rootScope', '$state', '$stateParams',
      function ($rootScope,   $state,   $stateParams) {
          $rootScope.$state = $state;
          $rootScope.$stateParams = $stateParams;        
      }
    ]
  )
  .config(
    [          '$stateProvider', '$urlRouterProvider', 'JQ_CONFIG', 'MODULE_CONFIG', '$compileProvider',
      function ($stateProvider,   $urlRouterProvider, JQ_CONFIG, MODULE_CONFIG, $compileProvider) {
          var layout = "tpl/app.html";
          $compileProvider.imgSrcSanitizationWhitelist(/^\s*(http|https|data|wxlocalresource|weixin):/);
          if(window.location.href.indexOf("material") > 0){
            layout = "tpl/blocks/material.layout.html";
            $urlRouterProvider
              .otherwise('/app/dashboard-v3');
          }else{
            $urlRouterProvider
              .otherwise('/access/signin');
          }
          
          $stateProvider
              .state('app', {
                  abstract: true,
                  url: '/app',
                  templateUrl: layout
              })
              .state('access', {
                  url: '/access',
                  template: '<div ui-view></div>'
              })
              //登录
              .state('access.signin', {
                  url: '/signin',
                  templateUrl: 'tpl/page_signin.html',
                  resolve: load( ['js/controllers/signin.js'] )
              })
              //gps menu
              .state('gps', {
                  url: '/gps',
                  template: '<div ui-view></div>'
              })
              //gps 安装激活登录
              .state('gps.signin', {
                  url: '/signin',
                  templateUrl: 'tpl/gps_signin.html',
                  resolve: load( ['js/controllers/gpsSigninController.js'] )
              })
              //gps 激活
              .state('gps.activate', {
                  url: '/activate',
                  templateUrl: 'tpl/gps_activate.html',
                  resolve: load( ['toaster', 'js/controllers/gpsActivateController.js', 'js/controllers/gpsActivateAlertController.js'] )
              })
              .state('gps.activateQueryDetails', {
                  url: '/activateQueryDetails',
                  templateUrl: 'tpl/gps_activateQueryDetails.html',
                  resolve: load( ['toaster', 'js/controllers/gpsActivateQueryDetailsController.js', 'js/controllers/gpsActivateQueryPromptController.js'] )
              })
              .state('gps.tutorial', {
                  url: '/tutorial',
                  templateUrl: 'tpl/gps_tutorial.html',
                  resolve: load( ['toaster', 'js/controllers/gpsTutorialController.js'] )
              })
              .state('gps.sendMsg', {
                  url: '/sendMsg',
                  templateUrl: 'tpl/send_message.html',
                  resolve: load( ['toaster', 'js/controllers/sendMsgController.js'] )
              })
              .state('gps.msgHistory', {
                  url: '/msgHistory',
                  templateUrl: 'tpl/gps_msgHistory.html',
                  resolve: load( ['toaster', 'ngGrid', 'js/controllers/msgHistoryController.js', 'js/controllers/msgDetailsController.js'] )
              })
              .state('gps.msglist', {
                  url: '/msglist/:terminalId',
                  templateUrl: 'tpl/gps_msglist.html',
                  resolve: load( ['toaster', 'ngGrid', 'js/controllers/msgListController.js'] )
              })
              .state('gps.dismantle', {
                  url: '/dismantle',
                  templateUrl: 'tpl/gps_dismantle.html',
                  resolve: load( ['toaster', 'ngGrid', 'js/controllers/gpsDismantledController.js'] )
              })
              .state('gps.dismantleList', {
                  url: '/dismantleList',
                  templateUrl: 'tpl/gps_dismantleList.html',
                  resolve: load( ['toaster', 'ngGrid', 'js/controllers/gpsDismantleListController.js'] )
              })
              //HPL menu
              .state('app.hpl', {
                  url: '/ui',
                  template: '<div ui-view></div>'
              })
              //默认页面
              .state('app.hpl.default', {
                  url: '/default',
                  templateUrl: 'tpl/hpl_default.html',
                  resolve: load(["js/controllers/defaultController.js"])
              })
              //合同查询 - 申请状态列表
              .state('app.hpl.apply', {
                  url: '/apply',
                  templateUrl: 'tpl/hpl_apply.html',
                  resolve: load(['js/controllers/contractSearchController.js', 'js/controllers/applyController.js'])
              })
              //合同查询 - 还款计划列表
              .state('app.hpl.repayment', {
                  url: '/repayment',
                  templateUrl: 'tpl/hpl_repayment.html',
                  resolve: load(['js/controllers/contractSearchController.js', 'js/controllers/repaymentController.js'])
              })
              //合同查询 - 申请状态详情
              .state('app.hpl.applyDetails', {
                  url: '/applyDetails/:target',
                  templateUrl: 'tpl/hpl_applyDetails.html',
                  resolve: load(['js/controllers/applyDetailsController.js'])
              })
              //合同查询 - 还款计划详情
              .state('app.hpl.repaymentDetails', {
                  url: '/repaymentDetails',
                  templateUrl: 'tpl/hpl_repaymentDetails.html',
                  resolve: load(['js/controllers/repaymentDetailsController.js'])
              })
              //销售查询 - 人员统计
              .state('app.hpl.market', {
                  url: '/market',
                  templateUrl: 'tpl/hpl_market.html',
                  resolve: load(['js/controllers/marketController.js', 'js/controllers/areaSelectorController.js'])
              })
              //销售查询 - 申请量统计
              .state('app.hpl.applyNum', {
                  url: '/applyNum',
                  templateUrl: 'tpl/hpl_applyNum.html',
                  resolve: load(['js/controllers/applyNumController.js', 'js/controllers/areaSelectorController.js'])
              })
              //销售查询 - CA人员审核量统计
              .state('app.hpl.auditor', {
                  url: '/auditor',
                  templateUrl: 'tpl/hpl_auditor.html',
                  resolve: load(['js/controllers/auditorController.js', 'js/controllers/areaSelectorController.js'])
              })
              //销售查询 - 合同统计
              .state('app.hpl.contract', {
                  url: '/contract',
                  templateUrl: 'tpl/hpl_contract.html',
                  resolve: load(['js/controllers/contractController.js', 'js/controllers/areaSelectorController.js'])
              })
              //销售查询 - 融资额统计
              .state('app.hpl.financing', {
                  url: '/financing',
                  templateUrl: 'tpl/hpl_financing.html',
                  resolve: load(['js/controllers/financingController.js', 'js/controllers/areaSelectorController.js'])
              })
              //先锋自动化信审决策报告
              .state('app.gxbReport', {
                  url: '/gxbReport',
                  templateUrl: 'tpl/gxb_report.html',
                  resolve: load(['js/controllers/gxbReport.js'])
              })
              //销售查询 - 销售计划（申请量）
              .state('app.hpl.marketApply', {
                  url: '/marketApply',
                  templateUrl: 'tpl/hpl_marketApply.html',
                  resolve: load(['js/controllers/marketApplyController.js', 'js/controllers/areaSelectorController.js'])
              })
              //销售查询 - 销售计划（合同量）
              .state('app.hpl.marketcontract', {
                  url: '/marketcontract',
                  templateUrl: 'tpl/hpl_marketcontract.html',
                  resolve: load(['js/controllers/marketcontractController.js', 'js/controllers/areaSelectorController.js'])
              })
              //审批申请量统计
              .state('app.applyReport', {
                  url: '/applyReport',
                  templateUrl: 'tpl/hpl_apply_report.html',
                  resolve: load(["ngGrid", 'js/controllers/applyReport.js'])
              })
              //产品详情
              .state('app.hpl.contractDetails', {
                  url: '/contractDetails',
                  templateUrl: 'tpl/hpl_contractDetails.html',
                  resolve: load(['js/controllers/contractDetailsController.js'])
              })
              //在线助力融微众一审
              .state('app.wzApply', {
                url: '/wzApply',
                templateUrl: 'tpl/weizhong/wx_wzApply.html',
                resolve: load( ['toaster','js/controllers/weizhong/wzApply.js', "js/controllers/weizhong/wxApplyFinanceController.js","js/controllers/weizhong/wxStatementController.js"] )
              })
              //四要素验证表单
              .state('app.factorVerification', {
                 url: '/factorVerification',
                 templateUrl: 'tpl/weizhong/wx_factorVerification.html',
                 resolve: load( ['toaster','js/controllers/fileModel.js','js/controllers/weizhong/wxFactorVerificationController.js','js/controllers/weizhong/wxMsgCodeController.js'] )
              })
              //通用四要素验证
              .state('app.allFactorVerification', {
                 url: '/allFactorVerification',
                 templateUrl: 'tpl/weizhong/wx_allFactorVerification.html',
                 resolve: load( ['toaster','js/controllers/fileModel.js','js/controllers/weizhong/wxAllFactorVerificationController.js','js/controllers/weizhong/wxMsgCodeController.js'] )
              })
              //放款卡收入地址录入
              .state('app.addressInput', {
                url: '/addressInput',
                templateUrl: 'tpl/weizhong/wx_addressInput.html',
                resolve: load( ['toaster','js/controllers/weizhong/wxAddressInputController.js'] )
              })
              //车辆信息填写
              .state('app.userCarAnalysis', {
                url: '/userCarAnalysis',
                templateUrl: 'tpl/weizhong/wx_usedCarAnalysis.html',
                resolve: load( ['toaster','js/controllers/weizhong/wxUsedCarAnalysisController.js'] )
              });
          function load(srcs, callback) {
            return {
                deps: ['$ocLazyLoad', '$q',
                  function( $ocLazyLoad, $q ){
                    var deferred = $q.defer();
                    var promise  = false;
                    srcs = angular.isArray(srcs) ? srcs : srcs.split(/\s+/);
                    if(!promise){
                      promise = deferred.promise;
                    }
                    angular.forEach(srcs, function(src) {
                      promise = promise.then( function(){
                        if(JQ_CONFIG[src]){
                          return $ocLazyLoad.load(JQ_CONFIG[src]);
                        }
                        angular.forEach(MODULE_CONFIG, function(module) {
                          if( module.name == src){
                            name = module.name;
                          }else{
                            name = src;
                          }
                        });
                        return $ocLazyLoad.load(name);
                      } );
                    });
                    deferred.resolve();
                    return callback ? promise.then(function(){ return callback(); }) : promise;
                }]
            }
          }


      }
    ]
  ).config(['$httpProvider', function($httpProvider) {
        //Handle 401 Error
        $httpProvider.interceptors.push(function($q, $injector) {
            return {
                response: function(response){
                    return response || $q.when(response);
                },
                responseError: function(rejection){
                    if(rejection.status === 401){
                        var state = $injector.get('$state');
                        state.go("access.signin");
                    }
                    return $q.reject(rejection);
                }
            };
        });
    }]);
