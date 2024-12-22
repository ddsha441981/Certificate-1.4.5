import React from 'react';
import Breadcrumb from './Breadcrumb';

const CalendarEvents = () => {
    const breadcrumbItems = [
        { name: 'Dashboard', link: '/dashboard', active: false },
        { name: `Google Calender `, link: `/calenderEvent`, active: true }
    ];
  return (


    <div className="main-content">
    <div className="page-content">
        <div className="container-fluid">
            <Breadcrumb title="Google Calender" breadcrumbItems={breadcrumbItems} />
            <div style={{ margin: 0, padding: 0, height: '100vh', width: '100vw' }}>
            <iframe 
        // src="https://calendar.google.com/calendar/embed?src=iinfinitytechsolutions%40gmail.com&ctz=Asia%2FKolkata"
        src="https://calendar.google.com/calendar/embed?src=iinfinitytechsolutions%40gmail.com&ctz=Asia%2FKolkata&mode=WEEK" 
        style={{ border: '0' }} // Change this line to use an object
        width="1000" 
        height="600" 
        frameBorder="0" // Use camelCase for frameBorder
        scrolling="no"
        title="Google Calendar">
      </iframe>
                </div>
                </div>
                </div>
                </div>
                
  );
};

export default CalendarEvents;














// import React, { useState, useEffect } from "react";
// import ICAL from "ical.js"; // Import ical.js

// const CalendarEvents = () => {
//   const [events, setEvents] = useState([]);

//   useEffect(() => {
//     const icalUrl = "https://calendar.google.com/calendar/ical/iinfinitytechsolutions%40gmail.com/public/basic.ics";

//     fetch(icalUrl)
//       .then((response) => response.text())
//       .then((data) => {
//         const jcalData = ICAL.parse(data); // Parse the .ics data using ical.js
//         const comp = new ICAL.Component(jcalData);
//         const vevents = comp.getAllSubcomponents("vevent");
        
//         // Extract event data and save it to state
//         const eventsArray = vevents.map((vevent) => {
//           const summary = vevent.getFirstPropertyValue("summary");
//           const start = vevent.getFirstPropertyValue("dtstart").toJSDate();
//           return { summary, start };
//         });

//         setEvents(eventsArray);
//       })
//       .catch((error) => console.error("Error fetching calendar data:", error));
//   }, []);

//   return (
//     <div>
//       <h1>Google Calendar Events</h1>
//       <ul>
//         {events.map((event, index) => (
//           <li key={index}>
//             <strong>{event.summary}</strong> - {event.start.toLocaleString()}
//           </li>
//         ))}
//       </ul>
//     </div>
//   );
// };

// export default CalendarEvents;



























// import React, { useState, useEffect } from "react";
// import ical from "ical"; // Install this using: npm install ical

// const CalendarEvents = () => {
//   const [events, setEvents] = useState([]);

//   useEffect(() => {
//     // const calendarId = "your_calendar_id@group.calendar.google.com";
//     const calendarId = "iinfinitytechsolutions%40gmail.com";
//     const icalUrl = `https://calendar.google.com/calendar/ical/${calendarId}/public/basic.ics`;
//     // const icalUrl = `https://www.google.com/calendar/ical/${calendarId}/public/full.ics`;
//     //https://calendar.google.com/calendar/embed?src=iinfinitytechsolutions%40gmail.com&ctz=Asia%2FKolkata
//     //https://calendar.google.com/calendar/ical/iinfinitytechsolutions%40gmail.com/public/basic.ics

//     fetch(icalUrl)
//       .then((response) => response.text())
//       .then((data) => {
//         const parsedData = ical.parseICS(data);
//         const eventsArray = Object.values(parsedData).filter(
//           (event) => event.type === "VEVENT"
//         );
//         setEvents(eventsArray);
//       })
//       .catch((error) => console.error("Error fetching calendar data:", error));
//   }, []);

//   return (
//     <div>
//       <h1>Google Calendar Events</h1>
//       <ul>
//         {events.map((event, index) => (
//           <li key={index}>
//             <strong>{event.summary}</strong> - {event.start.toString()}
//           </li>
//         ))}
//       </ul>
//     </div>
//   );
// };

// export default CalendarEvents;
