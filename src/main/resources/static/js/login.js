var dsiLogin = angular.module('loginModule', ['ngMaterial']);
 
dsiLogin.controller('LoginController', ['$scope','$mdDialog', 
                                     function($scope, $mdDialog){
     
//	  $scope.email = $cookies.get('email');
	  
      $scope.submit = function(){
//            $cookies.put('email', $scope.email);   
      }
}]);