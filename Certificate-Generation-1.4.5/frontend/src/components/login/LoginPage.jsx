

import React, { useState,useEffect } from 'react'; 

import axios from 'axios'; 

import { Link, useNavigate } from 'react-router-dom'; 
import "./login.css"
import { Container, Row, Col, Card, Button, Form, Alert } from 'react-bootstrap';
import { API_LOGIN_URL } from '../../env';
import { MDBContainer, MDBInput } from 'mdb-react-ui-kit';


  

function LoginPage() { 

    const [email, setEmail] = useState(''); 

    const [password, setPassword] = useState(''); 

    const [error, setError] = useState(''); 

    const navigate  = useNavigate(); 

  

    const handleLogin = async () => { 

        try { 

            if (!email || !password) { 

                setError('Please enter both username and password.'); 

                return; 

            } 

            const loginData = {
                email,
                password
            }

  

            const response = await axios.post(`${API_LOGIN_URL}/signing`, loginData); 

            clearFormData();
            console.log("Login data  is " , response.data);
            console.log("Token is " , response.data.responseData.token);

            const userId = response.data.responseData.userId;
            const token = response.data.responseData.token;
            const role = response.data.responseData.role;
            console.log(token);
            localStorage.setItem('userId', userId);
            localStorage.setItem('token', token);
            localStorage.setItem('role', role);
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            


            console.log('Login successful:', response.data); 

            if (response.data.role === 'ADMIN'){
            navigate ('/dashboard'); 
            }else{
              navigate ('/dashboard'); 
            }

        } catch (error) { 

            console.error('Login failed:', error.response ? error.response.data : error.message); 

            setError('Invalid username or password.'); 

        } 

    }; 

    const clearFormData = () => {
       setEmail('');
       setPassword('');
    }

    useEffect(() => {
        const token = localStorage.getItem('token');
        console.log(token);
        if (token) {
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            console.log(token);
            navigate('/dashboard');
        }
    }, []);
    

  

    return ( 
      <MDBContainer > 
                      /* <div class="account-pages my-5 pt-sm-5">
                      <div class="container-fluid">
        <div className="row justify-content-center">
          <div className="col-md-8 col-lg-6 col-xl-5">
            <div className="card overflow-hidden">
              <div className="bg-primary-subtle">
                <div className="row">
                  <div className="col-7">
                    <div className="text-primary p-4">
                      <h5 className="text-primary">Welcome Back!</h5>
                      <p>Sign in to continue to Skote.</p>
                    </div>
                  </div>
                  <div className="col-5 align-self-end">
                    <img
                      src="assets/images/logo.png"
                      alt=""
                      className="img-fluid"
                    />
                  </div>
                </div>
              </div>
              <div className="card-body pt-0">
        
                <div className="p-2">
                
                    <div className="mb-3">
                      <label htmlFor="username" className="form-label">
                        Username
                      </label>
                      <MDBInput
                        type="email"
                        className="form-control"
                        id="email"
                        placeholder='Email address'  value={email}  onChange={(e) => setEmail(e.target.value)}
                      />
                    </div>

                    <div className="mb-3">
                      <label className="form-label">Password</label>
                        <MDBInput
                          type="password"
                          
                          placeholder="Enter password"
                          value={password} onChange={(e) => setPassword(e.target.value)}
                        />
                     
                    </div>
                    {error && <p className="text-danger">{error}</p>} 
    
                    <div className="mt-3 d-grid">
                      <button
                        className="btn btn-primary waves-effect waves-light"
                        type="submit"
                        onClick={handleLogin}
                      >
                        Log In
                      </button>
                    </div>

                    <div className="mt-4 text-center">
                      <h5 className="font-size-14 mb-3">Sign in with</h5>

                      <ul className="list-inline">
                        <li className="list-inline-item">
                          <a
                            href="#" onClick={(e) => e.preventDefault()}
                            className="social-list-item bg-primary text-white border-primary"
                          >
                            <i className="mdi mdi-facebook"></i>
                          </a>
                        </li>
                        <li className="list-inline-item">
                          <a
                            href="#" onClick={(e) => e.preventDefault()}
                            className="social-list-item bg-info text-white border-info"
                          >
                            <i className="mdi mdi-twitter"></i>
                          </a>
                        </li>
                        <li className="list-inline-item">
                          <a
                            href="#" onClick={(e) => e.preventDefault()}
                            className="social-list-item bg-danger text-white border-danger"
                          >
                            <i className="mdi mdi-google"></i>
                          </a>
                        </li>
                      </ul>
                    </div>

                    <div className="mt-4 text-center">
                      <Link className="text-muted" to={'/forgotPassword'}><i className="mdi mdi-lock me-1"></i> Forgot your password?</Link>
                    </div>
            
                </div>
              </div>
            </div>
            <div className="mt-5 text-center">
              <div>
              <p>
                Don't have an account?{' '}
                <a href="/signup" className="fw-medium text-primary">
                  Signup now
                </a>{' '}
                |{' '}
                <img src="../assets/images/notification.png" alt="Subscribe to notifications" height={40} width={40} />{' '}
                <Link to="/pushnotification" className="fw-medium text-primary">
                  Subscribe Push Notification
                </Link>
            </p>

              </div>
            </div>
          </div>
        </div>
</div>
</div> 
</MDBContainer>

); 
} 

  

export default LoginPage; 

      

                 
           

       



