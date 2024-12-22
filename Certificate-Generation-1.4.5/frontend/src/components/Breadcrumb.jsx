import React from 'react';
import { Link } from 'react-router-dom';
import "../components/myapp.css";

const Breadcrumb = ({ title, breadcrumbItems }) => {
    return (
        <div className="page-title-box d-sm-flex align-items-center justify-content-between">
            <h4 className="mb-sm-0 font-size-18">{title}</h4>
            <div className="page-title-right">
                <ol className="breadcrumb m-0" style={{ display: 'flex', alignItems: 'center' }}>
                    {breadcrumbItems.map((item, index) => (
                        <React.Fragment key={index}>
                            <li className={`breadcrumb-item ${item.active ? 'active' : ''}`}>
                                {item.active ? (
                                    <span>{item.name}</span>
                                ) : (
                                    <Link to={item.link}>{item.name}</Link>
                                )}
                            </li>
                            {index < breadcrumbItems.length - 1 && (
                                //Use own format as per requiremnt 
                                
                                //Default
                                // <strong className="breadcrumb-separator" style={{ margin: '0 5px' }}>/</strong>
                                // <strong className="breadcrumb-separator" style={{ margin: '0 5px' }}>|</strong>
                                <strong className="breadcrumb-separator" style={{ margin: '0 5px' }}>»</strong>
                                // <strong className="breadcrumb-separator" style={{ margin: '0 5px' }}>›</strong>
                                // <strong className="breadcrumb-separator" style={{ margin: '0 5px' }}>→</strong>
                                // <strong className="breadcrumb-separator" style={{ margin: '0 5px' }}>⇒</strong>
                                // <strong className="breadcrumb-separator" style={{ margin: '0 5px' }}> → </strong>
                                // <strong className="breadcrumb-separator" style={{ margin: '0 5px' }}>▷</strong>
                                // <strong className="breadcrumb-separator" style={{ margin: '0 5px' }}>▸</strong>
                                // <strong className="breadcrumb-separator" style={{ margin: '0 5px' }}>~</strong>

                            )}
                        </React.Fragment>
                    ))}
                </ol>
            </div>
        </div>
    );
};


export default Breadcrumb;