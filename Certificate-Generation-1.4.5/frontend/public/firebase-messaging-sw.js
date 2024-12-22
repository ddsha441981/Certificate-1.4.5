// self.importScripts('https://www.gstatic.com/firebasejs/9.6.1/firebase-app.js');
// self.importScripts('https://www.gstatic.com/firebasejs/9.6.1/firebase-messaging.js');

// self.importScripts('/firebase-sdk/firebase-app.js');
// self.importScripts('/firebase-sdk/firebase-messaging.js');

self.importScripts('https://www.gstatic.com/firebasejs/8.10.0/firebase-app.js');
self.importScripts('https://www.gstatic.com/firebasejs/8.10.0/firebase-messaging.js');


const firebaseConfig = {

  apiKey: "YOUR_API_KEY",

  authDomain: "fcm-certificate-notification.firebaseapp.com",

  projectId: "fcm-certificate-notification",

  storageBucket: "fcm-certificate-notification.firebasestorage.app",

  messagingSenderId: "YOUR_SENDER_ID",

  appId: "YOUR_APP_ID",

  measurementId: "G-YOUR_MEASUREMENT_ID"

};




firebase.initializeApp(firebaseConfig);

const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) => {
  console.log('[firebase-messaging-sw.js] Background message received. ', payload);

  const notificationTitle = payload.notification.title;
  const notificationOptions = {
    body: payload.notification.body,
    icon: payload.notification.icon || '/default-icon.png',
  };

  self.registration.showNotification(notificationTitle, notificationOptions);
});
