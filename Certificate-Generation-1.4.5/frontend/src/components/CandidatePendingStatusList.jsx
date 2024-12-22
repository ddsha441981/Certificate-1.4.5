import { useEffect, useState } from "react";
import {API_BATCH_RUN_URL, API_PENDING_STATUS_URL } from "../env";
import axios from "axios";
import DualListBox from 'react-dual-listbox';
import 'react-dual-listbox/lib/react-dual-listbox.css';
import { showErrorNotification, showSuccessNotification } from "../notification/Notification";
import { ToastContainer } from "react-toastify";
import Breadcrumb from "./Breadcrumb";

const CandidatePendingStatusList = () => {

    const breadcrumbItems = [
        { name: 'Dashboard', link: '/dashboard', active: false },
        { name: 'View Candidates', link: '/candidateList', active: false },
        { name: `Batch Run`, link: `/candidatePendingStatusList`, active: true }
    ];

    const [selected, setSelected] = useState([]);
    const [options, setOptions] = useState([]);

    useEffect(() => {
        getPendingStatusList();
    }, []);

    const getPendingStatusList = async () => {
        try {
            const response = await axios.get(API_PENDING_STATUS_URL + '/PENDING');
            const data = response.data;
            const formattedOptions = formatOptions(data);
            setOptions(formattedOptions);
        } catch (error) {
            showErrorNotification("Error fetching pending batch list:", error);
        }
    };

    const formatOptions = (data) => {
        const companyMap = {};

        data.forEach(item => {
            const company = item.companyName; 
            if (!company) return;

            if (!companyMap[company]) {
                companyMap[company] = [];
            }

            companyMap[company].push({
                value: item.certificateId,
                label: item.candidateName,
            });
        });

        return Object.keys(companyMap).map(company => ({
            label: company,
            options: companyMap[company]
        }));
    };

    const sendSelectedData = async () => {
        try {
            const dataToSend = selected;
            console.log(dataToSend);

            // API call for batch run
            const response = await axios.post(API_BATCH_RUN_URL, { selectedIds: dataToSend });
            showSuccessNotification('Data sent successfully:', response.data);
            console.log(response.data);
            console.log("Data sent successfully");
            clearData();
        } catch (error) {
            showErrorNotification("Error sending selected data:", error);
        }
    };

    const clearData = () => {
        setSelected([]);
    };

    return (
        <>
         <div className="main-content">
                <div className="page-content">
                    <div className="container-fluid">
                        <Breadcrumb title="Pending Status" breadcrumbItems={breadcrumbItems} />
                        <div>
                        <ToastContainer/>
                        <DualListBox
                            canFilter
                            filterCallback={(option, filterInput, { getOptionLabel }) => {
                                if (filterInput === '') {
                                    return true;
                                }
                                return (new RegExp(filterInput, 'i')).test(getOptionLabel(option));
                            }}
                            options={options}  
                            selected={selected}
                            onChange={setSelected}
                        />
                            <div className="d-flex justify-content-center mt-3">
                                <button onClick={sendSelectedData} className="btn btn-outline-primary">Batch Run</button>
                            </div>

                    </div>
                </div>
            </div>
                        </div>
                
           
        </>
    );
};

export default CandidatePendingStatusList;