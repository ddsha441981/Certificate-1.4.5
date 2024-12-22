import { useEffect, useState } from 'react';

const UseInactivityLogout = (inactivityPeriod, onLogout) => {
  const [timer, setTimer] = useState(null);
  const [isActive, setIsActive] = useState(true); // Track user activity

  const resetTimer = () => {
    clearTimeout(timer);
    setTimer(setTimeout(logout, inactivityPeriod));
  };

  const logout = () => {
    localStorage.removeItem('token');
    onLogout();
  };

  useEffect(() => {
    const handleActivity = () => {
      resetTimer();
      setIsActive(true); // User is active
    };

    // Attach event listeners to track user activity
    window.addEventListener('mousemove', handleActivity);
    window.addEventListener('mousedown', handleActivity);
    window.addEventListener('keypress', handleActivity);
    window.addEventListener('scroll', handleActivity);

    resetTimer();

    // Clean up event listeners and timer on component unmount
    return () => {
      clearTimeout(timer);
      window.removeEventListener('mousemove', handleActivity);
      window.removeEventListener('mousedown', handleActivity);
      window.removeEventListener('keypress', handleActivity);
      window.removeEventListener('scroll', handleActivity);
    };
  }, [timer, onLogout]);

  // Handle logout only if the user is not active
  useEffect(() => {
    const idleTimer = setTimeout(() => {
      if (!isActive) {
        logout();
      }
    }, inactivityPeriod);

    return () => clearTimeout(idleTimer);
  }, [isActive, inactivityPeriod]);

  return null;
};

export default UseInactivityLogout;
