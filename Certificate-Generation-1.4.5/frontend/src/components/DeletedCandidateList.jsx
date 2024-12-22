import axios from "axios";
import { useEffect, useState } from "react";
import { ToastContainer } from "react-toastify";
import { showErrorNotification, showSuccessNotification } from "../notification/Notification";
import { API_BASE_URL_CERTIFICATE_BY_ID, API_SOFT_DELETED_CANDIDATE_LIST } from "../env";
import { Link } from "react-router-dom";
import Breadcrumb from "./Breadcrumb";

const DeletedCandidateList = () =>{

    const [candidateList,setCandidateList]= useState([]);

    const breadcrumbItems = [
        { name: 'Dashboard', link: '/dashboard', active: false },
        { name: 'Disabled Candidate List', link: `/viewDeletedCandidateList`, active: true }
    ];


    useEffect(()=>{
        //data load when page render
       fetchDeletedCandidateList();
    },[]);

    const fetchDeletedCandidateList = async () =>{
        try {
            const deleted = true;

            const response = await axios.get(`${API_SOFT_DELETED_CANDIDATE_LIST}/${deleted}`);

            setCandidateList(response.data);
            console.log(response.data);
            showSuccessNotification("data loaded from server")
        } catch (error) {
            showErrorNotification('Error loading data from the server');
            console.error('Error:', error);
        }
    };

    const activateCertificateAgain = async (certificateId) =>{
        const response = await axios.put(`${API_BASE_URL_CERTIFICATE_BY_ID}/${certificateId}`);
        console.log(response.data);
        fetchDeletedCandidateList();
    }

    return(
        <>
                    <div class="main-content">

                        <div class="page-content">
                            <div class="container-fluid">
                        <Breadcrumb title="Disabled Candidate" breadcrumbItems={breadcrumbItems} />
                        <div>
                        <ToastContainer />
                                <table className="table table-hover">
                            <thead>
                                <tr>
                                <th scope="col">S.NO</th>
                                    <th scope="col">#</th>
                                    <th scope="col">Candidate Name</th>
                                    <th scope="col">Status</th>
                                    <th scope="col">Action</th>
                                    
                                </tr>
                            </thead>
                            <tbody>
                                {candidateList.length > 0 ? (
                                    candidateList.map((certificate, index) => (
                                        <tr key={certificate.certificateId}>
                                            <th>{index + 1}</th>
                                            <th scope="row">{certificate.certificateId}</th>
                                            <td>{certificate.candidateName}</td>
                                            <td>{certificate.changeStatus}</td>
                                            <td>
                                                <Link 
                                                    to="#"
                                                    onClick={(e) => {
                                                        e.preventDefault();
                                                        activateCertificateAgain(certificate.certificateId);
                                                    }}
                                                >
                                                    <img
                                                        src="../assets/images/active.png"
                                                        alt="activate"
                                                        width="20"
                                                        height="20"
                                                    />
                                                </Link>
                                            </td>

                                        </tr>
                                    ))
                                ) : (
                                    <tr>
                                        <td colSpan="4" className="text-center">No deleted companies found</td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                            </div>
                        </div>
                        </div>
                    </div>
                    

                                
    
        </>
    );
};
export default DeletedCandidateList;