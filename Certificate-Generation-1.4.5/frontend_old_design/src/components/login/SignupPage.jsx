

import React, { useState } from 'react'; 

import axios from 'axios'; 

import { useNavigate } from 'react-router-dom'; // Import useHistory hook 
import { 

    MDBContainer, 
    MDBSelect, MDBSelectInput, MDBSelectOptions, MDBSelectOption,
    MDBInput, 

    MDBBtn, 

} from 'mdb-react-ui-kit'; 
import { API_LOGIN_URL } from '../../env';
import { showSuccessNotification } from '../../notification/Notification';
import ReCAPTCHA from 'react-google-recaptcha';

  

function SignupPage() { 

    const [firstName, setFirstName] = useState(''); 
    const [lastName, setLastName] = useState(''); 
    const [dob, setDob] = useState('');
    const [email, setEmail] = useState(''); 
    const [password, setPassword] = useState(''); 
    const [confirmPassword, setConfirmPassword] = useState(''); 
    const [role, setRole] = useState(''); 
    const [error, setError] = useState(''); 
    const history = useNavigate();
    const [captchaValue, setCaptchaValue] = useState(null);


  const handleCaptchaChange = (value) => {
    // console.log("-------------------------",value);
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
                dob

            }); 
            console.log(response.data);

            history('/'); 

        } catch (error) { 

            console.error('Signup failed:', error.response ? error.response.data : error.message); 

            setError(error.response ? error.response.data : error.message); 

        } 

    }; 

  

    return ( 

        <div className="d-flex justify-content-center align-items-center vh-100"> 

            <div className="border rounded-lg p-4" style={{width: '600px', height: 'auto'}}> 

                <MDBContainer className="p-3"> 

                    <h2 className="mb-4 text-center">Sign Up</h2> 

                    {error && <p className="text-danger">{error}</p>} 

                    <MDBInput wrapperClass='mb-3' id='firstName' placeholder={"First Name"} value={firstName} type='text'

                              onChange={(e) => setFirstName(e.target.value)}/> 
                    <MDBInput wrapperClass='mb-3' id='lastName' placeholder={"Last Name"} value={lastName} type='text'

                              onChange={(e) => setLastName(e.target.value)}/> 
                    <MDBInput wrapperClass='mb-3' id='dob' placeholder={"Date of Birth"} value={dob} type='date'

                              onChange={(e) => setDob(e.target.value)}/> 

                    <MDBInput wrapperClass='mb-3' placeholder='Email Address' id='email' value={email} type='email'

                              onChange={(e) => setEmail(e.target.value)}/> 

                    <MDBInput wrapperClass='mb-3' placeholder='Password' id='password' type='password' value={password} 

                              onChange={(e) => setPassword(e.target.value)}/> 

                    <MDBInput wrapperClass='mb-3' placeholder='Confirm Password' id='confirmPassword' type='password'

                              value={confirmPassword} 

                              onChange={(e) => setConfirmPassword(e.target.value)}/> 

  
                    <label className="form-label mb-1">Role:</label> 

                    <select className="form-select mb-10" value={role} onChange={(e) => setRole(e.target.value)}> 

                        <option value="#">Select Role</option> 
                        <option value="USER">User</option> 
                        <option value="ADMIN">Admin</option> 

                    </select> 

                     <ReCAPTCHA className='mt-3 mb-3'
                        sitekey="6LfZuOopAAAAAF6RlqxJVIaBkaGgUZvZhCRBHl_j"
                        onChange={handleCaptchaChange}
                    />

                    <button className="mb-4 d-block mx-auto fixed-action-btn btn-primary"

                            style={{height: '40px', width: '100%'}} 

                            onClick={handleSignup}>Sign Up 

                    </button> 

  

                    <div className="text-center"> 

                        <p>Already Register? <a href="/">Login</a></p> 

                    </div> 

  

                </MDBContainer> 

            </div> 

        </div> 

    ); 
} 

  

export default SignupPage;