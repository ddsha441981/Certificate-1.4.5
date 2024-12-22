import React from 'react';
import './myapp.css'; 

const Teams = () => {
    return (
       
            <div className="row mt-5">
                <div className="col-md-12">
                    <div className="card">
                        <div className="card-body">
                            <h6 className='text-center'>Our Team</h6>
                            <div className="team-members">
                                <div className="team-member">
                                    <img src="https://picsum.photos/200" alt="Team Member 1" />
                                    <h6>Team Member 1</h6>
                                </div>
                                <div className="team-member">
                                    <img src="https://picsum.photos/200" alt="Team Member 2" />
                                    <h6>Team Member 2</h6>
                                </div>

                                <div className="team-member">
                                    <img src="https://picsum.photos/200" alt="Team Member 3" />
                                    <h6>Team Member 3</h6>
                                </div>

                                <div className="team-member">
                                    <img src="https://picsum.photos/200" alt="Team Member 4" />
                                    <h6>Team Member 4</h6>
                                </div>
                                
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    );
}

export default Teams;
