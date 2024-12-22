import { API_BASE_URL } from '../env';
import React, { useEffect, useState } from 'react';
import {useParams } from 'react-router-dom';
import { showErrorNotification, showSuccessNotification } from '../notification/Notification';
import { ToastContainer } from 'react-toastify';

const CandidateUpdate = () =>{
    const [candidate, setCandidate] = useState({});
    const { certificateId } = useParams();


    useEffect(() => {
        fetchCandidateData(certificateId);
    }, [certificateId]);
    
    const fetchCandidateData = async (certificateId) => {
        try {
            const response = await fetch(`${API_BASE_URL}/id/${certificateId}`);
            const data = await response.json();
            setCandidate(data); 
            showSuccessNotification('Candidate loaded successfully from server');
        } catch (error) {
            showErrorNotification.error('Error fetching candidate data:');
        }
    };

    return (
        <> 
            <ToastContainer/>
            <div>
                <h2>Update Candidate {candidate.candidateName}</h2>
                <p>Email: {candidate.candidateEmail}</p>
                <p>Salary: {candidate.salaryInWord}</p>
                <p>Working on this page</p>
               
            </div>
        </>
    );
}

export default CandidateUpdate;
