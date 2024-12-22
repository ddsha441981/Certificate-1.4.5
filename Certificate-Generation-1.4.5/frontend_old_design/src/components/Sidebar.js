import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import "../components/myapp.css";

const Sidebar = () => {
    const [selectedItem, setSelectedItem] = useState('/dashboard');

    return (
        <>
            <div className="col main pt-5 mt-3">
                <div className="col-md-3 sidebar">
                    <ul>
                        <li className={`nav-item mb-2 ${selectedItem === '/dashboard' ? 'selected' : ''}`}>
                            <Link className="nav-link" to="/dashboard" onClick={() => setSelectedItem('/dashboard')}>
                                <i className="fas fa-home font-weight-bold"></i>
                                <span className="ml-3">Dashboard</span>
                            </Link>
                        </li>
                        <li className={`nav-item mb-2 ${selectedItem === '/companies' ? 'selected' : ''}`}>
                            <Link className="nav-link" to="/companies" onClick={() => setSelectedItem('/companies')}>
                                <i className="fas fa-building font-weight-bold"></i>
                                <span className="ml-3">Companies</span>
                            </Link>
                        </li>
                        <li className={`nav-item mb-2 ${selectedItem === '/candidate' ? 'selected' : ''}`}>
                            <Link className="nav-link" to="/candidate" onClick={() => setSelectedItem('/candidate')}>
                                <i className="fas fa-user font-weight-bold"></i>
                                <span className="ml-3">Register</span>
                            </Link>
                        </li>
                        <li className={`nav-item mb-2 ${selectedItem === '/candidateList' ? 'selected' : ''}`}>
                            <Link className="nav-link" to="/candidateList" onClick={() => setSelectedItem('/candidateList')}>
                                <i className="fas fa-users font-weight-bold"></i>
                                <span className="ml-3">Candidates</span>
                            </Link>
                        </li>


                        <li className={`nav-item mb-2 ${selectedItem === '/script' ? 'selected' : ''}`}>
                            <Link className="nav-link" to="/script" onClick={() => setSelectedItem('/script')}>
                                <i className="fas fa-code font-weight-bold"></i>
                                <span className="ml-3">Script</span>
                            </Link>
                        </li>


                        <li className={`nav-item mb-2 ${selectedItem === '/folderView' ? 'selected' : ''}`}>
                            <Link className="nav-link" to="/folderView" onClick={() => setSelectedItem('/folderView')}>
                                <i className="fas fa-code font-weight-bold"></i>
                                <span className="ml-3">folderView</span>
                            </Link>
                        </li>

                        

                        {/* <li className={`nav-item mb-2 ${selectedItem === '/viewToDefaultSalary' ? 'selected' : ''}`}>
                            <Link className="nav-link" to="/viewToDefaultSalary" onClick={() => setSelectedItem('/viewToDefaultSalary')}>
                                <i className="fas fa-dollar-sign font-weight-bold"></i>
                                <span className="ml-3">View Salary</span>
                            </Link>
                        </li>

                        <li className={`nav-item mb-2 ${selectedItem === '/viewToTaxSlab' ? 'selected' : ''}`}>
                            <Link className="nav-link" to="/viewToTaxSlab" onClick={() => setSelectedItem('/viewToTaxSlab')}>
                                <i className="fas fa-tax-sign font-weight-bold"></i>
                                <span className="ml-3">View Tax</span>
                            </Link>
                        </li> */}
                    </ul>
                </div>
            </div>
        </>
    );
}
export default Sidebar;
