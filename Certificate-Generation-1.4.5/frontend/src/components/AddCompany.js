import { ToastContainer } from "react-bootstrap";
import Breadcrumb from "./Breadcrumb";
import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import { useState } from "react";
import { Link } from "react-router-dom";

import axios from 'axios';
import "../components/myapp.css";
import { API_BASE_URL_COMPANY } from '../env';
import { showErrorNotification, showSuccessNotification, showWarningNotification } from '../notification/Notification';

const AddCompany = () =>{


    //UseState
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
    const [companyRegistrationNumber,setCompanyRegistrationNumber] = useState('');
    const [companyDomainType,setCompanyDomainType] = useState('');
    const [industryType,setIndustryType] = useState('');
    const [yearOfEstablishment,setYearOfEstablishment] = useState('');
    const [companySize,setCompanySize] = useState('');
    const [companyFounder,setCompanyFounder] = useState('');
    const [companyRevenue,setCompanyRevenue] = useState('');
    const [companyLicenseNumber,setCompanyLicenseNumber] = useState('');

    // HR
    const [hrName,setHrName] = useState('');
    const [hrContactNumber,setHrContactNumber] = useState('');
    const [hrEmail,setHrEmail] = useState('');
    
    // Manager
    const [managerName,setManagerName] = useState('');
    const [managerContactNumber,setManagerContactNumber] = useState('');
    const [managerEmail,setManagerEmail] = useState('');

    const breadcrumbItems = [
        { name: 'Dashboard', link: '/dashboard', active: false },
        { name: 'View Companies', link: '/companies', active: false },
        { name: `Add Company`, link: `/addCompany`, active: true }
    ];



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

            const documents = [{
                experienceLetterUrl,
                relievingLetterUrl,
                offerLetterUrl,
                salarySlipUrl,
                incrementLetterUrl,
                apparisalLetterUrl
            }];

            const addresses = [{ 
                    country,
                    zipCode,
                    buildingNumber,
                    city,
                    street,
                    landmark,
                    addressType
                }]; 
                const hrDetails ={
                    hrName,
                    hrEmail,
                    hrContactNumber
                }; 
                const managerDetails ={
                    managerName,
                    managerEmail,
                    managerContactNumber
                };           
            const companyData = {
                companyName,
                companyEmail,
                companyPhone,
                companyWebsite,
                companyRegistrationNumber,
                companyDomainType,
                industryType,
                yearOfEstablishment,
                companySize,
                companyFounder,
                companyRevenue,
                companyLicenseNumber,
                documents,
                addresses,
                hrDetails,
                managerDetails
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
    return(
        <>
        {/* Code Update in React-Bootsrap due to improve textbox design */}

            <div className="main-content">
                <div className="page-content">
                    <div className="container-fluid">
                        <Breadcrumb title="Add Company" breadcrumbItems={breadcrumbItems} />
                        <div>
                        <ToastContainer/>
                        <p className='msgFile'>All Mandatory field * :- Provide company authorization details along with your logo and signature</p>
                        <Form>
                             {/* -------------------------------Company----------------------------------------------- */}
                             <div className="hr-container">
                                <hr className="solid" />
                                <span className="hr-text">Company</span>
                                <hr className="solid" />
                            </div>
                            <Row className="mb-3">
                                <Form.Group as={Col} controlId="formGridCompanyName">
                                <Form.Label> Name</Form.Label>
                                <Form.Control type="text" name="companyName" id="companyName" value={companyName} onChange={(e)=> setCompanyName(e.target.value)} placeholder="Enter Company Name" />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridCompanyEmail">
                                <Form.Label> Email</Form.Label>
                                <Form.Control type="email" placeholder="Enter Company Email" id="companyEmail" name="companyEmail" value={companyEmail} onChange={(e)=> setCompanyEmail(e.target.value)} />
                                </Form.Group>
                            </Row>
                            
                            <Row className="mb-3">
                                <Form.Group as={Col} controlId="formGridCompanyPhoneNumber">
                                <Form.Label>Contact No</Form.Label>
                                <Form.Control type="text" placeholder="Enter Company conatct number" id="companyPhone" name="companyPhone" value={companyPhone} onChange={(e)=> setCompanyPhone(e.target.value)} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridCompanyWebsite">
                                <Form.Label>Website</Form.Label>
                                <Form.Control type="text" placeholder="Enter company website" id="companyWebsite" name="companyWebsite" value={companyWebsite} onChange={(e)=> setCompanyWebsite(e.target.value)}/>
                                </Form.Group>
                            </Row>

                            <Row className="mb-3">
                                <Form.Group as={Col} controlId="formGridCompanyLogo">
                                <Form.Label>Logo</Form.Label>
                                <Form.Control type="file" id="companyLogo" name="companyLogo" onChange={handleCompanyLogoChange} accept=".png" />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridCompanySignature">
                                <Form.Label>Signature</Form.Label>
                                <Form.Control type="file" id="signatureAuthorities" name="signatureAuthorities" onChange={handleSignatureChange} accept=".png"/>
                                </Form.Group>
                            </Row>

                            <Row className="mb-3">
                                <Form.Group as={Col} controlId="formGridCompanyRegistrationNumber">
                                <Form.Label>Registration No</Form.Label>
                                <Form.Control type="text" placeholder="Enter Registration Number" name="companyRegistrationNumber" id="companyRegistrationNumber" value={companyRegistrationNumber} onChange={(e)=> setCompanyRegistrationNumber(e.target.value)}  />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridCompanyLicenseNumber">
                                <Form.Label>License No</Form.Label>
                                <Form.Control type="text" placeholder="Enter Company Founder" name="companyLicenseNumber" id="companyLicenseNumber" value={companyLicenseNumber} onChange={(e)=> setCompanyLicenseNumber(e.target.value)} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridCompanyEstablishment">
                                <Form.Label>Establishment</Form.Label>
                                <Form.Control type="text" placeholder="Enter Company Year Of Establishment" name="yearOfEstablishment" id="yearOfEstablishment" value={yearOfEstablishment} onChange={(e)=> setYearOfEstablishment(e.target.value)} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridCompanyFounder">
                                <Form.Label>Founder</Form.Label>
                                <Form.Control type="text" placeholder="Enter Company Founder" name="companyFounder" id="companyFounder" value={companyFounder} onChange={(e)=> setCompanyFounder(e.target.value)} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridCompanyRevenue">
                                <Form.Label>Revenue</Form.Label>
                                <Form.Control type="text" placeholder="Enter Company Revenue" name="companyRevenue" id="companyRevenue" value={companyRevenue} onChange={(e)=> setCompanyRevenue(e.target.value)} />
                                </Form.Group>


                                <Form.Group as={Col} controlId="formGridCompanySize">
                                <Form.Label>Size</Form.Label>
                                <Form.Select defaultValue="Choose..." name="companySize" id="companySize" value={companySize} onChange={(e)=> setCompanySize(e.target.value)}>
                                    <option>Choose...</option>
                                    <option value="1-10">1 to 10 employees</option>
                                    <option value="11-50">11 to 50 employees</option>
                                    <option value="51-100">51 to 100 employees</option>
                                    <option value="101-500">101 to 500 employees</option>
                                    <option value="501-1000">501 to 1000 employees</option>
                                    <option value="1001-5000">1001 to 5000 employees</option>
                                    <option value="5001-10000">5001 to 10000 employees</option>
                                    <option value="10000+">10000+ employees</option>
                                </Form.Select>
                                </Form.Group>
                            </Row>

                            <Row className="mb-3">
                                <Form.Group as={Col} controlId="formGridCompanyDomainType">
                                <Form.Label>Domain</Form.Label>
                                <Form.Select defaultValue="Choose..." name="companyDomainType" id="companyDomainType" value={companyDomainType} onChange={(e)=> setCompanyDomainType(e.target.value)}>
                                    <option>Choose...</option>
                                    <option value="TECHNOLOGY">TECHNOLOGY</option>
                                    <option value="FINANCE">FINANCE</option>
                                    <option value="HEALTHCARE">HEALTHCARE</option>
                                    <option value="EDUCATION">EDUCATION</option>
                                    <option value="MANUFACTURING">MANUFACTURING</option>
                                    <option value="RETAIL">RETAIL</option>
                                    <option value="TRANSPORTATION">TRANSPORTATION</option>
                                    <option value="CONSTRUCTION">CONSTRUCTION</option>
                                    <option value="HOSPITALITY">HOSPITALITY</option>
                                    <option value="ENERGY">ENERGY</option>
                                    <option value="TELECOMMUNICATIONS">TELECOMMUNICATIONS</option>
                                    <option value="AGRICULTURE">AGRICULTURE</option>
                                    <option value="ENTERTAINMENT">ENTERTAINMENT</option>
                                    <option value="REAL_ESTATE">REAL ESTATE</option>
                                    <option value="CONSULTING">CONSULTING</option>
                                    <option value="FOOD_AND_BEVERAGE">FOOD AND BEVERAGE</option>
                                    <option value="PHARMACEUTICAL">PHARMACEUTICAL</option>
                                    <option value="INSURANCE">INSURANCE</option>
                                    <option value="AUTOMOTIVE">AUTOMOTIVE</option>
                                    <option value="MEDIA">MEDIA</option>
                                    <option value="AEROSPACE">AEROSPACE</option>
                                    <option value="GOVERNMENT">GOVERNMENT</option>
                                    <option value="LEGAL">LEGAL</option>
                                    <option value="NON_PROFIT">NON PROFIT</option>
                                </Form.Select>
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridIndustryType">
                                <Form.Label>Industry</Form.Label>
                                <Form.Select defaultValue="Choose..." name="industryType" id="industryType" value={industryType} onChange={(e)=> setIndustryType(e.target.value)}>
                                    <option>Choose...</option>
                                    <option value="PRIVATE">PRIVATE</option>
                                    <option value="PUBLIC">PUBLIC</option>
                                    <option value="GOVERNMENT">GOVERNMENT</option>
                                    <option value="NON_PROFIT">NON PROFIT</option>
                                    <option value="PARTNERSHIP">PARTNERSHIP</option>
                                    <option value="SOLE_PROPRIETORSHIP">SOLE PROPRIETORSHIP</option>
                                    <option value="LIMITED_LIABILITY_COMPANY">LIMITED LIABILITY COMPANY</option>
                                    <option value="COOPERATIVE">COOPERATIVE</option>
                                    <option value="MULTINATIONAL">MULTINATIONAL</option>
                                    <option value="JOINT_VENTURE">JOINT VENTURE</option>
                                    <option value="FRANCHISE">FRANCHISE</option>
                                    <option value="HOLDING_COMPANY">HOLDING COMPANY</option>
                                    <option value="SUBSIDIARY">SUBSIDIARY</option>
                                </Form.Select>
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridCountry">
                                <Form.Label>Country</Form.Label>
                                <Form.Select defaultValue="Choose..." name='country' id='country' className='form-control' value={country} onChange={(e) => setCountry(e.target.value)}> 
                                <option value="">Choose...</option>
                                <option value="INDIA">India</option>
                                </Form.Select>
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridCity">
                                <Form.Label>City</Form.Label>
                                <Form.Select defaultValue="Choose..." name='city' id='city' className='form-control' value={city} onChange={(e) => setCity(e.target.value)}>
                                    <option>Choose...</option>
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
                                </Form.Select>
                                </Form.Group>
                            </Row>

                            <Row className="mb-3">
                                <Form.Group as={Col} controlId="formGridCity">
                                <Form.Label>Address</Form.Label>
                                <Form.Select defaultValue="Choose..." name='addressType' id='addressType' className='form-control' value={addressType} onChange={(e) => setAddressType(e.target.value)}>
                                    <option>Choose...</option>
                                    <option value="CORPORATE_OFFICE">Corporate Office</option>
                                    <option value="HEAD_OFFICE">Head Office</option>
                                </Form.Select>
                                </Form.Group>
                            </Row>

                            <Row className="mb-3">
                                <Form.Group as={Col} controlId="formGridZip">
                                <Form.Label>Zip</Form.Label>
                                <Form.Control type="text" placeholder="Enter zip code" id="zipCode" name="zipCode" value={zipCode} onChange={(e)=> setZipCode(e.target.value)} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridBuilding">
                                <Form.Label>Building</Form.Label>
                                <Form.Control type="text" placeholder="Enter building no" id="buildingNumber" name="buildingNumber" value={buildingNumber} onChange={(e)=> setBuildingNumber(e.target.value)} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridStreet">
                                <Form.Label>Street</Form.Label>
                                <Form.Control type="text" placeholder="Enter street" id="street" name="street" value={street} onChange={(e)=> setStreet(e.target.value)} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridLandMark">
                                <Form.Label>Landmark</Form.Label>
                                <Form.Control type="text" placeholder="Enter landmark" id="landmark" name="landmark" value={landmark} onChange={(e)=> setLandmark(e.target.value)} />
                                </Form.Group>
                            </Row>

                            {/* -------------------------------Manager----------------------------------------------- */}
                            <div className="hr-container">
                                <hr className="solid" />
                                <span className="hr-text">Manager</span>
                                <hr className="solid" />
                            </div>

                            <Row className="mb-3">
                                <Form.Group as={Col} controlId="formGridManagerName">
                                <Form.Label>Name</Form.Label>
                                <Form.Control type="text" placeholder="Enter manager Name" id="managerName" name="managerName" value={managerName} onChange={(e)=> setManagerName(e.target.value)} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridManagerContactNumber">
                                <Form.Label>Contact No</Form.Label>
                                <Form.Control type="text" placeholder="Enter conatct No" id="managerContactNumber" name="managerContactNumber" value={managerContactNumber} onChange={(e)=> setManagerContactNumber(e.target.value)} />
                                </Form.Group>
                            </Row>

                            <Row className="mb-3">
                                <Form.Group as={Col} controlId="formGridManagerEmail">
                                <Form.Label>Email</Form.Label>
                                <Form.Control type="text" placeholder="Enter manager email" id="managerEmail" name="managerEmail" value={managerEmail} onChange={(e)=> setManagerEmail(e.target.value)}/>
                                </Form.Group>
                            </Row>

                            
                             {/* -------------------------------HR----------------------------------------------- */}
                             <div className="hr-container">
                                <hr className="solid" />
                                <span className="hr-text">HR</span>
                                <hr className="solid" />
                            </div>

                             <Row className="mb-3">
                                <Form.Group as={Col} controlId="formGridHRName">
                                <Form.Label>Name</Form.Label>
                                <Form.Control type="text" placeholder="Enter HR Name" id="hrName" name="hrName" value={hrName} onChange={(e)=> setHrName(e.target.value)} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridHRContactNumber">
                                <Form.Label>Contact No</Form.Label>
                                <Form.Control type="text" placeholder="Enter conatct No" id="hrContactNumber" name="hrContactNumber" value={hrContactNumber} onChange={(e)=> setHrContactNumber(e.target.value)} />
                                </Form.Group>
                            </Row>

                            <Row className="mb-3">
                                <Form.Group as={Col} controlId="formGridHREmail">
                                <Form.Label>Email</Form.Label>
                                <Form.Control type="text" placeholder="Enter HR email" id="hrEmail" name="hrEmail" value={hrEmail} onChange={(e)=> setHrEmail(e.target.value)} />
                                </Form.Group>
                            </Row>


                             {/* -------------------------------Documents----------------------------------------------- */}

                            <div className="hr-container">
                                <hr className="solid" />
                                <span className="hr-text">Documents</span>
                                <hr className="solid" />
                            </div>

                             <Row className="mb-3">
                                <Form.Group as={Col} controlId="formGridExperience">
                                <Form.Label>Experience</Form.Label>
                                <Form.Control type="text" placeholder="Enter Experience URL" id="experienceLetterUrl" name="experienceLetterUrl" value={experienceLetterUrl} onChange={(e)=> setExperienceLetterUrl(e.target.value)} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridRelieving">
                                <Form.Label>Relieving</Form.Label>
                                <Form.Control type="text" placeholder="Enter Relieving URL" id="relievingLetterUrl" name="relievingLetterUrl" value={relievingLetterUrl} onChange={(e)=> setRelievingLetterUrl(e.target.value)} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridOffer">
                                <Form.Label>Offer</Form.Label>
                                <Form.Control type="text" placeholder="Enter Offer URL" id="offerLetterUrl" name="offerLetterUrl" value={offerLetterUrl} onChange={(e)=> setOfferLetterUrl(e.target.value)} />
                                </Form.Group>
                            </Row>


                            <Row className="mb-3">
                                <Form.Group as={Col} controlId="formGridIncrement">
                                <Form.Label>Increment</Form.Label>
                                <Form.Control type="text" placeholder="Enter Increment URL"  id="incrementLetterUrl" name="incrementLetterUrl" value={incrementLetterUrl} onChange={(e)=> setIncrementLetterUrl(e.target.value)}/>
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridApparisal">
                                <Form.Label>Apparisal</Form.Label>
                                <Form.Control type="text" placeholder="Enter Apparisal URL" id="apparisalLetterUrl" name="apparisalLetterUrl" value={apparisalLetterUrl} onChange={(e)=> setSpparisalLetterUrl(e.target.value)} />
                                </Form.Group>
                            </Row>

                            <Row className="mb-3">
                                <Form.Group as={Col} controlId="formGridSalarySlip">
                                <Form.Label>Salary</Form.Label>
                                <Form.Control type="text" placeholder="Enter Salary URL" id="salarySlipUrl" name="salarySlipUrl" value={salarySlipUrl} onChange={(e)=> setSalarySlipUrl(e.target.value)} />
                                </Form.Group>
                            </Row>
                                    

                                    <Button variant="outline-primary" onClick={handleSubmit}>Submit</Button>{' '}
                                    <Link to={'/companies'}>
                                        <Button variant="outline-info">View</Button>{' '}
                                    </Link>
                                    
                            </Form>
                        </div>
                    </div>
                </div>
            </div>

        </>
    );
};
export default AddCompany;