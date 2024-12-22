import React, { useEffect, useState } from "react";
import axios from "axios";
import { API_BASE_URL, API_BASE_URL_SALARY, API_EXCEL_URL, API_PDF_URL } from "../env";
import { Link } from "react-router-dom";
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { showErrorNotification, showSuccessNotification, showWarningNotification } from "../notification/Notification";
import Modal from 'react-bootstrap/Modal';

const CandidateList = () => {

  const [jobs, setJobs] = useState([]);
  const [isGeneratingList, setIsGeneratingList] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [salaryFrom,setSalaryFrom] = useState('');
  const [salaryTo,setSalaryTo] = useState('');
 const  [selectedCertificateId, setSelectedCertificateId] = useState(null);
 const  [salaryMode , setSalaryMode] = useState('');

  useEffect(() => {
    fetchJobs();
  }, []);  

  const handleCloseModal = () => setShowModal(false);
  const handleShowModal = (certificateId) => {
    setSelectedCertificateId(certificateId);
    setShowModal(true);
    // console.log(certificateId);
    

  }

  /* -------------------------------------------------------------------------- */
  /*                            Fetch Candidate List                            */
  /* -------------------------------------------------------------------------- */
  const fetchJobs = async () => {
    try {
      const response = await axios.get(API_BASE_URL + "/all");
      if (response.data === 0){
        showErrorNotification("Error loading data from the server!");
      }
      setJobs(response.data);
      console.log(response);
      showSuccessNotification("Data loaded successfully from server!");
      setIsGeneratingList(new Array(response.data.length).fill(false));
    } catch (error) {
      showErrorNotification("Error loading data from the server!");
      console.error("Error:", error);
    }
  };

  // const handleDocument = async (certificateId, index) => {
  //   const updatedIsGeneratingList = [...isGeneratingList];
  //   updatedIsGeneratingList[index] = true;
  //   setIsGeneratingList(updatedIsGeneratingList);
  //   try {
  //     const response = await axios.get(`${API_BASE_URL}/documents/cache/${certificateId.toString()}`);
  //     showSuccessNotification("Document generated successfully!");
  //     console.log("Document generated successfully:", response.data);
    
  //   } catch (error) {
  //     showErrorNotification("Error generating document!");
  //     console.error("Error generating document:", error);
  //   } finally {
  //     updatedIsGeneratingList[index] = false;
  //     setIsGeneratingList(updatedIsGeneratingList);
  //   }
  // };

  const handleDocument = async (certificateId, index,changeStatus) => {
    try {
      const updatedJobs = [...jobs]; 
      updatedJobs[index] = { ...updatedJobs[index], isGenerating: true }; 
      setJobs(updatedJobs); 
  
      const response = await axios.get(`${API_BASE_URL}/documents/cache/${certificateId.toString()}`);
      showSuccessNotification("Document generated successfully!");
      console.log("Document generated successfully:", response.data);
     
      if (response.data===0) {
        showErrorNotification("Error generating document!");

      }else{
    
        //api call for status update
        const statusUpdateResponse = await axios.put(`${API_BASE_URL}/${certificateId}/status?status=${changeStatus}`);
        console.log("Status updated successfully:", statusUpdateResponse.data);
        fetchJobs();
      }
     
    } catch (error) {
      showErrorNotification("Error generating document!");
      console.error("Error generating document:", error);
    } finally {
      const updatedJobs = [...jobs];
      updatedJobs[index] = { ...updatedJobs[index], isGenerating: false }; 
      setJobs(updatedJobs); 
    }
  };

  const handleSalarySubmit = async(e) => {
    e.preventDefault();
  
   
    if (salaryMode !== "3months" && salaryMode !== "6months") {
      
      showWarningNotification("Please select a valid salary mode (3 months or 6 months).");
      return;
    }
  
    
    const startDate = new Date(salaryFrom);
    const endDate = new Date(salaryTo);
  
   
    const diffMonths = (endDate.getFullYear() - startDate.getFullYear()) * 12 + endDate.getMonth() - startDate.getMonth() + 1;
  
    
    if ((salaryMode === "3months" && diffMonths !== 3) || (salaryMode === "6months" && diffMonths !== 6)) {
      
      showErrorNotification(`Selected mode (${salaryMode}) does not match the duration.`);
      return;
    }
  
    console.log(`Generating salary for ${diffMonths} months based on mode: ${salaryMode}` ,selectedCertificateId);
       
        try {
          const salaryResponse = await axios.get(`${API_BASE_URL_SALARY}/generate`, {
            params:{
              salaryFrom,
            salaryTo,
            salaryMode,
            certificateId: selectedCertificateId
            }
          });
          console.log(salaryResponse);
          // console.log(JSON.stringify(salaryResponse));

    
         showSuccessNotification("Salary generated successfully:", salaryResponse.data);
    
          clearSalaryForm();
          setShowModal(false);
        } catch (error) {
          console.error("Error generating salary:", error);
         
        }
    
  };
  

  // const handleSalarySubmit = async (e) => {
  //   e.preventDefault();
  //   console.log("Selected Certificate Id is : " ,selectedCertificateId);
  //   console.log("Salary from:", salaryFrom);
  //   console.log("Salary to:", salaryTo);
  //   console.log("Salary mode:", salaryMode);
  //   clearSalaryForm();
    // try {
    //   const response = await axios.post(`${API_BASE_URL}/salary`, {
    //     salaryFrom: salaryFrom,
    //     salaryTo: salaryTo
    //   });
    //   showSuccessNotification("Salary generated successfully!");
    //   console.log("Salary generated successfully:", response.data);
    //   handleCloseModal();
    // } catch (error) {
    //   showErrorNotification("Error generating salary!");
    //   console.error("Error generating salary:", error);
    // }
  // };

  const clearSalaryForm = () => {
    setSalaryMode("");
    setSalaryFrom("");
    setSalaryTo("");
  };
  

  const handleCandidateUpdate = (certificateId) => {};

  const handleCandidateDelete = async (certificateId) => {
    try {
      const response = await axios.delete(`${API_BASE_URL}/${certificateId}`);
      showSuccessNotification("Data deleted successfully!");
      console.log("Data deleted successfully:", response.data);
      fetchJobs();
    } catch (error) {
      showErrorNotification("Error deleting data!");
      console.error("Error deleting data:", error);
    }
  };


  const generatePdfContent = async () => {
    try {
        const response = await axios.get(API_PDF_URL + '/generate', {
            params: {
                entityType: 'certificate'
            },
            responseType: 'blob'
        });

       
        const blobUrl = window.URL.createObjectURL(new Blob([response.data]));

       
        const link = document.createElement('a');
        link.href = blobUrl;
        link.setAttribute('download', 'report.pdf');
        document.body.appendChild(link);

        link.click();

        // Cleanup
        window.URL.revokeObjectURL(blobUrl);

        showSuccessNotification("Pdf file generated successfully of company");
    } catch (error) {
        console.log(error);
    }
}

const generateExcelContent = async () => {
    try {
        const response = await axios.get(API_EXCEL_URL + '/generate', {
            params: {
                entityType: 'certificate'
            },
            responseType: 'blob'
        });

        const blobUrl = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = blobUrl;
        link.setAttribute('download', 'report.xlsx');
        document.body.appendChild(link);
        link.click();
        window.URL.revokeObjectURL(blobUrl);

        showSuccessNotification("Excel file generated successfully of company");
    } catch (error) {
        console.log(error);
    }
}


  return (
    <>
      <ToastContainer />

      <Modal show={showModal} onHide={handleCloseModal}>
        <Modal.Header closeButton>
          <Modal.Title className="card-title text-center">Salary Generation</Modal.Title>
        </Modal.Header>
        <Modal.Body>
       
        <form>
        <div className='col-md-12'>
          <div className="form-group">
            <label htmlFor="salaryMode">Salary Mode</label>
            <select name='salaryMode' id='salaryMode' className='form-control' value={salaryMode} onChange={(e) => setSalaryMode(e.target.value)}>
              <option value="">Select Salary Type</option>
              <option value="3months">3 Months</option>
              <option value="6months">6 Months</option>
            </select>
          </div>
        </div>                               
             
          <div className="row">
            <div className="col-md-6">
              <label htmlFor="salaryFrom" className="col-form-label">From:</label>
              <input type="date" className="form-control" id="salaryFrom" required onChange={(e) => setSalaryFrom(e.target.value)} />
            </div>

            <div className="col-md-6">
              <label htmlFor="salaryTo" className="col-form-label">To:</label>
              <input type="date" className="form-control" id="salaryTo" required onChange={(e) => setSalaryTo(e.target.value)} />
            </div>

            <div className="col-md-12 d-flex justify-content-center mt-3">
              <button
                type="submit" 
                className="btn btn-warning" onClick={(e) => handleSalarySubmit(e)}
                style={{ height: '40px', width: '100px' }} 
              >
                Submit
              </button>
            </div>
          </div>
        </form>


        </Modal.Body>
        <Modal.Footer>
          <button className="btn btn-secondary" onClick={handleCloseModal}>
            Close
          </button>
        </Modal.Footer>
      </Modal>

      <div className="card">
        <div className="card-body">
          <div className='card'>
            <div className='card-header'>
              <h4 className='card-title text-center'>Candidate List</h4>
            </div>
          </div>
          <div className="table-responsive">
            <div>
              <Link to={"/candidate"}>
                <button
                  type="button"
                  className="btn btn-primary"
                  style={{ marginLeft: "15px", margin: "10px" }}
                >
                  Add New
                </button>
              </Link>
              <Link to={"/script"}>
                <button
                  type="button"
                  className="btn btn-primary"
                  style={{ margin: "10px" }}
                >
                  Add Script
                </button>
              </Link>
              <button type="button" onClick={generatePdfContent} className="btn btn-primary" style={{ marginLeft: '15px', margin: '10px' }}>PDF</button>
              <button type="button" onClick={generateExcelContent} className="btn btn-primary" style={{ marginLeft: '15px', margin: '10px' }}>Excel</button>
            </div>
            <table className="table table-bordered table-hover">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Company</th>
                  <th>Mode</th>
                  <th>Candidate</th>
                  <th>Email</th>
                  <th>Offer Date</th>
                  <th>Join Date</th>
                  <th>Job Title</th>
                  <th>Code</th>
                  <th>CTC</th>
                  <th>Income Tax</th>
                  <th>EPF</th>
                  <th>Bonus</th>
                  <th>In Hand Salary</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {jobs.length === 0 ? (
                  <tr>
                    <td colSpan="15" className="text-center">
                      No candidates found.
                    </td>
                  </tr>
                ) : (
                  jobs.map((job, index) => (
                    <tr key={job.certificateId}>
                      <td>{job.certificateId}</td>
                      <td>{job.companyName}</td>
                      <td>
                        <span className={`badge rounded-pill ${job.status === 'CREATED' ? 'bg-success' : 'bg-info'}`}>
                           {job.status}
                        </span>
                      </td>
                      <td>{job.candidateName.substring(0, 10)}</td>
                      <td>{job.candidateEmail.substring(0, 15)}</td>
                      <td>{job.offerDate}</td>
                      <td>{job.dateOfJoining}</td>
                      <td>{job.jobTitle.substring(0, 15)}</td>
                      <td>{job.employeeCode}</td>
                      <td>{job.salaryExpose.ctc ?? 0.0}</td>
                      <td>{job.salaryExpose.epf ?? 0.0}</td>
                      <td>{job.salaryExpose.incomeTax ?? 0.0}</td>
                      <td>{job.salaryExpose.performanceBonus ?? 0.0}</td>
                      <td>{job.salaryExpose.inHandSalary ?? 0.0}</td>
                      {/* <td>{job.ctc}</td>
                      <td>{job?.epf ?? 0.0}</td>
                      <td>{job?.incomeTax ?? 0.0}</td>
                      <td>{job?.performanceBonus ?? 0.0}</td>
                      <td>{job?.finalFixedSalary ?? 0.0}</td> */}
                      {/* <td><span className="badge rounded-pill bg-success">{job.changeStatus}</span></td> */}
                      <td>
                        {job.changeStatus.includes('GENERATED') ? (
                          <span className="badge rounded-pill bg-success">{job.changeStatus}</span>
                        ) : job.changeStatus.includes('PENDING') ? (
                          <span className="badge rounded-pill bg-warning">{job.changeStatus}</span>
                        ) : (
                          <span className="badge rounded-pill bg-danger">{job.changeStatus}</span>
                        )}
                      </td>

                      <td className="tbl-image-gap">
                      
                          <Link to={`/candidateUpdate/${job.certificateId}`}>
                          <img
                          onClick={() => 
                            handleCandidateUpdate(job.certificateId)}
                            src="../assets/images/edit.png"
                            alt="Edit"
                            width="20"
                            height="20"
                          />
                        </Link>
                        <Link
                          to="/candidateList"
                          onClick={() => handleCandidateDelete(job.certificateId)}
                        >
                          <img
                            src="../assets/images/delete.png"
                            alt="Edit"
                            width="20"
                            height="20"
                          />
                        </Link>
                        {/* <button
                          type="button"
                          className="btn btn-primary"
                          style={{ margin: "10px" }}
                          onClick={() => handleDocument(job.certificateId, index)}
                          disabled={isGeneratingList[index]}
                        >
                          {isGeneratingList[index] ? "Generating..." : "Generate"}
                        </button> */}
                        {/* <button
                          type="button"
                          className="btn btn-primary"
                          style={{ margin: "10px" }}
                          onClick={() => handleDocument(job.certificateId, index,job.changeStatus)}
                          disabled={job.isGenerating}
                          >
                          {job.isGenerating ? "Generating..." : "Generate"}
                        </button>
                        
                       <button type="button" className="btn btn-primary"  style={{ height: '40px' }} onClick={handleShowModal}>
           salary
          </button> */}
                        <div style={{ display: 'flex', alignItems: 'center' }}>
                            <button
                              type="button"
                              className="btn btn-primary"
                              style={{ margin: "10px" }}
                              onClick={() => handleDocument(job.certificateId, index, job.changeStatus)}
                              disabled={job.isGenerating}
                            >
                              {job.isGenerating ? "Generating..." : "Generate"}
                            </button>

                            <button
                              type="button"
                              className="btn btn-danger"
                              style={{ height: '40px', marginLeft: '10px' }} // Adjust margin as needed
                              //onClick={handleShowModal}
                              onClick={() => handleShowModal(job.certificateId)}
                            >
                              Salary
                            </button>
                        </div>
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

export default CandidateList;
