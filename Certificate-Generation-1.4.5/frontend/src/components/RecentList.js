import { API_BASE_URL, API_BASE_URL_COMPANY } from '../env';
import React, { useEffect , useState} from 'react';
import axios from 'axios';


const RecentList = () => {

    const [recentCandidates, setRecentCandidates] = useState([]);
    const [recentCompanies, setRecentCompanies] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchTopFiveData();
    }, []);

    const fetchTopFiveData = async () => {
        try {
            const responseCandidates = await axios.get(API_BASE_URL + '/topfive');
            setRecentCandidates(responseCandidates.data);
            
            const responseCompanies = await axios.get(API_BASE_URL_COMPANY + '/topFive');
            console.log(responseCompanies.data);
            setRecentCompanies(responseCompanies.data);
           
            
            setLoading(false); 
        } catch(error){
            console.error('Error fetching data:', error);
            
            setLoading(false); 
        }
    }

    return (
        <>
       
        <div>
            {loading ? (
                <p>Loading...</p>
            ) : (
                <div className='row mt-5'>
                    {/* Recent Candidates */}
                    <div className="col-md-6">
                        <div className="card">
                            <div className="card-body">
                                <h6 className='text-center'>Recent Candidates</h6>
                                {recentCandidates.length > 0 ? (
                                    <table className="table">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Candidate</th>
                                                <th>Company</th>
                                                <th>Salary</th>
                                                <th>Status</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {recentCandidates.map((candidates, index) => (
                                                <tr key={index}>
                                                    <td>{index + 1}</td>
                                                    <td>{candidates.candidateName}</td>
                                                    <td>{candidates.companyName}</td>
                                                    <td>{candidates.finalFixedSalary}</td>
                                                    <td><span className="badge rounded-pill bg-warning text-dark">{candidates.changeStatus}</span></td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                ) : (
                                    <p>No recent candidates found.</p>
                                )}
                            </div>
                        </div>
                    </div>
                    {/* Recent Companies */}
                    <div className="col-md-6">
                        <div className="card">
                            <div className="card-body">
                                <h6 className='text-center'>Recent Companies</h6>
                                {recentCompanies.length > 0 ? (
                                    <table className="table">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Company Name</th>
                                                <th>Status</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {recentCompanies.map((company, index) => (
                                                <tr key={index}>
                                                    <td>{index + 1}</td>
                                                    <td>{company.companyName}</td>
                                                    <td><span className="badge rounded-pill bg-success">{company.changeStatus}</span></td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                ) : (
                                    <p>No recent companies found.</p>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
        
        </>
    );
};

export default RecentList;
