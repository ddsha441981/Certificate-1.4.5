

import React, { useState } from 'react'; 

import axios from 'axios'; 

import { Link, useNavigate } from 'react-router-dom'; 
import { Container, Row, Col, Card, Button, Form, Alert } from 'react-bootstrap';
// import { 

//     MDBContainer, 
//     MDBInput, 

// } from 'mdb-react-ui-kit'; 
import { API_LOGIN_URL } from '../../env';
import ReCAPTCHA from 'react-google-recaptcha';
import { MDBContainer, MDBInput } from 'mdb-react-ui-kit';

  

function SignupPage() { 

    const [firstName, setFirstName] = useState(''); 
    const [lastName, setLastName] = useState(''); 
    const [dob, setDob] = useState('');
    const [email, setEmail] = useState(''); 
    const [password, setPassword] = useState(''); 
    const [confirmPassword, setConfirmPassword] = useState(''); 
    const [role, setRole] = useState(''); 
    const [gender, setGender] = useState(''); 
    const [error, setError] = useState(''); 
    const history = useNavigate();
    const [captchaValue, setCaptchaValue] = useState(null);


  const handleCaptchaChange = (value) => {
    setCaptchaValue(value);
  };

    const handleSignup = async () => { 

        try { 

            if (!firstName || !lastName || !email || !password || !confirmPassword) { 
                setError('Please fill in all fields.'); 
                return; 
            } 
            if (password !== confirmPassword) { 
                throw new Error("Passwords do not match"); 
            } 
            if (!captchaValue) {
                alert('Please complete the CAPTCHA');
                return;
            }

            const captchaResponse = await fetch(`${API_LOGIN_URL}/verify-captcha`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ captchaValue }),
            });

            const captchaResult = await captchaResponse.json();
            if (!captchaResult.success) {
                alert('CAPTCHA verification failed');
                return;
            }

            
            const response = await axios.post(`${API_LOGIN_URL}/signup`, { 

                firstName, 
                lastName,
                email, 
                password, 
                role,
                dob,
                gender

            }); 
            console.log(response.data);

            history('/'); 

        } catch (error) { 

            console.error('Signup failed:', error.response ? error.response.data : error.message); 

            setError(error.response ? error.response.data : error.message); 

        } 

    }; 

  

    return ( 

        <MDBContainer className="p-3"> 
        <div className="account-pages my-5 pt-sm-5">
<div className="container">
    <div className="row justify-content-center">
        <div className="col-md-8 col-lg-6 col-xl-5">
            <div className="card overflow-hidden">
                <div className="bg-primary-subtle">
                    <div className="row">
                        <div className="col-7">
                            <div className="text-primary p-4">
                                <h5 className="text-primary">Free Register</h5>
                                <p>Get your free  account now.</p>
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
                   
                   
                   
                   }
                   

                            <div className="mb-3">
                                <label for="firstName" className="form-label">First Name</label>
                                <MDBInput wrapperclassName='mb-3' id='firstName' placeholder={"First Name"} value={firstName} type='text'
                              
                                onChange={(e) => setFirstName(e.target.value)}/> 
                                </div>
                                <div className="mb-3">
                                <label for="lastName" className="form-label">Last Name</label>

                                <MDBInput wrapperclassName='mb-3' id='lastName' placeholder={"Last Name"} value={lastName} type='text'

                                onChange={(e) => setLastName(e.target.value)}/>
                                </div>
                                <div className="mb-3"> 
                                <label for="dob" className="form-label">Bithday</label>
                                <MDBInput wrapperclassName='mb-3' id='dob' placeholder={"Date of Birth"} value={dob} type='date'

                                onChange={(e) => setDob(e.target.value)}/> 
                                </div>
                                <div className="mb-3">
                                <label for="email" className="form-label">Email</label>

                                <MDBInput wrapperclassName='mb-3' placeholder='Email Address' id='email' value={email} type='email'

                                onChange={(e) => setEmail(e.target.value)}/> 
                                </div>
                                <div className="mb-3">
                                <label for="password" className="form-label">Password</label>

                                <MDBInput wrapperclassName='mb-3' placeholder='Password' id='password' type='password' value={password} 

                                onChange={(e) => setPassword(e.target.value)}/> 
                                </div>
                                <div className="mb-3">
                                <label for="confirmPassword" className="form-label">Confirm Password</label>

                                <MDBInput wrapperclassName='mb-3' placeholder='Confirm Password' id='confirmPassword' type='password'

                                value={confirmPassword} 

                                onChange={(e) => setConfirmPassword(e.target.value)}/> 
                                </div>
                                
                                <div className="mb-3">
                                    
                                    <label classNameName="form-label mb-1">Role:</label> 

                                    <select className="form-control form-select mb-10" value={role} onChange={(e) => setRole(e.target.value)}> 
                                        <option value="#">Select Role</option> 
                                        <option value="USER" disabled>User</option> 
                                        <option value="ADMIN">Admin</option> 
                                    </select>      
                                </div>

                                <div className="mb-3">
                                    <label classNameName="form-label mb-1">Gender:</label> 

                                    <select className="form-control form-select mb-10" value={gender} onChange={(e) => setGender(e.target.value)}> 
                                        <option value="#">Select Gender</option> 
                                        <option value="MALE">Male</option> 
                                        <option value="FEMALE">Female</option> 
                                        <option value="OTHERS">Others</option> 
                                    </select>      
                                </div>
    
                          
        
                            <ReCAPTCHA classNameName='mt-3 mb-3'
            sitekey="6LfZuOopAAAAAF6RlqxJVIaBkaGgUZvZhCRBHl_j"
            onChange={handleCaptchaChange}
        />
                            <div className="mt-4 d-grid">
                                <button className="btn btn-primary waves-effect waves-light" type="submit" onClick={handleSignup}>Register</button>
                            </div>

                            <div className="mt-4 text-center">
                                <h5 className="font-size-14 mb-3">Sign up using</h5>

                                <ul className="list-inline">
                                    <li className="list-inline-item">
                                        <a href="javascript::void()" className="social-list-item bg-primary text-white border-primary">
                                            <i className="mdi mdi-facebook"></i>
                                        </a>
                                    </li>
                                    <li className="list-inline-item">
                                        <a href="javascript::void()" className="social-list-item bg-info text-white border-info">
                                            <i className="mdi mdi-twitter"></i>
                                        </a>
                                    </li>
                                    <li className="list-inline-item">
                                        <a href="javascript::void()" className="social-list-item bg-danger text-white border-danger">
                                            <i className="mdi mdi-google"></i>
                                        </a>
                                    </li>
                                </ul>
                            </div>
    
                            <div className="mt-4 text-center">
                                <p className="mb-0">By registering you agree to  <a href="#" className="text-primary">Terms of Use</a></p>
                            </div>
                    </div>

                </div>
            </div>
            <div className="mt-5 text-center">
                
                <div>
                    <p>Already have an account ? <a href="/" className="fw-medium text-primary"> Login</a> </p>
                </div>
            </div>

        </div>
    </div>
</div>
</div>





    </MDBContainer> 


    ); 
} 
export default SignupPage;


              