

import React, { useState,useEffect } from 'react'; 

import axios from 'axios'; 
import {useParams } from 'react-router-dom';

import { Link, useNavigate } from 'react-router-dom'; 
import { 

    MDBContainer, 

    MDBInput, 

    MDBBtn, 

} from 'mdb-react-ui-kit'; 
import { API_LOGIN_URL } from '../../env';

  

function ChangePassword() { 
    const { userId } = useParams();

    const [password, setPassword] = useState(''); 
    const [confirmPassword, setConfirmPassword] = useState(''); 

    const [error, setError] = useState(''); 

    const navigate  = useNavigate(); 

  
    // console.log(userId)

    const handleChangePassword = async () => { 

        try { 

            if (!password || !confirmPassword) { 

                setError('Please enter  password.'); 

                return; 
            } 
            if(password !== confirmPassword){
                setError("Password not match");
                return;
            }

            const changePasswordData = {
                password
            }

  

            // const response = await axios.patch(`${API_LOGIN_URL}/change_password/`+{userId}, changePasswordData); 

            const response = await axios.patch(`${API_LOGIN_URL}/change_password/${userId}`, changePasswordData);

            console.log('Password change successful:', response.data); 

            navigate ('/login'); 

        } catch (error) { 

            console.error('Password change  failed:', error.response ? error.response.data : error.message); 

            setError('Invalid password.'); 

        } 

    }; 

  

    return ( 

        <div className="d-flex justify-content-center align-items-center vh-100"> 

            <div className="border rounded-lg p-4" style={{ width: '500px', height: 'auto' }}> 

                <MDBContainer className="p-3"> 

                    <h2 className="mb-4 text-center">Change Password</h2> 

                    <MDBInput wrapperClass='mb-4' placeholder='New Password' id='password' value={password} type='password' onChange={(e) => setPassword(e.target.value)} /> 
                    <MDBInput wrapperClass='mb-4' placeholder='Confirm Password' id='confirmPassword' value={confirmPassword} type='password' onChange={(e) => setConfirmPassword(e.target.value)} /> 
                    {error && <p className="text-danger">{error}</p>} 

                    <button className="mb-4 d-block btn-primary" style={{ height:'50px',width: '100%' }} onClick={handleChangePassword}>Submit</button> 

                </MDBContainer> 

            </div> 

        </div> 

    ); 
} 

  

export default ChangePassword; 

