import React, { useState, useEffect } from 'react';
import "../components/myapp.css";
import { API_BASE_URL, API_BASE_URL_COMPANY, API_CTC_CALCULATION_URL } from '../env';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import { showErrorNotification, showInfoNotification, showSuccessNotification, showWarningNotification } from '../notification/Notification';
import "../components/myapp.css";

const CandidateRegister = () => {

    /* -------------------------------------------------------------------------- */
    /*                         Candidate Register Details                         */
    /* -------------------------------------------------------------------------- */

    const [companies, setCompanies] = useState([]);
    const [selectedCompany, setSelectedCompany] = useState('');
    const [candidateName, setCandidateName] = useState('');
    const [candidateEmail, setCandidateEmail] = useState('');
    const [offerDate, setOfferDate] = useState('');
    const [dateOfJoining, setDateOfJoining] = useState('');
    const [jobTitle, setJobTitle] = useState('');
    const [employeeCode, setEmployeeCode] = useState('');
    const [selectedDocumentTypes, setSelectedDocumentTypes] = useState([]);
    const [documentOptions, setDocumentOptions] = useState([]);
    const [selectAllDocuments, setSelectAllDocuments] = useState(false);
    const [companyName, setCompanyName] = useState('');

    /* -------------------------------------------------------------------------- */
    /*                                Salary Details State                        */
    /* -------------------------------------------------------------------------- */
    const [ctc, setCtc] = useState('');
    const [epf, setEpf] = useState(0.0);
    const [performanceBonus, setPerformanceBonus] = useState(0.0);
    const [gratuity, setGratuity] = useState('');
    const [hra, setHra] = useState('');
    const [profTax, setProfTax] = useState('');
    const [incomeTax, setIncomeTax] = useState('');
    const [medicalInsurance, setMedicalInsurance] = useState('');
    const [investments80C, setInvestments80C] = useState('');
    const [grossSalary, setGrossSalary] = useState('');
    const [inHandSalary, setInHandSalary] = useState('');
    const [monthlyGrossSalary, setMonthlyGrossSalary] = useState('');
    const [monthlyInHandSalary, setMonthlyInHandSalary] = useState('');

    /* -------------------------------------------------------------------------- */
    /*                                Bank Details State                          */
    /* -------------------------------------------------------------------------- */

    const [bankName, setBankName] = useState('');
    const [ifscCode, setIfscCode] = useState('');
    const [accountNo, setAccountNo] = useState('');
    const [accountHolderName, setAccountHolderName] = useState('');
    const [customerId, setCustomerId] = useState('');
    const [uanNumber, setUanNumber] = useState('');
    const [esiNumber, setEsiNumber] = useState('');

    /* -------------------------------------------------------------------------- */
    /*                        Identification Details State                        */
    /* -------------------------------------------------------------------------- */

    const [aadarNumber, setAadarNumber] = useState('');
    const [panNumber, setPanNumber] = useState('');
    const [documentType,setDocumentType] = useState('');
    const [documentData,setDocumentData] = useState([]);
  

    // const calculateCTC = async () => {
    //     alert("Call");
    //     e.preventDefault(); 
    //     const data = {
    //         ctc: ctc,
    //         epf: epf,
    //         performanceBonus: performanceBonus

    //     };
    //     try{
    //         const response = await fetch(`${API_CTC_CALCULATION_URL}`,data);
    //         console.log(response.data); 
    //     }catch(error){
    //         showErrorNotification('Error calculating salary');
    //     }
    //     //call backend api and fetch data from api
    //     //URL : http://localhost:9593/api/v6/salary/expose/?ctc=1200000&epf=21500&performanceBonus=50000
    //     //fetched data is : 
    //         // "ctc": 1200000.0,
    //         // "epf": 21500.0,
    //         // "gratuity": 14423.0,
    //         // "hra": 150000.0,
    //         // "profTax": 2400.0,
    //         // "incomeTax": 65797.7,
    //         // "medicalInsurance": 7200.0,
    //         // "investments80C": 21500.0,
    //         // "performanceBonus": 50000.0,
    //         // "grossSalary": 1214077.0,
    //         // "inHandSalary": 1138679.3,
    //         // "monthlyGrossSalary": 101173.08333333333,
    //         // "monthlyInHandSalary": 94889.94166666667        
    // }

    useEffect(() => {
        if (ctc || epf || performanceBonus) {
            calculateCTC();
        }
    }, [ctc, epf, performanceBonus]);

    const calculateCTC = async () => {
        // e.preventDefault();
        const params = new URLSearchParams({
            ctc,
            epf,
            performanceBonus
        });
    
        try {
            const response = await axios.get(`${API_CTC_CALCULATION_URL}?${params.toString()}`);
            const result = response.data;
            console.log(result);
            
            // Set the state with the result values
            setGratuity(result.gratuity);
            setHra(result.hra);
            setProfTax(result.profTax);
            setIncomeTax(result.incomeTax);
            setMedicalInsurance(result.medicalInsurance);
            setInvestments80C(result.investments80C);
            setGrossSalary(result.grossSalary);
            setInHandSalary(result.inHandSalary);
            setMonthlyGrossSalary(result.monthlyGrossSalary);
            setMonthlyInHandSalary(result.monthlyInHandSalary);
        } catch (error) {
            showErrorNotification('Error calculating salary');
            console.error('Error calculating CTC:', error);
        }
    };
    

    useEffect(() => {
        fetchCompanies();
    }, []);

    const fetchCompanies = async () => {
        try {
            const response = await axios.get(API_BASE_URL_COMPANY + '/activecompany');
            setCompanies(response.data);
            if (response.data && response.data.length === 0) {
                showErrorNotification(" Companies not found");
            }else{
                showSuccessNotification("Companies loaded successfully from server");
            }
            
        } catch (error) {
            showErrorNotification("Error loading data from the server");
            console.error('Error fetching companies:', error);
        }
    };

    const handleCompanyChange = async (e) => {
        const companyId = e.target.value;
        const companyName = e.target.options[e.target.selectedIndex].text;
        setSelectedCompany(companyId);
        fetchDocuments(companyId);
        setCompanyName(companyName);
    };


    useEffect(() => {
        if (selectedCompany) {
            fetchDocuments(selectedCompany);
        }
    }, [selectedCompany]);

    const fetchDocuments = async (companyId) => {
        try {
            const response = await axios.get(`${API_BASE_URL_COMPANY}/documents/${companyId}`);
            
            if (response.data && typeof response.data === 'object') {
                const documentUrls = Object.values(response.data);
                setDocumentOptions(documentUrls.map(url => ({ value: url, label: url })));
                setSelectedDocumentTypes([]); 
                setSelectAllDocuments(false);
                if (documentUrls.length > 0) {
                    showSuccessNotification("Documents loaded successfully from server");
                }else{
                    showErrorNotification("No documents found for the selected company");
                }
            } else {
                showErrorNotification("Invalid response: documents not found or not in the expected format");
                console.error('Invalid response: documents not found or not in the expected format');
            }
        } catch (error) {
            showErrorNotification("Error loading data from the server");
            console.error('Error fetching documents:', error);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!selectedCompany) {
            showWarningNotification('Please select a company.');
            return;
        }

        if (!selectAllDocuments || selectedDocumentTypes.length !== documentOptions.length) {
            showInfoNotification('Please select all documents.');
            return;
        }

        try {
            const emailExistsResponse = await axios.post(API_BASE_URL + '/check-email', {
                candidateEmail
            });
    
            if (emailExistsResponse.data.emailExists) {
                showWarningNotification("Your email address already exists in our database!!");
                return;
            }

            const formData = new FormData();
            formData.append('docs', documentData);

            const salaryExpose = {
                ctc,
                epf,
                gratuity,
                hra,
                profTax,
                incomeTax,
                medicalInsurance,
                investments80C,
                performanceBonus,
                grossSalary,
                inHandSalary,
                monthlyGrossSalary,
                monthlyInHandSalary
              };

            const bank = {
                bankName,
                ifscCode,
                accountNo,
                accountHolderName,
                customerId,
                uanNumber,
                esiNumber
            };

            const identificationDetails = {
                aadarNumber,
                panNumber,
                documentType,
            };

            const certificateData = {
                candidateName,
                selectedCompany,
                companyName,
                offerDate,
                dateOfJoining,
                jobTitle,
                employeeCode,
                candidateEmail,
                salaryExpose,
                selectedDocumentTypes,
                bank,
                identificationDetails
            };

            formData.append('formData', JSON.stringify(certificateData));

            const response = await axios.post(API_BASE_URL + '/add/file',formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
           
            console.log(response.data);
            showSuccessNotification('Data added successfully to the server');
            clearForm();
            
        } catch (error) {
            showErrorNotification('Error adding data to the server');
            console.error('Error:', error);
        }
    };

    const handleSelectedDocs = (e)=>{
        const selectedFile = e.target.files[0];
       
        if (selectedFile) {
            setDocumentData(selectedFile);
        } else {
           
            const defaultFile = '../assets/images/logo.png';
            const defaultFileObject = new File([defaultFile], 'default.png', { type: 'image/png' });
            setDocumentData(defaultFileObject);
        }

    }



    const clearForm = () => {
        setCandidateName('');
        setSelectedCompany('');
        setCompanyName('');
        setOfferDate('');
        setDateOfJoining('');
        setJobTitle('');
        setEmployeeCode('');
        setCandidateEmail('');
        setSelectedDocumentTypes([]);
        setDocumentOptions([]);
    };

    const handleSelectAllDocuments = (e) => {
        const isChecked = e.target.checked;
        setSelectAllDocuments(isChecked);
        if (isChecked) {
            setSelectedDocumentTypes(documentOptions.map(option => option.value));
        } else {
            setSelectedDocumentTypes([]);
        }
    };

    

    return (
        <>
            <ToastContainer/>
           <div className='card'>
                <div className='card-header'>
                    <h4 className='card-title text-center'>Candidate Register</h4>
                </div>
            </div>
           

            <div className="card card-properties">
           
                <div className="card-body">
                    
                    <form onSubmit={handleSubmit}>
                        <input type="hidden" name="companyName" value={companyName} />
                        <p className='msgFile'>All Mandatory field *</p>
                            <div className="row mt-3">
                                <div className="col-md-6">
                                    <div className="form-group">
                                        <label htmlFor="candidateName">Candidate Name *</label>
                                        <input type="text" className="form-control" id="candidateName" name="candidateName" value={candidateName} onChange={(e) => setCandidateName(e.target.value)} required />
                                    </div>
                                </div>

                                <div className="col-md-6">
                                    <div className="form-group">
                                        <label htmlFor="company" className="form-label">Select Company *</label>
                                        <select
                                            className="form-control"
                                            id="company"
                                            name="company"
                                            value={selectedCompany}
                                            onChange={handleCompanyChange}
                                            required
                                        >
                                            <option value="">Select Company</option>
                                            {companies.map(company => (
                                                <option key={company.companyId} value={company.companyId}>{company.companyName}</option>
                                            ))}
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div className="row">

                            <div className="col-md-3">
                                <div className="form-group">
                                    <label htmlFor="offerDate" className="form-label">Offer Date *</label>
                                    <input type="date" className="form-control" id="offerDate" name="offerDate" value={offerDate} onChange={(e) => setOfferDate(e.target.value)} required />
                                </div>
                            </div>

                                <div className="col-md-3">
                                    <div className="form-group">
                                    <label htmlFor="dateOfJoining" className="form-label">Date of Joining *</label>
                                    <input type="date" className="form-control" id="dateOfJoining" name="setDateOfJoining" value={dateOfJoining} onChange={(e) => setDateOfJoining(e.target.value)} required />

                                </div>
                            </div>

                                <div className="col-md-6">
                                    <div className="form-group">
                                        <label htmlFor="candidateEmail" className="form-label">Email *</label>
                                        <input type="email" className="form-control" id="candidateEmail" name="candidateEmail" value={candidateEmail} onChange={(e) => setCandidateEmail(e.target.value)} required />

                                    </div>
                                </div>

                            </div>

                            <div className="row">
                                <div className="col-md-6">
                                    <div className="form-group">
                                        <label htmlFor="jobTitle" className="form-label">Job Title * </label>
                                        <input type="text" className="form-control" id="jobTitle" name="jobTitle" value={jobTitle} onChange={(e) => setJobTitle(e.target.value)} required />

                                    </div>
                                </div>

                                <div className="col-md-6">
                                    <div className="form-group">
                                        <label htmlFor="employeeCode" className="form-label">Employee Code *</label>
                                        <input type="text" className="form-control" id="employeeCode" name="employeeCode" value={employeeCode} onChange={(e) => setEmployeeCode(e.target.value)} required />

                                    </div>
                                </div>
                            </div>

                                <div class="line text-center mt-3"><span> Salary Calculation </span></div>
                            <p className='msgFile'>Note* This salary calculator takes into account Employee Provident Fund (EPF) contributions and allows for the optional inclusion of Performance Bonus.</p>

                            <div className="row mt-3">
                                <div className="col-md-4">
                                    <div className="form-group">
                                        <label htmlFor="ctc" className="form-label">CTC * </label>
                                        <input type="text" className="form-control" id="ctc" value={ctc} onChange={(e) => setCtc(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} />
                                    </div>
                                </div>

                                <div className="col-md-4">
                                    <div className="form-group">
                                        <label htmlFor="epf" className="form-label">EPF</label>
                                        <input type="text" className="form-control" id="epf" value={epf} onChange={(e) => setEpf(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} />
                                    </div>
                                </div>

                                <div className="col-md-4">
                                    <div className="form-group">
                                        <label htmlFor="performanceBonus" className="form-label">Performance Bonus</label>
                                        <input type="text" className="form-control" id="performanceBonus" value={performanceBonus} onChange={(e) => setPerformanceBonus(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} />
                                    </div>
                                </div>

                            </div>

                            <div className='row'>

                                <div className="col-md-4">
                                    <div className="form-group">
                                        <label htmlFor="gratuity" className="form-label">Gratuity</label>
                                        <input type="text" className="form-control" id="gratuity" value={gratuity}  onChange={(e) => setGratuity(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} readOnly />
                                    </div>
                                </div>

                                <div className="col-md-4">
                                    <div className="form-group">
                                        <label htmlFor="hra" className="form-label">HRA</label>
                                        <input type="text" className="form-control" id="hra" value={hra}  onChange={(e) => setHra(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} readOnly />
                                    </div>
                                </div>

                                <div className="col-md-4">
                                    <div className="form-group">
                                        <label htmlFor="profTax" className="form-label">Profesional Tax</label>
                                        <input type="text" className="form-control" id="profTax" value={profTax}  onChange={(e) => setProfTax(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} readOnly />
                                    </div>
                                </div>
                            </div>


                            <div className='row'>
                                <div className="col-md-4">
                                    <div className="form-group">
                                        <label htmlFor="incomeTax" className="form-label">Income Tax</label>
                                        <input type="text" className="form-control" id="incomeTax" value={incomeTax}  onChange={(e) => setIncomeTax(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} readOnly />
                                    </div>
                                </div>

                                <div className="col-md-4">
                                    <div className="form-group">
                                        <label htmlFor="medicalInsurance" className="form-label">Medical Insurance</label>
                                        <input type="text" className="form-control" id="medicalInsurance" value={medicalInsurance}  onChange={(e) => setMedicalInsurance(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} readOnly />
                                    </div>
                                </div>

                                <div className="col-md-4">
                                    <div className="form-group">
                                        <label htmlFor="investments80C" className="form-label">Investments 80C Tax</label>
                                        <input type="text" className="form-control" id="investments80C" value={investments80C}  onChange={(e) => setInvestments80C(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} readOnly />
                                    </div>
                                </div>
                            </div>


                            <div className='row'>
                                <div className="col-md-2">
                                    <div className="form-group">
                                        <label htmlFor="grossSalary" className="form-label">Gross Salary </label>
                                        <input type="text" className="form-control" id="grossSalary" value={grossSalary}  onChange={(e) => setGrossSalary(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} readOnly />
                                    </div>
                                </div>

                                <div className="col-md-2">
                                    <div className="form-group">
                                        <label htmlFor="inHandSalary" className="form-label">In Hand Salary</label>
                                        <input type="text" className="form-control" id="inHandSalary" value={inHandSalary}  onChange={(e) => setInHandSalary(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} readOnly />
                                    </div>
                                </div>

                                <div className="col-md-4">
                                    <div className="form-group">
                                        <label htmlFor="monthlyGrossSalary" className="form-label">Monthly Gross Salary</label>
                                        <input type="text" className="form-control" id="monthlyGrossSalary" value={monthlyGrossSalary}  onChange={(e) => setMonthlyGrossSalary(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} readOnly />
                                    </div>
                                </div>

                                <div className="col-md-4">
                                    <div className="form-group">
                                        <label htmlFor="monthlyInHandSalary" className="form-label">Monthly In Hand Salary</label>
                                        <input type="text" className="form-control" id="monthlyInHandSalary" value={monthlyInHandSalary}  onChange={(e) => setMonthlyInHandSalary(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()} readOnly />
                                    </div>
                                </div>
                            </div>

                            <div className="row">
                                <div className="form-check">
                                    <input
                                        type="checkbox"
                                        className="form-check-input"
                                        id="selectAllDocuments"
                                        checked={selectAllDocuments}
                                        onChange={handleSelectAllDocuments}
                                    />
                                    <label className="form-check-label" htmlFor="selectAllDocuments">Select All Documents</label>
                                </div>
                                <p className='msgFile'>* Warning: Please Ensure All Documents are Selected Before Submitting , changes cannot be made thereafter.</p>
                            </div>

                            <div class="line text-center mt-3"><span> Bank </span></div>
                            <p className='msgFile'>If you choose not to add bank details, you may skip this step. It is optional and can be added after registration</p>

                            <div className="row mt-3">
                                    <div className="col-md-3">
                                        <div className="form-group">
                                            <label htmlFor="aadharNumber" className="form-label">Bank Name</label>
                                            <input type="text" className="form-control" id="aadharNumber" name="aadharNumber" value={bankName} onChange={(e) => setBankName(e.target.value)} />
                                        </div>
                                    </div>
                                    <div className="col-md-3">
                                        <div className="form-group">
                                            <label htmlFor="ifscCode" className="form-label">IFSC Code</label>
                                            <input type="text" className="form-control" id="ifscCode" name="ifscCode" value={ifscCode} onChange={(e) => setIfscCode(e.target.value)}  />
                                        </div>
                                    </div>

                                    <div className="col-md-3">
                                        <div className="form-group">
                                            <label htmlFor="accountNo" className="form-label">Account Number</label>
                                            <input type="text" className="form-control" id="accountNo" name="accountNo" value={accountNo} onChange={(e) => setAccountNo(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()}  />
                                        </div>
                                    </div>

                                    <div className="col-md-3">
                                        <div className="form-group">
                                            <label htmlFor="accountHolderName" className="form-label">Account Holder Name</label>
                                            <input type="text" className="form-control" id="accountHolderName" name="accountHolderName" value={accountHolderName} onChange={(e) => setAccountHolderName(e.target.value)}  />
                                        </div>
                                    </div>
                             </div>

                             <div className='row'>
                                <div className="col-md-4">
                                    <div className="form-group">
                                    <label htmlFor="customerId" className="form-label">Customer Id</label>
                                    <input type="text" className="form-control" id="customerId" name="customerId" value={customerId} onChange={(e) => setCustomerId(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()}  />
                                    </div>
                                </div>

                                <div className="col-md-4">
                                    <div className="form-group">
                                    <label htmlFor="customerId" className="form-label">PF/UAN Number</label>
                                    <input type="text" className="form-control" id="customerId" name="customerId" value={uanNumber} onChange={(e) => setUanNumber(e.target.value)}  />
                                    </div>
                                </div>
                                <div className="col-md-4">
                                    <div className="form-group">
                                    <label htmlFor="customerId" className="form-label">E.S.I Number</label>
                                    <input type="text" className="form-control" id="customerId" name="customerId" value={esiNumber} onChange={(e) => setEsiNumber(e.target.value)}  />
                                    </div>
                                </div>
                            </div>

                            <div class="line text-center"><span>Identification</span></div>
                            <p className='msgFile'>If you choose not to add identification details, you may skip this step. It is optional and can be added after registration</p>

                            <div className="row mt-3">
                                <div className="col-md-6">
                                    <div className="form-group">
                                        <label htmlFor="aadarNumber" className="form-label">Aadhar Number</label>
                                        <input type="text" className="form-control" id="aadarNumber" name="aadarNumber" value={aadarNumber} 
                                        onChange={(e) => {
                                            const inputVal = e.target.value.replace(/\D/g, '');
                                            const formattedInput = inputVal.replace(/(\d{4})(?=\d)/g, '$1 ');
                                            if (formattedInput.length <= 14) {
                                                setAadarNumber(formattedInput);
                                                }
                                            }} 
                                             
                                        />
                                    </div>
                                </div>
                                <div className="col-md-6">
                                    <div className="form-group">
                                        <label htmlFor="panNumber" className="form-label">PAN Number</label>
                                        <input type="text" className="form-control" id="panNumber" name="panNumber" value={panNumber} 
                                         onChange={(e) => {
                                            const inputVal = e.target.value.toUpperCase().replace(/[^A-Z0-9]/g, '');
                                            if (inputVal.length <= 10) { 
                                                setPanNumber(inputVal);
                                            }
                                        }} 
                                         
                                    />
                                    </div>
                                </div>
                            </div>


                            <div className='row'>
                                <div className="col-md-6">
                                    <div className="form-group">
                                        <label htmlFor="documentType">Document Type</label>
                                        <select name='documentType' id='documentType' className='form-control'value={documentType} onChange={(e) => setDocumentType(e.target.value.replace(/\D/g, ''))} onPaste={(e) => e.preventDefault()}>
                                        <option value="">Select Document Type</option>
                                        <option value="aadar">Aadar</option>
                                        <option value="pancard">Pancard</option>
                                       
                                        </select>
                                    </div>
                                </div>

                                <div className='col-md-6'>
                                    <div className="form-group">
                                    <label htmlFor="documentData">
                                        {documentType ? null : 'Upload'}
                                        {documentType === 'aadar' ? ' Upload Aadar Card' : null}
                                        {documentType === 'pancard' ? '  Upload PAN Card' : null}
                                    </label>
                                        <input type="file" className="form-control" id="documentData" name="documentData" onChange={handleSelectedDocs} accept=".png"/>
                                        <p className='msgFile'>Note: If you prefer not to upload a document, you can proceed by uploading a blank file. This is acceptable</p>
                                    </div>
                                </div>
                                
                            </div>



                            <div className="col-12 d-flex justify-content-center mt-3">
                                <button type="submit" className="btn btn-primary">Submit</button>
                                <Link to={'/candidateList'}>
                                    <button type="button" className="btn btn-primary ml-2">View</button>
                                </Link>
                                <Link to={'/companies'}>
                                    <button type="button" className="btn btn-primary ml-2">Company</button>
                                </Link>
                                {/* <Link to={'/bank'}>
                                    <button type="button" className="btn btn-primary ml-2">Bank</button>
                                </Link>
                                <Link to={'/identificationDetails'}>
                                    <button type="button" className="btn btn-primary ml-2">Identification</button>
                                </Link> */}
                            </div>
                    </form>
                </div>
            </div>

        </>
    );
};

export default CandidateRegister;
