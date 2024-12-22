import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { API_FOLDER_VIEW_URL } from '../env';
import Breadcrumb from './Breadcrumb';

const FolderView = () => {
    const [folderData, setFolderData] = useState(null);
    const [error, setError] = useState(null);
    const [selectedCompany, setSelectedCompany] = useState(null); 
    const [selectedUserFolder, setSelectedUserFolder] = useState(null); 

    const breadcrumbItems = [
        { name: 'Dashboard', link: '/dashboard', active: false },
        { name: `Google Drive`, link: `/folderView`, active: true }
    ];
   
    useEffect(() => {
        const fetchFolderData = async () => {
            try {
                const response = await axios.get(API_FOLDER_VIEW_URL, {
                    params: { folderName: 'PDF Documents' }
                });
                console.log('API Response:', response.data);
                setFolderData(response.data);
            } catch (error) {
                setError('Error fetching folder data');
                console.error('Error fetching folder data:', error);
            }
        };

        fetchFolderData();
    }, []);

    
    const handleCompanyClick = (company) => {
        console.log('Selected Company:', company); 
        setSelectedCompany(company);
        setSelectedUserFolder(null); 
    };


    const handleUserFolderClick = (userFolder) => {
        console.log('Selected User Folder:', userFolder); 
        setSelectedUserFolder(userFolder);
    };

    
    const renderFiles = (files) => {
        if (!files || files.length === 0) {
            return <div className="text-muted">No files available</div>;
        }
        return (
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '1rem' }}>
                {files.map((file) => (
                    <div className="card" key={file.fileId} style={{ width: '18rem' }}>
                        <div className="card-body text-center">
                            <i className="fas fa-file-pdf text-danger mb-2" style={{ fontSize: '2rem' }}></i>
                            <a href={file.downloadUrl} target="_blank" rel="noopener noreferrer" className="text-muted small">
                                {file.fileName}
                            </a>
                        </div>
                    </div>
                ))}
            </div>
        );
    };

    const renderUserFolders = (users) => {
        if (!users || users.length === 0) {
            return <div className="text-muted">No subfolders available for this company</div>;
        }
        return (
            <div className="d-flex flex-wrap">
                {users.map((userFolder) => (
                    <div key={userFolder.folderId} className="mb-3">
                        <div 
                            className="d-flex align-items-center" 
                            style={{ cursor: 'pointer' }}
                            onClick={() => handleUserFolderClick(userFolder)}
                        >
                           
                            <img 
                                src={userFolder.profileImageUrl || `https://picsum.photos/seed/${userFolder.folderId}/50`} 
                                alt={userFolder.folderName} 
                                className="rounded-circle me-2"
                                style={{ width: '50px', height: '50px', objectFit: 'cover' }}
                            />
                            <h6 className="card-subtitle text-center text-muted">
                                {userFolder.folderName}
                            </h6>
                        </div>

                       
                        {selectedUserFolder && selectedUserFolder.folderId === userFolder.folderId && (
                            <div className="mt-2">
                                {renderFiles(userFolder.files)}
                            </div>
                        )}
                    </div>
                ))}
            </div>
        );
    };

  
    const renderCompanies = (companies) => {
        if (!companies || companies.length === 0) {
            return <div className="text-muted">No companies available</div>;
        }
        return companies.map((company) => (
            <div key={company.folderId} className="mb-3">
                <div 
                    className="d-flex align-items-center"
                    style={{ cursor: 'pointer' }}
                    onClick={() => handleCompanyClick(company)}
                >
                  
                    <img 
                        src={company.logoUrl || `https://picsum.photos/seed/${company.folderId}/50`}  
                        alt={company.folderName} 
                        className="me-3"
                        style={{ width: '50px', height: '50px', objectFit: 'cover' }}
                    />
                    <h5 className="card-title">
                        {company.folderName}
                    </h5>
                </div>
                
                {selectedCompany && selectedCompany.folderId === company.folderId && renderUserFolders(company.users)}
            </div>
        ));
    };

    if (error) {
        return <div>{error}</div>;
    }

    if (!folderData) {
        return <div>Loading...</div>;
    }

    return (
        <div className="main-content">
                <div className="page-content">
                    <div className="container-fluid">
                        <Breadcrumb title="Google Drive" breadcrumbItems={breadcrumbItems} />
                        <div>
                        <div className="row">
                        <div className="col-12">
                            <h1>{folderData.folderName}</h1>

                           
                            <div className="card mb-4">
                                <div className="card-body">
                                    <h2>Companies</h2>
                                    {renderCompanies(folderData.companies)}
                                </div>
                            </div>

                         
                            <div className="card mb-4">
                                <div className="card-body">
                                    <h2>All Files</h2>
                                    {renderFiles(folderData.files)}
                                </div>
                            </div>

                        </div>
                    </div>

                </div>
            </div>
        </div>
        </div>
    );
                      
                   

                    
};

export default FolderView;