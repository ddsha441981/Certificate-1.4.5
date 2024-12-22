import React, { useEffect, useState } from 'react';
import { Card, Col, Row, Container, ToastContainer } from 'react-bootstrap';
import Breadcrumb from './Breadcrumb';
import axios from 'axios';
import './interviewcard.css'; 
import { API_BASE_URL_INTERVIEW_BOOKED_LIST } from '../env';
import ColorPalette from './ColorPalette';
import { FaClock } from 'react-icons/fa';
import { FaPeopleRoof } from "react-icons/fa6";

const TodayBookedInterview = () => {
    const [interviewBookedList, setInterviewBookedList] = useState([]);
    const [loading, setLoading] = useState(true);

   
    const breadcrumbItems = [
        { name: 'Dashboard', link: '/dashboard', active: false },
        { name: 'View Scheduled Interview', link: '/viewScheduledInterviews', active: false },
        { name: 'View Today Interview', link: '/today_booked_interview', active: true },
    ];
    
    //Call Card Color Components
    const cardColors = ColorPalette();

   const getRandomColor = () => {
    const randomIndex = Math.floor(Math.random() * cardColors.length);
    return cardColors[randomIndex];
  };


    useEffect(() => {
        loadTodayBookedInterviewList();
    }, []);

    const loadTodayBookedInterviewList = async () => {
        setLoading(true);
        try {
           
            const response = await axios.get(API_BASE_URL_INTERVIEW_BOOKED_LIST);
            
          
            const data = response.data; 

            setInterviewBookedList(data); 
        } catch (error) {
            console.error('Error fetching interview data:', error);
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return <div>Loading...</div>;  
    }

    return (
        <div className="main-content">
        <div className="page-content">
          <div className="container-fluid">
            <Breadcrumb title="View Today Interview" breadcrumbItems={breadcrumbItems} />
            <div>
              <ToastContainer />
              <Container fluid className="my-4">
                <Row className="g-4">
                  {interviewBookedList.map((item, index) => (
                    <Col xs={12} sm={6} md={4} key={index}>
                      <Card className="shadow border-light rounded" style={{ backgroundColor: getRandomColor() }}>
                        <Card.Header className="text-center" style={{ fontSize: '1.25rem', fontWeight: 'bold'}}>
                          {item.companyName}
                        </Card.Header>
                        <Card.Body>
                          <Card.Text className="text-white"><strong>Candidate:</strong> {item.candidateName}</Card.Text>
                          <Card.Text className="text-white"><strong>Round:</strong> {item.interviewRound}</Card.Text>
                          <Card.Text className="text-white"><strong>Type:</strong> {item.interviewType}</Card.Text>
                        </Card.Body>
                        <Card.Footer className="d-flex justify-content-between align-items-center" style={{ backgroundColor: '#f8f9fa' }}>
                            <div className="Card-footer-text">
                                <FaClock className="me-2" />
                                <strong>{item.startTime} - {item.endTime}</strong>
                            </div>
                            <div className="Card-footer-text">
                                <FaPeopleRoof className="me-2" />
                                <strong>{item.location}</strong>
                            </div>
                        </Card.Footer>
                      </Card>
                    </Col>
                  ))}
                </Row>
              </Container>
            </div>
          </div>
        </div>
      </div>
    );
};

export default TodayBookedInterview;
