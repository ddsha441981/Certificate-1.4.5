// TODO: Need to enhance this script
// Like when already interview schuduled then it reschulded but if some one want to cancel it then it will not removed or cancel mark



function doGet(e) {
    var action = e.parameter.action;
    var jsonData = decodeURIComponent(e.parameter.jsonData);
    try {
        var data = JSON.parse(jsonData);

        var responseMessage = "Data processed successfully!";
        var responseData = {};


        if (action === "create") {
            var eventResponse = createEvent(data);
            responseData.googleEventId = eventResponse.googleEventId;
            responseMessage = eventResponse.message;
        } else if (action === "cancel") {
            cancelEvent(data);
        } else if (action === "reschedule") {
            var eventResponse = rescheduleEvent(data);
            responseData.googleEventId = eventResponse.googleEventId;
            responseMessage = eventResponse.message;
        } else if (action === "remove") {
            removeEvent(data);
        }

        var response = {
            status: "success",
            receivedData: data,
            message: responseMessage,
            eventData: responseData
        };

        return ContentService.createTextOutput(JSON.stringify(response))
            .setMimeType(ContentService.MimeType.JSON);
    } catch (error) {
        return ContentService.createTextOutput(JSON.stringify({status: "error", message: error.message}))
            .setMimeType(ContentService.MimeType.JSON);
    }
}


function createEvent(data) {
    Logger.log("Creating event with data: " + JSON.stringify(data));


    var startTime = new Date(data.interviewDate + " " + data.startTime);
    var endTime = new Date(data.interviewDate + " " + data.endTime);

    var title = data.interviewType + " " + data.interviewRound + " Interview with " + data.candidateName + " at " + data.companyName;

    var calendar = CalendarApp.getDefaultCalendar();

    try {
        Logger.log("Event Title: " + title);


        var event = calendar.createEvent(title, startTime, endTime, {
            location: data.location,
            description: 'Interview with ' + data.candidateName + ' at ' + data.companyName + '. Location: ' + data.location
        });

        var googleEventId = event.getId();
        Logger.log("Google Calendar event ID: " + googleEventId);

        event.setColor(CalendarApp.EventColor.GREEN);
        Logger.log("Event created: " + googleEventId);


        return {
            googleEventId: googleEventId,
            message: "Event created successfully."
        };
    } catch (error) {
        Logger.log("Error creating event: " + error.message);
        throw new Error("Failed to create event: " + error.message);
    }
}


function cancelEvent(data) {
    Logger.log("Cancelling event with ID: " + data.googleEventId);
    var calendar = CalendarApp.getDefaultCalendar();

    try {
        var event = calendar.getEventById(data.googleEventId);
        if (event) {
            event.setColor(CalendarApp.EventColor.RED);
            event.setTitle(event.getTitle() + " (CANCELED)");
            event.setDescription(event.getDescription() + "\nThis event has been canceled.");
            Logger.log("Event canceled: " + event.getId());
        } else {
            throw new Error("Event not found.");
        }
    } catch (error) {
        Logger.log("Error canceling event: " + error.message);
        throw new Error("Failed to cancel event: " + error.message);
    }
}

function rescheduleEvent(data) {
    Logger.log("Rescheduling event with ID: " + data.googleEventId);

    var calendar = CalendarApp.getDefaultCalendar();

    try {

        var newStartTime = new Date(data.interviewDate + " " + data.startTime);
        var newEndTime = new Date(data.interviewDate + " " + data.endTime);

        var newTitle = data.interviewType + " " + data.interviewRound + " Interview with " + data.candidateName + " at " + data.companyName + " (RESCHEDULED)";

        var newEvent = calendar.createEvent(newTitle, newStartTime, newEndTime, {
            location: data.location,
            description: 'Interview with ' + data.candidateName + ' at ' + data.companyName + '. Location: ' + data.location
        });

        newEvent.setColor(11);
        Logger.log("New event created and marked as RESCHEDULED: " + newEvent.getId());

        var oldEvent = calendar.getEventById(data.googleEventId);

        if (oldEvent) {
            oldEvent.deleteEvent();
            Logger.log("Old event deleted: " + data.googleEventId);
        } else {
            Logger.log("Old event not found: " + data.googleEventId);
        }

        return {
            googleEventId: newEvent.getId(),
            message: "Event rescheduled successfully, and old event deleted."
        };

    } catch (error) {
        Logger.log("Error rescheduling event: " + error.message);
        return {
            status: "error",
            message: "Failed to reschedule event: " + error.message
        };
    }
}

function removeEvent(data) {
    Logger.log("Removing event with ID: " + data.googleEventId);
    var calendar = CalendarApp.getDefaultCalendar();

    try {
        var event = calendar.getEventById(data.googleEventId);
        if (event) {
            event.deleteEvent();
            Logger.log("Event removed: " + data.googleEventId);
        } else {
            throw new Error("Event not found.");
        }
    } catch (error) {
        Logger.log("Error removing event: " + error.message);
        throw new Error("Failed to remove event: " + error.message);
    }
}
