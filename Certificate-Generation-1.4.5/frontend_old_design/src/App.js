import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import './components/myapp.css';
import Sidebar from './components/Sidebar';
import CandidateRegister from './components/CandidateRegister';
import CandidateList from './components/CandidateList';
import Navbars from './components/Navbars';
import CompanyList from './components/CompanyList';
import AddCompany from './components/AddCompany';
import GoogleScript from './components/GoogleScript';
import Dashboard from './components/Dashboard';
import CandidateUpdate from './components/CandidateUpdate';
import NotFound from './components/NotFound';
import 'react-toastify/dist/ReactToastify.css';
import UpdateCompany from './components/UpdateCompany';
import LoginPage from './components/login/LoginPage';
import SignupPage from './components/login/SignupPage';
import ForgetPassword from './components/login/ForgetPassword';
import ChangePassword from './components/login/ChangePasword';
import UserHomePage from './components/user/UserHomePage';
import AppASender from './components/AppASender';
import DefaultValueSalary from './components/salary/DefaultValueSalary';
import ViewTaxSlab from './components/salary/ViewTaxSlab';
import ViewDefaultSalary from './components/salary/ViewDefaultSalary';
import TaxSlab from './components/salary/TaxSlab';

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);


    const handleLogin = () => {
        setIsLoggedIn(true);
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        setIsLoggedIn(false);
    };


    return (
        <>
            <Router>
                <div className="col main pt-5 margin-dashboard">
                    <div className="container-fluid">
                        <div className="row ">

                            <Navbars isLoggedIn={isLoggedIn} onLogout={handleLogout} />

                            <Sidebar />
                            <div className="col-md-9 main-content">
                                <Routes>
                                   
                                    {/* {!isLoggedIn && <Route path="/*" element={<LoginPage onLogin={() => setIsLoggedIn(true)} />} />}
                                    {!isLoggedIn && <Route path="/signup" element={<SignupPage onLogin={() => setIsLoggedIn(true)} />} />}
                                    {!isLoggedIn && <Route path="/forgotPassword" element={<ForgetPassword onLogin={() => setIsLoggedIn(true)} />} />}
                                    {!isLoggedIn && <Route path="/changePassword/:userId" element={<ChangePassword onLogin={() => setIsLoggedIn(true)} />} />} */}
                                    {/* <Route path="/" element={isLoggedIn ? (<Navigate to="/dashboard" />) : (<LoginPage onLogin={handleLogin} />)}/> */}
                                    {!isLoggedIn && <Route path="/" element={<LoginPage onLogin={handleLogin} />} />}
                                    {!isLoggedIn && <Route path="/signup" element={<SignupPage onLogin={() => setIsLoggedIn(true)} />} />}
                                    {!isLoggedIn && <Route path="/forgotPassword" element={<ForgetPassword onLogin={() => setIsLoggedIn(true)} />} />}
                                    {!isLoggedIn && <Route path="/changePassword/:userId" element={<ChangePassword onLogin={() => setIsLoggedIn(true)} />} />}
    
                                    {/* Redirect to dashboard if logged in */}
                                    {isLoggedIn && <Route path="/" element={<Navigate to="/dashboard" />} />}

                                    <Route path="/addDefaultSalary" element={<DefaultValueSalary />} />
                                    <Route path="/viewToDefaultSalary" element={<ViewDefaultSalary />} />
                                    <Route path="/addTaxSlab" element={<TaxSlab />} />
                                    <Route path="/viewToTaxSlab" element={<ViewTaxSlab />} />
                                    <Route path="/sendsms" element={<AppASender />} />
                                    <Route path="/dashboard" element={<Dashboard />} />
                                    <Route path="/userhomepage" element={<UserHomePage />} />
                                    <Route path="/candidate" element={<CandidateRegister />} />
                                    <Route path="/candidateList" element={<CandidateList />} />
                                    <Route path="/companies" element={<CompanyList />} />
                                    <Route path="/addCompany" element={<AddCompany />} />
                                    <Route path="/script" element={<GoogleScript />} />
                                    <Route path="/candidateUpdate/:certificateId" element={<CandidateUpdate />} />
                                    <Route path="/updateCompany/:companyId" element={<UpdateCompany />} />
                                    <Route path="/folderView" element={<FolderView />} />
                                    <Route path="*" element={<NotFound />} />
                                </Routes>
                               
                            </div>
                        </div>
                    </div>
                </div>
            </Router>
        </>
    );
}

export default App;
