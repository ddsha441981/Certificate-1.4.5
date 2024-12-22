import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { API_FOLDER_VIEW_URL } from '../env';


const FolderView = () => {
    const [folderData, setFolderData] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchFolderData = async () => {
            try {
                const response = await axios.get(API_FOLDER_VIEW_URL, {
                    params: { folderName: 'PDF Documents' }
                });
                setFolderData(response.data);
            } catch (error) {
                setError('Error fetching folder data');
                console.error('Error fetching folder data:', error);
            }
        };

        fetchFolderData();
    }, []);

    const renderFiles = (files) => {
        if (!files || files.length === 0) {
            return <div className="text-muted">No files available</div>;
        }
        return (
            <div className="row">
                {files.map((file) => (
                    <div className="col-md-3 mb-4" key={file.fileId}>
                        <div className="card h-100">
                            <div className="card-body d-flex flex-column align-items-center">
                                <i className="fas fa-file-pdf text-danger mb-2" style={{ fontSize: '2rem' }}></i>
                                <a href={file.downloadUrl} target="_blank" rel="noopener noreferrer" className="text-muted small">
                                    {file.fileName}
                                </a>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        );
    };

    const renderSubfolders = (subfolders, title) => {
        if (!subfolders || subfolders.length === 0) {
            return null;
        }
        return (
            <div className="card mb-4">
                <div className="card-body">
                    <h5 className="card-title">{title}</h5>
                    {subfolders.map((subfolder) => (
                        <div key={subfolder.folderId} className="mb-3">
                            <h6 className="card-subtitle mb-2 text-muted">{subfolder.folderName}</h6>
                            {renderFiles(subfolder.files)}
                            {renderSubfolders(subfolder.companies, 'Companies')}
                            {renderSubfolders(subfolder.users, 'Users')}
                        </div>
                    ))}
                </div>
            </div>
        );
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
                    <div className="row">
                        <div className="col-12">
                            <div className="page-title-box d-sm-flex align-items-center justify-content-between">
                                <h4 className="mb-sm-0 font-size-18">Dashboard</h4>
                                <div className="page-title-right">
                                    <ol className="breadcrumb m-0">
                                        <li className="breadcrumb-item"><a href="javascript: void(0);">Dashboard</a></li>
                                        <li className="breadcrumb-item active">Folder View</li>
                                    </ol>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="row">
                        <div className="col-12">
                            <h1>{folderData.folderName}</h1>

                           
                            <div className="card mb-4">
                                <div className="card-body">
                                    <h2>Companies</h2>
                                    {renderSubfolders(folderData.companies, 'Companies')}
                                </div>
                            </div>

                           
                            <div className="card mb-4">
                                <div className="card-body">
                                   
                                    {renderSubfolders(folderData.users, 'Users')}
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
    );
};

export default FolderView;
