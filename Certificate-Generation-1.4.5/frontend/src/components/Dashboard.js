import { API_BASE_URL, API_BASE_URL_COMPANY, API_BASE_URL_COMPANY_WISE_COUNT_UPDATE, API_BASE_URL_INTERVIEW_BOOK_COUNT,API_BASE_URL_INTERVIEW_CANCEL_COUNT} from '../env';
import React, { useEffect , useState} from 'react';
import axios from 'axios';
import RecentList from './RecentList';
// import Teams from './Teams';
// import ContactUs from './ContactUs';
import { ToastContainer } from 'react-toastify';
import { showErrorNotification } from '../notification/Notification';
import ActivityLog from './ActivityLog';
import { useDispatch, useSelector } from 'react-redux';
import { fetchLoggedInUserDetails } from './redux/userSlice';
import { Link } from 'react-router-dom';
import Teams from './Teams';
import Birthday from './Birthday';
import Breadcrumb from './Breadcrumb';
import GenderCount from './GenderCount';


const Dashboard = () => {

const [candidateSize, setCandidateSize] = useState(0);
const [companySize, setCompanySize] = useState(0);
const [companyWiseCount, setCompanyWiseCount] = useState([]);
const [todayInterviewBookedCount,setTodayInterviewBookedCount]= useState([]);
const [todayInterviewCancelCount,setTodayInterviewCancelCount]= useState([]);

const breadcrumbItems = [
  { name: 'Dashboard', link: '/dashboard', active: true }
];



useEffect(() => {
  fetchCandidateSize();
  fetchCompanySize();
  fetchCompanyWiseCount();
  fetchTodayInterviewBookedListCount();
  fetchTodayInterviewCancelListCount();
},[]);


//Redux
const dispatch = useDispatch();
const { fullName, role, gender} = useSelector((state) => state.user);

useEffect(() => {
  const userId = localStorage.getItem('userId');
  if (userId) {
    dispatch(fetchLoggedInUserDetails(userId));
  }
}, [dispatch]);


const fetchCandidateSize = async () =>{
  try{
    const response = await axios.get(API_BASE_URL + '/size');
    setCandidateSize(response.data);
    console.log(response.data);
    //showSuccessNotification('Data loaded successfully from server');
   
  }catch(error){
    showErrorNotification('Error loading data from the server');
    console.error('Error fetching candidate size:', error);
  }
}

const fetchCompanySize = async () =>{
  try{
    const response = await axios.get(API_BASE_URL_COMPANY + '/companySize');
    setCompanySize(response.data);
    
    //showSuccessNotification('Data loaded successfully from server');
  }catch(error){
    showErrorNotification('Error loading data from the server');
    console.error('Error fetching company size:', error);
  }
}

const fetchCompanyWiseCount = async () =>{
  try{
    const response = await axios.get(API_BASE_URL_COMPANY_WISE_COUNT_UPDATE);
    setCompanyWiseCount(response.data);
    console.log(response.data);
  }catch(error){
    showErrorNotification('Error loading data from the server');
    console.error('Error fetching company wise count:', error);
  }
}

const fetchTodayInterviewBookedListCount = async() =>{
    try{
        const response = await axios.get(`${API_BASE_URL_INTERVIEW_BOOK_COUNT}`);
        setTodayInterviewBookedCount(response.data);

    }catch(error){
        console.error("Error fetching today linterview count");
    }
}

const fetchTodayInterviewCancelListCount = async() =>{
    try{
        const response = await axios.get(`${API_BASE_URL_INTERVIEW_CANCEL_COUNT}`);
        setTodayInterviewCancelCount(response.data);

    }catch(error){
        console.error("Error fetching today linterview count");
    }
}

    return (
        <>
         <div className="main-content">
                <div className="page-content">
                    <div className="container-fluid">
                        <Breadcrumb title="Dashboard" breadcrumbItems={breadcrumbItems} />
                        <div>
                        <ToastContainer/>
        <div className="row">
            <div className="col-xl-4">
                <div className="card overflow-hidden">
                    <div className="bg-primary-subtle">
                        <div className="row">
                            <div className="col-7">
                                <div className="text-primary p-3">
                                    <h5 className="text-primary">Welcome Back !</h5>
                                    <h6>{fullName}</h6>
                                </div>
                            </div>
                            <div className="col-5 align-self-end">
                                <img src="assets/images/profile-img.png" alt="" className="img-fluid"/>
                            </div>
                        </div>
                    </div>
                    <div className="card-body pt-0">
                        <div className="row">
                            <div className="col-sm-4">
                                <div className="avatar-md profile-user-wid mb-4">
                                    <img className="img-thumbnail rounded-circle"
                                src={
                                gender === 'MALE' ? 'assets/images/man.png' :
                                gender === 'FEMALE' ? 'assets/images/women.png' :
                                'assets/images/avatar-1.png'
                            }
                                alt="Header Avatar"
                            />
                                </div>

                                <h5 className="font-size-15 text-truncate">{role}</h5>
                            </div>

                            <div className="col-sm-8">
                                <div className="pt-4">

                                    <div className="row">
                                        <div className="col-6">
                                            <h5 className="font-size-15">125</h5>
                                            <p className="text-muted mb-0">Projects</p>
                                        </div>
                                        <div className="col-6">
                                            <h5 className="font-size-15">$1245</h5>
                                            <p className="text-muted mb-0">Revenue</p>
                                        </div>
                                    </div>
                                    <div className="mt-4">
                                    <a href="javascript: void(0);" className="btn waves-effect waves-dark btn-sm" style={{ backgroundColor: 'blue', color: 'white' }}>
                                        <Link to="/adminProfile" style={{ color: 'white', textDecoration: 'none' }}>
                                            View Profile
                                        </Link>
                                        <i className="mdi mdi-arrow-right ms-1"></i>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            
                        </div>
                    </div>
                </div>
            </div>
            <div className="col-xl-8">
                <div className="row">
                    <div className="col-md-4">
                        <div className="card mini-stats-wid">
                            <div className="card-body">
                                <div className="d-flex">
                                    <div className="flex-grow-1">
                                        <p className="text-muted fw-medium">Candidate</p>
                                        <h4 className="mb-0"> {candidateSize}</h4>
                                    </div>

                                    <div className="flex-shrink-0 align-self-center">
                                        <div className="mini-stat-icon avatar-sm rounded-circle bg-primary">
                                            <span className="avatar-title">
                                                <i className="bx bx-copy-alt font-size-24"></i>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-md-4">
                        <div className="card mini-stats-wid">
                            <div className="card-body">
                                <div className="d-flex">
                                    <div className="flex-grow-1">
                                        <p className="text-muted fw-medium">Company</p>
                                        <h4 className="mb-0"> {companySize}</h4>
                                    </div>

                                    <div className="flex-shrink-0 align-self-center ">
                                        <div className="avatar-sm rounded-circle bg-primary mini-stat-icon">
                                            <span className="avatar-title rounded-circle bg-primary">
                                                <i className="bx bx-archive-in font-size-24"></i>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-md-4">
                        <div className="card mini-stats-wid">
                            <div className="card-body">
                                <div className="d-flex">
                                    <div className="flex-grow-1">
                                        <p className="text-muted fw-medium">Today Booked Interview</p>
                                        <h4 className="mb-0">{todayInterviewBookedCount}</h4>
                                    </div>

                                    <div className="flex-shrink-0 align-self-center">
                                        <div className="avatar-sm rounded-circle bg-primary mini-stat-icon">
                                            <span className="avatar-title rounded-circle bg-primary">
                                                <i className="bx bx-purchase-tag-alt font-size-24"></i>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-md-4">
                        <div className="card mini-stats-wid">
                            <div className="card-body">
                                <div className="d-flex">
                                    <div className="flex-grow-1">
                                        <p className="text-muted fw-medium">Today Cancel Interview</p>
                                        <h4 className="mb-0">{todayInterviewCancelCount}</h4>
                                    </div>

                                    <div className="flex-shrink-0 align-self-center">
                                        <div className="avatar-sm rounded-circle bg-primary mini-stat-icon">
                                            <span className="avatar-title rounded-circle bg-primary">
                                                <i className="bx bx-purchase-tag-alt font-size-24"></i>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-md-4">
                        <div className="card mini-stats-wid">
                            <div className="card-body">
                                <div className="d-flex">
                                    <div className="flex-grow-1">
                                        <p className="text-muted fw-medium">Average Price</p>
                                        <h4 className="mb-0">$16.2</h4>
                                    </div>

                                    <div className="flex-shrink-0 align-self-center">
                                        <div className="avatar-sm rounded-circle bg-primary mini-stat-icon">
                                            <span className="avatar-title rounded-circle bg-primary">
                                                <i className="bx bx-purchase-tag-alt font-size-24"></i>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-md-4">
                        <div className="card mini-stats-wid">
                            <div className="card-body">
                                <div className="d-flex">
                                    <div className="flex-grow-1">
                                        <p className="text-muted fw-medium">Average Price</p>
                                        <h4 className="mb-0">$16.2</h4>
                                    </div>

                                    <div className="flex-shrink-0 align-self-center">
                                        <div className="avatar-sm rounded-circle bg-primary mini-stat-icon">
                                            <span className="avatar-title rounded-circle bg-primary">
                                                <i className="bx bx-purchase-tag-alt font-size-24"></i>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
           

            </div>
        </div>
     

        <div className="row">
  {/* Birthday */}
  <Birthday />

  {/* Activity Log */}
  <ActivityLog />

 

  <div className="col-xl-4">
    <div className="card">
      <div className="card-body">
        <h4 className="card-title mb-4">Top Companies</h4>
        <div className="text-center">
          <div className="mb-4">
            <i className="bx bx-map-pin text-primary display-4"></i>
          </div>
        </div>

        {companyWiseCount && companyWiseCount.length > 0 ? (
          <div className="table-responsive mt-4">
            <table className="table align-middle table-nowrap">
              <tbody>
                {companyWiseCount.map((company, index) => (
                  <tr key={index}>
                    <td style={{ width: "30%" }}>
                      <p className="mb-0">{company.first}</p>
                    </td>
                    <td style={{ width: "25%" }}>
                      <h5 className="mb-0">{company.second}</h5>
                    </td>
                    <td>
                      <div className="progress bg-transparent progress-sm">
                        <div
                          className="progress-bar bg-primary rounded"
                          role="progressbar"
                          style={{ width: `${company.second * 10}%` }}
                          aria-valuenow={company.second * 10}
                          aria-valuemin="0"
                          aria-valuemax="100"
                        ></div>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <p className="text-center mt-4">No companies found.</p>
        )}
      </div>
    </div>
  </div>
</div>

        <Teams/>




  
    </div>
 
</div>



<div className="modal fade transaction-detailModal" tabindex="-1" role="dialog" aria-labelledby="transaction-detailModalLabel" aria-hidden="true">
    <div className="modal-dialog modal-dialog-centered" role="document">
        <div className="modal-content">
            <div className="modal-header">
                <h5 className="modal-title" id="transaction-detailModalLabel">Order Details</h5>
                <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div className="modal-body">
                <p className="mb-2">Product id: <span className="text-primary">#SK2540</span></p>
                <p className="mb-4">Billing Name: <span className="text-primary">Neal Matthews</span></p>

                <div className="table-responsive">
                    <table className="table align-middle table-nowrap">
                        <thead>
                            <tr>
                                <th scope="col">Product</th>
                                <th scope="col">Product Name</th>
                                <th scope="col">Price</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th scope="row">
                                    <div>
                                        <img src="assets/images/product/img-7.png" alt="" className="avatar-sm"/>
                                    </div>
                                </th>
                                <td>
                                    <div>
                                        <h5 className="text-truncate font-size-14">Wireless Headphone (Black)</h5>
                                        <p className="text-muted mb-0">$ 225 x 1</p>
                                    </div>
                                </td>
                                <td>$ 255</td>
                            </tr>
                            <tr>
                                <th scope="row">
                                    <div>
                                        <img src="assets/images/product/img-4.png" alt="" className="avatar-sm"/>
                                    </div>
                                </th>
                                <td>
                                    <div>
                                        <h5 className="text-truncate font-size-14">Phone patterned cases</h5>
                                        <p className="text-muted mb-0">$ 145 x 1</p>
                                    </div>
                                </td>
                                <td>$ 145</td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <h6 className="m-0 text-right">Sub Total:</h6>
                                </td>
                                <td>
                                    $ 400
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <h6 className="m-0 text-right">Shipping:</h6>
                                </td>
                                <td>
                                    Free
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <h6 className="m-0 text-right">Total:</h6>
                                </td>
                                <td>
                                    $ 400
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div className="modal-footer">
                <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>



   {/* <div className="modal fade" id="subscribeModal" tabindex="-1" aria-labelledby="subscribeModalLabel" aria-hidden="true">
    <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content">
            <div className="modal-header border-bottom-0">
                <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div className="modal-body">
                <div className="text-center mb-4">
                    <div className="avatar-md mx-auto mb-4">
                        <div className="avatar-title bg-light rounded-circle text-primary h1">
                            <i className="mdi mdi-email-open"></i>
                        </div>
                    </div>

                    <div className="row justify-content-center">
                        <div className="col-xl-10">
                            <h4 className="text-primary">Subscribe !</h4>
                            <p className="text-muted font-size-14 mb-4">Subscribe our newletter and get notification to stay update.</p>

                            <div className="input-group bg-light rounded">
                                <input type="email" className="form-control bg-transparent border-0" placeholder="Enter Email address" aria-label="Recipient's username" aria-describedby="button-addon2"/>
                                
                                <button className="btn btn-primary" type="button" id="button-addon2">
                                    <i className="bx bxs-paper-plane"></i>
                                </button>
                                
                            </div>
                            
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>  */}



<footer className="footer">
    <div className="container-fluid">
        <div className="row">
            <div className="col-sm-6">
                <script>document.write(new Date().getFullYear())</script> © infinity@info.
            </div>
            <div className="col-sm-6">
                <div className="text-sm-end d-none d-sm-block">
                    Design & Develop by Deendayal Kumawat 
                </div>
            </div>
        </div>
    </div>
</footer>
</div>
                        </div>           
     
        </>
    );
}

export default Dashboard;
