# spring-websocket-chat
Current version: 0.0.1

This chat is a simple WebSocket application based on Spring 4 on the back-end and AngularJS on the front-end. Spring WebSockets and SockJS are used for transfering STOMP messages over the channel between client and server (bidirectonally) with ability to subscribe on different message events. Bootstrap and basic responsive GUI design rules are used to ensure that site is displaying properly on all devices.

Demo version of this project is available at http://ws-chat.cfapps.io/

Tasks for 0.0.2 version:  
1) prevent login with same usernames  
2) change main page background (use [Trianglify library](http://qrohlf.com/trianglify/) instead of static bacground image)  
3) implement socket close mechanism on logout  
4) chat page i18n (currently only login page is translated in both english and russion)  
5) improve chat GUI

This project is a part of ASIS (Awesome Student Information System), which will replace old information system at my university because of its uselessness.
