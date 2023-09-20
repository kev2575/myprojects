//I know we had a signin file already but I'm just 
//following along on the notes

import React, { useState, useContext } from "react";
import { Form, Button, Card, Alert, Container } from "react-bootstrap";
import axios from "axios";
import UserContext from "../context/UserContext";
import { useNavigate, Link } from "react-router-dom";

function Login() {
    const [email, setEmail] = useState();
    const [password, setPassword] = useState();
    const [error, setError] = useState();
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const { setUserData } = useContext(UserContext);

    async function handleSubmit(e) {
        e.preventDefault();
        setLoading(true);
        try {
            const loginUser = { email, password };
            const loginRes = await axios.post("http://localhost:8082/api/users/login", loginUser);
            setUserData({
                token: loginRes.data.token,
                user: loginRes.data.user,
            });

            localStorage.setItem("auth-token", loginRes.data.token);
            navigate('/SecondPage');
        } catch (err) {
            setLoading(false);
            err.response.data.msg && setError(err.response.data.msg);
        }
    }

    return (
        <Container
            className="d-flex align-items-center justify-content-center"
        >
            <div className="w-100">
                <>
                    <Card>
                        <div className = 'Menu'>
                            <Link to='/' className="menu-button">
                                Back
                            </Link>
                        </div>
                        <Card.Body className="login-page">
                            <h2 className="text-center mb-4">Log In</h2>
                            {error && <Alert variant="danger">{error}</Alert>}
                            <Form onSubmit={handleSubmit}>
                                <Form.Group id="email">
                                    <b>Email</b>
                                    <br />
                                    <Form.Control type="email" required onChange={e => setEmail(e.target.value)}/>
                                </Form.Group>
                                <Form.Group id="password">
                                    <b>Password</b>
                                    <br />
                                    <Form.Control type="password" required onChange={e => setPassword(e.target.value)}/>
                                </Form.Group>
                                <Button disabled={loading} className="w-100 mt-2" type="submit">
                                    Log In
                                </Button>
                            </Form>
                            <div className="w-100 text-center mt-2">Need an account?<Link to="/Signup">Sign up</Link></div>
                        </Card.Body>
                    </Card>
                </>
            </div>
        </Container>
    );
}

export default Login;
