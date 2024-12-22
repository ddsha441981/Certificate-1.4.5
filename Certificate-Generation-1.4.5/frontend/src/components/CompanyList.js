import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import {API_BASE_URL_COMPANY, API_BASE_URL_COMPANY_SOFT_DELETE, API_EXCEL_URL, API_PDF_URL} from '../env';
import { ToastContainer } from 'react-toastify';
import Toggle from 'react-toggle'
import "react-toggle/style.css"
import { showErrorNotification, showSuccessNotification } from '../notification/Notification';
import  { LogoViewer, SignatureViewer } from './imageview/ImageView';
import Breadcrumb from './Breadcrumb';

const CompanyList = () => {
    const [companies, setCompanies] = useState([]);

    const breadcrumbItems = [
        { name: 'Dashboard', link: '/dashboard', active: false },
        { name: 'View Companies', link: '/companyList', active: true }
    ];

    useEffect(() => {
        fetchCompanies();
    }, []);

    const fetchCompanies = async () => {
        try {

            const response = await axios.get(`${API_BASE_URL_COMPANY}/activecompany`);
            setCompanies(response.data);
            console.log(response.data);
           if (response.data === 0) {
               showErrorNotification('No data found');
           } else {
                showSuccessNotification('Companies loaded successfully from server');
           }
        } catch (error) {
            showErrorNotification('Error loading data from the server');
            console.error('Error:', error);
        }
    };

    const handleUpdate = (id) => {
    };

    const handleDelete = async (id) => {
        showSuccessNotification('Company deleted successfully');
        console.log('Delete clicked for ID:', id);
        try {
                //Permanent Delete
            // const response = await axios.delete(`${API_BASE_URL_COMPANY}/${id}`);
                 //Soft Delete
            const response = await axios.put(`${API_BASE_URL_COMPANY_SOFT_DELETE}/${id}`);
            console.log(response.data);
            
            fetchCompanies();
        } catch (err) {
            showErrorNotification('Failed to delete company');
            console.log('Failed to delete company');
        }
    };

    
    // const handleToggleChange = async (companyId, currentStatus) => {
    //     try {
    //         const newStatus = currentStatus === 'active' ? 'inactive' : 'active';
    //         const response = await axios.put(`${API_BASE_URL_COMPANY}/companies/${companyId}/status?status=${newStatus}`);
    //         console.log(response.data);
    //         fetchCompanies(); // Fetch updated list of companies
    //     } catch (error) {
    //         showErrorNotification('Failed to update company status');
    //         console.error('Error:', error);
    //     }
    // };

    const handleToggleChange = async (companyId, currentStatus) => {
        try {
            console.log('Current status:', currentStatus);
            const newStatus = currentStatus === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
            console.log('New status:', newStatus);


            const response = await axios.put(`${API_BASE_URL_COMPANY}/companies/${companyId}/status?status=${newStatus}`);
            console.log(response.data);
            fetchCompanies(); 
        } catch (error) {
            showErrorNotification('Failed to update company status');
            console.error('Error:', error);
        }
    };
    
    const generatePdfContent = async () => {
        try {
            const response = await axios.get(API_PDF_URL + '/generate', {
                params: {
                    gateway: 'company'
                },
                responseType: 'blob'
            });
    
           
            const blobUrl = window.URL.createObjectURL(new Blob([response.data]));
    
           
            const link = document.createElement('a');
            link.href = blobUrl;
            link.setAttribute('download', 'report.pdf');
            document.body.appendChild(link);
    
            link.click();
    
            // Cleanup
            window.URL.revokeObjectURL(blobUrl);
    
            showSuccessNotification("Pdf file generated successfully of company");
        } catch (error) {
            console.log(error);
        }
    }
    
    const generateExcelContent = async () => {
        try {
            const response = await axios.get(API_EXCEL_URL + '/generate', {
                params: {
                    gateway: 'company'
                },
                responseType: 'blob'
            });
    
            const blobUrl = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = blobUrl;
            link.setAttribute('download', 'report.xlsx');
            document.body.appendChild(link);
            link.click();
            window.URL.revokeObjectURL(blobUrl);
    
            showSuccessNotification("Excel file generated successfully of company");
        } catch (error) {
            console.log(error);
        }
    }
    

    

    return (
      
       <>
           
           <div className="main-content">
                <div className="page-content">
                    <div className="container-fluid">
                        <Breadcrumb title="Company List" breadcrumbItems={breadcrumbItems} />
                        <div>
                        <ToastContainer/>
            <div className='card'>
                <div className='card-body'>
                
                    <div className="table-responsive">
                <div>
                    <Link to={'/addCompany'}>
                        <button type="button" className="btn btn-outline-primary" style={{ marginLeft: '15px', margin: '10px' }}>Add Company</button>
                    </Link>
                    <button type="button" onClick={generatePdfContent} className="btn btn-outline-danger" style={{ marginLeft: '15px', margin: '10px' }}>PDF</button>
                    <button type="button" onClick={generateExcelContent} className="btn btn-outline-warning" style={{ marginLeft: '15px', margin: '10px' }}>Excel</button>
                    
                    <Link to={'/viewToDefaultSalary'}>
                         <button type="button" className="btn btn-outline-info" style={{ marginLeft: '15px', margin: '10px' }}>Default Salary</button>
                    </Link>

                    <Link to={'/viewToTaxSlab'}>
                    <button type="button"  className="btn btn-outline-danger" style={{ marginLeft: '15px', margin: '10px' }}>Tax Slab</button>
                    </Link>
                    
                </div>

                <table className="table table-bordered table-hover">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Company</th>
                            <th>Mode</th>
                            <th>Logo</th>
                            <th>Signature</th>
                            <th>Doc Id</th>
                            <th>Experience</th>
                            <th>Relieving</th>
                            <th>Offer</th>
                            <th>Salary Slip</th>
                            <th>Increment</th>
                            <th>Apparisal</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    {/* <tbody>
                        {companies.length === 0 ? (
                            <tr>
                                <td colSpan="3" className='text-center'>No companies found.</td>
                            </tr>
                        ) : (
                            companies.map(company => (
                                <tr key={company.companyId}>
                                    <td>{company.companyId}</td>
                                    <td>{company.companyName}</td>
                                    <td>{company.documents}</td>
                                    <td>167jgjczjczxjcbzjxbxcbvkjjkkjkjhkjsd-dkjfhdkfhakjfhdsk-fbdjfbsdjf</td>
                                    <td>167jgjczjczxjcbzjxbxcbvkjjkkjkjhkjsd-dkjfhdkfhakjfhdsk-fbdjfbsdjf</td>
                                    <td>167jgjczjczxjcbzjxbxcbvkjjkkjkjhkjsd-dkjfhdkfhakjfhdsk-fbdjfbsdjf</td>
                                    <td>167jgjczjczxjcbzjxbxcbvkjjkkjkjhkjsd-dkjfhdkfhakjfhdsk-fbdjfbsdjf</td>
                                    <td>167jgjczjczxjcbzjxbxcbvkjjkkjkjhkjsd-dkjfhdkfhakjfhdsk-fbdjfbsdjf</td>
                                    <td>167jgjczjczxjcbzjxbxcbvkjjkkjkjhkjsd-dkjfhdkfhakjfhdsk-fbdjfbsdjf</td>
                                    <td>
                                        <button type="button" className="btn btn-primary" style={{ margin: '10px' }} onClick={() => handleUpdate(company.companyId)}>Edit</button>
                                        <button type="button" className="btn btn-primary" style={{ margin: '10px' }} onClick={() => handleDelete(company.companyId)}>Delete</button>
                                    </td>
                                </tr>
                            ))
                        )}
                    </tbody> */}
                    <tbody>
                        {companies.length === 0 ? (
                            <tr>
                            <td colSpan="9" className='text-center'>No companies found.</td>
                            </tr>
                        ) : (
                            companies.map(company => (
                            <React.Fragment key={company.companyId}>
                                <tr>
                                    <td rowSpan={company.documents.length}>{company.companyId}</td>
                                    <td rowSpan={company.documents.length}>{company.companyName}</td>
                                    {/* <td rowSpan={company.documents.length}>{company.status}</td> */}
                                    <td>
                                        <span className={`badge rounded-pill ${company.status === 'CREATED' ? 'bg-success' : 'bg-info'}`}>
                                            {company.status}
                                        </span>
                                    </td>
                                    <td rowSpan={company.documents.length}>  <LogoViewer logoData={company.logoData} /></td>
                                    <td rowSpan={company.documents.length}> <SignatureViewer signatureData={company.signatureData} /></td>
                                    
                                    <td>{company.documents[0].documentId}</td>
                                    <td>{company.documents[0].experienceLetterUrl.substring(0, 10)}</td>
                                    <td>{company.documents[0].relievingLetterUrl.substring(0, 10)}</td>
                                    <td>{company.documents[0].offerLetterUrl.substring(0, 10)}</td>
                                    <td>{company.documents[0].salarySlipUrl.substring(0, 10)}</td>
                                    <td>{company.documents[0].incrementLetterUrl.substring(0, 10)}</td>
                                    <td>{company.documents[0].apparisalLetterUrl.substring(0, 10)}</td>
                                    <td>
                                        <span className={`badge rounded-pill ${company.changeStatus === 'ACTIVE' ? 'bg-success' : 'bg-danger'}`}>
                                            {company.changeStatus}
                                        </span>
                                    </td>

                                
    
                                    <td className='tbl-image-gap'>
                                        {/* If you need toggle then uncomment it */}
                                        
                                         {/* <Toggle
                                            id={`company-toggle-${company.companyId}`}
                                            checked={company.changeStatus === 'ACTIVE'}
                                             onChange={() => handleToggleChange(company.companyId, company.changeStatus)}
                                        /> */}
                                        <Link to={`/viewCompany/${company.companyId}`}>
                                            <img src="../assets/images/view.png" alt="View" width="20" height="20"/>
                                        </Link>

                                        <Link to={`/updateCompany/${company.companyId}`}>
                                            <img src="../assets/images/edit.png" alt="Edit" width="20" height="20" onClick={() => handleUpdate(company.companyId)}/>
                                        </Link>
                                        <img src="../assets/images/delete.png" alt="Edit" width="20" height="20" onClick={() => handleDelete(company.companyId)}/>      
                                    </td>
                                </tr>
                                {/* {company.documents.slice(1).map(document => (
                                <tr key={document.documentId}>
                                    <td>{document.experienceLetterUrl.substring(0, 10)}</td>
                                    <td>{document.relievingLetterUrl.substring(0, 10)}</td>
                                    <td>{document.offerLetterUrl.substring(0, 10)}</td>
                                    <td>{document.salarySlipUrl.substring(0, 10)}</td>
                                    <td>{document.incrementLetterUrl.substring(0, 10)}</td>
                                    <td>{document.apparisalLetterUrl.substring(0, 10)}</td>
                                </tr>
                                ))} */}
                            </React.Fragment>
                            ))
                        )}
                    </tbody>

                </table>
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

export default CompanyList;
