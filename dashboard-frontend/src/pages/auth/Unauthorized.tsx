import { Button } from "@heroui/react";
import { useNavigate } from "react-router-dom";
import Logo from '../../assets/豹豹就这样哭.svg';

const Unauthorized = () => {
    const navigate = useNavigate();

    const logout = () => {
        localStorage.clear();
        navigate('/login');
    }

    return (
        <div className="flex flex-col gap-8 items-center">
            <img src={Logo} className="max-w-80" />
            <div className="flex flex-col items-center gap-2">
                <h1 className="text-6xl font-bold bg-gradient-to-br from-slate-400 to-slate-500 bg-clip-text text-transparent">
                    Unauthorised
                </h1>
                <h2 className="text-xl text-slate-600">
                    Sorry! You are not authorised for this page. What about going back? 
                </h2>
            </div>
            <div className="bg-gradient-to-b from-gray-50/80 to-white/70 p-3 rounded-full flex gap-10 items-center justify-between mx-auto shadow-xl backdrop-blur-lg backdrop-saturate-200">
                <Button color="primary" className="bg-teal-800/40" radius="full">Go Back</Button>
                <Button color="danger" className="bg-red-600" onPress={logout} radius="full">Logout</Button>
            </div>
        </div>

    );
}

export default Unauthorized;