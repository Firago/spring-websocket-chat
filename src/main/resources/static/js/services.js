'use strict';

/* Services */

angular.module('app.services', [])
        .factory('ChatSocket', ['$rootScope', function ($rootScope) {
                var stompClient;

                var wrappedSocket = {
                    init: function (url) {
                        var sock = new SockJS(url);
                        sock.onclose = function () {
                            console.log('close');
                        };
                        stompClient = Stomp.over(sock);
                    },
                    connect: function (successCallback, errorCallback) {

                        stompClient.connect({}, function (frame) {
                            $rootScope.$apply(function () {
                                successCallback(frame);
                            });
                        }, function (error) {
                            $rootScope.$apply(function () {
                                errorCallback(error);
                            });
                        });
                    },
                    subscribe: function (destination, callback) {
                        stompClient.subscribe(destination, function (message) {
                            $rootScope.$apply(function () {
                                callback(message);
                            });
                        });
                    },
                    send: function (destination, headers, object) {
                        stompClient.send(destination, headers, object);
                    }
                };

                return wrappedSocket;

            }]);