import { Col, Form, Row } from "react-bootstrap";

const PushNotification = () =>{

    return(
        <>
            <div>
                <h1>Push Notification</h1>
                <Form>
                    <Row className="mb-3">
                        <Form.Group as={Col} controlId="formGridEmail">
                            <Form.Label> Email</Form.Label>
                            <Form.Control type="text" name="email" id="email" />

                        </Form.Group>
                    </Row>
                </Form>
            </div>
        </>
    );
};

export default PushNotification;