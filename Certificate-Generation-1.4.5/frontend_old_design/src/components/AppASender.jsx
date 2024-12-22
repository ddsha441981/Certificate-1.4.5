import axios from 'axios';
import React from 'react';
import { Link } from 'react-router-dom';
import { API_VIDEO_CALL_LINK_GENERATE } from '../env';

const AppASender = () => {


// const openWebsite = async () => {
//     window.open('https://p2p.mirotalk.com', '_blank');
//   };

  const generateLink = async () => {
    try {
        console.log("Clicked");

        
        const id = localStorage.getItem('userId');
        console.log("User ID:", id);

       
        const token = localStorage.getItem('token');
        const role = localStorage.getItem('role');
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

     
        const response = await axios.get(`${API_VIDEO_CALL_LINK_GENERATE}/admin/details/${id}`);
        console.log("API Response:", response);


        
        const {password, email,firstName,lastName } = response.data;

        const adminData = {
          email,
          password,
          firstName,
          lastName,
          role,
          token
      };

        // Send admin data to the backend
       await sendAdminInfoToBackend(adminData);
    } catch (error) {
        console.error("Error:", error);
     
    }

   
}

// Function to send admin info to the backend
const sendAdminInfoToBackend = async (adminData) => {
  try {

    const token = localStorage.getItem('token');
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

      console.log("Admin Data:", adminData);
 
    const response = await axios.post(`${API_VIDEO_CALL_LINK_GENERATE}/admin/video-link/loginDetails`, adminData);
    response.headers(response.data);
    console.log("Backend Response:", response);
} catch (error) {
    console.error("Error sending admin info to backend:", error);
  
}
}

  return(
    <>
     <button onClick={generateLink}>Create Meeting</button>
     <label> Publish Meeting Link</label>
     <input type='text' name='publishMeeting' id='publishMeeting'/>
    </>
  )
};

  
export default AppASender;
