import React, { useState } from 'react';

const CheckBox = () => {
  const [selectedCompany, setSelectedCompany] = useState('');
  const [companyData, setCompanyData] = useState({ documents: []});

  // Function to fetch company data based on selected company name (simulated fake API call)
  const fetchCompanyData = (companyId) => {
    // Simulate an asynchronous fake API call with setTimeout
    setTimeout(() => {
      // Fake data for different companies
      const data = {
        "1": { documents: ["Document 1", "Document 2", "Document 3"] },
        "2": { documents: ["Document A", "Document B", "Document C"] }
        // Add data for other companies as needed
      };

      const fetchedData = data[companyId] || { documents: [] };
      setCompanyData(fetchedData);
    }, 1000); // Simulate delay of 1 second
  }

  // Event handler for company selection change
  const handleCompanySelect = (event) => {
    const companyId = event.target.value;
    setSelectedCompany(companyId);
    fetchCompanyData(companyId); // Call the fetchCompanyData function to simulate the API call
  }

  return (
    <div>
      <select value={selectedCompany} onChange={handleCompanySelect}>
        <option value="">Select a company</option>
        <option value="1">Samsung</option>
        <option value="2">Another Company</option>
        {/* Add options for other companies */}
      </select>

      <div>
        <h2>Documents:</h2>
        {companyData.documents.map((document, index) => (
          <div key={index}>
            <input type="checkbox" value={document} />
            <label>{document}</label>
          </div>
        ))}
      </div>
    </div>
  );
}

export default CheckBox;
