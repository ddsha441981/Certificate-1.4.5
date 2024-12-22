import React from 'react';
import Sidebar from '../..//Sidebar';
import Navbars from '../../Navbars';

const AdminLayout = ({ children, onLogout, isLoggedIn }) => {
  return (
    <div id="layout-wrapper">
      <Navbars isLoggedIn={isLoggedIn} onLogout={onLogout} />
      <Sidebar />
      <div className="main-content">
        {children}
      </div>
    </div>
  );
};

export default AdminLayout;
