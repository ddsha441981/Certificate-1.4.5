import React, { useEffect, useState } from "react";
import axios from "axios";
import { API_ACTIVITY_LOGS_URL } from "../env";

const ActivityLog = () => {
  const [activityLogs, setActivityLogs] = useState([]); 
  const [visibleLogs, setVisibleLogs] = useState(3);   
  const [expanded, setExpanded] = useState({});      

  useEffect(() => {
    const fetchActivityLogs = async () => {
      try {
        const response = await axios.get(API_ACTIVITY_LOGS_URL); 
        setActivityLogs(response.data); 
      } catch (error) {
        console.error("Error fetching activity logs:", error);
      }
    };

    fetchActivityLogs();
  }, []);

  const handleViewMore = () => {
    if (visibleLogs === 3) {
      setVisibleLogs(10); 
    } else {
      setVisibleLogs(3);
    }
  };

  const toggleReadMore = (index) => {
    setExpanded((prevState) => ({
      ...prevState,
      [index]: !prevState[index],
    }));
  };


  const renderDetails = (details, index) => {
    const maxLength = 100;
    if (details.length > maxLength) {
      return (
        <>
          {expanded[index] ? details : `${details.slice(0, maxLength)}...`}
          <button
            className="btn btn-link p-0 ms-2"
            onClick={() => toggleReadMore(index)}
            style={{ color: "blue", textDecoration: "underline", cursor: "pointer" }}
          >
            {expanded[index] ? "Read Less" : "Read More"}
          </button>
        </>
      );
    }
    return details;
  };

  return (
    <div className="col-xl-4">
      <div className="card">
        <div className="card-body">
          <h4 className="card-title mb-5">Activity</h4>
  
          {activityLogs.length === 0 ? (
            <p className="text-center">No activity found.</p>
          ) : (
            <ul className="verti-timeline list-unstyled">
              {activityLogs.slice(0, visibleLogs).map((log, index) => (
                <li key={index} className="event-list">
                  <div className="event-timeline-dot">
                    <i className="bx bx-right-arrow-circle font-size-18"></i>
                  </div>
                  <div className="d-flex">
                    <div className="flex-shrink-0 me-3">
                      <h5 className="font-size-14">
                        {new Date(log.actionTimestamp).toLocaleDateString()}{" "}
                        <i className="bx bx-right-arrow-alt font-size-16 text-primary align-middle ms-2"></i>
                      </h5>
                    </div>
                    <div className="flex-grow-1">
                      <div>{renderDetails(log.details, index)}</div>
                    </div>
                  </div>
                </li>
              ))}
            </ul>
          )}
  
          {activityLogs.length > 3 && (
            <div className="text-center mt-4">
              <button
                onClick={handleViewMore}
                className="btn btn-primary waves-effect waves-light btn-sm"
              >
                {visibleLogs === 3 ? "View More" : "View Less"}{" "}
                <i className="mdi mdi-arrow-right ms-1"></i>
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
  
};

export default ActivityLog;