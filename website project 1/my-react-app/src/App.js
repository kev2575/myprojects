
import { BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import FirstPage from './elements/FirstPage';
//import SignIn from './elements/SignIn';
import SecondPage from './elements/SecondPage';
import EditPage from './elements/EditPage';
import CreateItem from './elements/CreateItem';
import ItemDetails from './elements/ItemDetails';
import { useEffect } from 'react';
import UserContext from './context/UserContext';
import Signup from './elements/Signup';
import Login from './elements/Login';
import ErrorPage from './elements/ErrorPage';
import axios from 'axios';
import {useState} from 'react';

function App() {
   const [userData, setUserData] = useState({
    token: undefined,
    user: undefined,
  });
  useEffect(() => {
    const checkLoggedIn = async () => {
      let token = localStorage.getItem("auth-token");
      if (token === null) {
        localStorage.setItem("auth-token", "");
        token = "";
      }
      const tokenResponse = await axios.post(
        "http://localhost:8082/api/users/tokenIsValid",
        null,
        { headers: { "x-auth-token": token } }
      );
      if (tokenResponse.data) {
        const userRes = await axios.get("http://localhost:8082/api/users/", {
          headers: { "x-auth-token": token },
        });
        setUserData({
          token,
          user: userRes.data,
        });
      }
    };
    checkLoggedIn();
  }, []);

 return(
  <UserContext.Provider value={{ userData, setUserData }}>
  <Router>
      <div>
        <Routes>
          <Route exact path='/' element={<FirstPage />} />
          <Route path='/SecondPage' element={<SecondPage />} />
          <Route path='/Login' element={<Login />} />
          <Route path='/Signup' element={<Signup />} />
          <Route path='/CreateItem' element={<CreateItem />} />
          <Route path='/ItemDetails/:id' element={<ItemDetails />}/>
          <Route path='/EditPage/:id' element={<EditPage />}/>
          <Route path='*' element={<ErrorPage />} />
        </Routes>
      </div>
    </Router>
    </UserContext.Provider>
 );
}

export default App;
