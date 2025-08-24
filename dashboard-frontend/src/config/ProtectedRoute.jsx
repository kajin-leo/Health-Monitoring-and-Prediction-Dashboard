import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children, requiredRole }) => {
    const token = localStorage.getItem('token');
    if (!token) {
        return <Navigate to='/login' replace />;
    }

    if (requiredRole && !requiredRole.includes(localStorage.getItem('role'))){
        return <Navigate to='/unauthorized' />;
    }

    return children;
}

export default ProtectedRoute;
