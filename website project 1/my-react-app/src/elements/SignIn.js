import './SignIn.css';
import { Link } from 'react-router-dom';
function SignIn() {
    return(
        <div className = 'loginform'>
            <b>Username</b>
            <input type="text" placeholder="Enter Username" name="uname" required/>

            <b>Password</b>
            <input type="password" placeholder="Enter Password" name="psw" required/>
        
            <br />
            <Link className = 'button'
             to='/SecondPage'
            >
                Sign In
            </Link>
            <Link className = 'button'
             to='/'
            >
                Cancel
            </Link>
        </div>
    )
}
export default SignIn;
