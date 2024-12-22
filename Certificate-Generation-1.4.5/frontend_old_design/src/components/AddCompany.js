import React, { useState } from 'react';
import axios from 'axios';
import "../components/myapp.css";
import { Link } from 'react-router-dom';
import { API_BASE_URL_COMPANY } from '../env';
import { ToastContainer } from 'react-toastify';
import { showErrorNotification, showSuccessNotification, showWarningNotification } from '../notification/Notification';

const AddCompany = () => {
    const [companyName, setCompanyName] = useState('');
    const [companyEmail, setCompanyEmail] = useState('');
    const [companyPhone, setCompanyPhone] = useState('');
    const [companyWebsite,setCompanyWebsite] = useState('');
    const [companyLogo,setCompanyLogo] = useState([]);
    const [signatureAuthorities,setSignatureAuthorities] = useState([]);
    const [experienceLetterUrl, setExperienceLetterUrl] = useState('');
    const [relievingLetterUrl, setRelievingLetterUrl] = useState('');
    const [offerLetterUrl, setOfferLetterUrl] = useState('');
    const [salarySlipUrl, setSalarySlipUrl] = useState('');
    const [incrementLetterUrl, setIncrementLetterUrl] = useState('');
    const [apparisalLetterUrl, setSpparisalLetterUrl] = useState('');
    const [country,setCountry] = useState('');
    const [zipCode,setZipCode] = useState('');
    const [buildingNumber,setBuildingNumber] = useState('');
    const [city,setCity] = useState('');
    const [street,setStreet] = useState('');
    const [landmark,setLandmark] = useState('');
    const [addressType,setAddressType] = useState('');


    // const handleSubmit = async (e) => {
    //     e.preventDefault();
    //     try {
    //         const documents = [
    //             {
    //                 experienceLetterUrl,
    //                 relievingLetterUrl,
    //                 offerLetterUrl,
    //                 salarySlipUrl,
    //                 incrementLetterUrl,
    //                 apparisalLetterUrl
    //             }
    //         ];
    //         const response = await axios.post(API_BASE_URL_COMPANY + '/', {
    //             companyName,
    //             companyEmail,
    //             companyAddress,
    //             companyPhone,
    //             companyWebsite,
    //             companyLogo,
    //             signatureAuthorities,
    //             documents
    //         });
    //         console.log(response.data); 
    //          //clear form data
    //          clearForm();
    //     } catch (error) {
    //         console.error('Error:', error);
    //     }
    // }


    const handleCompanyLogoChange = (e) => {
        const selectedFile = e.target.files[0];
       
        if (selectedFile && selectedFile.type === 'image/png') {
            console.log(selectedFile);
            setCompanyLogo(selectedFile);
          } else {
            showWarningNotification('Please select a PNG file.');
          }
    }

    const handleSignatureChange = (e) => {
        const selectedFile = e.target.files[0];
       
        if (selectedFile && selectedFile.type === 'image/png') {
            console.log(selectedFile);
            setSignatureAuthorities(selectedFile);
          } else {
            showWarningNotification('Please select a PNG file.');
          }
    }
    
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const formData = new FormData();
            formData.append('logo', companyLogo);
            formData.append('signature', signatureAuthorities);

            const documents = [
                            {
                            experienceLetterUrl,
                            relievingLetterUrl,
                            offerLetterUrl,
                            salarySlipUrl,
                            incrementLetterUrl,
                            apparisalLetterUrl
                            }
                        ];

                        const addresses = [
                            {
                            country,
                            zipCode,
                            buildingNumber,
                            city,
                            street,
                            landmark,
                            addressType
                            }
                        ];
    
            const companyData = {
                companyName,
                companyEmail,
                companyPhone,
                companyWebsite,
                documents,
                addresses

            };
            formData.append('formData', JSON.stringify(companyData));
    
            const response = await axios.post(API_BASE_URL_COMPANY + '/', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
    
            showSuccessNotification(response.data);
            console.log(response.data); 
            //clear form data
            clearForm();
        } catch (error) {
            showErrorNotification("Error adding company");
            console.error('Error:', error);
        }
    }
    

    function clearForm() {
        setCompanyName('');
        setCompanyEmail('');
        setCompanyPhone('');
        setCompanyWebsite('');
        setCompanyLogo('');
        setSignatureAuthorities('');
        setExperienceLetterUrl('');
        setRelievingLetterUrl('');
        setOfferLetterUrl('');
        setSalarySlipUrl('');
        setIncrementLetterUrl('');
        setSpparisalLetterUrl('');
        setCountry('');
        setZipCode('');
        setBuildingNumber('');
        setCity('');
        setStreet('');
        setLandmark('');
        setAddressType('');
    }

   
    return (
        <>
             <ToastContainer/>
            <div className='card'>
                <div className='card-header'>
                    <h4 className='card-title text-center'>Add Company</h4>
                </div>
            </div>

            <div className='card card-properties'>
           
                <div className='card-body'>
                        <div className="container borders">
                            <form>
                                <div className='container-fluid'>
                                <p className='msgFile'>All Mandatory field *</p>
                                    <div className='row mt-3'>
                                        <div className='col-md-6'>
                                            <div className="form-group">
                                                <label for="companyName">Company Name</label>
                                                <input type="text" className="form-control" id="companyName" name="companyName" value={companyName} onChange={(e)=> setCompanyName(e.target.value)} />
                                            </div>
                                        </div>

                                        <div className='col-md-6'>
                                            <div className="form-group">
                                                <label for="companyEmail">Company Email</label>
                                                <input type="email" className="form-control" id="companyEmail" name="companyEmail" value={companyEmail} onChange={(e)=> setCompanyEmail(e.target.value)} />
                                            </div>
                                        </div>
                                    </div>

                                        <div className='row'>
                                            <div className='col-md-6'>
                                                <div className="form-group">
                                                    <label for="companyPhone">Company Phone</label>
                                                    <input type="text" className="form-control" id="companyPhone" name="companyPhone" value={companyPhone} onChange={(e)=> setCompanyPhone(e.target.value)} />
                                                </div>
                                            </div>

                                            <div className='col-md-6'>
                                                <div className="form-group">
                                                    <label for="companyWebsite">Company Website</label>
                                                    <input type="text" className="form-control" id="companyWebsite" name="companyWebsite" value={companyWebsite} onChange={(e)=> setCompanyWebsite(e.target.value)} />
                                                </div>
                                            </div>
                                    </div>

                                    <div class="line text-center"><span>Company Authorities Information</span></div>
                                    <p className='msgFile'>All Mandatory field * :- Provide company authorization details along with your logo and signature</p>
                                   
                                    <div className='row mt-3'>
                                        <div className='col-md-6'>
                                            <div className="form-group">
                                                <label for="companyLogo">Company Logo</label>
                                                <input type="file" className="form-control" id="companyLogo" name="companyLogo" onChange={handleCompanyLogoChange} accept=".png"/>
                                            </div>
                                        </div>

                                        <div className='col-md-6'>
                                            <div className="form-group">
                                                <label for="signatureAuthorities">Company Signature</label>
                                                <input type="file" className="form-control" id="signatureAuthorities" name="signatureAuthorities" onChange={handleSignatureChange} accept=".png"/>
                                            </div>
                                        </div>
                                    </div>

                                   
                                <div className='row'>
                                    <div className='col-md-4'>
                                        <div className="form-group">
                                            <label htmlFor="country">Country</label>
                                            <select name='country' id='country' className='form-control' value={country} onChange={(e) => setCountry(e.target.value)}>
                                                <option value="">Select Country</option>
                                                <option value="INDIA">India</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div className='col-md-4'>
                                        <div className="form-group">
                                            <label htmlFor="city">City</label>
                                            <select name='city' id='city' className='form-control' value={city} onChange={(e) => setCity(e.target.value)}>
                                                <option value="">Select City</option>
                                                {country === "INDIA" && (
                                                    <>
                                                        <option value="KANPUR">Kanpur</option>
                                                        <option value="JAIPUR">Jaipur</option>
                                                        <option value="PUNE">Pune</option>
                                                        <option value="NOIDA">Noida</option>
                                                        <option value="DELHI">Delhi</option>
                                                        <option value="MUMBAI">Mumbai</option>
                                                        <option value="HYDERABAD">Hyderabad</option>
                                                        <option value="CHENNAI">Chennai</option>
                                                        <option value="BANGALORE">Bangalore</option>
                                                        <option value="DELHI_NCR">Delhi NCR</option>
                                                        <option value="KOLKATA">Kolkata</option>
                                                        <option value="CHANDIGARH">Chandigarh</option>
                                                        <option value="AHMEDABAD">Ahmedabad</option>
                                                        <option value="LUCKNOW">Lucknow</option>
                                                        <option value="NAGPUR">Nagpur</option>
                                                        <option value="SURAT">Surat</option>
                                                       
                                                    </>
                                                )}
                                            </select>
                                        </div>
                                    </div>

                                    <div className='col-md-4'>
                                        <div className="form-group">
                                            <label htmlFor="addressType">Address Type</label>
                                            <select name='addressType' id='addressType' className='form-control' value={addressType} onChange={(e) => setAddressType(e.target.value)}>
                                                <option value="">Select Addresss Type</option>
                                                <option value="CORPORATE_OFFICE">Corporate Office</option>
                                                <option value="HEAD_OFFICE">Head Office</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                                <div className='row'>
                                   <div className='col-md-3'>
                                            <div className="form-group">
                                                <label for="zipCode">Zip Code</label>
                                                <input type="text" className="form-control" id="zipCode" name="zipCode" value={zipCode} onChange={(e)=> setZipCode(e.target.value)} />
                                            </div>
                                    </div>
                                    <div className='col-md-3'>
                                            <div className="form-group">
                                                <label for="buildingNumber">Building Number</label>
                                                <input type="text" className="form-control" id="buildingNumber" name="buildingNumber" value={buildingNumber} onChange={(e)=> setBuildingNumber(e.target.value)} />
                                            </div>
                                    </div>
                                    <div className='col-md-3'>
                                            <div className="form-group">
                                                <label for="street">Street</label>
                                                <input type="text" className="form-control" id="street" name="street" value={street} onChange={(e)=> setStreet(e.target.value)} />
                                            </div>
                                    </div>
                                    <div className='col-md-3'>
                                            <div className="form-group">
                                                <label for="landmark">Landmark</label>
                                                <input type="text" className="form-control" id="landmark" name="landmark" value={landmark} onChange={(e)=> setLandmark(e.target.value)} />
                                            </div>
                                    </div>
                                </div>





                                    <div class="line text-center"><span>Additional Information</span></div>
                                    <p className='msgFile'>All Mandatory field * :- Provide URL</p>
                                    <div className='row mt-3'>
                                        <div className='col-md-6'>
                                            <div className="form-group">
                                                <label for="experienceLetterUrl">Experience Letter</label>
                                                <input type="text" className="form-control" id="experienceLetterUrl" name="experienceLetterUrl" value={experienceLetterUrl} onChange={(e)=> setExperienceLetterUrl(e.target.value)} />
                                            </div>
                                        </div>

                                        <div className='col-md-6'>
                                            <div className="form-group">
                                                <label for="relievingLetterUrl">Relieving Letter</label>
                                                <input type="text" className="form-control" id="relievingLetterUrl" name="relievingLetterUrl" value={relievingLetterUrl} onChange={(e)=> setRelievingLetterUrl(e.target.value)} />
                                            </div>
                                        </div>
                                    </div>

                                    <div className='row'>
                                        <div className='col-md-6'>
                                            <div className="form-group">
                                                <label for="offerLetterUrl">Offer Letter</label>
                                                <input type="text" className="form-control" id="offerLetterUrl" name="offerLetterUrl" value={offerLetterUrl} onChange={(e)=> setOfferLetterUrl(e.target.value)} />
                                            </div>
                                        </div>

                                        <div className='col-md-6'>
                                            <div className="form-group">
                                                <label for="salarySlipUrl">Salary Slip</label>
                                                <input type="text" className="form-control" id="salarySlipUrl" name="salarySlipUrl" value={salarySlipUrl} onChange={(e)=> setSalarySlipUrl(e.target.value)} />
                                            </div>
                                        </div>
                                    </div>

                                    <div className='row'>
                                        <div className='col-md-6'>
                                            <div className="form-group">
                                                <label for="incrementLetterUrl">Increment Letter</label>
                                                <input type="text" className="form-control" id="incrementLetterUrl" name="incrementLetterUrl" value={incrementLetterUrl} onChange={(e)=> setIncrementLetterUrl(e.target.value)} />
                                            </div>
                                        </div>

                                        <div className='col-md-6'>
                                            <div className="form-group">
                                                <label for="apparisalLetterUrl">Apparisal Letter</label>
                                                <input type="text" className="form-control" id="apparisalLetterUrl" name="apparisalLetterUrl" value={apparisalLetterUrl} onChange={(e)=> setSpparisalLetterUrl(e.target.value)} />
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                {/* <div className="col-12">  
                                    <button 
                                        type="submit" 
                                        className="btn btn-primary" 
                                        onClick={handleSubmit}
                                        style={{ marginLeft: '15px', margin: '10px' }}
                                    >
                                        Submit
                                    </button>
                                    <Link to={'/companies'}>
                                        <button 
                                            type="button" 
                                            className="btn btn-primary" 
                                            style={{ marginLeft: '15px', margin: '10px' }}
                                        >
                                            View
                                        </button>
                                    </Link>
                                </div> */}
                                    <div className="col-12 d-flex justify-content-center mt-5" >
                                        <button type="submit" className="btn btn-primary"  onClick={handleSubmit}>Submit</button>
                                        <Link to={'/companies'}>
                                                <button type="button" className="btn btn-primary ml-2">View</button>
                                            </Link>      
                                    </div>
                            </form>
                    </div>
                </div>
            </div>
        </>
    );
}

export default AddCompany;