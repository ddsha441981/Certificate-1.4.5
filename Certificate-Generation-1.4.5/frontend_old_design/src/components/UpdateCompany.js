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
            showErrorNotification.error('Error fetching company data:');
        }
    };

    return (
        <> 
            <ToastContainer/>
            <div>
                <h2>Update Company {company.companyName}</h2>
                <p>Email: {company.companyEmail}</p>
                <p>Phone: {company.companyPhone}</p>
                <p>Working on this page</p>
               
            </div>
        </>
    );
}

export default UpdateCompany;
