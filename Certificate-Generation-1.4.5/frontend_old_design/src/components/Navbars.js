import { Link } from "react-router-dom";
import { useState } from "react"; 

const Navbars = () =>{
    const [isLoggedIn, setIsLoggedIn] = useState(false); 

   
    const handleLogout = () => {
        setIsLoggedIn(false);
    }

    return (
        <>
            <nav className="navbar navbar-expand-lg fixed-top navbar-bg">
                <a className="navbar-brand">
                    <img className="blinking-image" src="../assets/images/logo.png" alt="Logo" height="50" />
                </a>
                <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav mr-auto">
                        <li className="nav-item active">
                            <a className="nav-link"><Link to={'/dashboard'}>Home</Link> <span className="sr-only">(current)</span></a>
                        </li>
                    </ul>
                    {/* {isLoggedIn && 
                        <ul className="navbar-nav">
                            <li className="nav-item active">
                                <a className="nav-link" onClick={handleLogout}>Logout <span className="sr-only">(current)</span></a>
                            </li>
                        </ul>
                    } */}
                </div>
            </nav>
        </>
    );
}
export default Navbars;
