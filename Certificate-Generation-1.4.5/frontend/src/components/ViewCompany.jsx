import axios from "axios";
import Breadcrumb from "./Breadcrumb";
import { API_BASE_URL_COMPANY } from "../env";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import "./viewCompany.css";

const ViewCompany = () => {
    const { id } = useParams();
    const [company, setCompany] = useState({});

    const breadcrumbItems = [
        { name: 'Dashboard', link: '/dashboard', active: false },
        { name: 'Company List', link: '/companies', active: false },
        { name: `Company Profile`, link: `/viewCompany/${id}`, active: true }
    ];

    useEffect(() => {
        getCompanyProfileById(id);
    }, []);

    const getCompanyProfileById = async (id) => {
        const response = await axios.get(`${API_BASE_URL_COMPANY}/${id}`);
        setCompany(response.data);
    };

    return (
        <>
            <div className="main-content">
                <div className="page-content">
                    <div className="container-fluid">
                        <Breadcrumb title="Company Profile" breadcrumbItems={breadcrumbItems} />
                        
                        <div className="company-profile">
                        <p>TODO: Make Changes to your Profile as per your need and design it as you want</p>
                            <div className="row">
                                <div className="col-md-4">
                                    <div className="card">
                                        <img src={company.logoData} alt="Company Logo" className="card-img-top company-logo" />
                                        <div className="card-body">
                                            <h5 className="card-title">Company Details</h5>
                                            <p className="card-text"><strong>Email:</strong> {company.companyEmail}</p>
                                            <p className="card-text"><strong>Phone:</strong> {company.companyPhone}</p>
                                            <p className="card-text"><strong>Website:</strong> <a href={company.companyWebsite} target="_blank" rel="noopener noreferrer">{company.companyWebsite}</a></p>
                                            <p className="card-text"><strong>Status:</strong> {company.status}</p>
                                        </div>
                                    </div>
                                </div>

                                <div className="col-md-8">
                                    <div className="card">
                                        <div className="card-body">
                                            <h5 className="card-title">Address</h5>
                                            {company.addresses && company.addresses.length > 0 ? (
                                                company.addresses.map((address, index) => (
                                                    <div key={index} className="address-card">
                                                        <p><strong>Country:</strong> {address.country}</p>
                                                        <p><strong>City:</strong> {address.city}</p>
                                                        <p><strong>Street:</strong> {address.street}</p>
                                                        <p><strong>Building Number:</strong> {address.buildingNumber}</p>
                                                        <p><strong>Landmark:</strong> {address.landmark}</p>
                                                        <p><strong>Address Type:</strong> {address.addressType}</p>
                                                    </div>
                                                ))
                                            ) : (
                                                <p>No addresses available.</p>
                                            )}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
};

export default ViewCompany;
