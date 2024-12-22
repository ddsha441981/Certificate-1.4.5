import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { API_DEFAULT_TAX_VALUE_URL } from '../../env';
import Breadcrumb from '../Breadcrumb';

const ViewTaxSlab = () => {
    const [defaultTax, setDefaultTax] = useState([]);
    const breadcrumbItems = [
        { name: 'Dashboard', link: '/dashboard', active: false },
        { name: 'View Companies', link: '/companies', active: false },
        { name: 'View Tax', link: '/viewToTaxSlab', active: true }
    ];

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(`${API_DEFAULT_TAX_VALUE_URL}/slabList`);
                console.log(response.data);
                if (Array.isArray(response.data)) {
                    setDefaultTax(response.data);
                } else if (typeof response.data === 'object') {
                    setDefaultTax([response.data]);
                } else {
                    console.error('Unexpected response data:', response.data);
                }
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, []);

    const handleUpdate = (id) => {
        // Navigate to update page or handle update logic
        console.log('Edit:', id);
    };

    const handleDelete = async (id) => {
        try {
            await axios.delete(`${API_DEFAULT_TAX_VALUE_URL}/${id}`);
            setDefaultTax(defaultTax.filter(item => item.id !== id));
        } catch (error) {
            console.error('Error deleting data:', error);
        }
    };

    return (
        <>
             <div className="main-content">
                <div className="page-content">
                    <div className="container-fluid">
                        <Breadcrumb title="Default Tax Slab" breadcrumbItems={breadcrumbItems} />
                        <div>
                        <div className='card'>
                <div className='card-body'>
                   
                    <div className="table-responsive">
                        <div>
                            <Link to={'/addTaxSlab'}>
                                <button type="button" className="btn btn-outline-primary" style={{ marginLeft: '15px', margin: '10px' }}>Add Tax</button>
                            </Link>
                        </div>

                        <table className="table table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th>S.No</th>
                                    <th>Slab Limit</th>
                                    <th>Tax Rate</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {defaultTax.length === 0 ? (
                                    <tr>
                                        <td colSpan="7" className='text-center'>No data found.</td>
                                    </tr>
                                ) : (
                                    defaultTax.map((defaultValue, index) => (
                                        <tr key={defaultValue.id}>
                                            <td>{index + 1}</td>
                                            <td>{defaultValue.slabLimit}</td>
                                            <td>{defaultValue.taxRate}</td>
                                            <td>
                                                <button type="button" className="btn btn-outline-primary" style={{ margin: '10px' }} onClick={() => handleUpdate(defaultValue.id)}>Edit</button>
                                                <button type="button" className="btn btn-outline-danger" style={{ margin: '10px' }} onClick={() => handleDelete(defaultValue.id)}>Delete</button>
                                            </td>
                                        </tr>
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

export default ViewTaxSlab;
