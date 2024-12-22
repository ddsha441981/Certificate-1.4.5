

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

       

                <MDBContainer classNameName="p-3"> 

       <div className="account-pages my-5 pt-sm-5">
       <div className="container">
           <div className="row justify-content-center">
               <div className="col-md-8 col-lg-6 col-xl-5">
                   <div className="card overflow-hidden">
                       <div className="bg-primary-subtle">
                           <div className="row">
                               <div className="col-7">
                                   <div className="text-primary p-4">
                                       <h5 className="text-primary"> Reset Password</h5>
                                       <p><p classNameName='msgFile' >You will receive an email with instructions to reset your password if an account exists for this email address.</p></p>
                                   </div>
                               </div>
                               <div className="col-5 align-self-end">
                                   <img src="assets/images/profile-img.png" alt="" className="img-fluid"/>
                               </div>
                           </div>
                       </div>
                       <div className="card-body pt-0"> 
                         
                           
                           <div className="p-2">
                              
                               {error &&
                               
                               
                               <div className="alert alert-success text-center mb-4" role="alert">
                                
                                {error}
                                
                                
                                </div>
                               
                               
                               
                               } {/* Render error message if exists */} 
                              
       
                                   <div className="mb-3">
                                       <label for="useremail" className="form-label">Email</label>
                                       <MDBInput wrapperclassName='mb-4' placeholder='Email address' id='email' value={email} type='email' onChange={(e) => setEmail(e.target.value)} /> 
                                       <MDBInput wrapperclassName='mb-4'  id='changePasswordUrl' value={changePasswordUrl} type='hidden' onChange={(e) => setChangePasswordUrl(e.target.value)} /> 
                                   </div>
                                 
                                   <div className="text-end">
                                       <button className="btn btn-primary w-md waves-effect waves-light" type="submit"  onClick={handleForgotPassword}>Reset</button>
                                   </div>

                           </div>
       
                       </div>
                   </div>
                   <div className="mt-5 text-center">
                       <p>Remember It ? <a href="/" className="fw-medium text-primary"> Sign In here</a> </p>
                   </div>

               </div>
           </div>
       </div>
   </div>

   </MDBContainer> 
    ); 
} 
export default ForgetPassword; 

