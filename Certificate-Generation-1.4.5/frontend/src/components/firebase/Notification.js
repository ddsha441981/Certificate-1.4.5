import React, { useState, useEffect } from 'react';
import './Notification.css';

const Notification = ({ title, message, onClose }) => {
  const [isVisible, setIsVisible] = useState(false);

  useEffect(() => {
    if (title && message) {
      setIsVisible(true);
      const timer = setTimeout(() => {
        setIsVisible(false);
        if (onClose) onClose();
      }, 5000);

      return () => clearTimeout(timer);
    }
  }, [title, message, onClose]);

  if (!isVisible) return null;

  return (
    <div className="notification-container">
      <div className="notification-content">
        <h4 className="notification-title">{title}</h4>
        <p className="notification-body">{message}</p>
        <button className="close-btn" onClick={() => setIsVisible(false)}>Close</button>
      </div>
    </div>
  );
};

export default Notification;
