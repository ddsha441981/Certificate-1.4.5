import React, { useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import {API_DEFAULT_TAX_VALUE_URL } from "../../env";
import { showSuccessNotification, showErrorNotification } from "../../notification/Notification";
import Breadcrumb from '../Breadcrumb';

const TaxSlab = () => {

    const [slabLimit, setSlabLimit] = useState('');
    const [taxRate, setTaxRate] = useState('');

    const breadcrumbItems = [
        { name: 'Dashboard', link: '/dashboard', active: false },
        { name: 'View Companies', link: '/companies', active: false },
        { name: 'View Tax', link: '/viewToTaxSlab', active: false },
        { name: `Add Tax`, link: `/addTaxSlab`, active: true }
    ];

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
           const response = await axios.post(`${API_DEFAULT_TAX_VALUE_URL}/`, { 
            slabLimit, 
            taxRate
        });
    
            showSuccessNotification("Default tax data added successfully.");
            console.log(response.data); 
            clearForm();
        } catch (error) {
            showErrorNotification("Error adding Default tax Data");
            console.error('Error:', error);
        }


    }
    
    function clearForm() {
        setSlabLimit('');
        setTaxRate('');
    }


    return (
        <>
             <div className="main-content">
                <div className="page-content">
                    <div className="container-fluid">
                        <Breadcrumb title="Tax" breadcrumbItems={breadcrumbItems} />
                        <div>
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
                                            <label htmlFor="gratuity">Slab Tax</label>
                                            <input type="text" className="form-control" id="gratuity" value={slabLimit} onChange={(e) => setSlabLimit(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} required />
                                        </div>
                                    </div>

                                    <div className='col-md-6'>
                                        <div className="form-group">
                                            <label htmlFor="hra">Tax Rate</label>
                                            <input type="text" className="form-control" id="hra" value={taxRate} onChange={(e) => setTaxRate(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} required />
                                        </div>
                                    </div>
                                </div>

                                <div className="col-12 d-flex justify-content-center mt-5">
                                    <button type="submit" className="btn btn-outline-primary">Submit</button>
                                    <Link to={'/viewToTaxSlab'}>
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
            </div>
        </>
    );
}

export default TaxSlab;
