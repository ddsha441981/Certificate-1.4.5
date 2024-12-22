import React, { useState } from 'react';
import axios from 'axios';
import "../components/myapp.css";

import { Link } from 'react-router-dom';
import {API_BASE_URL_SCRIPT_SCHEDULAR} from '../env';
import { showErrorNotification, showSuccessNotification } from '../notification/Notification';
import { ToastContainer } from 'react-toastify';


const GoogleScript = () => {
    const [newUrl, setNewUrl] = useState('');
    const [scriptType,setScriptType] = useState('');
    // const app = "app"; 

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
            <ToastContainer/>
           <div className='card'>
                <div className='card-header'>
                    <h4 className='card-title text-center'>Google App Script</h4>
                </div>
            </div>

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
                                    </select>
                            </div>
                        </div>
                        <div className="col-md-12">
                            <label htmlFor="newUrl">New Google Script URL:</label>
                            <input type="text" className="form-control" id="newUrl" name="newUrl" value={newUrl} onChange={handleUrlChange} placeholder='https://script.google.com/macros/s/AKfycbwBJPhYXwpMHQu2IpBR54kqZtiB0b4yPBYWTUfZXqi14NPbB2BeOv93JqNel6RZWh-L_g/exec' required />
                        </div>

                        <div className="col-12 d-flex justify-content-center mt-5">
                        <button type="submit" className="btn btn-primary" onClick={handleSubmit} style={{ marginLeft: '15px',margin: '10px' }}>Update</button>
                            <Link to={'/candidateList'}>
                                <button type="button" className="btn btn-primary" style={{ marginLeft: '15px',margin: '10px' }}>View</button>
                            </Link>
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