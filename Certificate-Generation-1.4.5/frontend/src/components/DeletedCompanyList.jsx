import { useEffect, useState } from "react";
import { ToastContainer } from "react-toastify";
import { API_BASE_URL_COMPANY_SOFT_DELETE, API_SOFT_DELETED_COMPANIES_LIST_TRUE } from "../env";
import { showErrorNotification, showSuccessNotification } from "../notification/Notification";
import axios from "axios";
import { Link } from "react-router-dom";
import Breadcrumb from "./Breadcrumb";

const DeletedComapnyList = () => {

    const [companiesList, setCompaniesList] = useState([]);
    
    const breadcrumbItems = [
        { name: 'Dashboard', link: '/dashboard', active: false },
        { name: 'Disabled Company List', link: `/viewDeletedComapnyList`, active: true }
    ];

    useEffect(() => {
        // Data load when page render
        fetchDeletedCompanyList();
    }, []);

    const fetchDeletedCompanyList = async () => {
        try {
            const deleted = true;
            const response = await axios.get(`${API_SOFT_DELETED_COMPANIES_LIST_TRUE}/${deleted}`);

            setCompaniesList(response.data);
            console.log(response.data);
            showSuccessNotification("Data loaded from server");
        } catch (error) {
            showErrorNotification('Error loading data from the server');
            console.error('Error:', error);
        }
    };

    const activateCompanyAgain = async (id)=>{
        console.log(id);
        const deleted = false;
        const response = await axios.put(`${API_BASE_URL_COMPANY_SOFT_DELETE}/change/${id}/status/${deleted}`);
        console.log(response.data);
        fetchDeletedCompanyList();
    }

    return (
        <>
            <div className="main-content">
                <div className="page-content">
                    <div className="container-fluid">
                    <Breadcrumb title="Disabled Companies" breadcrumbItems={breadcrumbItems} />
                    <ToastContainer />
                        
                        <table className="table table-hover">
                            <thead>
                                <tr>
                                <th scope="col">S.NO</th>
                                    <th scope="col">#</th>
                                    <th scope="col">Company Name</th>
                                    <th scope="col">Status</th>
                                    <th scope="col">Action</th>
                                    
                                </tr>
                            </thead>
                            <tbody>
                                {companiesList.length > 0 ? (
                                    companiesList.map((company, index) => (
                                        <tr key={company.companyId}>
                                            <th>{index + 1}</th>
                                            <th scope="row">{company.companyId}</th>
                                            <td>{company.companyName}</td>
                                            <td>{company.changeStatus}</td>
                                            <td>
                                                <Link 
                                                    to="#"
                                                    onClick={(e) => {
                                                        e.preventDefault();
                                                        activateCompanyAgain(company.companyId);
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
                       
        </>
    );
};

export default DeletedComapnyList;
