import { ToastContainer } from "react-toastify";
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Breadcrumb from "./Breadcrumb";
import { Table, Pagination, PaginationItem, PaginationLink, Spinner, Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';
import { API_BASE_URL_CANCEL_INTERVIEW, API_BASE_URL_INTERVIEW_LIST_PAGINATION, API_BASE_URL_REMOVE_INTERVIEW, API_BASE_URL_SCHEDULE_INTERVIEW } from "../env";
import { BsThreeDotsVertical } from 'react-icons/bs';
import { Link } from "react-router-dom";
import RescheduleModal from "./RescheduleModal";

const ViewScheduledInterview = ({ interviews = [] }) => { 
    
    const breadcrumbItems = [
        { name: 'Dashboard', link: '/dashboard', active: false },
        { name: 'View Scheduled Interview', link: '/viewScheduledInterviews', active: true },
    ];

    const [interviewData, setInterviewData] = useState([]);
    const [pageNumber, setPageNumber] = useState(1);    
    const [pageSize, setPageSize] = useState(10);      
    const [sortBy, setSortBy] = useState('interviewDate');      
    const [sortOrder, setSortOrder] = useState('dsc');  
    const [totalPages, setTotalPages] = useState(1);    
    const [loading, setLoading] = useState(false);     
    const [dropdownOpen, setDropdownOpen] = useState(null); 
    const [modalOpen, setModalOpen] = useState(false);
    const [selectedInterview, setSelectedInterview] = useState(null);

    const toggleDropdown = (eventId) => {
        setDropdownOpen((prevOpen) => (prevOpen === eventId ? null : eventId));
    };

    useEffect(() => {
        fetchInterviewData();
    }, [pageNumber, pageSize, sortBy, sortOrder]);

    const fetchInterviewData = async () => {
        setLoading(true);
        try {
            const response = await axios.get(`${API_BASE_URL_INTERVIEW_LIST_PAGINATION}`, {
                params: {
                    pageNumber: pageNumber - 1,
                    pageSize: pageSize,
                    sortBy: 'interviewDate', 
                    sortOrder: 'asc',  
                },
            });
            
            const interviewList = response.data.items[0]?.interviewEventList || [];
            setInterviewData(interviewList);
            setTotalPages(Math.ceil(response.data.items[0].totalElements / pageSize));
        } catch (error) {
            console.error('Error fetching interview data:', error);
        }
        setLoading(false);
    };

    const handlePageChange = (newPageNumber) => {
        if (newPageNumber >= 1 && newPageNumber <= totalPages) {
            setPageNumber(newPageNumber);
        }
    };

    const handleCancel = async (eventId) => {
        try {
            const action = "cancel";
            console.log(`Cancel interview with ID: ${eventId}`);
            const response = await axios.get(`${API_BASE_URL_CANCEL_INTERVIEW}/${eventId}/action/${action}`);
            console.log(response.data);
            fetchInterviewData();
        } catch (error) {
            console.error("Error canceling interview:", error);
        }
    };

    const handleRemove = async (eventId) => {
        try {
            const action = "remove";
            console.log(`Remove interview with ID: ${eventId}`);
            const response = await axios.get(`${API_BASE_URL_REMOVE_INTERVIEW}/${eventId}/action/${action}`);
            console.log(response.data);
            fetchInterviewData();
        } catch (error) {
            console.error("Error removing interview:", error);
        }
    };

    const handleReschedule = (eventId) => {
        const interview = interviewData.find((i) => i.eventId === eventId); 
        setSelectedInterview(interview);  
        setModalOpen(true);
    };

    return (
        <>
            <div className="main-content">
                <div className="page-content">
                    <div className="container-fluid">
                       
                        <Breadcrumb title="View Scheduled Interview" breadcrumbItems={breadcrumbItems} />

                        <div>
                            <ToastContainer />
                            <div>
                                <Link to={'/interviewScheduler'}>
                                    <button type="button" className="btn btn-outline-primary" style={{ marginLeft: '15px', margin: '10px' }}>Schedule Interview</button>
                                </Link>
                                <Link to={'/today_booked_interview'}>
                                    <button type="button" className="btn btn-outline-success" style={{ marginLeft: '15px', margin: '10px' }}>Today Interview</button>
                                </Link>
                                <Link to={'/today_cancel_interview'}>
                                    <button type="button" className="btn btn-outline-danger" style={{ marginLeft: '15px', margin: '10px' }}>Today Cancel Interview</button>
                                </Link>
                            </div>
                            <div className="container mt-4">
                                {loading ? (
                                    <div className="text-center">
                                        <Spinner color="primary" />
                                        <p>Loading...</p>
                                    </div>
                                ) : (
                                    <>
                                        <Table striped bordered hover>
                                            <thead className="thead-dark">
                                                <tr>
                                                    <th>S.NO</th>
                                                    <th>Interview ID</th>
                                                    <th>Candidate Name</th>
                                                    <th>Company</th>
                                                    <th>Date</th>
                                                    <th>Start Time</th>
                                                    <th>End Time</th>
                                                    <th>Slot</th>
                                                    <th>Status</th>
                                                    <th>Actions</th> 
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {interviewData.length > 0 ? (
                                                    interviewData.map((interview, index) => (
                                                        <tr key={index}>
                                                            <td>{index + 1}</td>
                                                            <td>{interview.eventId}</td>
                                                            <td>{interview.candidateName}</td>
                                                            <td>{interview.companyName}</td>
                                                            <td>{interview.interviewDate}</td>
                                                            <td>{interview.startTime}</td>
                                                            <td>{interview.endTime}</td>
                                                            <td>
                                                                {
                                                                    interview.status === 'SCHEDULED' ? (
                                                                    <span className="badge rounded-pill text-bg-danger">{interview.slot}</span>
                                                                    ) : interview.status === 'CANCELED' || interview.status === 'REMOVED' || interview.status === 'RESCHEDULED' ? (
                                                                    <span className="badge rounded-pill text-bg-success">{interview.slot}</span>
                                                                    ) : (
                                                                    'N/A'
                                                                    )
                                                                }
                                                            </td>
                                                            <td>
                                                            {
                                                                interview.status === 'SCHEDULED' ? (
                                                                    <span className="badge rounded-pill text-bg-success">SCHEDULED</span>
                                                                ) : interview.status === 'CANCELED' ? (
                                                                    <span className="badge rounded-pill text-bg-danger">CANCELED</span>
                                                                ) : interview.status === 'REMOVED' ? (
                                                                    <span className="badge rounded-pill text-bg-warning">REMOVED</span>
                                                                ) : interview.status === 'RESCHEDULED' ? (
                                                                    <span className="badge rounded-pill text-bg-info">RESCHEDULED</span>
                                                                ) : (
                                                                    'N/A'
                                                                )
                                                            }

                                                            </td>
                                                            <td>
                                                                <Dropdown
                                                                    isOpen={dropdownOpen === interview.eventId}
                                                                    toggle={() => toggleDropdown(interview.eventId)}
                                                                >
                                                                    <DropdownToggle tag="span">
                                                                        <BsThreeDotsVertical style={{ cursor: 'pointer' }} />
                                                                    </DropdownToggle>
                                                                    <DropdownMenu>
                                                                        <DropdownItem onClick={() => handleCancel(interview.eventId)}>Cancel</DropdownItem>
                                                                        <DropdownItem onClick={() => handleRemove(interview.eventId)}>Remove</DropdownItem>
                                                                        <DropdownItem onClick={() => handleReschedule(interview.eventId)}>
                                                                            Reschedule
                                                                        </DropdownItem>
                                                                    </DropdownMenu>
                                                                </Dropdown>
                                                            </td>
                                                        </tr>
                                                    ))
                                                ) : (
                                                    <tr>
                                                        <td colSpan="10" className="text-center">No interviews found.</td>
                                                    </tr>
                                                )}
                                            </tbody>
                                        </Table>

                                        <Pagination className="justify-content-center">
                                            <PaginationItem disabled={pageNumber === 1}>
                                                <PaginationLink previous onClick={() => handlePageChange(pageNumber - 1)} />
                                            </PaginationItem>

                                            {[...Array(totalPages)].map((_, i) => (
                                                <PaginationItem key={i} active={i + 1 === pageNumber}>
                                                    <PaginationLink onClick={() => handlePageChange(i + 1)}>
                                                        {i + 1}
                                                    </PaginationLink>
                                                </PaginationItem>
                                            ))}

                                            <PaginationItem disabled={pageNumber === totalPages}>
                                                <PaginationLink next onClick={() => handlePageChange(pageNumber + 1)} />
                                            </PaginationItem>
                                        </Pagination>
                                    </>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {selectedInterview && (
                <RescheduleModal
                    isOpen={modalOpen}
                    toggle={() => setModalOpen(!modalOpen)}
                    interview={selectedInterview}
                />
            )}
        </>
    );
};

export default ViewScheduledInterview;
