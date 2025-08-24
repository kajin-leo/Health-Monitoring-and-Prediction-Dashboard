import { Button } from "@heroui/react";
import { useNavigate } from "react-router-dom";

const Unauthorized = () => {
    const navigate = useNavigate();

    const logout = () => {
        localStorage.clear();
        navigate('/login');
    }

    return (
        <div className="flex flex-col gap-2">
            <h1>Unauthorised!!</h1>
            <Button onPress={logout}>Logout</Button>
        </div>

    );
}

export default Unauthorized;