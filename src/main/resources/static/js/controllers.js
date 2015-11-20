'use strict';

/* Controllers */

angular.module('app.controllers', ['toaster', 'pascalprecht.translate', 'ngCookies'])

        .config(["$translateProvider", function ($translateProvider) {
                $translateProvider.translations('en', {
                    "app_name": "iSAPS",
                    "login_panel_header": "Sign In",
                    "username_placeholder": "Username",
                    "login_button": "Login",
                    "logout_button": "Log Out"
                });
                $translateProvider.translations('ru', {
                    "app_name": "iSAPS",
                    "login_panel_header": "Вход",
                    "username_placeholder": "Имя пользователя",
                    "login_button": "Войти",
                    "logout_button": "Выход"
                });
                $translateProvider.useCookieStorage();
                $translateProvider.preferredLanguage('en');
                $translateProvider.useSanitizeValueStrategy('escape');
            }])

        .controller('ChatController', ['$scope', '$interval', 'toaster', 'ChatSocket', function ($scope, $interval, toaster, chatSocket) {

                var typing = undefined;

                $scope.username = '';
                $scope.sendTo = 'everyone';
                $scope.participants = [];
                $scope.messages = [];
                $scope.newMessage = '';

                $scope.sendMessage = function () {
                    var destination = "/app/chat.message";

                    if ($scope.sendTo !== "everyone") {
                        destination = "/app/chat.private." + $scope.sendTo;
                        $scope.messages.unshift({content: $scope.newMessage, sender: $scope.username, private: true, to: $scope.sendTo});
                    }

                    chatSocket.send(destination, {}, JSON.stringify({sender: null, content: $scope.newMessage}));
                    $scope.newMessage = '';
                };

                $scope.broadcast = function (message) {
                    var destination = "/app/chat.broadcast";
                    chatSocket.send(destination, {}, JSON.stringify({sender: null, content: message}));
                };

                $scope.startTyping = function () {
                    // Don't send notification if we are still typing or we are typing a private message
                    if (angular.isDefined(typing) || $scope.sendTo !== "everyone")
                        return;

                    typing = $interval(function () {
                        $scope.stopTyping();
                    }, 500);

                    chatSocket.send("/topic/chat.typing", {}, JSON.stringify({username: $scope.username, typing: true}));
                };

                $scope.stopTyping = function () {
                    if (angular.isDefined(typing)) {
                        $interval.cancel(typing);
                        typing = undefined;

                        chatSocket.send("/topic/chat.typing", {}, JSON.stringify({username: $scope.username, typing: false}));
                    }
                };

                $scope.privateSending = function (username) {
                    $scope.sendTo = (username !== $scope.sendTo) ? username : 'everyone';
                };

                var initStompClient = function () {
                    chatSocket.init('/ws');

                    chatSocket.connect(function (frame) {

                        $scope.username = frame.headers['user-name'];

                        chatSocket.subscribe("/app/chat.participants", function (message) {
                            $scope.participants = JSON.parse(message.body);
                        });

                        chatSocket.subscribe("/topic/chat.login", function (message) {
                            var event = JSON.parse(message.body);
                            var username = event.target.username;
                            $scope.participants.unshift({username: username, typing: false});
                            $scope.broadcast(username + ' has joined the chat');
                        });

                        chatSocket.subscribe("/topic/chat.logout", function (message) {
                            var event = JSON.parse(message.body);
                            var username = event.target.username;
                            for (var index in $scope.participants) {
                                if ($scope.participants[index].username === username) {
                                    $scope.participants.splice(index, 1);
                                    $scope.broadcast(username + ' has left the chat');
                                }
                            }
                        });

                        chatSocket.subscribe("/topic/chat.typing", function (message) {
                            var parsed = JSON.parse(message.body);
                            if (parsed.username === $scope.username)
                                return;

                            for (var index in $scope.participants) {
                                var participant = $scope.participants[index];

                                if (participant.username === parsed.username) {
                                    $scope.participants[index].typing = parsed.typing;
                                }
                            }
                        });

                        chatSocket.subscribe("/topic/chat.message", function (message) {
                            $scope.messages.unshift(JSON.parse(message.body));
                        });

                        chatSocket.subscribe("/topic/chat.broadcast", function (message) {
                            $scope.messages.unshift(JSON.parse(message.body));
                        });

                        chatSocket.subscribe("/user/exchange/amq.direct/chat.message", function (message) {
                            var parsed = JSON.parse(message.body);
                            parsed.priv = true;
                            $scope.messages.unshift(parsed);
                        });

                        chatSocket.subscribe("/user/exchange/amq.direct/errors", function (message) {
                            toaster.pop('error', "Error", message.body);
                        });

                    }, function (error) {
                        toaster.pop('error', 'Error', 'Connection error ' + error);
                    });
                };

                initStompClient();

            }])

        .controller('NavbarController', ['$scope', '$translate', function ($scope, $translate) {

                $scope.logout = angular.isDefined($scope.logout) ? $scope.logout : true;

                $scope.setLocale = function (locale) {
                    $scope.locale = $translate.use(locale);
                };
                $scope.getLocale = function () {
                    return $translate.use();
                };
            }]);