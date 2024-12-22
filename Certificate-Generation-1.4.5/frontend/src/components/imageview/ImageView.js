import React from 'react';

function LogoViewer({ logoData }) {
  const convertBase64ToDataURL = (base64String) => {
    return `data:image/png;base64,${base64String}`;
  };

  const logoUrl = convertBase64ToDataURL(logoData);

  return (
    <div>
     
      {logoUrl && <img src={logoUrl} alt="Company Logo"  className='round-image'/>}
    </div>
  );
}

function SignatureViewer({ signatureData }) {
  const convertBase64ToDataURL = (base64String) => {
    return `data:image/png;base64,${base64String}`;
  };

  const signatureUrl = convertBase64ToDataURL(signatureData);

  return (
    <div>
     
      {signatureUrl && <img src={signatureUrl} alt="Signature"  className='round-image'/>}
    </div>
  );
}

export { LogoViewer, SignatureViewer };