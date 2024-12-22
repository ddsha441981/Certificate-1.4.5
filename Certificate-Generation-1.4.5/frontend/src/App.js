// import React, { useState } from 'react';
// import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
// import './components/myapp.css';
// import AdminLayout from './components/login/layouts/AdminLayout'
// import AuthLayout from './components/login/layouts/AuthLayout'
// import CandidateRegister from './components/CandidateRegister';
// import CandidateList from './components/CandidateList';
// import CompanyList from './components/CompanyList';
// import AddCompany from './components/AddCompany';
// import Dashboard from './components/Dashboard';
// import NotFound from './components/NotFound';
// import LoginPage from './components/login/LoginPage';
// import SignupPage from './components/login/SignupPage';
// import ForgetPassword from './components/login/ForgetPassword';
// import ChangePassword from './components/login/ChangePasword';

// import PushNotificationSetup from './components/firebase/NotificationSetup';
// import CandidateUpdate from './components/CandidateUpdate';
// import ViewCompany from './components/ViewCompany';
// import CalendarEvents from './components/CalendarEvents';
// import InterviewScheduler from './components/InterviewScheduler';
// import ViewScheduledInterview from './components/ViewScheduledInterview';
// import DefaultValueSalary from './components/salary/DefaultValueSalary';
// import ViewDefaultSalary from './components/salary/ViewDefaultSalary';
// import TaxSlab from './components/salary/TaxSlab';
// import ViewTaxSlab from './components/salary/ViewTaxSlab';
// import DeletedCandidateList from './components/DeletedCandidateList';
// import DeletedCompanyList from './components/DeletedCompanyList';
// import CandidatePendingStatusList from './components/CandidatePendingStatusList';
// import FolderView from './components/FolderView';
// import AppASender from './components/AppASender';
// import AdminProfile from './components/AdminProfile';
// import UserProfile from './components/user/UserProfile';

// function App() {
//     const [isLoggedIn, setIsLoggedIn] = useState(false);

//     const handleLogin = () => {
//         setIsLoggedIn(true);
//     };

//     const handleLogout = () => {
//         localStorage.removeItem('token');
//         setIsLoggedIn(false);
//     };

//     return (
//         <Router>
//             <Routes>
//                 {/* Authentication Pages - Use AuthLayout */}
//                 {!isLoggedIn && (
//                     <>
//                         <Route path="/" element={
//                             <AuthLayout>
//                                 <LoginPage onLogin={handleLogin} />
//                             </AuthLayout>
//                         } />
//                         <Route path="/signup" element={
//                             <AuthLayout>
//                                 <SignupPage onLogin={handleLogin} />
//                             </AuthLayout>
//                         } />
//                         <Route path="/forgotPassword" element={
//                             <AuthLayout>
//                                 <ForgetPassword onLogin={handleLogin} />
//                             </AuthLayout>
//                         } />
//                         <Route path="/changePassword/:userId" element={
//                             <AuthLayout>
//                                 <ChangePassword onLogin={handleLogin} />
//                             </AuthLayout>
//                         } />
//                         <Route path="/pushnotification" element={
//                             <AuthLayout>
//                                 <PushNotificationSetup onLogin={handleLogin} />
//                             </AuthLayout>
//                         } />
//                     </>
//                 )}

//                 {/* Admin Pages - Use AdminLayout for all admin pages */}
//                 {isLoggedIn && (
//                     <>
//                         {/* Redirect to dashboard if logged in and trying to access "/" */}
//                         <Route path="/" element={<Navigate to="/dashboard" />} />

//                         {/* Admin Layout pages */}
//                         <Route path="/dashboard" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <Dashboard />
//                             </AdminLayout>
//                         } />
//                         <Route path="/candidate" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <CandidateRegister />
//                             </AdminLayout>
//                         } />
//                         <Route path="/candidateList" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <CandidateList />
//                             </AdminLayout>
//                         } />
//                         <Route path="/companies" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <CompanyList />
//                             </AdminLayout>
//                         } />
//                         <Route path="/addCompany" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <AddCompany />
//                             </AdminLayout>
//                         } />
//                         <Route path="/viewCompany/:id" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <ViewCompany />
//                             </AdminLayout>
//                         } />
//                         <Route path="/candidateUpdate/:certificateId" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <CandidateUpdate />
//                             </AdminLayout>
//                         } />
//                         <Route path="/calenderEvent" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <CalendarEvents />
//                             </AdminLayout>
//                         } />
//                         <Route path="/interviewScheduler" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <InterviewScheduler />
//                             </AdminLayout>
//                         } />
//                         <Route path="/viewScheduledInterviews" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <ViewScheduledInterview />
//                             </AdminLayout>
//                         } />
//                         <Route path="/viewDeletedCandidateList" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <DeletedCandidateList />
//                             </AdminLayout>
//                         } />
//                         <Route path="/viewDeletedCompanyList" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <DeletedCompanyList />
//                             </AdminLayout>
//                         } />
//                         <Route path="/candidatePendingStatusList" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <CandidatePendingStatusList />
//                             </AdminLayout>
//                         } />
//                         <Route path="/folderView" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <FolderView />
//                             </AdminLayout>
//                         } />
//                         <Route path="/addDefaultSalary" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <DefaultValueSalary />
//                             </AdminLayout>
//                         } />
//                         <Route path="/viewToDefaultSalary" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <ViewDefaultSalary />
//                             </AdminLayout>
//                         } />
//                         <Route path="/addTaxSlab" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <TaxSlab />
//                             </AdminLayout>
//                         } />
//                         <Route path="/viewToTaxSlab" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <ViewTaxSlab />
//                             </AdminLayout>
//                         } />
//                         <Route path="/sendsms" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <AppASender />
//                             </AdminLayout>
//                         } />
//                         <Route path="/adminProfile" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <AdminProfile />
//                             </AdminLayout>
//                         } />
//                         <Route path="/userProfile/:id" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <UserProfile />
//                             </AdminLayout>
//                         } />
//                         <Route path="*" element={
//                             <AdminLayout isLoggedIn={isLoggedIn} onLogout={handleLogout}>
//                                 <NotFound />
//                             </AdminLayout>
//                         } />
//                     </>
//                 )}

//                 {/* Catch-all route for Not Found */}
//                 {!isLoggedIn && (
//                     <Route path="*" element={<Navigate to="/" />} />
//                 )}
//             </Routes>
//         </Router>
//     );
// }

// export default App;

















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
import UserHomePage from './components/user/UserProfile';
import AppASender from './components/AppASender';
import DefaultValueSalary from './components/salary/DefaultValueSalary';
import ViewTaxSlab from './components/salary/ViewTaxSlab';
import ViewDefaultSalary from './components/salary/ViewDefaultSalary';
import TaxSlab from './components/salary/TaxSlab';
import FolderView from './components/FolderView';
import AdminProfile from './components/AdminProfile';
import CandidatePendingStatusList from './components/CandidatePendingStatusList';
import DeletedCandidateList from './components/DeletedCandidateList';
import DeletedComapnyList from './components/DeletedCompanyList';
import UserProfile from './components/user/UserProfile';
import ViewCompany from './components/ViewCompany';
import CalendarEvents from './components/CalendarEvents';
import InterviewScheduler from './components/InterviewScheduler';
import ViewScheduledInterview from './components/ViewScheduledInterview';
import PushNotificationSetup from './components/firebase/NotificationSetup';
import TodayBookedInterview from './components/TodayBookedInterview';
import TodayCancelInterview from './components/TodayCancelInterview';

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
            <div id="layout-wrapper">
            <Navbars isLoggedIn={isLoggedIn} onLogout={handleLogout} />
            <Sidebar />
                
                            
                                <Routes>
                                    {!isLoggedIn && <Route path="/" element={<LoginPage onLogin={handleLogin} />} />}
                                    {!isLoggedIn && <Route path="/login" element={<LoginPage onLogin={handleLogin} />} />}
                                    {!isLoggedIn && <Route path="/signup" element={<SignupPage onLogin={() => setIsLoggedIn(true)} />} />}
                                    {!isLoggedIn && <Route path="/pushNotification" element={<PushNotificationSetup onLogin={() => setIsLoggedIn(true)} />} />}
                                    {!isLoggedIn && <Route path="/forgotPassword" element={<ForgetPassword onLogin={() => setIsLoggedIn(true)} />} />}
                                    {!isLoggedIn && <Route path="/changePassword/:userId" element={<ChangePassword onLogin={() => setIsLoggedIn(true)} />} />}
                                    {!isLoggedIn && <Route path="/adminProfile" element={<AdminProfile onLogin={() => setIsLoggedIn(true)} />} />}
                                    {/* Redirect to dashboard if logged in */}
                                    {isLoggedIn && <Route path="/" element={<Navigate to="/dashboard" />} />}

                                    <Route path="/userProfile/:id" element={<UserProfile />} />

                                    <Route path="/candidatePendingStatusList" element={<CandidatePendingStatusList />} />
                                    <Route path="/calenderEvent" element={<CalendarEvents />} />
                                    <Route path="/today_booked_interview" element={<TodayBookedInterview />} />
                                    <Route path="/today_cancel_interview" element={<TodayCancelInterview />} />
                                    <Route path="/interviewScheduler" element={<InterviewScheduler />} />
                                    <Route path="/viewScheduledInterviews" element={<ViewScheduledInterview />} />
                                    <Route path="/viewDeletedCandidateList" element={<DeletedCandidateList />} />
                                    <Route path="/viewDeletedComapnyList" element={<DeletedComapnyList />} />
                                    <Route path="/folderView" element={<FolderView />} />
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
                                    <Route path="/viewCompany/:id" element={<ViewCompany />} />
                                    <Route path="/script" element={<GoogleScript />} />
                                    <Route path="/candidateUpdate/:certificateId" element={<CandidateUpdate />} />
                                    <Route path="/updateCompany/:companyId" element={<UpdateCompany />} />
                                    <Route path="*" element={<NotFound />} />
                                </Routes>
                            
                        </div>
            </Router>
        </>
    );
}

export default App;
