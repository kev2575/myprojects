import { createContext } from "react";
export default createContext(null);
/*await axios.post("http://localhost:8082/api/users/signup", newUser);
const loginRes = await axios.post("http://localhost:8082/api/users/login", {
    email,
    password,
});
setUserData({
    token: loginRes.data.token,
    user: loginRes.data.user,
});
localStorage.setItem("auth-token", loginRes.data.token); */
