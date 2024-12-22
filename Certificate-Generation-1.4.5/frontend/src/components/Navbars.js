import { useEffect, useState } from "react"; 
import { useDispatch, useSelector } from "react-redux";
import { fetchLoggedInUserDetails } from "./redux/userSlice";
import { Link, redirect, useNavigate } from "react-router-dom";
import "../components/myapp.css";


const Navbars = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false); 
    const [isMenuCollapsed, setIsMenuCollapsed] = useState(false);
    const navigate = useNavigate();

    const dispatch = useDispatch();
    const { fullName, gender } = useSelector((state) => state.user);

    // Redux
    useEffect(() => {
        const userId = localStorage.getItem('userId');
        if (userId) {
            dispatch(fetchLoggedInUserDetails(userId));
        }
    }, [dispatch]);

    const handleLogout = () => {
      localStorage.removeItem('role');
      localStorage.removeItem('token');
      localStorage.removeItem('userId');
      setIsLoggedIn(false);  
      navigate('/');
    }

    const toggleMenu = () => {
        setIsMenuCollapsed(!isMenuCollapsed); 
    }

    return (
        <>
            <header id="page-topbar">
                <div className="navbar-header">
                    <div className="d-flex">
                        <div className="navbar-brand-box">
                            <a href="/dashboard" className="logo logo-dark">
                                <span className="logo-sm">
                                    <img src="assets/images/logo.png" alt="" height="22" />
                                </span>
                                <span className="logo-lg">
                                    <img src="assets/images/logo.png" alt="" height="17" />
                                </span>
                            </a>
                            <a href="/dashboard" className="logo logo-light">
                                <span className="logo-sm">
                                    <img src="assets/images/logo.png" alt="" height="22" />
                                </span>
                                <span className="logo-lg">
                                    <img src="assets/images/logo.png" alt="" height="19" />
                                </span>
                            </a>
                        </div>

                        <button
                            type="button"
                            className="btn btn-sm px-3 font-size-16 header-item waves-effect"
                            id="vertical-menu-btn"
                            onClick={toggleMenu} 
                        >
                            <i className={`fa fa-fw fa-bars ${isMenuCollapsed ? 'collapsed' : ''}`}></i>
                        </button>
                    </div>

                    <div className="d-flex">
                        <div className="dropdown d-inline-block">
                            <button
                                type="button"
                                className="btn header-item noti-icon waves-effect"
                                id="page-header-notifications-dropdown"
                                data-bs-toggle="dropdown"
                                aria-haspopup="true"
                                aria-expanded="false"
                            >
                                <i className="bx bx-bell bx-tada"></i>
                                <span className="badge bg-danger rounded-pill">3</span>
                            </button>
                            <div
                                className="dropdown-menu dropdown-menu-lg dropdown-menu-end p-0"
                                aria-labelledby="page-header-notifications-dropdown"
                            >
                                <div className="p-3">
                                    <div className="row align-items-center">
                                        <div className="col">
                                            <h6 className="m-0" key="t-notifications">Notifications</h6>
                                        </div>
                                        <div className="col-auto">
                                            <a href="#!" className="small" key="t-view-all">
                                                View All
                                            </a>
                                        </div>
                                    </div>
                                </div>

                                <div data-simplebar style={{ maxHeight: '230px' }}>
                                    <a href="javascript:void(0);" className="text-reset notification-item">
                                        <div className="d-flex">
                                            <div className="avatar-xs me-3">
                                                <span className="avatar-title bg-primary rounded-circle font-size-16">
                                                    <i className="bx bx-cart"></i>
                                                </span>
                                            </div>
                                            <div className="flex-grow-1">
                                                <h6 className="mb-1" key="t-your-order">
                                                    Your order is placed
                                                </h6>
                                                <div className="font-size-12 text-muted">
                                                    <p className="mb-1" key="t-grammer">
                                                        If several languages coalesce the grammar
                                                    </p>
                                                    <p className="mb-0">
                                                        <i className="mdi mdi-clock-outline"></i>
                                                        <span key="t-min-ago">3 min ago</span>
                                                    </p>
                                                </div>
                                            </div>
                                        </div>
                                    </a>
                                </div>

                                <div className="p-2 border-top d-grid">
                                    <a
                                        className="btn btn-sm btn-link font-size-14 text-center"
                                        href="javascript:void(0)"
                                    >
                                        <i className="mdi mdi-arrow-right-circle me-1"></i>
                                        <span key="t-view-more">View More..</span>
                                    </a>
                                </div>
                            </div>
                        </div>

                        {!isLoggedIn && (
                            <div className="dropdown d-inline-block">
                                <button
                                    type="button"
                                    className="btn header-item waves-effect"
                                    id="page-header-user-dropdown"
                                    data-bs-toggle="dropdown"
                                    aria-haspopup="true"
                                    aria-expanded="false"
                                >
                                    <img
                                        className="rounded-circle header-profile-user"
                                        src={
                                            gender === 'MALE' ? 'assets/images/man.png' :
                                            gender === 'FEMALE' ? 'assets/images/women.png' :
                                            'assets/images/avatar-1.png'
                                        }
                                        alt="Header Avatar"
                                    />
                                    <span className="d-none d-xl-inline-block ms-1" key="t-henry">
                                        {fullName}
                                    </span>
                                    <i className="mdi mdi-chevron-down d-none d-xl-inline-block"></i>
                                </button>
                                <div className="dropdown-menu dropdown-menu-end">
                                    <a className="dropdown-item" href="javascript:void(0);">
                                        <i className="bx bx-user font-size-16 align-middle me-1"></i>
                                        <Link to="/adminProfile"><span key="t-profile">Profile</span></Link>
                                    </a>
                                    <a className="dropdown-item d-block" href="#">
                                        <span className="badge bg-success float-end">11</span>
                                        <i className="bx bx-wrench font-size-16 align-middle me-1"></i>
                                        <span key="t-settings">Settings</span>
                                    </a>
                                    <div className="dropdown-divider"></div>
                                    <button className="dropdown-item text-danger" onClick={handleLogout}>
                                        <i className="bx bx-power-off font-size-16 align-middle me-1 text-danger"></i>
                                        <span key="t-logout">Logout</span>
                                    </button>
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            </header>
        </>
    );
}

export default Navbars;