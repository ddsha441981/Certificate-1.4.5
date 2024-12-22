import { API_BASE_URL, API_BASE_URL_COMPANY } from '../env';
import React, { useEffect , useState} from 'react';
import axios from 'axios';
import RecentList from './RecentList';
import Teams from './Teams';
import ContactUs from './ContactUs';
import { ToastContainer } from 'react-toastify';
import { showErrorNotification, showSuccessNotification } from '../notification/Notification';


const Dashboard = () => {

const [candidateSize, setCandidateSize] = useState(0);
const [companySize, setCompanySize] = useState(0);


useEffect(() => {
  fetchCandidateSize();
  fetchCompanySize();
},[]);

const fetchCandidateSize = async () =>{
  try{
    const response = await axios.get(API_BASE_URL + '/size');
    setCandidateSize(response.data);
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


    return (
        <>
            <ToastContainer/>
            <div className="container-fluid">
            <div className="row">
                <div className="col-md-3">
                    <div className="card bg-danger text-white">
                        <div className="card-body">
                            <h1 className='text-center text-white'>{candidateSize}</h1>
                            <p>Candidate List </p>
                            <i className="fas fa-chart-bar"></i>
                        </div>
                    </div>
                </div>

                <div className="col-md-3">
                    <div className="card bg-info text-white">
                        <div className="card-body">
                        <h1 className='text-center text-white'>{companySize}</h1>
                            <p>Available Companies</p>
                            <i className="fas fa-tachometer-alt"></i>
                        </div>
                    </div>
                </div>

                <div className="col-md-3">
                    <div className="card bg-success text-white">
                        <div className="card-body">
                        <h1 className='text-center text-white'>67%</h1>
                            <p>Monitor your progress</p>
                            <i className="fas fa-chart-line"></i>
                        </div>
                    </div>
                </div>

                <div className="col-md-3">
                    <div className="card bg-warning text-dark">
                        <div className="card-body">
                        <h1 className='text-center text-white'>67%</h1>
                            <p>Keep track of important</p>
                            <i className="fas fa-bullseye"></i>
                        </div>
                    </div>
                </div>
            </div>

              <RecentList/>
              <Teams/>
              <ContactUs/>
           
        </div>
        </>
    );
}

export default Dashboard;
