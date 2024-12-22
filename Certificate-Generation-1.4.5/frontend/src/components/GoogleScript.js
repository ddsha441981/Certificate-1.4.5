import React, { useState } from 'react';
import axios from 'axios';
import "../components/myapp.css";

import { Link } from 'react-router-dom';
import {API_BASE_URL_SCRIPT_SCHEDULAR} from '../env';
import { showErrorNotification, showSuccessNotification } from '../notification/Notification';
import { ToastContainer } from 'react-toastify';
import Breadcrumb from './Breadcrumb';


const GoogleScript = () => {

    const [newUrl, setNewUrl] = useState('');
    const [scriptType,setScriptType] = useState('');
    // const app = "app"; 

    const breadcrumbItems = [
        { name: 'Dashboard', link: '/dashboard', active: false },
        { name: `Google Script `, link: `/script`, active: true }
    ];

    const handleUrlChange = (e) => {
        setNewUrl(e.target.value);
    };

    const handleSubmit = (e) => {
        e.preventDefault(); 
        const data = {
            googleId: scriptType,
            scriptUrl: newUrl 
        };
        let dynamicScriptType = '';
        if (scriptType === 'document') {
            dynamicScriptType = 'document';
        } else if (scriptType === 'salary') {
            dynamicScriptType = 'salary';
        }
        else if (scriptType === 'drive') {
            dynamicScriptType = 'drive';
        }
        else if (scriptType === 'interview') {
            dynamicScriptType = 'interview';
        }


console.log(dynamicScriptType);
       axios.put(`${API_BASE_URL_SCRIPT_SCHEDULAR}/update/${dynamicScriptType}`, data, {
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            showSuccessNotification('URL updated successfully');
            console.log(response.data); 
            //clear form data
           clearFormData();
            
        })
        .catch(error => {
            showErrorNotification('Error updating URL');
            console.error('Error updating URL:', error); 
        });
    };

    const clearFormData = ()=>{
        setNewUrl('');
        setScriptType('');
    }

    return (
       <>
            <div className="main-content">
                <div className="page-content">
                    <div className="container-fluid">
                        <Breadcrumb title="Google App Script" breadcrumbItems={breadcrumbItems} />
                        <div>
                        <ToastContainer/>
           

            <div className='card card-properties'>
                <div className='card-body'>
                    <div className="container borders">
                    <p className='msgFile'>All Mandatory field *</p>
                    <div className="row mt-3">
                        <div className='col-md-12'>
                        <div className="form-group">
                                <label htmlFor="addressType">Scritpt Type</label>
                                    <select name='scriptType' id='scriptType' className='form-control'value={scriptType} onChange={(e) => setScriptType(e.target.value)}>
                                        <option value="">Select Script Type</option>
                                        <option value="document">Generate Documents</option>
                                        <option value="salary">Generate Pay Slip</option>
                                        <option value="drive">Download Docs</option>
                                        <option value="interview">Schedule Interview</option>
                                    </select>
                            </div>
                        </div>
                        <div className="col-md-12">
                            <label htmlFor="newUrl">New Google Script URL:</label>
                            <input type="text" className="form-control" id="newUrl" name="newUrl" value={newUrl} onChange={handleUrlChange} placeholder='https://script.google.com/macros/s/AKfycbwBJPhYXwpMHQu2IpBR54kqZtiB0b4yPBYWTUfZXqi14NPbB2BeOv93JqNel6RZWh-L_g/exec' required />
                        </div>

                        <div className="col-12 d-flex justify-content-center mt-5">
                        <button type="submit" className="btn btn-outline-primary" onClick={handleSubmit} style={{ marginLeft: '15px',margin: '10px' }}>Update</button>
                            <Link to={'/candidateList'}>
                                <button type="button" className="btn btn-outline-info" style={{ marginLeft: '15px',margin: '10px' }}>View</button>
                            </Link>
                        </div>
                    </div>
                       
                    </div>
                </div>
            </div>
                        </div>
                    </div>
                </div>
            </div>
       </>
    );
}

export default GoogleScript;