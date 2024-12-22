

import React, { useState,useEffect } from 'react'; 

import axios from 'axios'; 

import { Link, useNavigate } from 'react-router-dom'; 
import { 

    MDBContainer, 

    MDBInput, 

    MDBBtn, 

} from 'mdb-react-ui-kit'; 
import { API_LOGIN_URL } from '../../env';

  

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
                navigate('/userhomepage');
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

        <div className="d-flex justify-content-center align-items-center vh-100"> 

            <div className="border rounded-lg p-4" style={{ width: '500px', height: 'auto' }}> 

                <MDBContainer className="p-3"> 

                    <h2 className="mb-4 text-center">Login</h2> 

                    <MDBInput wrapperClass='mb-4' placeholder='Email address' id='email' value={email} type='email' onChange={(e) => setEmail(e.target.value)} /> 

                    <MDBInput wrapperClass='mb-4' placeholder='Password' id='password' type='password' value={password} onChange={(e) => setPassword(e.target.value)} /> 

                    {error && <p className="text-danger">{error}</p>} {/* Render error message if exists */} 

                    <button className="mb-4 d-block btn-primary" style={{ height:'50px',width: '100%' }} onClick={handleLogin}>Sign in</button> 

                    <div className="d-flex justify-content-between"> 
    <p>Not Registered? <a href="/signup">Register</a></p> 
    <p>Forgot Password? <Link to={'/forgotPassword'}>Click Here</Link></p>
</div>

                </MDBContainer> 

            </div> 

        </div> 

    ); 
} 

  

export default LoginPage; 

