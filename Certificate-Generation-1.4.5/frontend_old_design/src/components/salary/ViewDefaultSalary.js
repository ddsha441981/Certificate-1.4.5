import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { API_DEFAULT_SALARY_VALUE_URL } from '../../env';

const ViewDefaultSalary = () => {
    const [defaultSalary, setDefaultSalary] = useState([]);
    const id = 1; // Static ID

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(`${API_DEFAULT_SALARY_VALUE_URL}/${id}`);
                console.log(response.data);
                if (Array.isArray(response.data)) {
                    setDefaultSalary(response.data);
                } else if (typeof response.data === 'object') {
                    setDefaultSalary([response.data]);
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
        console.log('Edit:', id);
    };

    const handleDelete = async (id) => {
        try {
            await axios.delete(`${API_DEFAULT_SALARY_VALUE_URL}/${id}`);
            setDefaultSalary(defaultSalary.filter(item => item.id !== id));
        } catch (error) {
            console.error('Error deleting data:', error);
        }
    };

    return (
        <>
            <div className='card'>
                <div className='card-body'>
                    <div className='card'>
                        <div className='card-header'>
                            <h4 className='card-title text-center'>Default Salary</h4>
                        </div>
                    </div>
                    <div className="table-responsive">
                        <div>
                            <Link to={'/addDefaultSalary'}>
                                <button type="button" className="btn btn-primary" style={{ marginLeft: '15px', margin: '10px' }}>Add Salary</button>
                            </Link>
                        </div>

                        <table className="table table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Gratuity</th>
                                    <th>HRA</th>
                                    <th>Professional Tax</th>
                                    <th>Medical Insurance</th>
                                    <th>Investment</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {defaultSalary.length === 0 ? (
                                    <tr>
                                        <td colSpan="7" className='text-center'>No data found.</td>
                                    </tr>
                                ) : (
                                    defaultSalary.map((defaultValue, index) => (
                                        <tr key={defaultValue.id}>
                                            <td>{index + 1}</td>
                                            <td>{defaultValue.gratuity}</td>
                                            <td>{defaultValue.hra}</td>
                                            <td>{defaultValue.profTax}</td>
                                            <td>{defaultValue.medicalInsurance}</td>
                                            <td>{defaultValue.investments80C}</td>
                                            <td>
                                                <button type="button" className="btn btn-primary" style={{ margin: '10px' }} onClick={() => handleUpdate(defaultValue.id)}>Edit</button>
                                                <button type="button" className="btn btn-danger" style={{ margin: '10px' }} onClick={() => handleDelete(defaultValue.id)}>Delete</button>
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </>
    );
};

export default ViewDefaultSalary;
