import React, { useState } from 'react';
import { getToken, onMessage } from 'firebase/messaging';
import { messaging } from './firebase'; 
import { API_BASE_URL_FIREBASE_NOTIFICATION } from '../../env'; 
import axios from 'axios';
import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import { Card, Container } from 'react-bootstrap';

const PushNotificationSetup = () => {
    const [email, setEmail] = useState("");             
    const [token, setToken] = useState("");             
    const [message, setMessage] = useState("");       
    const [permissionGranted, setPermissionGranted] = useState(false); 
    const [submitted, setSubmitted] = useState(false); 

    
    const handleSubmit = (event) => {
        event.preventDefault();  
        if (email) {
            setSubmitted(true);
            requestPermission();
            alert("Please enter a valid email.");
        }
    };

    
    const requestPermission = async () => {
        try {
            const permission = await Notification.requestPermission();
            if (permission === "granted") {
                console.log("Notification permission granted.");
                setPermissionGranted(true);
                getDeviceToken(); 
            } else {
                console.log("Notification permission denied.");
                alert("You need to allow notifications to receive updates.");
            }
        } catch (error) {
            console.error("Error requesting permission:", error);
            alert("There was an error requesting permission. Please try again.");
        }
    };

   
    const getDeviceToken = async () => {
        try {
            const token = await getToken(messaging, {
                vapidKey: "BEPTjKtT6N4XKdq1_7e3CmrXSqEAYjjiuUoA5Mc4csZbgFg20K3rhqSIdZgfPy3lmbaRQy6zliwI91LtBms_p60"
            });

            if (token) {
                console.log("Device Token: ", token);
                setToken(token);
                sendTokenToBackend(token, email); 
            } else {
                alert("Failed to retrieve device token.");
            }
        } catch (error) {
            console.error("Error getting device token:", error);
            alert("There was an error retrieving the device token.");
        }
    };

    
    const sendTokenToBackend = async (token, email) => {
        if (!email || !token) {
            console.error("Email or token is missing.");
            return;
        }

        const requestData = { email, token };

        try {
            const response = await axios.post(`${API_BASE_URL_FIREBASE_NOTIFICATION}/update/device/token`, requestData);

            if (response.status === 200) {
                console.log("Device token stored successfully:", response.data);
                alert("Device token stored successfully!");
            } else {
                console.error("Failed to store device token:", response.data);
                alert("Failed to store device token. Please try again.");
            }
        } catch (error) {
            console.error("Error storing device token", error);
            alert("There was an error storing the device token. Please try again.");
        }
    };


    //copy this code in app.js for entire application  
    // Listen for incoming messages background
    onMessage(messaging, (payload) => {
        console.log('Message received:', payload);
        setMessage(payload.notification.body);
        // show a custom notification on the web page
        alert(`${payload.notification.title}: ${payload.notification.body}`);
    });

    return (
       <>


<Container fluid className="d-flex justify-content-center align-items-center min-vh-100">
            <Card style={{ width: '100%', maxWidth: '500px' }} className="p-4">
                <h1 className="text-center">Push Notification</h1>

                {!submitted && (
                    <Form onSubmit={handleSubmit}>
                        <Row className="mb-3">
                            <Form.Group as={Col} controlId="formGridEmail">
                                <p className="text-center animated-text">
                                    Enter your email address to get your device token and receive push notifications for interviews.
                                </p>
                                <Form.Label>Email</Form.Label>
                                <Form.Control
                                    type="email"
                                    name="email"
                                    id="email"
                                    placeholder="Enter your email address here"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    required
                                />
                            </Form.Group>
                        </Row>
                        <Button variant="outline-info" type="submit" className="w-100">
                            Submit
                        </Button>
                    </Form>
                )}

                {submitted && !permissionGranted && (
                    <p className="text-center mt-3">Please enable notifications to receive updates.</p>
                )}

                {permissionGranted && (
                    <div className="text-center mt-4">
                        <Card className="p-3 shadow-sm">
                            <h3 className="animated-text">Your Device Token generated successfully now you can receive future notification asscoited with your email address:</h3>
                            {/* <p
                                className="word-break-all p-2 bg-light rounded"
                                style={{ overflowWrap: 'break-word', wordWrap: 'break-word' }}
                            >
                                {token}
                            </p>
                            <p>{message ? `New message: ${message}` : 'No new notifications'}</p> */}
                        </Card>
                    </div>
                )}
            </Card>
        </Container>

            {/* <div>
                <h1>Push Notification</h1>
                {!submitted && (
                <Form onSubmit={handleSubmit}>
                    <Row className="mb-3">
                        <Form.Group as={Col} controlId="formGridemail">
                            <p>Enter your email address to get your device token to get push notification for interview</p>
                            <Form.Label> Email</Form.Label>
                            <Form.Control type="text" name="email" id="email" placeholder='Enter your email address here' value={email} onChange={(e) => setEmail(e.target.value)} required/>
                        </Form.Group>
                    </Row>
                    <Button  variant="outline-info">Submit</Button>
                </Form>
                )}

                {submitted && !permissionGranted && (
                        <p>Please enable notifications to receive updates.</p>
                )}

                {permissionGranted && (
                    <div>
                       <p>Your Device Token: {token}</p>
                       <p>{message ? `New message: ${message}` : "No new notifications"}</p>
                    </div>
                    )}
            </div> */}
           
             {/* <div>
                    <h1>Push Notification</h1>
                    {!submitted && (
                        <form onSubmit={handleSubmit}>
                            <label>Email:
                                <input
                                    type="email"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)} 
                                    required
                                />
                            </label>
                            <button type="submit">Submit</button>
                        </form>
                    )}

                    {submitted && !permissionGranted && (
                        <p>Please enable notifications to receive updates.</p>
                    )}

                    {permissionGranted && (
                        <div>
                            <p>Your Device Token: {token}</p>
                            <p>{message ? `New message: ${message}` : "No new notifications"}</p>
                        </div>
                    )}
        </div> */}
        
       </>
    );
};

export default PushNotificationSetup;
