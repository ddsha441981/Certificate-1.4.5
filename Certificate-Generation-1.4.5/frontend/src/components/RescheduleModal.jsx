import axios from 'axios';
import React, { useState } from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button, Form, FormGroup, Label, Input } from 'reactstrap';
import { API_BASE_URL_INTERVIEW } from '../env';
import { Select } from '@mui/material';
import { FormSelect } from 'react-bootstrap';

const RescheduleModal = ({ isOpen, toggle, interview }) => {
    const [interviewDate, setInterviewDate] = useState(interview.interviewDate);
    const [startTime, setStartTime] = useState(interview.startTime);
    const [endTime, setEndTime] = useState(interview.endTime);
    const [action, setAction] = useState("reschedule");

    console.log('Modal isOpen:', isOpen);

    const handleSubmit = (e) => {
        e.preventDefault();
        // const action = "reschedule";
        const updatedInterview = {
            eventId: interview.eventId,
            interviewDate,
            startTime,
            endTime,
            action,
        };
        axios.put(`${API_BASE_URL_INTERVIEW}/reschedule/interview`, updatedInterview)
        .then(response => {
            console.log('Response:', response.data);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error during request: ' + error.message);
        });
        toggle();
    };

    return (
        <Modal isOpen={isOpen} toggle={toggle}>
            <ModalHeader toggle={toggle}>Reschedule Interview</ModalHeader>
            <ModalBody>
                <Form onSubmit={handleSubmit}>
                    <FormGroup>
                        <Label for="interviewDate">Interview Date</Label>
                        <Input
                            type="date"
                            id="interviewDate"
                            value={interviewDate}
                            onChange={(e) => setInterviewDate(e.target.value)}
                            required
                        />
                    </FormGroup>
                    <FormGroup>
                        <Label for="startTime">Start Time</Label>
                        <Input
                            type="time"
                            id="startTime"
                            value={startTime}
                            onChange={(e) => setStartTime(e.target.value)}
                            required
                        />
                    </FormGroup>
                    <FormGroup>
                        <Label for="endTime">End Time</Label>
                        <Input
                            type="time"
                            id="endTime"
                            value={endTime}
                            onChange={(e) => setEndTime(e.target.value)}
                            required
                        />
                    </FormGroup>
                    <FormGroup>
                        <Label for="action">Action</Label>
                        <FormSelect
                            id="action"
                            value={action}
                            onChange={(e) => setAction(e.target.value)}
                            required
                        >
                            <option value="" disabled>Select an action</option>
                            <option value="reschedule">Reschedule</option>
                        </FormSelect>
                    </FormGroup>
                </Form>
            </ModalBody>
            <ModalFooter>
                <Button color="primary" onClick={handleSubmit}>Save Changes</Button>
                <Button color="secondary" onClick={toggle}>Cancel</Button>
            </ModalFooter>
        </Modal>
    );
};

export default RescheduleModal;
