import React, { useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import { API_DEFAULT_SALARY_VALUE_URL } from "../../env";
import { showSuccessNotification, showErrorNotification } from "../../notification/Notification";
import Breadcrumb from '../Breadcrumb';

const DefaultValueSalary = () => {

    const [gratuity, setGratuity] = useState('');
    const [hra, setHra] = useState('');
    const [profTax, setProfTax] = useState('');
    const [medicalInsurance, setMedicalInsurance] = useState('');
    const [investments80C, setInvestments80C] = useState(0.0);

    const breadcrumbItems = [
        { name: 'Dashboard', link: '/dashboard', active: false },
        { name: 'View Companies', link: '/companies', active: false },
        { name: 'View Salary', link: '/viewToDefaultSalary', active: false },
        { name: `Add Salary`, link: `/addDefaultSalary`, active: true }
    ];

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
           const response = await axios.post(`${API_DEFAULT_SALARY_VALUE_URL}/`, { 
            gratuity, 
            hra,
            profTax, 
            medicalInsurance, 
            investments80C
        });
    
            showSuccessNotification("Default salary data added successfully.");
            console.log(response.data); 
            clearForm();
        } catch (error) {
            showErrorNotification("Error adding Default Salary Data");
            console.error('Error:', error);
        }


    }
    
    function clearForm() {
        setGratuity('');
        setHra('');
        setProfTax('');
        setMedicalInsurance('');
        setInvestments80C('');
    }


    return (
        <>
           <div className="main-content">
                <div className="page-content">
                    <div className="container-fluid">
                        <Breadcrumb title="Default Salary" breadcrumbItems={breadcrumbItems} />
                        <ToastContainer />


<div className='card card-properties'>
    <div className='card-body'>
        <div className="container borders">
            <form onSubmit={handleSubmit}>
                <div className='container-fluid'>
                    <p className='msgFile'>All fields are mandatory *</p>
                    <div className='row mt-3'>
                        <div className='col-md-6'>
                            <div className="form-group">
                                <label htmlFor="gratuity">Gratuity</label>
                                <input type="text" className="form-control" id="gratuity" value={gratuity} onChange={(e) => setGratuity(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} required />
                            </div>
                        </div>

                        <div className='col-md-6'>
                            <div className="form-group">
                                <label htmlFor="hra">HRA</label>
                                <input type="text" className="form-control" id="hra" value={hra} onChange={(e) => setHra(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} required />
                            </div>
                        </div>
                    </div>

                    <div className='row'>
                        <div className='col-md-4'>
                            <div className="form-group">
                                <label htmlFor="profTax">Professional Tax</label>
                                <input type="text" className="form-control" id="profTax" value={profTax} onChange={(e) => setProfTax(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} required />
                            </div>
                        </div>

                        <div className='col-md-4'>
                            <div className="form-group">
                                <label htmlFor="medicalInsurance">Medical Insurance</label>
                                <input type="text" className="form-control" id="medicalInsurance" value={medicalInsurance} onChange={(e) => setMedicalInsurance(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} required />
                            </div>
                        </div>

                        <div className='col-md-4'>
                            <div className="form-group">
                                <label htmlFor="investments80C">Investments 80C</label>
                                <input type="text" className="form-control" id="investments80C" value={investments80C} onChange={(e) => setInvestments80C(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} required readOnly />
                            </div>
                        </div>
                    </div>

                    <div className="col-12 d-flex justify-content-center mt-5">
                        <button type="submit" className="btn btn-outline-primary">Submit</button>
                        <Link to={'/viewToDefaultSalary'}>
                            <button type="button" className="btn btn-outline-info ml-2">View</button>
                        </Link>      
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</div>
</div>
</div>
        </>
    );
}

export default DefaultValueSalary;
