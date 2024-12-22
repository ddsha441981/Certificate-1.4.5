import React, { useState } from 'react';
import moment from 'moment-timezone';
import Breadcrumb from './Breadcrumb';
import { API_BASE_URL_INTERVIEW } from '../env';
import axios from 'axios';
import { showErrorNotification, showSuccessNotification } from '../notification/Notification';
import { ToastContainer } from 'react-toastify';
import { Link } from 'react-router-dom';

const InterviewScheduler = () => {
  const breadcrumbItems = [
    { name: 'Dashboard', link: '/dashboard', active: false },
    { name: 'View Scheduled Interview ', link: '/viewScheduledInterviews', active: false },
    { name: 'Interview Scheduler ', link: '/interviewScheduler', active: true },
  ];

  const [interviewDate,setInterviewDate] = useState('');
  const [startTime,setStartTime] = useState('');
  const [endTime,setEndTime] = useState('');
  const [companyName,setCompanyName] = useState('');
  const [interviewRound,setInterviewRound] = useState('');
  const [interviewType,setInterviewType] = useState('');
  const [candidateName,setCandidateName] = useState('');
  const [timeZone,setTimeZone] = useState('Asia/Kolkata');
  const [location,setLocation] = useState('');
  const [action,setAction] = useState('create');
  const [email,setEmail] = useState('');



    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!interviewDate || !startTime || !endTime || !candidateName) {
            alert('Please fill in all required fields.');
            return;
            }
        try {
           const response = await axios.post(`${API_BASE_URL_INTERVIEW}/schedule/interview`, { 
            interviewDate,
            email,
            startTime,
            endTime,
            companyName,
            interviewRound,
            interviewType,
            candidateName,
            timeZone,
            location,
            action,
        });
    
            showSuccessNotification('Interview scheduled successfully:');
            console.log(response.data); 
            // clearForm();
        } catch (error) {
            showErrorNotification('Server responded with a status:', error.response.status);
            console.error('Error:', error);
        }
    }

  

  return (
    <div className="main-content">
      <div className="page-content">
        <div className="container-fluid">
          <Breadcrumb title="Interview Scheduler" breadcrumbItems={breadcrumbItems} />
          <div>
          <ToastContainer/>
            <div style={{ padding: '20px' }}>
             <form onSubmit={handleSubmit}>
             <div className="row">
                    <div className="col-md-12">
                        <div className="cs-form">
                            <label>Email:</label>
                            <input
                            type="text"
                            className="form-control"
                            name="email"
                            value={email} onChange={(e)=> setEmail(e.target.value)}
                            required
                            />
                        </div>
                    </div>
            </div>

              <div className="row">
                    <div className="col-md-4">
                        <div className="cs-form">
                            <label>Candidate Name:</label>
                            <input
                            type="text"
                            className="form-control"
                            name="candidateName"
                            value={candidateName} onChange={(e)=> setCandidateName(e.target.value)}
                            required
                            />
                        </div>
                    </div>

                <div className="col-md-4">
                    <div className="cs-form">
                        <label>Company Name:</label>
                        <input
                        type="text"
                        className="form-control"
                        name="companyName"
                        value={companyName} onChange={(e)=> setCompanyName(e.target.value)}
                        />
                    </div>
                </div>

                <div className="col-md-4">
                    <div className="cs-form">
                    <label>Interview Date:</label>
                    <input
                        type="date" className='form-control'
                        name="interviewDate"
                        value={interviewDate} onChange={(e)=> setInterviewDate(e.target.value)}
                        required
                    />
                    </div>
                </div>
            </div>


            <div className='row'>
                <div className='col-md-3'>
                    <div className='cs-form'>
                        <label>Start Time:</label>
                        <input
                            type="time" className='form-control'
                            name="startTime"
                            value={startTime} onChange={(e)=> setStartTime(e.target.value)}
                            placeholder="e.g., 09:00 AM"
                            required
                        />
                    </div>
                </div>

                <div className='col-md-3'>
                    <div className='cs-form'>
                        <label>End Time:</label>
                        <input
                            type="time" className='form-control'
                            name="endTime"
                            value={endTime} onChange={(e)=> setEndTime(e.target.value)}
                            placeholder="e.g., 10:00 AM"
                            required
                        />
                    </div>
                </div>
                
                <div className='col-md-3'>
                    <div className='cs-form'>
                        <label>Select Interview Round:</label>
                        <select name="interviewRound" value={interviewRound} onChange={(e)=> setInterviewRound(e.target.value)} className='form-control'>
                            
                        <option value="#" disabled>Select Interview Round</option>
                            <option value="Technical Round 1">Technical Round 1</option>
                            <option value="Technical Round 2">Technical Round 2</option>
                            <option value="Technical Round 3">Technical Round 3</option>
                            <option value="Technical Round 4">Technical Round 4</option>
                            <option value="Technical Round 5">Technical Round 5</option>
                            <option value="Managerial Round">Managerial Round </option>
                            <option value="Hr Round">HR Round </option>
                            <option value="Final Round">Final Round </option>
                            <option value="Offer Stage">Offer Stage </option>
                            <option value="Client Round">Client Round </option>
                            <option value="Screening Round">Screening Round </option>
                        </select>

                    </div>
                </div>

                <div className='col-md-3'>
                    <div className='cs-form'>
                        <label>Select Interview Type:</label>
                        <select name="interviewType" value={interviewType} onChange={(e)=> setInterviewType(e.target.value)} className='form-control'>
                            <option value="#" disabled>Select Interview Tpye</option>
                            <option value="Human Resources">Human Resources</option>
                            <option value="Technical Interview">Technical Interview</option>
                            <option value="Managerial Interview">Managerial Interview</option>
                            <option value="Behavioral Interview">Behavioral Interview</option>
                            <option value="Final Round Interview">Final Round Interview</option>
                            <option value="Telephonic Interview">Telephonic Interview</option>
                            <option value="Case Study Interview">Case Study</option>
                            <option value="Panel Interview">Panel Interview</option>
                            <option value="Coding Interview">Coding Interview</option>
                            <option value="Assignment Interview">Assignment Interview</option>
                            <option value="Mock Interview">Mock Interview</option>
                            <option value="Problem Interview">Problem Solving Interview</option>
                            <option value="Others Interview">Others</option>
                        </select>
                    </div>
                </div>
            </div>

            

            <div className='row'>
                <div className='col-md-6'>
                    <div className='cs-form'>
                        <label>Select Interview Platform:</label>
                        <select name="location" value={location} onChange={(e)=> setLocation(e.target.value)} className='form-control'> 
                            <option value="#" disabled>Select Interview Platform</option>
                            <option value="Zoom">Zoom</option>
                            <option value="Google Meet">Google Meet</option>
                            <option value="Microsoft Teams">Microsoft Teams</option>
                            <option value="Skype">Skype</option>
                            <option value="Others">Others</option>
                        </select>
                    </div>
                </div>

                <div className='col-md-6'>
                    <div className='cs-form'>
                        <label>Select Interview Action:</label>
                        <select name="action" value={action} onChange={(e)=> setAction(e.target.value)} className='form-control'>
                            <option value="#" disabled>Select Interview Action</option>
                            <option key={action} value={action}>Create</option>
                        </select>
                    </div>
                </div>
            </div>

            <div className='row'>
                <div className='col-md-12'>
                    <div className='cs-form'>
                        <label>Select Timezone:</label>
                        <select name="timeZone" value={timeZone} onChange={(e)=> setTimeZone(e.target.value)} className='form-control'>
                            {moment.tz.names().map((zone) => (
                            <option key={zone} value={zone}>
                                {zone}
                            </option>
                            ))}
                        </select>
                    </div>
                </div>
            </div>

                <div className="col-12 d-flex justify-content-center mt-5">
                        <button type="submit" className="btn btn-outline-primary ">Schedule Interview</button>
                        <Link to="/viewScheduledInterviews"><button type="submit" className="btn btn-outline-primary">View</button></Link>
                </div>
            </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default InterviewScheduler;
