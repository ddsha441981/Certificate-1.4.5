import React, { useEffect } from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { Provider } from 'react-redux';
import { store } from './components/redux/store';

const ServiceWorkerWrapper = () => {
  useEffect(() => {
    const registerServiceWorker = () => {
      if ('serviceWorker' in navigator) {
        navigator.serviceWorker
          .register('/firebase-messaging-sw.js') 
          .then((registration) => {
            console.log('Service Worker registered with scope:', registration.scope);
          })
          .catch((err) => {
            console.error('Service Worker registration failed: ', err);
          });
      }
    };

    
    registerServiceWorker();
  }, []);  

  return null;
};

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Provider store={store}>
      <App />
      <ServiceWorkerWrapper />
    </Provider>
  </React.StrictMode>
);

reportWebVitals();

