angular.module('starter', ['ionic', 'starter.controllers', 'starter.services'])
	.run(function($ionicPlatform) {
		$ionicPlatform.ready(function() {
			// Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
			// for form inputs)
			if(window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
				cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
				cordova.plugins.Keyboard.disableScroll(true);

			}
			if(window.StatusBar) {
				// org.apache.cordova.statusbar required
				StatusBar.styleDefault();
			}
		});
	})
	.config(function($stateProvider, $urlRouterProvider, $ionicConfigProvider) {

		/*用于修改安卓tab居下 （在参数里要加入$ionicConfigProvider）*/
		$ionicConfigProvider.platform.ios.tabs.style('standard');
		$ionicConfigProvider.platform.ios.tabs.position('bottom');
		$ionicConfigProvider.platform.android.tabs.style('standard');
		$ionicConfigProvider.platform.android.tabs.position('standard');

		$ionicConfigProvider.platform.ios.navBar.alignTitle('center');
		$ionicConfigProvider.platform.android.navBar.alignTitle('left');

		$ionicConfigProvider.platform.ios.backButton.previousTitleText('').icon('ion-ios-arrow-thin-left');
		$ionicConfigProvider.platform.android.backButton.previousTitleText('').icon('ion-android-arrow-back');

		$ionicConfigProvider.platform.ios.views.transition('ios');
		$ionicConfigProvider.platform.android.views.transition('android');
		/*用于修改安卓tab居下 --结束*/
		$ionicConfigProvider.views.swipeBackEnabled(false)//用于禁止ios侧滑返回上一页面
		
		$stateProvider
			.state('tab', { //父级页面路径
				url: '/tab', //本级页面路径
				abstract: true, //是否自动跳转
				templateUrl: 'templates/tabs.html' //模板路径
			})
			.state('tab.dash', {
				url: '/dash',
				cache: false, 
				views: { 
					'tab-dash': {
						templateUrl: 'templates/tab-dash.html',
						controller: 'DashCtrl'
					}
				}
			})
			.state('tab.room1', {
				url: '/room1',
				cache: false, 
				views: { 
					'tab-dash': { 
						templateUrl: 'templates/tab-room1.html',
						controller: 'Room1Ctrl'
					}
				}
			})
		$urlRouterProvider.otherwise('/tab/dash');

	});