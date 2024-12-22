import { API_BASE_URL_COMPANY } from '../env';
import React, { useEffect, useState } from 'react';
import {useParams } from 'react-router-dom';
import { showErrorNotification, showSuccessNotification } from '../notification/Notification';
import { ToastContainer } from 'react-toastify';

const UpdateCompany = () =>{
    const [company, setCompany] = useState({});
    const { companyId } = useParams();

    useEffect(() => {
        fetchCompanyData(companyId);
    }, [companyId]);
    
    const fetchCompanyData = async (companyId) => {
        try {
            const response = await fetch(`${API_BASE_URL_COMPANY}/${companyId}`);
            const data = await response.json();
            setCompany(data); 
            showSuccessNotification('Company loaded successfully from server');
        } catch (error) {
            console.log('Error fetching company data:', error);
            // showErrorNotification.error('Error fetching company data:');
        }
    };

    return (
        <> 
         <div class="main-content">

        <div class="page-content">
            <div class="container-fluid">

                <div class="row">
                    <div class="col-12">
                        <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                            <h4 class="mb-sm-0 font-size-18">Dashboard</h4>

                            <div class="page-title-right">
                                <ol class="breadcrumb m-0">
                                    <li class="breadcrumb-item"><a href="javascript: void(0);">Dashboards</a></li>
                                    <li class="breadcrumb-item active">Dashboard</li>
                                </ol>
                            </div>

                        </div>
                    </div>
                </div>
                </div>
                <ToastContainer/>
            <div>
                <h2>Update Company {company.companyName}</h2>
                <p>Email: {company.companyEmail}</p>
                <p>Phone: {company.companyPhone}</p>
                <p>Working on this page</p>
               
            </div>
                </div>
            </div>
        
            
        </>
    );
}

export default UpdateCompany;
