var app = angular.module('myApp', []);
app.controller('documentsCtrl', function($scope, $http) {
    $http.get("/api/VasyaPupkn/documents/get").then(function (response) {
        $scope.docs = response.data.records;
    });
});