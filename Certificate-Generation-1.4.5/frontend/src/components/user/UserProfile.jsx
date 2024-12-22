import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Breadcrumb from '../Breadcrumb';
import { API_BASE_URL } from '../../env';
import axios from 'axios';
import './userProfile.css'; 

const UserProfile = () => {
    const { id } = useParams(); 
    const [certificate, setCertificate] = useState({}); 

    const breadcrumbItems = [
        { name: 'Dashboard', link: '/dashboard', active: false },
        { name: 'Candidate List', link: '/candidateList', active: false },
        { name: `User Profile`, link: `/userProfile/${id}`, active: true }
    ];

    useEffect(()=>{
        getUserProfileById(id);
    }, []);
    
    const getUserProfileById = async (id) =>{
        const response = await axios.get(`${API_BASE_URL}/id/${id}`);
        console.log(response.data);
        setCertificate(response.data);
    };

    return (
        <>
            <div className="main-content">
                <div className="page-content">
                    <div className="container-fluid">
                        <Breadcrumb title="User Profile" breadcrumbItems={breadcrumbItems} />
                        <p>TODO: Make Changes to your Profile as per your need and design it as you want</p>
                        <div className="user-profile-header text-center">
                            <img 
                                src="https://avatar.iran.liara.run/public" 
                                alt="User" 
                                className="rounded-circle profile-img"
                            />
                            <h1 className="mt-3">{certificate.candidateName || 'User Name'}</h1>
                            <p className="job-title">{certificate.jobTitle || 'Job Title'}</p>
                        </div>

                       
                        <div className="row">
                            
                            <div className="col-md-6">
                                <div className="card mb-4">
                                    <div className="card-header">
                                        <h5>Personal Information</h5>
                                    </div>
                                    <div className="card-body">
                                        <p><strong>Employee Code:</strong> {certificate.employeeCode || 'N/A'}</p>
                                        <p><strong>Email:</strong> {certificate.candidateEmail || 'N/A'}</p>
                                        <p><strong>Date of Birth:</strong> {certificate.dob || 'N/A'}</p>
                                        <p><strong>Status:</strong> {certificate.status || 'N/A'}</p>
                                        <p><strong>Date of Joining:</strong> {certificate.dateOfJoining || 'N/A'}</p>
                                    </div>
                                </div>
                            </div>

                            
                            <div className="col-md-6">
                                <div className="card mb-4">
                                    <div className="card-header">
                                        <h5>Company Information</h5>
                                    </div>
                                    <div className="card-body">
                                        <p><strong>Company Name:</strong> {certificate.companyName || 'N/A'}</p>
                                        <p><strong>Selected Company:</strong> {certificate.selectedCompany || 'N/A'}</p>
                                        <p><strong>Offer Date:</strong> {certificate.offerDate || 'N/A'}</p>
                                        <p><strong>Change Status:</strong> {certificate.changeStatus || 'N/A'}</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="row">
                           
                            <div className="col-md-6">
                                <div className="card mb-4">
                                    <div className="card-header">
                                        <h5>Bank Information</h5>
                                    </div>
                                    <div className="card-body">
                                        <p><strong>Bank Name:</strong> {certificate?.bank?.bankName || 'N/A'}</p>
                                        <p><strong>Account Number:</strong> {certificate?.bank?.accountNo || 'N/A'}</p>
                                        <p><strong>IFSC Code:</strong> {certificate?.bank?.ifscCode || 'N/A'}</p>
                                        <p><strong>UAN Number:</strong> {certificate?.bank?.uanNumber || 'N/A'}</p>
                                        <p><strong>ESI Number:</strong> {certificate?.bank?.esiNumber || 'N/A'}</p>
                                    </div>
                                </div>
                            </div>

                           
                            <div className="col-md-6">
                                <div className="card mb-4">
                                    <div className="card-header">
                                        <h5>Salary Information</h5>
                                    </div>
                                    <div className="card-body">
                                        <p><strong>CTC:</strong> {certificate?.salaryExpose?.ctc || 'N/A'}</p>
                                        <p><strong>Gross Salary:</strong> {certificate?.salaryExpose?.grossSalary || 'N/A'}</p>
                                        <p><strong>In-Hand Salary:</strong> {certificate?.salaryExpose?.inHandSalary || 'N/A'}</p>
                                        <p><strong>Performance Bonus:</strong> {certificate?.salaryExpose?.performanceBonus || 'N/A'}</p>
                                        <p><strong>Income Tax:</strong> {certificate?.salaryExpose?.incomeTax || 'N/A'}</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="row">
                           
                            <div className="col-md-12">
                                <div className="card mb-4">
                                    <div className="card-header">
                                        <h5>Identification Details</h5>
                                    </div>
                                    <div className="card-body">
                                        <p><strong>Aadhar Number:</strong> {certificate?.identificationDetails?.aadarNumber || 'N/A'}</p>
                                        <p><strong>PAN Number:</strong> {certificate?.identificationDetails?.panNumber || 'N/A'}</p>
                                        <p><strong>Document Type:</strong> {certificate?.identificationDetails?.documentType || 'N/A'}</p>
                                        <p><strong>Document Name:</strong> {certificate?.identificationDetails?.docName || 'N/A'}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
};

export default UserProfile;









