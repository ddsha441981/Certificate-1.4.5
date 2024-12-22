import { useDispatch, useSelector } from "react-redux";
import { fetchLoggedInUserDetails } from "./redux/userSlice";
import { useEffect } from "react";
import Breadcrumb from "./Breadcrumb";

const AdminProfile = () =>{
    
    const breadcrumbItems = [
        { name: 'Dashboard', link: '/dashboard', active: false },
        { name: `Admin Profile`, link: `/adminProfile`, active: true }
    ];
    //Redux
const dispatch = useDispatch();
const { fullName,firstName, lastName, gender,role,email,dob} = useSelector((state) => state.user);

useEffect(() => {
  const userId = localStorage.getItem('userId');
  if (userId) {
    dispatch(fetchLoggedInUserDetails(userId));
  }
}, [dispatch]);

    
    return(
        <>
             <div className="main-content">
                <div className="page-content">
                    <div className="container-fluid">
                        <Breadcrumb title="Dashboard" breadcrumbItems={breadcrumbItems} />
                        <div>
                        <p>TODO: Make Changes to your Profile as per your need and design it as you want</p>
            <div>
                <h1>Welcome, {fullName}</h1>
                <p>Gender: {gender}</p>
                <p>Role: {role}</p>
                <p>Email: {email}</p>
                <p>Date of Birth: {dob}</p>
                <p>First Name: {firstName}</p>
                <p>Last Name: {lastName}</p>
            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default AdminProfile;