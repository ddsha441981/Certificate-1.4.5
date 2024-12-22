

import React, { useState,useEffect } from 'react'; 

import axios from 'axios'; 

import { Link, useNavigate } from 'react-router-dom'; 
import { 

    MDBContainer, 

    MDBInput, 
    MDBRow,

    MDBBtn, 

} from 'mdb-react-ui-kit'; 
import { API_LOGIN_URL } from '../../env';
import { showSuccessNotification } from '../../notification/Notification';

  

function ForgetPassword() { 

    const defaultUserId = 0; // Default userId
    const [email, setEmail] = useState(''); 
    // const [changePasswordUrl,setChangePasswordUrl] = useState('http://localhost:3000/changePassword/'+{userId});
    const [changePasswordUrl, setChangePasswordUrl] = useState(`http://localhost:3000/changePassword/${defaultUserId}`);
    const [error, setError] = useState(''); 

    const navigate  = useNavigate(); 

  

    const handleForgotPassword = async () => { 

        try { 

            if (!email) { 

                setError('Please enter  email.'); 

                return; 

            } 
            

            const forgotPasswordData = {
                email,
                changePasswordUrl
            }

            console.log(forgotPasswordData);
  

            const response = await axios.post(`${API_LOGIN_URL}/forget_password`, forgotPasswordData); 

            const token = response.data.token;
            console.log(token);


            showSuccessNotification("Email send successful");
            console.log('Email send successful:', response.data); 

            navigate ('/login'); 

        } catch (error) { 

            console.error('Email send  failed:', error.response ? error.response.data : error.message); 

            setError('Invalid email.'); 

        } 

    }; 

  

    return ( 

        <div className="d-flex justify-content-center align-items-center vh-100"> 

            <div className="border rounded-lg p-4" style={{ width: '500px', height: 'auto' }}> 

                <MDBContainer className="p-3"> 


                    <h2 className="mb-4 text-center">Forget Password </h2> 
                    <p className='msgFile' >You will receive an email with instructions to reset your password if an account exists for this email address.</p>
                    <MDBInput wrapperClass='mb-4' placeholder='Email address' id='email' value={email} type='email' onChange={(e) => setEmail(e.target.value)} /> 
                    <MDBInput wrapperClass='mb-4'  id='changePasswordUrl' value={changePasswordUrl} type='hidden' onChange={(e) => setChangePasswordUrl(e.target.value)} /> 
                    {error && <p className="text-danger">{error}</p>} {/* Render error message if exists */} 

                    <button className="mb-4 d-block btn-primary" style={{ height:'50px',width: '100%' }} onClick={handleForgotPassword}>Submit</button> 

                </MDBContainer> 

            </div> 

        </div> 

    ); 
} 

  

export default ForgetPassword; 

