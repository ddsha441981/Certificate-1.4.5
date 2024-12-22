import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { API_BASE_URL } from '../env';
import { Pie } from 'react-chartjs-2';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';

ChartJS.register(ArcElement, Tooltip, Legend);

const GenderCount = () => {
  const [genderData, setGenderData] = useState([]);
  const [totalCount, setTotalCount] = useState(0);

  useEffect(() => {
    findCandidateGenderCount();
  }, []);

  const findCandidateGenderCount = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/count/gender`);
      
    
      const data = response.data;
      setGenderData(data);

     
      const total = data.reduce((sum, item) => sum + item.count, 0);
      setTotalCount(total);
    } catch (error) {
      console.error("Error fetching gender count data", error);
    }
  };

  const calculatePercentage = (count) => {
    return totalCount === 0 ? 0 : ((count / totalCount) * 100).toFixed(2);
  };

  const pieData = {
    labels: genderData.map((item) => item.gender),  
    datasets: [
      {
        label: 'Gender Distribution',
        data: genderData.map((item) => calculatePercentage(item.count)),  
        backgroundColor: ['#36A2EB', '#FF6384', '#FFCE56'],  
        hoverOffset: 4,
      },
    ],
  };

  return (
    <div className='card'>
        <div className="card-body">
            <h4 className="card-title mb-1">Gender Distribution</h4>
        </div>
      
      <div style={{ width: '350px', height: '350px' }}>
        <Pie data={pieData} />
      </div>
    </div>
  );
};

export default GenderCount;
