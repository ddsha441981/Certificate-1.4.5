import { useEffect, useState } from "react";
import { API_BIRTHDAY_REMINDER_URL } from "../env";
import axios from "axios";
import { showErrorNotification } from "../notification/Notification";
import GenderCount from "./GenderCount";

const Birthday = () => {
    const [candidates, setCandidates] = useState([]);



useEffect(() => {
  fetchUpcomingBirthdays();
},[]);

const fetchUpcomingBirthdays = async () =>{
    try{
      const response = await axios.get(API_BIRTHDAY_REMINDER_URL);
      setCandidates(response.data);
      console.log(response.data);
    }catch(error){
      showErrorNotification('Error loading data from the server');
      console.error('Error fetching upcoming birthdays:', error);
    }
  }
    return (
       <>
           <div className="col-xl-4">
    <div className="card">
        <div className="card-body">
            <h4 className="card-title mb-4">Today Birthdays</h4>
            {candidates.length === 0 ? (
                <div className="text-center mt-4">
                    <h5>No birthdays found for today</h5>
                </div>
            ) : (
                <div className="row mt-4">
                    {candidates.map((candidate, index) => (
                        <div className="col-4" key={index}>
                            <div className="social-source text-center mt-3">
                                <div className="avatar-xs mx-auto mb-3">
                                    <span className="avatar-title rounded-circle bg-primary font-size-16">
                                        <img
                                            src={
                                                candidate.gender === "MALE"
                                                    ? "assets/images/man.png"
                                                    : candidate.gender === "FEMALE"
                                                    ? "assets/images/women.png"
                                                    : "assets/images/others.png"
                                            }
                                            alt={candidate.gender}
                                            className="rounded-circle"
                                            style={{ width: '100%', height: 'auto' }}
                                        />
                                    </span>
                                </div>
                                <h5 className="font-size-15">{candidate.candidateName}</h5>
                                <p className="text-muted mb-0"></p>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    </div>
    <GenderCount/>
    
</div>

       </>
    )
}

export default Birthday;
