import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import "../components/myapp.css";

const Sidebar = () => {
  const [expandedMenu, setExpandedMenu] = useState(null);

  const toggleMenu = (menu) => {
    if (expandedMenu === menu) {
      setExpandedMenu(null);  
    } else {
      setExpandedMenu(menu);
    }
  };

  return (
    <div className="vertical-menu">
      <div data-simplebar className="h-100">
        <div id="sidebar-menu">
          <ul className="metismenu list-unstyled" id="side-menu">
            <li className="menu-title" key="t-menu">Menu</li>

            <li>
              <Link to="dashboard">
                <i className="bx bx-home-circle"></i>
                <span key="t-dashboards">Dashboard</span>
              </Link>
            </li>

            <li className="menu-title" key="t-apps">Apps</li>

            {/* Company Menu */}
            <li>
              <a href="javascript:void(0);" className="has-arrow waves-effect" onClick={() => toggleMenu('company')}>
                <i className="bx bxs-building-house"></i>
                <span key="t-contacts">Company</span>
              </a>
              <ul className={`sub-menu ${expandedMenu === 'company' ? 'show' : ''}`} aria-expanded={expandedMenu === 'company'}>
                <li>
                  <Link to="companies" key="t-user-grid">Companies</Link>
                </li>
                <li>
                  <Link to="viewDeletedComapnyList" key="t-profile">Deleted Companies</Link>
                </li>
              </ul>
            </li>

            {/* Candidate Menu */}
            <li>
              <a href="javascript:void(0);" className="has-arrow waves-effect" onClick={() => toggleMenu('candidate')}>
                <i className='bx bx-terminal'></i>
                <span key="t-contacts">Candidate</span>
              </a>
              <ul className={`sub-menu ${expandedMenu === 'candidate' ? 'show' : ''}`} aria-expanded={expandedMenu === 'candidate'}>
                {/* <li>
                  <Link to="candidate" key="t-user-list">Register</Link>
                </li> */}
                <li>
                  <Link to="candidateList" key="t-profile">Candidates</Link>
                </li>
                <li>
                  <Link to="viewDeletedCandidateList" key="t-profile">Deleted Candidates</Link>
                </li>
                <li>
                  <Link to="folderView" key="t-profile">Folder View</Link>
                </li>
              </ul>
            </li>

            {/* Google Menu */}
            <li>
              <a href="javascript:void(0);" className="has-arrow waves-effect" onClick={() => toggleMenu('google')}>
                <i className="bx bxl-google-plus-circle"></i>
                <span key="t-contacts">Google</span>
              </a>
              <ul className={`sub-menu ${expandedMenu === 'google' ? 'show' : ''}`} aria-expanded={expandedMenu === 'google'}>
                <li>
                  <Link to="script" key="t-profile">Script</Link>
                </li>
              </ul>
            </li>

             {/* Interview Menu */}
             <li>
              <a href="javascript:void(0);" className="has-arrow waves-effect" onClick={() => toggleMenu('interview')}>
                {/* <i className="bx bxl-google-plus-circle"></i> */}
                <i class='bx bx-collapse'></i>
                <span key="t-contacts">Interview</span>
              </a>
              <ul className={`sub-menu ${expandedMenu === 'interview' ? 'show' : ''}`} aria-expanded={expandedMenu === 'interview'}>
                <li>
                  <Link to="calenderEvent" key="t-profile">Interview Event</Link>
                </li>
                <li>
                  
                {/* <li>
                  <Link to="interviewScheduler" key="t-profile">Interview Scheduler</Link>
                </li> */}

                <li>
                  <Link to="viewScheduledInterviews" key="t-profile">View Events </Link>
                </li>
            </li>
              </ul>
            </li>

           

          </ul>
        </div>
      </div>
    </div>
  );
};

export default Sidebar;