import React from "react";

const ContactUs = () => {
  return (
    <>
      <div className="row mt-5">
        <div className="col-md-12">
          <div className="card">
                      <iframe className="map-properties" src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d34695.300127023394!2d75.69446455481481!3d26.909388372710584!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x396db49e043a7acb%3A0xdad09ace57371810!2sVaishali%20Nagar%2C%20Jaipur%2C%20Rajasthan!5e0!3m2!1sen!2sin!4v1709783295862!5m2!1sen!2sin"  allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>
          </div>
        </div>
      </div>
        <div className="row mt-5">

        <div className="col-md-6">
          <div className="card">
          <div className="card-body text-center">
              <img src="../assets/images/contact-us.png" alt="conatct us" className="image-properties" />

              <h6 className="text-center">ContactUs </h6>
              <div className="row">
                <div className="col-md-12">
                  <div className="form-floating mb-3">
                    <input
                      type="text"
                      className="form-control"
                      id="floatingInput"
                      placeholder="Enter Your Name"
                    />
                  </div>
                </div>
              </div>
              <div className="row">
                <div className="col-md-12">
                  <div className="form-floating mb-3">
                    <input
                      type="email"
                      className="form-control"
                      id="floatingInput"
                      placeholder="Enter Your Email"
                    />
                  </div>
                </div>
              </div>
              <div className="col-12 d-flex justify-content-center">
                <button type="submit" className="btn btn-primary">
                  Submit
                </button>
              </div>
            </div>
          </div>
        </div>

        <div className="col-md-6">
          <div className="card">
          <div className="card-body text-center">
          <img src="../assets/images/earth.jpeg" alt="conatct us" className="image-contact_us" />
            </div>
          </div>
        </div>

        </div>
    </>
  );
};

export default ContactUs;
