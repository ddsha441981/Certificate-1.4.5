import { initializeApp } from 'firebase/app';
import { getMessaging, getToken, onMessage } from 'firebase/messaging';

const firebaseConfig = {

    apiKey: "YOUR_API_KEY",
  
    authDomain: "fcm-certificate-notification.firebaseapp.com",
  
    projectId: "fcm-certificate-notification",
  
    storageBucket: "fcm-certificate-notification.firebasestorage.app",
  
    messagingSenderId: "YOUR_SENDER_ID",
  
    appId: "YOUR_APP_ID",
  
    measurementId: "YOUR_MEASUREMENT_ID"
  
  };
  
  

const app = initializeApp(firebaseConfig);

const messaging = getMessaging(app);

export { messaging, getToken, onMessage };
