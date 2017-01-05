'use strict';

angular.module('myApp.home', ['ngRoute', 'ui.bootstrap'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/home', {
        templateUrl: 'views/home.html',
        controller: 'HomeCtrl'
    });
}])

.controller('HomeCtrl', ['$scope', '$http', '$timeout', '$modal', function ($scope, $http, $timeout, $modal) {

    $scope.data = {
        item: { note: "", category: ""},
        items: null,
        comments: null
    };

    $scope.loadData = function() {
        myUtils.showPleaseWait();
        $http.get('/api/ToDoes').success(function (data) {
            $scope.data.items = data;
        }).finally(function(data) {
            myUtils.hidePleaseWait();
        });
    };

    $scope.addItem = function (item) {
        $http.post('/api/ToDoes', item).success(function (data) {
        }).finally(function (data) {
            $scope.loadData();
        });
    };
    
    $scope.showComments = function (gid) {
        $http.get('/api/ToDoComments', {params:{"gid": gid}})
        .success(function (data) {
            $scope.data.comments = data;
        }).finally(function(data) {
        });
    };

    $scope.loadData();

}]);