'use strict';

/* Directives */

angular.module('app.directives', [])

        .directive('navbar', function () {
            return {
                restrict: 'E',
                templateUrl: 'js/templates/navbar.directive.html',
                controller: 'NavbarController',
                scope: {
                    logout: '=?'
                }
            };
        })

        .directive('printMessage', function () {
            return {
                restrict: 'A',
                templateUrl: 'js/templates/message.directive.html'
            };
        });